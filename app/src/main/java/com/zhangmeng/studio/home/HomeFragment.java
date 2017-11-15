package com.zhangmeng.studio.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.zhangmeng.studio.R;
import com.zhangmeng.studio.beans.*;
import com.zhangmeng.studio.contract.DownloadContract;
import com.zhangmeng.studio.presenter.FTPresenter;
import com.zhangmeng.studio.utils.*;
import com.zhangmeng.studio.view.BaseFragment;
import com.zhangmeng.studio.view.BehaviorView;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.util.Timer;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.zhangmeng.studio.utils.DownloadUtils.isStop;
import static com.zhangmeng.studio.utils.LogUtil.showLog;

/**
 * Created by zhangmeng on 2017/11/4.
 */

public class HomeFragment extends BaseFragment implements DownloadContract.DownloadView{
    @BindView(R.id.floatingButton)
    FloatingActionButton floatingButton;    //Tag为false时为开始下载,反之为停止下载
    @BindView(R.id.number_progress_bar)
    NumberProgressBar progressBar;  //下载进度条
    @BindView(R.id.thread_seekbar)
    DiscreteSeekBar discreteSeekBar;
    @BindView(R.id.text_filepath)
    EditText text_filepath;
    @BindView((R.id.text_threadnum))
    TextView text_threanNum;
    @BindView(R.id.textSpeed)
    TextView speed;
    @BindView(R.id.textRemainder)
    TextView time;
    private FTPClientPool pool;
    private Timer speedTimer;   //监测下载速度的定时器,每秒运行一次计算速度.
    private BehaviorView behaviorView;  //BottonSheet弹出View的集体
    private FTPresenter ftp;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home,container,false);
        ButterKnife.bind(this,view);

        floatingButton.setOnClickListener(new DownloadListener());
        floatingButton.setTag("false");
        View viewsheet=view.findViewById(R.id.nestedscroll);
        BottomSheetBehavior bottomSheetBehavior=BottomSheetBehavior.from(viewsheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        TextInputLayout username=(TextInputLayout)view.findViewById(R.id.edit_username);
        TextInputLayout password=(TextInputLayout)view.findViewById(R.id.edit_password);
        TextInputLayout ip=(TextInputLayout) view.findViewById(R.id.ip_inputLayout);

        behaviorView=new BehaviorView(progressBar,speed,time);
        behaviorView.setBehavior(bottomSheetBehavior);
        FTPClientConfig config=new FTPClientConfig();
        ip.getEditText().setText(config.getHost());
        username.getEditText().setText(config.getUsername());
        password.getEditText().setText(config.getPassword());
        behaviorView.setInputLayout(ip,username,password);
        text_filepath.setText(config.getRemotepath());
        DownloadUtils.setRemotePath(config.getRemotepath());
        DownloadUtils.setContext(context);
        DownloadUtils.setTotalFileSize(1073741824);//默认配置


        behaviorView.setText_filepath(text_filepath);
        text_filepath.setKeyListener(null);
        text_threanNum.setText("当前线程个数:"+discreteSeekBar.getProgress());
        discreteSeekBar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                text_threanNum.setText("当前线程个数:"+value);
                DownloadUtils.setThreadnum(value);
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

            }
        });
        Preferences.setContext(context);

        ftp=new FTPresenter(this,behaviorView);
        return view;
    }








    @Override
    public boolean getFloatingTag() {
        if(floatingButton==null||!floatingButton.getTag().equals("true"))
            return false;
        return true;
    }

    @Override
    public void setFilePath(String path) {
        text_filepath.setText(path);
    }

    @Override
    public void setFilePathListener(View.OnClickListener listener) {
        text_filepath.setOnClickListener(listener);
        showLog("设置监听");
    }

    /**
     *控制下载的FloatingActionButton的 状态更改监听类
     */
    private class DownloadListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {if(v.getTag().equals("true"))
        {
            stopDownload();
        }
        else
        {
            v.setTag("true");
            if(behaviorView.checkIp())
                Snackbar.make(v,"开始下载",Snackbar.LENGTH_SHORT).show();
            floatingButton.setImageResource(R.mipmap.ic_stop);
            DownloadUtils.setIsStop(false);
            ftp.startDownload();
        }
            //BottomSheet相应操作
            if(v.getTag().equals("true"))
                behaviorView.show();
            else
                behaviorView.hide();
        }
    }




    /**
     * 停止下载的系列操作
     */
    public void stopDownload()
    {
        DownloadUtils.init();
        DownloadUtils.setIsStop(true);
        floatingButton.setTag("false");
        floatingButton.setImageResource(R.mipmap.ic_download);
        if(behaviorView!=null)
            behaviorView.hide();
    }
}
