
package com.example.schedulemydiet.models.food;

import java.io.Serializable;
import java.util.List;


public class Recipe implements Serializable {

    public String uri;
    public String label;
    public String image;
    public Images images;
    public String source;
    public String url;
    public String shareAs;
    public Double yield;
    public List<String> dietLabels;
    public List<String> healthLabels;
    public List<String> cautions;
    public List<String> ingredientLines;
    public List<Ingredient> ingredients;
    public Double calories;
    public Double totalWeight;
    public List<String> cuisineType;
    public List<String> mealType;
    public List<String> dishType;
    public TotalNutrients totalNutrients;
    public TotalNutrients totalDaily;
    public List<Digest> digest;

    //Extra for
    public Double totalCO2Emissions;
    public String co2EmissionsClass;
    public Double totalTime;



    //Helper Methods

    public String getMealTypeString() {
        String data  = "";
        for(String str: mealType){
            data += (str.substring(0, 1).toUpperCase() + str.substring(1)) + ", ";
        }

        return  data.substring(0, data.length() - 2);
    }

    public String getCuisineTypeString() {
        String data  = "";
        for(String str: cuisineType){
            data += (str.substring(0, 1).toUpperCase() + str.substring(1)) + ", ";
        }

        return  data.substring(0, data.length() - 2);
    }

    public String getDishTypeString() {
        String data  = "";
        for(String str: dishType){
            data += (str.substring(0, 1).toUpperCase() + str.substring(1)) + ", ";
        }

        return  data.substring(0, data.length() - 2);
    }

    public String getLabelsString() {

        String data  = "";

        for(String str: cautions){
            String temp = "<font color=#cc0029>"
                    + (str.substring(0, 1).toUpperCase() + str.substring(1))
                    + "</font>, ";

            data += temp;
        }

        for(String str: dietLabels){
             String temp = "<font color=#17470b>"
                     + (str.substring(0, 1).toUpperCase() + str.substring(1))
                     + "</font>, ";

            data += temp;
        }
        for(String str: healthLabels){
            String temp = "<font color=#17470b>"
                    + (str.substring(0, 1).toUpperCase() + str.substring(1))
                    + "</font>, ";

            data += temp;
        }
        return  data.substring(0, data.length() - 2);
    }


}
