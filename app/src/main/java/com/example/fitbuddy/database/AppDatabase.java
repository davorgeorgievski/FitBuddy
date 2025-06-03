package com.example.fitbuddy.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.fitbuddy.model.CompletedExercise;

@Database(entities = {CompletedExercise.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract ExerciseDao exerciseDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    "fitbuddy_database"
            ).fallbackToDestructiveMigration().build();
        }
        return INSTANCE;
    }
}
