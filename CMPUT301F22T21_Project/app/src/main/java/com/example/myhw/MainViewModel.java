package com.example.myhw;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myhw.Ingredient.Ingredient;
import com.example.myhw.plan.AnotherIngredient;
import com.example.myhw.plan.Plan;
import com.example.myhw.recipes.Recipes;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.Collator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


//9.每一次点到 shopping list，比较meal plan和storage 数量上的差值，meal plan 里的东西如果storage 没有就把它加到shopping list 更新shopping list
//10. recepie 添加食物的时候，要现场添加新的事物信息（escription amount unit ingredient category）而不是跑到 ingredient storage


public class MainViewModel extends ViewModel {
    private MutableLiveData<List<Ingredient>> ingredients = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<List<Recipes>> recipes = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<List<Ingredient>> shoppingList = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<List<Plan>> plans = new MutableLiveData<>(new ArrayList<>());
    private String shoppingListOrderBy = "description";
    private String recipesOrderBy = "title";

    /**
     * This decide the order of recipes.
     * @param recipesOrderBy This is the order of the recipes
     */
    public void setRecipesOrderBy(String recipesOrderBy) {
        this.recipesOrderBy = recipesOrderBy;
        refreshRecipe();
    }
    /**
     * This returns a list of ingredients
     * @return
     *      Return the ingredients
     */
    public LiveData<List<Ingredient>> observerIngredients() {
        return ingredients;
    }

    /**
     * This returns the shopping list.
     * @return
     *      Return the shopping list
     */
    public LiveData<List<Ingredient>> observerShoppingList() {
        return shoppingList;
    }

    /**
     * This returns the meal plan.
     * @return
     *      Return the meal plan
     */
    public LiveData<List<Plan>> observerPlans() {
        return plans;
    }

    /**
     * This returns the recipes.
     * @return
     *      Return the recipes
     */
    public LiveData<List<Recipes>> observerRecipes() {
        return recipes;
    }

    /**
     * This decide the order of shopping list.
     * @param orderBy This is the order of the shopping list.
     */
    public void changeShoppingListOrderBy(String orderBy) {
        shoppingListOrderBy = orderBy;
        List<Ingredient> value = shoppingList.getValue();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            value.sort((o1, o2) -> {
                if (shoppingListOrderBy.equals("description")) {
                    return Collator.getInstance(Locale.CHINESE).compare(o1.description, o2.description);
                } else {
                    return Collator.getInstance(Locale.CHINESE).compare(o1.category, o2.category);
                }
            });
        }
        shoppingList.setValue(value);
    }

    /**
     * This update the recipe order.
     */
    public void refreshRecipe() {
        FirebaseUtil.getRecipesCollection().orderBy(recipesOrderBy, Query.Direction.ASCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<Recipes> recipesList = new ArrayList<>();
                Log.d("TAG", "->: onSuccess");
                List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot document : documents) {
                    Recipes recipes = document.toObject(Recipes.class);
                    Log.d("TAG", "->: " + recipes.id);
                    recipesList.add(recipes);
                }
                recipes.setValue(recipesList);
            }
        });
    }

    /**
     * This update the meal plan order.
     */
    public void refreshPlans() {
        FirebaseUtil.getPlanCollection().get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<Plan> preData = new ArrayList<>();
                Log.d("TAG", "getPlanCollection->: onSuccess");
                List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot document : documents) {
                    Plan plan = document.toObject(Plan.class);
                    Log.d("TAG", "getPlanCollection->: " + plan.id);
                    preData.add(plan);
                }
                plans.setValue(preData);
            }
        });
    }

    /**
     * This update the ingredient storage order.
     */
    public void refreshIngredients() {
        FirebaseUtil.getIngredientCollection().get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<Ingredient> preData = new ArrayList<>();
                List<Ingredient> shoppingListPreDate = new ArrayList<>();
                List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot document : documents) {
                    Ingredient ingredient = document.toObject(Ingredient.class);
                    Log.d("TAG", "->: " + ingredient.id);
                    preData.add(ingredient);
                    if (ingredient.count < 0) {
                        shoppingListPreDate.add(ingredient);
                    }
                }
                ingredients.setValue(preData);
                shoppingListPreDate.sort((o1, o2) -> {
                    if (shoppingListOrderBy.equals("description")) {
                        return Collator.getInstance(Locale.CHINESE).compare(o1.description, o2.description);
                    } else {
                        return Collator.getInstance(Locale.CHINESE).compare(o1.category, o2.category);
                    }
                });
                shoppingList.setValue(shoppingListPreDate);
            }
        });
    }

    /**
     * This change the count of ingredient in storage.
     * @param id This is the id of the count attribute of the ingredient
     */
    public void changeCount(String id, int count) {
        List<Ingredient> value = ingredients.getValue();
        for (Ingredient ingredient : value) {
            if (ingredient.id.equals(id)) {
                ingredient.count -= count;
                FirebaseUtil.getIngredientCollection().document(id).update(ingredient.toMap()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        refreshIngredients();
                    }
                });
                break;
            }
        }
    }

    /**
     * This add a new plan in the meal plan.
     * @param ingredient This is the target ingredient
     * @param count This is the count of the ingredient
     */
    public void addPlan(Ingredient ingredient, int count) {
        Plan plan = new Plan();
        plan.list = new ArrayList<>();
        AnotherIngredient anotherIngredient = new AnotherIngredient();
        anotherIngredient.init(ingredient);
        anotherIngredient.count = count;
        plan.list.add(anotherIngredient);
        addPlan(plan);
    }

    /**
     * This update the added meal plan into the database.
     * @param plan This is the target meal plan
     */
    public void addPlan(Plan plan) {
        FirebaseUtil.getPlanCollection().add(plan).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                refreshPlans();
            }
        });
    }

    /**
     * Update the meal plan
     * @param ingredient Target ingredient
     * @param plan Target meal plan
     * @param count Target count
     */
    public void updatePlan(Ingredient ingredient, Plan plan, int count) {
        AnotherIngredient existsIngredient = null;
        for (AnotherIngredient anotherIngredient : plan.list) {
            if (anotherIngredient.ingredientId.equals(ingredient.id)) {
                existsIngredient = anotherIngredient;
                break;
            }
        }
        if (existsIngredient != null) {
            existsIngredient.count += count;
        } else {
            existsIngredient = new AnotherIngredient();
            existsIngredient.init(ingredient);
            existsIngredient.count = count;
            plan.list.add(existsIngredient);
        }
        updatePlan(plan);
    }

    /**
     * Update the meal plan
     * @param plan The provided plan
     */
    public void updatePlan(Plan plan) {
        FirebaseUtil.getPlanCollection()
                .document(plan.id)
                .update(plan.toMap())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        refreshPlans();
                    }
                });
    }

    /**
     * Change the count of the ingredient
     * @param recipesIngredients The provided ingredient in recipes
     */
    public void changeCount(List<AnotherIngredient> recipesIngredients) {
//        for (计划 计划item: 计划List){
//            for (食物 食物item: 计划Item.食物List){
//                for (食物 仓库食物Item:仓库List){
//
//                }
//            }
//        }

        List<Ingredient> value = ingredients.getValue();
        for (Ingredient ingredient : value) {
            for (AnotherIngredient recipesIngredient : recipesIngredients) {
                if (ingredient.id.equals(recipesIngredient.ingredientId)) {
                    changeCount(ingredient.id, recipesIngredient.count);
                }
            }
        }
    }
}
