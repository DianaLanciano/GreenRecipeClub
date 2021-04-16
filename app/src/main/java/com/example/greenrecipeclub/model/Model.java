package com.example.greenrecipeclub.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.greenrecipeclub.MyApplication;

import java.util.List;

public class Model {

    public final static Model instance = new Model();

    public interface Listener<T> {
        void onComplete(T data);
    }

    public interface CompListener {
        void onComplete();
    }

    private Model() {
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
        ModelFirebase.deleteRecipe(recipe, listener);
        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... strings) {
                AppLocalDb.db.RecipeDao().deleteRecipe(recipe);
                return "";
            }
        }.execute();


    }


    public void updateUserProfile(String username, String profileImgUri, Listener<Boolean> listener) {
        ModelFirebase.updateUserProfile(username, profileImgUri, listener);
    }


    public void setUserEmail(String email) {
        ModelFirebase.setUserEmail(email);
    }


    public LiveData<List<Recipe>> getAllRecipes() {
        LiveData<List<Recipe>> liveData = AppLocalDb.db.RecipeDao().getAllRecipes();
        refreshRecipesList(null);
        return liveData;
    }

    public LiveData<List<Recipe>> getRecipesByCategory(String categoryId) {
        LiveData<List<Recipe>> liveData = AppLocalDb.db.RecipeDao().getAllRecipesPerCategory(categoryId);
        refreshRecipesList(null);
        return liveData;
    }

    public void refreshRecipesList(final CompListener listener) {
        long lastUpdated = MyApplication.context.getSharedPreferences("TAG", Context.MODE_PRIVATE).getLong("RecipesLastUpdateDate", 0);
        ModelFirebase.getAllRecipesSince(lastUpdated, new Listener<List<Recipe>>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onComplete(List<Recipe> data) {
                new AsyncTask<String, String, String>() {
                    @Override
                    protected String doInBackground(String... strings) {
                        long lastUpdated = 0;
                        for (Recipe r : data) {
                            AppLocalDb.db.RecipeDao().insertAllRecipes(r);
                            if (r.getLastUpdated() > lastUpdated)
                                lastUpdated = r.getLastUpdated();
                        }
                        SharedPreferences.Editor edit = MyApplication.context.getSharedPreferences("TAG", Context.MODE_PRIVATE).edit();
                        edit.putLong("RecipesLastUpdateDate", lastUpdated);
                        edit.commit();
                        return "";
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        cleanLocalDb();
                        if (listener != null)
                            listener.onComplete();
                    }
                }.execute("");

            }
        });
    }


    public LiveData<List<Recipe>> getUserRecipes(String userId) {
        LiveData<List<Recipe>> liveData = AppLocalDb.db.RecipeDao().getAllRecipesPerUser(userId);
        refreshRecipesList(null);
        return liveData;
    }

    @SuppressLint("StaticFieldLeak")
    private void cleanLocalDb() {
        ModelFirebase.getDeletedRecipesId(new Listener<List<String>>() {
            @Override
            public void onComplete(final List<String> data) {
                new AsyncTask<String, String, String>() {
                    @Override
                    protected String doInBackground(String... strings) {
                        for (String id : data) {
                            Log.d("TAG", "deleted id: " + id);
                            AppLocalDb.db.RecipeDao().deleteByRecipeId(id);
                        }
                        return "";
                    }
                }.execute("");
            }
        });
    }


}
