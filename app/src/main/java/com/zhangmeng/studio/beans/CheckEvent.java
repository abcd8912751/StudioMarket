package com.zhangmeng.studio.beans;

import org.apache.commons.net.ftp.FTPClient;

/**
 * Created by zhangmeng on 2016/9/11.
 */
public class CheckEvent {
    private int number;//默认三线程即1,2,3
    private boolean isFinished;//是否完成
    private String msg;//传递信息
    private boolean isFailed;
    private boolean isStop;

    public CheckEvent(boolean isFinished,int num,boolean isStop)
    {
        this.number=num;
        this.isStop=isStop;
        setFinished(isFinished);
    }
    public CheckEvent(boolean isFailed,boolean isFinished,int num)
    {
        this(isFinished,num,false);
        setFailed(isFailed);
    }
    public int getThreadNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isFailed() {
        return isFailed;
    }

    public void setFailed(boolean isFailed) {
        this.isFailed = isFailed;
    }

    @Override
    public String toString() {
        String string=number+"线程 ";
        if(isFailed())
            string=string+"下载出错";
        if(isStop())
            string=string+"下载中止";
        else if(isFinished())
            string=string+"下载完成";
        return string;
    }

    public boolean isStop() {
        return isStop;
    }

    public void setStop(boolean stop) {
        isStop = stop;
    }
}
