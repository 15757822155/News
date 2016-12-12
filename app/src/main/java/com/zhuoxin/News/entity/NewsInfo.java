package com.zhuoxin.News.entity;

/**
 * Created by Administrator on 2016/11/30.
 */

public class NewsInfo {
    String url;
    String imageUrl;
    String largeImageURL;
    String title;
    String type;

    public NewsInfo(String url, String imageUrl, String largeImageURL, String title, String type) {
        this.url = url;
        this.imageUrl = imageUrl;
        this.largeImageURL = largeImageURL;
        this.title = title;
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLargeImageURL() {
        return largeImageURL;
    }

    public void setLargeImageURL(String largeImageURL) {
        this.largeImageURL = largeImageURL;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
