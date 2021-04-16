package com.example.greenrecipeclub.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.greenrecipeclub.R;
import com.example.greenrecipeclub.model.DataWarehouse;
import com.example.greenrecipeclub.model.Model;
import com.example.greenrecipeclub.model.Recipe;
import com.example.greenrecipeclub.model.User;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

public class RecipePageFragment extends Fragment {
    View recipeView;
    TextView recipeTitle;
    ImageView recipeImage;
    ImageView editRecipeIcon;
    TextView ingredientsContent;
    TextView instructionsContent;
    Button deleteButton;
    Recipe recipeButton;

    public RecipePageFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        recipeView = inflater.inflate(R.layout.fragment_recipe_page, container, false);
        recipeTitle = recipeView.findViewById(R.id.screen_singleRecipe_title);
        recipeImage = recipeView.findViewById(R.id.screen_singleRecipe_img);
        editRecipeIcon = recipeView.findViewById(R.id.screen_singleRecipe_editIcon);
        ingredientsContent = recipeView.findViewById(R.id.screen_singleRecipe_ingreadents);
        instructionsContent = recipeView.findViewById(R.id.screen_singleRecipe_instructions);
        deleteButton = recipeView.findViewById(R.id.screen_singleRecipe_deleteRecipe);


        instructionsContent.setMovementMethod(new ScrollingMovementMethod());
        ingredientsContent.setMovementMethod(new ScrollingMovementMethod());

        recipeButton = RecipePageFragmentArgs.fromBundle(getArguments()).getRecipe();
        if (recipeButton != null) {
            recipeTitle.setText(recipeButton.getRecipeName());
            ingredientsContent.setText(recipeButton.getIngredient());
            instructionsContent.setText(recipeButton.getInstructions());

            if (recipeButton.getRecipeImgUrl() != null) {
                Picasso.get().load(recipeButton.getRecipeImgUrl()).placeholder(R.drawable.mainlogo).into(recipeImage);
            } else {
                recipeImage.setImageResource(R.drawable.ic_launcher_background);
            }

        }
        editRecipeIcon.setVisibility(recipeView.INVISIBLE);
        deleteButton.setVisibility(recipeView.INVISIBLE);

        if (isPublisher(recipeButton.getPublisherId(), User.getInstance().userId)) {
            // edit button
            editRecipeIcon.setVisibility(recipeView.VISIBLE);
            editRecipeIcon.setOnClickListener(v -> navigateToEditRecipe(recipeButton));

            // delete button
            deleteButton.setVisibility(recipeView.VISIBLE);
            deleteButton.setOnClickListener(v -> deleteRecipe(recipeButton));
        }

        return recipeView;
    }

    private boolean isPublisher(String publisherId, String userId) {
        return publisherId.equals(userId);
    }


    private void navigateToEditRecipe(Recipe recipe) {
        NavController navController = Navigation.findNavController(getActivity(), R.id.main_navhost);
        RecipePageFragmentDirections.ActionRecipePageFragmentToEditRecipeFragment action = RecipePageFragmentDirections.actionRecipePageFragmentToEditRecipeFragment(recipe);
        navController.navigate(action);
    }

    private void deleteRecipe(Recipe recipeToDelete) {

        Model.instance.deleteRecipe(recipeToDelete, data -> DataWarehouse.deleteImage(recipeButton.getRecipeImgUrl(), new DataWarehouse.Listener() {
            @Override
            public void onSuccess(String url) {
                NavController navController = Navigation.findNavController(recipeView);
                navController.navigateUp();
            }

            @Override
            public void onFail() {
                Snackbar.make(recipeView, "Failed to delete recipe in database", Snackbar.LENGTH_LONG).show();
            }
        }));
    }
}