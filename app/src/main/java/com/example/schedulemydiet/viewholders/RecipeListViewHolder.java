package com.example.schedulemydiet.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schedulemydiet.R;
import com.example.schedulemydiet.adapters.RecipeListAdapter;

public class RecipeListViewHolder extends RecyclerView.ViewHolder {
    public TextView title;
    public TextView dietlabels;
    public TextView mealtype;
    public TextView cusineType;
    public TextView dishType;
    public TextView calories;
    public ImageView cardImageView;
    public ImageView favIcon;


    public RecipeListViewHolder(@NonNull View itemView, RecipeListAdapter.RecipeClickListener listner) {
        super(itemView);
        title = itemView.findViewById(R.id.id_list_adapter_title_textView);
        cardImageView = itemView.findViewById(R.id.id_list_adapter_imageView);
        dietlabels = itemView.findViewById(R.id.id_list_adapte_dietLabels_textView);
        mealtype = itemView.findViewById(R.id.id_list_adapter_mealtype_textView);
        dishType = itemView.findViewById(R.id.id_list_adapter_dishtype_textView);
        cusineType = itemView.findViewById(R.id.id_list_adapter_cusinetype_textView);
        calories = itemView.findViewById(R.id.id_list_adapter_calories_count_textView);
        favIcon = itemView.findViewById(R.id.id_fav_imageView);

        favIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listner != null) {
                    listner.onClickItem(v, getAdapterPosition());
                }
            }
        });

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listner != null) {
                    listner.onClickItem(v, getAdapterPosition());
                }
            }
        });
    }
}
