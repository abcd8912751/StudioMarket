package com.zhangmeng.studio.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.zhangmeng.studio.R;
import com.zhangmeng.studio.view.WaveView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.zhangmeng.studio.utils.LogUtil.showLog;

/**
 * Created by zhangmeng on 2017/11/14.
 */

public class SplashActivity extends AppCompatActivity {
    @BindView(R.id.waveView)
    WaveView waveView;
    public Handler handler;
    public final int CIRCLE_MESSAGE=101;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==CIRCLE_MESSAGE)
                {
                    if(waveView.isOutofScreen())
                    {
                        Intent intent=new Intent(SplashActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                        handler.sendEmptyMessageDelayed(CIRCLE_MESSAGE,1200);
                }
            }
        };
        handler.sendEmptyMessage(CIRCLE_MESSAGE);

    }
}
