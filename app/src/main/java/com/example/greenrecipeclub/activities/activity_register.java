package com.example.greenrecipeclub.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.greenrecipeclub.R;
import com.example.greenrecipeclub.model.ModelFirebase;
import com.example.greenrecipeclub.model.User;
import com.example.greenrecipeclub.model.Utils;
import com.google.android.material.textfield.TextInputLayout;

public class activity_register extends AppCompatActivity {

    ImageView profileImage;
    EditText email;
    EditText username;
    EditText password;
    Button registerBtn;
    Button loginBtn;
    Uri profileImageUri;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        profileImage = findViewById(R.id.register_screen_addImgView);
        email = findViewById(R.id.screen_register_input_email);
        username = findViewById(R.id.screen_register_input_username);
        password = findViewById(R.id.screen_register_input_password);
        registerBtn = findViewById(R.id.screen_register_btn_register);
        loginBtn = findViewById(R.id.screen_register_btn_login);
        profileImageUri = null;


        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.uploadImgFromGallery(activity_register.this);
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ModelFirebase.registerUserAccount(username.getText().toString(), password.getText().toString(), email.getText().toString(), profileImageUri, new ModelFirebase.Listener<Boolean>() {
                    @Override
                    public void onComplete() {
                        activity_register.this.finish();
                    }

                    @Override
                    public void onFail() {

                    }
                });
            }
        });


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toLoginPage();
            }
        });

    }


    private void toLoginPage() {
        Intent intent = new Intent(this, activity_login.class);
        startActivity(intent);
    }


}
