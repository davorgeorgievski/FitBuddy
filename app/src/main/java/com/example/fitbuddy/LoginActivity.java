package com.example.fitbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.*;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.*;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_GOOGLE_SIGN_IN = 9001;

    private FirebaseAuth mAuth;
    private CallbackManager callbackManager;
    private FirebaseAnalytics analytics;

    private EditText emailEditText, passwordEditText;
    private Button emailLoginButton, googleLoginButton, facebookLoginButton, anonymousLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        analytics = FirebaseAnalytics.getInstance(this);

        Bundle screenBundle = new Bundle();
        screenBundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "LoginActivity");
        screenBundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "LoginActivity");
        analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, screenBundle);

        // Init UI
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        emailLoginButton = findViewById(R.id.emailLoginButton);
        googleLoginButton = findViewById(R.id.googleLoginButton);
        facebookLoginButton = findViewById(R.id.facebookLoginButton);
        anonymousLoginButton = findViewById(R.id.anonymousLoginButton);

        // Email login
        emailLoginButton.setOnClickListener(v -> {
            logLoginEvent("Email");
            loginWithEmail();
        });

        // Anonymous login
        anonymousLoginButton.setOnClickListener(v -> {
            logLoginEvent("Anonymous");
            loginAnonymously();
        });

        // Google login
        googleLoginButton.setOnClickListener(v -> {
            logLoginEvent("Google");
            signInWithGoogle();
        });

        // Facebook login
        callbackManager = CallbackManager.Factory.create();
        facebookLoginButton.setOnClickListener(v -> {
            logLoginEvent("Facebook");
            loginWithFacebook();
        });
    }

    private void logLoginEvent(String method) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.METHOD, method);
        analytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle);
    }

    private void loginWithEmail() {
        String email = emailEditText.getText().toString().trim();
        String pass = passwordEditText.getText().toString().trim();

        if (email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Enter all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        openMain();
                    } else {
                        Toast.makeText(this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loginAnonymously() {
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        openMain();
                    } else {
                        Toast.makeText(this, "Anonymous login failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void signInWithGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleSignInClient client = GoogleSignIn.getClient(this, gso);
        Intent signInIntent = client.getSignInIntent();
        startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN);
    }

    private void loginWithFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        AuthCredential credential = FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken());
                        mAuth.signInWithCredential(credential)
                                .addOnCompleteListener(LoginActivity.this, task -> {
                                    if (task.isSuccessful()) {
                                        openMain();
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Facebook login failed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(LoginActivity.this, "Facebook login canceled", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Toast.makeText(LoginActivity.this, "Facebook error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (callbackManager != null) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

        if (requestCode == RC_GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                mAuth.signInWithCredential(credential)
                        .addOnCompleteListener(this, task1 -> {
                            if (task1.isSuccessful()) {
                                openMain();
                            } else {
                                Toast.makeText(this, "Google sign-in failed", Toast.LENGTH_SHORT).show();
                            }
                        });
            } catch (ApiException e) {
                Toast.makeText(this, "Google sign-in error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openMain() {
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
