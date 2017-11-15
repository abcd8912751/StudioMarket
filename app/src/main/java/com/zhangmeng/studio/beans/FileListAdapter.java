package com.zhangmeng.studio.beans;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

import com.zhangmeng.studio.R;
import com.zhangmeng.studio.utils.FTPClientFactory;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import static com.zhangmeng.studio.utils.LogUtil.showLog;

/**
 * Created by zhangmeng on 2016/12/2.
 */
public class FileListAdapter extends BaseAdapter {

    private Context context;
    private FTPFile[] list_path;
    private FTPClient ftpClient;
    private int position;
    private String currentPath;
    private Timer timer;
    public FileListAdapter(Context con,FTPClient client)
    {
        this.context=con;
        this.ftpClient=client;
        this.position=0;
        this.currentPath="";
        try {
            list_path=ftpClient.listFiles();
        } catch (IOException e) {
            e.printStackTrace();
        }

        timer= new Timer();
        timer.schedule(new NoopTask(),30*1000,30*1000);   //每20s发送一次noop命令以保持长连接
    }

    @Override
    public int getCount() {
        return list_path.length;
    }

    @Override
    public FTPFile getItem(int position) {
        if(position<getCount())
            return list_path[position];
        else
            return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView==null)
        {
            convertView= LayoutInflater.from(context).inflate(R.layout.dialog_child,null);
        }
        CheckedTextView textView= (CheckedTextView)convertView;
        if(getItem(position)!=null)
            textView.setText(getItem(position).getName());
        return convertView;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(final int position) {
        this.position = position;
        showLog("选择"+position);
        if(ftpClient!=null)
        {
            Observable.create(new Observable.OnSubscribe<String>() {
                @Override
                public void call(Subscriber<? super String> subscriber) {
                    try {
                        String path=getItem(position).getName();
                        list_path=ftpClient.listFiles(path);
                        subscriber.onNext(path);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>() {
                @Override
                public void call(String o) {
                    updateCurrentPath(o);
                    showLog(getCurrentPath());
                    //作更新等操作
                    notifyDataSetChanged();
                }
            });

        }
    }


    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void refresh()
    {
        final String workdirectory="/";
        if(TextUtils.isEmpty(getCurrentPath())||getCurrentPath().equals(workdirectory))
            return;
        setCurrentPath("");
        try {
            if(ftpClient==null||!ftpClient.isConnected())   //当ftpClient失效时重新生成
            {
                ftpClient = new FTPClientFactory().makeObject().getObject();
                timer= new Timer();
                timer.schedule(new NoopTask(),30*1000,30*1000);
            }
            ftpClient.changeWorkingDirectory(workdirectory);
            list_path=ftpClient.listFiles();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getCurrentPath() {
        return currentPath;
    }

    public void setCurrentPath(String currentPath) {
        this.currentPath = currentPath;
    }
    private String getPath(String name)
    {
        return "/"+name;
    }

    public void updateCurrentPath(String pathname)
    {
        setCurrentPath(this.currentPath+getPath(pathname));
    }


    public void disconnect()
    {
        if(timer!=null)
        {
            showLog("ListAdapter开始释放FTPClient");
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        if(ftpClient==null)
                            return;
                        ftpClient.logout();
                        ftpClient.disconnect();
                        timer.cancel();
                        timer=null;
                    } catch (IOException e) {
                        e.printStackTrace();
                        showLog("ListAdapter释放FTPClient出错");
                    }
                    finally {
                        ftpClient=null;
                        showLog("ListAdapter释放FTPClient完成");
                    }
                }
            },0);
        }
    }

    private class NoopTask extends TimerTask{

        @Override
        public void run() {
            if (ftpClient!=null)
                try {
                    showLog("ListAdapter中的FTPClient心跳答复:"+ftpClient.noop());
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
}
