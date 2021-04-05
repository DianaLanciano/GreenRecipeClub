package com.example.greenrecipeclub.model;

import com.example.greenrecipeclub.MyApplication;
import android.content.ContentResolver;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class ModelFirebase {

    public interface Listener<T>
    {
        void onComplete();
        void onFail();
    }


    public static void register(String userName, String password, String email, Uri imageUrl, Listener<Boolean> listener){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null){
            firebaseAuth.signOut();
        }

        if(userName!=null
                && !userName.equals("") && password !=null && !password.equals("")
                && email != null && !email.equals("") && imageUrl !=null){

            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Toast.makeText(MyApplication.context, "Success Registered!", Toast.LENGTH_SHORT).show();
                    setUserInFirebase(userName, email, imageUrl);
                    listener.onComplete();
                }
            }).addOnFailureListener(new OnFailureListener(){
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MyApplication.context, "Registration failed!", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            Toast.makeText(MyApplication.context, "Fill up all inputs fields!", Toast.LENGTH_SHORT).show();
            listener.onFail();
        }

    }

    private static void setUserInFirebase(String userName, String email, Uri imageUri){
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("images");

        if (imageUri != null){
            String imageName = userName + "." + getExtension(imageUri);
            final StorageReference imageRef = storageReference.child(imageName);

            UploadTask uploadTask = imageRef.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return imageRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){

                        Map<String,Object> data = new HashMap<>();
                        data.put("profileImageUrl", task.getResult().toString());
                        data.put("username", userName);
                        data.put("email", email);
                        data.put("info", "NA");
                        firebaseFirestore.collection("userData").document(email).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                if (firebaseAuth.getCurrentUser() != null){
                                    firebaseAuth.signOut();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MyApplication.context, "Fails to create user and upload data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else if (!task.isSuccessful()){
                        Toast.makeText(MyApplication.context, task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else {
            Toast.makeText(MyApplication.context, "Please choose a profile image", Toast.LENGTH_SHORT).show();
        }
    }


    public static String getExtension(Uri uri)
    {
        try{
            ContentResolver contentResolver = MyApplication.context.getContentResolver();
            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
            return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

        } catch (Exception e) {
            Toast.makeText(MyApplication.context, "Register page: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }



    public static void geRecipesByCategory(final Model.Listener<List<Recipe>> listener, String category) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();



    }

    public static void addRecipe(Recipe recipe, Model.Listener<Boolean> listener) {

    }

    public static void deleteRecipe(Recipe recipe, Model.Listener<Boolean> listener) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
    }
}
