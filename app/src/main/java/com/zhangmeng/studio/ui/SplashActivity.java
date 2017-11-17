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
}
