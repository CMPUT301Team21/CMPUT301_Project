package com.example.myhw;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
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

        /**
         * Create a listview holder
         *  @return
         *       Return holder
         */
        @Override
        public ItemIngredientBinding createHolder(ViewGroup parent) {
            return ItemIngredientBinding.inflate(getLayoutInflater(), parent, false);
        }

        /**
         * Set fragment information
         */
        @SuppressLint("SetTextI18n")
        @Override
        public void bind(ItemIngredientBinding itemIngredientBinding, Ingredient ingredient, int position) {
            itemIngredientBinding.tvCategory.setText("Category:" + ingredient.category);

            itemIngredientBinding.tvCount.setText("Count:" + (Math.abs(ingredient.count)));
            itemIngredientBinding.tvUnitCost.setText("Unit:" + ingredient.unit);

            itemIngredientBinding.tvDescription.setText("Description:" + ingredient.description);
            itemIngredientBinding.tvLocation.setText("Location:" + ingredient.location);
            itemIngredientBinding.tvLocation.setVisibility(View.GONE);
            itemIngredientBinding.tvBastBeforeDate.setText("Bast Before Date:" + ingredient.time);
            itemIngredientBinding.tvBastBeforeDate.setVisibility(View.GONE);
            if (currentIndex == position) {
                itemIngredientBinding.getRoot().setBackgroundColor(Color.GRAY);
            } else {
                itemIngredientBinding.getRoot().setBackgroundColor(Color.WHITE);
            }
            itemIngredientBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position == currentIndex) {
                        currentIndex = -1;
                    } else {
                        currentIndex = position;
                    }
                    adapter.notifyDataSetChanged();
                }
            });
        }
    };

    /**
     * Initialize data.
     */
    @Override
    protected void initData() {
        viewBinder.rvData.setAdapter(adapter);
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
    }

    /**
     * Initialize listener
     */
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
    }

    /**
     * Set item selected
     * @param item This is a menu item.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_shopping:
                if (currentIndex == -1) {
                    break;
                }
                startActivity(new Intent(getActivity(), AddIngredientActivity.class)
                        .putExtra("ingredient", adapter.getData().get(currentIndex))
                        .putExtra("type", 1)
                );
                break;

            case R.id.menu_sort_shopping: {
                showShoppingListSort();
            }
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Display sorted shopping list
     */
    private void showShoppingListSort() {
        String[] items = getResources().getStringArray(R.array.orderBy);
        new AlertDialog.Builder(requireActivity()).setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                viewModel.changeShoppingListOrderBy(items[which]);
            }
        }).show();
    }
}
