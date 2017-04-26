package com.uisys.firebasechatapp;

/**
 * Created by Admin on 4/24/17.
 */

public class FilmNews {
    String news_header;
    String news_text;
    String imagePath;

    FilmNews()
    {

    }
    FilmNews(String news_header,String news_text, String imagePath)
    {
     this.news_header=news_header;
     this.news_text=news_text;
     this.imagePath=imagePath;


    }

    public String getNews_header() {
        return news_header;
    }

    public void setNews_header(String news_header) {
        this.news_header = news_header;
    }

    public String getNews_text() {
        return news_text;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setNews_text(String news_text) {
        this.news_text = news_text;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
