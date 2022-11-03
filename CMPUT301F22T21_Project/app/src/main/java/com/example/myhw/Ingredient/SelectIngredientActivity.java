package com.example.myhw.Ingredient;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.myhw.MainViewModel;
import com.example.myhw.base.BaseBindingActivity;
import com.example.myhw.base.BindAdapter;
import com.example.myhw.databinding.ActivitySelectIngredientBinding;
import com.example.myhw.databinding.ItemIngredientBinding;

import java.util.List;

public class SelectIngredientActivity extends BaseBindingActivity<ActivitySelectIngredientBinding> {
    private BindAdapter<ItemIngredientBinding, Ingredient> adapter = new BindAdapter<ItemIngredientBinding, Ingredient>() {
        @Override
        public ItemIngredientBinding createHolder(ViewGroup parent) {
            return ItemIngredientBinding.inflate(getLayoutInflater(), parent, false);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void bind(ItemIngredientBinding itemIngredientBinding, Ingredient ingredient, int position) {
            itemIngredientBinding.tvCategory.setText("Category:" + ingredient.category);
            itemIngredientBinding.tvBastBeforeDate.setText("Bast Before Date:" + ingredient.time);
            itemIngredientBinding.tvCount.setText("Count:" + (Math.max(ingredient.count, 0)));
            itemIngredientBinding.tvUnitCost.setText("Unit:" + ingredient.unit);
            itemIngredientBinding.tvLocation.setText("Location:" + ingredient.location);
            itemIngredientBinding.tvDescription.setText("Description:" + ingredient.description);
            itemIngredientBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setResult(RESULT_OK, new Intent().putExtra("ingredient", ingredient));
                    finish();
                }
            });
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

    private MainViewModel viewModel;

    @Override
    protected void initData() {
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        viewBinder.rvData.setAdapter(adapter);
        viewModel.refreshIngredients();
        viewModel.observerIngredients().observe(this, new Observer<List<Ingredient>>() {
            @Override
            public void onChanged(List<Ingredient> ingredients) {
                adapter.getData().clear();
                adapter.getData().addAll(ingredients);
                adapter.notifyDataSetChanged();
            }
        });
    }

}