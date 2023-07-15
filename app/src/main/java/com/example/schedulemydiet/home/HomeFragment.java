package com.example.schedulemydiet.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.schedulemydiet.databinding.FragmentHomeBinding;
import com.example.schedulemydiet.helpers.DishFilterHelper;
import com.example.schedulemydiet.homeflow.RecipeListActivity;
import com.example.schedulemydiet.models.food.FinalRecipes;
import com.example.schedulemydiet.network.ApiCompletionHandler;
import com.example.schedulemydiet.network.ApiService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.util.Map;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        binding.idDietSpinner.setAdapter(DishFilterHelper.getInstance().getMyAdapter(DishFilterHelper.FILTER_TYPE.dietType,getContext()));
        binding.idHealthSpinner.setAdapter(DishFilterHelper.getInstance().getMyAdapter(DishFilterHelper.FILTER_TYPE.healthType,getContext()));
        binding.idCusinetypeSpinner.setAdapter(DishFilterHelper.getInstance().getMyAdapter(DishFilterHelper.FILTER_TYPE.cuisineType,getContext()));
        binding.idMealtypeSpinner.setAdapter(DishFilterHelper.getInstance().getMyAdapter(DishFilterHelper.FILTER_TYPE.mealType,getContext()));
        binding.idDishtypeSpinner.setAdapter(DishFilterHelper.getInstance().getMyAdapter(DishFilterHelper.FILTER_TYPE.dishType,getContext()));
        binding.idRecipeSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                performSearch(query);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        binding.idSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSearch(binding.idRecipeSearchView.getQuery().toString());
            }
        });

        return root;
    }

    void refreshView(FinalRecipes myModel) {
        Intent intent = new Intent(getContext(), RecipeListActivity.class);
        intent.putExtra("data", myModel);
        startActivity(intent);
    }

    private void handleSuccessResponse(JsonObject json) {
        FinalRecipes myModel = new Gson().fromJson(json, FinalRecipes.class);
        getActivity().runOnUiThread(new Runnable(){
            @Override
            public void run(){
                refreshView(myModel);
            }
        });
    }

    private void performSearch(String searchString){
        Map<String, Object>  prams =  DishFilterHelper.getInstance().getRequestParam(
                searchString,
                binding.idEditTextTextExcludedItems.getText().toString(),
                binding.idEditTextCalories.getText().toString(),
                binding.idEditTextGlycemiceIndex.getText().toString()
                );

        makeApiCall(prams);
    }

    private void makeApiCall(Map<String, Object> requestParams) {
        ApiService.getInstance().makeApiCall(
                getContext(),
                ApiService.RequestType.GET,
                "recipes/v2",
                requestParams,
                new ApiCompletionHandler() {
                    @Override
                    public void apiCallCompleted(boolean isSuccess, JsonObject json) {
                        if(!isSuccess) {
                            return;
                        }
                        handleSuccessResponse(json);
                    }
                });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}