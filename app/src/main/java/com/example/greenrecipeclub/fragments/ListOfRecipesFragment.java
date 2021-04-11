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
import android.widget.ProgressBar;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.lifecycle.Observer;
import com.example.greenrecipeclub.R;
import  com.example.greenrecipeclub.model.Model;
import com.example.greenrecipeclub.model.Recipe;
import com.squareup.picasso.Picasso;
import com.example.greenrecipeclub.activities.MainActivity;
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
        category = ListOfRecipesFragmentArgs.fromBundle(getArguments()).getCategory();
        Log.d("TAG","arg_category "+ category);
        list = view.findViewById(R.id.RecyclerlistOfrecipes_recipes_screen);
        list.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        list.setLayoutManager(layoutManager);

        adapter = new MyAdapter();
        list.setAdapter(adapter);

        adapter.setOnClickListener(new OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Recipe recipe = data.get(position);

                //ListOfRecipesFragmentDirections.
               // Navigation.findNavController(view).navigate(action);
            }
        });

        //live data
        liveData = viewList.getDataByCategory(category);
        liveData.observe(getViewLifecycleOwner(), new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {

                List<Recipe> reversedData = reverseData(recipes);
                data = reversedData;
                adapter.notifyDataSetChanged();
            }
        });


        final SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.feed_list_swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewList.refresh(new Model.CompListener() {
                    @Override
                    public void onComplete() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });



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
            recipeCategory.setText(recipeToBind.getCategoryId());
            recipe = recipeToBind;
            if (recipeToBind.getRecipeImgUrl() != null) {
                Picasso.get().load(recipeToBind.getRecipeImgUrl()).placeholder(R.drawable.hassa ).into(recipeImg);
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


    private List<Recipe> reverseData(List<Recipe> recipes) {
        List<Recipe> reversedData = new LinkedList<>();
        for (Recipe recipe: recipes) {
            reversedData.add(0, recipe);
        }
        return reversedData;
    }



}





