package com.example.schedulemydiet.network;
import com.google.gson.JsonObject;

public interface ApiCompletionHandler {
    public void apiCallCompleted(boolean isSuccess, JsonObject json);
}
