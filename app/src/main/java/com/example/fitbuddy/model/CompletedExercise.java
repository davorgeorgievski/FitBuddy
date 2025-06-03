package com.example.fitbuddy.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "completed_exercises")
public class CompletedExercise {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String dayName;
    public String exerciseName;
    public long timestamp;

    public CompletedExercise(String dayName, String exerciseName) {
        this.dayName = dayName;
        this.exerciseName = exerciseName;
        this.timestamp = System.currentTimeMillis(); // тековен timestamp
    }

    // Room бара празен конструктор ако има повеќе од еден
    public CompletedExercise() {
    }
}
