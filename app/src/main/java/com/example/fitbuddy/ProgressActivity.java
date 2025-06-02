package com.example.fitbuddy;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.*;
import java.util.*;

public class ProgressActivity extends AppCompatActivity {

    private ListView progressListView;
    private FirebaseFirestore db;
    private String userId;
    private List<String> days = Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        progressListView = findViewById(R.id.progressListView);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "Не сте најавени", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        db = FirebaseFirestore.getInstance();
        userId = user.getUid();

        loadProgress();
    }

    private void loadProgress() {
        List<String> results = new ArrayList<>();
        for (String day : days) {
            db.collection("users")
                    .document(userId)
                    .collection("workouts")
                    .document(day)
                    .get()
                    .addOnSuccessListener(document -> {
                        int total = 0, completed = 0;
                        Map<String, Object> data = document.getData();
                        if (data != null) {
                            for (Object value : data.values()) {
                                if (value instanceof Boolean) {
                                    total++;
                                    if ((Boolean) value) completed++;
                                }
                            }
                        }
                        results.add(day + ": " + completed + " од " + total + " завршени");
                        if (results.size() == days.size()) {
                            updateListView(results);
                        }
                    });
        }
    }

    private void updateListView(List<String> data) {
        Collections.sort(data); // за конзистентен редослед
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
        progressListView.setAdapter(adapter);
    }
}
