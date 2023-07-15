package com.example.schedulemydiet.homeflow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.example.schedulemydiet.R;
import com.example.schedulemydiet.adapters.RecipeListAdapter;
import com.example.schedulemydiet.adapters.SimpleListAdapter;
import com.example.schedulemydiet.databinding.ActivityRecipeDetailsBinding;
import com.example.schedulemydiet.models.food.FinalRecipes;
import com.example.schedulemydiet.models.food.Hit;
import com.example.schedulemydiet.models.food.Recipe;

import java.util.List;

public class RecipeDetailsActivity extends AppCompatActivity {
    ActivityRecipeDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecipeDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle("Recipe Details");
        Recipe myModel = (Recipe)((getIntent().getSerializableExtra("data")));
        refreshView(myModel);
    }

    void refreshView(Recipe data) {
        SimpleListAdapter adpter = new SimpleListAdapter(data, this);
        binding.idRecipeDetailsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.idRecipeDetailsRecyclerView.setAdapter(adpter);
        binding.idRecipeDetailsRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }
}