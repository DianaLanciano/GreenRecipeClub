package com.example.greenrecipeclub.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.greenrecipeclub.R;

public class ListOfRecipesFragment extends Fragment {

    RecyclerView categoryList;


    public ListOfRecipesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_of_recipes, container, false);

        categoryList = view.findViewById(R.id.RecyclerlistOfrecipes_recipes_screen);
        categoryList.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        categoryList.setLayoutManager(layoutManager);



        return view;



    }
}