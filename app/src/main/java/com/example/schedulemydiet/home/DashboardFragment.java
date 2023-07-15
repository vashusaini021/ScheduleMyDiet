package com.example.schedulemydiet.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.schedulemydiet.R;
import com.example.schedulemydiet.adapters.RecipeListAdapter;
import com.example.schedulemydiet.databinding.FragmentDashboardBinding;
import com.example.schedulemydiet.helpers.DatabaseHelper;
import com.example.schedulemydiet.helpers.TaskCompletion;
import com.example.schedulemydiet.homeflow.RecipeDetailsActivity;
import com.example.schedulemydiet.homeflow.RecipeListActivity;
import com.example.schedulemydiet.models.FavouriteModel;
import com.example.schedulemydiet.models.food.Hit;
import com.example.schedulemydiet.network.Loader;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment implements RecipeListAdapter.RecipeClickListener {

    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        listen();

        if(DatabaseHelper.getInstance().favouriteRecipes != null) {
            refreshView(DatabaseHelper.getInstance().favouriteRecipes.favs);
        } else {
            getNewData();
        }

        return root;
    }


    void refreshView(List<Hit> data) {
        RecipeListAdapter adpter = new RecipeListAdapter(data, getContext(), this, true);
        binding.idFavoriteRecipieRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.idFavoriteRecipieRecyclerview.setVisibility(View.VISIBLE);
        binding.idNoDataAvailable.setVisibility(View.INVISIBLE);
        binding.idFavoriteRecipieRecyclerview.setAdapter(adpter);
        binding.idFavoriteRecipieRecyclerview.setItemAnimator(new DefaultItemAnimator());
    }

    void getNewData() {
        Loader.show(getContext());
        DatabaseHelper.getInstance().getFavouriteRecipes(new TaskCompletion<FavouriteModel>() {
            @Override
            public void taskCompletion(boolean isSuccess, FavouriteModel data) {
                binding.idFavoriteRecipieRecyclerview.setVisibility(View.INVISIBLE);
                binding.idNoDataAvailable.setVisibility(View.VISIBLE);
                Loader.dismiss();
                if (isSuccess && data != null) {
                    refreshView(data.favs);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClickItem(View vue, int position) {
        if (vue.getId() == R.id.id_fav_imageView) {
            DatabaseHelper.getInstance().favouriteRecipes.favs.remove(position);
            DatabaseHelper.getInstance().updateFavouriteRecipe(null, new TaskCompletion<FavouriteModel>() {
                @Override
                public void taskCompletion(boolean isSuccess, FavouriteModel data) {
                    Toast.makeText(getContext(), "Favourite Removed", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Intent intent = new Intent(getActivity(), RecipeDetailsActivity.class);
            intent.putExtra("data", DatabaseHelper.getInstance().favouriteRecipes.favs.get(position).recipe);
            startActivity(intent);
        }
    }
    private void listen() {
        DatabaseHelper.getInstance().db.collection("favouriteRecipes")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e("Favourite List", "listen:error", e);
                            return;
                        }
                        if(binding != null) {
                            getNewData();
                        }
                    }
                });
    }
}
