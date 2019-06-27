package com.example.news.DataBase;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.news.DataBase.DAO.NewsDao;
import com.example.news.DataBase.DAO.SourcesDao;
import com.example.news.Model.NewsResponse.ArticlesItem;
import com.example.news.Model.SourcesResponse.SourcesItem;



@Database(entities = {SourcesItem.class, ArticlesItem.class},
        version = 1,exportSchema = false)
public abstract class NewsDataBase extends RoomDatabase {

    final static String DBName  = "newsDataBase";
    private static NewsDataBase newsDataBase;
    public abstract SourcesDao sourcesDao();
    public abstract NewsDao newsDao();
    public static NewsDataBase init(Context context){
        if(newsDataBase==null){
            newsDataBase = Room.databaseBuilder(context,NewsDataBase.class,DBName)
                    .fallbackToDestructiveMigration()
                    //.allowMainThreadQueries()
                    .build();

        }
        return newsDataBase;
    }

    public static NewsDataBase getInstance(){
        return newsDataBase;
    }


}
