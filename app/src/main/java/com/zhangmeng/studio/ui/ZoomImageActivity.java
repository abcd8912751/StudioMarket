package com.zhangmeng.studio.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.zhangmeng.studio.R;
import com.zhangmeng.studio.contract.ZoomContract;
import com.zhangmeng.studio.utils.Constants;
import com.zhangmeng.studio.view.DoubleTapGestureListener;
import com.zhangmeng.studio.view.ZoomPagerAdapter;
import com.zhangmeng.studio.view.ZoomableDraweeView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhangmeng on 2017/11/13.
 */

public class ZoomImageActivity extends AppCompatActivity implements ZoomContract.ZoomView{
    @BindView(R.id.zoomViewPager)
    ViewPager viewPager;
    @BindView(R.id.text_imageIndex)
    TextView imageIndex;
    private ZoomPagerAdapter mAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoomimage);
        ButterKnife.bind(this);
        Intent intent=getIntent();
        if(intent==null)
            finish();
        int position=Integer.valueOf(intent.getAction());
        ArrayList<String> urls=intent.getStringArrayListExtra(Constants.ZOOM_EXTRA_NAME);
        mAdapter=new ZoomPagerAdapter(urls,this);
        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(position);
    }

    @Override
    public void setImageIndex(String index) {
        imageIndex.setText(index);
    }
}
