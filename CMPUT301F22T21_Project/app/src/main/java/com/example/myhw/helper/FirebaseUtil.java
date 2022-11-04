package com.example.myhw.helper;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseUtil {
    public static CollectionReference getIngredientCollection() {
        return FirebaseFirestore.getInstance().collection("Ingredient");
    }

    public static CollectionReference getRecipesCollection() {
        return FirebaseFirestore.getInstance().collection("Recipes");
    }

    public static CollectionReference getPlanCollection() {
        return FirebaseFirestore.getInstance().collection("Plan");
    }

    public static void loadImage(String file, ImageView imageView) {
        Glide.with(imageView).load(getImagesReference().child(file)).into(imageView);
    }

    public static StorageReference getImagesReference() {
        return FirebaseStorage.getInstance().getReference().child("Images");
    }

}
