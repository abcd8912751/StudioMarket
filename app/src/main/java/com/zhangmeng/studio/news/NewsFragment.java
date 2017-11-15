package com.zhangmeng.studio.news;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhangmeng.studio.R;
import com.zhangmeng.studio.contract.NewsContract;
import com.zhangmeng.studio.view.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhangmeng on 2017/11/4.
 */

public class NewsFragment extends BaseFragment implements NewsContract.NewsView {
    @BindView(R.id.news_viewPager)
    ViewPager viewPager;
    @BindView(R.id.tabLayout_news)
    TabLayout tabLayout;
    private NewsPaperAdapter newsPaperAdapter;


    public void initMVPcontract() {
        NewsPaperAdapter adapter=new NewsPaperAdapter(this);
        viewPager.setAdapter(adapter);
        repeatAddTabItem();
    }

    /**
     * setupWithViewPager中会removeAllTabs,重新添加并设置监听
     */
    private void repeatAddTabItem() {
        tabLayout.setupWithViewPager(viewPager, true);
        tabLayout.getTabAt(0).setText(R.string.item_tab_toutiao);
        tabLayout.getTabAt(1).setText(R.string.item_tab_shehui);
        tabLayout.getTabAt(2).setText(R.string.item_tab_guonei);
        tabLayout.getTabAt(3).setText(R.string.item_tab_guoji);
        tabLayout.getTabAt(4).setText(R.string.item_tab_keji);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Nullable
    @Override
    public android.view.View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.from(context).inflate(R.layout.fragment_news, container, false);
        ButterKnife.bind(this, view);

        initMVPcontract();
        return view;
    }

    @Override
    public int getTabCount() {
        return tabLayout.getTabCount();
    }

    @Override
    public int getSelectedTabPos() {
        return tabLayout.getSelectedTabPosition();
    }
    @Override
    public Context getContext() {
        return context;
    }
}
