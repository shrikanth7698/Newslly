package com.shrikanthravi.newslly;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import com.shrikanthravi.newslly.data.model.Source;

public class CategorySelectionActivity extends AppCompatActivity {

    RecyclerView categoryRV;
    List<Category> categories;
    CategoryAdapter categoryAdapter;
    Button continueButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_category_selection);

        Typeface regular = Typeface.createFromAsset(getAssets(), "fonts/product_san_regular.ttf");
        FontChanger fontChanger = new FontChanger(regular);
        fontChanger.replaceFonts((ViewGroup)this.findViewById(android.R.id.content));

        continueButton = findViewById(R.id.ContinueButton);
        categoryRV = findViewById(R.id.CategoryRV);
        categories = new ArrayList<>();
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
        categoryAdapter = new CategoryAdapter(categories,getApplicationContext());
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);
        categoryRV.setLayoutManager(layoutManager);
        categoryRV.setAdapter(categoryAdapter);

        try{


            /*JSONObject obj = new JSONObject(loadJSONFromAsset());

            JSONArray sources = obj.getJSONArray("sources");
            for (int i = 0; i < sources.length(); i++) {
                if(!categories.contains(sources.getJSONObject(i).getString("category"))) {
                    categories.add(sources.getJSONObject(i).getString("category").replace("-"," "));
                    System.out.println(sources.getJSONObject(i).getString("category").replace("-"," "));
                    categoryAdapter.notifyDataSetChanged();
                }
            }*/

            categoryAdapter.notifyDataSetChanged();
        }catch (Exception e){
            e.printStackTrace();
        }
        final Handler handler = new Handler();
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(categoryAdapter.getSelectedCount()>=3){
                    continueButton.setVisibility(View.VISIBLE);
                }
                else{
                    continueButton.setVisibility(View.GONE);
                }
                handler.postDelayed(this,100);
            }
        });
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Category> selected = categoryAdapter.getSelected();
                String categoriesString="";
                for(int i=0;i<selected.size();i++){
                    if(selected.get(i).getSelected().equals("1")) {
                        categoriesString = categoriesString + categories.get(i).getName()+"#";
                    }
                }
                PreferenceConnector.writeString(getApplicationContext(),PreferenceConnector.Categories,categoriesString);
                System.out.println("cats "+categoriesString);
                startActivity(new Intent(CategorySelectionActivity.this,NewHomeActivity.class));
            }
        });



    }

    public String loadJSONFromAsset() {
        String json = null;
        try {


            InputStream is = getAssets().open("news_sources.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }
    public String getSelected(String s){

        /*String[] cats = PreferenceConnector.readString(getApplicationContext(),PreferenceConnector.Categories,"").split("#");

        for(int i=0;i<cats.length;i++){
            if(cats[i].equals(s)){
                return "1";
            }
        }*/
        return "0";
    }
}
