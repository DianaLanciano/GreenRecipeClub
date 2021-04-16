package com.example.greenrecipeclub.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.greenrecipeclub.MyApplication;
import com.example.greenrecipeclub.R;
import com.example.greenrecipeclub.model.DataWarehouse;
import com.example.greenrecipeclub.model.Model;
import com.example.greenrecipeclub.model.Recipe;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.io.FileDescriptor;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public class EditRecipeFragment extends Fragment {
    View editRecipeView;
    Recipe recipe;
    EditText recipeTitle;
    ImageView imageView;
    EditText ingredientsContent;
    EditText instructionsContent;
    ImageView editImageIcon;
    Button saveButton;
    Spinner categoriesDropdownSpinner;
    Uri recipeImageUri;
    Bitmap recipeImgBitmap;
    static int REQUEST_CODE = 1;

    public EditRecipeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        editRecipeView = inflater.inflate(R.layout.fragment_edit_recipe, container, false);
        recipeTitle = editRecipeView.findViewById(R.id.screen_editRecipe_title);
        categoriesDropdownSpinner = editRecipeView.findViewById(R.id.screen_editRecipe_categorySpinner);
        ingredientsContent = editRecipeView.findViewById(R.id.screen_editRecipe_ing);
        instructionsContent = editRecipeView.findViewById(R.id.screen_editRecipe_ins);
        editImageIcon = editRecipeView.findViewById(R.id.screen_editRecipe_img);
        saveButton = editRecipeView.findViewById(R.id.screen_editRecipe_saveBtn);
        imageView = editRecipeView.findViewById(R.id.screen_editRecipe_imgView);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(MyApplication.context,
                R.array.categoryArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoriesDropdownSpinner.setAdapter(adapter);

        recipe = EditRecipeFragmentArgs.fromBundle(getArguments()).getRecipe();

        if (recipe != null) {
            // load recipe's content
            loadRecipeContent();
        }

        editImageIcon.setOnClickListener(view -> getImageFromGallery());

        saveButton.setOnClickListener(view -> updateRecipe());


        return editRecipeView;
    }

    private void navigateToRecipe(View editRecipeView) {
        NavController navCtrl = Navigation.findNavController(editRecipeView);
        navCtrl.navigateUp();
    }

    void updateRecipe() {
        if (recipeImageUri != null) {
            DataWarehouse.imageUploading(recipeImgBitmap, new DataWarehouse.Listener() {
                @Override
                public void onSuccess(String url) {
                    Model.instance.addRecipe(updateRecipeFields(url), data -> navigateToRecipe(editRecipeView));
                }

                @Override
                public void onFail() {
                    Snackbar.make(editRecipeView, "Failed to upload recipe's  image.", Snackbar.LENGTH_LONG).show();
                }
            });
        } else {
            Model.instance.addRecipe(updateRecipeFields(null), data -> navigateToRecipe(editRecipeView));
        }

    }

    private Recipe updateRecipeFields(String imageUrl) {
        Recipe currentRecipe = recipe;

        if (recipeTitle.getText().toString() != null && !recipeTitle.getText().toString().isEmpty())
            currentRecipe.setRecipeName(recipeTitle.getText().toString());
        else currentRecipe.setRecipeName(recipe.getRecipeName());

        if (ingredientsContent.getText().toString() != null && !ingredientsContent.getText().toString().isEmpty())
            currentRecipe.setIngredient(ingredientsContent.getText().toString());
        else currentRecipe.setIngredient(recipe.getIngredient());

        if (instructionsContent.getText().toString() != null && !instructionsContent.getText().toString().isEmpty())
            currentRecipe.setInstructions(instructionsContent.getText().toString());
        else currentRecipe.setInstructions(recipe.getInstructions());

        if (categoriesDropdownSpinner.getSelectedItem().toString() != null && !categoriesDropdownSpinner.getSelectedItem().toString().isEmpty())
            currentRecipe.setCategoryId(categoriesDropdownSpinner.getSelectedItem().toString());
        else currentRecipe.setCategoryId(recipe.getRecipeId());

        if (imageUrl != null)
            currentRecipe.setRecipeImgUrl(imageUrl);

        return currentRecipe;
    }

    private void loadRecipeContent() {
        if (recipe.getRecipeImgUrl() != null) {
            Picasso.get().load(recipe.getRecipeImgUrl()).noPlaceholder().into(imageView);
        }
        recipeTitle.setText(recipe.getRecipeName());
        instructionsContent.setText(recipe.getInstructions());
        ingredientsContent.setText(recipe.getIngredient());
    }

    private void getImageFromGallery() {
        try {
            Intent openGalleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
            openGalleryIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

            startActivityForResult(openGalleryIntent, REQUEST_CODE);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Edit post Page: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null && resultCode == RESULT_OK) {
            recipeImageUri = data.getData();
            imageView.setImageURI(recipeImageUri);
            recipeImgBitmap = convertImageUriToBitmap(recipeImageUri);

        } else {
            Toast.makeText(getContext(), "No image was selected", Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap convertImageUriToBitmap(Uri selectedFileUri) {
        try {
            ParcelFileDescriptor parcelFileDescriptor = getContext().getContentResolver().openFileDescriptor(selectedFileUri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            parcelFileDescriptor.close();
            return image;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}