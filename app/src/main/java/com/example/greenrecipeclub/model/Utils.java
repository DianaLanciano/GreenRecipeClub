package com.example.greenrecipeclub.model;

import android.app.Activity;
import android.content.Intent;
import android.provider.MediaStore;
import android.widget.Toast;

import com.example.greenrecipeclub.MyApplication;

public class Utils {

    static int REQ_CODE = 1;

    public static void UploadImageFromGallery(Activity sender) {
        try {
            Intent openGalleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
            openGalleryIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

            sender.startActivityForResult(openGalleryIntent, REQ_CODE);
        } catch (Exception e) {
            Toast.makeText(MyApplication.context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
