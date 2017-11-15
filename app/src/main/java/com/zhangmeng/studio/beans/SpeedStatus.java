package com.zhangmeng.studio.beans;

/**
 * Created by zhangmeng on 2016/12/3.
 */
public class SpeedStatus {
    private long progress;

    public long getProgress() {
        return progress;
    }

    public void setProgress(long progress) {
        this.progress = progress;
    }

    public long getTmp() {
        return tmp;
    }

    public void setTmp(long tmp) {
        this.tmp = tmp;
    }

    public long getSpeed() {
        return speed;
    }

    public void setSpeed(long speed) {
        this.speed = speed;
    }

    private long tmp;//剩余需下载大小
    private long speed;//每s下载的大小

    public SpeedStatus(long progress, long tmp, long sizeps) {
        this.progress = progress;
        this.tmp = tmp;
        this.speed = sizeps;
    }
    public void updateStatus(long progress, long tmp, long sizeps){
        this.progress = progress;
        this.tmp = tmp;
        this.speed = sizeps;
    }
    public String getRemainTime()
    {
        if(speed ==0)
            return null;
        long minutes= tmp/ speed /60;
        long seconds=tmp/ speed %60;

        if(seconds==0)
        {
            if(minutes==0)
                return "0秒";
            return minutes+"分";
        }
        else
        {
            if(minutes==0)
                return seconds+"秒";
            else
                return minutes+"分"+seconds+"秒";
        }


    }
}
