package com.zhangmeng.studio.utils;

import android.content.Context;
import android.content.res.Resources;

import com.zhangmeng.studio.R;

/**
 * Created by zhangmeng on 2017/11/10.
 */

public class ADFilterTool {
    public static boolean hasAd(Context context, String url){
        Resources res= context.getResources();
        String[]adUrls =res.getStringArray(R.array.adBlockUrl);
        for(String adUrl :adUrls)
        {
            if(url.contains(adUrl)){
                return true;
            }
        }
        return false;
    }
    public static boolean isWhiteUrl(String url)
    {
        if(url.contains("eastday.com"))
            return true;
        else
            return false;
    }
}
