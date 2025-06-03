
package com.example.fitbuddy;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fitbuddy.database.AppDatabase;
import com.example.fitbuddy.model.CompletedExercise;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executors;

public class ProgressActivity extends AppCompatActivity {

    private ListView progressListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        FirebaseAnalytics analytics = FirebaseAnalytics.getInstance(this);
        Bundle screenBundle = new Bundle();
        screenBundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "ProgressActivity");
        screenBundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "ProgressActivity");
        analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, screenBundle);

        progressListView = findViewById(R.id.progressListView);

        Executors.newSingleThreadExecutor().execute(() -> {
            List<CompletedExercise> list = AppDatabase.getInstance(this).exerciseDao().getAllExercisesSorted();

            List<String> displayList = new ArrayList<>();
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());

            for (CompletedExercise ce : list) {
                String time = sdf.format(new Date(ce.timestamp));
                displayList.add(ce.dayName + " - " + ce.exerciseName + " Завршено на: " + time);
            }

            runOnUiThread(() -> {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, displayList);
                progressListView.setAdapter(adapter);
            });
        });
    }
}
