package com.zhangmeng.studio.beans;

/**
 * Created by zhangmeng on 2016/9/11.
 */
public class FTPClientConfig {
    private String host;

    private int port;

    private String username;

    private String password;

    private String passiveMode;

    private String encoding;

    private int clientTimeout;

    private int transferFileType;
    private boolean renameUploaded;
    private int retryTime;

    public String getThread_num() {
        return thread_num;
    }

    public void setThread_num(String thread_num) {
        this.thread_num = thread_num;
    }

    private String thread_num;

    public String getRemotepath() {
        return remotepath;
    }

    private  String remotepath;

    public FTPClientConfig()
    {
        setHost("122.229.28.110");
        setUsername("huawei");
        setPassword("hw76@#");
        setClientTimeout(60000);
        setPort(21);
        setEncoding("utf-8");
        setPassiveMode("true");
        setRemotepath("/ltedown/ftp1g"); //测试使用 /ltedown/ftp50m
        setTransferFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
        setThread_num("3");
    }


    public String toUri() {
        String maohao=":",at="@";
        return "ftp://"+username+maohao+password+at+host+maohao+port+remotepath;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassiveMode() {
        return passiveMode;
    }

    public void setPassiveMode(String passiveMode) {
        this.passiveMode = passiveMode;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public int getClientTimeout() {
        return clientTimeout;
    }

    public void setClientTimeout(int clientTimeout) {
        this.clientTimeout = clientTimeout;
    }


    public int getTransferFileType() {
        return transferFileType;
    }

    public void setTransferFileType(int transferFileType) {
        this.transferFileType = transferFileType;
    }

    public boolean isRenameUploaded() {
        return renameUploaded;
    }

    public void setRenameUploaded(boolean renameUploaded) {
        this.renameUploaded = renameUploaded;
    }

    public int getRetryTime() {
        return retryTime;
    }

    public void setRetryTime(int retryTime) {
        this.retryTime = retryTime;
    }

    public  void setRemotepath(String remotepath) {
        this.remotepath = remotepath;
    }

    public void savaPreference()
    {
        Preferences.saveIP(host);
        Preferences.saveUser(username);
        Preferences.savePassword(password);
        Preferences.saveRemotePath(remotepath);
        Preferences.saveThreadNum(thread_num);
    }
}
