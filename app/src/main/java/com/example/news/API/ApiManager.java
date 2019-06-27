package com.example.news.API;

import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiManager  {

    private static Retrofit retrofitInstance;

    private static Retrofit getInstance(){
        if(retrofitInstance==null){

            HttpLoggingInterceptor httpLoggingInterceptor
                    =new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    Log.e("api",message);
                }
            });

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(httpLoggingInterceptor)
                    .build();

            retrofitInstance = new Retrofit.Builder()
                    .baseUrl("https://newsapi.org/v2/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();


        }
        return retrofitInstance;

    }

    public static Services getAPIs(){
      return   getInstance()
                .create(Services.class);
    }



}
