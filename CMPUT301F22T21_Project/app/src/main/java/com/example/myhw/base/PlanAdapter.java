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

    public List<Object> getData() {
        return objects;
    }

    @NonNull
    @Override
    public BindHolder<?> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1) {
            return new BindHolder<>(ItemIngredientBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else {
            return new BindHolder<>(ItemPlanRecipesBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }

    }

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

    @Override
    public int getItemCount() {
        return objects.size();
    }

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
