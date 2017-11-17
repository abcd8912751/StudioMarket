package com.zhangmeng.studio.application;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.zhangmeng.studio.utils.Constants;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;


/**
 * Created by zhangmeng on 2017/11/4.
 */

public class StudioApplication extends Application {
    public Context getContext() {
        return context;
    }

    private static Context context;

    public static StudioApplication getInstance()
    {
        return (StudioApplication)context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.context=getApplicationContext();
        initSomeFramework();
    }

    /**
     *初始化第三方框架
     */
    private void initSomeFramework() {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new LoggerInterceptor(Constants.TAG))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();
        OkHttpUtils.initClient(okHttpClient);
        Fresco.initialize(this);
    }

    public static int getScreenWidth()
    {

        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
