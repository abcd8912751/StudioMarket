package com.zhangmeng.studio.ui;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.zhangmeng.studio.R;
import com.zhangmeng.studio.view.DropBallView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.zhangmeng.studio.utils.LogUtil.showLog;

/**
 * Created by zhangmeng on 2017/11/26.
 */

public class DropBallActivity extends AppCompatActivity{
    @BindView(R.id.dropBall)
    DropBallView dropBallView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dropball);
        ButterKnife.bind(this);
    }
}
