package com.zhangmeng.studio.contract;

import com.zhangmeng.studio.view.BaseFragment;

/**
 * Created by zhangmeng on 2017/11/4.
 */

public interface MainContract {

    interface MainView {

        public void updateDrawableTop();
    }

    interface Presenter {
        public BaseFragment getTransferFragment(int position);
        public BaseFragment getTempFragment();
        public void setTempInfos(int pos);
    }
}
