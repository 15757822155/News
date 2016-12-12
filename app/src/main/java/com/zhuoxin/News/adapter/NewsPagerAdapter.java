package com.zhuoxin.News.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.zhuoxin.News.fragment.NewsFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/9.
 */

public class NewsPagerAdapter extends FragmentPagerAdapter {
    List<NewsFragment> newsFragmentList = new ArrayList<>();

    public NewsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public List<NewsFragment> getNewsFragmentList() {
        return newsFragmentList;
    }

    public void setNewsFragmentList(List<NewsFragment> newsFragmentList) {
        this.newsFragmentList = newsFragmentList;
    }

    @Override
    public int getCount() {
        return newsFragmentList.size();
    }

    @Override
    public Fragment getItem(int position) {
        return newsFragmentList.get(position);
    }


}
