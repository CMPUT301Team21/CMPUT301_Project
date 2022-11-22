package com.example.myhw.Ingredient;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

import androidx.annotation.NonNull;

import com.example.myhw.helper.FirebaseUtil;
import com.example.myhw.base.BaseBindingActivity;
import com.example.myhw.databinding.ActivityAddIngredientBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddIngredientActivity extends BaseBindingActivity<ActivityAddIngredientBinding> {
    private String time = "";
    Calendar instance = Calendar.getInstance();

    @Override
    protected void initListener() {
        viewBinder.btnBack.setOnClickListener(v -> finish());
        viewBinder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUtil.getIngredientCollection()
                        .document(ingredient.description)
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
                String unitCost = viewBinder.etUnitCost.getSelectedItemPosition()==0?"g":"kg";
                String location = viewBinder.etLocation.getText().toString();
                if (category.isEmpty() || count.isEmpty() || description.isEmpty() || location.isEmpty()) {
                    toast("Please complete everything");
                    return;
                }
                if ((type != 1) && time.isEmpty()) {
                    toast("Please choose the best time");
                    return;
                }
                if (ingredient == null) {
                    ingredient = new Ingredient();
                    ingredient.category = category;
                    ingredient.description = description;
                    ingredient.location = location;
                }
                ingredient.time = time;
                ingredient.unit = unitCost;
                ingredient.count = Integer.parseInt(count);
                if (type == 1) {
                    setResult(RESULT_OK, new Intent().putExtra("ingredient", ingredient));
                    finish();
                    return;
                }
                if (oldIngredient != null) {
                    ingredient.count += oldIngredient.count;
                }
                showLoading();
                FirebaseUtil.getIngredientCollection()
                        .document(ingredient.description)
                        .set(ingredient)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                dismissLoading();
                                toast("Update successful");
                                finish();
                            }
                        });

            }
        });
    }

    private Ingredient ingredient;
    private Ingredient oldIngredient;
    private int type;

    @SuppressLint("SetTextI18n")
    @Override
    protected void initData() {
        type = getIntent().getIntExtra("type", 0);
        ingredient = (Ingredient) getIntent().getSerializableExtra("ingredient");
        oldIngredient = (Ingredient) getIntent().getSerializableExtra("oldIngredient");
        setTitle("INGREDIENT STORAGE");
        List<String> units = new ArrayList<>();
        units.add("g");
        units.add("kg");
        if (ingredient != null) {
            viewBinder.etDescription.setEnabled(false);
            viewBinder.etCategory.setText(ingredient.category);
            viewBinder.etLocation.setText(ingredient.location);
            viewBinder.etUnitCost.setSelection(units.indexOf(ingredient.unit),false);
            viewBinder.etCount.setText(ingredient.count + "");
            viewBinder.etDescription.setText(ingredient.description);
            time = ingredient.time == null ? "" : ingredient.time;
            viewBinder.btnDate.setText(time.isEmpty() ? "best before date" : time);
            viewBinder.btnDelete.setVisibility(View.VISIBLE);
        }
        if (type == 1) {
            setTitle("ADD TO RECIPE");
            viewBinder.btnSave.setText("SAVE");
            viewBinder.btnDate.setVisibility(View.GONE);
        }
        if (type == 2) {
            viewBinder.btnSave.setVisibility(View.GONE);
            viewBinder.btnDate.setVisibility(View.GONE);
            viewBinder.btnDelete.setVisibility(View.GONE);
            viewBinder.llLocation.setVisibility(View.GONE);
            viewBinder.etDescription.setEnabled(false);
            viewBinder.etCategory.setEnabled(false);
            viewBinder.etLocation.setEnabled(false);
            viewBinder.etUnitCost.setEnabled(false);
            viewBinder.etCount.setEnabled(false);
            setTitle("DETAILS");
        }

    }
}