package com.example.schedulemydiet.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.schedulemydiet.R;
import com.example.schedulemydiet.models.food.Digest;
import com.example.schedulemydiet.models.food.Recipe;
import com.example.schedulemydiet.viewholders.LabelsViewHolder;
import com.example.schedulemydiet.viewholders.RecipeListViewHolder;
import com.squareup.picasso.Picasso;
import java.text.DecimalFormat;

public class SimpleListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    Recipe data;
    Context context;

    public SimpleListAdapter(Recipe data, Context cntxt) {
        this.context = cntxt;
        this.data = data;
        System.out.println("DIGEST SIZE--> " + data.digest.size());
        System.out.println("INGREDIENTS SIZE--> " + data.ingredientLines.size());
    }


    @Override
    public int getItemCount() {
        return (data.ingredientLines.size() + data.digest.size() + 3);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return getMyView(viewType, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        setData(holder, position);
    }

    @Override
    public int getItemViewType(int position) {
        return getViewTypeFromPosition(position).ordinal();
    }

    public RecyclerView.ViewHolder getMyView(int viewType, ViewGroup parent) {

        if (viewType == VIEW_TYPE.TABLE_HEADER.ordinal()) {

            View view = LayoutInflater.from(context).inflate(R.layout.recipe_list_layout ,parent, false);
            RecipeListViewHolder viewHolder = new RecipeListViewHolder(view, null);

            return viewHolder;
          } else {
            View view = LayoutInflater.from(context).inflate(R.layout.label_list_layout, parent, false);
            LabelsViewHolder viewHolder = new LabelsViewHolder(view);

            return viewHolder;
        }
    }

    void setData(RecyclerView.ViewHolder holder, int position){
        VIEW_TYPE viewType = getViewTypeFromPosition(position);

        if(viewType == VIEW_TYPE.TABLE_HEADER) {
            RecipeListViewHolder view = (RecipeListViewHolder)holder;

            view.title.setText(data.label);
            view.mealtype.setText("Meal Type: " + data.getMealTypeString());
            view.cusineType.setText("Cuisine Type: " + data.getCuisineTypeString());
            view.dishType.setText("Dish Type: " + data.getDishTypeString());
            view.calories.setText("" + (data.calories.intValue()));
            view.dietlabels.setText(Html.fromHtml(data.getLabelsString()) );
            Picasso.get().load( data.image).into(view.cardImageView);
        } else {
            LabelsViewHolder view = (LabelsViewHolder)holder;
            switch (viewType) {
                case SECTION_HEADER:
                    view.title1.setTypeface(null, Typeface.BOLD_ITALIC);
                    view.layout.setBackgroundColor(Color.parseColor("#FF0000"));
                    if (position == 1) {
                        view.title1.setText("INGREDIENTS");
                    } else {
                        view.title1.setText("CONTENTS");
                    }
                    break;
                case INGREDIENTS:
                    view.title1.setText(data.ingredientLines.get(position-2));
                    break;
                case DIGEST:
                    int index = (position - (data.ingredients.size()+2));
                    if(index >= data.digest.size()){
                        return;
                    }
                    Digest digest = data.digest.get(index);
                    view.title1.setText(digest.label);
                    String str =  ""+ digest.total.intValue() + " "+ digest.unit + " ("+ digest.daily.intValue() +"%)";
                    view.title2.setText(str);
                    break;
            }
        }
    }


    private VIEW_TYPE getViewTypeFromPosition(int position) {
        if (position == 0) {
            return VIEW_TYPE.TABLE_HEADER;
        } else if (position == 1 || position == data.ingredientLines.size()+2) {
            return VIEW_TYPE.SECTION_HEADER;
        } else if (position > 1 && position < data.ingredientLines.size()+2) {
            return VIEW_TYPE.INGREDIENTS;
        } else {
            return VIEW_TYPE.DIGEST;
        }
    }

    String getFormattedDouble(Double value) {
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");

        return decimalFormat.format(value);
    }
    enum VIEW_TYPE {
        TABLE_HEADER,
        SECTION_HEADER,
        INGREDIENTS,
        DIGEST
    }
}
