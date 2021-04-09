package com.example.greenrecipeclub.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.greenrecipeclub.R;
import com.example.greenrecipeclub.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;
import java.util.List;

public class ListOfRecipesFragment extends Fragment {


    RecyclerView list;
    List<Recipe> data = new LinkedList<>();
    String category;
    MyAdapter adapter;
    listOfRecipesView viewList;
    LiveData<List<Recipe>> liveData;

    public ListOfRecipesFragment() {
        // Required empty public constructor
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
        category = ListOfRecipesFragmentArgs.fromBundle(getArguments()).getCategories();
        list = view.findViewById(R.id.RecyclerlistOfrecipes_recipes_screen);
        list.hasFixedSize();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        list.setLayoutManager(layoutManager);

        adapter = new MyAdapter();
        list.setAdapter(adapter);


        return view;


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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION)
                            listener.onClick(position);
                    }
                }
            });

        }

        public void bind(Recipe recipeToBind) {
            recipeTitle.setText(recipeToBind.getRecipeName());
            recipePublisher.setText(recipeToBind.getPublisherName());
            recipe = recipeToBind;
            if (recipeToBind.getRecipeImgUrl() != null) {
                Picasso.get().load(recipeToBind.getRecipeImgUrl()).placeholder(R.drawable.chocolate).into(recipeImg);
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
        void setOnClickListener(OnItemClickListener listener){ this.listener=listener; }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            //will run when we create row object
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.recipe_list_row, parent, false);
            MyViewHolder recipeViewHolder = new MyViewHolder(view,listener);
            return recipeViewHolder;

        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            //will run when binding row to data
            Recipe recipe = data.get(position);
            holder.bind(recipe);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }



}





