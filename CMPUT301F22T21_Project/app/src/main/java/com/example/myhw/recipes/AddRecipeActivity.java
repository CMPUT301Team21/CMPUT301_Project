package com.example.myhw.recipes;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.myhw.FirebaseUtil;
import com.example.myhw.Ingredient.Ingredient;
import com.example.myhw.Ingredient.SelectIngredientActivity;
import com.example.myhw.R;
import com.example.myhw.base.BaseBindingActivity;
import com.example.myhw.base.BindAdapter;
import com.example.myhw.databinding.ActivityAddRecipeBinding;
import com.example.myhw.databinding.ItemRecipeIngredientBinding;
import com.example.myhw.plan.AnotherIngredient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class AddRecipeActivity extends BaseBindingActivity<ActivityAddRecipeBinding> {
    private String imageDocId = "";
    private BindAdapter<ItemRecipeIngredientBinding, AnotherIngredient> adapter = new BindAdapter<ItemRecipeIngredientBinding, AnotherIngredient>() {
        @Override
        public ItemRecipeIngredientBinding createHolder(ViewGroup parent) {
            return ItemRecipeIngredientBinding.inflate(getLayoutInflater(), parent, false);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void bind(ItemRecipeIngredientBinding itemRecipeIngredientBinding, AnotherIngredient recipesIngredient, int position) {
            itemRecipeIngredientBinding.getRoot().setText(recipesIngredient.description + "," + recipesIngredient.count + "," + recipesIngredient.unit + "," + recipesIngredient.category);
        }
    };

    @Override
    protected void initListener() {
        viewBinder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoading();
                FirebaseUtil.getRecipesCollection().document(recipes.id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        dismissLoading();
                        toast("delete success");
                        finish();
                    }
                });
            }
        });
        viewBinder.btnSave.setOnClickListener(v -> {
            String title = viewBinder.etTitle.getText().toString();
            String category = viewBinder.etCategory.getText().toString();
            String comments = viewBinder.etComments.getText().toString();
            String preparationTime = viewBinder.etPreparationTime.getText().toString();
            String number = viewBinder.etNumber.getText().toString();
            if (title.isEmpty() || category.isEmpty() || comments.isEmpty() || preparationTime.isEmpty() || number.isEmpty()) {
                toast("Please complete everything");
                return;
            }
            if (imageDocId.isEmpty()) {
                toast("Please upload pictures");
                return;
            }
            if (adapter.getData().isEmpty()) {
                toast("Please upload at least one ingredient");
                return;
            }
            Map<String, Object> recipeMap = new HashMap<>();
            recipeMap.put("title", title);
            recipeMap.put("numberOfServings", Integer.parseInt(number));
            recipeMap.put("preparationTime", preparationTime);
            recipeMap.put("category", category);
            recipeMap.put("comments", comments);
            recipeMap.put("ingredients", adapter.getData());
            recipeMap.put("photo", imageDocId);
            showLoading();
            if (recipes != null) {
                FirebaseUtil
                        .getRecipesCollection()
                        .document(recipes.id)
                        .update(recipeMap)
                        .addOnSuccessListener(unused -> {
                            dismissLoading();
                            toast("Update successfully");
                            finish();
                        });
            } else {
                FirebaseUtil.getRecipesCollection().add(recipeMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        dismissLoading();
                        toast("Added successfully");
                        finish();
                    }
                });
            }

        });
        viewBinder.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        viewBinder.btnAddIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(AddRecipeActivity.this, SelectIngredientActivity.class), 100);
            }
        });
        viewBinder.ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_GET_CONTENT).setType("image/*"), 101);
            }
        });
    }

    private Recipes recipes;

    @SuppressLint("SetTextI18n")
    @Override
    protected void initData() {
        viewBinder.rvIngredient.setAdapter(adapter);
        recipes = (Recipes) getIntent().getSerializableExtra("recipes");
        if (recipes != null) {
            viewBinder.etTitle.setText(recipes.title);
            viewBinder.etPreparationTime.setText(recipes.preparationTime);
            viewBinder.etNumber.setText(recipes.numberOfServings + "");
            viewBinder.etCategory.setText(recipes.category);
            viewBinder.etComments.setText(recipes.comments);
            FirebaseUtil.loadImage(imageDocId = recipes.photo, viewBinder.ivImage);
            adapter.getData().addAll(recipes.ingredients);
            adapter.notifyDataSetChanged();
            viewBinder.btnDelete.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 100: {
                    Ingredient ingredient = (Ingredient) data.getSerializableExtra("ingredient");
                    showCountInputDialog(ingredient);
                }
                break;
                case 101:
                    try {
                        uploadImage(data.getData());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    private void uploadImage(Uri data) throws FileNotFoundException {
        viewBinder.ivImage.setImageResource(R.drawable.ic_baseline_loop_24);
        String preImageDocId = System.currentTimeMillis() + "";
        StorageReference reference = FirebaseUtil.getImagesReference();
        StorageReference child = reference.child(preImageDocId);
        child.putStream(getContentResolver().openInputStream(data)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                toast("The picture was uploaded successfully.");
                imageDocId = preImageDocId;
                viewBinder.ivImage.setImageURI(data);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                viewBinder.ivImage.setImageResource(R.drawable.ic_baseline_image_24);
                toast("Upload failed->" + e.getLocalizedMessage());
            }
        });
    }

    private void showCountInputDialog(Ingredient ingredient) {
        EditText editText = new EditText(this);
        editText.setHint("Input Count");
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        new AlertDialog.Builder(this)
                .setTitle("Please enter the count")
                .setView(editText)
                .setPositiveButton("CANCEL", null)
                .setNegativeButton("SURE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String countStr = editText.getText().toString().trim();
                        if (countStr.isEmpty()) return;
                        AnotherIngredient recipesIngredient = new AnotherIngredient();
                        recipesIngredient.count = Integer.parseInt(countStr);
                        recipesIngredient.category = ingredient.category;
                        recipesIngredient.description = ingredient.description;
                        recipesIngredient.location = ingredient.location;
                        recipesIngredient.ingredientId = ingredient.id;
                        recipesIngredient.time = ingredient.time;
                        recipesIngredient.unit = ingredient.unit;
                        adapter.getData().add(recipesIngredient);
                        adapter.notifyDataSetChanged();
                    }
                })
                .show();
    }
}