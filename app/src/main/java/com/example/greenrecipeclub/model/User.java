package com.example.greenrecipeclub.model;

public class User {
    private static User theUser = null;

    public String userName;
    public String email;
    public String userId;
    public String password;
    public String address;
    public String profileImageUrl;

    private User()
    {
        userName = null;
        email = null;
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
