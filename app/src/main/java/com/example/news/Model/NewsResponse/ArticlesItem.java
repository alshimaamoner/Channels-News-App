package com.example.news.Model.NewsResponse;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.news.Model.SourcesResponse.SourcesItem;
import com.google.gson.annotations.SerializedName;
import com.like.LikeButton;


import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = SourcesItem.class,
        parentColumns = "id",
        childColumns = "sourceId",
        onDelete = CASCADE))
public class ArticlesItem{

	@SerializedName("publishedAt")
	private String publishedAt;

	@SerializedName("author")
	private String author;

	@SerializedName("urlToImage")
	private String urlToImage;

	@SerializedName("description")
	private String description;

	@Ignore
	@SerializedName("source")
	private SourcesItem source;

	String sourceId;

	String sourceName;


	@SerializedName("title")
	private String title;

	@NonNull
	@PrimaryKey
	@SerializedName("url")
	private String url;

	@SerializedName("content")
	private String content;

	String Country;



	String category;

	public void setPublishedAt(String publishedAt){
		this.publishedAt = publishedAt;
	}

	public String getPublishedAt(){
		return publishedAt;
	}

	public void setAuthor(String author){
		this.author = author;
	}

	public String getAuthor(){
		return author;
	}

	public void setUrlToImage(String urlToImage){
		this.urlToImage = urlToImage;
	}

	public String getUrlToImage(){
		return urlToImage;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}

	public void setSource(SourcesItem source){
		this.source = source;
	}

	public SourcesItem getSource(){
		return source;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setUrl(String url){
		this.url = url;
	}

	public String getUrl(){
		return url;
	}

	public void setContent(String content){
		this.content = content;
	}

	public String getContent(){
		return content;
	}

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }
	public String getCountry() {
		return Country;
	}

	public void setCountry(String Country) {
		this.Country = Country;
	}
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Override
 	public String toString(){
		return 
			"ArticlesItem{" + 
			"publishedAt = '" + publishedAt + '\'' + 
			",author = '" + author + '\'' + 
			",urlToImage = '" + urlToImage + '\'' + 
			",description = '" + description + '\'' + 
			",source = '" + source + '\'' + 
			",title = '" + title + '\'' + 
			",url = '" + url + '\'' + 
			",content = '" + content + '\'' + 
			"}";
		}
}