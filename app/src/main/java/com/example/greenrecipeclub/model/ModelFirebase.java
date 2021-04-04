package com.example.greenrecipeclub.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelFirebase {

    public interface Listener<T>
    {
        void onComplete();
        void onFail();
    }


    public static void geRecipesByCategory(final Model.Listener<List<Recipe>> listener, String category) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();



    }

    public static void addRecipe(Recipe recipe, Model.Listener<Boolean> listener) {

    }

    public static void deleteRecipe(Recipe recipe, Model.Listener<Boolean> listener) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
    }
}
