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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.greenrecipeclub.R;
import com.example.greenrecipeclub.fragments.VIewModels.categoryRecipesViewModel;
import com.example.greenrecipeclub.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ListOfRecipesFragment extends Fragment {
    RecyclerView recyclerView;
    List<Recipe> recipeList = new LinkedList<>();
    String categoryName;
    MyAdapter adapter;
    categoryRecipesViewModel categoryRecipesViewModel;
    LiveData<List<Recipe>> liveData;

    public ListOfRecipesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        categoryRecipesViewModel = new ViewModelProvider(this).get(categoryRecipesViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_of_recipes, container, false);
        categoryName = ListOfRecipesFragmentArgs.fromBundle(getArguments()).getCategory();
        Log.d("TAG", "arg_category " + categoryName);
        recyclerView = view.findViewById(R.id.RecyclerlistOfrecipes_recipes_screen);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new MyAdapter();
        recyclerView.setAdapter(adapter);

        adapter.setOnClickListener(position -> {
            Recipe recipe = recipeList.get(position);

            ListOfRecipesFragmentDirections.ActionListOfRecipesFragmentToRecipePageFragment action = ListOfRecipesFragmentDirections.actionListOfRecipesFragmentToRecipePageFragment(recipe);
            Navigation.findNavController(view).navigate(action);
        });
        liveData = categoryRecipesViewModel.getDataByCategory(categoryName);
        liveData.observe(getViewLifecycleOwner(), recipes -> {
            Collections.reverse(recipes);
            recipeList = recipes;
            adapter.notifyDataSetChanged();
        });


        final SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.feed_list_swipe_refresh2);
        swipeRefreshLayout.setOnRefreshListener(() -> categoryRecipesViewModel.refresh(() -> swipeRefreshLayout.setRefreshing(false)));

        return view;


    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        //reference to row items
        ImageView recipeImg;
        TextView recipeTitle;
        TextView recipeCategory;
        TextView recipePublisher;
        Recipe recipe;

        public MyViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            recipeImg = itemView.findViewById(R.id.row_inList_img_imgOfRecipe);
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

        public void bind(Recipe recipeToBind) {
            recipeTitle.setText(recipeToBind.getRecipeName());
            recipePublisher.setText(recipeToBind.getPublisherName());
            recipeCategory.setText(recipeToBind.getCategoryId());
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


    class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        private OnItemClickListener listener;

        void setOnClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            //will run when we create row object
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.recipe_list_row, parent, false);
            return new MyViewHolder(view, listener);

        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            //will run when binding row to data
            Recipe recipe = recipeList.get(position);
            holder.bind(recipe);
        }

        @Override
        public int getItemCount() {
            return recipeList.size();
        }
    }
}





