package com.zhangmeng.studio.view;

import android.content.Context;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.TextView;
import com.daimajia.numberprogressbar.NumberProgressBar;

import java.util.TimerTask;

import static com.zhangmeng.studio.utils.LogUtil.showLog;


/**
 * Created by zhangmeng on 2016/12/3.
 */
public class BehaviorView {
    private TextView speedText;             //主界面弹出BottomSheet中的用于显示速度的TextView
    private TextView timeText;              //主界面弹出BottomSheet中的用于显示剩余时间的TextView
    private NumberProgressBar progressBar;  //主界面弹出BottomSheet中的进度条
    private BottomSheetBehavior behavior;   //主界面BottomSheet行为
    private TextInputLayout ipInputLayout;       //主界面FTP主机ip输入视图
    private TextInputLayout username_input; //主界面用户名输入视图
    private TextInputLayout password_input; //主界面密码输入视图
    private EditText text_filepath;

    /**
     * 定义BottomSheet中的View
     * @param numberProgressBar
     * @param speed
     * @param time
     */
    public BehaviorView(NumberProgressBar numberProgressBar,TextView speed,TextView time )
    {
        this.progressBar=numberProgressBar;
        this.speedText=speed;
        this.timeText=time;
    }

    /**
     * 定义主布局的TextInputLayout
     * @param ip
     * @param username
     * @param password
     */
    public void setInputLayout(TextInputLayout ip,TextInputLayout username,TextInputLayout password)
    {
        this.ipInputLayout=ip;
        this.username_input=username;
        this.password_input=password;
    }
    /**
     * 获取用户名输入框中的文本
     * @return
     */
    public String getUserName()
    {
        if(getUsername_input()==null)
            return "username:null";
        else
            return username_input.getEditText().getText().toString();
    }

    /**
     * 获取密码输入框中的文本
     * @return
     */
    public String getPassword()
    {
        if(getPassword_input()==null)
            return "password:null";
        return password_input.getEditText().getText().toString();
    }

    /**
     * 获取IP输入框中的文本
     * @return
     */
    public String getIp()
    {
        if(getIp_input()==null)
            return "ip:null";
        else
            return ipInputLayout.getEditText().getText().toString();
    }

    public void setSpeed(long speed)
    {
        if(speedText!=null)
            speedText.setText("下载速度: "+speed/1024+" kbps");
    }

    public TextInputLayout getIp_input() {
        return ipInputLayout;
    }



    public TextInputLayout getUsername_input() {
        return username_input;
    }

    public void setUsername_input(TextInputLayout username_input) {
        this.username_input = username_input;
    }

    public TextInputLayout getPassword_input() {
        return password_input;
    }

    public void setPassword_input(TextInputLayout password_input) {
        this.password_input = password_input;
    }




    public void setTime(String time)
    {
        if(timeText!=null)
        {
            if(TextUtils.isEmpty(time))
                time=" 正在计算";
            timeText.setText("剩余时间:  "+time);
        }
    }

    public void setProgress(long progress)
    {
        if(progressBar!=null)
            progressBar.setProgress((int)progress);
    }

    public TextView getSpeedText() {
        return speedText;
    }

    public void setSpeedText(TextView speedText) {
        this.speedText = speedText;
    }

    public TextView getTimeText() {
        return timeText;
    }

    public void setTimeText(TextView timeText) {
        this.timeText = timeText;
    }

    public NumberProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(NumberProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public BottomSheetBehavior getBehavior() {
        return behavior;
    }

    public void setBehavior(BottomSheetBehavior behavior) {
        this.behavior = behavior;
    }

    /**
     * 判断BottomSheet是否弹出
     */
    public boolean isDisplay()
    {
        if(behavior!=null)
        {
            if(behavior.getState()== BottomSheetBehavior.STATE_EXPANDED)
                return true;
        }
        return false;
    }

    /**
     * 显示BottomSheet
     */
    public void show()
    {
        if(this.behavior==null)
            return;
        if(behavior.getState()!=BottomSheetBehavior.STATE_EXPANDED)
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        //进行一系列使各种输入框不可用的操作
        ipInputLayout.getEditText().setEnabled(false);
        username_input.getEditText().setEnabled(false);
        password_input.getEditText().setEnabled(false);

    }

    /**
     * 隐藏BottomSheet
     */
    public void hide()
    {
        if(behavior==null)
            return;
        if(behavior.getState()!=BottomSheetBehavior.STATE_COLLAPSED)
        {
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            showLog("已收腹");
        }
        if(behavior.getState()!=BottomSheetBehavior.STATE_HIDDEN)
            behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        //进行一系列使各种输入框可用的操作
        ipInputLayout.getEditText().setEnabled(true);
        username_input.getEditText().setEnabled(true);
        password_input.getEditText().setEnabled(true);
    }

    /**
     * 将ip_textInputLaout中的文本以正则表达式校验并反馈
     * @return
     */
    public boolean checkIp()
    {
        String text_ip=ipInputLayout.getEditText().getText().toString();
        if(!TextUtils.isEmpty(text_ip))
        {
            String rexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
            showLog(text_ip);
            if(!text_ip.matches(rexp))
            {
                ipInputLayout.setError("IP地址格式错误");
                return false;
            }
            else
                return true;
        }
        else
            showLog("没有捕捉到文本");
        return false;
    }

    public void setText_filepath(EditText text_filepath) {
        this.text_filepath = text_filepath;
    }
}

