package com.shrikanthravi.newslly;


import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FeedCustomizationFragment extends Fragment {


    public FeedCustomizationFragment() {
        // Required empty public constructor
    }

    RecyclerView categoryRV;
    List<Category> categories;
    CategoryAdapter categoryAdapter;
    Button updateButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feed_customization, container, false);

        Typeface regular = Typeface.createFromAsset(getActivity().getAssets(), "fonts/product_san_regular.ttf");
        FontChanger fontChanger = new FontChanger(regular);
        fontChanger.replaceFonts((ViewGroup)view);
        categoryRV = view.findViewById(R.id.CategoryRV);
        categories = new ArrayList<>();
        updateButton = view.findViewById(R.id.UpdateButton);
        final String[] cats = PreferenceConnector.readString(getActivity().getApplicationContext(),PreferenceConnector.Categories,"").split("#");
        categories.add(new Category("general",getSelected("general")));
        categories.add(new Category("business",getSelected("business")));
        categories.add(new Category("technology",getSelected("technology")));
        categories.add(new Category("sport",getSelected("sport")));
        categories.add(new Category("politics",getSelected("politics")));
        categories.add(new Category("entertainment",getSelected("entertainment")));
        categories.add(new Category("gaming",getSelected("gaming")));
        categories.add(new Category("health-and-medical",getSelected("health-and-medical")));
        categories.add(new Category("music",getSelected("music")));
        categories.add(new Category("science-and-nature",getSelected("science-and-nature")));

        categoryRV.setNestedScrollingEnabled(true);
        categoryAdapter = new CategoryAdapter(categories,getActivity().getApplicationContext());
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity().getApplicationContext(),2);
        categoryRV.setLayoutManager(layoutManager);
        categoryRV.setAdapter(categoryAdapter);
        categoryAdapter.notifyDataSetChanged();
        int count=0;
        for(int i=0;i<categories.size();i++){
            if(categories.get(i).getSelected().equals("1")){
                count=count+1;
            }
        }
         categoryAdapter.setSelectedCount(count);
        final Handler handler = new Handler();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(categoryAdapter.getSelectedCount()>=3){
                    updateButton.setVisibility(View.VISIBLE);
                }
                else{
                    updateButton.setVisibility(View.GONE);
                }
                handler.postDelayed(this,100);
            }
        });


        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cats1="";
                List<Category> temp = categoryAdapter.getSelected();
                for(int i=0;i<temp.size();i++){
                    if(temp.get(i).getSelected().equals("1")) {
                        cats1 = cats1 + temp.get(i).getName() + "#";
                    }
                }
                System.out.println("feed customization check "+cats1);
                Toast.makeText(getActivity().getApplicationContext(),"updated",Toast.LENGTH_SHORT).show();
                PreferenceConnector.writeString(getActivity().getApplicationContext(),PreferenceConnector.Categories,cats1);
            }
        });

        return view;
    }


    public void setSelectedAdapter(){

    }

    public String getSelected(String s){


        String[] cats = PreferenceConnector.readString(getActivity().getApplicationContext(),PreferenceConnector.Categories,"").split("#");

        for(int i=0;i<cats.length;i++){
            if(cats[i].equals(s)){
                return "1";
            }
        }
        return "0";
    }

}
