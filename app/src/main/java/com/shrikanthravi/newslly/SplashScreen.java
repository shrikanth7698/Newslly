package com.shrikanthravi.newslly;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    TextView appname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash_screen);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        Typeface regular = Typeface.createFromAsset(getAssets(), "fonts/product_san_regular.ttf");
        FontChanger fontChanger = new FontChanger(regular);
        fontChanger.replaceFonts((ViewGroup)this.findViewById(android.R.id.content));

        appname = findViewById(R.id.ssAppnameTV);
        SpannableString wordSpan = new SpannableString("Newslly");
        wordSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color1)),0,1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color2)),1,2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color3)),2,3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color4)),3,4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color1)),4,5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color2)),5,6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color3)),6,7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        appname.setText(wordSpan);
        //startService(new Intent(this,NewsBackgroundService.class));
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(PreferenceConnector.readString(getApplicationContext(),PreferenceConnector.Categories,"").equals("")) {
                    startActivity(new Intent(SplashScreen.this, CategorySelectionActivity.class));
                }
                else{
                    //startActivity(new Intent(SplashScreen.this,HomeActivity.class));
                    startActivity(new Intent(SplashScreen.this,NewHomeActivity.class));

                }
            }
        },800);

    }
}
