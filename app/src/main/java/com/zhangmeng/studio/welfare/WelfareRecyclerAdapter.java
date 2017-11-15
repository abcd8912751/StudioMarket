package com.zhangmeng.studio.welfare;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhangmeng.studio.R;
import com.zhangmeng.studio.beans.WelfareMissJson;
import com.zhangmeng.studio.ui.ZoomImageActivity;
import com.zhangmeng.studio.utils.Constants;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

import static com.zhangmeng.studio.utils.LogUtil.showLog;

/**
 * Created by zhangmeng on 2017/11/4.
 */

public class WelfareRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private List<WelfareMissJson.MissBean> welfareMisses;
    private List<Integer> randomHeights;    //用来存储随机生成的照片高度,防止滑动移位
    private int pageNo;
    private ObjectMapper welfareMissMapper;
    private int width;
    public WelfareRecyclerAdapter()
    {
        initMisses();
        welfareMissMapper=new ObjectMapper();
        width=1080;
        randomHeights=new ArrayList<Integer>();
    }

    private void initMisses() {
        setPageNo(1);
        appendMisses();
    }



    public void appendMisses()
    {
        requestMissData(Constants.WELFARE_URL_STRING +pageNo);
    }

    private void requestMissData(String url)
    {
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback()
                {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            WelfareMissJson welfareMissJson =welfareMissMapper.readValue(response,WelfareMissJson.class);
                            addMissAndHeight(welfareMissJson);
                            displayMisses();
                        } catch (IOException e) {
                            e.printStackTrace();
                            showLog("miss序列化失败");
                        }
                    }

                });
    }

    private void addMissAndHeight(WelfareMissJson welfareMissJson) {
        List<WelfareMissJson.MissBean>jsonResults=welfareMissJson.getResults();
        if(welfareMisses==null)
            welfareMisses=jsonResults;
        else
            welfareMisses.addAll(jsonResults);

        for(WelfareMissJson.MissBean miss:jsonResults)
        {
            int height=(int) (500+Math.random()*300);
            randomHeights.add(height);
        }
        showLog("size:"+welfareMisses.size());
    }

    private void displayMisses() {
        notifyDataSetChanged();
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        Context context=parent.getContext();
        View view= LayoutInflater.from(context).inflate( R.layout.recyclerview_welfare_item, parent,false);
        WelfareViewHolder viewHolder=new WelfareViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(!(holder instanceof WelfareViewHolder))
            return;
        SimpleDraweeView image_RecyclerItem=((WelfareViewHolder) holder).image_RecyclerItem;
        WelfareMissJson.MissBean miss=welfareMisses.get(position);
        Uri imageUri= Uri.parse(miss.getUrl());
        image_RecyclerItem.setTag(imageUri);
        ViewGroup.LayoutParams params = image_RecyclerItem.getLayoutParams();
        params.height =  randomHeights.get(position) ;
        params.width=width/2;
        image_RecyclerItem.setLayoutParams(params);
        Object tag=image_RecyclerItem.getTag();
        if(tag==null)
            image_RecyclerItem.setImageURI(imageUri);
        else
        {
            Uri imageTag=(Uri)tag;
            if(imageUri.equals(imageTag))
                image_RecyclerItem.setImageURI(imageUri);
        }
        image_RecyclerItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context=v.getContext();
                Intent intent=new Intent(context, ZoomImageActivity.class);
                intent.putStringArrayListExtra(Constants.ZOOM_EXTRA_NAME, getExtraList());
                intent.setAction(position+"");
                context.startActivity(intent);
            }

            private ArrayList<String> getExtraList() {
                ArrayList<String> urls=new ArrayList<String>();
                for(WelfareMissJson.MissBean missBean:welfareMisses)
                {
                    urls.add(missBean.getUrl());
                }
                showLog("urls.length"+urls.size());
                return urls;
            }
        });
    }

    @Override
    public int getItemCount() {
        if(welfareMisses==null)
            return 0;
        else
            return welfareMisses.size();

    }

    public void turnUpPageNo() {
        setPageNo(this.pageNo+1);
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public class WelfareViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image_recyclerView_item)
        SimpleDraweeView image_RecyclerItem;
        public WelfareViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }


}
