package com.example.myhw;

import com.example.myhw.Ingredient.Ingredient;
import com.example.myhw.helper.FirebaseUtil;
import com.google.android.gms.tasks.OnSuccessListener;

import org.junit.Test;

public class ActionLabTest {
    @Test
    public void CreateIngredient(){
        Ingredient ingredient = new Ingredient();

        ingredient.category = "food";
        ingredient.unit = "10";
        ingredient.description = "Apple";
        ingredient.location = "fridge";
        ingredient.time = "2022-11-30";
        ingredient.count = 10;



    }
}
