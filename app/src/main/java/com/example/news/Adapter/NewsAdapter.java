package com.example.news.Adapter;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.news.Model.NewsResponse.ArticlesItem;
import com.example.news.R;


import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    List<ArticlesItem> newsList;
   // OnItemClickListener onItemClickListener;
   OnItemsClicksListener OnItemsClicksListener;


    public NewsAdapter(List<ArticlesItem> newsList) {
        this.newsList = newsList;
    }
    public void setOnItemsClicksListener(NewsAdapter.OnItemsClicksListener onItemsClicksListener) {
        OnItemsClicksListener = onItemsClicksListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_view_news, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        final ArticlesItem news = newsList.get(i);
        viewHolder.title.setText(news.getTitle());
        viewHolder.time.setText(news.getPublishedAt());
        if (news.getSource() != null) {
            viewHolder.sourceName.setText(news.getSource().getName());
        } else {
            viewHolder.sourceName.setText(news.getSourceName());

        }
       Glide.with(viewHolder.itemView).load(news.getUrlToImage()).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(viewHolder.imageView);
    if(OnItemsClicksListener!=null){
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnItemsClicksListener.onItemClicks(news);
            }
        });
    }
    }


    public void changeData(List<ArticlesItem> newsList){
        this.newsList=newsList;
        notifyDataSetChanged();
    }
    public void setfilter(List<ArticlesItem> newList)
    {
        newsList=new ArrayList<>();
        newsList.addAll(newList);
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        if(newsList==null) return 0;
        return newsList.size();
    }

    class  ViewHolder extends RecyclerView.ViewHolder{
        TextView title,sourceName,time;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.sourceTitle);
            sourceName=itemView.findViewById(R.id.sourceName);
            time=itemView.findViewById(R.id.newsTime);
            imageView=itemView.findViewById(R.id.imageView);

        }
    }


    public  interface  OnItemsClicksListener{
        void onItemClicks(ArticlesItem articlesItem);
    }

}

