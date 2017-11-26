package com.zhangmeng.studio.beans;

import com.zhangmeng.studio.application.StudioApplication;

import static com.zhangmeng.studio.utils.LogUtil.showLog;

/**
 * Created by zhangmeng on 2017/11/25.
 */

public class FloatPoint {
    private float x;
    private float y;
    private int screenWidth;
    public FloatPoint()
    {
        initXY();
        screenWidth= StudioApplication.getScreenWidth();
    }
    public void set(float x,float y)
    {
        this.x=x;
        this.y=y;
    }
    public void refreshXY()
    {
        this.x=this.x+15;
//        showLog("this.x:"+this.x);
//        showLog("jixian:"+screenWidth*3/5);
        if(getX()>screenWidth*3/5)
        {
            initXY();
        }
        else
            this.y=this.y+45;
    }

    private void initXY() {
        this.x=45;
        this.y=45;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
