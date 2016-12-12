package com.zhuoxin.News.interfaces;

import com.zhuoxin.News.entity.GirlsInfo;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Administrator on 2016/12/7.
 */

public interface GirlsInterface {

    /**
     * 调用第三方框架Retrofit.方法,创建了一个接口方法getGirls();
     * GET的参数为具体的目标子地址
     */
    @GET("api/data/福利/50/1")
    Call<GirlsInfo> getGirls();

}
