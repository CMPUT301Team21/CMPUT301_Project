package com.example.myhw;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import android.widget.ListView;

import com.example.myhw.Ingredient.AddIngredientActivity;
import com.example.myhw.Ingredient.Ingredient;
import com.example.myhw.recipes.Recipes;

import org.junit.Test;

import java.util.ArrayList;

public class ProjectUnitTest {
    private Ingredient mockIngredient(){
        Ingredient ingredient = new Ingredient();
        ingredient.count = 12;
        ingredient.unit = "kg";
        ingredient.description = "test";
        ingredient.location = "fridge";
        ingredient.category = "testType";
        ingredient.time = "2020-08-19";

        return ingredient;
    }

    @Test
    public void testIngredientEqual(){
        Ingredient ingredient1 = new Ingredient();
        ingredient1.count = 12;
        ingredient1.unit = "kg";
        ingredient1.description = "test";
        ingredient1.location = "fridge";
        ingredient1.category = "testType";
        ingredient1.time = "2020-08-19";

        Ingredient ingredient2 = new Ingredient();
        ingredient2.count = 12;
        ingredient2.unit = "kg";
        ingredient2.description = "test";
        ingredient2.location = "fridge";
        ingredient2.category = "testType";
        ingredient2.time = "2020-08-08";

        boolean result = ingredient1.equals(ingredient2);
        assertTrue(result);

        assertTrue(ingredient1.equals(ingredient1));
    }

    @Test
    public void testRecipesEqual(){
        ArrayList<Ingredient> list_ing = new ArrayList<>();
        Ingredient ingredient1 = new Ingredient();
        ingredient1.count = 12;
        ingredient1.unit = "kg";
        ingredient1.description = "test";
        ingredient1.location = "fridge";
        ingredient1.category = "testType";
        ingredient1.time = "2020-08-19";
        list_ing.add(ingredient1);
        Recipes recipes1 = new Recipes();
        recipes1.category = "testCat";
        recipes1.id = "testID";
        recipes1.numberOfServings = 3;
        recipes1.title = "testTitle";
        recipes1.comments = "testCom";
        recipes1.photo = "testPhoto";
        recipes1.preparationTime = "40";
        recipes1.ingredients = list_ing;

        Recipes recipes2 = new Recipes();
        recipes2.category = "testCat";
        recipes2.id = "testID";
        recipes2.numberOfServings = 3;
        recipes2.title = "testTitle_non";
        recipes2.comments = "testCom";
        recipes2.photo = "testPhoto";
        recipes2.preparationTime = "40";
        //recipes1.ingredients.add(ingredient1);

        boolean result;
        result = recipes1.equals(recipes2);
        assertFalse(result);

        result = recipes1.equals(recipes1);
        assertTrue(result);

    }

}
