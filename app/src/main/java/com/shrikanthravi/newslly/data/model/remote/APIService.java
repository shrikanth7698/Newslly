package com.shrikanthravi.newslly.data.model.remote;

import com.shrikanthravi.newslly.data.model.NewsApiResponse;
import com.shrikanthravi.newslly.data.model.Source_list;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by shrikanthravi on 13/12/17.
 */

public interface APIService {


    @GET("top-headlines")
    Call<NewsApiResponse> NewsApiCall(
                                @Query("country") String country,
                                @Query("apikey") String apikey);


    @GET("top-headlines")
    Call<NewsApiResponse> NewsApiCallCategory(
                                @Query("category") String category,
                                @Query("language") String language,
                                @Query("country") String country,
                                @Query("apikey") String apikey);

    @GET("sources")
    Call<Source_list> NewsApiCallSource(
                                @Query("apikey") String apikey);


    @GET("everything")
    Call<NewsApiResponse> SearchArticles(
                                @Query("q") String q,
                                @Query("language") String language,
                                @Query("apikey") String apikey);

    @GET("search")
    Call<ResponseBody> SuggestionCall(
                                @Query("output") String output,
                                @Query("hl") String hl,
                                @Query("q") String q
    );
}
