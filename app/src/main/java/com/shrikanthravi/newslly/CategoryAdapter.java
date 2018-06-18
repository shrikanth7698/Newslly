package com.shrikanthravi.newslly;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shrikanthravi on 13/12/17.
 */


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
    private List<Category> CategoryList;
    Context context;
    int selectedCount=0;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView CategoryName;
        public ImageView CategoryImage;
        public CardView CategoryCardView;

        public MyViewHolder(View view) {
            super(view);
            CategoryName = (TextView) view.findViewById(R.id.CategoryName);
            CategoryImage = (ImageView) view.findViewById(R.id.CategoryImage);
            CategoryCardView = (CardView) view.findViewById(R.id.CategoryCardView);



        }
    }

    public CategoryAdapter(List<Category> verticalList, Context context) {
        this.CategoryList = verticalList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_row_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/product_san_regular.ttf");

        String name = CategoryList.get(position).getName().replace("-"," ");
        holder.CategoryName.setText(name.substring(0,1).toUpperCase()+name.substring(1));
        holder.CategoryName.setTypeface(font);
        String str = name;
        str = str.contains(" ") ? str.split(" ")[0] : str;
        Picasso.with(context).load("file:///android_asset/"+str+".png").into(holder.CategoryImage);
        if(CategoryList.get(position).getSelected().equals("1")){

            holder.CategoryCardView.setCardBackgroundColor(context.getResources().getColor(R.color.color2));
            holder.CategoryName.setTextColor(context.getResources().getColor(android.R.color.white));
        }
        else{

            holder.CategoryCardView.setCardBackgroundColor(context.getResources().getColor(android.R.color.white));
            holder.CategoryName.setTextColor(context.getResources().getColor(android.R.color.tab_indicator_text));
        }
        holder.CategoryCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //System.out.println("value check "+CategoryList.get(position));
                if(CategoryList.get(position).getSelected().equals("0")){

                    System.out.println("first click");
                    selectedCount=selectedCount+1;
                    holder.CategoryCardView.setCardBackgroundColor(context.getResources().getColor(R.color.color2));
                    CategoryList.get(position).setSelected("1");
                    holder.CategoryName.setTextColor(context.getResources().getColor(android.R.color.white));
                }
                else{
                    selectedCount=selectedCount-1;
                    CategoryList.get(position).setSelected("0");
                    holder.CategoryCardView.setCardBackgroundColor(context.getResources().getColor(android.R.color.white));
                    holder.CategoryName.setTextColor(context.getResources().getColor(android.R.color.tab_indicator_text));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return CategoryList.size();
    }

    public List<Category> getSelected(){
        return CategoryList;
    }

    public int getSelectedCount(){
        return selectedCount;
    }

    public void setSelectedCount(int count){
        this.selectedCount = count;
    }

}

