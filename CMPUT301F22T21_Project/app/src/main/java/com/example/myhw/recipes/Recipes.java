package com.example.myhw.recipes;

import android.util.Log;

import com.example.myhw.Ingredient.Ingredient;
import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.IgnoreExtraProperties;

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
    public String photo;
    public List<Ingredient> ingredients;
}
