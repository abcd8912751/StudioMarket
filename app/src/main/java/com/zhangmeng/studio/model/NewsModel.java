package com.zhangmeng.studio.model;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.zhangmeng.studio.R;
import com.zhangmeng.studio.contract.NewsContract;
import com.zhangmeng.studio.news.NewsPaperRecyclerAdapter;
import com.zhangmeng.studio.beans.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangmeng on 2017/11/4.
 */

public class NewsModel implements NewsContract.Model {

    private List<View> viewPages;
    private Context context;
    private List<String> tabNames;
    public NewsModel(Context view_context)
    {
        this.context=view_context;
        initTabNames();
        initViewPages();
    }


    private void initViewPages() {
        viewPages=new ArrayList<View>();
        RecycleViewDivider itemDecoration=new RecycleViewDivider(context);
        for(int i=0;i<5;i++)
        {
            View view= LayoutInflater.from(context).inflate(R.layout.viewpage_news,null);
            RecyclerView recyclerView=(RecyclerView)view.findViewById(R.id.recyclerView_newspaper);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            NewsPaperRecyclerAdapter recyclerAdapter=new NewsPaperRecyclerAdapter(tabNames.get(i));
            recyclerView.addItemDecoration(itemDecoration);
            SwipeRefreshLayout swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipeRefresh_newspaper);
            recyclerAdapter.setSwipeRefreshLayout(swipeRefreshLayout);
            recyclerView.setAdapter(recyclerAdapter);
            viewPages.add(view);
        }
    }

    private void initTabNames() {
        tabNames=new ArrayList<String>();
        tabNames.add("toutiao");
        tabNames.add("shehui");
        tabNames.add("guonei");
        tabNames.add("guoji");
        tabNames.add("keji");
    }
    @Override
    public int getCount() {
        return viewPages.size();
    }

    @Override
    public View getViewPage(int position) {
        return viewPages.get(position);
    }




}
