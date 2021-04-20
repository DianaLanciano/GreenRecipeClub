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
    ImageButton vegan;
    ImageButton glutenFree;
    ImageButton salads;
    ImageButton fish;
    ImageButton spaghetti;
    ImageButton meat;
    ImageButton soup;
    ImageButton chicken;
    ImageButton deserts;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        vegan = view.findViewById(R.id.vegan);
        glutenFree = view.findViewById(R.id.glutenFree);
        salads = view.findViewById(R.id.salads);
        fish = view.findViewById(R.id.fishes);
        spaghetti = view.findViewById(R.id.spaguetti);
        meat = view.findViewById(R.id.meat);
        soup = view.findViewById(R.id.soup);
        chicken = view.findViewById(R.id.chicken);
        deserts = view.findViewById(R.id.desserts);


        vegan.setOnClickListener(v -> {
            HomeFragmentDirections.ActionHomeFragmentToListOfRecipesFragment action = HomeFragmentDirections.actionHomeFragmentToListOfRecipesFragment("Vegan");
            Navigation.findNavController(view).navigate(action);
        });
        glutenFree.setOnClickListener(v -> {
            HomeFragmentDirections.ActionHomeFragmentToListOfRecipesFragment action = HomeFragmentDirections.actionHomeFragmentToListOfRecipesFragment("Gluten Free");
            Navigation.findNavController(view).navigate(action);
        });
        salads.setOnClickListener(v -> {
            HomeFragmentDirections.ActionHomeFragmentToListOfRecipesFragment action = HomeFragmentDirections.actionHomeFragmentToListOfRecipesFragment("Salads");
            Navigation.findNavController(view).navigate(action);
        });
        fish.setOnClickListener(v -> {
            HomeFragmentDirections.ActionHomeFragmentToListOfRecipesFragment action = HomeFragmentDirections.actionHomeFragmentToListOfRecipesFragment("Fishes");
            Navigation.findNavController(view).navigate(action);
        });
        spaghetti.setOnClickListener(v -> {
            HomeFragmentDirections.ActionHomeFragmentToListOfRecipesFragment action = HomeFragmentDirections.actionHomeFragmentToListOfRecipesFragment("Spaghetti");
            Navigation.findNavController(view).navigate(action);
        });
        meat.setOnClickListener(v -> {
            HomeFragmentDirections.ActionHomeFragmentToListOfRecipesFragment action = HomeFragmentDirections.actionHomeFragmentToListOfRecipesFragment("Meat");
            Navigation.findNavController(view).navigate(action);
        });
        soup.setOnClickListener(v -> {
            HomeFragmentDirections.ActionHomeFragmentToListOfRecipesFragment action = HomeFragmentDirections.actionHomeFragmentToListOfRecipesFragment("Soup");
            Navigation.findNavController(view).navigate(action);
        });
        chicken.setOnClickListener(v -> {
            HomeFragmentDirections.ActionHomeFragmentToListOfRecipesFragment action = HomeFragmentDirections.actionHomeFragmentToListOfRecipesFragment("Chicken");
            Navigation.findNavController(view).navigate(action);
        });
        deserts.setOnClickListener(v -> {
            HomeFragmentDirections.ActionHomeFragmentToListOfRecipesFragment action = HomeFragmentDirections.actionHomeFragmentToListOfRecipesFragment("Dessert");
            Navigation.findNavController(view).navigate(action);
        });


        return view;
    }
}