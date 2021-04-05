package com.example.greenrecipeclub.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.greenrecipeclub.R;

public class activity_register extends AppCompatActivity {

    Button loginBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        loginBtn = findViewById(R.id.loginButton_register_screen);
        loginBtn.setOnClickListener(new View.OnClickListener(){

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