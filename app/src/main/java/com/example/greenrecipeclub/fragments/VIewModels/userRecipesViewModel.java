package com.example.greenrecipeclub.fragments.VIewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.greenrecipeclub.model.Model;
import com.example.greenrecipeclub.model.Recipe;

import java.util.List;

public class userRecipesViewModel extends ViewModel {

    LiveData<List<Recipe>> userRecipes;

    public LiveData<List<Recipe>> getUserRecipes(String userId){
        if (userRecipes == null)
            userRecipes = Model.instance.getUserRecipes(userId);
        return userRecipes;
    }

    public void refresh(Model.CompListener listener){
        Model.instance.refreshRecipesList(listener);
    }

}
