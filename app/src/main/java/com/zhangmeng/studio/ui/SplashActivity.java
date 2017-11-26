package com.zhangmeng.studio.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.zhangmeng.studio.R;
import com.zhangmeng.studio.view.WaveView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.zhangmeng.studio.utils.LogUtil.showLog;

/**
 * Created by zhangmeng on 2017/11/14.
 */

public class SplashActivity extends AppCompatActivity {
    @BindView(R.id.waveView)
    WaveView waveView;
    @BindView(R.id.btn_roller)
    Button btn_roller;
    @BindView(R.id.btn_dropBall)
    Button btn_dropBall;
    public boolean isJump=false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        waveView.setOnLoadFinishListener(new WaveView.LoadFinishListener() {
            @Override
            public void OnLoadingFinish() {
                if(isJump)
                    return;
                isJump=true;
                Intent intent=new Intent(SplashActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });



    }
    @OnClick({R.id.btn_roller,R.id.btn_dropBall})
    public void onClick(View v)
    {
        Intent intent;
        if(v.getId()==R.id.btn_roller)
            intent=new Intent(this,RollerActivity.class);
        else
            intent=new Intent(this,DropBallActivity.class);
        startActivity(intent);
    }
}
