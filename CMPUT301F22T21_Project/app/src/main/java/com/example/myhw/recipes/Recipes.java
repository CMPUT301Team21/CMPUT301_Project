package com.example.myhw.recipes;

import com.example.myhw.plan.AnotherIngredient;
import com.google.firebase.firestore.DocumentId;

import java.io.Serializable;
import java.util.List;

public class Recipes implements Serializable {
    @DocumentId
    public String id;
    public String title;
    public String preparationTime;
    public int numberOfServings;
    public String category;
    public String comments;
    public List<AnotherIngredient> ingredients;
    public String photo;

    public String getIngredients() {
        StringBuilder builder = new StringBuilder();
        for (AnotherIngredient ingredient : ingredients) {
            builder.append(ingredient.description).append("\t\t\t\tX").append(ingredient.count).append("\n");
        }
        builder.append("Number Of Servings:").append(numberOfServings);
        return builder.toString();
    }
}
