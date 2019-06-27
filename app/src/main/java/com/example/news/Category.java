package com.example.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.news.Adapter.TopicsAdapter;
import com.example.news.Base.BaseActivity;
import com.example.news.Model.NewsResponse.ArticlesItem;
import com.example.news.Repo.NewsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Category extends BaseActivity {
    private TextView mChannelss;
    NewsRepository newsRepositry;
    private RecyclerView mTopicsRecyler;
    private DrawerLayout mDrawerLayout;
    List<ArticlesItem> news;
    TopicsAdapter newsAdapter;
    RecyclerView.LayoutManager layoutManager;
    String Lang = Locale.getDefault().getLanguage();
    String Country="us";
    ImageButton back;
    String category;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        init();
        newsRepositry = new NewsRepository(Lang);
        mChannelss.setText(getIntent().getStringExtra("name"));
        layoutManager=new LinearLayoutManager(activity);
        newsAdapter=new TopicsAdapter(news);
        mTopicsRecyler.setAdapter(newsAdapter);
        mTopicsRecyler.setLayoutManager(layoutManager);
        category=getIntent().getStringExtra("category");
        newsRepositry.getCategory(Country,category,null,onNewsPreparedListener);
        newsAdapter.setOnItemsClicksListener(new TopicsAdapter.OnItemsClicksListener() {
            @Override
            public void onItemClicks(ArticlesItem articlesItem) {
                Intent intent=new Intent(getBaseContext(),StoriesActivity.class);
                intent.putExtra("url",articlesItem.getUrlToImage());
                intent.putExtra("title",articlesItem.getTitle());
                intent.putExtra("date",articlesItem.getPublishedAt());
                intent.putExtra("author",articlesItem.getAuthor());
                intent.putExtra("desc",articlesItem.getDescription());
                intent.putExtra("content",articlesItem.getContent());
                intent.putExtra("link",articlesItem.getUrl());
                startActivityForResult(intent,0);
            }
        });
        back.setOnClickListener(backListener);
    }

    View.OnClickListener backListener=  new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    };
    NewsRepository.OnNewsPreparedListener onNewsPreparedListener = new NewsRepository.OnNewsPreparedListener() {
        @Override
        public void onNewsPrepared(final List<ArticlesItem> newsList) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    news=new ArrayList<>(newsList);
                    newsAdapter.changeData(newsList);                }
            });
        }
    };



    private void init() {
        mChannelss = findViewById(R.id.channelss);
        mTopicsRecyler = findViewById(R.id.topicsRecyler);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        back=findViewById(R.id.back);
    }

}
