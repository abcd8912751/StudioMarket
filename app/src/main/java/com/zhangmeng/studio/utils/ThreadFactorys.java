package com.zhangmeng.studio.utils;

import android.support.annotation.NonNull;
import org.apache.commons.net.io.DotTerminatedMessageWriter;

import java.util.concurrent.ThreadFactory;

/**
 * Created by zhangmeng on 2016/12/8.
 */
public class ThreadFactorys implements ThreadFactory{

    @Override
    public Thread newThread(@NonNull Runnable r) {
        Thread t=new Thread(r);
        t.setName("我是救场的");
        return t;
    }
}
