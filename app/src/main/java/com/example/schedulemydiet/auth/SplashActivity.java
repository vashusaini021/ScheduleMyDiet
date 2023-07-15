package com.example.schedulemydiet.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.schedulemydiet.R;
import com.example.schedulemydiet.helpers.DatabaseHelper;
import com.example.schedulemydiet.home.NavigationMainActivity;
import com.google.firebase.FirebaseApp;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        DatabaseHelper.applicationContext = getApplicationContext();

        String userId = DatabaseHelper.getInstance().getPref().getString("userId","");

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if ( userId!= null && !userId.isEmpty()) {
                    finish();
                    startActivity(new Intent(SplashActivity.this, NavigationMainActivity.class));
                } else {
                    finish();
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }
            }
        };
        Timer timr = new Timer();
        timr.schedule(task, 5000);
    }
}