package com.shrikanthravi.newslly;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shrikanthravi.newslly.data.model.NewsApiResponse;
import com.shrikanthravi.newslly.data.model.Source;
import com.shrikanthravi.newslly.data.model.Source_list;
import com.shrikanthravi.newslly.data.model.remote.APIService;
import com.shrikanthravi.newslly.data.model.remote.GlobalData;

import org.angmarch.views.NiceSpinner;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class SourceSelectionFragment extends Fragment {


    public SourceSelectionFragment() {
        // Required empty public constructor
    }

    RecyclerView sourceRv;
    List<Source> sourceList ;
    SourceAdapter sourceAdapter;
    List<Source> temp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_source_selection, container, false);

        sourceRv =(RecyclerView) view.findViewById(R.id.sourceRV);
        sourceRv.setNestedScrollingEnabled(false);
        sourceList = new ArrayList<>();
        temp = new ArrayList<>();
        sourceAdapter = new SourceAdapter(sourceList,getActivity().getApplicationContext());
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        sourceRv.setLayoutManager(horizontalLayoutManager);
        sourceRv.setAdapter(sourceAdapter);
        requestSources();
        FloatingActionButton floatingActionButton = view.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(getContext());
                View promptsView = li.inflate(R.layout.source_filter_dialog, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        getContext());
                alertDialogBuilder.setView(promptsView);


                Typeface regular = Typeface.createFromAsset(getActivity().getAssets(), "fonts/product_san_regular.ttf");
                FontChanger fontChanger = new FontChanger(regular);
                fontChanger.replaceFonts((ViewGroup)promptsView);

                final AlertDialog alertDialog = alertDialogBuilder.create();
                final List<String> categories = new LinkedList<>(Arrays.asList("general", "business", "gaming", "entertainment", "health-and-medical","music","politics","science-and-nature","sport","technology"));
                final List<String> languages = new LinkedList<>(Arrays.asList("ar","en", "cn", "de" ,"es", "fr", "he", "it" ,"nl", "no", "pt", "ru", "sv", "ud"));
                final List<String> countries = new LinkedList<>(Arrays.asList("ar", "au", "br", "ca", "cn", "de", "es", "fr" ,"gb" ,"hk" ,"ie", "in" ,"is" ,"it" ,"nl", "no" ,"pk", "ru" ,"sa" ,"sv" ,"us", "za"));
                System.out.println("size "+languages.size()+" "+countries.size());
                List<TextView> CatTvList = new ArrayList<>();
                List<TextView> LatTvList = new ArrayList<>();
                List<TextView> ConTvList = new ArrayList<>();

                CatTvList.add((TextView) promptsView.findViewById(R.id.c1));
                CatTvList.add((TextView) promptsView.findViewById(R.id.c2));
                CatTvList.add((TextView) promptsView.findViewById(R.id.c3));
                CatTvList.add((TextView) promptsView.findViewById(R.id.c4));
                CatTvList.add((TextView) promptsView.findViewById(R.id.c5));
                CatTvList.add((TextView) promptsView.findViewById(R.id.c6));
                CatTvList.add((TextView) promptsView.findViewById(R.id.c7));
                CatTvList.add((TextView) promptsView.findViewById(R.id.c8));
                CatTvList.add((TextView) promptsView.findViewById(R.id.c9));
                CatTvList.add((TextView) promptsView.findViewById(R.id.c10));

                LatTvList.add((TextView) promptsView.findViewById(R.id.l1));
                LatTvList.add((TextView) promptsView.findViewById(R.id.l2));
                LatTvList.add((TextView) promptsView.findViewById(R.id.l3));
                LatTvList.add((TextView) promptsView.findViewById(R.id.l4));
                LatTvList.add((TextView) promptsView.findViewById(R.id.l5));
                LatTvList.add((TextView) promptsView.findViewById(R.id.l6));
                LatTvList.add((TextView) promptsView.findViewById(R.id.l7));
                LatTvList.add((TextView) promptsView.findViewById(R.id.l8));
                LatTvList.add((TextView) promptsView.findViewById(R.id.l9));
                LatTvList.add((TextView) promptsView.findViewById(R.id.l10));
                LatTvList.add((TextView) promptsView.findViewById(R.id.l11));
                LatTvList.add((TextView) promptsView.findViewById(R.id.l12));
                LatTvList.add((TextView) promptsView.findViewById(R.id.l13));
                LatTvList.add((TextView) promptsView.findViewById(R.id.l14));

                ConTvList.add((TextView) promptsView.findViewById(R.id.co1));
                ConTvList.add((TextView) promptsView.findViewById(R.id.co2));
                ConTvList.add((TextView) promptsView.findViewById(R.id.co3));
                ConTvList.add((TextView) promptsView.findViewById(R.id.co4));
                ConTvList.add((TextView) promptsView.findViewById(R.id.co5));
                ConTvList.add((TextView) promptsView.findViewById(R.id.co6));
                ConTvList.add((TextView) promptsView.findViewById(R.id.co7));
                ConTvList.add((TextView) promptsView.findViewById(R.id.co8));
                ConTvList.add((TextView) promptsView.findViewById(R.id.co9));
                ConTvList.add((TextView) promptsView.findViewById(R.id.co10));
                ConTvList.add((TextView) promptsView.findViewById(R.id.co11));
                ConTvList.add((TextView) promptsView.findViewById(R.id.co12));
                ConTvList.add((TextView) promptsView.findViewById(R.id.co13));
                ConTvList.add((TextView) promptsView.findViewById(R.id.co14));
                ConTvList.add((TextView) promptsView.findViewById(R.id.co15));
                ConTvList.add((TextView) promptsView.findViewById(R.id.co16));
                ConTvList.add((TextView) promptsView.findViewById(R.id.co17));
                ConTvList.add((TextView) promptsView.findViewById(R.id.co18));
                ConTvList.add((TextView) promptsView.findViewById(R.id.co19));
                ConTvList.add((TextView) promptsView.findViewById(R.id.co20));
                ConTvList.add((TextView) promptsView.findViewById(R.id.co21));
                ConTvList.add((TextView) promptsView.findViewById(R.id.co22));

                for(int i=0;i<CatTvList.size();i++){
                    CatTvList.get(i).setText(categories.get(i));
                }

                for(int i=0;i<LatTvList.size();i++){
                    LatTvList.get(i).setText(languages.get(i));
                }

                for(int i=0;i<ConTvList.size();i++){
                    ConTvList.get(i).setText(countries.get(i));
                }

                Button filterButton = promptsView.findViewById(R.id.filterBtn);
                filterButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //System.out.println("selected" +categories.get(niceSpinner1.getSelectedIndex()));
                        //filterSources(categories.get(niceSpinner1.getSelectedIndex()),languages.get(niceSpinner2.getSelectedIndex()),countries.get(niceSpinner3.getSelectedIndex()));
                        alertDialog.cancel();
                    }
                });
                // set dialog message
                alertDialogBuilder.setCancelable(true);

                // create alert dialog

                // show it
                alertDialog.show();
            }
        });
        return view;
    }

    public void requestSources(){
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
        Call call1 = mAPIService1.NewsApiCallSource(GlobalData.ApiKeyTesting);
        call1.enqueue(new Callback<Source_list>() {
            @Override
            public void onResponse(Call<Source_list> call, Response<Source_list> response) {

                System.out.println("news response code"+response.code()+" "+response.message());

                if (response.isSuccessful()) {
                    sourceList.clear();
                    sourceList.addAll(response.body().getSources());
                    sourceAdapter.notifyDataSetChanged();
                    temp.addAll(sourceList);

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
            public void onFailure(Call<Source_list> call, Throwable t) {
                //Log.e(TAG, "Unable to submit post to API.");
                System.out.println("news request error");

            }
        });
    }

    public void filterSources(String Category,String Language,String Country){
        sourceList.clear();
        for(int i=0;i<temp.size();i++){
            if(temp.get(i).getCategory().equals(Category)){
                sourceList.add(temp.get(i));
            }
        }
        sourceAdapter.notifyDataSetChanged();
    }


}
