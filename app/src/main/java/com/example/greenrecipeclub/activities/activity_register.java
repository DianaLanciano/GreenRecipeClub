package com.example.greenrecipeclub.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.greenrecipeclub.R;

public class activity_register extends AppCompatActivity {

    Button loginBtn;
    EditText userName = findViewById(R.id.usernameInput_register_screen);
    EditText password = findViewById(R.id.passwordInput_register_screen);
    EditText email = findViewById(R.id.emailInput_register_screen);
    ImageButton userImage = findViewById(R.id.imageInput_register_screen);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        loginBtn = findViewById(R.id.loginButton_register_screen);
        loginBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                toLoginPage();
            }
        });


        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void toLoginPage() {
        Intent intent = new Intent(this, activity_login.class);
        startActivity(intent);
    }
}