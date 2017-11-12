package zjy.com.zhoukaotwo.presenter;

import java.util.List;

import zjy.com.zhoukaotwo.bean.MyBean;
import zjy.com.zhoukaotwo.model.Model;
import zjy.com.zhoukaotwo.view.IView;

/**
 * Created by ZhangJiaYu on 2017/11/11.
 */

public class IPresenter implements Model.OnFinish{
    Model model;
    IView iView;

    public IPresenter(IView iView) {
        this.iView = iView;
        model = new Model();
        model.setOnFinish(this);
    }

    public void geturl(String url,int page){
        model.getUrl(url,page);
    }

    @Override
    public void OnFinishLitenter(List<MyBean.DataBean> list) {
        iView.getData(list) ;
    }
}
