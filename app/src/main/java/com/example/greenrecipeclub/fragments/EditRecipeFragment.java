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
    EditText titleChange;
    ImageView theImage;
    EditText ingChange;
    EditText insChange;
    ImageView editImgIcon;
    Button saveBtn;
    Spinner categories;
    Uri recipeImageUri;
    Bitmap recipeImgBitmap;
    static int REQUEST_CODE = 1;

    public EditRecipeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_edit_recipe, container, false);
        titleChange = view.findViewById(R.id.screen_editRecipe_title);
        categories = view.findViewById(R.id.screen_editRecipe_categorySpinner);
        ingChange = view.findViewById(R.id.screen_editRecipe_ing);
        insChange = view.findViewById(R.id.screen_editRecipe_ins);
        editImgIcon = view.findViewById(R.id.screen_editRecipe_img);
        saveBtn = view.findViewById(R.id.screen_editRecipe_saveBtn);
        theImage = view.findViewById(R.id.screen_editRecipe_imgView);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(MyApplication.context,
                R.array.categoryArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categories.setAdapter(adapter);

        recipe = EditRecipeFragmentArgs.fromBundle(getArguments()).getRecipe();

        if (recipe != null)
        {
            setEditRecipeHints();
        }

        editImgIcon.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                chooseImageFromGallery();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateRecipe();
            }
        });


        return view;
    }


    void updateRecipe()
    {

        if (recipeImageUri != null)
        {
            DataWarehouse.imageUploading(recipeImgBitmap, new DataWarehouse.Listener() {
                @Override
                public void onSuccess(String url) {

                    Model.instance.addRecipe(generatedEditedRecipe(url), new Model.Listener<Boolean>() {
                        @Override
                        public void onComplete(Boolean data)
                        {
                            NavController navCtrl = Navigation.findNavController(view);
                            navCtrl.navigateUp();
                            navCtrl.navigateUp();
                        }
                    });
                }

                @Override
                public void onFail()
                {
                    Snackbar.make(view, "Failed to edit post", Snackbar.LENGTH_LONG).show();
                }
            });
        }
        else {
            Model.instance.addRecipe(generatedEditedRecipe(null), new Model.Listener<Boolean>() {
                @Override
                public void onComplete(Boolean data)
                {
                    NavController navCtrl = Navigation.findNavController(view);
                    navCtrl.navigateUp();
                    navCtrl.navigateUp();
                }
            });
        }

    }

    private Recipe generatedEditedRecipe(String imageUrl)
    {

        Recipe currentRecipe = recipe;

        if (titleChange.getText().toString() != null && !titleChange.getText().toString().equals(""))
            currentRecipe.setRecipeName(titleChange.getText().toString());
        else currentRecipe.setRecipeName(recipe.getRecipeName());

        if (ingChange.getText().toString() != null && !ingChange.getText().toString().equals(""))
            currentRecipe.setIngredient(ingChange.getText().toString());
        else currentRecipe.setIngredient(recipe.getIngredient());

        if (insChange.getText().toString() != null && !insChange.getText().toString().equals(""))
            currentRecipe.setInstructions( insChange.getText().toString());
        else currentRecipe.setInstructions(recipe.getInstructions());

        if (categories.getSelectedItem().toString() != null && !categories.getSelectedItem().toString().equals(""))
            currentRecipe.setCategoryId(categories.getSelectedItem().toString());
        else currentRecipe.setCategoryId(recipe.getRecipeId());

        if (imageUrl != null)
            currentRecipe.setRecipeImgUrl(imageUrl);

        return currentRecipe;
    }

    private void setEditRecipeHints()
    {
        if (recipe.getRecipeImgUrl() != null)
        {
            Picasso.get().load(recipe.getRecipeImgUrl()).noPlaceholder().into(theImage);
        }
        titleChange.setText(recipe.getRecipeName());
        insChange.setText(recipe.getInstructions());
        ingChange.setText(recipe.getIngredient());
    }

    private void chooseImageFromGallery()
    {

        try
        {
            Intent openGalleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
            openGalleryIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

            startActivityForResult(openGalleryIntent, REQUEST_CODE);
        }
        catch (Exception e)
        {
            Toast.makeText(getContext(), "Edit post Page: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(data != null && resultCode == RESULT_OK){
            recipeImageUri = data.getData();
            theImage.setImageURI(recipeImageUri);
            recipeImgBitmap = uriToBitmap(recipeImageUri);

        }
        else {
            Toast.makeText(getContext(), "No image was selected", Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap uriToBitmap(Uri selectedFileUri)
    {
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