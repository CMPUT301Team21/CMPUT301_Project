package com.example.myhw;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myhw.Ingredient.Ingredient;
import com.example.myhw.helper.FirebaseUtil;
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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;


//9.每一次点到 shopping list，比较meal plan和storage 数量上的差值，meal plan 里的东西如果storage 没有就把它加到shopping list 更新shopping list
//10. recepie 添加食物的时候，要现场添加新的事物信息（escription amount unit ingredient category）而不是跑到 ingredient storage


public class MainViewModel extends ViewModel {
    private MutableLiveData<List<Ingredient>> ingredients = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<List<Recipes>> recipes = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<List<Ingredient>> shoppingList = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<List<Plan>> plans = new MutableLiveData<>(new ArrayList<>());
    private String shoppingListOrderBy = "description";
    private String recipesOrderBy = "title";

    public void setRecipesOrderBy(String recipesOrderBy) {
        this.recipesOrderBy = recipesOrderBy;
        refreshRecipe();
    }

    public LiveData<List<Ingredient>> observerIngredients() {
        return ingredients;
    }

    public LiveData<List<Ingredient>> observerShoppingList() {
        return shoppingList;
    }

    public LiveData<List<Plan>> observerPlans() {
        return plans;
    }

    public LiveData<List<Recipes>> observerRecipes() {
        return recipes;
    }

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

    public void refreshPlans() {
        FirebaseUtil.getPlanCollection().get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<Plan> preData = new ArrayList<>();
                List<Ingredient> shoppingListPreDate = new ArrayList<>();
                Log.d("TAG", "getPlanCollection->: onSuccess");
                List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot document : documents) {
                    Plan plan = document.toObject(Plan.class);
                    Log.d("TAG", "getPlanCollection->: " + plan.id);
                    preData.add(plan);
                }
                plans.setValue(preData);
                List<Ingredient> ingredientsValue = ingredients.getValue();
                Map<String, Integer> countMap = new HashMap<>();
                for (Plan plan : preData) {
                    for (AnotherIngredient anotherIngredient : plan.list) {
                        Integer integer = countMap.get(anotherIngredient.ingredientId);
                        if (integer == null) {
                            integer = 0;
                        }
                        integer += anotherIngredient.count;
                        countMap.put(anotherIngredient.ingredientId, integer);
                    }
                }
                for (Ingredient preDatum : ingredientsValue) {
                    Integer integer = countMap.get(preDatum.id);
                    if (integer != null && (integer > preDatum.count)) {
                        preDatum.count = Math.abs(integer - preDatum.count);
                        shoppingListPreDate.add(preDatum);
                    }
                }
                shoppingList.setValue(shoppingListPreDate);
            }
        });
    }

    public void calculateShoppingCart() {
        FirebaseUtil.getPlanCollection().get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<Plan> planList = new ArrayList<>();
                List<Ingredient> shoppingListPreDate = new ArrayList<>();
                List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot document : documents) {
                    Plan plan = document.toObject(Plan.class);
                    planList.add(plan);
                }
                FirebaseUtil.getIngredientCollection().get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<Ingredient> IngredientList = new ArrayList<>();
                        List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot document : documents) {
                            Ingredient ingredient = document.toObject(Ingredient.class);
                            Log.d("TAG", "->: " + ingredient.id);
                            IngredientList.add(ingredient);
                        }
                        Map<String, Integer> countMap = new HashMap<>();
                        for (Plan plan : planList) {
                            for (AnotherIngredient anotherIngredient : plan.list) {
                                Integer integer = countMap.get(anotherIngredient.ingredientId);
                                if (integer == null) {
                                    integer = 0;
                                }
                                integer += anotherIngredient.count;
                                countMap.put(anotherIngredient.ingredientId, integer);
                            }
                        }
                        for (Ingredient preDatum : IngredientList) {
                            Integer integer = countMap.get(preDatum.id);
                            if (integer != null && (integer > preDatum.count)) {
                                preDatum.count = Math.abs(integer - preDatum.count);
                                shoppingListPreDate.add(preDatum);
                            }
                        }
                        shoppingList.setValue(shoppingListPreDate);
                    }
                });

            }
        });


    }

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
                }
                ingredients.setValue(preData);

            }
        });
    }


    public void addPlan(Ingredient ingredient, int count) {
        Plan plan = new Plan();
        plan.list = new ArrayList<>();
        AnotherIngredient anotherIngredient = new AnotherIngredient();
        anotherIngredient.init(ingredient);
        anotherIngredient.count = count;
        plan.list.add(anotherIngredient);
        addPlan(plan);
    }

    public void addPlan(Plan plan) {
        FirebaseUtil.getPlanCollection().add(plan).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                refreshPlans();
            }
        });
    }

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
}
