package com.example.schedulemydiet.models;

import com.example.schedulemydiet.helpers.DatabaseHelper;
import com.example.schedulemydiet.models.food.Hit;
import com.example.schedulemydiet.models.food.Recipe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FavouriteModel implements Serializable {

    public String userId = DatabaseHelper.getInstance().getPref().getString("userId", "");
    public List<Hit> favs;
    String documentId;

    public FavouriteModel() {
      favs = new ArrayList<Hit>();
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getDocumentId() {
        return documentId;
    }
}
