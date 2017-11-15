package com.zhangmeng.studio.utils;

import android.text.TextUtils;
import org.apache.commons.net.ftp.FTPClient;

import java.io.*;
import java.nio.ByteBuffer;

import static com.zhangmeng.studio.utils.LogUtil.showLog;
import static com.zhangmeng.studio.utils.DownloadUtils.isStop;

/**
 * 使用FTPClient下载FTP服务器文件
 */
public class DownloadRunnable implements Runnable {
    private FTPClient ftpClient;
    private int threadno;//当前线程编号
    private int threadnum;//线程总数
    private String remotepath;//远端文件路径
    private ByteBuffer bytebuffer;//内存映射缓冲区
    private long currentPos;    //写入文件的相应位置
    private int tempPosition;//临时位置
    public DownloadRunnable(FTPClient client, int threadno, int threadnum) {
        this.ftpClient = client;
        this.threadno = threadno;
        this.threadnum = threadnum;
        if (!TextUtils.isEmpty(DownloadUtils.getRemotePath()))
            this.remotepath = DownloadUtils.getRemotePath();
        bytebuffer=ByteBuffer.allocate(512*1024);//申请512KB容量的内存
        DownloadUtils.makeFile(remotepath);
    }



    @Override
    public void run() {
        try {
            remotepath = "/ltedown/ftp1g";
            showLog(threadno + "线程 开始下载");
            //在这里计算文件的起始值
            long require_length, start = 0, localsize = 0;//文件起始值,需下载的长度,已下载的长度
            long totalFileSize = DownloadUtils.getTotalFileSize();//远端文件的总长度
            require_length = totalFileSize / threadnum;
            start = (threadno - 1) * require_length;
            if (totalFileSize % threadnum != 0)
            {
                if (threadno == threadnum)
                    require_length = require_length + totalFileSize % threadnum;
            }
            ftpClient.setRestartOffset(start);
            InputStream input = ftpClient.retrieveFileStream(remotepath);

            if (input == null) {
                showLog(threadno + "线程无法获取InputStream");
            }
            else
                showLog("到这了");
            long tmp;
            int length = 0;
            byte[] b = new byte[1024];
            setCurrentPos(start);   //开始写入前设置起始位置为start
            showLog(threadno + "线程"+start);
            while ((length = input.read(b)) != -1) {//read方法在未获取到有效资源前会一直阻塞
                if (isStop())
                    break;
                if(length==0)
                    showLog(threadno + "线程获取byte数为0");
                if(bytebuffer.capacity()-bytebuffer.position()<1024*threadno)
                {
                    flush();    //当缓冲区未处理数据达到Max时flush数据
                    showLog(threadno + "线程flush完成");
                }
                tmp = localsize + length;
                if (tmp >= require_length)
                {
                    length = (int) (require_length - localsize);
                    bytebuffer.put(b, 0, length);
                    DownloadUtils.addFileSize(length);
                    localsize = localsize + length;
                    break;
                }
                bytebuffer.put(b, 0, length);
                DownloadUtils.addFileSize(length);
                localsize = localsize + length;
            }
            input.close();
            if (!isStop())
                showLog(threadno + "线程" + " 下载完成");
        } catch (Exception e) {
            showLog(threadno + "线程 捕捉到下载异常");
            e.printStackTrace();
            DownloadUtils.ftpRelatedStatus.setDownloadError(true);
            e.printStackTrace();
        } finally {
            try {
                //测试完成在这里作 传出信号的操作且无论下载过程有无异常均上报下载结束
                DownloadUtils.addFinish();
                unmap();    //释放资源,释放前会flush数据
                ftpClient.completePendingCommand();
                ftpClient.logout();
                ftpClient.disconnect();
                showLog(threadno + "线程 结束战斗");
            }
            catch (IOException e) {
                showLog(threadno + "线程 FTPClient释放异常");
                e.printStackTrace();
            }
        }
    }

    private  void flush()
    {
        if(bytebuffer!=null)
        {
            tempPosition=bytebuffer.position();
            bytebuffer.flip();
            DownloadUtils.write(bytebuffer,getCurrentPos());
            bytebuffer.compact();
            bytebuffer.clear();
            addCurrentPos(tempPosition);
            showLog(threadno+"线程__Curposition:"+getCurrentPos());
        }
    }


    /**
     * 显式回收MappedByteBuffer实例
     */
    private void unmap() {
        flush();        //刷入数据
        if(bytebuffer==null)
            return;
        bytebuffer.clear();
        bytebuffer=null;
    }

    public void setRemotepath(String remotepath) {
        this.remotepath = remotepath;
    }


    public long getCurrentPos() {
        return currentPos;
    }

    public void setCurrentPos(long currentPos) {
        this.currentPos = currentPos;
    }

    public void addCurrentPos(long variable)
    {
        setCurrentPos(this.currentPos + variable);
    }

}
