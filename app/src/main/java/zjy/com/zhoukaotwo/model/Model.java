package zjy.com.zhoukaotwo.model;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import zjy.com.zhoukaotwo.bean.MyBean;

/**
 * Created by ZhangJiaYu on 2017/11/11.
 */

public class Model implements IModel{
    OnFinish onfinish;

    public interface OnFinish{
        void OnFinishLitenter(List<MyBean.DataBean> list);
    }

    public void setOnFinish(OnFinish onFinish) {
        this.onfinish = onFinish;
    }

    @Override
    public void getUrl(String url,int page) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiService.URL).addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build();
        ApiService apiService = retrofit.create(ApiService.class);
        Observable<List<MyBean>> bean = apiService.getbean(page);
        bean.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<MyBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<MyBean> myBaen) {
                        List<MyBean.DataBean> data = myBaen.get(0).getData();
                        onfinish.OnFinishLitenter(data);
                    }
                });

    }
}
