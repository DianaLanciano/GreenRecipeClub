package com.example.greenrecipeclub.model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.greenrecipeclub.MyApplication;

@Database(entities = {Recipe.class }, version = 4)
abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract RecipeDao RecipeDao();
}

public class AppLocalDb {

    static public AppLocalDbRepository db =
            Room.databaseBuilder(
                    MyApplication.context,
                    AppLocalDbRepository.class,
                    "AppLocalDb.db")
                    .fallbackToDestructiveMigration()
                    .build();

}