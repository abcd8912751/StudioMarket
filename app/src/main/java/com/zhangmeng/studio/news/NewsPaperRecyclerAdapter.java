package com.zhangmeng.studio.news;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhangmeng.studio.R;
import com.zhangmeng.studio.application.StudioApplication;
import com.zhangmeng.studio.beans.NewsPaperJson;
import com.zhangmeng.studio.beans.NewsPaperJson.ResultBean.*;
import com.zhangmeng.studio.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import static com.zhangmeng.studio.utils.Constants.*;
import static com.zhangmeng.studio.utils.LogUtil.showLog;

/**
 * Created by zhangmeng on 2017/11/9.
 */

public class NewsPaperRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final static int ONE_IMAGE_VIEWTYPE=1;
    private final static int TWO_IMAGE_VIEWTYPE=2;
    private final static int THREE_IMAGE_VIEWTYPE=3;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<NewsBean> newsBeans;
    private String NEWS_TYPE;
    private ObjectMapper newsMapper;
    private LinearLayout.LayoutParams layoutParams;
    private int screenWidth;
    public NewsPaperRecyclerAdapter(String type)
    {
        this.NEWS_TYPE=type;
        requestNews();
        newsMapper=new ObjectMapper();
        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        screenWidth= StudioApplication.getScreenWidth();
    }


    private void requestNews()
    {
        OkHttpUtils
                .get()
                .url(getNewsUrlByType())
                .build()
                .execute(new StringCallback()
                {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if(TextUtils.isEmpty(response))
                        {
                            ToastUtils.showToast("需重新加载数据");
                            return;
                        }
                        try {
                            showLog(response);
                            NewsPaperJson newsPaperJson=newsMapper.readValue(response,NewsPaperJson.class);
                            addNewsUnit(newsPaperJson);
                            notifyDataSetChanged();
                            if(swipeRefreshLayout!=null)
                                swipeRefreshLayout.setRefreshing(false);
                        } catch (IOException e) {
                            showLog("实例化News失败");
                            e.printStackTrace();
                        }
                    }

                    private void addNewsUnit(NewsPaperJson newsPaperJson) {
                        if(newsPaperJson==null)
                        {
                            showLog("NewsPaperJson is null");
                            return;
                        }
                        NewsPaperJson.ResultBean resultBean=newsPaperJson.getResult();
                        if(resultBean==null)
                        {
                            showLog("API受限");
                            return;
                        }
                        List<NewsBean> units=resultBean.getData();
                        newsBeans=units;
                    }
                });
    }


    private   String getNewsUrlByType()
    {
        return NEWS_URL_STRING+NEWS_TYPE+JUHE_APPKEY_STRING;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType)
        {
            case ONE_IMAGE_VIEWTYPE:
                view=getInflateView(R.layout.newspaper_item_one,parent);
                ViewHolderOne viewHolder=new ViewHolderOne(view);
                return viewHolder;
            case TWO_IMAGE_VIEWTYPE:
                view=getInflateView(R.layout.newspaper_item_two,parent);
                ViewHolderTwo viewHolderTwo=new ViewHolderTwo(view);
                return viewHolderTwo;
            case THREE_IMAGE_VIEWTYPE:
                view=getInflateView(R.layout.newspaper_item_three,parent);
                ViewHolderThree viewHolderThree=new ViewHolderThree(view);
                return viewHolderThree;
        }
        view=getInflateView(R.layout.newspaper_item_one,parent);
        ViewHolderOne viewHolder=new ViewHolderOne(view);
        return viewHolder;
    }

    private View getInflateView(int resource,ViewGroup parent)
    {
        Context context=parent.getContext();
        return LayoutInflater.from(context).inflate(resource,parent,false);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        NewsBean newsBean=newsBeans.get(position);
        Uri imageUri_s01=null;
        ItemViewOnclickListener itemViewOnclickListener=new ItemViewOnclickListener(newsBean.getUrl());
        holder.itemView.setOnClickListener(itemViewOnclickListener);
        if(holder instanceof ViewHolderOne)
        {
            ViewHolderOne viewHolder=(ViewHolderOne)holder;
            viewHolder.newsTitle.setText(newsBean.getTitle());
            imageUri_s01=Uri.parse(newsBean.getThumbnail_pic_s());
            viewHolder.newsImage_s01.setImageURI(imageUri_s01);
            int heigh=viewHolder.newsTitle.getHeight();
            int margintop=viewHolder.newsImage_s01.getHeight()-heigh;
            viewHolder.newsStamp.setText(getStampOfNewsBean(newsBean));
        }
        else if(holder instanceof ViewHolderTwo)
            {
                ViewHolderTwo viewHolder=(ViewHolderTwo) holder;
                viewHolder.newsTitle.setText(newsBean.getTitle());
                imageUri_s01=Uri.parse(newsBean.getThumbnail_pic_s());
                viewHolder.newsImage_s01.setImageURI(imageUri_s01);
                Uri imageUri_s02=Uri.parse(newsBean.getThumbnail_pic_s02());
                viewHolder.newsImage_s02.setImageURI(imageUri_s02);
                viewHolder.newsStamp.setText(getStampOfNewsBean(newsBean));
            }
            else if (holder instanceof ViewHolderThree)
            {
                ViewHolderThree viewHolder=(ViewHolderThree)holder;
                viewHolder.newsTitle.setText(newsBean.getTitle());
                imageUri_s01=Uri.parse(newsBean.getThumbnail_pic_s());
                viewHolder.newsImage_s01.setImageURI(imageUri_s01);
                Uri imageUri_s02=Uri.parse(newsBean.getThumbnail_pic_s02());
                viewHolder.newsImage_s02.setImageURI(imageUri_s02);
                Uri imageUri_s03=Uri.parse(newsBean.getThumbnail_pic_s03());
                viewHolder.newsImage_s03.setImageURI(imageUri_s03);
                viewHolder.newsStamp.setText(getStampOfNewsBean(newsBean));
            }
    }



    public String getStampOfNewsBean(@NonNull NewsBean newsBean)
    {
        return newsBean.getDate()+"  "+newsBean.getAuthor_name();
    }

    @Override
    public int getItemCount() {
        if(newsBeans==null)
            return 0;
        else
            return newsBeans.size();
    }

    @Override
    public int getItemViewType(int position) {
        NewsBean newsBean=newsBeans.get(position);
        if(newsBean==null)
            return super.getItemViewType(position);
        if(!TextUtils.isEmpty(newsBean.getThumbnail_pic_s03()))
        {
            return THREE_IMAGE_VIEWTYPE;
        }
        else
            if(!TextUtils.isEmpty(newsBean.getThumbnail_pic_s02()))
            {
                return TWO_IMAGE_VIEWTYPE;
            }
            else
            {
                return ONE_IMAGE_VIEWTYPE;
            }
    }

    public void setSwipeRefreshLayout(@NonNull SwipeRefreshLayout refreshLayout) {
        this.swipeRefreshLayout = refreshLayout;
        if(swipeRefreshLayout!=null)
        {
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    requestNews();
                }
            });

        }
    }



    public class ViewHolderOne extends RecyclerView.ViewHolder{
        @BindView(R.id.news_one_title)
        TextView newsTitle;
        @BindView(R.id.news_one_stamp)
        TextView newsStamp;
        @BindView(R.id.news_one_image1)
        SimpleDraweeView newsImage_s01;
        public ViewHolderOne(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            newsTitle.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            ViewGroup.LayoutParams layoutParam=newsImage_s01.getLayoutParams();
            layoutParam.width=screenWidth/2+20;
            layoutParam.height=layoutParam.width*3/4;
            newsImage_s01.setLayoutParams(layoutParam);
        }
    }

    public class ViewHolderTwo extends RecyclerView.ViewHolder{
        @BindView(R.id.news_two_title)
        TextView newsTitle;
        @BindView(R.id.news_two_stamp)
        TextView newsStamp;
        @BindView(R.id.news_two_image1)
        SimpleDraweeView newsImage_s01;
        @BindView(R.id.news_two_image2)
        SimpleDraweeView newsImage_s02;
        public ViewHolderTwo(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            newsTitle.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            ViewGroup.LayoutParams layoutParam=newsImage_s01.getLayoutParams();
            layoutParam.width=screenWidth/2-3;
            layoutParam.height=layoutParam.width*3/4;
            newsImage_s01.setLayoutParams(layoutParam);
            newsImage_s02.setLayoutParams(layoutParam);
        }
    }

    public class ViewHolderThree extends RecyclerView.ViewHolder{
        @BindView(R.id.news_three_title)
        TextView newsTitle;
        @BindView(R.id.news_three_stamp)
        TextView newsStamp;
        @BindView(R.id.news_three_image1)
        SimpleDraweeView newsImage_s01;
        @BindView(R.id.news_three_image2)
        SimpleDraweeView newsImage_s02;
        @BindView(R.id.news_three_image3)
        SimpleDraweeView newsImage_s03;
        public ViewHolderThree(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            newsTitle.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            ViewGroup.LayoutParams layoutParam=newsImage_s01.getLayoutParams();
            layoutParam.width=screenWidth/3-3;
            layoutParam.height=layoutParam.width*3/4;
            newsImage_s01.setLayoutParams(layoutParam);
            newsImage_s02.setLayoutParams(layoutParam);
            newsImage_s03.setLayoutParams(layoutParam);
        }
    }
}
