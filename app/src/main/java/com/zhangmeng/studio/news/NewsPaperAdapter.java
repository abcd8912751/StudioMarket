package com.zhangmeng.studio.news;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.zhangmeng.studio.R;
import com.zhangmeng.studio.contract.NewsContract;
import com.zhangmeng.studio.model.NewsModel;

import java.util.ArrayList;
import java.util.List;

import static com.zhangmeng.studio.utils.LogUtil.showLog;



public class NewsPaperAdapter extends PagerAdapter implements NewsContract.Presenter{
    private NewsContract.NewsView news_View;
    private NewsModel newsModel;
    public NewsPaperAdapter(NewsContract.NewsView newsView)
    {
        this.news_View = newsView;
        Context context=newsView.getContext();
        newsModel =new NewsModel(context);
        Fresco.initialize(context);
        notifyDataSetChanged();
    }



    @Override
    public int getCount() {
        return newsModel.getCount();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view=newsModel.getViewPage(position);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(newsModel.getViewPage(position));//删除页卡
    }


}
