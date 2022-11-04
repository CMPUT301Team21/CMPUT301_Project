package com.example.myhw.recipes;

import androidx.annotation.NonNull;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.myhw.helper.FirebaseUtil;
import com.example.myhw.R;
import com.example.myhw.base.BaseBindingActivity;
import com.example.myhw.base.BindAdapter;
import com.example.myhw.databinding.ActivityRecipesDetailBinding;
import com.example.myhw.databinding.ItemRecipeIngredient1Binding;
import com.example.myhw.plan.AnotherIngredient;

public class RecipesDetailActivity extends BaseBindingActivity<ActivityRecipesDetailBinding> {
    private BindAdapter<ItemRecipeIngredient1Binding, AnotherIngredient> adapter = new BindAdapter<ItemRecipeIngredient1Binding, AnotherIngredient>() {
        @Override
        public ItemRecipeIngredient1Binding createHolder(ViewGroup parent) {
            return ItemRecipeIngredient1Binding.inflate(getLayoutInflater(), parent, false);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void bind(ItemRecipeIngredient1Binding itemRecipeIngredientBinding, AnotherIngredient ingredient, int position) {
            if (position == 0) {
                itemRecipeIngredientBinding.tvDescription.setText("description");
                itemRecipeIngredientBinding.tvCount.setText("count");
                itemRecipeIngredientBinding.tvUnitCost.setText("unit");
                itemRecipeIngredientBinding.tvCategory.setText("category");
            } else {
                itemRecipeIngredientBinding.tvDescription.setText(ingredient.description);
                itemRecipeIngredientBinding.tvCount.setText(ingredient.count + "");
                itemRecipeIngredientBinding.tvUnitCost.setText(ingredient.unit);
                itemRecipeIngredientBinding.tvCategory.setText(ingredient.category);
            }


        }
    };

    @Override
    protected void initListener() {
        viewBinder.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    Recipes recipes;

    @SuppressLint("SetTextI18n")
    @Override
    protected void initData() {
        recipes = (Recipes) getIntent().getSerializableExtra("recipes");

        adapter.getData().addAll(recipes.ingredients);
        adapter.getData().add(0, new AnotherIngredient());
        viewBinder.rvIngredient.setAdapter(adapter);
        viewBinder.tvTitle.setText("Title:" + recipes.title);
        viewBinder.tvPreparationTime.setText("Preparation time:" + recipes.preparationTime);
        viewBinder.tvNumber.setText("Number of serving:" + recipes.numberOfServings);
        viewBinder.tvComments.setText("Common:" + recipes.comments);
        viewBinder.tvCategory.setText("Recipe category:" + recipes.category);
        FirebaseUtil.loadImage(recipes.photo, viewBinder.ivImage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_recipes_detial, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        startActivity(new Intent(this, AddRecipeActivity.class).putExtra("recipes", recipes));
        finish();
        return super.onOptionsItemSelected(item);
    }
}