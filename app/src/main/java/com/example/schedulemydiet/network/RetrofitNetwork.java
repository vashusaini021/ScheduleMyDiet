package com.example.schedulemydiet.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitNetwork {
    private final static String baseUrl= "https://api.edamam.com/api/";
    private static RetrofitNetwork instance;
    private Retrofit retrofit;

    private RetrofitNetwork() {
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized RetrofitNetwork getInstance() {
        if (instance == null) {
            instance = new RetrofitNetwork();
        }
        return instance;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}
