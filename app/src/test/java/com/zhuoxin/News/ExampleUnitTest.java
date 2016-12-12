package com.zhuoxin.News;


import com.google.gson.Gson;
import com.zhuoxin.News.entity.GirlsInfo;
import com.zhuoxin.News.interfaces.GirlsInterface;
import com.zhuoxin.News.utils.HttpClientUtil;
import com.zhuoxin.News.entity.NewsOfJuhe;


import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void getJson() {
        URL targetUrl = null;
        try {
            targetUrl = new URL("http://v.juhe.cn/toutiao/index?type=top&key=d728ab4e75e137c4f23aec12ed3ee6cd");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpClientUtil.getJson(targetUrl, new HttpClientUtil.OnJsonGetListener() {
            @Override
            public void jsonGetSuccess(String json) {
                System.out.print(json);
                //使用Gosn解析数据
                Gson gson = new Gson();
                //将解析后的数据保存在自定义类中
                NewsOfJuhe newsData = gson.fromJson(json, NewsOfJuhe.class);
                //从保存的数据中取出数据
                List<NewsOfJuhe.Data> dataList = newsData.getResult().getData();
                for (NewsOfJuhe.Data data : dataList) {
                    //只输出解析后的作者---标题
                    System.out.println(data.getAuthor_name() + "---" + data.getTitle());
                }
            }

            @Override
            public void jsonGetFail(int responsedCode) {
                System.out.print("获取数据失败,访问码为:" + responsedCode);
            }

            @Override
            public void jsonGetException(Exception e) {
                System.out.print("获取数据失败,异常原因为:" + e.getMessage());
            }
        });
    }
    @Test
    public void getGirls(){
        //2.创建Retrofit
        Retrofit retrofit =new Retrofit.Builder()
                .baseUrl("http://gank.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //3.获取接口对象
        GirlsInterface girlsInterface =retrofit.create(GirlsInterface.class);
        //4.加入队列进行数据解析
        Call<GirlsInfo> call =girlsInterface.getGirls();
        call.enqueue(new Callback<GirlsInfo>() {
            //成功的返回
            @Override
            public void onResponse(Call<GirlsInfo> call, Response<GirlsInfo> response) {
                GirlsInfo girlsInfo =response.body();
                List<GirlsInfo.ResultsBean> resultsBeanList =girlsInfo.getResults();
                for (int i =0;i<resultsBeanList.size();i++){
                    System.out.print(resultsBeanList.get(i).getUrl());
                }

            }
            //失败的返回
            @Override
            public void onFailure(Call<GirlsInfo> call, Throwable t) {
            System.out.print(t.getMessage());
            }
        });
    }
}