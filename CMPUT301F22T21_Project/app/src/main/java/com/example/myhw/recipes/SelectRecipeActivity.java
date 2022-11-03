package com.example.myhw.recipes;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.myhw.FirebaseUtil;
import com.example.myhw.MainViewModel;
import com.example.myhw.base.BaseBindingActivity;
import com.example.myhw.base.BindAdapter;
import com.example.myhw.databinding.ActivitySelectRecipeBinding;
import com.example.myhw.databinding.ItemSelectRecipesBinding;

import java.io.Serializable;
import java.util.List;

public class SelectRecipeActivity extends BaseBindingActivity<ActivitySelectRecipeBinding> {
    private MainViewModel viewModel;
    private BindAdapter<ItemSelectRecipesBinding, Recipes> adapter = new BindAdapter<ItemSelectRecipesBinding, Recipes>() {
        @Override
        public ItemSelectRecipesBinding createHolder(ViewGroup parent) {
            return ItemSelectRecipesBinding.inflate(getLayoutInflater(), parent, false);
        }

        @Override
        public void bind(ItemSelectRecipesBinding itemSelectRecipesBinding, Recipes recipes, int position) {
            FirebaseUtil.loadImage(recipes.photo, itemSelectRecipesBinding.ivImage);
            itemSelectRecipesBinding.tvIngredient.setText(recipes.getIngredients());
            itemSelectRecipesBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setResult(RESULT_OK,new Intent()
                            .putExtra("ingredients", (Serializable) recipes.ingredients)
                            .putExtra("number",recipes.numberOfServings)
                    );
                    finish();
                }
            });
        }
    };

    @Override
    protected void initListener() {
        viewModel.observerRecipes().observe(this, new Observer<List<Recipes>>() {
            @Override
            public void onChanged(List<Recipes> recipes) {
                adapter.getData().clear();
                adapter.getData().addAll(recipes);
                adapter.notifyDataSetChanged();
            }
        });
        viewModel.refreshRecipe();
    }

    @Override
    protected void initData() {
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        viewBinder.rvData.setAdapter(adapter);

    }
}