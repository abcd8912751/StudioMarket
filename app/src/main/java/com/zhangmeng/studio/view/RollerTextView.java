package com.zhangmeng.studio.view;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.TranslateAnimation;

import com.zhangmeng.studio.R;
import com.zhangmeng.studio.beans.FloatPoint;

import java.util.ArrayList;
import java.util.List;

import static com.zhangmeng.studio.utils.LogUtil.showLog;

/**
 * Created by zhangmeng on 2017/11/25.
 */

public class RollerTextView extends View {
    private String rollerText;
    private FloatPoint point;
    private Paint paint;
    private List<Integer> colors;
    private boolean isPause;
    public RollerTextView(Context context) {
        this(context,null);
    }

    public RollerTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RollerTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        colors=new ArrayList<Integer>();
        colors.add(getResources().getColor(R.color.colorBacktint));
        colors.add(getResources().getColor(R.color.tabIndicatorColor));
        colors.add(getResources().getColor(R.color.tabSelectedTextColor));
        colors.add(getResources().getColor(R.color.colorBacktint));
        colors.add(getResources().getColor(R.color.imageIndexBorder));
        colors.add(getResources().getColor(R.color.holo_white));
        colors.add(getResources().getColor(R.color.holo_circle));
        colors.add(getResources().getColor(R.color.holo_purple));
        colors.add(getResources().getColor(R.color.colorAccentQuarter));
        this.rollerText="静静聆听,不要打断";
        paint=new Paint();
        paint.setTextSize(40);
        paint.setStrokeWidth(6);
        point=new FloatPoint();
        point.set(10,20);
        setPause(false);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RollerTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

    }


    public String getRollerText() {
        return rollerText;
    }

    public void setRollerText(String rollerText) {
        this.rollerText = rollerText;
        postInvalidate();
        setPause(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawRollerText(canvas);

    }

    private void drawRollerText(Canvas canvas) {
        paint.setColor(getRandomColor());
        canvas.rotate(45,point.getX(),point.getY());
        canvas.drawText(this.rollerText,point.getX(),point.getY(),paint);
        canvas.rotate(-45,point.getX(),point.getY());
        point.refreshXY();
        if(!isPause())
            postInvalidateDelayed(500);
    }

    private int getRandomColor()
    {
        int randomIndex= (int) (Math.random()*9);
        paint.setStrokeWidth(paint.getStrokeWidth()+randomIndex+5);
        return colors.get(randomIndex);
    }



    public boolean isPause() {
        return isPause;
    }

    public void setPause(boolean pause) {
        isPause = pause;
    }
}
