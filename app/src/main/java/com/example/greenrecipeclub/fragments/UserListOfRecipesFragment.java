package com.example.greenrecipeclub.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.greenrecipeclub.R;
import com.example.greenrecipeclub.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class UserListOfRecipesFragment extends Fragment {
    String userId;
    RecyclerView recyclerView;
    List<Recipe> data = new LinkedList<>();
    UserListOfRecipesFragment.MyRecipeListAdapter adapter;
    MyListViewModel viewModel;
    LiveData<List<Recipe>> liveData;

    public UserListOfRecipesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(MyListViewModel.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_list_of_recipes, container, false);
        userId = UserListOfRecipesFragmentArgs.fromBundle(getArguments()).getUserId();

        recyclerView = view.findViewById(R.id.screen_userRecipes_recyclerView);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new MyRecipeListAdapter();

        recyclerView.setAdapter(adapter);

        adapter.setOnClickListener(position -> {
            Recipe recipe = data.get(position);
            UserListOfRecipesFragmentDirections.ActionUserListOfRecipesFragmentToRecipePageFragment action = UserListOfRecipesFragmentDirections.actionUserListOfRecipesFragmentToRecipePageFragment(recipe);
            Navigation.findNavController(view).navigate(action);
        });

        //live data
        liveData = viewModel.getUserData(userId);
        liveData.observe(getViewLifecycleOwner(), recipes -> {

            Collections.reverse(recipes);
            data = recipes;
            adapter.notifyDataSetChanged();
        });


        final SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.feed_list_swipe_refresh2);
        swipeRefreshLayout.setOnRefreshListener(() -> viewModel.refresh(() -> swipeRefreshLayout.setRefreshing(false)));

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    static class RecipeViewHolder extends RecyclerView.ViewHolder {
        TextView recipeTitle;
        ImageView recipeImage;
        TextView username;
        TextView category;
        Recipe recipe;

        public RecipeViewHolder(@NonNull View itemView, final UserListOfRecipesFragment.OnItemClickListener listener) {
            super(itemView);
            recipeTitle = itemView.findViewById(R.id.row_inList_text_titleOfRecipe);
            recipeImage = itemView.findViewById(R.id.row_inList_img_imgOfRecipe);
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

        public void bindRecipe(Recipe recipeToBind) {
            recipeTitle.setText(recipeToBind.getRecipeName());
            category.setText(recipeToBind.getCategoryId());
            username.setText(recipeToBind.getPublisherName());
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


    class MyRecipeListAdapter extends RecyclerView.Adapter<RecipeViewHolder> {
        private OnItemClickListener listener;

        void setOnClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }

        @NonNull
        @Override
        public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            //create row
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.recipe_list_row, parent, false);
            RecipeViewHolder recipeViewHolder = new RecipeViewHolder(view, listener);
            return recipeViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
            Recipe recipe = data.get(position);
            holder.bindRecipe(recipe);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }


}