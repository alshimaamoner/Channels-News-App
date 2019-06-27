package com.example.news.API;




import com.example.news.Model.NewsResponse.NewsResponse;
import com.example.news.Model.SourcesResponse.SourcesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface Services {

    @GET("sources")
    Call<SourcesResponse> getNewsSources(@Query("apiKey") String apikey,
                                         @Query("language") String language);


    @GET("everything")
    Call<NewsResponse> getNewsBySourceId(@Query("apiKey") String apikey,
                                         @Query("language") String language,
                                         @Query("sources") String sources);
    @GET("top-headlines")
    Call<NewsResponse> getTopics(@Query("country") String country,@Query("apiKey") String apikey,@Query("sources") String sources);

    @GET("top-headlines")
    Call<NewsResponse> getCategory(@Query("country") String country,@Query("category") String category,@Query("apiKey") String apikey,@Query("sources") String sources);
}
