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
    View view;
    ImageView userImage;
    EditText userNameInput;
    Button saveChangesButton;
    Uri profileImageUri;
    Bitmap imageBitmap;
    static int REQUEST_CODE = 1;

    public EditProfileFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        userImage = view.findViewById(R.id.screen_editProfile_img_profileImg);
        userNameInput = view.findViewById(R.id.screen_editProfile_input_userNAme);
        saveChangesButton = view.findViewById(R.id.screen_editProfile_btn_saveChange);

        userImage.setOnClickListener(view -> uploadImageFromGallery());

        saveChangesButton.setOnClickListener(view -> updateUserProfile());
        setEditProfileHints();

        return view;
    }


    private void setEditProfileHints() {
        if (User.getInstance().profileImageUrl != null) {
            Picasso.get().load(User.getInstance().profileImageUrl).noPlaceholder().into(userImage);
        }
        userNameInput.setHint(User.getInstance().userName);
    }

    void updateUserProfile() {
        final String username;
        if (userNameInput.getText().toString() != null && !userNameInput.getText().toString().equals(""))
            username = userNameInput.getText().toString();
        else username = User.getInstance().userName;

        if (profileImageUri != null) {
            DataWarehouse.imageUploading(imageBitmap, new DataWarehouse.Listener() {
                @Override
                public void onSuccess(String url) {
                    Model.instance.updateUserProfile(username, url, data -> {
                        Model.instance.setUserAppData(User.getInstance().email);
                        NavController navCtrl = Navigation.findNavController(view);
                        navCtrl.navigateUp();
                    });
                }

                @Override
                public void onFail() {
                    Snackbar.make(view, "Couldn't edit user's profile", Snackbar.LENGTH_LONG).show();
                }
            });
        } else {
            Model.instance.updateUserProfile(username, null, data -> {
                Model.instance.setUserAppData(User.getInstance().email);
                NavController navCtrl = Navigation.findNavController(view);
                navCtrl.navigateUp();
            });
        }

    }

    private void uploadImageFromGallery() {
        try {
            Intent openGalleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
            openGalleryIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

            startActivityForResult(openGalleryIntent, REQUEST_CODE);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Couldn't upload image from the gallery: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            profileImageUri = data.getData();
            userImage.setImageURI(profileImageUri);
            imageBitmap = convertUriToBitmap(profileImageUri);

        } else {
            Toast.makeText(getContext(), "No image was selected", Toast.LENGTH_SHORT).show();
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