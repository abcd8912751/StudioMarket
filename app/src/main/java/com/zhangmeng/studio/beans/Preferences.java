package com.zhangmeng.studio.beans;

import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by hzxuwen on 2015/4/13.
 */
public class Preferences {
    private static final String KEY_USER = "user";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_IP="ftpip";
    private static final String KEY_REMOTE_PATH="remotepath";
    private static final String KEY_THREAD_NUM="threadnum";
    private static Context context;

    /**
     * 设置静态Context
     * @param contex
     */
    public static void setContext(Context contex)
    {
        context=contex;
    }

    public static String getUser() {
        return getString(KEY_USER);
    }

    public static String getPassword() {
        return getString(KEY_PASSWORD);
    }

    /**
     *保存当前测试次数
     */
    public static void saveUser(String user)
    {
        saveString(KEY_USER,user);
    }
    /**
     *保存需测试次数
     */
    public static void savePassword(String pass)
    {
        saveString(KEY_PASSWORD,pass);
    }
    public static void saveIP(String ftpip) {
        saveString(KEY_IP, ftpip);
    }

    public static String getIP() {
        return getString(KEY_IP);
    }

    public static void saveRemotePath(String path) {
        saveString(KEY_REMOTE_PATH, path);
    }

    public static String getRemotePath() {
        return getString(KEY_REMOTE_PATH);
    }

    public static void saveThreadNum(String thread_num) {
        saveString(KEY_THREAD_NUM, thread_num);
    }

    public static String getThreadNum() {
        return getString(KEY_THREAD_NUM);
    }


    private static void saveString(String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(key, value);
        editor.commit();
    }

    private static String getString(String key) {
        return getSharedPreferences().getString(key, null);
    }

    static SharedPreferences getSharedPreferences() {
        if(context==null)
            return null;
        return context.getSharedPreferences("FTP_CONFIG", Context.MODE_PRIVATE);
    }

    public static void clear() {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.clear();
        editor.commit();
    }
}
