package zjy.com.zhoukaotwo.view;

import java.util.List;

import zjy.com.zhoukaotwo.bean.MyBean;

/**
 * Created by ZhangJiaYu on 2017/11/11.
 */

public interface IView {
    void getData(List<MyBean.DataBean> list);
}
