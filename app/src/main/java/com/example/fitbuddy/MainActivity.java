package com.example.fitbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

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

        if (user != null) {
            if (user.isAnonymous()) {
                welcomeTextView.setText(getString(R.string.welcome_guest));
            } else {
                String nameOrEmail = user.getDisplayName() != null ? user.getDisplayName() : user.getEmail();
                welcomeTextView.setText(getString(R.string.welcome_user, nameOrEmail));
            }
        } else {
            welcomeTextView.setText(getString(R.string.welcome_generic));
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

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String token = task.getResult();
                        Log.d("FCM", "Token from MainActivity: " + token);
                    }
                });

    }
}
