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

    public interface Listener
    {
        void onSuccess(String url);
        void onFail();
    }

    public static void imageUploading(Bitmap imageBitmap, final Listener listener)
    {

        Date date = new Date();
        String imageName = User.getInstance().userName + date.getTime();
        FirebaseStorage storageReference = FirebaseStorage.getInstance();
        final StorageReference imageRef = storageReference.getReference().child("images").child(imageName);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onFail();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Uri downloadUrl = uri;
                        listener.onSuccess(downloadUrl.toString());
                    }
                });
            }
        });
    }

    public static void deleteImage(String imageUrl, final Listener listener)
    {

        Date date = new Date();
        FirebaseStorage storageReference = FirebaseStorage.getInstance();
        final StorageReference imageRef = storageReference.getReferenceFromUrl(imageUrl);

        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid)
            {
                listener.onSuccess("");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                listener.onFail();
            }
        });
    }

}
