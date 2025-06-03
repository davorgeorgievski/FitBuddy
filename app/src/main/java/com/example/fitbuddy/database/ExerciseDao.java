package com.example.fitbuddy.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.fitbuddy.model.CompletedExercise;

import java.util.List;

@Dao
public interface ExerciseDao {

    @Insert
    void insert(CompletedExercise exercise);

    @Query("SELECT * FROM completed_exercises WHERE dayName = :dayName")
    List<CompletedExercise> getExercisesForDay(String dayName);

    @Query("DELETE FROM completed_exercises")
    void deleteAll();

    @Query("SELECT * FROM completed_exercises ORDER BY timestamp DESC")
    List<CompletedExercise> getAllExercisesSorted();
}