package com.zhangmeng.studio.utils;


import com.zhangmeng.studio.beans.FTPClientConfig;
import com.zhangmeng.studio.beans.FTPRelatedStatus;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import static com.zhangmeng.studio.utils.LogUtil.showLog;

/**
 * Created by zhangmeng on 2016/9/11.
 */
public class FTPClientFactory implements PooledObjectFactory<FTPClient> {
    private FTPClientConfig config;

    public FTPClientFactory() {
        this.config = new FTPClientConfig();
    }

    public DefaultPooledObject<FTPClient> makeObject() throws Exception {
        FTPClient ftpClient = new FTPClient();
        ftpClient.setConnectTimeout(config.getClientTimeout());
        try {
            DownloadUtils.ftpRelatedStatus.setConnecting(true);
            ftpClient.connect(config.getHost(), config.getPort());
            showLog("尝试连接服务器");
            int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                showLog("连接服务器失败");
                DownloadUtils.ftpRelatedStatus.setConnectError(true).setTag(FTPRelatedStatus.TAG_HOST_ERROR);

                return null;
            }
            boolean result = ftpClient.login(config.getUsername(), config.getPassword());
            if (!result) {
                showLog("ftpClient login failed");
                DownloadUtils.ftpRelatedStatus.setConnectError(true).setTag(FTPRelatedStatus.TAG_USER_ERROR);

                return null;
            }
            showLog("成功登录服务器");
            ftpClient.setKeepAlive(true);
            ftpClient.setControlKeepAliveTimeout(180);//保持连接3分钟
            ftpClient.setFileType(config.getTransferFileType());
            ftpClient.setBufferSize(1024);
            ftpClient.setControlEncoding(config.getEncoding());
            if (config.getPassiveMode().equals("true")) {
                ftpClient.enterLocalPassiveMode();
            }
            DownloadUtils.ftpRelatedStatus.setConnected(true);
        }
        catch (Exception e) {
            showLog("create ftp connection failed");
            DownloadUtils.ftpRelatedStatus.setConnectError(true).setTag(FTPRelatedStatus.TAG_UNKNOWN_ERROR);
//            DownloadUtils.showToast();
            e.printStackTrace();
        }
        DefaultPooledObject<FTPClient> pooledObj=new DefaultPooledObject<>(ftpClient);
        return pooledObj;
    }

    @Override
    public void destroyObject(PooledObject<FTPClient> pooledObject) throws Exception {
        FTPClient ftpClient=pooledObject.getObject();
        try {
            if(ftpClient != null && ftpClient.isConnected()) {
                ftpClient.logout();
            }
        } catch (Exception e) {
            showLog("logout failed");
            throw e;
        } finally {
            if(ftpClient != null) {
                ftpClient.disconnect();
            }
        }
    }

    @Override
    public boolean validateObject(PooledObject<FTPClient> pooledObject) {
        FTPClient ftpClient=pooledObject.getObject();
        try {
            return ftpClient.sendNoOp();
        } catch (Exception e) {
            showLog("failed to validate");
        }
        return false;
    }

    @Override
    public void activateObject(PooledObject<FTPClient> pooledObject) throws Exception {
    }

    @Override
    public void passivateObject(PooledObject<FTPClient> pooledObject) throws Exception {
    }


    public void destroyObject(FTPClient ftpClient) throws Exception {
        try {
            if(ftpClient != null && ftpClient.isConnected()) {
                ftpClient.logout();
            }
        } catch (Exception e) {
            showLog("ftpclient.logout failed");
            throw e;
        } finally {
            if(ftpClient != null) {
                ftpClient.disconnect();
            }
        }
    }


    public boolean validateObject(FTPClient ftpClient) {
        try {
            return ftpClient.sendNoOp();
        } catch (Exception e) {
        }
        return false;
    }


    public void activateObject(FTPClient obj) throws Exception {
        //Do nothing

    }


    public void passivateObject(FTPClient obj) throws Exception {
        //Do nothing

    }
}
