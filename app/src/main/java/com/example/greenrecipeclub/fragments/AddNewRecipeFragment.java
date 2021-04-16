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
import android.util.Log;
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
    Spinner categoryList;
    EditText recipeName;
    EditText ingredients;
    EditText instruction;
    ImageView addRecipeImg;
    Button uploadRecipeBtn;
    Uri imageUri;
    Bitmap convertedImg;

    public AddNewRecipeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_new_recipe, container, false);
        recipeName = view.findViewById(R.id.screen_newRcipe_input_recipeTitle);
        ingredients = view.findViewById(R.id.screen_newRcipe_input_ingrediantes);
        instruction = view.findViewById(R.id.screen_newRcipe_input_instructions);
        addRecipeImg = view.findViewById(R.id.screen_newRcipe_img_addImageRecipe);
        uploadRecipeBtn = view.findViewById(R.id.screen_newRcipe_btn_uploadRecipe);
        categoryList = (Spinner) view.findViewById(R.id.screen_newRcipe_spinner);

        addRecipeImg.setOnClickListener((view) -> {
            uploadFromGallery();
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(MyApplication.context,
                R.array.categoryArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryList.setAdapter(adapter);

        uploadRecipeBtn.setOnClickListener(v -> {
            uploadRecipeBtn.setEnabled(false);
            if (imageUri != null && recipeName != null && ingredients != null && instruction != null)
                saveRecipe();

            else {
                Toast.makeText(getContext(), "Please fill all fields and add a photo", Toast.LENGTH_SHORT).show();
                uploadRecipeBtn.setEnabled(true);
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
        Log.i("Tag", "Saving recipe");
        DataWarehouse.imageUploading(convertedImg, new DataWarehouse.Listener() {
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
                Snackbar.make(view, "Failed to save recipe in database", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private Recipe createRecipe() {
        Recipe recipe = new Recipe();
        recipe.setRecipeId(UUID.randomUUID().toString());
        recipe.setRecipeName(recipeName.getText().toString());
        recipe.setCategoryId(categoryList.getSelectedItem().toString());
        recipe.setIngredient(ingredients.getText().toString());
        recipe.setInstructions(instruction.getText().toString());
        recipe.setRecipeImgUrl(User.getInstance().profileImageUrl);
        recipe.setPublisherId(User.getInstance().userId);
        recipe.setPublisherName(User.getInstance().userName);

        return recipe;
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null && resultCode == RESULT_OK) {
            imageUri = data.getData();
            addRecipeImg.setImageURI(imageUri);
            convertedImg = convertImageToBitmap(imageUri);
        } else {
            Toast.makeText(getActivity(), "No image was selected", Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap convertImageToBitmap(Uri selectedFileUri) {
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