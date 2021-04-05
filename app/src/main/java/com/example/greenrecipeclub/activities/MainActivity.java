package com.example.greenrecipeclub.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.example.greenrecipeclub.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        NavController navController = Navigation.findNavController(this, R.id.main_navhost);
        NavigationUI.setupActionBarWithNavController(this, navController);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_manu);
        NavigationUI.setupWithNavController(bottomNav,navController);
    }
}