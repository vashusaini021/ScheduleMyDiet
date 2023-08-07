package com.example.schedulemydiet.helpers;

import android.content.Context;
import com.example.schedulemydiet.adapters.CheckableSpinnerAdapter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DishFilterHelper {
   // https://api.edamam.com/api/recipes/v2?type=public&q=chicken&app_id=26c668bc&app_key=192064616f12c93db9b1a38f86ab5eb4&diet=high-fiber

    // &nutrients%5BMG%5D=5.0-50.0&nutrients%5BSUGAR%5D=50&nutrients%5BCA%5D=50%2B
    //&nutrients[MG]=5.0-50.0&nutrients[SUGAR]=50&nutrients[CA]=50+


//    String range = "50+";
//    String encodedRange = URLEncoder.encode(range, StandardCharsets.UTF_8.toString());

    //        https://developer.edamam.com/edamam-docs-recipe-api - advance filter
//        https://github.com/whilu/AndroidTagView
    //digest
    //ingredientLines


    Set<String> selectedDietType = new HashSet<String>();
     Set<String> selectedHealthType = new HashSet<String>();
     Set<String> selectedCuisineType = new HashSet<String>();
     Set<String> selectedMealType = new HashSet<String>();
     Set<String> selectedDishType = new HashSet<String>();

    private static DishFilterHelper instance;

    public static synchronized DishFilterHelper getInstance() {
        if (instance == null) {
            instance = new DishFilterHelper();
        }
        return instance;
    }

    public void clearFilter() {
        selectedDietType.clear();
        selectedHealthType.clear();
        selectedCuisineType.clear();
        selectedMealType.clear();
        selectedDishType.clear();
    }

    public CheckableSpinnerAdapter getMyAdapter(FILTER_TYPE filter, Context context) {

        switch (filter) {
            case dietType:
                return  new CheckableSpinnerAdapter(context, "Diet Type", Arrays.asList(dietType),selectedDietType);
            case healthType:
                return  new CheckableSpinnerAdapter(context, "Health Type", Arrays.asList(healthtype),selectedHealthType);
            case cuisineType:
                return  new CheckableSpinnerAdapter(context, "Cuisine Type", Arrays.asList(cuisineType),selectedCuisineType);
            case mealType:
                return  new CheckableSpinnerAdapter(context, "Meal Type", Arrays.asList(mealType),selectedMealType);
            case dishType:
                return  new CheckableSpinnerAdapter(context, "Dish Type", Arrays.asList(dishType),selectedDishType);
            default:
                return  null;
        }
    }
    public Map<String, Object> getRequestParam(String search, String excluded, String calories, String glycemicIndex) {
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("type", "public");
        requestParams.put("q", search.trim());
        requestParams.put("app_id", "26c668bc");
        requestParams.put("app_key", "192064616f12c93db9b1a38f86ab5eb4");

        requestParams.put("diet", selectedDietType);
        requestParams.put("health", selectedHealthType);
        requestParams.put("cuisineType", selectedCuisineType);
        requestParams.put("mealType", selectedMealType);
        requestParams.put("dishType", selectedDishType);

        if (!excluded.equals("")){
            String[] excludedItems = excluded.split(",");
            requestParams.put("excluded", new HashSet<>(Arrays.asList(excludedItems)));
        }
        if (!calories.equals("")){
            requestParams.put("calories", calories.trim());
        }
        if (!glycemicIndex.equals("")){
            requestParams.put("glycemicIndex", glycemicIndex.trim());
        }

        return requestParams;
    }


    private String[] dietType = {
        "balanced",
        "high-fiber",
        "high-protein",
        "low-carb",
        "low-fat",
        "low-sodium"
    };

    private String[] healthtype = {
            "alcohol-cocktail",
            "alcohol-free",
            "celery-free",
            "crustacean-free",
            "dairy-free",
            "DASH",
            "egg-free",
            "fish-free",
            "fodmap-free",
            "gluten-free",
            "immuno-supportive",
            "keto-friendly",
            "kidney-friendly",
            "kosher",
            "low-potassium",
            "low-sugar",
            "lupine-free",
            "Mediterranean",
            "mollusk-free",
            "mustard-free",
            "no-oil-added",
            "paleo",
            "peanut-free",
            "pescatarian",
            "pork-free",
            "red-meat-free",
            "sesame-free",
            "shellfish-free",
            "soy-free",
            "sugar-conscious",
            "sulfite-free",
            "tree-nut-free",
            "vegan",
            "vegetarian",
            "wheat-free"
    };

    private String[] cuisineType = {
              "American",
              "Asian",
              "British",
              "Caribbean",
              "Central Europe",
              "Chinese",
              "Eastern Europe",
              "French",
              "Indian",
              "Italian",
              "Japanese",
              "Kosher",
              "Mediterranean",
              "Mexican",
              "Middle Eastern",
              "Nordic",
              "South American",
              "South East Asian",
      };

    private String[] mealType = {
              "Breakfast",
              "Dinner",
              "Lunch",
              "Snack",
              "Teatime"
      };

    private String[] dishType = {
              "Biscuits and cookies",
              "Bread",
              "Cereals",
              "Condiments and sauces",
              "Desserts",
              "Drinks",
              "Main course",
              "Pancake",
              "Preps",
              "Preserve",
              "Salad",
              "Sandwiches",
              "Side dish",
              "Soup",
              "Starter",
              "Sweets"
      };

      public enum  FILTER_TYPE {

          dietType,
          healthType,
          cuisineType,
          mealType,
          dishType,
      }
}
