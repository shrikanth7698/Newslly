package com.shrikanthravi.newslly;


import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.InputMethodManager;

import com.shrikanthravi.newslly.data.model.Article;
import com.shrikanthravi.newslly.data.model.NewsApiResponse;
import com.shrikanthravi.newslly.data.model.remote.APIService;
import com.shrikanthravi.newslly.data.model.remote.GlobalData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class TopHeadlinesFragment extends Fragment {


    public TopHeadlinesFragment() {
        // Required empty public constructor
    }
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView newsRV;
    NewsAdapter newsAdapter;
    List<Article> newsList;
    int flag=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_top_headlines, container, false);

        Typeface regular = Typeface.createFromAsset(getActivity().getAssets(), "fonts/product_san_regular.ttf");
        FontChanger fontChanger = new FontChanger(regular);
        fontChanger.replaceFonts((ViewGroup)view);
        swipeRefreshLayout = view.findViewById(R.id.TopHeadlinesSwiperefreshlayout);
        newsRV =(RecyclerView) view.findViewById(R.id.newsRV);
        newsList = new ArrayList<>();
        newsAdapter = new NewsAdapter(newsList,getActivity().getApplicationContext());

        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        newsRV.setLayoutManager(horizontalLayoutManager);

        ScaleInAnimationAdapter animationAdapter = new ScaleInAnimationAdapter(newsAdapter);
        animationAdapter.setFirstOnly(false);
        animationAdapter.setDuration(300);
        newsRV.setAdapter(animationAdapter);
        if(isOnline()) {
            requestHeadlines();
        }
        else{
            final Handler handler = new Handler();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(isOnline()){
                        flag=flag+1;
                        if(flag==1){
                            requestHeadlines();
                        }
                        else{
                            handler.removeCallbacks(this);
                        }
                    }
                }
            });
        }
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                flag=0;
                if(isOnline()) {
                        requestHeadlines();
                    }
                else{
                        final Handler handler = new Handler();
                        getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(isOnline()){
                                flag=flag+1;
                                if(flag==1){
                                    requestHeadlines();
                                }
                                else{
                                    handler.removeCallbacks(this);
                                }
                            }
                        }
                });
            }
            }
        });
        return view;
    }

    public void requestHeadlines(){
    swipeRefreshLayout.setRefreshing(true);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = null;
        retrofit = new Retrofit.Builder()
                .baseUrl("https://newsapi.org/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        APIService mAPIService1;
        mAPIService1 = retrofit.create(APIService.class);
        Call call1 = mAPIService1.NewsApiCall("in", GlobalData.ApiKeyTesting);
        call1.enqueue(new Callback<NewsApiResponse>() {
            @Override
            public void onResponse(Call<NewsApiResponse> call, Response<NewsApiResponse> response) {

                System.out.println("news response code"+response.code()+" "+response.message());

                if (response.isSuccessful()) {
                    try {
                    newsList.clear();

                    newsList.addAll(response.body().getArticles());
                    swipeRefreshLayout.setRefreshing(false);

                    try {
                        Collections.sort(newsList, new Comparator<Article>() {
                            public int compare(Article m1, Article m2) {
                                return m2.getDate().compareTo(m1.getDate());
                            }
                        });
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    newsAdapter.notifyDataSetChanged();



                    }

                    catch (Exception e) {
                        if (e.getMessage() != null) {

                        }
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<NewsApiResponse> call, Throwable t) {
                //Log.e(TAG, "Unable to submit post to API.");
                System.out.println("news request error");

            }
        });
    }

    public boolean isOnline() {
        boolean connected = false;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getApplicationContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() &&
                    networkInfo.isConnected();
            return connected;

        } catch (Exception e) {
            System.out.println("CheckConnectivity Exception: " + e.getMessage());
            Log.v("connectivity", e.toString());
        }
        return connected;
    }
    @Override
    public void setUserVisibleHint(boolean isVisibletoUser){
        if(isVisibletoUser){


                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);


        }
    }

}
