package com.zhangmeng.studio.beans;

/**
 * FTP相关操作的状态,连接态和下载状态等
 */
public class FTPRelatedStatus {

    private boolean isDownloading;  //正在下载
    private boolean isConnectError; //连接错误
    private boolean isConnected;    //连接成功
    private boolean isConnecting;   //正在连接
    private boolean isDownloadError;//下载错误
    private int     tag;            //状态标记

    public final static int TAG_DISCONNET_YET   =100;   //尚未连接FTP或初始状态
    public final static int TAG_USER_ERROR      =101;   //用户名或密码错误时的标记
    public final static int TAG_HOST_ERROR      =102;   //无法连接提供的FTP IP
    public final static int TAG_UNKNOWN_ERROR   =103;   //连接过程中的未知错误
    public final static int TAG_DOWNLOAD_ERROR  =104;   //下载过程中出错

    /**
     * 构造器
     */
    public FTPRelatedStatus()
    {
        initAll();
    }

    public void initAll()
    {
        setConnected(false);
        setConnectError(false);
        setDownloading(false);
        setDownloadError(false);
        setConnecting(false);
        setTag(TAG_DISCONNET_YET);
    }

    public String getStatusDetail()
    {
        //默认返回  "尚未连接至FTP服务器"
        if (isConnected())
            return "连接并登录至FTP服务器";
        switch(this.tag)
        {
            case TAG_USER_ERROR:
                return "用户名或密码错误";
            case TAG_HOST_ERROR:
                return "无法连接FTP服务器,需校验IP地址";
            case TAG_DOWNLOAD_ERROR:
                if(isConnected())
                    return "下载过程中出错,请重试";
                else
                    return "FTP连接中断,请重试";
            case TAG_UNKNOWN_ERROR:
                return "连接FTP服务器出现未知错误";
        }
        return "尚未连接至FTP服务器";
    }



    public boolean isDownloadError() {
        return isDownloadError;
    }

    public void setDownloadError(boolean downloadError) {
        isDownloadError = downloadError;
        if(downloadError)
            setTag(TAG_DOWNLOAD_ERROR);
    }

    public boolean isDownloading() {
        return isDownloading;
    }

    public void setDownloading(boolean downloading) {
        isDownloading = downloading;
        isConnected=true;
    }

    public boolean isConnectError() {
        return isConnectError;
    }

    public FTPRelatedStatus setConnectError(boolean connectError) {
        isConnectError = connectError;
        setConnecting(false);
        return this;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
        isConnecting = false;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }


    public boolean isConnecting() {
        return isConnecting;
    }

    public void setConnecting(boolean connecting) {
        isConnecting = connecting;
        isConnected=false;
    }

}
