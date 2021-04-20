package com.example.greenrecipeclub.fragments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.greenrecipeclub.model.Model;
import com.example.greenrecipeclub.model.Recipe;

import java.util.List;

public class listOfRecipesView extends ViewModel {
    LiveData<List<Recipe>> liveData;

    public LiveData<List<Recipe>> getData() {
        if (liveData == null)
            liveData = Model.instance.getAllRecipes();
        return liveData;
    }

    public LiveData<List<Recipe>> getDataByCategory(String categoryId) {
        if (liveData == null)
            liveData = Model.instance.getAllRecipesPerCategory(categoryId);
        return liveData;
    }

    public void refresh(Model.CompListener listener) {
        Model.instance.refreshRecipesList(listener);
    }
}
