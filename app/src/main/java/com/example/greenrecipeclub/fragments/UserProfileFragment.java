package com.example.greenrecipeclub.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.greenrecipeclub.R;
import com.example.greenrecipeclub.activities.activity_login;
import com.example.greenrecipeclub.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;


public class UserProfileFragment extends Fragment {

    TextView userName;
    TextView email;
    ImageView editBtn;
    ImageView userImg;
    Button myRecipesListBtn;
    Button logoutBtn;

    public UserProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        userImg = view.findViewById(R.id.screen_profile_img_userImg);
        userName = view.findViewById(R.id.screen_profile_text_username);
        email = view.findViewById(R.id.screen_profile_text_email);
        editBtn = view.findViewById(R.id.screen_profile_img_edirprofile);
        myRecipesListBtn = view.findViewById(R.id.screen_profile_btn_userRecipes);
        logoutBtn = view.findViewById(R.id.screen_profile_btn_logout);

        myRecipesListBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                toEditProfilePage();
            }
        });

        myRecipesListBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                UserProfileFragmentDirections.ActionUserProfileFragmentToUserListOfRecipesFragment action = UserProfileFragmentDirections.actionUserProfileFragmentToUserListOfRecipesFragment(User.getInstance().userId);
                Navigation.findNavController(view).navigate(action);
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) { toLoginPage();}

        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toEditProfilePage();
            }
        });


        setUserDetails();

        return view;
    }

    private void setUserDetails() {
        userName.setText(User.getInstance().userName);
        email.setText(User.getInstance().email);

        if (User.getInstance().profileImageUrl != null)
        {
            Picasso.get().load(User.getInstance().profileImageUrl).noPlaceholder().into(userImg);
        }
    }

    private void toLoginPage() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this.getActivity(), activity_login.class));
    }

    private void toEditProfilePage() {

        NavController navCtrl = Navigation.findNavController(getActivity(), R.id.main_navhost);
        NavDirections directions = UserProfileFragmentDirections.actionUserProfileFragmentToEditProfileFragment();
        navCtrl.navigate(directions);
    }
}