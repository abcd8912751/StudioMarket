package com.zhangmeng.studio.beans;

import org.apache.commons.net.ftp.FTPClient;

/**
 * 自定义下载客户端与线程id的集合
 */
public class FTPClientSet {
    private int thread_no;
    private FTPClient ftpClient;
    public FTPClientSet()
    {
        
    }

    public FTPClientSet(int thread_no, FTPClient ftpClient) {
        this.thread_no = thread_no;
        this.ftpClient = ftpClient;
    }

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }

    public int getThread_no() {
        return thread_no;
    }

    public void setThread_no(int thread_no) {
        this.thread_no = thread_no;
    }
}
