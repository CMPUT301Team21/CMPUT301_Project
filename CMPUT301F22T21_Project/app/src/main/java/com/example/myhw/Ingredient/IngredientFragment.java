package com.example.myhw.Ingredient;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.myhw.MainViewModel;
import com.example.myhw.R;
import com.example.myhw.base.BaseBindingFragment;
import com.example.myhw.base.BindAdapter;
import com.example.myhw.databinding.FragmentIngredientBinding;
import com.example.myhw.databinding.ItemIngredientBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class IngredientFragment extends BaseBindingFragment<FragmentIngredientBinding> {
    private MainViewModel viewModel;
    private BindAdapter<ItemIngredientBinding, Ingredient> adapter = new BindAdapter<ItemIngredientBinding, Ingredient>() {
        @Override
        public ItemIngredientBinding createHolder(ViewGroup parent) {
            return ItemIngredientBinding.inflate(getLayoutInflater(), parent, false);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void bind(ItemIngredientBinding itemIngredientBinding, Ingredient ingredient, int position) {
            itemIngredientBinding.tvCategory.setText( ingredient.category);
            itemIngredientBinding.tvBastBeforeDate.setText( ingredient.time);
            itemIngredientBinding.tvCount.setText((Math.max(ingredient.count, 0))+"");
            itemIngredientBinding.tvUnitCost.setText( ingredient.unit);
            itemIngredientBinding.tvLocation.setText( ingredient.location);
            itemIngredientBinding.tvDescription.setText( ingredient.description);
            itemIngredientBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), AddIngredientActivity.class).putExtra("ingredient", ingredient));
                }
            });
        }
    };

    @Override
    protected void initData() {
        viewBinder.rvData.setAdapter(adapter);
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        showLoading();
        viewModel.observerIngredients().observe(this, new Observer<List<Ingredient>>() {
            @Override
            public void onChanged(List<Ingredient> ingredients) {
                dismissLoading();
                adapter.getData().clear();
                adapter.getData().addAll(ingredients);
                adapter.notifyDataSetChanged();
            }
        });
        viewBinder.add.setOnClickListener(v -> startActivity(AddIngredientActivity.class));
    }


    @Override
    public void onResume() {
        super.onResume();
        viewModel.refreshIngredients();
    }


    @Override
    protected void initListener() {
    }
}
