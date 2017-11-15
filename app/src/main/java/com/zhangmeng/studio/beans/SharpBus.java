package com.zhangmeng.studio.beans;

import android.support.annotation.NonNull;
import rx.Observable;
import rx.Subscriber;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 基于RxAndroid、RxJAVA而作的事件处理类
 */

public class SharpBus {
    private static SharpBus sharpBus;   //静态也是唯一的实例
    private ConcurrentHashMap<String, Subject> obseorvableMap;
    public static SharpBus getInstance()
    {
        if(sharpBus==null)
            sharpBus=new SharpBus();
        return sharpBus;
    }

    /**
     * 私有化构造方法以锁定全局只有一个实例
     */
    private SharpBus()
    {
        obseorvableMap=new ConcurrentHashMap<>();
    }

    /**
     * 注册观察者并放入相应Map
     *
     */
    public synchronized <T> Observable<T> register(@NonNull String tag) {
        Subject<T, T> subject = PublishSubject.create();
        obseorvableMap.put(tag,subject);
        return subject;
    }

    /**
     * 解注册
     * @param tag
     */
    public synchronized void unregister(String tag)
    {
        obseorvableMap.remove(tag);
    }

    /**
     * 发送事件
     * @param tag
     * @param event
     */
    public synchronized void post(@NonNull String tag,@NonNull Object event)
    {
        Subject subject=obseorvableMap.get(tag);
        synchronized (this)
        {
            if(subject!=null)
                subject.onNext(event);
        }
    }
}
