package com.example.myhw.plan;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.myhw.Ingredient.Ingredient;
import com.example.myhw.Ingredient.SelectIngredientActivity;
import com.example.myhw.MainViewModel;
import com.example.myhw.R;
import com.example.myhw.base.BaseBindingFragment;
import com.example.myhw.base.BindAdapter;
import com.example.myhw.databinding.FragmentPlanBinding;
import com.example.myhw.databinding.ItemPanBinding;
import com.example.myhw.recipes.SelectRecipeActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlanFragment extends BaseBindingFragment<FragmentPlanBinding> {
    private int currentPlanIndex = -1;
    private MainViewModel viewModel;
    private BindAdapter<ItemPanBinding, Plan> adapter = new BindAdapter<ItemPanBinding, Plan>() {
        @Override
        public ItemPanBinding createHolder(ViewGroup parent) {
            return ItemPanBinding.inflate(getLayoutInflater(), parent, false);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void bind(ItemPanBinding itemPanBinding, Plan plan, int position) {
            itemPanBinding.tvDay.setText("Day" + (position + 1));
            itemPanBinding.tvList.setText(plan.getListStr());
            if (currentPlanIndex == position) {
                itemPanBinding.getRoot().setBackgroundColor(Color.parseColor("#B1B1B1"));
            } else {
                itemPanBinding.getRoot().setBackgroundColor(Color.parseColor("#D7D7D7"));
            }
            itemPanBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentPlanIndex == position) {
                        currentPlanIndex = -1;
                    } else {
                        currentPlanIndex = position;
                    }
                    adapter.notifyDataSetChanged();
                }
            });
        }
    };

    @Override
    protected void initData() {
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        viewBinder.rvData.setAdapter(adapter);
        viewModel.observerPlans().observe(getViewLifecycleOwner(), new Observer<List<Plan>>() {
            @Override
            public void onChanged(List<Plan> plans) {
                adapter.getData().clear();
                Collections.reverse(plans);
                adapter.getData().addAll(plans);
                adapter.notifyDataSetChanged();
            }
        });
        viewModel.refreshPlans();
    }

    @Override
    protected void initListener() {
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_add_plan) {
            showAddFunctionDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAddFunctionDialog() {
        new AlertDialog.Builder(requireActivity()).setItems(
                new CharSequence[]{
                        "Add from ingredient",
                        "Add from recipe",
                }, (dialog, which) -> {
                    if (!adapter.getData().isEmpty()) {
                        if (currentPlanIndex == -1) {
                            showTipsDialog(which);
                            return;
                        }
                    }
                    if (which == 0) {
                        startActivityForResult(new Intent(getActivity(), SelectIngredientActivity.class), 100);
                    } else {
                        startActivityForResult(new Intent(getActivity(), SelectRecipeActivity.class), 101);
                    }
                }).show();
    }

    private void showTipsDialog(int index) {
        new AlertDialog.Builder(getContext()).setMessage("Do you want to plan another day").setNegativeButton("sure", (dialog, which) -> {
            if (index == 0) {
                startActivityForResult(new Intent(getActivity(), SelectIngredientActivity.class), 100);
            } else {
                startActivityForResult(new Intent(getActivity(), SelectRecipeActivity.class), 101);
            }

        }).setPositiveButton("cancel", null).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 100:
                    Ingredient ingredient = (Ingredient) data.getSerializableExtra("ingredient");
                    if (currentPlanIndex == -1) {
                        showAdd2PlanDialog(count -> {
                            viewModel.changeCount(ingredient.id, count);
                            viewModel.addPlan(ingredient, count);
                        });
                    } else {
                        showAdd2PlanDialog(count -> {
                            Plan plan = adapter.getData().get(currentPlanIndex);
                            viewModel.changeCount(ingredient.id, count);
                            viewModel.updatePlan(ingredient, plan, count);
                        });
                    }
                    break;
                case 101: {
                    List<AnotherIngredient> ingredients = (List<AnotherIngredient>) data.getSerializableExtra("ingredients");
                    int number = data.getIntExtra("number", 0);
                    showAdd2PlanDialog(new OnInputListener() {
                        @Override
                        public void result(int count) {
                            for (AnotherIngredient item : ingredients) {
                                item.count = item.count * number * count;
                            }
                            viewModel.changeCount(ingredients);
                            if (currentPlanIndex == -1) {
                                Plan plan = new Plan();
                                plan.list = new ArrayList<>();
                                plan.list.addAll(ingredients);
                                viewModel.addPlan(plan);
                            } else {
                                Plan plan = adapter.getData().get(currentPlanIndex);
                                for (AnotherIngredient anotherIngredient : plan.list) {
                                    for (AnotherIngredient ingredient1 : ingredients) {
                                        if (anotherIngredient.ingredientId.equals(ingredient1.ingredientId)) {
                                            anotherIngredient.count += ingredient1.count;
                                        }
                                    }
                                }
                                viewModel.updatePlan(plan);
                            }
                        }
                    });


                }
                break;

            }
        }
    }

    private void showAdd2PlanDialog(OnInputListener listener) {
        EditText editText = new EditText(getContext());
        editText.setHint("Input Count");
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        new AlertDialog.Builder(getContext())
                .setTitle("Please enter the count")
                .setView(editText)
                .setPositiveButton("CANCEL", null)
                .setNegativeButton("SURE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String countStr = editText.getText().toString();
                        if (countStr.isEmpty()) return;
                        int count = Integer.parseInt(countStr);
                        if (count == 0) return;
                        listener.result(count);
                    }
                })
                .show();
    }

    private interface OnInputListener {
        void result(int input);
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.refreshPlans();
    }
}
