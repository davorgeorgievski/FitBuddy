package com.example.fitbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private TextView welcomeTextView;
    private Button workoutPlanButton, progressButton, logoutButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        welcomeTextView = findViewById(R.id.welcomeTextView);
        workoutPlanButton = findViewById(R.id.workoutPlanButton);
        progressButton = findViewById(R.id.progressButton);
        logoutButton = findViewById(R.id.logoutButton);

        // Поправено прикажување на текстот за добредојде
        if (user != null) {
            if (user.isAnonymous()) {
                welcomeTextView.setText("Добредојде, кориснику!");
            } else if (user.getEmail() != null) {
                welcomeTextView.setText("Добредојде, " + user.getEmail() + "!");
            } else {
                welcomeTextView.setText("Добредојде!");
            }
        } else {
            welcomeTextView.setText("Добредојде!");
        }

        workoutPlanButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TrainingPlanActivity.class);
            startActivity(intent);
        });

        progressButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProgressActivity.class);
            startActivity(intent);
        });


        logoutButton.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        });
    }
}
