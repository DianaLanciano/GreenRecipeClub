package com.example.greenrecipeclub.data;

import com.example.greenrecipeclub.data.model.LoggedInUser;

import java.io.IOException;

public class LoginDataSource {

    public Result login(String username, String password) {
        try {
            LoggedInUser fakeUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            "Diana Yoni");
            return new Result.Success<>(fakeUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
    }
}