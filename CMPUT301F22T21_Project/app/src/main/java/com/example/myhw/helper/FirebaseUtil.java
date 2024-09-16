<<<<<<< HEAD
package com.example.myhw.helper;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.myhw.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseUtil {
    /**
     * Connect Firebase with Ingredient
     * @return Firebase
     */
    public static CollectionReference getIngredientCollection() {
        return FirebaseFirestore.getInstance().collection("Ingredient");
    }

    /**
     * Connect Firebase with Recipe
     * @return Firebase
     */
    public static CollectionReference getRecipesCollection() {
        return FirebaseFirestore.getInstance().collection("Recipes");
    }

    /**
     * Connect Firebase with Plan
     * @return Firebase
     */
    public static CollectionReference getPlanCollection() {
        return FirebaseFirestore.getInstance().collection("Plan");
    }

    /**
     * Load Images from firebase
     */
    public static void loadImage(String file, ImageView imageView) {
        if (file.isEmpty()){
            Glide.with(imageView).load(R.drawable.ic_shape).into(imageView);
        }else {
            Glide.with(imageView).load(getImagesReference().child(file)).into(imageView);
        }
    }

    /**
     * Find the index of Ingredient in Storage
     * @return Firebase
     */
    public static StorageReference getImagesReference() {
        return FirebaseStorage.getInstance().getReference().child("Images");
    }

}
=======
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
>>>>>>> 03150a9d2766ca86201d08fd172dab617f1a88ad
