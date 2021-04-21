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
    View view;
    Recipe recipe;
    EditText titleChangeText;
    ImageView recipeImage;
    EditText ingredientsText;
    EditText instructionsText;
    ImageView editImageView;
    Button saveButton;
    Spinner categoriesSpinner;
    Uri recipeImageUri;
    Bitmap recipeImageBitmap;
    static int REQUEST_CODE = 1;

    public EditRecipeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_edit_recipe, container, false);
        titleChangeText = view.findViewById(R.id.screen_editRecipe_title);
        categoriesSpinner = view.findViewById(R.id.screen_editRecipe_categorySpinner);
        ingredientsText = view.findViewById(R.id.screen_editRecipe_ing);
        instructionsText = view.findViewById(R.id.screen_editRecipe_ins);
        editImageView = view.findViewById(R.id.screen_editRecipe_img);
        saveButton = view.findViewById(R.id.screen_editRecipe_saveBtn);
        recipeImage = view.findViewById(R.id.screen_editRecipe_imgView);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(MyApplication.context,
                R.array.categoryArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoriesSpinner.setAdapter(adapter);

        recipe = EditRecipeFragmentArgs.fromBundle(getArguments()).getRecipe();

        if (recipe != null) {
            setEditRecipeHints();
        }

        editImageView.setOnClickListener(view -> uploadImageFromGallery());
        saveButton.setOnClickListener(view -> updateRecipe());

        return view;
    }


    void updateRecipe() {
        if (recipeImageUri != null) {
            DataWarehouse.uploadImage(recipeImageBitmap, new DataWarehouse.Listener() {
                @Override
                public void onSuccess(String url) {
                    Model.instance.addRecipe(generatedEditedRecipe(url), data -> {
                        NavController navCtrl = Navigation.findNavController(view);
                        navCtrl.navigateUp();
                    });
                }

                @Override
                public void onFail() {
                    Snackbar.make(view, "Failed to update the recipe", Snackbar.LENGTH_LONG).show();
                }
            });
        } else {
            Model.instance.addRecipe(generatedEditedRecipe(null), data -> {
                NavController navCtrl = Navigation.findNavController(view);
                navCtrl.navigateUp();
            });
        }

    }

    private Recipe generatedEditedRecipe(String imageUrl) {
        Recipe currentRecipe = recipe;

        if (titleChangeText.getText().toString() != null && !titleChangeText.getText().toString().equals(""))
            currentRecipe.setRecipeName(titleChangeText.getText().toString());
        else currentRecipe.setRecipeName(recipe.getRecipeName());

        if (ingredientsText.getText().toString() != null && !ingredientsText.getText().toString().equals(""))
            currentRecipe.setIngredient(ingredientsText.getText().toString());
        else currentRecipe.setIngredient(recipe.getIngredient());

        if (instructionsText.getText().toString() != null && !instructionsText.getText().toString().equals(""))
            currentRecipe.setInstructions(instructionsText.getText().toString());
        else currentRecipe.setInstructions(recipe.getInstructions());

        if (categoriesSpinner.getSelectedItem().toString() != null && !categoriesSpinner.getSelectedItem().toString().equals(""))
            currentRecipe.setCategoryId(categoriesSpinner.getSelectedItem().toString());
        else currentRecipe.setCategoryId(recipe.getRecipeId());

        if (imageUrl != null)
            currentRecipe.setRecipeImgUrl(imageUrl);

        return currentRecipe;
    }

    private void setEditRecipeHints() {
        if (recipe.getRecipeImgUrl() != null) {
            Picasso.get().load(recipe.getRecipeImgUrl()).noPlaceholder().into(recipeImage);
        }

        titleChangeText.setText(recipe.getRecipeName());
        instructionsText.setText(recipe.getInstructions());
        ingredientsText.setText(recipe.getIngredient());
    }

    private void uploadImageFromGallery() {
        try {
            Intent openGalleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
            openGalleryIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

            startActivityForResult(openGalleryIntent, REQUEST_CODE);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Failed to upload images from gallery" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null && resultCode == RESULT_OK) {
            recipeImageUri = data.getData();
            recipeImage.setImageURI(recipeImageUri);
            recipeImageBitmap = convertUriToBitmap(recipeImageUri);

        } else {
            Toast.makeText(getContext(), "Please select a recipe image", Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap convertUriToBitmap(Uri selectedFileUri) {
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