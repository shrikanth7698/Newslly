package com.shrikanthravi.newslly;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.shrikanthravi.newslly.data.model.Suggestion;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shrikanthravi on 13/12/17.
 */


public class SearchSuggestionAdapter extends RecyclerView.Adapter<SearchSuggestionAdapter.MyViewHolder> {
    private List<Suggestion> SuggestionList;
    Context context;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView SuggestionName;
        public ImageView SuggestionImage;
        public CardView SuggestionCardView;

        public MyViewHolder(View view) {
            super(view);
            SuggestionName = (TextView) view.findViewById(R.id.SuggestionName);



        }
    }

    public SearchSuggestionAdapter(List<Suggestion> verticalList, Context context) {
        this.SuggestionList = verticalList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.suggestion_row_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/product_san_regular.ttf");

        String name = SuggestionList.get(position).getData().toString().replace("\"","").trim();
        holder.SuggestionName.setText(name);
        holder.SuggestionName.setTypeface(font);

    }

    @Override
    public int getItemCount() {
        return SuggestionList.size();
    }
}
