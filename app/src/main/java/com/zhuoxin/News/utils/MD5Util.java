package com.zhuoxin.News.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 2016/12/2.
 * 这个类主要将任何文件转化成MD5
 */

public class MD5Util {
    public static String getMD5(String message) {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            //获取数据指纹类型
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            //获取信息到数据指纹中
            messageDigest.update(message.getBytes("UTF-8"));
            //取出数据
            byte data[] = messageDigest.digest();
            //for循环对data中的数据进行16进制的转化
            for (int i = 0; i < data.length; i++) {
                //把对应数据转化成16进制(0xff为与运算)
                String str = Integer.toHexString(0xff & data[i]);
                if (str.length() == 1) {
                    //如果str中的数据只有1位(如B),就在它前面加一个0
                    stringBuffer.append("0").append(str);
                } else {
                    stringBuffer.append(str);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuffer.toString();
    }
}
