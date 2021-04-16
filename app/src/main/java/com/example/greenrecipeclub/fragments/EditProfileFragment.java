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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import static android.app.Activity.RESULT_OK;

import com.example.greenrecipeclub.R;
import com.example.greenrecipeclub.model.DataWarehouse;
import com.example.greenrecipeclub.model.Model;
import com.example.greenrecipeclub.model.User;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.io.FileDescriptor;
import java.io.IOException;


public class EditProfileFragment extends Fragment {

    View editProfileView;
    ImageView userImage;
    EditText userNameInputField;
    Button saveChangesButton;
    Uri profileImageUri;
    Bitmap postImgBitmap;
    static int REQUEST_CODE = 1;

    public EditProfileFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        editProfileView = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        userImage = editProfileView.findViewById(R.id.screen_editProfile_img_profileImg);
        userNameInputField = editProfileView.findViewById(R.id.screen_editProfile_input_userNAme);
        saveChangesButton = editProfileView.findViewById(R.id.screen_editProfile_btn_saveChange);

        userImage.setOnClickListener(view -> chooseImageFromGallery());

        saveChangesButton.setOnClickListener(view -> updateUserProfile());
        setEditProfileContent();

        return editProfileView;
    }


    private void setEditProfileContent() {
        if (User.getInstance().profileImageUrl != null)
            Picasso.get().load(User.getInstance().profileImageUrl).noPlaceholder().into(userImage);
        userNameInputField.setHint(User.getInstance().userName);
    }

    void updateUserProfile() {
        final String userName;
        if (userNameInputField.getText().toString() != null && !userNameInputField.getText().toString().isEmpty())
            userName = userNameInputField.getText().toString();
        else userName = User.getInstance().userName;

        if (profileImageUri != null) {
            DataWarehouse.imageUploading(postImgBitmap, new DataWarehouse.Listener() {
                @Override
                public void onSuccess(String uri) {
                    Model.instance.updateUserProfile(userName, uri, data -> {
                        Model.instance.setUserEmail(User.getInstance().email);
                        NavController navCtrl = Navigation.findNavController(editProfileView);
                        navCtrl.navigateUp();
                        navCtrl.navigateUp();
                    });
                }

                @Override
                public void onFail() {
                    Snackbar.make(editProfileView, "Failed to edit user's profile.", Snackbar.LENGTH_LONG).show();
                }
            });
        } else {
            Model.instance.updateUserProfile(userName, null, data -> {
                Model.instance.setUserEmail(User.getInstance().email);
                NavController navCtrl = Navigation.findNavController(editProfileView);
                navCtrl.navigateUp();
                navCtrl.navigateUp();
            });
        }

    }

    private void chooseImageFromGallery() {
        try {
            Intent openGalleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
            openGalleryIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

            startActivityForResult(openGalleryIntent, REQUEST_CODE);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Failed to choose image from gallery: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            profileImageUri = data.getData();
            userImage.setImageURI(profileImageUri);
            //todo: extract method to utils
            postImgBitmap = convertImageUriToBitmap(profileImageUri);

        } else {
            Toast.makeText(getContext(), "Profile image wasn't selected.", Toast.LENGTH_SHORT).show();
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