package com.zhangmeng.studio.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import com.zhangmeng.studio.R;

import java.util.List;

import static com.zhangmeng.studio.utils.LogUtil.showLog;

/**
 * Created by zhangmeng on 2017/11/14.
 */

public class WaveView extends View{
    private Paint paint;
    private int alpha;
    private int slotTime;
    private float radiusAddend;
    private float radiusCurrent;
    private Runnable runnable;
    private float centerX;
    private float centerY;
    private float roundWidth;
    public WaveView(Context context) {
        this(context,null);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.WaveView);
        paint = new Paint();
        int color= typedArray.getColor(R.styleable.WaveView_circleColor,getResources().getColor(R.color.colorBacktint));
        slotTime = typedArray.getInteger(R.styleable.WaveView_timeSlotms, 300);
        radiusAddend = typedArray.getFloat(R.styleable.WaveView_radiusAddend, 10);
        paint.setColor(color);
        radiusCurrent=typedArray.getDimension(R.styleable.WaveView_centerCircleRadius, 200);
        roundWidth=typedArray.getDimension(R.styleable.WaveView_roundWidth, 2);
        typedArray.recycle();
        paint.setStyle(Paint.Style.STROKE); //设置空心
        paint.setStrokeWidth(roundWidth); //设置圆环的宽度
        paint.setAntiAlias(true);  //消除锯齿
        alpha=255;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public WaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        initCenterXY();
        paint.setStrokeWidth(6); //设置圆环的宽度
        canvas.drawCircle(centerX,centerY,200,paint);
        drawCustomCircle(canvas);
        super.onDraw(canvas);
    }

    private void initCenterXY() {
        if(getCenterX()<1)
        {
            setCenterX(getWidth()/2);
            setCenterY(getHeight()/2);
        }
    }

    private void drawCustomCircle(Canvas canvas) {
        if(radiusCurrent>=getCenterX()-10)
        {
            radiusCurrent=200;
            alpha=255;
        }
        paint.setAlpha(alpha);
        paint.setStrokeWidth(roundWidth); //设置圆环的宽度
        canvas.drawCircle(centerX,centerY,radiusCurrent,paint);
        addRadiusAndAlpha();
        postInvalidateDelayed(slotTime);
    }
    public boolean isOutofScreen()
    {
        showLog("alpha:"+alpha);
        if(alpha<100)
            return true;
        if(centerX<1||radiusCurrent<centerX-20)
            return false;
        else
            return true;
    }
    private void addRadiusAndAlpha()
    {
        radiusCurrent=radiusCurrent+radiusAddend;
        alpha=alpha-15;
    }

    public float getCenterY() {
        return centerY;
    }

    public void setCenterY(int centerY) {
        this.centerY = centerY;
    }

    public float getCenterX() {
        return centerX;
    }

    public void setCenterX(int centerX) {
        this.centerX = centerX;
    }
}
