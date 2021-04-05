package com.example.greenrecipeclub.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.greenrecipeclub.R;

public class activity_login extends AppCompatActivity {


    EditText emailInput;
    EditText passwordInput;
    Button loginBtn;
    Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        registerBtn = findViewById(R.id.registerButton_login_screen);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                toRegisterPage();
            }
        });

    }

    private void toRegisterPage() {
        Intent intent = new Intent(this, activity_register.class);
        startActivity(intent);
    }
}