package com.example.greenrecipeclub.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.Observer;

import com.example.greenrecipeclub.R;
import com.example.greenrecipeclub.fragments.VIewModels.userRecipesViewModel;
import com.example.greenrecipeclub.model.Model;
import com.example.greenrecipeclub.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class UserListOfRecipesFragment extends Fragment {
    String userId;
    RecyclerView recyclerView;
    List<Recipe> recipes = new LinkedList<>();
    UserListOfRecipesFragment.MyRecipeListAdapter adapter;
    userRecipesViewModel userRecipesViewModel;
    LiveData<List<Recipe>> userRecipes;

    public UserListOfRecipesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        userRecipesViewModel = new ViewModelProvider(this).get(userRecipesViewModel.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View userRecipesView = inflater.inflate(R.layout.fragment_user_list_of_recipes, container, false);
        userId = UserListOfRecipesFragmentArgs.fromBundle(getArguments()).getUserId();

        recyclerView = userRecipesView.findViewById(R.id.screen_userRecipes_recyclerView);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new MyRecipeListAdapter();

        recyclerView.setAdapter(adapter);

        adapter.setOnClickListener(position -> {
            Recipe recipe = recipes.get(position);
            UserListOfRecipesFragmentDirections.ActionUserListOfRecipesFragmentToRecipePageFragment action = UserListOfRecipesFragmentDirections.actionUserListOfRecipesFragmentToRecipePageFragment(recipe);
            Navigation.findNavController(userRecipesView).navigate(action);
        });

        //live data
        userRecipes = userRecipesViewModel.getUserRecipes(userId);
        userRecipes.observe(getViewLifecycleOwner(), recipes -> {

           Collections.reverse(recipes);
            UserListOfRecipesFragment.this.recipes = recipes;
            adapter.notifyDataSetChanged();
        });


        final SwipeRefreshLayout swipeRefreshLayout = userRecipesView.findViewById(R.id.feed_list_swipe_refresh2);
        swipeRefreshLayout.setOnRefreshListener(() -> userRecipesViewModel.refresh(() -> swipeRefreshLayout.setRefreshing(false)));

        return userRecipesView;
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    static class MyRecipeViewHolder extends RecyclerView.ViewHolder {

        TextView recipeTitle;
        ImageView recipeImg;
        TextView username;
        TextView category;
        Recipe recipe;

        public MyRecipeViewHolder(@NonNull View itemView, final UserListOfRecipesFragment.OnItemClickListener listener) {
            super(itemView);

            recipeTitle = itemView.findViewById(R.id.row_inList_text_titleOfRecipe);
            recipeImg = itemView.findViewById(R.id.row_inList_img_imgOfRecipe);
            username = itemView.findViewById(R.id.row_inList_text_userName);
            category = itemView.findViewById(R.id.row_inList_text_category);

            itemView.setOnClickListener(view -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION)
                        listener.onClick(position);
                }
            });
        }

        public void bind(Recipe recipeToBind) {
            recipeTitle.setText(recipeToBind.getRecipeName());
            category.setText(recipeToBind.getCategoryId());
            username.setText(recipeToBind.getPublisherName());
            recipe = recipeToBind;
            if (recipeToBind.getRecipeImgUrl() != null) {
                Picasso.get().load(recipeToBind.getRecipeImgUrl()).placeholder(R.drawable.mainlogo).into(recipeImg);
            } else {
                recipeImg.setImageResource(R.drawable.ic_launcher_background);
            }

        }
    }

    interface OnItemClickListener {
        void onClick(int position);
    }


    class MyRecipeListAdapter extends RecyclerView.Adapter<MyRecipeViewHolder> {

        private OnItemClickListener listener;

        void setOnClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }

        @NonNull
        @Override
        public MyRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            //create row
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.recipe_list_row, parent, false);
            UserListOfRecipesFragment.MyRecipeViewHolder myrecipeViewHolder = new UserListOfRecipesFragment.MyRecipeViewHolder(view, listener);
            return myrecipeViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyRecipeViewHolder holder, int position) {
            Recipe recipe = recipes.get(position);
            holder.bind(recipe);
        }


        @Override
        public int getItemCount() {
            return recipes.size();
        }
    }


}