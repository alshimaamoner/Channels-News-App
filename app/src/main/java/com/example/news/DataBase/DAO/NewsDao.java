package com.example.news.DataBase.DAO;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.news.Model.NewsResponse.ArticlesItem;
import com.like.LikeButton;

import java.util.List;


@Dao
public interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void  insertNewsList(List<ArticlesItem> items);

    @Query("select * from ArticlesItem where sourceId=:sourceId")
    List<ArticlesItem>getNewsBySourceId(String sourceId);
    @Query("SELECT * FROM ArticlesItem WHERE title LIKE '%'|| :search ||'%'")
    public List<ArticlesItem> findUserWithName(String search);

    @Query("select * from ArticlesItem where Country=:country")
    List<ArticlesItem>getNewsByCountry(String country);

    @Query("select * from ArticlesItem where Country=:country and category=:category")
    List<ArticlesItem>getNewsByCategory(String country,String category);




}
