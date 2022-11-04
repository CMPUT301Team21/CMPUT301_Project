package com.example.myhw.plan;

import com.example.myhw.Ingredient.Ingredient;

import java.util.Map;

public class AnotherIngredient extends Ingredient {
    public String ingredientId;
    /**
     * Initialize
     * @param ingredient This is the target ingredient
     */
    public void init(Ingredient ingredient) {
        this.description = ingredient.description;
        this.count = ingredient.count;
        this.unit = ingredient.unit;
        this.category = ingredient.category;
        this.location = ingredient.location;
        this.time = ingredient.time;
        this.ingredientId = ingredient.id;
    }
    /**
     * Build a hash map
     * @return The HashMap
     */
    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = super.toMap();
        map.put("ingredientId", ingredientId);
        return map;
    }
}
