package com.example.fitbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

public class TrainingPlanActivity extends AppCompatActivity {

    private ListView trainingListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_plan);

        trainingListView = findViewById(R.id.trainingListView);

        String[] weeklyPlan = {
                "Monday: Chest + Triceps",
                "Tuesday: Back + Biceps",
                "Wednesday: Legs",
                "Thursday: Shoulders + Abs",
                "Friday: Full Body HIIT",
                "Saturday: Cardio + Core",
                "Sunday: Rest / Stretching"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                weeklyPlan
        );

        trainingListView.setAdapter(adapter);

        trainingListView.setOnItemClickListener((AdapterView<?> parent, android.view.View view, int position, long id) -> {
            String selectedItem = weeklyPlan[position];
            String dayName = selectedItem.split(":")[0];

            Intent intent = new Intent(TrainingPlanActivity.this, WorkoutDetailActivity.class);
            intent.putExtra("day_name", dayName);
            startActivity(intent);
        });
    }
}
