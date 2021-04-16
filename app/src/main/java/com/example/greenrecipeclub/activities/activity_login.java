package com.example.greenrecipeclub.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.greenrecipeclub.R;
import com.example.greenrecipeclub.model.ModelFirebase;
import com.google.firebase.auth.FirebaseAuth;

public class activity_login extends AppCompatActivity {


    EditText emailInput;
    EditText passwordInput;
    Button loginBtn;
    Button registerBtn;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = firebaseAuth.getInstance();
        registerBtn = findViewById(R.id.registerB_loginScreen);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toRegisterPage();
            }
        });


        /////////////////////////////////////////

        emailInput = findViewById(R.id.screen_login_input_email);
        passwordInput = findViewById(R.id.screen_login_input_password);

        if (firebaseAuth.getCurrentUser() != null) {
            ModelFirebase.setUserEmail(firebaseAuth.getCurrentUser().getEmail());
            startActivity(new Intent(activity_login.this, MainActivity.class));
            finish();
        }
        this.setTitle("Login");

        loginBtn = findViewById(R.id.screen_login_btn_login);
        loginBtn.setOnClickListener(view -> ModelFirebase.setUserLogin(emailInput.getText().toString(), passwordInput.getText().toString(), new ModelFirebase.Listener<Boolean>() {
            @Override
            public void onComplete() {
                startActivity(new Intent(activity_login.this, MainActivity.class));
                activity_login.this.finish();
            }

            @Override
            public void onFail() {
            }
        }));


    }


    private void toRegisterPage() {
        Intent intent = new Intent(this, activity_register.class);
        startActivity(intent);
    }
}