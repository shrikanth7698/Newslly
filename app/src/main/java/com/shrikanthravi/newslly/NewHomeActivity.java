package com.shrikanthravi.newslly;

import android.animation.ValueAnimator;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;
import com.shrikanthravi.customnavigationdrawer2.data.MenuItem;
import com.shrikanthravi.customnavigationdrawer2.widget.SNavigationDrawer;
import com.shrikanthravi.newslly.data.model.Article;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewHomeActivity extends AppCompatActivity {

    @BindView(R.id.sNavigationDrawer)
    SNavigationDrawer navigationDrawer;


    Class fragmentClass;
    public static Fragment fragment;

    public static boolean land=false;
    public int currentPos=0;

    public static boolean savedState=false;
    static SharedPreferences settings;

    public static boolean isNight=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_home);
        ButterKnife.bind(this);
        settings = getSharedPreferences("Infinity", Context.MODE_PRIVATE);
        if(savedInstanceState==null) {
            savedState=false;
            init(false);
        }
        else {

            savedState=true;
            currentPos = savedInstanceState.getInt("currentPos");
            navigationDrawer.setCurrentPos(currentPos);
            init(true);

            System.out.println("saved state daw "+currentPos);
        }
        navigationDrawer.setOnNightModeClickListener(new SNavigationDrawer.onNightModeClickListener() {
            @Override
            public void onNightModeClicked() {
                if(navigationDrawer.isNight()){
                    setNightModeOff();
                }
                else {
                    setNightmodeOn();
                }
            }
        });
    }
    private void replaceFragment (Fragment fragment){
        String backStateName = fragment.getClass().getName();

        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);

        if (!fragmentPopped){ //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction().setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
            ft.replace(R.id.frameLayout, fragment);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }

    private void openFragment(Fragment newFragment){
        Fragment containerFragment = getSupportFragmentManager().findFragmentById(R.id.frameLayout);
        if (containerFragment.getClass().getName().equalsIgnoreCase(newFragment.getClass().getName()))
            return;
        else
            replaceFragment(newFragment);
        // Start transaction and replace fragment
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        System.out.println("bla bla bla orientation stuff");
        if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){
            land=true;
            navigationDrawer.setCurrentPos(currentPos);
            navigationDrawer.setOrientation(land);

        }
        else {
            land=false;
            navigationDrawer.setCurrentPos(currentPos);
            navigationDrawer.setOrientation(land);
        }

    }

    public void init(boolean isSaved){
        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem("Feed", R.drawable.news_bg));
        menuItems.add(new MenuItem("Top Headlines", R.drawable.feed_bg));
        menuItems.add(new MenuItem("Customization", R.drawable.music_bg));

        navigationDrawer.setMenuItemList(menuItems);
        if(isSaved){

            switch (currentPos){
                case 0:{
                    fragmentClass = FeedFragment.class;
                    navigationDrawer.setAppbarTitleTV("Feed");
                    break;
                }
                case 1:{
                    navigationDrawer.setAppbarTitleTV("Top Headlines");
                    fragmentClass = TopHeadlinesFragment.class;
                    break;
                }
                case 3:{
                    navigationDrawer.setAppbarTitleTV("Customization");
                    fragmentClass = FeedCustomizationFragment.class;
                    break;
                }
            }
        }
        else {
            fragmentClass = FeedFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).replace(R.id.frameLayout, fragment).commit();
        }

        navigationDrawer.setOnMenuItemClickListener(new SNavigationDrawer.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClicked(int position) {
                System.out.println("Position " + position);
                currentPos = position;
                switch (position) {
                    case 0: {
                        fragment = new FeedFragment();
                        break;
                    }
                    case 1: {
                        fragment = new TopHeadlinesFragment();
                        break;
                    }
                    case 2: {
                        fragment = new FeedCustomizationFragment();
                        break;
                    }

                }

                navigationDrawer.setDrawerListener(new SNavigationDrawer.DrawerListener() {

                    @Override
                    public void onDrawerOpened() {

                    }

                    @Override
                    public void onDrawerOpening() {

                    }

                    @Override
                    public void onDrawerClosing() {

                    }

                    @Override
                    public void onDrawerClosed() {
                        System.out.println("Drawer closed");

                        if (fragment != null) {
                            /*FragmentManager fragmentManager = getSupportFragmentManager();

                            fragmentManager.beginTransaction().setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).replace(R.id.frameLayout, fragment).commit();*/
                            openFragment(fragment);

                        }
                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                        System.out.println("State " + newState);
                    }
                });
            }
        });

        navigationDrawer.setOnTermsClickListener(new SNavigationDrawer.OnTermsClickListener() {
            @Override
            public void onTermsClicked() {
                Intent web = new Intent(NewHomeActivity.this,WebActivity.class);
                web.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                web.putExtra("url","file:///android_asset/newslly_privacy_policy.html");
                startActivity(web);
            }
        });
    }

    protected void onSaveInstanceState(Bundle out) {
        super.onSaveInstanceState(out);
        out.putInt("currentPos", currentPos);

        /*SharedPreferences.Editor editor;

        editor = settings.edit();
        Gson gson = new Gson();
        String ItemsJson1 = gson.toJson(FeedFragment.newsList);
        String ItemsJson2 = gson.toJson(TopHeadlinesFragment.newsList);
        editor.putString("FeedData", ItemsJson1);
        editor.putString("TopData", ItemsJson1);
        editor.apply();*/

    }

    public static void updateFeedFromSavedInstance(){


        SharedPreferences.Editor editor;


        if (settings.contains("FeedData")){
            String jsonItems = settings.getString("FeedData", null);
            Gson gson = new Gson();
            List<Article> shared = gson.fromJson(jsonItems, List.class);
            List<Article> ItemsFromSharedPrefs = new ArrayList<>(shared);
            FeedFragment.newsList.clear();
            FeedFragment.newsList.addAll(ItemsFromSharedPrefs);
        }
    }

    public static void updateTopFromSavedInstance(){


        SharedPreferences.Editor editor;

        if(settings.contains("TopData")){
            String jsonItems = settings.getString("TopData", null);
            Gson gson = new Gson();
            List<Article> shared = gson.fromJson(jsonItems, List.class);
            List<Article> ItemsFromSharedPrefs = new ArrayList<>(shared);
            TopHeadlinesFragment.newsList.clear();
            TopHeadlinesFragment.newsList.addAll(ItemsFromSharedPrefs);
        }
    }

    protected void setNightmodeOn(){
        isNight=true;
        switch (currentPos){
            case 0:{
                FeedFragment.setNighMode(false);
                break;
            }
            case 1:{
                TopHeadlinesFragment.setNighMode(false);
                break;
            }
            case 2:{
                FeedCustomizationFragment.setNighMode(false);

                break;
            }
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            getWindow().getDecorView().setSystemUiVisibility(0);

        }
        ValueAnimator valueAnimator = ValueAnimator.ofArgb(Color.parseColor("#ffffff"),Color.parseColor("#1e1e1e"));
        valueAnimator.setDuration(400);
        valueAnimator.start();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                getWindow().setStatusBarColor((int)animation.getAnimatedValue());
            }
        });

    }

    protected void setNightModeOff(){
        isNight=false;
        switch (currentPos){
            case 0:{
                FeedFragment.setNighMode(true);
                break;
            }
            case 1:{
                TopHeadlinesFragment.setNighMode(true);
                break;
            }
            case 2:{
                FeedCustomizationFragment.setNighMode(true);

                break;
            }
        }
        ValueAnimator valueAnimator = ValueAnimator.ofArgb(Color.parseColor("#1e1e1e"),Color.parseColor("#ffffff"));
        valueAnimator.setDuration(400);
        valueAnimator.start();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                getWindow().setStatusBarColor((int)animation.getAnimatedValue());
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int flags = getWindow().getDecorView().getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            getWindow().getDecorView().setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE);
        }


    }


}
