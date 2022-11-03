package com.example.myhw.recipes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.myhw.FirebaseUtil;
import com.example.myhw.Ingredient.Ingredient;
import com.example.myhw.MainViewModel;
import com.example.myhw.R;
import com.example.myhw.base.BaseBindingFragment;
import com.example.myhw.base.BindAdapter;
import com.example.myhw.databinding.FragmentRecipesBinding;
import com.example.myhw.databinding.ItemRecipesBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;

public class RecipesFragment extends BaseBindingFragment<FragmentRecipesBinding> {

    private MainViewModel viewModel;

    private BindAdapter<ItemRecipesBinding, Recipes> adapter = new BindAdapter<ItemRecipesBinding, Recipes>() {
        @Override
        public ItemRecipesBinding createHolder(ViewGroup parent) {
            return ItemRecipesBinding.inflate(getLayoutInflater(), parent, false);
        }

        @Override
        public void bind(ItemRecipesBinding itemRecipesBinding, Recipes recipes, int position) {
            FirebaseUtil.loadImage(recipes.photo, itemRecipesBinding.ivImage);
            itemRecipesBinding.tvCategory.setText(recipes.category);
            itemRecipesBinding.tvNumber.setText(recipes.numberOfServings + "");
            itemRecipesBinding.tvTitle.setText(recipes.title);
            itemRecipesBinding.tvTime.setText(recipes.preparationTime);
            itemRecipesBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), RecipesDetailActivity.class).putExtra("recipes", recipes));
                }
            });
        }
    };

    @Override
    protected void initData() {
        viewBinder.rvData.setAdapter(adapter);
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        viewModel.observerRecipes().observe(getViewLifecycleOwner(), new Observer<List<Recipes>>() {
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
    protected void initListener() {

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_recipes:
                startActivity(AddRecipeActivity.class);
                break;
            case R.id.menu_sort: {
                showSortDialog();
            }
            break;
        }
        return true;
    }

    private void showSortDialog() {
        new AlertDialog.Builder(getContext()).setItems(new CharSequence[]{
                "Sort by title",
                "Sort by time",
                "Sort by number",
                "Sort by category"
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        viewModel.setRecipesOrderBy("title");
                        break;
                    case 1:
                        viewModel.setRecipesOrderBy("preparationTime");
                        break;
                    case 2:
                        viewModel.setRecipesOrderBy("numberOfServings");
                        break;
                    case 3:
                        viewModel.setRecipesOrderBy("category");
                        break;
                }
            }
        }).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.refreshRecipe();
    }
}
