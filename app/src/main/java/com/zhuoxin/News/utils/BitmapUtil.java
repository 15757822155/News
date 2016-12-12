package com.zhuoxin.News.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2016/12/2.
 * 主要是对图片的三级缓存
 */

public class BitmapUtil {
    //定义缓存空间大小
    private static final int LRU_SIZE = (int) (Runtime.getRuntime().maxMemory() / 8192);

    //一级缓存,LRUCache
    public static LruCache<String, Bitmap> bitmapLruCache = new LruCache<String, Bitmap>(LRU_SIZE) {
        @Override
        protected int sizeOf(String key, Bitmap value) {
            return value.getWidth() * value.getHeight();
        }
    };

    //二级文件缓存,将缓存文件存到手机中
    public static void setFileCache(Context context, String imageURL, Bitmap bitmap) {
        //获取手机中的缓存目录
        File cacheFile = context.getCacheDir();
        //先将图片的路径转成MD5格式
        String fileMD5 = MD5Util.getMD5(imageURL);
        //构建输出文件,创建了一个路径为(cacheFile, fileMD5 + ".PNG")的缓存文件
        File targetFile = new File(cacheFile, fileMD5 + ".PNG");
        //通过输出流构建
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(targetFile);
            //对获得的位图进行压缩(压缩格式,压缩质量,文件保存位置)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //二级文件缓存,获取手机中的文件缓存
    public static Bitmap getFileCache(Context context, String imageURL) {
        Bitmap bitmap = null;
        //获取手机中的缓存目录
        File cacheFile = context.getCacheDir();
        //先将图片的路径转成MD5格式
        String fileMD5 = MD5Util.getMD5(imageURL);
        //获取输入文件
        File targetFile = new File(cacheFile, fileMD5 + ".PNG");
        //解析二级缓存的路径
        bitmap = BitmapFactory.decodeFile(targetFile.getAbsolutePath());
        return bitmap;
    }

    //使用三级缓存
    public static void setBitmap(Context context, String imageURL, ImageView imageView) {
        Bitmap bitmap = null;
        String fileMD5 = MD5Util.getMD5(imageURL);
        //使用一级缓存
        bitmap = bitmapLruCache.get(fileMD5);
        if (bitmap == null) {
            //如果一级缓存为空,就使用二级缓存
            bitmap = getFileCache(context, imageURL);
            //如果二级缓存为空,就使用三级缓存
            if (bitmap == null) {
                try {
                    URL targetURL = new URL(imageURL);
                    HttpClientUtil.setImageURLToImageView(context, targetURL, imageView);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            } else {
                //如果使用二级缓存,就先将二级缓存的内容存到一级缓存中
                bitmapLruCache.put(fileMD5, bitmap);
                //获取对应图片的标记
                String tag = (String) imageView.getTag();
                //如果标记跟图片地址相同
                if (tag.equals(imageURL)) {
                    //设置imageView的bitmap
                    imageView.setImageBitmap(bitmap);
                }
            }
        } else {
            //获取对应图片的标记
            String tag = (String) imageView.getTag();
            //如果标记跟图片地址相同
            if (tag.equals(imageURL)) {
                //设置imageView的bitmap
                imageView.setImageBitmap(bitmap);
            }
        }
    }
}
