package com.example.greenrecipeclub.model;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.greenrecipeclub.MyApplication;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class Model {

    ModelFirebase modelFirebase = new ModelFirebase();
    public final static Model instance = new Model();

    public interface Listener<T> {
        void onComplete(T data);
    }

    public interface CompListener {
        void onComplete();
    }

    private  Model(){

    }


    @SuppressLint("StaticFieldLeak")
    public void addRecipe(final Recipe recipe, Listener<Boolean> listener) {
        ModelFirebase.addRecipe(recipe, listener);
        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... strings) {
                AppLocalDb.db.RecipeDao().insertAllRecipes(recipe);
                return "";
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void deleteRecipe(final Recipe recipe, Listener<Boolean> listener) {

    }

    public void updateUserProfile(String username, String profileImgUrl, Listener<Boolean> listener) {
        ModelFirebase.updateUserProfile(username, profileImgUrl, listener);
    }


    public void setUserAppData(String email) {
        ModelFirebase.setUserAppData(email);
    }




}
