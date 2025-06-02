package com.example.fitbuddy;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.*;

public class WorkoutDetailActivity extends AppCompatActivity {

    private TextView dayTitleTextView, dayDescriptionTextView;
    private ListView exerciseListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_detail);

        dayTitleTextView = findViewById(R.id.dayTitleTextView);
        dayDescriptionTextView = findViewById(R.id.dayDescriptionTextView);
        exerciseListView = findViewById(R.id.exerciseListView);

        String dayName = getIntent().getStringExtra("day_name");
        if (dayName != null) {
            dayTitleTextView.setText("Тренинг за " + getTranslatedDay(dayName));
            dayDescriptionTextView.setText(getDescriptionForDay(dayName));

            List<String> exercises = getExercisesForDay(dayName);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, exercises);
            exerciseListView.setAdapter(adapter);
            exerciseListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        } else {
            dayTitleTextView.setText("Непознат ден");
        }
    }

    private String getTranslatedDay(String dayEnglish) {
        switch (dayEnglish) {
            case "Monday": return "Понеделник";
            case "Tuesday": return "Вторник";
            case "Wednesday": return "Среда";
            case "Thursday": return "Четврток";
            case "Friday": return "Петок";
            case "Saturday": return "Сабота";
            case "Sunday": return "Недела";
            default: return dayEnglish;
        }
    }

    private String getDescriptionForDay(String day) {
        switch (day) {
            case "Monday": return "Фокус: гради и трицепс.";
            case "Tuesday": return "Фокус: грб и бицепс.";
            case "Wednesday": return "Фокус: нозе.";
            case "Thursday": return "Фокус: рамена и стомачни.";
            case "Friday": return "Фокус: цело тело (HIIT).";
            case "Saturday": return "Фокус: кардио и јадро.";
            case "Sunday": return "Одмор и истегнување.";
            default: return "";
        }
    }

    private List<String> getExercisesForDay(String day) {
        switch (day) {
            case "Monday":
                return Arrays.asList("Bench Press", "Push-ups", "Triceps Dips");
            case "Tuesday":
                return Arrays.asList("Pull-ups", "Lat Pulldown", "Barbell Curl");
            case "Wednesday":
                return Arrays.asList("Squats", "Lunges", "Leg Press");
            case "Thursday":
                return Arrays.asList("Shoulder Press", "Lateral Raises", "Crunches");
            case "Friday":
                return Arrays.asList("Burpees", "Mountain Climbers", "Jumping Jacks");
            case "Saturday":
                return Arrays.asList("Running", "Plank", "Bicycle Crunches");
            case "Sunday":
                return Arrays.asList("Yoga", "Foam Rolling", "Stretching");
            default:
                return Collections.singletonList("Нема вежби");
        }
    }
}
