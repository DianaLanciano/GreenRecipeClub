package com.example.greenrecipeclub.model;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

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


}
