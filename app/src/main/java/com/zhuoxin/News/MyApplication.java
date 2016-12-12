package com.zhuoxin.News;

import android.app.Application;

import org.xutils.x;

/**
 * Created by Administrator on 2016/12/8.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //xutils框架设置
        x.Ext.init(this);
    }
}
