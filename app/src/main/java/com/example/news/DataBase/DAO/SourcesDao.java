package com.example.news.DataBase.DAO;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.news.Model.SourcesResponse.SourcesItem;

import java.util.List;


@Dao
public interface SourcesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void  insertSourcesList(List<SourcesItem> items);

    @Query("select * from SourcesItem")
    List<SourcesItem>getAllSources();

}
