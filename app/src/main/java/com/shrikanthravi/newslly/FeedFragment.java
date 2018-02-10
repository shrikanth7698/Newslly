package com.shrikanthravi.newslly;


import android.content.Context;
import android.graphics.Movie;
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
import android.widget.Toast;

import com.shrikanthravi.newslly.data.model.Article;
import com.shrikanthravi.newslly.data.model.NewsApiResponse;
import com.shrikanthravi.newslly.data.model.remote.APIService;
import com.shrikanthravi.newslly.data.model.remote.GlobalData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.TimeZone;
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
public class FeedFragment extends Fragment {


    public FeedFragment() {
        // Required empty public constructor
    }


    RecyclerView newsRV;
    NewsAdapter newsAdapter;
    List<Article> newsList;
    List<Article> copy;
    SimpleDateFormat simpleDateFormat,simpleDateFormat1;
    Calendar c;
    Date d1,d2;
    int flag=0;
    List<String> categoryList;
    SwipeRefreshLayout swipeRefreshLayout;
    int lastFlag=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        Typeface regular = Typeface.createFromAsset(getActivity().getAssets(), "fonts/product_san_regular.ttf");
        FontChanger fontChanger = new FontChanger(regular);
        fontChanger.replaceFonts((ViewGroup)view);
        swipeRefreshLayout = view.findViewById(R.id.feedSwipeRefreshLayout);
        newsRV =(RecyclerView) view.findViewById(R.id.feedRV);
        newsList = new ArrayList<>();
        newsAdapter = new NewsAdapter(newsList,getActivity().getApplicationContext());

        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        newsRV.setLayoutManager(horizontalLayoutManager);
        newsRV.setNestedScrollingEnabled(true);

        ScaleInAnimationAdapter animationAdapter = new ScaleInAnimationAdapter(newsAdapter);
        animationAdapter.setFirstOnly(false);
        animationAdapter.setDuration(300);
        newsRV.setAdapter(animationAdapter);
        categoryList = new ArrayList<>();
        String[] cats = PreferenceConnector.readString(getActivity().getApplicationContext(),PreferenceConnector.Categories,"").split("#");
        for(int i=0;i<cats.length;i++){
            System.out.println("feed fragment cats "+cats[i]);
            categoryList.add(cats[i]);
        }
        if(isOnline()) {
            for (int i = 0; i < categoryList.size(); i++) {
                String[] lang = {"us","in"};
                for(int j=0;j<2;j++) {
                    requestHeadlines(categoryList.get(i), i,lang[j]);
                }

            }
        }
        else{

            Toast.makeText(getActivity().getApplicationContext(),"No internet connection",Toast.LENGTH_LONG).show();
            final Handler handler = new Handler();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(isOnline()){
                        flag=flag+1;
                        if(flag==1){
                            flag=flag+12;
                            System.out.println("handler check ");
                            for (int i = 0; i < categoryList.size(); i++) {
                                String[] lang = {"us","in"};
                                for(int j=0;j<2;j++) {
                                    requestHeadlines(categoryList.get(i), i,lang[j]);
                                }

                            }
                        }
                        else{
                            handler.removeCallbacks(this);
                        }

                    }
                    handler.postDelayed(this,2000);
                }
            });
        }
        simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        simpleDateFormat1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        simpleDateFormat1.setTimeZone(TimeZone.getTimeZone("UTC"));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                flag=0;
                if(isOnline()) {

                    newsList.clear();
                    categoryList.clear();
                    String[] cats = PreferenceConnector.readString(getActivity().getApplicationContext(),PreferenceConnector.Categories,"").split("#");
                    for(int i=0;i<cats.length;i++){
                        System.out.println("feed fragment cats "+cats[i]);
                        categoryList.add(cats[i]);
                    }
                    for(int i=0;i<categoryList.size();i++){
                        String[] lang = {"us","in"};
                        for(int j=0;j<2;j++) {
                            requestHeadlines(categoryList.get(i), i,lang[j]);
                        }

                    }
                }
                else{
                    Toast.makeText(getActivity().getApplicationContext(),"No internet connection",Toast.LENGTH_LONG).show();
                    final Handler handler = new Handler();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(isOnline()){
                                flag=flag+1;
                                if(flag==1){
                                    flag=flag+12;
                                    newsList.clear();
                                    categoryList.clear();
                                    System.out.println("handler check ");
                                    String[] cats = PreferenceConnector.readString(getActivity().getApplicationContext(),PreferenceConnector.Categories,"").split("#");
                                    for(int i=0;i<cats.length;i++){
                                        System.out.println("feed fragment cats "+cats[i]);
                                        categoryList.add(cats[i]);
                                    }
                                    for (int i = 0; i < categoryList.size(); i++) {
                                        String[] lang = {"us","in"};
                                        for(int j=0;j<2;j++) {
                                            requestHeadlines(categoryList.get(i), i,lang[j]);
                                        }

                                    }
                                }
                                else{
                                    handler.removeCallbacks(this);
                                }

                            }
                            handler.postDelayed(this,2000);
                        }
                    });
                }
            }
        });
        return view;
    }

    public void requestHeadlines(String category, final int last,String lang){
        {
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
            Call call1 = mAPIService1.NewsApiCallCategory(category,"en",lang, GlobalData.ApiKeyTesting);
            call1.enqueue(new Callback<NewsApiResponse>() {
                @Override
                public void onResponse(Call<NewsApiResponse> call, Response<NewsApiResponse> response) {

                    System.out.println("news response code"+response.code()+" "+response.message());

                    if (response.isSuccessful()) {
                        if(last==categoryList.size()-1){
                            lastFlag=1;
                        }
                        filterandAdd(response.body().getArticles());

                        try {

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
    }

    public void filterandAdd(List<Article> articles){

        for(int i=0;i<articles.size();i++){
            try{
                c = Calendar.getInstance();
                d2 = simpleDateFormat.parse(simpleDateFormat.format(c.getTime()));
                d1 = simpleDateFormat1.parse(articles.get(i).getPublishedAt().substring(0,18).replace("-","/").replace("T"," "));
                System.out.println("date check "+getDifference(d1,d2));
                if(getDifference(d1,d2)<20){
                    newsList.add(articles.get(i));
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if(lastFlag==1) {
            System.out.println("count");
            swipeRefreshLayout.setRefreshing(false);
            Collections.shuffle(newsList);

            HashSet<Article> hashSet = new HashSet<>();

            Collections.sort(newsList, new Comparator<Article>() {
                public int compare(Article m1, Article m2) {
                    return m2.getDate().compareTo(m1.getDate());
                }
            });
            hashSet.addAll(newsList);
            newsList.clear();
            newsList.addAll(hashSet);
            lastFlag=0;
            newsAdapter.notifyDataSetChanged();
        }
    }

    public long getDifference(Date startDate, Date endDate) {
        //milliseconds
        long result;
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : "+ endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        result = elapsedHours;

        System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);
        return result;
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

        }
    }
}
