package com.example.schedulemydiet.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.schedulemydiet.R;
import com.example.schedulemydiet.auth.LoginActivity;
import com.example.schedulemydiet.helpers.DatabaseHelper;
import com.example.schedulemydiet.helpers.TaskCompletion;
import com.example.schedulemydiet.models.MyUserData;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.schedulemydiet.databinding.ActivityNavigationMainBinding;

public class NavigationMainActivity extends AppCompatActivity {

    private ActivityNavigationMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNavigationMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_navigation_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        DatabaseHelper.getInstance().fetchUser(new TaskCompletion<MyUserData>() {
            @Override
            public void taskCompletion(boolean isSuccess, MyUserData data) {
                DatabaseHelper.getInstance().getFavouriteRecipes(null);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_nav_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.id_navmenu_logout_btn) {
            DatabaseHelper.getInstance().mAuth.signOut();
                SharedPreferences.Editor editor = DatabaseHelper.getInstance().getPref().edit();
                editor.clear();
                editor.apply();
                finish();
                Intent newInt = new Intent(this, LoginActivity.class);
                startActivity(newInt);
        } else{
            return super.onOptionsItemSelected(item);

        }
        return true;

    }
}

//https://api.edamam.com/api/recipes/v2?type=public&q=chicken&app_id=cb2116e0&app_key=6f4cbc7a24c657161d29606ba1167029&diet=high-fiber&diet=high-protein&health=mustard-free&cuisineType=Asian&calories=200


