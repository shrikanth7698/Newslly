package com.shrikanthravi.newslly.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by shrikanthravi on 14/12/17.
 */

public class NewsApiResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("articles")
    @Expose

    private List<Article> articles = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

}
