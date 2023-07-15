
package com.example.schedulemydiet.models.food;

import java.io.Serializable;
import java.util.List;


public class FinalRecipes implements Serializable {

    public Integer from;
    public Integer to;
    public Integer count;
    public Links _links;
    public List<Hit> hits;

}
