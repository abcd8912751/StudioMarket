package com.zhangmeng.studio.news;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.zhangmeng.studio.beans.NewsPaperJson.ResultBean.*;
import com.zhangmeng.studio.ui.WebSurfActivity;

import static com.zhangmeng.studio.utils.LogUtil.showLog;

/**
 * Created by zhangmeng on 2017/11/10.
 */

public class ItemViewOnclickListener implements View.OnClickListener
{


    private String newsUrl;
    public ItemViewOnclickListener(@NonNull String url)
    {
        this.newsUrl=url;
    }
    @Override
    public void onClick(View v) {
        if(TextUtils.isEmpty(newsUrl))
            return;
        Context context=v.getContext();
        Intent intent=new Intent(context, WebSurfActivity.class);
        intent.setData(getNewsUri());
        context.startActivity(intent);
    }

    public Uri getNewsUri() {
        return Uri.parse(newsUrl);
    }
}
