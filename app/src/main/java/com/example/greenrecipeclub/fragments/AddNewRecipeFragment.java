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

import com.example.greenrecipeclub.model.DataWarehouse;
import com.example.greenrecipeclub.model.Model;
import com.example.greenrecipeclub.model.User;
import com.example.greenrecipeclub.MyApplication;
import com.example.greenrecipeclub.R;
import com.example.greenrecipeclub.model.Recipe;
import com.google.android.material.snackbar.Snackbar;

import static android.app.Activity.RESULT_OK;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.UUID;

public class AddNewRecipeFragment extends Fragment {
    static int REQUEST_CODE = 1;
    View view;
    Spinner categoryListSpinner;
    EditText recipeNameText;
    EditText ingredientsText;
    EditText instructionText;
    ImageView addRecipeImageView;
    Button uploadRecipeButton;
    Uri imageUri;
    Bitmap ConvertedImageBitmap;

    public AddNewRecipeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_new_recipe, container, false);
        recipeNameText = view.findViewById(R.id.screen_newRcipe_input_recipeTitle);
        ingredientsText = view.findViewById(R.id.screen_newRcipe_input_ingrediantes);
        instructionText = view.findViewById(R.id.screen_newRcipe_input_instructions);
        addRecipeImageView = view.findViewById(R.id.screen_newRcipe_img_addImageRecipe);
        uploadRecipeButton = view.findViewById(R.id.screen_newRcipe_btn_uploadRecipe);
        categoryListSpinner = view.findViewById(R.id.screen_newRcipe_spinner);

        addRecipeImageView.setOnClickListener((view) -> {
            uploadFromGallery();
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(MyApplication.context,
                R.array.categoryArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryListSpinner.setAdapter(adapter);

        uploadRecipeButton.setOnClickListener(v -> {
            uploadRecipeButton.setEnabled(false);
            if (imageUri != null && recipeNameText != null && ingredientsText != null && instructionText != null)
                saveRecipe();
            else {
                Toast.makeText(getContext(), "Please fill all fields and add a photo", Toast.LENGTH_SHORT).show();
                uploadRecipeButton.setEnabled(true);
            }
        });

        return view;
    }


    void uploadFromGallery() {
        try {
            Intent openGalleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
            openGalleryIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

            startActivityForResult(openGalleryIntent, REQUEST_CODE);
        } catch (Exception e) {
            Toast.makeText(getActivity(), "New post recipe Page: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    void saveRecipe() {
        final Recipe recipe = createRecipe();
        DataWarehouse.imageUploading(ConvertedImageBitmap, new DataWarehouse.Listener() {
            @Override
            public void onSuccess(String url) {
                recipe.setRecipeImgUrl(url);
                Model.instance.addRecipe(recipe, data -> {
                    NavController navCtrl = Navigation.findNavController(view);
                    navCtrl.navigateUp();
                });
            }

            @Override
            public void onFail() {
                Snackbar.make(view, "Failed to create recipe and save it in database.", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private Recipe createRecipe() {
        Recipe recipe = new Recipe();
        recipe.setRecipeId(UUID.randomUUID().toString());
        recipe.setRecipeName(recipeNameText.getText().toString());
        recipe.setCategoryId(categoryListSpinner.getSelectedItem().toString());
        recipe.setIngredient(ingredientsText.getText().toString());
        recipe.setInstructions(instructionText.getText().toString());
        recipe.setRecipeImgUrl(User.getInstance().profileImageUrl);
        recipe.setPublisherId(User.getInstance().userId);
        recipe.setPublisherName(User.getInstance().userName);

        return recipe;
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null && resultCode == RESULT_OK) {
            imageUri = data.getData();
            addRecipeImageView.setImageURI(imageUri);
            ConvertedImageBitmap = convertUriToBitmap(imageUri);
        } else {
            Toast.makeText(getActivity(), "Please select an Image for the Recipe", Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap convertUriToBitmap(Uri selectedFileUri) {
        try {
            ParcelFileDescriptor parcelFileDescriptor = getContext().getContentResolver().openFileDescriptor(selectedFileUri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            Bitmap bitmapImage = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            parcelFileDescriptor.close();
            return bitmapImage;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}