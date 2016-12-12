package com.zhuoxin.News.fragment;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.zhuoxin.News.R;
import com.zhuoxin.News.activity.NewsActivity;
import com.zhuoxin.News.adapter.NewsAdapter;
import com.zhuoxin.News.entity.NewsInfo;
import com.zhuoxin.News.entity.NewsOfJuhe;
import com.zhuoxin.News.utils.DBUtil;
import com.zhuoxin.News.utils.HttpClientUtil;
import com.zhuoxin.News.utils.MyDBOpenHelper;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * Created by Administrator on 2016/12/1.
 */

public class NewsFragment extends Fragment {
    private String type = "top";
    public static final String TYPE_TOP = "top";
    public static final String TYPE_SHEHUI = "shehui";
    public static final String TYPE_GUOJI = "guoji";
    public static final String TYPE_KEJI = "keji";
    View view;
    final int JSON_STATUS_SUCCESS = 0;
    final int JSON_STATUS_FAIL = 1;
    final int JSON_STATUS_EXCEPTION = 2;
    @InjectView(R.id.lv_home_news)
    ListView lv_home_news;
    NewsAdapter newsAdapter;
    List<NewsInfo> newsInfoList = new ArrayList<NewsInfo>();
    Context context;
    Handler handle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what) {
                //如果成功就刷新数据
                case JSON_STATUS_SUCCESS:
                    newsAdapter.notifyDataSetChanged();
                    break;
                //如果连接失败,就返回访问码
                case JSON_STATUS_FAIL:
                    Toast.makeText(context, "获取数据失败,返回码为" + msg.arg1, Toast.LENGTH_SHORT).show();
                    break;
                //如果连接异常,就返回异常原因
                case JSON_STATUS_EXCEPTION:
                    Exception e = (Exception) msg.obj;
                    Toast.makeText(context, "获取数据异常,异常原因为" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    public NewsFragment() {

    }

    @SuppressLint("ValidFragment")
    public NewsFragment(String type) {

        this.type = type;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //从Fragment中可以获取到依附的context
        context = getActivity();
        //实例化一个adapter,并附空数据,之后通过notiyf更新数据
        newsAdapter = new NewsAdapter(context);
        newsAdapter.setDataList(newsInfoList);
        lv_home_news.setAdapter(newsAdapter);

        lv_home_news.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                switch (i) {
                    case SCROLL_STATE_FLING:
                        newsAdapter.isFlying = true;
                        break;
                    case SCROLL_STATE_IDLE:
                        newsAdapter.isFlying = false;
                        newsAdapter.notifyDataSetChanged();
                        break;
                    case SCROLL_STATE_TOUCH_SCROLL:
                        newsAdapter.isFlying = false;
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });
        //对每一个条目设置单机监听事件
        lv_home_news.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //参数(整个Listview的activity,ListView中单个条目,位置,id(getid同位置))
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //先找到要进行转场动画的view
                View iv_home_news_icon = view.findViewById(R.id.iv_home_news_icon);
                Intent intent = new Intent(context, NewsActivity.class);
                NewsInfo newsInfo = newsAdapter.getItem(i);
                intent.putExtra("url", newsInfo.getUrl());
                intent.putExtra("largeImageURL", newsInfo.getLargeImageURL());
                intent.putExtra("title", newsInfo.getTitle());
                //如果版本大于21(5.0),就运行转场动画,否则,直接跳转
                if (Build.VERSION.SDK_INT >= 21) {
                    //转场动画参数(环境,要转场动画的控件id,转场控件name)
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity(), iv_home_news_icon, "news").toBundle());
                } else {
                    startActivity(intent);
                }
            }
        });
        //设置长按事件
        lv_home_news.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                //创建一个popupwindow
                final PopupWindow popupWindow = new PopupWindow();
                //设置自定义view的布局
                View popupView = LayoutInflater.from(context).inflate(R.layout.layout_popupwindow, null);
                //设置popupwindow的宽高
                popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                //设置popupwindow内容
                popupWindow.setContentView(popupView);
                //设置popupwindow的背景颜色
                popupWindow.setBackgroundDrawable(new ColorDrawable(0xffffffff));
                //是否设置外部点击事件(点击外部,popupwindow消失)
                popupWindow.setOutsideTouchable(true);
                //设置焦点,在popupWindow未消失前,点击其他地方,popupwindow消失
                popupWindow.setFocusable(true);
                //设置popupwindow的显示位置(需要写在最后,等内容颜色等都设置完,在设置位置,否则写在位置后的设置无法实现)
                popupWindow.showAtLocation(view, Gravity.CENTER_VERTICAL, 0, 50);
                popupView.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //创建一个数据库
                        MyDBOpenHelper myDBOpenHelper = new MyDBOpenHelper(context, "News.db", null, 1);
                        //获取可读(也可写)数据库,如果内存满后,只能读取
                        SQLiteDatabase database = myDBOpenHelper.getReadableDatabase();
                        //调用添加数据的方法
                        if (!DBUtil.query(database, newsAdapter.getItem(i))) {
                            DBUtil.insert(database, newsAdapter.getItem(i));
                            Toast.makeText(context, "收藏成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "已收藏", Toast.LENGTH_SHORT).show();
                        }
                        //使popupwindow消失
                        popupWindow.dismiss();
                    }
                });
                popupView.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //使popupwindow消失
                        popupWindow.dismiss();
                    }
                });
                return true;//如果return一个false,则表示,在响应完长按事件后,还可以响应别的事件,如点击事件
            }
        });
        initListView();

    }

    private void initListView() {
        //耗时操作放在子线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL targetUrl = null;
                try {
                    Logger.d(type);
                    targetUrl = new URL("http://v.juhe.cn/toutiao/index?type=" + type + "&key=d728ab4e75e137c4f23aec12ed3ee6cd");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                HttpClientUtil.getJson(targetUrl, onJsonGetListener);
            }
        }).start();
    }

    /**
     * 调用接口对象
     * 对获得的数据用Gson进行解析
     * 对获得的数据进行判断的步骤
     */
    private HttpClientUtil.OnJsonGetListener onJsonGetListener = new HttpClientUtil.OnJsonGetListener() {
        @Override
        public void jsonGetSuccess(String json) {
            //使用Gosn解析数据
            Gson gson = new Gson();
            //如果从服务器获取的json不为空,做以下内容
            if (json != null) {
                //将解析后的数据保存在自定义类中
                NewsOfJuhe newsData = gson.fromJson(json, NewsOfJuhe.class);
                NewsOfJuhe.Result result = newsData.getResult();
                //如果从服务器获取的result不为空,做以下内容
                if (result != null) {
                    //从保存的数据中取出数据
                    List<NewsOfJuhe.Data> dataList = result.getData();
                    //如果从服务器获取的dataList不为空,做以下内容
                    if (dataList != null) {
                        //循环获取数据,并添加到列表中
                        for (NewsOfJuhe.Data data : dataList) {
                            //获取图片地址
                            String url = data.getUrl();
                            String imageUrl = data.getThumbnail_pic_s();
                            String largeImageURL = data.getThumbnail_pic_s03();
                            String title = data.getTitle();
                            String newsType;
                            //判断数据获取类型是头条还是别的社会什么的
                            if (type.equals(TYPE_TOP)) {
                                newsType = data.getRealtype();
                            } else {
                                //除头条外,别的类型没有realtype,只能获取category
                                newsType = data.getCategory();
                            }
                            NewsInfo newsInfo = new NewsInfo(url, imageUrl, largeImageURL, title, newsType);
                            newsInfoList.add(newsInfo);
                        }
                        //handle机制,msg传递数据
                        Message msg = handle.obtainMessage();
                        msg.what = JSON_STATUS_SUCCESS;
                        handle.sendMessage(msg);
                    } else {
                        Message msg = handle.obtainMessage();
                        msg.what = JSON_STATUS_FAIL;
                        msg.arg1 = -1;
                        handle.sendMessage(msg);
                    }
                } else {
                    Message msg = handle.obtainMessage();
                    msg.what = JSON_STATUS_FAIL;
                    msg.arg1 = -1;
                    handle.sendMessage(msg);
                }
            } else {
                Message msg = handle.obtainMessage();
                msg.what = JSON_STATUS_FAIL;
                msg.arg1 = -1;
                handle.sendMessage(msg);
            }
        }

        @Override
        public void jsonGetFail(int responsedCode) {
            Message msg = handle.obtainMessage();
            //传递返回码
            msg.arg1 = responsedCode;
            msg.what = JSON_STATUS_FAIL;
            handle.sendMessage(msg);
        }

        @Override
        public void jsonGetException(Exception e) {
            Message msg = handle.obtainMessage();
            //传递异常原因
            msg.obj = e;
            msg.what = JSON_STATUS_EXCEPTION;
            handle.sendMessage(msg);
        }
    };
}
