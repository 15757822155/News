package com.zhuoxin.News.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.zhuoxin.News.R;
import com.zhuoxin.News.utils.BitmapUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class NewsActivity extends AppCompatActivity {
    @InjectView(R.id.wv_news)
    WebView wv_news;
    @InjectView(R.id.iv_news)
    ImageView iv_news;
    String url;
    String largeImageURL;
    String title;
    @InjectView(R.id.fab_news)
    FloatingActionButton fab_news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        ButterKnife.inject(this);
        //初始化第三方分享SDK键值
        ShareSDK.initSDK(this, "157fc72150700");

        //将原本写在onCreate中的系统ui设置统一放到以下方法中
        initSystemUI();
        //设置webView客户端,避免弹出到系统浏览器
        wv_news.setWebViewClient(new WebViewClient());
        //设置chrome的客户端
        wv_news.setWebChromeClient(new WebChromeClient());
        //设置Java脚本
        wv_news.getSettings().setJavaScriptEnabled(true);
        //设置读取网址
        wv_news.loadUrl(url);

    }

    private void initSystemUI() {
        //获取数据
        url = getIntent().getStringExtra("url");
        largeImageURL = getIntent().getStringExtra("largeImageURL");
        title = getIntent().getStringExtra("title");
        //转场动画设置的顶部actionbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //设置标题栏
        getSupportActionBar().setTitle(title);
        //悬浮框的设置
        fab_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showShare();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //设置标记,若不设置,会报空指针
        iv_news.setTag(largeImageURL);
        BitmapUtil.setBitmap(this, largeImageURL, iv_news);

    }
    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle("每日新闻");
        // titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl(url);
        //设置图片
        oks.setImageUrl(largeImageURL);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(title);
        // 启动分享GUI
        oks.show(this);

    }
}
