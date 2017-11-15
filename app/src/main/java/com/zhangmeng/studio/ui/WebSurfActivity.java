package com.zhangmeng.studio.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zhangmeng.studio.R;
import com.zhangmeng.studio.beans.DefaultWebConfig;
import com.zhangmeng.studio.utils.ADFilterTool;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.zhangmeng.studio.utils.LogUtil.showLog;

/**
 * 打开咨询所示网页的APP
 */

public class WebSurfActivity extends AppCompatActivity {
    @BindView(R.id.websurf_webView)
    WebView webView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_websurf);
        ButterKnife.bind(this);

        DefaultWebConfig.setDefaultConfig(webView);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                url= url.toLowerCase();
                if(ADFilterTool.isWhiteUrl(url)){
                    return super.shouldInterceptRequest(view,url);//正常加载
                }else{
                    return new WebResourceResponse(null,null,null);//含有广告资源屏蔽请求
                }
            }
        });
        Intent intent=getIntent();
        Uri uri=intent.getData();
        if(uri==null)
            finish();
        String url=uri.toString();
        webView.loadUrl(url);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            if(!webView.canGoBack())
                finish();
            else
                webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
