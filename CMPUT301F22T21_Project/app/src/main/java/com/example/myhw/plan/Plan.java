package com.example.myhw.plan;

import com.example.myhw.Ingredient.Ingredient;
import com.example.myhw.recipes.Recipes;
import com.google.firebase.firestore.DocumentId;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Build a hash map
 */
public class Plan {
    public String time;
    public List<Ingredient> ingredients;
    public List<Recipes> recipes;

    public Map<String, Object> toMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("time", time);
        map.put("ingredients", ingredients);
        map.put("recipes", recipes);
        return map;
    }
}
