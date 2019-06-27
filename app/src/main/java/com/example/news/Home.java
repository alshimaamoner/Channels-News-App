package com.example.news;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.news.Adapter.NewsAdapter;
import com.example.news.Base.BaseActivity;
import com.example.news.DataBase.NewsDataBase;
import com.example.news.Model.NewsResponse.ArticlesItem;
import com.example.news.Model.SourcesResponse.SourcesItem;
import com.example.news.Repo.NewsRepository;
import com.like.LikeButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Home extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TabLayout mChannelLayout;
    private RecyclerView mNewsRecyler;
    NewsRepository newsRepositry;
    SearchView searchView;
    List<ArticlesItem> news;
   // List<LikesResponse> like;
    NewsAdapter newsAdapter;
    RecyclerView.LayoutManager layoutManager;
  //public static  List<LikesResponse> buttons=new ArrayList<>();
    String Lang = Locale.getDefault().getLanguage();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
          toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        mChannelLayout = findViewById(R.id.channelLayout);
        mNewsRecyler = findViewById(R.id.newsRecyler);
        layoutManager=new LinearLayoutManager(activity);
        newsAdapter=new NewsAdapter(null);
        mNewsRecyler.setAdapter(newsAdapter);
        mNewsRecyler.setLayoutManager(layoutManager);
        newsRepositry = new NewsRepository(Lang);
        newsRepositry.getNewsSources(onSourcePreparedListener);
        newsAdapter.setOnItemsClicksListener(new NewsAdapter.OnItemsClicksListener() {
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
    }
    NewsRepository.OnSourcesPreparedListener onSourcePreparedListener = new NewsRepository.OnSourcesPreparedListener(){
        @Override
        public void onSourcePrepared(final List<SourcesItem> sourcesItems) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showSourceInTabLayout(sourcesItems);
                }
            });

        }
    };

static String id;
    public void showSourceInTabLayout(List<SourcesItem> sourcesItems) {
        if (sourcesItems == null || sourcesItems.size()==0) return;
        for (SourcesItem sourcesItem : sourcesItems) {
            TabLayout.Tab tab = mChannelLayout.newTab();
            tab.setText(sourcesItem.getName());
            tab.setTag(sourcesItem);
            mChannelLayout.addTab(tab);

        }
        mChannelLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
             SourcesItem source = ((SourcesItem) tab.getTag());
             id=source.getId();
                newsRepositry.getNewsBySourceId(source.getId(), onNewsPreparedListener);


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                SourcesItem source = ((SourcesItem)tab.getTag());

                newsRepositry.getNewsBySourceId(source.getId(), onNewsPreparedListener);



            }
        });
       mChannelLayout.getTabAt(0).select();
    }

static List<ArticlesItem> article=null;

    NewsRepository.OnNewsPreparedListener onNewsPreparedListener=new NewsRepository.OnNewsPreparedListener()

    {
        @Override
        public void onNewsPrepared (final List < ArticlesItem > newsList) {

            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    article=new ArrayList<>(newsList);
                    newsAdapter.changeData(newsList);

                }
            });

    }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        final MenuItem myActionMenuItem = menu.findItem(R.id.search);
        searchView = (SearchView) myActionMenuItem.getActionView();
        changeSearchViewTextColor(searchView);
        ((EditText) searchView.findViewById(
                android.support.v7.appcompat.R.id.search_src_text)).
                setHintTextColor(getResources().getColor(R.color.colorPrimary));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override            public boolean onQueryTextSubmit(String query) {
                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                myActionMenuItem.collapseActionView();
                return false;
    }

            @Override
            public boolean onQueryTextChange(String newText) {
                final  List<ArticlesItem> filtermodelist=filter(article,newText);
                newsAdapter.setfilter(filtermodelist);
                return true;
            }
        });
        return true;
    }
    private List<ArticlesItem> filter(List<ArticlesItem> pl,String query)
    {
        query=query.toLowerCase();
        final List<ArticlesItem> filteredModeList=new ArrayList<>();
        for (ArticlesItem model:pl)
        {
            final String text=model.getTitle().toLowerCase();
            if (text.contains(query))
            {
                filteredModeList.add(model);
            }
        }
        return filteredModeList;
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void changeSearchViewTextColor(View view) {
        if (view != null) {
            if (view instanceof TextView) {
                ((TextView) view).setTextColor(Color.WHITE);
                return;
            } else if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    changeSearchViewTextColor(viewGroup.getChildAt(i));
                }
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        Intent intent = new Intent(this, Topics.class);
        Intent intent2 = new Intent(this, Category.class);
        if (id == R.id.usa) {
            intent.putExtra("countr", "us");
            intent.putExtra("country", "United States");
            startActivity(intent);
            // Handle the camera action
        } else if (id == R.id.eg) {
            intent.putExtra("countr", "eg");
            intent.putExtra("country", "Egypt");
            startActivity(intent);
        } else if (id == R.id.fr) {
            intent.putExtra("countr", "fr");
            intent.putExtra("country", "France");
            startActivity(intent);
        } else if (id == R.id.kr) {
            intent.putExtra("countr", "kr");
            intent.putExtra("country", "Korea");
            startActivity(intent);
        } else if (id == R.id.sport) {
            intent2.putExtra("category", "sports");
            intent2.putExtra("name", "Sports");
            startActivity(intent2);
        } else if (id == R.id.tech) {
            intent2.putExtra("category", "technology");
            intent2.putExtra("name", "Technology");
            startActivity(intent2);

        }else if(id ==R.id.health){
            intent2.putExtra("category", "health");
            intent2.putExtra("name", "Health");
            startActivity(intent2);

        } else if(id ==R.id.business){
            intent2.putExtra("category", "business");
            intent2.putExtra("name", "Business");
            startActivity(intent2);

        }else if(id ==R.id.entertainment){
            intent2.putExtra("category", "entertainment");
            intent2.putExtra("name", "Entertainment");
            startActivity(intent2);

        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
