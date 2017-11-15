package com.zhangmeng.studio.utils;

import android.content.Context;
import android.widget.Toast;

import com.zhangmeng.studio.application.StudioApplication;

/**
 * Created by zhangmeng on 2017/11/9.
 */

public class ToastUtils {
    private static Toast toast;
    public static Context context;

    public static void showToast(String message)
    {
        if(context==null)
            context= StudioApplication.getInstance().getContext();
        if(toast==null)
            toast=Toast.makeText(context,message,Toast.LENGTH_SHORT);
        else
            toast.setText(message);
        toast.show();
    }
}
