package com.example.greenrecipeclub.fragments.VIewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.greenrecipeclub.model.Model;
import com.example.greenrecipeclub.model.Recipe;

import java.util.List;

public class categoryRecipesViewModel extends ViewModel {
    LiveData<List<Recipe>> categoryRecipes;

    public LiveData<List<Recipe>> getDataByCategory(String categoryId) {
        if (categoryRecipes == null)
            categoryRecipes = Model.instance.getRecipesByCategory(categoryId);
        return categoryRecipes;
    }

    public void refresh(Model.CompListener listener) {
        Model.instance.refreshRecipesList(listener);
    }
}
