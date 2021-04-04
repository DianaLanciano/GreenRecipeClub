package com.example.greenrecipeclub.model;

public class User {
    private static User theUser = null;

    public String userUsername;
    public String userEmail;
    public String userId;
    public String password;
    public String address;
    public String profileImageUrl;

    private User()
    {
        userEmail = null;
        userUsername = null;
        profileImageUrl = null;
        password = null;
        address = null;
        userId = null;
    }

    public static User getInstance()
    {
        if (theUser == null)
            theUser = new User();

        return theUser;
    }
}
