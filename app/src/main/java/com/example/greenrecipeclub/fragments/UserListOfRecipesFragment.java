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
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.lifecycle.Observer;

import com.example.greenrecipeclub.R;
import com.example.greenrecipeclub.model.Model;
import com.example.greenrecipeclub.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;
import java.util.List;



public class UserListOfRecipesFragment extends Fragment {


    String userId;
    RecyclerView list;
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

        list= view.findViewById(R.id.screen_userRecipes_recyclerView);
        list.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        list.setLayoutManager(layoutManager);

        adapter = new MyRecipeListAdapter();

        list.setAdapter(adapter);

        adapter.setOnClickListener(new UserListOfRecipesFragment.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Recipe recipe = data.get(position);
                UserListOfRecipesFragmentDirections.ActionUserListOfRecipesFragmentToRecipePageFragment action = UserListOfRecipesFragmentDirections.actionUserListOfRecipesFragmentToRecipePageFragment(recipe);
                Navigation.findNavController(view).navigate(action);
            }
        });

        //live data
        liveData = viewModel.getDataByUser(userId);
        liveData.observe(getViewLifecycleOwner(), new Observer<List<Recipe>>() {

            @Override
            public void onChanged(List<Recipe> recipes) {

                List<Recipe> reversedData = reverseData(recipes);
                data = reversedData;
                adapter.notifyDataSetChanged();
            }
        });


        final SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.feed_list_swipe_refresh2);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.refresh(new Model.CompListener() {
                    @Override
                    public void onComplete() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });

        return view;
    }


    private List<Recipe> reverseData(List<Recipe> recipes) {
        List<Recipe> reversedData = new LinkedList<>();
        for (Recipe recipe: recipes) {
            reversedData.add(0, recipe);
        }
        return reversedData;
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

        public void bind(Recipe recipeToBind){
            recipeTitle.setText(recipeToBind.getRecipeName());
            category.setText(recipeToBind.getCategoryId());
            username.setText(recipeToBind.getPublisherName());
            recipe = recipeToBind;
            if (recipeToBind.getRecipeImgUrl() !=null)
            {
                Picasso.get().load(recipeToBind.getRecipeImgUrl()).placeholder(R.drawable.hassa).into(recipeImg);
            }else {
                recipeImg.setImageResource(R.drawable.ic_launcher_background);
            }

        }
    }

    interface OnItemClickListener {
        void onClick(int position);
    }


    class MyRecipeListAdapter extends RecyclerView.Adapter<MyRecipeViewHolder> {

        private OnItemClickListener listener;

        void setOnClickListener(OnItemClickListener listener){ this.listener=listener; }

        @NonNull
        @Override
        public MyRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            //create row
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.recipe_list_row,parent,false);
            UserListOfRecipesFragment.MyRecipeViewHolder myrecipeViewHolder = new UserListOfRecipesFragment.MyRecipeViewHolder(view,listener);
            return myrecipeViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyRecipeViewHolder holder, int position) {
            Recipe recipe = data.get(position);
            holder.bind(recipe);
        }


        @Override
        public int getItemCount() {return data.size();}
    }






}