package com.example.greenrecipeclub.model;

import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Date;

public class DataWarehouse {

    public interface Listener {
        void onSuccess(String url);

        void onFail();
    }

    public static void imageUploading(Bitmap imageBitmap, final Listener listener) {

        Date date = new Date();
        String imageName = User.getInstance().userName + date.getTime();
        FirebaseStorage storageReference = FirebaseStorage.getInstance();
        final StorageReference imageRef = storageReference.getReference().child("images").child(imageName);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] data = byteArrayOutputStream.toByteArray();

        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnFailureListener(e -> listener.onFail()).addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Uri downloadUrl = uri;
            listener.onSuccess(downloadUrl.toString());
        }));
    }

    public static void deleteImage(String imageUrl, final Listener listener) {
        FirebaseStorage storageReference = FirebaseStorage.getInstance();
        final StorageReference imageRef = storageReference.getReferenceFromUrl(imageUrl);

        imageRef.delete().addOnSuccessListener(aVoid -> listener.onSuccess("")).addOnFailureListener(e -> listener.onFail());
    }

}
