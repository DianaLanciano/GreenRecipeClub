package com.example.greenrecipeclub.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.greenrecipeclub.R;
import com.example.greenrecipeclub.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ListOfRecipesFragment extends Fragment {


    RecyclerView recyclerView;
    List<Recipe> recipesList = new LinkedList<>();
    String categoryString;
    RecipesListAdapter adapter;
    listOfRecipesView viewList;
    LiveData<List<Recipe>> liveDataRecipesList;

    public ListOfRecipesFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        viewList = new ViewModelProvider(this).get(listOfRecipesView.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_of_recipes, container, false);
        categoryString = ListOfRecipesFragmentArgs.fromBundle(getArguments()).getCategory();
        recyclerView = view.findViewById(R.id.RecyclerlistOfrecipes_recipes_screen);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecipesListAdapter();
        recyclerView.setAdapter(adapter);

        adapter.setOnClickListener(position -> {
            Recipe recipe = recipesList.get(position);

            ListOfRecipesFragmentDirections.ActionListOfRecipesFragmentToRecipePageFragment action = ListOfRecipesFragmentDirections.actionListOfRecipesFragmentToRecipePageFragment(recipe);
            Navigation.findNavController(view).navigate(action);
        });
        liveDataRecipesList = viewList.getDataByCategory(categoryString);
        liveDataRecipesList.observe(getViewLifecycleOwner(), recipes -> {

            Collections.reverse(recipes);
            recipesList = recipes;
            adapter.notifyDataSetChanged();
        });


        final SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.feed_list_swipe_refresh2);
        swipeRefreshLayout.setOnRefreshListener(() -> viewList.refresh(() -> swipeRefreshLayout.setRefreshing(false)));

        return view;


    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder {
        //reference to row items
        ImageView recipeImage;
        TextView recipeTitle;
        TextView recipeCategory;
        TextView recipePublisher;
        Recipe recipe;

        public RecipeViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            recipeImage = itemView.findViewById(R.id.row_inList_img_imgOfRecipe);
            recipeTitle = itemView.findViewById(R.id.row_inList_text_titleOfRecipe);
            recipeCategory = itemView.findViewById(R.id.row_inList_text_category);
            recipePublisher = itemView.findViewById(R.id.row_inList_text_userName);

            itemView.setOnClickListener(view -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION)
                        listener.onClick(position);
                }
            });

        }

        public void bindRecipe(Recipe recipeToBind) {
            recipeTitle.setText(recipeToBind.getRecipeName());
            recipePublisher.setText(recipeToBind.getPublisherName());
            recipeCategory.setText(recipeToBind.getCategoryId());
            recipe = recipeToBind;
            if (recipeToBind.getRecipeImgUrl() != null) {
                Picasso.get().load(recipeToBind.getRecipeImgUrl()).placeholder(R.drawable.mainlogo).into(recipeImage);
            } else {
                recipeImage.setImageResource(R.drawable.ic_launcher_background);
            }

        }
    }


    interface OnItemClickListener {
        void onClick(int position);
    }


    class RecipesListAdapter extends RecyclerView.Adapter<RecipeViewHolder> {
        private OnItemClickListener listener;

        void setOnClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }

        @NonNull
        @Override
        public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            //will run when we create row object
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.recipe_list_row, parent, false);
            RecipeViewHolder recipeViewHolder = new RecipeViewHolder(view, listener);
            return recipeViewHolder;

        }

        @Override
        public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
            //will run when binding row to data
            Recipe recipe = recipesList.get(position);
            holder.bindRecipe(recipe);
        }

        @Override
        public int getItemCount() {
            return recipesList.size();
        }
    }
}





