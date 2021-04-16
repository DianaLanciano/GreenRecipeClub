package com.example.greenrecipeclub.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Recipe implements Serializable {
    @PrimaryKey
    @NonNull
    private String recipeId;
    private String recipeName;
    private String categoryId;
    private String ingredient;
    private String instructions;
    private String publisherId;
    private String publisherName;
    private String recipeImgUrl;
    private long lastUpdated;


    public Recipe() {
        this.recipeId = "";
        this.recipeName = "";
        this.categoryId = "";
        this.publisherId = "";
        this.publisherName = "";
        this.recipeImgUrl = "";
        this.instructions = "";
        this.ingredient = "";
    }

    public Recipe(@NonNull String recipeId, String recipeName, String categoryId, String ingredient
            , String instructions, String recipeImgUrl, String publisherId, String publisherName) {
        this.recipeId = recipeId;
        this.recipeName = recipeName;
        this.categoryId = categoryId;
        this.ingredient = ingredient;
        this.publisherId = publisherId;
        this.publisherName = publisherName;
        this.recipeImgUrl = recipeImgUrl;
        this.instructions = instructions;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    @NonNull
    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(@NonNull String recipeId) {
        this.recipeId = recipeId;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }


    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public String getRecipeImgUrl() {
        return recipeImgUrl;
    }

    public void setRecipeImgUrl(String recipeImgUrl) {
        this.recipeImgUrl = recipeImgUrl;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
