package com.example.greenrecipeclub.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.greenrecipeclub.R;

public class HomeFragment extends Fragment {

    ImageButton veganButton;
    ImageButton glutenFreeButton;
    ImageButton saladButton;
    ImageButton fishButton;
    ImageButton italianButton;
    ImageButton meatButton;
    ImageButton soupButton;
    ImageButton chickenButton;
    ImageButton dessertButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        veganButton = view.findViewById(R.id.vegan);
        glutenFreeButton = view.findViewById(R.id.glutenFree);
        saladButton = view.findViewById(R.id.salads);
        fishButton = view.findViewById(R.id.fishes);
        italianButton = view.findViewById(R.id.spaguetti);
        meatButton = view.findViewById(R.id.meat);
        soupButton = view.findViewById(R.id.soup);
        chickenButton = view.findViewById(R.id.chicken);
        dessertButton = view.findViewById(R.id.desserts);


        veganButton.setOnClickListener(v -> {
            HomeFragmentDirections.ActionHomeFragmentToListOfRecipesFragment action = HomeFragmentDirections.actionHomeFragmentToListOfRecipesFragment("Vegan");
            Navigation.findNavController(view).navigate(action);
        });
        glutenFreeButton.setOnClickListener(v -> {
            HomeFragmentDirections.ActionHomeFragmentToListOfRecipesFragment action = HomeFragmentDirections.actionHomeFragmentToListOfRecipesFragment("Gluten Free");
            Navigation.findNavController(view).navigate(action);
        });
        saladButton.setOnClickListener(v -> {
            HomeFragmentDirections.ActionHomeFragmentToListOfRecipesFragment action = HomeFragmentDirections.actionHomeFragmentToListOfRecipesFragment("Salads");
            Navigation.findNavController(view).navigate(action);
        });
        fishButton.setOnClickListener(v -> {
            HomeFragmentDirections.ActionHomeFragmentToListOfRecipesFragment action = HomeFragmentDirections.actionHomeFragmentToListOfRecipesFragment("Fishes");
            Navigation.findNavController(view).navigate(action);
        });
        italianButton.setOnClickListener(v -> {
            HomeFragmentDirections.ActionHomeFragmentToListOfRecipesFragment action = HomeFragmentDirections.actionHomeFragmentToListOfRecipesFragment("Spaguatti");
            Navigation.findNavController(view).navigate(action);
        });
        meatButton.setOnClickListener(v -> {
            HomeFragmentDirections.ActionHomeFragmentToListOfRecipesFragment action = HomeFragmentDirections.actionHomeFragmentToListOfRecipesFragment("Meat");
            Navigation.findNavController(view).navigate(action);
        });
        soupButton.setOnClickListener(v -> {
            HomeFragmentDirections.ActionHomeFragmentToListOfRecipesFragment action = HomeFragmentDirections.actionHomeFragmentToListOfRecipesFragment("Soup");
            Navigation.findNavController(view).navigate(action);
        });
        chickenButton.setOnClickListener(v -> {
            HomeFragmentDirections.ActionHomeFragmentToListOfRecipesFragment action = HomeFragmentDirections.actionHomeFragmentToListOfRecipesFragment("Chicken");
            Navigation.findNavController(view).navigate(action);
        });
        dessertButton.setOnClickListener(v -> {
            HomeFragmentDirections.ActionHomeFragmentToListOfRecipesFragment action = HomeFragmentDirections.actionHomeFragmentToListOfRecipesFragment("Dessert");
            Navigation.findNavController(view).navigate(action);
        });



        return view;
    }
}