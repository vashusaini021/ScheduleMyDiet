package com.example.schedulemydiet.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.schedulemydiet.R;
import com.example.schedulemydiet.models.food.Hit;
import com.example.schedulemydiet.viewholders.RecipeListViewHolder;
import com.squareup.picasso.Picasso;
import java.util.List;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListViewHolder>{

    List<Hit> data;
    Context context;
    RecipeClickListener listner;
    boolean isfFav = false;

    public RecipeListAdapter(List<Hit> data, Context cntxt, RecipeClickListener listner, boolean isfFav) {
        this.context = cntxt;
        this.data = data;
        this.listner = listner;
        this.isfFav = isfFav;
    }

    @NonNull
    @Override
    public RecipeListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View vue = LayoutInflater.from(context).inflate(R.layout.recipe_list_layout, parent, false);
        RecipeListViewHolder viewHolder = new RecipeListViewHolder(vue, listner);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeListViewHolder holder, int position) {

        holder.title.setText(data.get(position).recipe.label);
        holder.mealtype.setText("Meal Type: " + data.get(position).recipe.getMealTypeString());
        holder.cusineType.setText("Cuisine Type: " + data.get(position).recipe.getCuisineTypeString());
        holder.dishType.setText("Dish Type: " + data.get(position).recipe.getDishTypeString());
        holder.calories.setText("" + (data.get(position).recipe.calories.intValue()));
        holder.dietlabels.setText(Html.fromHtml(data.get(position).recipe.getLabelsString()) );
        Picasso.get().load( data.get(position).recipe.image).into(holder.cardImageView);
        if(isfFav) {
            holder.favIcon.setImageResource(R.drawable.delete);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
    public interface RecipeClickListener {

        void onClickItem( View vue, int position);
    }
}
