package com.zhangmeng.studio.contract;

import android.view.View;

/**
 * Created by zhangmeng on 2017/11/4.
 */

public interface DownloadContract {

    interface DownloadView {
        public boolean getFloatingTag();
        public void setFilePath(String path);
        public void setFilePathListener(View.OnClickListener listener);
    }

    interface Presenter {

    }
}
