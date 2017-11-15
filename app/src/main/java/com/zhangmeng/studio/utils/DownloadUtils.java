package com.zhangmeng.studio.utils;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;

import com.zhangmeng.studio.beans.FTPRelatedStatus;
import com.zhangmeng.studio.beans.SharpBus;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import javax.security.auth.Subject;

import rx.Observable;
import rx.subjects.PublishSubject;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static com.zhangmeng.studio.utils.LogUtil.showLog;

/**
 * Created by zhangmeng on 2016/9/11.
 */
public class DownloadUtils {
    public static File downloadfile;
    public static FileChannel fileChannel;
    public static DownloadUtils utils;
    public static long currentSize; //实时文件大小;
    public static Context context;  //上下文对象

    public static long lastLength=0;  //上次文件大小
    public static int finishnum;    //已完成下载的线程数

    public static int threadnum=3;    //线程数目
    public static boolean isStop;   //是否停止下载

    public static String remotePath;    //远端地址

    private static long totalFileSize;     //总大小

    public static FTPRelatedStatus ftpRelatedStatus;    //连接状态

    public static long getLastLength() {
        return lastLength;
    }


    public PublishSubject<Object> subject;
    public static void setLastLength(long lastLength) {
        DownloadUtils.lastLength = lastLength;
    }
    public static String getRemotePath() {
        return remotePath;
    }

    public static void setRemotePath(String remotePath) {
        DownloadUtils.remotePath = remotePath;
        downloadfile=null;
    }

    public static boolean isStop() {
        return isStop;
    }

    public static void setIsStop(boolean isStop) {
        DownloadUtils.isStop = isStop;
        if(ftpRelatedStatus==null)
            return;
        if(isStop)
        {
            ftpRelatedStatus.setDownloading(false);
        }
        else
            ftpRelatedStatus.setDownloading(true);
    }


    public static void setThreadnum(int threadnum) {
        DownloadUtils.threadnum = threadnum;
    }


    public static int getThreadnum() {
        return threadnum;
    }

    public static int getFinishnum() {
        return finishnum;
    }
    public static void setFinishnum(int finishnum) {
        DownloadUtils.finishnum = finishnum;
    }

    public static long getCurrentSize() {
        return currentSize;
    }

    public static synchronized void setCurrentSize(long currentsize) {
        DownloadUtils.currentSize = currentsize;
    }


    public static DownloadUtils getInstance()
    {
        if(utils==null)
            utils=new DownloadUtils();
        init();
        return utils;
    }






    /**
     * 依据远程文件路径获得文件名
     * @param remotepath
     * @return
     */
    public static String getFilename(String remotepath)
    {
        if(remotepath==null)
            remotepath=remotePath;
        return remotepath.substring(remotepath.lastIndexOf('/')+1);
    }


    public static Context getContext() {
        return context;
    }

    /**
     * 传入主界面的上下文对象并初始化 DownloadUtils
     * @param context
     */
    public static void setContext(Context context) {
        DownloadUtils.context = context;
        init();
    }


    public static long getTotalFileSize() {

        return totalFileSize;
    }

    public static void setTotalFileSize(long totalFileSize) {
        DownloadUtils.totalFileSize = totalFileSize;
    }


    /**
     * 获取存放下载文件的目录
     * @return
     */
    public static String getLocalpath()
    {
        return Environment.getExternalStorageDirectory()+"/ftptmp";
    }


    /**
     * 创建文件并获取通道
     * @param remotepath
     */
    public static synchronized void makeFile(String remotepath) {
        if(downloadfile==null) {
            new File(getLocalpath()).mkdirs();
            downloadfile = new File(getLocalpath(), getFilename(remotepath));
            try {
                if(!downloadfile.exists())
                    downloadfile.createNewFile();
                FileOutputStream fout=new FileOutputStream(downloadfile);
                fileChannel=fout.getChannel();
            } catch (IOException e) {
                showLog(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static synchronized void write(ByteBuffer b, long position)
    {
        if(fileChannel!=null)
        {
            try {
                fileChannel.write(b,position);
            } catch (IOException e) {
                showLog("fileChannel写入遇错");
                e.printStackTrace();
            }
        }
    }


    public static void addFileSize(int length)
    {
        setCurrentSize(currentSize+length);
    }

    public static  void addFinish()
    {
        setFinishnum(finishnum+1);
        //在这里验证下是否下载完成
        if(finishnum==threadnum)
        {
            SharpBus   sharpBus= SharpBus.getInstance();
            showLog(finishnum+"个线程全部下载完成");
            sharpBus.post("download_finish","true");

        }
    }

    /**
     * 初始化状态
     */
    public static void init()
    {
        if(ftpRelatedStatus==null)
            ftpRelatedStatus=new FTPRelatedStatus();
        else
            ftpRelatedStatus.initAll();
        setFinishnum(0);
        setCurrentSize(0);
        setLastLength(0);
        setIsStop(false);
    }
}
