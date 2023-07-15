package com.example.schedulemydiet.homeflow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.schedulemydiet.R;
import com.example.schedulemydiet.databinding.ActivityReceipeListBinding;
import com.example.schedulemydiet.adapters.RecipeListAdapter;
import com.example.schedulemydiet.helpers.DatabaseHelper;
import com.example.schedulemydiet.helpers.TaskCompletion;
import com.example.schedulemydiet.models.food.FinalRecipes;
import com.example.schedulemydiet.models.food.Hit;

import java.util.List;

public class RecipeListActivity extends AppCompatActivity implements RecipeListAdapter.RecipeClickListener {

    ActivityReceipeListBinding binding;
    List<Hit> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReceipeListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle("Recipes ");

        FinalRecipes myModel = (FinalRecipes)((getIntent().getSerializableExtra("data")));
        data = myModel.hits;
        refreshView();
    }

    void refreshView() {
         RecipeListAdapter adpter = new RecipeListAdapter(data, this, this, false);
        binding.idMainRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.idMainRecyclerView.setVisibility(View.VISIBLE);
        binding.idNoDataAvailable.setVisibility(View.INVISIBLE);
        binding.idMainRecyclerView.setAdapter(adpter);
        binding.idMainRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onClickItem(View vue, int position) {
        if (vue.getId() == R.id.id_fav_imageView) {
            DatabaseHelper.getInstance().saveFavouriteRecipe(data.get(position), new TaskCompletion() {
                @Override
                public void taskCompletion(boolean isSuccess, Object data) {
                    Toast.makeText(getBaseContext(), "Favourite Added", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Intent intent = new Intent(RecipeListActivity.this, RecipeDetailsActivity.class);
            intent.putExtra("data", data.get(position).recipe);
            startActivity(intent);
        }
    }
}