package com.example.greenrecipeclub;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {

    static public Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}
