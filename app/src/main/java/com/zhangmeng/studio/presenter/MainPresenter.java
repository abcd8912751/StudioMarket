package com.zhangmeng.studio.presenter;

import com.zhangmeng.studio.contract.MainContract;
import com.zhangmeng.studio.home.HomeFragment;
import com.zhangmeng.studio.news.NewsFragment;
import com.zhangmeng.studio.view.BaseFragment;
import com.zhangmeng.studio.welfare.WelfareFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangmeng on 2017/11/4.
 */

public class MainPresenter implements MainContract.Presenter {
    private MainContract.MainView mainView;
    private List<BaseFragment> baseFragments;
    private int temp_position;
    private BaseFragment tempFragment;
    public MainPresenter(MainContract.MainView mainview)
    {
        this.mainView=mainview;
        this.temp_position=-1;
        initBaseFragments();
    }

    private void initBaseFragments() {
        baseFragments=new ArrayList<BaseFragment>();
        HomeFragment homeFragment=new HomeFragment();
        NewsFragment shoppingCartFragment=new NewsFragment();
        WelfareFragment welfareFragment=new WelfareFragment();
        baseFragments.add(homeFragment);
        baseFragments.add(shoppingCartFragment);
        baseFragments.add(welfareFragment);
    }

    @Override
    public BaseFragment getTransferFragment(int position) {
        if(position>=baseFragments.size())
        {
            position=baseFragments.size()-1;

        }
        return baseFragments.get(position);
    }

    @Override
    public BaseFragment getTempFragment() {

        return tempFragment;
    }

    @Override
    public void setTempInfos(int position) {
        setTempPosition(position);
        setTempFragment();
    }


    public void setTempPosition(int position) {
        this.temp_position=position;
    }

    public int getTemp_position() {
        return temp_position;
    }

    public void setTempFragment() {
        BaseFragment tempFragment=
        this.tempFragment = baseFragments.get(temp_position);
    }
}
