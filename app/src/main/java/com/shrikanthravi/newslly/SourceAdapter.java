package com.shrikanthravi.newslly;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gjiazhe.scrollparallaximageview.ScrollParallaxImageView;
import com.gjiazhe.scrollparallaximageview.parallaxstyle.VerticalMovingStyle;
import com.shrikanthravi.newslly.data.model.Source;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by shrikanthravi on 17/12/17.
 */

public class SourceAdapter extends RecyclerView.Adapter<SourceAdapter.MyViewHolder> {
    private List<Source> SourceList;
    Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView sourceName;
        public TextView sourceLanguage;
        public TextView sourceDescription;
        public TextView sourceCategory;
        public TextView sourceCountry;
        public MyViewHolder(View view) {
            super(view);
            sourceName = (TextView) view.findViewById(R.id.name);
            sourceLanguage = (TextView) view.findViewById(R.id.language);
            sourceDescription = (TextView) view.findViewById(R.id.description);
            sourceCategory = (TextView) view.findViewById(R.id.category);
            sourceCountry = (TextView) view.findViewById(R.id.country);


        }
    }


    public SourceAdapter(List<Source> verticalList, Context context) {
        this.SourceList = verticalList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_source_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/product_san_regular.ttf");
        holder.sourceName.setText(SourceList.get(position).getName().toString());
        holder.sourceName.setTypeface(font);
        holder.sourceLanguage.setTypeface(font);
        holder.sourceDescription.setTypeface(font);
        holder.sourceCategory.setTypeface(font);
        holder.sourceCountry.setTypeface(font);
        holder.sourceDescription.setText(SourceList.get(position).getDescription());
        holder.sourceLanguage.setText(SourceList.get(position).getLanguage().toString());
        holder.sourceCategory.setText(SourceList.get(position).getCategory().toString());
        holder.sourceCountry.setText(SourceList.get(position).getCountry().toString());

    }

    @Override
    public int getItemCount() {
        return SourceList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
