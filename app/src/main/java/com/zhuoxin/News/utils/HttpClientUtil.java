package com.zhuoxin.News.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 主要用于对网络数据的解析,网络访问工具类,使用原生的HttpURLConnection
 * Created by Administrator on 2016/11/30.
 * 第三方的网络访问框架,XUtils,OKHttp,Retrofit
 */

public class HttpClientUtil {
    /**
     * 定义了一个接口
     */
    public interface OnJsonGetListener {
        public void jsonGetSuccess(String json);

        public void jsonGetFail(int responsedCode);

        public void jsonGetException(Exception e);
    }

    /**
     * 定义一个接口对象
     */
    public static OnJsonGetListener listener;

    /**
     * 获取网络收据json的方法
     *
     * @param targetURL
     * @param listener
     */
    public static void getJson(URL targetURL, OnJsonGetListener listener) {
        HttpURLConnection httpURLConnection = null;
        //为了效率和优化,用StringBuffer
        StringBuffer json = new StringBuffer();
        try {
            //打开目标链接
            httpURLConnection = (HttpURLConnection) targetURL.openConnection();
            //设置连接超时
            httpURLConnection.setConnectTimeout(5000);
            //设置读取超时
            httpURLConnection.setReadTimeout(5000);

            //开始连接//请求方式(GET和POST)默认为GET
            //httpURLConnection.setRequestMethod("POST");
            //设置输出权限
            //httpURLConnection.setDoOutput(false);
            httpURLConnection.connect();
            //获取响应码
            if (httpURLConnection.getResponseCode() == 200) {
                //使用输入流读取链接数据
                InputStream inputStream = httpURLConnection.getInputStream();
                //转化为字符输入流
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                //缓冲流
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String msg = null;
                //如果读下一行的数据不为空
                while ((msg = bufferedReader.readLine()) != null) {
                    //将读到的msg全部保存到json
                    json.append(msg);
                }
                //关闭相关的流
                bufferedReader.close();
                inputStreamReader.close();
                inputStream.close();
                //调用获得json成功的接口方法
                listener.jsonGetSuccess(json.toString());
            } else {
                //调用json失败的接口方法
                listener.jsonGetFail(httpURLConnection.getResponseCode());
            }
        } catch (IOException e) {
            //调用异常的接口方法
            listener.jsonGetException(e);
        } finally {
            //最后关闭连接
            httpURLConnection.disconnect();
        }
    }

    /**
     * 这个方法主要从网络获取图片,并设置到指定的ImageView
     *
     * @param imageUrl  图片网址
     * @param imageView 图片地址
     */
    public static void setImageURLToImageView(final Context context, final URL imageUrl, final ImageView imageView) {
        final String fileMD5 = MD5Util.getMD5(imageUrl.toString());
        //建立异步任务
        AsyncTask<URL, Void, Bitmap> asyncTask = new AsyncTask<URL, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(URL... urls) {
                Bitmap bitmap = null;
                //从可变参数列表中获取url
                URL url = urls[0];
                try {
                    //用原生网址访问打开这个URL地址
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    //连接超时
                    httpURLConnection.setConnectTimeout(5000);
                    //读取超时
                    httpURLConnection.setReadTimeout(5000);
                    //开始连接
                    httpURLConnection.connect();
                    if (httpURLConnection.getResponseCode() == 200) {
                        InputStream inputStream = httpURLConnection.getInputStream();
                        //从输入流直接解析位图
                        bitmap = BitmapFactory.decodeStream(inputStream);
                        inputStream.close();
                    }
                    //关闭连接
                    httpURLConnection.disconnect();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                //返回获得的bitmap
                return bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                //获取adapter中设定的标记
                String tag = (String) imageView.getTag();
                //如果标记存的地址和图片的地址相同,就做以下内容
                if (tag.equals(imageUrl.toString())) {
                    //放一级缓存
                    BitmapUtil.bitmapLruCache.put(fileMD5, bitmap);
                    //放二级缓存
                    BitmapUtil.setFileCache(context, imageUrl.toString(), bitmap);
                    //直接设置主线程的imageView
                    imageView.setImageBitmap(bitmap);
                }
            }

        };
        //开启这个异步操作
        asyncTask.execute(imageUrl);
    }
}
