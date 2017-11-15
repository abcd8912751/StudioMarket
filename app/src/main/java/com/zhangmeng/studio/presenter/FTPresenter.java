package com.zhangmeng.studio.presenter;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.zhangmeng.studio.beans.FTPClientSet;
import com.zhangmeng.studio.beans.FileListAdapter;
import com.zhangmeng.studio.beans.SharpBus;
import com.zhangmeng.studio.beans.SpeedStatus;
import com.zhangmeng.studio.contract.DownloadContract.*;
import com.zhangmeng.studio.utils.DownloadRunnable;
import com.zhangmeng.studio.utils.DownloadUtils;
import com.zhangmeng.studio.utils.FTPClientFactory;
import com.zhangmeng.studio.utils.FTPClientPool;
import com.zhangmeng.studio.view.BehaviorView;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

import static com.zhangmeng.studio.utils.LogUtil.showLog;
import static com.zhangmeng.studio.utils.DownloadUtils.context;
import static com.zhangmeng.studio.utils.DownloadUtils.isStop;

/**
 * Created by zhangmeng on 2017/11/4.
 */

public class FTPresenter implements Presenter {
    private FTPClientPool pool;
    private Timer speedTimer;   //监测下载速度的定时器,每秒运行一次计算速度.
    private BehaviorView behaviorView;  //BottonSheet弹出View的集体
    private SpeedStatus speedStatus;
    private ExecutorService executorService;//固定数目线程池对象
    private int thread_num;
    private String localpath;
    private FileListAdapter listAdapter;
    private AlertDialog.Builder builder;
    private DownloadView downloadView;
    public FTPresenter(DownloadView downloadView,BehaviorView behaviorView) {
            localpath= DownloadUtils.getLocalpath();
            File file=new File(localpath);
            file.mkdirs();
            this.downloadView=downloadView;
            this.behaviorView=behaviorView;
        executorService= Executors.newFixedThreadPool(DownloadUtils.getThreadnum());
            init();
    }

        /**
         * 开启子线程进行下载
         */
        public void startDownload() {
            thread_num= DownloadUtils.getThreadnum();
            DownloadUtils.init();   //初始化状态
            deleteLocal();  //删除之前下载的文件
            DownloadUtils.ftpRelatedStatus.setDownloading(true);    //设置当前正在下载
            if(listAdapter!=null)
                listAdapter.disconnect();
            if(speedTimer==null)
            {
                speedTimer=new Timer();
                speedTimer.schedule(new SpeedMonitorTask(),0,1000);
            }
            Observable.create(new Observable.OnSubscribe<FTPClientSet>() {
                //被观察者 进行连接FTP服务器、生成连接池等操作
                @Override
                public void call(Subscriber<? super FTPClientSet> subscriber) {
                    try {
                        if (pool != null)
                            pool.clear();
                        FTPClientFactory factory = new FTPClientFactory();
                        pool = new FTPClientPool(thread_num,factory);
                    } catch (Exception e) {
                        showLog("连接池生成错误");
                        e.printStackTrace();
                    }
                    showLog("开始下载");
                    for(int i=1;i<=thread_num;i++)
                        subscriber.onNext(new FTPClientSet(i, pool.borrowObject()));
                }
            })
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(new Subscriber<FTPClientSet>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(FTPClientSet ftpClientSet) {
                            //使用线程池
                            if(executorService!=null)
                            {
                                executorService.submit(new DownloadRunnable(ftpClientSet.getFtpClient(), ftpClientSet.getThread_no(), thread_num));
                            }
                        }
                    });
        }
        /**
         ** 删除下载目录里文件以免浪费资源
         */
        public void deleteLocal(){
            File file[] = new File(localpath).listFiles();
            try {
                for (File f : file) {
                    if (!f.isDirectory())
                        f.delete();
                }
            }catch(Exception e)
            {
            }
            showLog("删除文件完成");
        }

    /**
     * 初始化操作
     */
    private void init() {

        DownloadUtils.setTotalFileSize(1073741824);     //默认设置1G文件的大小
        showLog("生成订阅者");

        builder=new AlertDialog.Builder(context);
        Subscriber<FTPClient> subscriber=new Subscriber<FTPClient>() {
            @Override
            public void onCompleted() {
            }
            @Override
            public void onError(Throwable throwable) {
            }
            @Override
            public void onNext(final FTPClient s) {
                listAdapter=new FileListAdapter(context, s);
                builder.setSingleChoiceItems(listAdapter, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FTPFile ftpFile=listAdapter.getItem(which);
                        if(ftpFile==null)
                            return;
                        if(ftpFile.isDirectory())
                            listAdapter.setPosition(which);
                        else
                        {
                            dialog.cancel();
                            listAdapter.updateCurrentPath(ftpFile.getName());
                            DownloadUtils.setTotalFileSize(ftpFile.getSize());
                            DownloadUtils.setRemotePath(listAdapter.getCurrentPath());
                            showLog("totalSize:"+ftpFile.getSize());
                            downloadView.setFilePath(listAdapter.getCurrentPath());
                        }
                    }
                });
            }
        };
        Observable.create(new Observable.OnSubscribe<FTPClient>() {
            @Override
            public void call(Subscriber<? super FTPClient> subscriber) {
                FTPClient ftpClient= null;
                try {
                    ftpClient = new FTPClientFactory().makeObject().getObject();
                } catch (Exception e) {
                    subscriber.onError(e.getCause());
                }
                subscriber.onNext(ftpClient);
            }
        }).subscribeOn(Schedulers.newThread()).subscribe(subscriber);
        builder.setTitle("选择远程文件");

        downloadView.setFilePathListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLog("点击text");
                Observable.create(new Observable.OnSubscribe<Object>() {
                    @Override
                    public void call(Subscriber<? super Object> subscriber) {
                        if(listAdapter==null)
                        {
                            showLog("未生成可用的listAdapter");
                            return;
                        }
                        listAdapter.refresh();
                        subscriber.onNext("更新");
                    }
                })
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Object>() {
                            @Override
                            public void call(Object o) {
                                listAdapter.notifyDataSetChanged();
                                builder.create().show();
                            }
                        });
            }
        });
        Observable<String> observable= SharpBus.getInstance().register("download_finish");
        observable.subscribe(new Action1<String>() {
            @Override
            public void call(String o) {
                if(o.contains("true"))
                {
                    if(!isStop())   //循环下载
                        startDownload();

                }
            }
        });
    }
    public class SpeedMonitorTask extends TimerTask
    {
        private long allFilelength=0;
        private int noSpeedCount=0;

        @Override
        public void run() {
            if(!downloadView.getFloatingTag())
                return;
            Observable.create(new Observable.OnSubscribe<SpeedStatus>() {
                @Override
                public void call(Subscriber<? super SpeedStatus> subscriber) {
                    if(allFilelength==0)
                        allFilelength= DownloadUtils.getTotalFileSize();
                    long currentlength= DownloadUtils.getCurrentSize();
                    if(allFilelength==0)
                    {
                        showLog("总大小为0");
                        return;
                    }
                    long tmp=allFilelength-currentlength;
                    long speed;
                    if(currentlength==0)
                        speed=0;
                    else
                    {
                        speed = currentlength - DownloadUtils.getLastLength();
                    }
                    DownloadUtils.setLastLength(currentlength);
                    if(speed==0)
                    {
                        noSpeedCount++;
                        if(noSpeedCount>200)    //大约3分钟
                        {
                            showLog("持续3分钟无速率,继续下载");
                            if(!isStop())
                                startDownload();
                        }
                    }
                    else
                        noSpeedCount=0;
                    if(behaviorView.isDisplay())
                    {
                        if(speedStatus==null)
                            speedStatus=new SpeedStatus(currentlength*100/allFilelength,tmp,speed);
                        else
                            speedStatus.updateStatus(currentlength*100/allFilelength,tmp,speed);
                        subscriber.onNext(speedStatus);
                    }
                }
            })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<SpeedStatus>() {
                        @Override
                        public void call(SpeedStatus tmp) {
                            //进行UI更新
                            if(behaviorView!=null)
                            {
                                showLog("isDisplay："+behaviorView.isDisplay());
                                if(behaviorView.isDisplay())
                                {
                                    behaviorView.setProgress(tmp.getProgress());
                                    behaviorView.setSpeed(tmp.getSpeed());
                                    behaviorView.setTime(tmp.getRemainTime());
                                }
                                else
                                    showLog("当前底部栏没有弹出");
                            }
                        }
                    });
        }
    }
}



