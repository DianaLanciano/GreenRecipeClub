package com.example.greenrecipeclub.model;

import java.util.List;

public class Model {

    public final static Model instance = new Model();
    private  Model(){

    }

    List<Recipe> getAllRecipes(){

        List<Recipe> data = AppLocalDb.db.RecipeDao().getAllRecipes();
        return data;
    }
    void addRecipe(Recipe recipe){
        AppLocalDb.db.RecipeDao().insertRecipe(recipe);
    }
    void deleteRecipeById(String id){

    }


}
