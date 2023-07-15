package com.example.schedulemydiet.helpers;

public interface TaskCompletion<T> {
    public void taskCompletion(boolean isSuccess, T data);
}
