package zjy.com.zhoukaotwo.model;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;
import zjy.com.zhoukaotwo.bean.MyBean;

/**
 * Created by ZhangJiaYu on 2017/11/11.
 */

public interface ApiService {

    public static final String URL = "http://mnews.gw.com.cn/";

    @GET("wap/data/news/txs/page_{page}.json")
    Observable<List<MyBean>> getbean(@Path("page") int page);
}
