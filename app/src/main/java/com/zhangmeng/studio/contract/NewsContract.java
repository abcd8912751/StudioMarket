package com.zhangmeng.studio.contract;

import android.content.Context;
import android.view.View;

/**
 * Created by zhangmeng on 2017/11/4.
 */

public interface NewsContract {
    interface Model {
        public int getCount();
        public View getViewPage(int position);
    }

    interface NewsView {
        public int getTabCount();
        public int getSelectedTabPos();
        public Context getContext();
    }

    interface Presenter {
    }
}
