package com.example.fitbuddy;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private TextView welcomeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("LoginDebug", "MainActivity started");
        setContentView(R.layout.activity_main); // ќе ја направиме подолу

        welcomeText = findViewById(R.id.welcomeText);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Log.d("MainActivityDebug", "User: " + user);
        if (user != null) {
            if (user.isAnonymous()) {
                welcomeText.setText("Welcome, guest!");
            } else {
                String message = "Welcome, " + (user.getEmail() != null ? user.getEmail() : "user");
                welcomeText.setText(message);
            }
        } else {
            welcomeText.setText("Welcome, unknown user");
            Log.d("MainActivityDebug", "FirebaseAuth.getInstance().getCurrentUser() returned null");
        }
    }
}