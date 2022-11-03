package com.example.myhw;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.myhw.Ingredient.AddIngredientActivity;
import com.example.myhw.Ingredient.Ingredient;
import com.example.myhw.base.BaseBindingFragment;
import com.example.myhw.base.BindAdapter;
import com.example.myhw.databinding.FragmentShoppingListBinding;
import com.example.myhw.databinding.ItemIngredientBinding;

import java.util.List;

public class ShoppingListFragment extends BaseBindingFragment<FragmentShoppingListBinding> {
    private MainViewModel viewModel;
    private int currentIndex = -1;
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
            itemIngredientBinding.tvCount.setText("Count:" + (Math.abs(ingredient.count)));
            itemIngredientBinding.tvUnitCost.setText("Unit:" + ingredient.unit);
            itemIngredientBinding.tvLocation.setText("Location:" + ingredient.location);
            itemIngredientBinding.tvDescription.setText("Description:" + ingredient.description);
            if (currentIndex == position) {
                itemIngredientBinding.getRoot().setBackgroundColor(Color.GRAY);
            } else {
                itemIngredientBinding.getRoot().setBackgroundColor(Color.WHITE);
            }
            itemIngredientBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position==currentIndex){
                        currentIndex=-1;
                    }else {
                        currentIndex = position;
                    }
                    adapter.notifyDataSetChanged();
                }
            });
        }
    };

    @Override
    protected void initData() {
        viewBinder.rvData.setAdapter(adapter);
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        viewBinder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    viewModel.changeShoppingListOrderBy(((TextView)view).getText().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void initListener() {
        viewModel.observerShoppingList().observe(this, new Observer<List<Ingredient>>() {
            @Override
            public void onChanged(List<Ingredient> ingredients) {
                currentIndex = -1;
                adapter.getData().clear();
                adapter.getData().addAll(ingredients);
                adapter.notifyDataSetChanged();
            }
        });
        viewBinder.btnAddToStorage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentIndex == -1) return;
                startActivity(new Intent(getActivity(), AddIngredientActivity.class)
                        .putExtra("ingredient", adapter.getData().get(currentIndex))
                        .putExtra("type", 1)
                );
            }
        });
    }

}
