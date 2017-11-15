package com.zhangmeng.studio.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioButton;

import com.zhangmeng.studio.R;
import com.zhangmeng.studio.contract.MainContract;
import com.zhangmeng.studio.home.HomeFragment;
import com.zhangmeng.studio.presenter.MainPresenter;
import com.zhangmeng.studio.view.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.zhangmeng.studio.utils.LogUtil.showLog;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,MainContract.MainView {
    @BindView(R.id.radio_home)
    RadioButton radioHome;
    @BindView(R.id.radio_news)
    RadioButton radioNews;
    @BindView(R.id.radio_welfare)
    RadioButton radioWelfare;
    private MainPresenter mainPresenter;
    private FragmentManager fm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mainPresenter=new MainPresenter(this);
        CheckSavedstate(savedInstanceState);
        ButterKnife.bind(this);     //important

    }

    private void CheckSavedstate(Bundle savedInstanceState) {
        fm = getSupportFragmentManager();
        FragmentTransaction fts = fm.beginTransaction();
        if (savedInstanceState == null) {
            transferFragment(mainPresenter.getTransferFragment(0),"0");
        }
        else {
            BaseFragment homeFragment=(BaseFragment)fm.findFragmentByTag("0");
            BaseFragment shoppingcartFragment=(BaseFragment)fm.findFragmentByTag("1");
            BaseFragment welfareFragment= (BaseFragment)fm.findFragmentByTag("2");
            if(shoppingcartFragment!=null&&shoppingcartFragment.isAdded())
                fts.hide(shoppingcartFragment);
            if(welfareFragment!=null&&welfareFragment.isAdded())
                fts.hide(welfareFragment);
            if(homeFragment==null)
            {
                homeFragment=mainPresenter.getTransferFragment(0);
            }
            showFragment(homeFragment,"0");
        }
        mainPresenter.setTempInfos(0);
    }
    @OnClick({R.id.radio_home,R.id.radio_news,R.id.radio_welfare})
    public void onClick(View v) {
        int position;
        switch (v.getId()) {
            case R.id.radio_home:
                position=0;
                break;
            case R.id.radio_news:
                position=1;
                break;
            case R.id.radio_welfare:
                position=2;
                break;
            default:
                position=0;
        }
        showLog("RadioGroup_position: "+position);
        if(position!=mainPresenter.getTemp_position())
        {
            BaseFragment baseFragment=mainPresenter.getTransferFragment(position);
            transferFragment(baseFragment,""+position);
            mainPresenter.setTempInfos(position);
            updateDrawableTop();
        }
    }




    private void transferFragment(BaseFragment to,String tag) {
        BaseFragment from=mainPresenter.getTempFragment();
        FragmentTransaction fts = fm.beginTransaction();
        if(from!=null)
        {
            fts.hide(from).commit();
        }
        showFragment(to, tag);
    }

    private void showFragment(BaseFragment to, String tag) {
        FragmentTransaction fts = fm.beginTransaction();
        if(to.isAdded())
        {
            fts.show(to);
        }
        else
        {
            fts.add(R.id.content_frame, to,tag);
        }
        fts.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void updateDrawableTop() {
        initDrawableTop();
        int position=mainPresenter.getTemp_position();
        switch (position)
        {
            case 0:
                radioHome.setCompoundDrawablesWithIntrinsicBounds(0,R.mipmap.radio_home_press,0,0);
                break;
            case 1:
                radioNews.setCompoundDrawablesWithIntrinsicBounds(0,R.mipmap.radio_news_press,0,0);
                break;
            case 2:
                radioWelfare.setCompoundDrawablesWithIntrinsicBounds(0,R.mipmap.radio_welfare_press,0,0);
                break;
        }
    }

    private void initDrawableTop() {
        radioHome.setCompoundDrawablesWithIntrinsicBounds(0,R.mipmap.radio_home,0,0);
        radioNews.setCompoundDrawablesWithIntrinsicBounds(0,R.mipmap.radio_news,0,0);
        radioWelfare.setCompoundDrawablesWithIntrinsicBounds(0,R.mipmap.radio_welfare,0,0);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        return super.onKeyDown(keyCode, event);
    }
}
