package com.zhangmeng.studio.view;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by zhangmeng on 2017/11/4.
 */

public  abstract class BaseFragment extends android.support.v4.app.Fragment {
    public Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getActivity();
    }

}
