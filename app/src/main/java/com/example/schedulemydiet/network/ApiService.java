package com.example.schedulemydiet.network;

import android.content.Context;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ApiService {

    private static ApiService instance;

    public static synchronized ApiService getInstance() {
        if (instance == null) {
            instance = new ApiService();
        }
        return instance;
    }

    public void makeApiCall(Context context,
                            RequestType requestType,
                            String endpoint,
                            Map<String, Object> params,
                            ApiCompletionHandler callback) {

        Loader.show(context);
        Request request;
        if(requestType == RequestType.GET) {
            request = makeGetRequest(params, endpoint);
        } else {
            request = makePostRequest(params, endpoint);
        }

        RetrofitNetwork.getInstance().getRetrofit().callFactory().newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Loader.dismiss();
                e.printStackTrace();
                Log.d("API", "URL: " + call.request().url());
                Log.e("API", "Failure: " + e.getMessage());
                callback.apiCallCompleted(false, null);
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                Loader.dismiss();
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    JsonElement jsonElement = new JsonParser().parse(responseData);
                    Log.d("API", "URL: " + call.request().url());
                    Log.d("API", "Success: " + jsonElement);
                    callback.apiCallCompleted(true, jsonElement.getAsJsonObject());
                } else {
                    Log.d("API", "URL: " + call.request().url());
                    Log.e("API", "Failure: " + response.toString());
                    callback.apiCallCompleted(false, null);
                }
            }
        });
    }

    //Internal Helper Methods
    private Request makeGetRequest(Map<String, Object> params, String endpoint) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(RetrofitNetwork.getInstance().getRetrofit().baseUrl().toString() + endpoint).newBuilder();

        if (params != null) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                if (value instanceof Set) {
                    Set<String> listValue = (Set<String>) value;
                    for (String item : listValue) {
                        urlBuilder.addQueryParameter(key, item.trim());
                    }
                } else {
                    urlBuilder.addQueryParameter(key, String.valueOf(value));
                }
            }
        }

        String url = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        return request;
    }

    private Request makePostRequest(Map<String, Object> params, String endpoint) {
        Request.Builder requestBuilder = new Request.Builder()
                .url(RetrofitNetwork.getInstance().getRetrofit().baseUrl().toString() + endpoint)
                .post(null); // Set initial request without a body
        if (params != null) {
            // Add request body if params is not null
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), new Gson().toJson(params));
            requestBuilder.post(requestBody);
        }
        Request request = requestBuilder.build();
        return request;
    }

    public enum RequestType {
        GET,
        POST,
        PUT
    }
}


