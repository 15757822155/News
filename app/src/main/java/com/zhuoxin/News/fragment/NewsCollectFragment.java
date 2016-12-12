package com.zhuoxin.News.fragment;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhuoxin.News.R;
import com.zhuoxin.News.adapter.NewsCollectAdapter;
import com.zhuoxin.News.entity.NewsInfo;
import com.zhuoxin.News.utils.DBUtil;
import com.zhuoxin.News.utils.MyDBOpenHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class NewsCollectFragment extends Fragment {
    @InjectView(R.id.rv_news_collect)
    RecyclerView rv_news_collect;
    List<NewsInfo> newsInfoList = new ArrayList<>();
    NewsCollectAdapter adapter;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_collect, container, false);
        ButterKnife.inject(this, view);
        context = getActivity();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //先查询本地数据库表中所有数据,将查到的数据全部赋值到newsInfoList中
        MyDBOpenHelper myDBOpenHelper = new MyDBOpenHelper(context, "News.db", null, 1);
        SQLiteDatabase database = myDBOpenHelper.getWritableDatabase();
        newsInfoList = DBUtil.queryAll(database);
        //实例化adapter
        adapter = new NewsCollectAdapter(context);
        //设置数据
        adapter.setNewsInfoList(newsInfoList);
        //设置布局(显示效果为线性排布,可以)
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        rv_news_collect.setLayoutManager(layoutManager);
        //设置adapter
        rv_news_collect.setAdapter(adapter);
    }
}
