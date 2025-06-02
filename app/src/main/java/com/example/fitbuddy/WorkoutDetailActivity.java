package com.example.fitbuddy;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.*;

import java.util.*;

public class WorkoutDetailActivity extends AppCompatActivity {

    private TextView dayTitleTextView;
    private ListView exerciseListView;
    private FirebaseFirestore db;
    private String userId;
    private String dayName;

    private Map<String, List<String>> weeklyExercises = new HashMap<String, List<String>>() {{
        put("Monday", Arrays.asList("Bench Press - 3x10", "Push-ups - 3x15", "Triceps Dips - 3x12"));
        put("Tuesday", Arrays.asList("Pull-ups - 3x8", "Barbell Rows - 3x10", "Bicep Curls - 3x12"));
        put("Wednesday", Arrays.asList("Squats - 3x10", "Lunges - 3x12", "Leg Press - 3x12"));
        put("Thursday", Arrays.asList("Shoulder Press - 3x10", "Lateral Raises - 3x15", "Crunches - 3x20"));
        put("Friday", Arrays.asList("Jump Squats - 3x15", "Mountain Climbers - 3x20", "Burpees - 3x10"));
        put("Saturday", Arrays.asList("Running - 20 mins", "Plank - 60 sec", "Russian Twists - 3x20"));
        put("Sunday", Arrays.asList("Stretching - 20 mins", "Foam Rolling - 10 mins"));
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_detail);

        dayTitleTextView = findViewById(R.id.dayTitleTextView);
        exerciseListView = findViewById(R.id.exerciseListView);

        dayName = getIntent().getStringExtra("day_name");
        if (dayName == null) dayName = "Unknown";

        dayTitleTextView.setText("Тренинг за " + getTranslatedDay(dayName));

        db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "Не сте најавени", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        userId = user.getUid();

        loadExercises();
    }

    private void loadExercises() {
        db.collection("users")
                .document(userId)
                .collection("workouts")
                .document(dayName)
                .get()
                .addOnSuccessListener(document -> {
                    Map<String, Boolean> exercises = new LinkedHashMap<>();
                    Map<String, Object> data = document.getData();

                    if (data != null && !data.isEmpty()) {
                        for (Map.Entry<String, Object> entry : data.entrySet()) {
                            if (entry.getValue() instanceof Boolean) {
                                exercises.put(entry.getKey(), (Boolean) entry.getValue());
                            }
                        }
                        setupListView(exercises);
                    } else {
                        // Прв пат? → користи стандардна листа за денот
                        List<String> list = weeklyExercises.getOrDefault(dayName, Arrays.asList("Rest day"));
                        for (String ex : list) exercises.put(ex, false);

                        // Сними во база
                        db.collection("users")
                                .document(userId)
                                .collection("workouts")
                                .document(dayName)
                                .set(exercises)
                                .addOnSuccessListener(unused -> setupListView(exercises))
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "Неуспешно запишување", Toast.LENGTH_SHORT).show();
                                    setupListView(exercises);
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Неуспешно вчитување", Toast.LENGTH_SHORT).show();
                });
    }

    private void setupListView(Map<String, Boolean> exercises) {
        List<String> items = new ArrayList<>(exercises.keySet());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, items);
        exerciseListView.setAdapter(adapter);

        for (int i = 0; i < items.size(); i++) {
            exerciseListView.setItemChecked(i, exercises.get(items.get(i)));
        }

        exerciseListView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedExercise = items.get(position);
            boolean isChecked = exerciseListView.isItemChecked(position);

            db.collection("users")
                    .document(userId)
                    .collection("workouts")
                    .document(dayName)
                    .update(selectedExercise, isChecked);
        });
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
}
