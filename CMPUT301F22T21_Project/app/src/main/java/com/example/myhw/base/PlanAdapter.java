package com.example.myhw.base;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myhw.Ingredient.AddIngredientActivity;
import com.example.myhw.Ingredient.Ingredient;
import com.example.myhw.databinding.ItemIngredientBinding;
import com.example.myhw.databinding.ItemPlanRecipesBinding;
import com.example.myhw.helper.FirebaseUtil;
import com.example.myhw.recipes.Recipes;
import com.example.myhw.recipes.RecipesDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class PlanAdapter extends RecyclerView.Adapter<BindHolder<?>> {
    List<Object> objects = new ArrayList<>();

    /**
     * This function returns the object in the list
     * @return object
     */
    public List<Object> getData() {
        return objects;
    }

    /**
     * This is a holder that stores bind. Helps to inflate
     * @param parent
     * @param viewType
     * @return BindHolder
     */
    @NonNull
    @Override
    public BindHolder<?> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1) {
            return new BindHolder<>(ItemIngredientBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else {
            return new BindHolder<>(ItemPlanRecipesBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }

    }

    /**
     * Choose whether Ingredient or Recipe
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull BindHolder<?> holder, int position) {
        int itemViewType = getItemViewType(position);
        Object object = objects.get(position);
        if (itemViewType == 1) {
            bindIngredient((ItemIngredientBinding) holder.getVb(), (Ingredient) object);
        } else if (itemViewType == 2) {
            bindRecipes((ItemPlanRecipesBinding) holder.getVb(), (Recipes) object);
        }
    }

    /**
     * Set information of Recipes
     * @param itemPlanRecipesBinding Recipe view
     * @param recipes Recipe
     */
    private void bindRecipes(ItemPlanRecipesBinding itemPlanRecipesBinding, Recipes recipes) {
        itemPlanRecipesBinding.tvNumber.setText(recipes.numberOfServings + "");
        itemPlanRecipesBinding.tvTitle.setText(recipes.title);
        FirebaseUtil.loadImage(recipes.photo,itemPlanRecipesBinding.ivImage);
        StringBuilder builder = new StringBuilder();
        for (Ingredient ingredient : recipes.ingredients) {
            builder.append(ingredient.description).append("X").append(ingredient.count).append(",");
        }
        builder.delete(builder.length() - 1, builder.length());
        itemPlanRecipesBinding.tvIngredient.setText(builder.toString());
        itemPlanRecipesBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, RecipesDetailActivity.class);
                intent.putExtra("type", 1);
                intent.putExtra("recipes", recipes);
                context.startActivity(intent);
            }
        });
    }

    /**
     * Set information of Ingredient
     * @param itemIngredientBinding Ingredient view
     * @param ingredient Ingredient
     */
    private void bindIngredient(ItemIngredientBinding itemIngredientBinding, Ingredient ingredient) {
        itemIngredientBinding.tvBastBeforeDate.setVisibility(View.GONE);
        itemIngredientBinding.tvCategory.setVisibility(View.GONE);
        itemIngredientBinding.tvUnitCost.setVisibility(View.GONE);
        itemIngredientBinding.tvLocation.setVisibility(View.GONE);
        itemIngredientBinding.tvDescription.setText(ingredient.description);
        itemIngredientBinding.tvCount.setText(ingredient.count + "");
        itemIngredientBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, AddIngredientActivity.class);
                intent.putExtra("type", 2);
                intent.putExtra("ingredient", ingredient);
                context.startActivity(intent);
            }
        });
    }

    /**
     * Return the number of items
     * @return list size
     */
    @Override
    public int getItemCount() {
        return objects.size();
    }

    /**
     * Return the type of view
     * @param position Position of selected item
     * @return Whether Ingredient or Recipe
     */
    @Override
    public int getItemViewType(int position) {
        if (objects.get(position) instanceof Ingredient) {
            return 1;
        } else if (objects.get(position) instanceof Recipes) {
            return 2;
        }
        return -1;
    }
}
