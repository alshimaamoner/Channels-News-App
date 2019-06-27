package com.example.news;

import android.app.Application;

import com.example.news.DataBase.DAO.NewsDao;
import com.example.news.DataBase.NewsDataBase;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        NewsDataBase.init(this);
    }
}
