package com.shrikanthravi.newslly;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.Image;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gjiazhe.scrollparallaximageview.ScrollParallaxImageView;
import com.gjiazhe.scrollparallaximageview.parallaxstyle.VerticalMovingStyle;
import com.shrikanthravi.newslly.data.model.Article;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import uk.co.deanwild.flowtextview.FlowTextView;

/**
 * Created by shrikanthravi on 14/12/17.
 */


public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {
    private List<Article> articleList;
    Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView newsName;
        public ScrollParallaxImageView newsImage;
        public TextView newsSource;
        public LinearLayout newsDescriptionLayout;
        public TextView newsDescription;
        public ImageView upDownButton;
        public TextView publishedTime;
        public CardView rootCardLayout;
        public MyViewHolder(View view) {
            super(view);
            newsName = (TextView) view.findViewById(R.id.title);
            newsImage = (ScrollParallaxImageView) view.findViewById(R.id.newsImage);
            newsImage.setParallaxStyles(new VerticalMovingStyle());
            newsSource = (TextView) view.findViewById(R.id.source);
            newsDescriptionLayout = (LinearLayout) view.findViewById(R.id.descriptionLayout);
            newsDescription = (TextView) view.findViewById(R.id.description);
            upDownButton = (ImageView) view.findViewById(R.id.up_down_arrow);
            publishedTime = (TextView) view.findViewById(R.id.updatedTime);
            rootCardLayout = (CardView) view.findViewById(R.id.rootCardLayout);



        }
    }


    public NewsAdapter(List<Article> verticalList, Context context) {
        this.articleList = verticalList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_card_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/product_san_regular.ttf");
        if(NewHomeActivity.isNight){
            holder.rootCardLayout.setCardBackgroundColor(Color.parseColor("#FF2D2D2D"));
            holder.newsDescription.setTextColor(Color.parseColor("#ffffff"));
        }
        else {
            holder.rootCardLayout.setCardBackgroundColor(Color.parseColor("#ffffff"));
            holder.newsDescription.setTextColor(Color.parseColor("#000000"));
        }
        holder.newsName.setText(articleList.get(position).getTitle().replace("<em>","").replace("em>","").toString());
        holder.newsName.setTypeface(font);
        holder.newsSource.setTypeface(font);
        holder.newsDescription.setTypeface(font);
        holder.publishedTime.setTypeface(font);
        try {
            Picasso.with(context).load(articleList.get(position).getUrlToImage()).error(context.getDrawable(R.drawable.newslly_icon)).into(holder.newsImage);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        holder.newsDescription.setText(articleList.get(position).getDescription());
        holder.newsSource.setText(articleList.get(position).getSource().getName().toString());
        holder.upDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.newsDescriptionLayout.getVisibility()==View.VISIBLE){
                    holder.newsDescriptionLayout.setVisibility(View.GONE);
                    final int[] stateSet = {android.R.attr.state_checked * (-1)};
                    holder.upDownButton.setImageState(stateSet,true);

                }
                else{
                    holder.newsDescriptionLayout.setVisibility(View.VISIBLE);
                    final int[] stateSet = {android.R.attr.state_checked * (1)};
                    holder.upDownButton.setImageState(stateSet,true);


                }
            }
        });

        holder.newsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent web = new Intent(context,WebActivity.class);
                web.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                web.putExtra("url",articleList.get(position).getUrl());
                context.startActivity(web);
            }
        });
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        simpleDateFormat1.setTimeZone(TimeZone.getTimeZone("UTC"));
        try{

            Calendar c = Calendar.getInstance();
            Date d2 = simpleDateFormat.parse(simpleDateFormat.format(c.getTime()));
            Date d1 = simpleDateFormat1.parse(articleList.get(position).getPublishedAt().substring(0,18).replace("-","/").replace("T"," "));
            System.out.println("date check "+getDifference(d1,d2));
            holder.publishedTime.setText(getDifference(d1,d2));
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    public String getDifference(Date startDate, Date endDate) {
        //milliseconds
        String result;
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
        if(elapsedDays==0) {
            if(elapsedHours==0){
                result = elapsedMinutes+"m ago";
            }
            else{
                result =elapsedHours+"h ago";
            }
        }
        else{
            result = elapsedDays+"d ago";
        }
        System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);
        return result;
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


}




