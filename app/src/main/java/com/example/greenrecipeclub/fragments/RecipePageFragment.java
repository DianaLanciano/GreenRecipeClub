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

    View view;
    TextView title;
    ImageView recipeImg;
    ImageView editRecipeIcon;
    TextView ingredients;
    TextView instructions;
    Button deleteBtn;
    Recipe recipe;

    public RecipePageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       view = inflater.inflate(R.layout.fragment_recipe_page, container, false);

        title = view.findViewById(R.id.screen_singleRecipe_title);
        recipeImg = view.findViewById(R.id.screen_singleRecipe_img);
        editRecipeIcon = view.findViewById(R.id.screen_singleRecipe_editIcon);
        ingredients =  view.findViewById(R.id.screen_singleRecipe_ingreadents);
        instructions = view.findViewById(R.id.screen_singleRecipe_instructions);
        deleteBtn = view.findViewById(R.id.screen_singleRecipe_deleteRecipe);

        instructions.setMovementMethod(new ScrollingMovementMethod());
        ingredients.setMovementMethod(new ScrollingMovementMethod());


        recipe = RecipePageFragmentArgs.fromBundle(getArguments()).getRecipe();
        if (recipe !=null){


            title.setText(recipe.getRecipeName());
            ingredients.setText(recipe.getIngredient());
            instructions.setText(recipe.getInstructions());

            if (recipe.getRecipeImgUrl() != null)
            {
                Picasso.get().load(recipe.getRecipeImgUrl()).placeholder(R.drawable.mainlogo).into(recipeImg);
            }else {
                recipeImg.setImageResource(R.drawable.ic_launcher_background);
            }

        }
        editRecipeIcon.setVisibility(view.INVISIBLE);
        deleteBtn.setVisibility(view.INVISIBLE);

        if (recipe.getPublisherId().equals(User.getInstance().userId))
        {
            editRecipeIcon.setVisibility(view.VISIBLE);
            editRecipeIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toEditRecipePage(recipe);
                }
            });

            deleteBtn.setVisibility(view.VISIBLE);
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteRecipe(recipe);
                }
            });
        }




        return view;
    }


    private void toEditRecipePage(Recipe recipe) {

        NavController navController = Navigation.findNavController(getActivity(),R.id.main_navhost);

       RecipePageFragmentDirections.ActionRecipePageFragmentToEditRecipeFragment directions =RecipePageFragmentDirections.actionRecipePageFragmentToEditRecipeFragment(recipe);
        navController.navigate(directions);

    }

    private void deleteRecipe(Recipe recipeToDelete) {

        Model.instance.deleteRecipe(recipeToDelete, new Model.Listener<Boolean>() {
            @Override
            public void onComplete(Boolean data) {
                DataWarehouse.deleteImage(recipe.getRecipeImgUrl(), new DataWarehouse.Listener() {
                    @Override
                    public void onSuccess(String url) {
                        NavController navController = Navigation.findNavController(view);
                        navController.navigateUp();
                    }

                    @Override
                    public void onFail() {

                        Snackbar.make(view,"Failed to delete recipe in database",Snackbar.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}