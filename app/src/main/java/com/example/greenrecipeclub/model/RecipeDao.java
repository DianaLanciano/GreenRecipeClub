package com.example.greenrecipeclub.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RecipeDao {

    @Query("select * from recipe")
    LiveData<List<Recipe>> getAllRecipes();

    @Query("select * from Recipe where categoryId = :categoryId")
    LiveData<List<Recipe>> getAllRecipesPerCategory(String categoryId);

    @Query("select * from Recipe where publisherId = :userId")
    LiveData<List<Recipe>> getAllRecipesPerUser(String userId);

    @Query("select * from Recipe where recipeId = :recipeId")
    Recipe GetRecipeById(String recipeId);

    //inserting and updating
    //... is used when we don't know how many arguments will pass..it can be 0 Recipe, 1 or more...
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllRecipes(Recipe... recipes);

    @Delete
    void deleteRecipe(Recipe recipe);

    @Query("select exists(select * from Recipe where recipeId = :recipeId)")
    boolean isRecipeExists(String recipeId);

    @Query("delete from Recipe where recipeId = :recipeId")
    void deleteByRecipeId(String recipeId);
}
