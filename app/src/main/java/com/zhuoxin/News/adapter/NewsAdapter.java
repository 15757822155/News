package com.zhuoxin.News.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zhuoxin.News.R;
import com.zhuoxin.News.base.MyBaseAdapter;
import com.zhuoxin.News.entity.NewsInfo;
import com.zhuoxin.News.utils.BitmapUtil;
import com.zhuoxin.News.utils.HttpClientUtil;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2016/11/30.
 */

public class NewsAdapter extends MyBaseAdapter<NewsInfo> {
    public NewsAdapter(Context context) {
        super(context);
    }

    //设置是否在飞速滑动状态
    public boolean isFlying = false;

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.item_home_news, null);
            holder = new ViewHolder();
            holder.iv_home_news_icon = (ImageView) view.findViewById(R.id.iv_home_news_icon);
            holder.tv_home_news_title = (TextView) view.findViewById(R.id.tv_home_news_title);
            holder.tv_hone_news_type = (TextView) view.findViewById(R.id.tv_home_news_type);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        //设置标记,根据每个条目的imageUrl,之后在AsyncTask中获取标记
        holder.iv_home_news_icon.setTag(getItem(i).getImageUrl());
        URL url = null;
        try {
            url = new URL(getItem(i).getImageUrl());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        ////如果在飞速滑动中
        ////加载默认图标
        //holder.iv_home_news_icon.setImageResource(R.drawable.ic_launcher);
        ////先使用默认的图标在加载图片,解决加载缓存闪屏问题
        ////holder.iv_home_news_icon.setImageResource(R.drawable.ic_launcher);
        ////调用异步操作,设置imageView,这里不需要用到holder.set...
        ////HttpClientUtil.setImageURLToImageView(url, holder.iv_home_news_icon);
        //BitmapUtil.setBitmap(context, getItem(i).getImageUrl(), holder.iv_home_news_icon);
        //用第三方框架picasso来加载图片文件
        Picasso.with(context)
                .load(getItem(i).getImageUrl())
                .placeholder(R.drawable.ic_launcher)
                .error(R.drawable.shape_button_red)
                .into(holder.iv_home_news_icon);
        holder.tv_home_news_title.setText(getItem(i).getTitle());
        holder.tv_hone_news_type.setText(getItem(i).getType());
        return view;
    }

    class ViewHolder {
        ImageView iv_home_news_icon;
        TextView tv_home_news_title;
        TextView tv_hone_news_type;
    }
}
