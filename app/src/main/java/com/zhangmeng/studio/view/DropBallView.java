package com.zhangmeng.studio.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.zhangmeng.studio.R;

import static android.content.Context.SENSOR_SERVICE;
import static com.zhangmeng.studio.utils.LogUtil.showLog;

/**
 * Created by zhangmeng on 2017/11/26.
 */

public class DropBallView extends SurfaceView implements SurfaceHolder.Callback,Runnable ,SensorEventListener {
    public static final int TIME_IN_FRAME = 200;

    /** 游戏画笔 **/
    Paint mPaint;
    Paint mTextPaint;
    SurfaceHolder mSurfaceHolder;
    Canvas mCanvas;
    boolean mIsRunning = false;

    private SensorManager mSensorMgr;
    Sensor mSensor;

    int mScreenWidth;
    int mScreenHeight;

    /**小球资源文件越界区域**/
    private int mScreenBallWidth = 0;
    private int mScreenBallHeight = 0;

    private Bitmap mbitmapBall;
    private float mPosX = 200;
    private float mPosY = 0;

    /**重力感应X轴 Y轴 Z轴的重力值**/
    private float mGX = 0;
    private float mGY = 0;
    public DropBallView(Context context) {
        this(context,null);
    }

    public DropBallView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DropBallView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        try{
            this.setFocusable(true);
            /** 设置当前View拥有触摸事件 **/
            this.setFocusableInTouchMode(true);
            /** 拿到SurfaceHolder对象 **/
            mSurfaceHolder = this.getHolder();
            /** 将mSurfaceHolder添加到Callback回调函数中 **/
            mSurfaceHolder.addCallback(this);
            /*透明背景             */
            this.setZOrderOnTop(true);
            mSurfaceHolder.setFormat(PixelFormat.TRANSLUCENT);

            /** 创建画布 **/
            mCanvas = new Canvas();
            /** 创建曲线画笔 **/
            mPaint = new Paint();
            mPaint.setColor(getResources().getColor(R.color.holo_purple));
            /**加载小球资源**/
            mbitmapBall = BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_ball);
            /**得到SensorManager对象**/
            mSensorMgr = (SensorManager) context.getSystemService(SENSOR_SERVICE);
            mSensor = mSensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

            mSensorMgr.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_GAME);
            showLog("开始创造");
        }
        catch(Exception e)
        {
            showLog("就是我搞的鬼");
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        mGX=sensorEvent.values[0];
        mGY=sensorEvent.values[1];
        mPosX -= mGX * 2;
        mPosY += mGY * 2;

        //检测小球是否超出边界
        if (mPosX < 0) {
            mPosX = 0;
        }
        else if (mPosX > mScreenBallWidth) {
            mPosX = mScreenBallWidth;
        }
        if (mPosY < 0) {
            mPosY = 0;
        }
        else if (mPosY > mScreenBallHeight) {
            mPosY = mScreenBallHeight;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mIsRunning = true;
        new Thread(this).start();
        /**得到当前屏幕宽高**/
        mScreenWidth = this.getWidth();
        mScreenHeight = this.getHeight();

        /**得到小球越界区域**/
        mScreenBallWidth = mScreenWidth - mbitmapBall.getWidth();
        mScreenBallHeight = mScreenHeight - mbitmapBall.getHeight();

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        mIsRunning = false;
    }

    @Override
    public void run() {
        while (mIsRunning) {

            try {
                long startTime = System.currentTimeMillis();
                synchronized (mSurfaceHolder) {
                    mCanvas = mSurfaceHolder.lockCanvas();
                    draw();
                    mSurfaceHolder.unlockCanvasAndPost(mCanvas);
                }
                long endTime = System.currentTimeMillis();
                int diffTime = (int) (endTime - startTime);
                while (diffTime <= TIME_IN_FRAME) {
                    diffTime = (int) (System.currentTimeMillis() - startTime);
                    Thread.yield();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void clearCanvas() {
        Canvas canvas = null;
        try{

            canvas = mSurfaceHolder.lockCanvas(null);
            canvas.drawColor(Color.WHITE);
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.SRC);

        }catch(Exception e){


        }finally{

            if(canvas != null){

                mSurfaceHolder.unlockCanvasAndPost(canvas);

            }
        }
    }

    private void draw() {
        try {
//            mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            mPaint.setColor(Color.WHITE);
            mCanvas.drawRect(0, 0, mScreenWidth, mScreenHeight, mPaint);

            mCanvas.drawBitmap(mbitmapBall, mPosX,mPosY, mPaint);

            /**X轴 Y轴 Z轴的重力值**/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
