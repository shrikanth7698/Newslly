package com.shrikanthravi.newslly;


import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cielyang.android.clearableedittext.ClearableEditText;
import com.github.mmin18.widget.RealtimeBlurView;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.shrikanthravi.newslly.data.model.Article;
import com.shrikanthravi.newslly.data.model.NewsApiResponse;
import com.shrikanthravi.newslly.data.model.SearchSuggestion;
import com.shrikanthravi.newslly.data.model.Suggestion;
import com.shrikanthravi.newslly.data.model.remote.APIService;
import com.shrikanthravi.newslly.data.model.remote.GlobalData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInLeftAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {


    public SearchFragment() {
        // Required empty public constructor
    }
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView newsRV;
    RecyclerView searchSuggestionRv;
    NewsAdapter newsAdapter;
    SearchSuggestionAdapter searchSuggestionAdapter;
    List<Article> newsList;
    List<Suggestion> suggestionList;
    ClearableEditText searchET;
    ImageView searchBack;
    RealtimeBlurView blurView;
    int suggestionflag=0;
    boolean isChecked;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final  View view = inflater.inflate(R.layout.fragment_search, container, false);
        searchBack = view.findViewById(R.id.searchBack);
        blurView = view.findViewById(R.id.blurLayout);
        blurView.setVisibility(View.GONE);
        Typeface regular = Typeface.createFromAsset(getActivity().getAssets(), "fonts/product_san_regular.ttf");
        FontChanger fontChanger = new FontChanger(regular);
        fontChanger.replaceFonts((ViewGroup)view);
        swipeRefreshLayout = view.findViewById(R.id.searchSwipeRefreshLayout);
        newsRV =(RecyclerView) view.findViewById(R.id.searchRV);
        searchSuggestionRv = view.findViewById(R.id.SearchSuggestionRv);
        newsList = new ArrayList<>();
        suggestionList = new ArrayList<>();
        newsAdapter = new NewsAdapter(newsList,getActivity().getApplicationContext());
        searchSuggestionAdapter = new SearchSuggestionAdapter(suggestionList,getActivity().getApplicationContext());
        searchET = view.findViewById(R.id.searchET);
        //searchButton = view.findViewById(R.id.searchButton);
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        newsRV.setLayoutManager(horizontalLayoutManager);
        FlexboxLayoutManager suggestionLayoutManager = new FlexboxLayoutManager(getActivity().getApplicationContext());
        suggestionLayoutManager.setFlexDirection(FlexDirection.ROW);
        suggestionLayoutManager.setJustifyContent(JustifyContent.FLEX_START);

        searchSuggestionRv.setLayoutManager(suggestionLayoutManager);
        ScaleInAnimationAdapter animationAdapter = new ScaleInAnimationAdapter(newsAdapter);
        animationAdapter.setFirstOnly(false);
        animationAdapter.setDuration(300);
        newsRV.setAdapter(animationAdapter);
        searchSuggestionRv.setAdapter(searchSuggestionAdapter);
        searchSuggestionRv.addOnItemTouchListener(
                new MyRecyclerItemClickListener(getActivity().getApplicationContext(), new MyRecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {


                        isChecked=false;
                        final int[] stateSet = {android.R.attr.state_checked * (-1)};
                        searchBack.setImageState(stateSet,true);
                        blurView.setVisibility(View.GONE);
                        searchSuggestionRv.setVisibility(View.GONE);
                        suggestionflag=1;
                        searchET.setText(suggestionList.get(position).getData().replace("\"",""));
                        searchET.setCursorVisible(false);
                        hideKeyboard(view);
                        searchArticles(suggestionList.get(position).getData());

                    }
                })
        );

        searchBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isChecked==true) {
                    searchET.setCursorVisible(false);
                    hideKeyboard(view);
                    blurView.setVisibility(View.GONE);
                    searchSuggestionRv.setVisibility(View.GONE);
                    final int[] stateSet = {android.R.attr.state_checked * (-1)};
                    searchBack.setImageState(stateSet,true);
                    isChecked=false;
                }


            }
        });

        searchET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                newsRV.smoothScrollToPosition(0);
                isChecked=true;
                final int[] stateSet = {android.R.attr.state_checked * (1)};
                searchBack.setImageState(stateSet,true);
                suggestionList.clear();
                blurView.setVisibility(View.VISIBLE);
                searchSuggestionRv.setVisibility(View.VISIBLE);
                searchET.setCursorVisible(true);
                suggestionflag=0;
            }
        });
        searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(suggestionflag==0) {
                    if (s.length() != 0) {
                        searchSuggestionRv.setVisibility(View.VISIBLE);
                        blurView.setVisibility(View.VISIBLE);
                        getSuggestion(s.toString());
                    }
                    else {
                        suggestionList.clear();
                        suggestionList.clear();
                        suggestionList.clear();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(searchET.getText().toString().trim().length()==0) {
                    Toast.makeText(getActivity().getApplicationContext(),"Search field is empty",Toast.LENGTH_LONG).show();
                }
                else {

                    searchArticles(searchET.getText().toString());
                }
            }
        });

        searchET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    suggestionList.clear();
                    searchET.setCursorVisible(false);
                    hideKeyboard(view);
                    blurView.setVisibility(View.GONE);
                    searchSuggestionRv.setVisibility(View.GONE);
                    final int[] stateSet = {android.R.attr.state_checked * (-1)};
                    searchBack.setImageState(stateSet,true);
                    isChecked=false;
                    searchArticles(searchET.getText().toString());
                    return true;
                }
                return false;
            }
        });

        return view;

    }

    public void getSuggestion(String q){

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = null;
        retrofit = new Retrofit.Builder()
                .baseUrl("http://suggestqueries.google.com/complete/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIService mAPIService1;
        mAPIService1 = retrofit.create(APIService.class);
        Call<ResponseBody> call1 = mAPIService1.SuggestionCall("firefox","en", q);
        call1.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                System.out.println("Suggestion response code"+response.code()+" "+response.message());

                if (response.isSuccessful()) {
                    try {
                        suggestionList.clear();
                        String temp = response.body().string();
                        String[] data = temp.replace("[","").replace("]","").split(",");
                        System.out.println("suggestion data "+data[1]+" "+temp.length());
                        for(int i=0;i<data.length;i++){
                            suggestionList.add(new Suggestion(data[i]));
                        }
                        searchSuggestionAdapter.notifyDataSetChanged();
                    }

                    catch (Exception e) {
                        if (e.getMessage() != null) {

                        }
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //Log.e(TAG, "Unable to submit post to API.");
                System.out.println("Suggestion request error");

            }
        });
    }

    public void searchArticles(String q){
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
        Call call1 = mAPIService1.SearchArticles(q,"en", GlobalData.ApiKeyTesting);
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
                        newsRV.smoothScrollToPosition(0);
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

    public void parseXML(){



        String xml = "<model><name>my name</name><description>my description</description></model>";
        //SimpleModel model = gsonXml.fromXml(xml, SimpleModel.class);
    }

    public void hideKeyboard(View v){

        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
