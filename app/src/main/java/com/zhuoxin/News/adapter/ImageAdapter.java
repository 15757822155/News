package com.zhuoxin.News.adapter;

import android.content.Context;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.zhuoxin.News.R;
import com.zhuoxin.News.entity.GirlsInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/7.
 * 此类是图片的RecycleView的adapter类
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    List<GirlsInfo.ResultsBean> resultsBeanList = new ArrayList<>();
    Context context;
    String url;

    public ImageAdapter(Context context) {
        this.context = context;
    }

    public List<GirlsInfo.ResultsBean> getResultsBeanList() {
        return resultsBeanList;
    }

    public void setResultsBeanList(List<GirlsInfo.ResultsBean> resultsBeanList) {
        this.resultsBeanList = resultsBeanList;
    }

    //创建一个ViewHolder内部类继承RecycleView的ViewHolder
    static class ViewHolder extends RecyclerView.ViewHolder {
        //只有一个imageview
        ImageView iv_image;

        public ViewHolder(View itemView) {
            super(itemView);
            //fbi
            iv_image = (ImageView) itemView.findViewById(R.id.iv_image);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_girls, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //设置图片,用到了第三方框架Picasso
        Picasso.with(context)
                .load(resultsBeanList.get(position).getUrl())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.drawable.shape_button_red)
                .into(holder.iv_image);
    }

    @Override
    public int getItemCount() {
        return resultsBeanList.size();
    }


}
