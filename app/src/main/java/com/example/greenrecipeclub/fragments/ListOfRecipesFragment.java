package com.example.greenrecipeclub.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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


   static class RecipeViewHolder extends RecyclerView.ViewHolder{
        public RecipeViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
        }
    }

    interface OnItemClickListener {
        void onClick(int position);
    }


    public class MyAdapter extends RecyclerView.Adapter<RecipeViewHolder>{

        private OnItemClickListener listener;
        void setOnClickListener(OnItemClickListener listener){ this.listener=listener; }


        @NonNull
        @Override
        public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(getActivity()).inflate(R.layout.recipe_list_row, parent, false);
            RecipeViewHolder recipeViewHolder = new RecipeViewHolder(view, listener);
            return recipeViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }



}


