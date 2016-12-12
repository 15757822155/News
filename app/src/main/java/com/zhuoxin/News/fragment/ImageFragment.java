package com.zhuoxin.News.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zhuoxin.News.R;
import com.zhuoxin.News.adapter.ImageAdapter;
import com.zhuoxin.News.entity.GirlsInfo;
import com.zhuoxin.News.interfaces.GirlsInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2016/12/1.
 */

public class ImageFragment extends Fragment {
    @InjectView(R.id.rv_image)
    RecyclerView rv_image;
    List<GirlsInfo.ResultsBean> resultsBeanList = new ArrayList<>();
    ImageAdapter imageAdapter;
    Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, container, false);
        ButterKnife.inject(this, view);
        context = getActivity();
        //实例化adapter
        imageAdapter = new ImageAdapter(context);
        //设置布局(交错布局),参数(行数或列数,横线排布或纵向排布)
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rv_image.setLayoutManager(layoutManager);
        rv_image.setAdapter(imageAdapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //调用方法(获取json数据,解析,调整返回list数据)
        getGirls();
    }

    private void getGirls() {

        /**
         * 2.创建Retrofit
         * baseUrl(传入基础网址)
         * addConverterFactory(通过第三方框架,直接解析获得的json数据,解析成我们在接口中定义的泛型类型)
         * build创建
         */
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://gank.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //3.获取接口对象,通过retrofit创建
        GirlsInterface girlsInterface = retrofit.create(GirlsInterface.class);
        //4.加入队列进行数据解析,通过我们写的接口方法获得
        Call<GirlsInfo> call = girlsInterface.getGirls();
        call.enqueue(new Callback<GirlsInfo>() {
            //成功的返回
            @Override
            public void onResponse(Call<GirlsInfo> call, Response<GirlsInfo> response) {
                //第三方框架已经将获取的全部数据都保存在response中
                GirlsInfo girlsInfo = response.body();
                //获取response中的数据,并设置到resultsBeanList中
                resultsBeanList = girlsInfo.getResults();
                imageAdapter.setResultsBeanList(resultsBeanList);
                imageAdapter.notifyDataSetChanged();
            }

            //失败的返回
            @Override
            public void onFailure(Call<GirlsInfo> call, Throwable t) {
            }
        });
    }
}
