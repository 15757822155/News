package com.zhuoxin.News.utils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zhuoxin.News.entity.NewsInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/6.
 * 进行对News数据的增删改查
 */

public class DBUtil {
    //添加信息到数据库表中的方法
    public static void insert(SQLiteDatabase database, NewsInfo newsInfo) {
        //新建一条信息
        ContentValues contentValues = new ContentValues();
        contentValues.put("url", newsInfo.getUrl());
        contentValues.put("imageURL", newsInfo.getImageUrl());
        contentValues.put("largeImageURL", newsInfo.getLargeImageURL());
        contentValues.put("title", newsInfo.getTitle());
        contentValues.put("type", newsInfo.getType());
        //将数据添加到数据表中(表名,默认无参赋值,要插入的信息)
        database.insert("News", null, contentValues);
    }

    //删除数据库表中内容的方法
    public static void delete(SQLiteDatabase database, NewsInfo newsInfo) {
        //删除单条数据库
        database.delete("News", "url = ?", new String[]{newsInfo.getUrl()});
    }

    //查找单条数据的方法,主要用来判断是否重复添加
    public static boolean query(SQLiteDatabase database, NewsInfo newsInfo) {
        //查询单条数据
        Cursor cursor = database.query("News", null, "url = ?", new String[]{newsInfo.getUrl()}, null, null, null);
        //如果能找到就返回true
        if (cursor.moveToNext()) {
            //关闭curson
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    //查找所有数据的方法,主要用来显示数据
    public static List<NewsInfo> queryAll(SQLiteDatabase database) {
        List<NewsInfo> newsInfoList = new ArrayList<>();
        //查询数据库表中所有数据
        Cursor cursor = database.query("News", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String url = cursor.getString(cursor.getColumnIndex("url"));
            String imageURL = cursor.getString(cursor.getColumnIndex("imageURL"));
            String largeImageURL = cursor.getString(cursor.getColumnIndex("largeImageURL"));
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String type = cursor.getString(cursor.getColumnIndex("type"));
            //保存查到的数据
            NewsInfo newsInfo = new NewsInfo(url, imageURL, largeImageURL, title, type);
            //将查到的数据保存的list中
            newsInfoList.add(newsInfo);
        }
        cursor.close();
        return newsInfoList;
    }
}
