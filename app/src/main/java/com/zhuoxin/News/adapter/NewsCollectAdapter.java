package com.zhuoxin.News.adapter;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zhuoxin.News.R;
import com.zhuoxin.News.activity.NewsActivity;
import com.zhuoxin.News.entity.NewsInfo;
import com.zhuoxin.News.utils.BitmapUtil;
import com.zhuoxin.News.utils.DBUtil;
import com.zhuoxin.News.utils.MyDBOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/6.
 */

public class NewsCollectAdapter extends RecyclerView.Adapter<NewsCollectAdapter.MyViewHolder> {
    List<NewsInfo> newsInfoList = new ArrayList<>();
    Context context;
    LayoutInflater inflater;

    //构造函数
    public NewsCollectAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    //获取列表的方法
    public List<NewsInfo> getNewsInfoList() {
        return newsInfoList;
    }

    //设置list的方法
    public void setNewsInfoList(List<NewsInfo> newsInfoList) {
        this.newsInfoList = newsInfoList;
    }

    //创建MyViewHolder继承自带的ViewHolder
    static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_news;
        TextView tv_title;
        TextView tv_type;

        //找到控件id
        public MyViewHolder(View itemView) {
            super(itemView);
            iv_news = (ImageView) itemView.findViewById(R.id.iv_home_news_icon);
            tv_title = (TextView) itemView.findViewById(R.id.tv_home_news_title);
            tv_type = (TextView) itemView.findViewById(R.id.tv_home_news_type);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_home_news, null);
        //将myViewHolder和view进行绑定,并返回myViewHolder,最后在系统的作用下,自动调到下面的方法
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    //这里的holder是从上一个方法传进来的
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final NewsInfo newsInfo = newsInfoList.get(position);
        //获取holder的所有view
        View view = holder.itemView;
        //对每一个view设置点击事件
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //先找到要进行转场动画的view
                View iv_home_news_icon = view.findViewById(R.id.iv_home_news_icon);
                Intent intent = new Intent(context, NewsActivity.class);
                intent.putExtra("url", newsInfo.getUrl());
                intent.putExtra("largeImageURL", newsInfo.getLargeImageURL());
                intent.putExtra("title", newsInfo.getTitle());
                //如果版本大于21(5.0),就运行转场动画,否则,直接跳转
                context.startActivity(intent);

            }
        });
        //对每一个view设置长按事件
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(context)
                        .setTitle("是否取消收藏?")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                MyDBOpenHelper myDBOpenHelper = new MyDBOpenHelper(context, "News.db", null, 1);
                                DBUtil.delete(myDBOpenHelper.getWritableDatabase(), newsInfo);
                                newsInfoList.remove(newsInfo);
                                //1.直接改变整个view,无动画
                                //notifyDataSetChanged();
                                //2.通知范围改变,有动画
                                // notifyItemRemoved(position);
                                // notifyItemRangeChanged(position,newsInfoList.size());
                                //3.直接获取数据改变后的位置,有动画
                                notifyItemRemoved(holder.getLayoutPosition());
                            }
                        })
                        .setNegativeButton("取消", null)
                        .create();
                alertDialog.show();
                return true;
            }
        });
        //设置标题
        holder.tv_title.setText(newsInfo.getTitle());
        //设置类型
        holder.tv_type.setText(newsInfo.getType());
        ////设置标记(不设会报空指针),图片
        //holder.iv_news.setTag(newsInfo.getImageUrl());
        //BitmapUtil.setBitmap(context, newsInfo.getImageUrl(), holder.iv_news);
        //用第三方框架picasso来加载图片文件
        Picasso.with(context)
                .load(newsInfo.getImageUrl())
                .placeholder(R.drawable.ic_launcher)
                .error(R.drawable.shape_button_red)
                .into(holder.iv_news);
    }

    //条目总数
    @Override
    public int getItemCount() {
        return newsInfoList.size();
    }
}
