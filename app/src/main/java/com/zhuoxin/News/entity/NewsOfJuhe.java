package com.zhuoxin.News.entity;

import java.util.List;

/**
 * Created by Administrator on 2016/11/30.
 */

public class NewsOfJuhe {
    String reason;
    Result result;

    public String getReason() {
        return reason;
    }

    public Result getResult() {
        return result;
    }

    public class Result {
        int stat;
        List<Data> data;

        public int getStat() {
            return stat;
        }

        public List<Data> getData() {
            return data;
        }
    }

    public class Data {
        String title;
        String date;
        String author_name;
        String thumbnail_pic_s;
        String thumbnail_pic_s02;
        String thumbnail_pic_s03;
        String url;
        String uniquekey;
        String type;
        String realtype;
        String category;

        public String getCategory() {
            return category;
        }

        public String getTitle() {
            return title;
        }

        public String getDate() {
            return date;
        }

        public String getAuthor_name() {
            return author_name;
        }

        public String getThumbnail_pic_s() {
            return thumbnail_pic_s;
        }

        public String getThumbnail_pic_s02() {
            return thumbnail_pic_s02;
        }

        public String getThumbnail_pic_s03() {
            return thumbnail_pic_s03;
        }

        public String getUrl() {
            return url;
        }

        public String getUniquekey() {
            return uniquekey;
        }

        public String getType() {
            return type;
        }

        public String getRealtype() {
            return realtype;
        }
    }
}
