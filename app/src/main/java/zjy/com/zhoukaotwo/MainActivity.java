package zjy.com.zhoukaotwo;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import zjy.com.zhoukaotwo.adapter.MyAdapter;
import zjy.com.zhoukaotwo.bean.MyBean;
import zjy.com.zhoukaotwo.model.ApiService;
import zjy.com.zhoukaotwo.presenter.IPresenter;
import zjy.com.zhoukaotwo.view.IView;

public class MainActivity extends AppCompatActivity implements IView,ProgressResponseBody.ProgressListener{

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    private MyAdapter adapter;
    ProgressBar pb;
    public static final String TAG = "MainActivity";
    public static final String PACKAGE_URL = "http://gdown.baidu.com/data/wisegame/df65a597122796a4/weixin_821.apk";
    private long breakPoints;
    private ProgressDownloader downloader;
    private File file;
    private long totalBytes;
    private long contentLength;
    private LinearLayoutManager manager;
    ListDataSave save;
    int page =1;
    private IPresenter iPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerview);
        swipeRefreshLayout = findViewById(R.id.srl);
        pb = findViewById(R.id.pb);
        manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        iPresenter = new IPresenter(this);
        iPresenter.geturl(ApiService.URL,page);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page++;
                iPresenter.geturl(ApiService.URL,page);
                recyclerView.setAdapter(adapter);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    @Override
    public void getData(final List<MyBean.DataBean> list) {
        adapter = new MyAdapter(list,MainActivity.this);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int last = manager.findLastVisibleItemPosition();
                if(last == list.size()-1){
                    page++;
                    iPresenter.geturl(ApiService.URL,page);
                }
            }
        });
        adapter.setOnItemClickListener(new MyAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                pb.setVisibility(View.VISIBLE);
                breakPoints = 0L;
                file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"sample.apk");
                downloader = new ProgressDownloader(PACKAGE_URL,file, (ProgressResponseBody.ProgressListener) MainActivity.this);
                downloader.download(0L);
            }
        });
        adapter.setOnItemLongClickListener(new MyAdapter.OnRecyclerViewItemLongClickListener() {
            @Override
            public void onItemLongClickListener(View view, int position) {
                downloader.pause();
                Toast.makeText(MainActivity.this,"下载暂停",Toast.LENGTH_SHORT).show();
                breakPoints = totalBytes;
            }
        });
    }

    @Override
    public void onPreExecute(long contentLength) {
        if(this.contentLength == 0L){
            this.contentLength = contentLength;
            pb.setMax((int) (contentLength/1024));
        }
    }

    @Override
    public void update(long totalBytes, boolean done) {
        this.totalBytes = totalBytes+breakPoints;
        pb.setProgress((int) (totalBytes+breakPoints)/1024);
        if(done){
            Observable.empty()
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnCompleted(new Action0() {
                        @Override
                        public void call() {
                            Toast.makeText(MainActivity.this,"下载完成",Toast.LENGTH_SHORT).show();
                        }
                    }).subscribe();
        }
    }
}
