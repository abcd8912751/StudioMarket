package com.zhangmeng.studio.contract;

/**
 * Created by zhangmeng on 2017/11/14.
 */

public interface ZoomContract {

    interface ZoomView {
        public void setImageIndex(String index);
    }

    interface Presenter {
        public void setImageIndex(String index);
    }
}
