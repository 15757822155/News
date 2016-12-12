package com.zhuoxin.News.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/12/6.
 */

public class MyDBOpenHelper extends SQLiteOpenHelper {
    private static final String CREATE_TABLE_NEWS = "create table News(" +
            "url text," +
            "imageURL text," +
            "largeImageURL text," +
            "title text," +
            "type text)";

    public MyDBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //执行一条SQL语句
        sqLiteDatabase.execSQL(CREATE_TABLE_NEWS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
