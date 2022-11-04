package com.example.myhw;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseUtil {
    /**
     * This connects database with ingredients.
     * @return Ingredient collection in database
     */
    public static CollectionReference getIngredientCollection() {
        return FirebaseFirestore.getInstance().collection("Ingredient");
    }

    /**
     * This connects database with recipes.
     * @return Recipes collection in database
     */
    public static CollectionReference getRecipesCollection() {
        return FirebaseFirestore.getInstance().collection("Recipes");
    }

    /**
     * This connects database with meal plans.
     * @return Meal Plan collection in database
     */
    public static CollectionReference getPlanCollection() {
        return FirebaseFirestore.getInstance().collection("Plan");
    }

    /**
     * This connects database with images and load them.
     * @param file  This is the target file
     * @param imageView This is the target image view
     */
    public static void loadImage(String file, ImageView imageView) {
        Glide.with(imageView).load(getImagesReference().child(file)).into(imageView);
    }

    /**
     * This connects database with images.
     * @return Images collection in database
     */
    public static StorageReference getImagesReference() {
        return FirebaseStorage.getInstance().getReference().child("Images");
    }

}
