package com.example.myhw.Ingredient;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

import androidx.annotation.NonNull;

import com.example.myhw.FirebaseUtil;
import com.example.myhw.base.BaseBindingActivity;
import com.example.myhw.databinding.ActivityAddIngredientBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddIngredientActivity extends BaseBindingActivity<ActivityAddIngredientBinding> {
    private String time = "";
    Calendar instance = Calendar.getInstance();

    /**
     * Initialize a listener
     */
    @Override
    protected void initListener() {
        viewBinder.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        viewBinder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUtil.getIngredientCollection()
                        .document(ingredient.id)
                        .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                finish();
                            }
                        });
            }
        });
        viewBinder.btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(AddIngredientActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        time = year + "-" + (month + 1) + "-" + dayOfMonth;
                        viewBinder.btnDate.setText(time);
                    }
                }, instance.get(Calendar.YEAR), instance.get(Calendar.MONTH), instance.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });
        viewBinder.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category = viewBinder.etCategory.getText().toString();
                String count = viewBinder.etCount.getText().toString();
                String description = viewBinder.etDescription.getText().toString();
                String unitCost = viewBinder.etUnitCost.getText().toString();
                String location = viewBinder.etLocation.getText().toString();
                if (category.isEmpty() || count.isEmpty() || description.isEmpty() || unitCost.isEmpty() || location.isEmpty()) {
                    toast("Please complete everything");
                    return;
                }
                if (time.isEmpty()) {
                    toast("Please choose the best time");
                    return;
                }
                Map<String, Object> ingredientMap = new HashMap<>();
                ingredientMap.put("category", category);
                ingredientMap.put("description", description);
                ingredientMap.put("unit", unitCost);
                ingredientMap.put("location", location);
                if (type == 1) {
                    ingredientMap.put("count", ingredient.count + Integer.parseInt(count));
                } else {
                    ingredientMap.put("count", Integer.parseInt(count));
                }

                ingredientMap.put("time", time);
                showLoading();
                if (ingredient != null) {
                    FirebaseUtil.getIngredientCollection()
                            .document(ingredient.id)
                            .update(ingredientMap)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    dismissLoading();
                                    toast("Update successful");
                                    Log.d("TAG", "onSuccess: " + ingredient.id);
                                    finish();
                                }
                            });
                } else {
                    FirebaseUtil.getIngredientCollection()
                            .add(ingredientMap)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    toast("Saved successfully");
                                    dismissLoading();
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("TAG", "onFailure: " + e);
                                    toast("Save failed");
                                }
                            });
                }
            }
        });
    }

    private Ingredient ingredient;
    private int type;

    /**
     * Initialize a data
     */
    @SuppressLint("SetTextI18n")
    @Override
    protected void initData() {
        type = getIntent().getIntExtra("type", 0);
        ingredient = (Ingredient) getIntent().getSerializableExtra("ingredient");
        if (ingredient != null) {
            viewBinder.etCategory.setText(ingredient.category);
            viewBinder.etLocation.setText(ingredient.location);
            if (type == 0) {
                viewBinder.etCount.setText((Math.max(ingredient.count, 0)) + "");
            } else {
                viewBinder.etCount.setText((Math.abs(ingredient.count)) + "");
            }
            viewBinder.etUnitCost.setText(ingredient.unit);
            viewBinder.etDescription.setText(ingredient.description);
            viewBinder.btnDate.setText(time = ingredient.time);
            viewBinder.btnDelete.setVisibility(View.VISIBLE);
        }

    }
}