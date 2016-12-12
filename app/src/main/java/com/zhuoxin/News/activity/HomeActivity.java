package com.zhuoxin.News.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.zhuoxin.News.R;
import com.zhuoxin.News.fragment.ImageFragment;
import com.zhuoxin.News.fragment.NewsCollectFragment;
import com.zhuoxin.News.fragment.NewsPagerFragment;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.tencent.qq.QQ;


public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Fragment currentFragment;
    ImageView iv_user_photo;
    TextView tv_user_name;
    TextView tv_user_other;
    Context context;
    boolean isFirst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        context = this;
        ShareSDK.initSDK(this, "157fc72150700");
        initSystemUI();
        showNewsPagerFragment();
        isFirst = getSharedPreferences("config", Context.MODE_PRIVATE).getBoolean("isFirst", true);
        if (!isFirst) {
            logon(false);
        }
    }

    private void initSystemUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_news);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View handView = navigationView.getHeaderView(0);
        tv_user_name = (TextView) handView.findViewById(R.id.tv_user_name);
        tv_user_other = (TextView) handView.findViewById(R.id.tv_user_other);
        iv_user_photo = (ImageView) handView.findViewById(R.id.iv_user_photo);
        iv_user_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logon(true);
                getSharedPreferences("config", Context.MODE_PRIVATE).edit().putBoolean("isFirst", false).commit();
                isFirst = false;
                //Toast.makeText(HomeActivity.this, "用户头像", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onBackPressed() {
        //找到抽屉
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //如果抽屉是打开状态
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            //按返回键关闭抽屉
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //调用父类方法,finish()
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_receiver) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_news) {
            // Handle the camera action
            showNewsPagerFragment();
        } else if (id == R.id.nav_gallery) {
            showImageFragment();
        } else if (id == R.id.nav_news_collect) {
            showNewsCollectFragment();
        } else if (id == R.id.nav_login) {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {
            showShare();
        } else if (id == R.id.nav_send) {
            Toast.makeText(this, "发送成功", Toast.LENGTH_SHORT).show();


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showNewsPagerFragment() {
        //如果currentFragment属于NewsFragment,吐司,否则跳转
        if (currentFragment instanceof NewsPagerFragment) {
            Toast.makeText(this, "数据未改变", Toast.LENGTH_SHORT).show();
        } else {
            //获取FragmentManager,管理Fragment
            FragmentManager fm = getSupportFragmentManager();
            //开启Fragment事务管理
            FragmentTransaction ft = fm.beginTransaction();
            //实例化要替换的Fragment
            NewsPagerFragment newsPagerFragment = new NewsPagerFragment();
            //替换Fragment
            ft.replace(R.id.fl_home, newsPagerFragment);
            //提交
            ft.commit();
            //初始化currentFragment为当前Fragment
            currentFragment = newsPagerFragment;
        }
    }

    private void showImageFragment() {
        if (currentFragment instanceof ImageFragment) {
            Toast.makeText(this, "数据未改变", Toast.LENGTH_SHORT).show();
        } else {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ImageFragment imageFragment = new ImageFragment();
            ft.replace(R.id.fl_home, imageFragment);
            ft.commit();
            currentFragment = imageFragment;
        }
    }

    private void showNewsCollectFragment() {
        if (currentFragment instanceof NewsCollectFragment) {
            Toast.makeText(this, "数据未改变", Toast.LENGTH_SHORT).show();
        } else {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            NewsCollectFragment newsCollectFragment = new NewsCollectFragment();
            ft.replace(R.id.fl_home, newsCollectFragment);
            ft.commit();
            currentFragment = newsCollectFragment;
        }
    }

    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle("每日新闻");
        // titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl("http://www.jikedaohang.com/");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("每日新闻今日头条");
        //设置图片
        oks.setImageUrl("http://www.jikedaohang.com/images/gaoxiao.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        //oks.setUrl("http://www.baidu.com");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        //oks.setComment("母猪排队掉进水沟视频");
        // site是分享此内容的网站名称，仅在QQ空间使用
        //oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        //oks.setSiteUrl("http://www.baidu.com");
        // 启动分享GUI
        oks.show(this);

    }

    private void logon(boolean isLogon) {
        Platform qq = ShareSDK.getPlatform(context, QQ.NAME);
        //回调信息，可以在这里获取基本的授权返回的信息，但是注意如果做提示和UI操作要传到主线程handler里去执行
        qq.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(final Platform platform, int i, HashMap<String, Object> hashMap) {
                //用Logger输出所有授权信息
                Logger.d("登录成功" + platform.getDb().exportData());
                final String user_name = platform.getDb().getUserName();
                final String user_photo = platform.getDb().getUserIcon();
                //将一些对UI界面的设置在主线程中运行
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //将头像设置成圆形
                        ImageOptions imageOptions = new ImageOptions.Builder()
                                .setCircular(true)
                                .build();
                        //使用xutils加载图片,并设置显示的形状
                        x.image().bind(iv_user_photo, user_photo, imageOptions);
                        //Picasso.with(context).load(user_photo).into(iv_user_photo);
                        //设置用户名
                        tv_user_name.setText(user_name);
                    }
                });
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {

            }

            @Override
            public void onCancel(Platform platform, int i) {

            }
        });
        //设置false表示使用SSO授权方式
        qq.SSOSetting(false);
        //单独授权,OnComplete返回的hashmap是空的
        if (isLogon) {
            qq.authorize();
        }
        //授权并获取用户信息
        qq.showUser(null);
    }

}
