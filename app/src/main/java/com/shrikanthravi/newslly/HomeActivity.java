package com.shrikanthravi.newslly;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.SubMenu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.NO_ID;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ViewPager viewPager;
    AdView mAdView;
     HomeFragment homeFragment;
     FeedCustomizationFragment feedCustomizationFragment;
     SourceSelectionFragment sourceSelectionFragment;
     SettingsFragment settingsFragment;
    String fragmentTag = "";
    String adUnitId = "ca-app-pub-1385508150167219/8860327065";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
/*
        ImageView bg = findViewById(R.id.homeActivitybg);
        Picasso.with(getApplicationContext()).load("file:///android_asset/pattern_bg.png").into(bg);*/

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        homeFragment = new HomeFragment();
        feedCustomizationFragment = new FeedCustomizationFragment();
        sourceSelectionFragment = new SourceSelectionFragment();
        settingsFragment = new SettingsFragment();

        getSupportFragmentManager().beginTransaction().add(R.id.mainFrame, homeFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.mainFrame, feedCustomizationFragment).commit();
        //getSupportFragmentManager().beginTransaction().add(R.id.mainFrame, sourceSelectionFragment).commit();
        //getSupportFragmentManager().beginTransaction().add(R.id.mainFrame, settingsFragment).commit();

        SpannableString wordSpan = new SpannableString("Newslly");
        wordSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color1)),0,1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color2)),1,2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color3)),2,3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color4)),3,4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color1)),4,5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color2)),5,6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color3)),6,7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);/*
        toolbar.setLogo(getResources().getDrawable(R.drawable.newslly_reduced));*/
        toolbar.setTitleMargin(0,0,0,0);
        getSupportActionBar().setTitle(wordSpan);
        getWindow().setStatusBarColor(getResources().getColor(android.R.color.white));
        getSupportActionBar().setElevation(0);

        Typeface regular = Typeface.createFromAsset(getAssets(), "fonts/product_san_regular.ttf");
        FontChanger fontChanger = new FontChanger(regular);
        fontChanger.replaceFonts((ViewGroup)this.findViewById(android.R.id.content));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        fontChanger.replaceFonts((ViewGroup)drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //fontChanger.replaceFonts(navigationView);
        Menu m = navigationView.getMenu();
        for (int i=0;i<m.size();i++) {
            MenuItem mi = m.getItem(i);

            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu!=null && subMenu.size() >0 ) {
                for (int j=0; j <subMenu.size();j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }

            //the method we have create in activity
            applyFontToMenuItem(mi);
        }
        navigationView.setNavigationItemSelectedListener(this);
        /*FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainFrame, new HomeFragment());
        ft.commit();*/
        getSupportFragmentManager().beginTransaction().show(homeFragment).commit();
        getSupportFragmentManager().beginTransaction().hide(feedCustomizationFragment).commit();
        //getSupportFragmentManager().beginTransaction().hide(sourceSelectionFragment).commit();
       // getSupportFragmentManager().beginTransaction().hide(settingsFragment).commit();

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory(Intent.CATEGORY_HOME);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();

            if (id == R.id.nav_home) {


                getSupportFragmentManager().beginTransaction().show(homeFragment).commit();
                getSupportFragmentManager().beginTransaction().hide(feedCustomizationFragment).commit();
               // getSupportFragmentManager().beginTransaction().hide(sourceSelectionFragment).commit();
                //getSupportFragmentManager().beginTransaction().hide(settingsFragment).commit();

            } else if (id == R.id.CustomizeFeed) {

                getSupportFragmentManager().beginTransaction().hide(homeFragment).commit();
                getSupportFragmentManager().beginTransaction().show(feedCustomizationFragment).commit();
                //getSupportFragmentManager().beginTransaction().hide(sourceSelectionFragment).commit();
                //getSupportFragmentManager().beginTransaction().hide(settingsFragment).commit();


            } else if (id == R.id.ChangeNewsSource) {


                getSupportFragmentManager().beginTransaction().hide(homeFragment).commit();
                getSupportFragmentManager().beginTransaction().hide(feedCustomizationFragment).commit();
               // getSupportFragmentManager().beginTransaction().show(sourceSelectionFragment).commit();
                //getSupportFragmentManager().beginTransaction().hide(settingsFragment).commit();

            } else if (id == R.id.Settings) {

                getSupportFragmentManager().beginTransaction().hide(homeFragment).commit();
                getSupportFragmentManager().beginTransaction().hide(feedCustomizationFragment).commit();
                //getSupportFragmentManager().beginTransaction().hide(sourceSelectionFragment).commit();
                //getSupportFragmentManager().beginTransaction().show(settingsFragment).commit();

            } else if (id == R.id.NightMode) {

            } else if (id == R.id.nav_share) {
                
            } else if (id == R.id.AboutDeveloper) {

            }




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    private void applyFontToMenuItem(MenuItem mi) {

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/product_san_regular.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    public void setNightmode(){
        getWindow().setStatusBarColor(getResources().getColor(android.R.color.black));
        getWindow().setStatusBarColor(getResources().getColor(android.R.color.black));
    }

}
