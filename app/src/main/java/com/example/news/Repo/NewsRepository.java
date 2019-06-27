package com.example.news.Repo;

import android.util.Log;

import com.example.news.API.ApiManager;
import com.example.news.DataBase.NewsDataBase;
import com.example.news.Model.NewsResponse.ArticlesItem;
import com.example.news.Model.NewsResponse.NewsResponse;
import com.example.news.Model.SourcesResponse.SourcesItem;
import com.example.news.Model.SourcesResponse.SourcesResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NewsRepository {
    String Lang;
    public static final String APIKEY = "2d4b610daa564404867322070c866a88";
//  public static List<LikeButton> likes=new ArrayList<>();


    public NewsRepository(String Lang) {
        this.Lang = Lang;
    }

    public void getNewsSources(final OnSourcesPreparedListener
                                       onSourcesPreparedListener) {
        ApiManager
                .getAPIs()
                .getNewsSources(APIKEY, Lang)
                .enqueue(new Callback<SourcesResponse>() {
                    @Override
                    public void onResponse(Call<SourcesResponse> call,
                                           Response<SourcesResponse> response) {
                        if (response.isSuccessful()) {
                            if (onSourcesPreparedListener != null) {
                                List<SourcesItem> list =
                                        response.body().getSources();
                                onSourcesPreparedListener.onSourcePrepared(list);
                                //open dB insert sources
                                InsertSourcesThread th = new InsertSourcesThread(list);
                                th.start();
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<SourcesResponse> call, Throwable t) {
                        RetrieveSourcesThread th =
                                new RetrieveSourcesThread(onSourcesPreparedListener);
                        th.start();

                    }
                });
    }

    // List<String> url=new ArrayList<>() ;
    List<ArticlesItem> newsList = new ArrayList<>();

    public void getNewsBySourceId(final String sourceId,
                                  final OnNewsPreparedListener onNewsPreparedListener) {
        ApiManager.getAPIs()
                .getNewsBySourceId(APIKEY, Lang, sourceId)
                .enqueue(new Callback<NewsResponse>() {
                    @Override
                    public void onResponse(Call<NewsResponse> call,
                                           Response<NewsResponse> response) {
                        if (response.isSuccessful()) {
                            newsList = response.body().getArticles();

                            if (onNewsPreparedListener != null) {
                                onNewsPreparedListener.onNewsPrepared(newsList);
                                InsertArticlesThread th = new InsertArticlesThread(newsList);
                                th.start();

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<NewsResponse> call, Throwable t) {
                        RetrieveNewsBySourceIdThread th =
                                new RetrieveNewsBySourceIdThread(sourceId, onNewsPreparedListener);
                        th.start();

                    }
                });

    }

    /*  public  void getLikes(String url,OnLikesPreparedListener onLikesPreparedListener){
           RetrieveLikesThread th =
                   new RetrieveLikesThread(url,onLikesPreparedListener);
           th.start();

       }

       public  void setLikes(LikesResponse likes){
           InsertLikesThread th =
                   new InsertLikesThread(likes);
           th.start();

       }
    */
    public interface OnSourcesPreparedListener {
        void onSourcePrepared(List<SourcesItem> sources);
    }

    public interface OnNewsPreparedListener {
        void onNewsPrepared(List<ArticlesItem> newsList);
    }

    /*  public interface  OnLikesPreparedListener{
          void  OnLikesPrepared(String url,LikesResponse likes);
      }

  */
    class InsertSourcesThread extends Thread {

        List<SourcesItem> sourcesList;

        public InsertSourcesThread(List<SourcesItem> sourcesItems) {
            this.sourcesList = sourcesItems;
        }

        @Override
        public void run() {
            NewsDataBase.getInstance()
                    .sourcesDao()
                    .insertSourcesList(sourcesList);
        }
    }

    /* static class InsertLikesThread extends Thread{
          LikesResponse likesResponses;
          public InsertLikesThread(LikesResponse likesResponses){
              this.likesResponses = likesResponses;

          }
          @Override
          public void run() {
              NewsDataBase.getInstance()
                      .LikesDao()
                      .insertLikeList(likesResponses);
          }
      }
  */
    class RetrieveSourcesThread extends Thread {
        OnSourcesPreparedListener onSourcesPreparedListener;

        RetrieveSourcesThread(OnSourcesPreparedListener onSourcesPreparedListener) {
            this.onSourcesPreparedListener = onSourcesPreparedListener;
        }

        @Override
        public void run() {
            List<SourcesItem> sources = NewsDataBase.getInstance()
                    .sourcesDao()
                    .getAllSources();
            onSourcesPreparedListener.onSourcePrepared(sources);
        }
    }



    class InsertArticlesThread extends Thread {

        List<ArticlesItem> newsList;

        public InsertArticlesThread(List<ArticlesItem> list) {
            this.newsList = list;
        }

        @Override
        public void run() {
            for (ArticlesItem item : newsList) {
                item.setSourceId(item.getSource().getId());
                item.setSourceName(item.getSource().getName());

            }


            NewsDataBase.getInstance()
                    .newsDao()
                    .insertNewsList(newsList);

        }
    }

    class InsertTopicsThread extends Thread {

        List<ArticlesItem> newsList;

        public InsertTopicsThread(List<ArticlesItem> list) {
            this.newsList = list;
        }

        @Override
        public void run() {
            for (ArticlesItem item : newsList) {
                item.setSourceId(item.getSourceId());
                item.setSourceName(item.getSource().getName());
            }


            NewsDataBase.getInstance()
                    .newsDao()
                    .insertNewsList(newsList);

        }
    }


    class RetrieveNewsBySourceIdThread extends Thread {

        String sourceId;

        OnNewsPreparedListener onNewsPreparedListener;

        public RetrieveNewsBySourceIdThread(String sourceId, OnNewsPreparedListener listener) {
            this.sourceId = sourceId;
            onNewsPreparedListener = listener;


        }

        @Override
        public void run() {

            List<ArticlesItem> newsList = NewsDataBase.getInstance().newsDao()
                    .getNewsBySourceId(sourceId);
            Log.e("list size", newsList.size() + "");
            onNewsPreparedListener.onNewsPrepared(newsList);
        }
    }
    class RetrieveTopics extends Thread {

        String sourceId;
        String Country;
        OnNewsPreparedListener onNewsPreparedListener;

        public RetrieveTopics(String sourceId, OnNewsPreparedListener listener,String Country) {
            this.sourceId = sourceId;
            onNewsPreparedListener = listener;
            this.Country=Country;

        }

        @Override
        public void run() {

            List<ArticlesItem> newsList = NewsDataBase.getInstance().newsDao()
                    .getNewsByCountry(Country);
            Log.e("list size", newsList.size() + "");
            onNewsPreparedListener.onNewsPrepared(newsList);
        }
    }
    List<ArticlesItem> topicsList = new ArrayList<>();

    public void getTopics(final String Country,final String sourceId,
                                  final OnNewsPreparedListener onNewsPreparedListener) {
        ApiManager.getAPIs()
                .getTopics(Country,APIKEY, sourceId)
                .enqueue(new Callback<NewsResponse>() {
                    @Override
                    public void onResponse(Call<NewsResponse> call,
                                           Response<NewsResponse> response) {
                        if (response.isSuccessful()) {
                            topicsList = response.body().getArticles();
                            for (int i=0;i<topicsList.size();i++) {
                                 topicsList.get(i).setCountry(Country);
                            }
                            if (onNewsPreparedListener != null) {
                                onNewsPreparedListener.onNewsPrepared(topicsList);
                                InsertTopicsThread th = new InsertTopicsThread(topicsList);
                                th.start();

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<NewsResponse> call, Throwable t) {
                        RetrieveTopics th =
                                new RetrieveTopics(sourceId, onNewsPreparedListener,Country);
                        th.start();

                    }
                });

    }
    class RetrieveCategory extends Thread {

        String sourceId;
        String Country;
        String category;
        OnNewsPreparedListener onNewsPreparedListener;

        public RetrieveCategory(String sourceId,String category, OnNewsPreparedListener listener,String Country) {
            this.sourceId = sourceId;
            onNewsPreparedListener = listener;
            this.Country=Country;
            this.category=category;

        }

        @Override
        public void run() {

            List<ArticlesItem> newsList = NewsDataBase.getInstance().newsDao()
                    .getNewsByCategory(Country,category);
            Log.e("list size", newsList.size() + "");
            onNewsPreparedListener.onNewsPrepared(newsList);
        }
    }
    List<ArticlesItem> categoryList = new ArrayList<>();

    public void getCategory(final String Country,final String category,final String sourceId,
                          final OnNewsPreparedListener onNewsPreparedListener) {
        ApiManager.getAPIs()
                .getCategory(Country,category,APIKEY, sourceId)
                .enqueue(new Callback<NewsResponse>() {
                    @Override
                    public void onResponse(Call<NewsResponse> call,
                                           Response<NewsResponse> response) {
                        if (response.isSuccessful()) {
                            categoryList = response.body().getArticles();
                            for (int i=0;i<categoryList.size();i++) {
                                categoryList.get(i).setCountry(Country);
                                categoryList.get(i).setCategory(category);

                            }
                            if (onNewsPreparedListener != null) {
                                onNewsPreparedListener.onNewsPrepared(categoryList);
                                InsertTopicsThread th = new InsertTopicsThread(categoryList);
                                th.start();

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<NewsResponse> call, Throwable t) {
                        RetrieveCategory th =
                                new RetrieveCategory(sourceId,category, onNewsPreparedListener,Country);
                        th.start();

                    }
                });

    }

}