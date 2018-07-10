package com.codepath.alveera.parstagram;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    EditText tvUsername;
    EditText tvPassword;
    Button bLogin;
    Button bSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tvUsername = (EditText) findViewById(R.id.tvUsername);
        tvPassword = (EditText) findViewById(R.id.tvPassword);
        bLogin = (Button) findViewById(R.id.bLogin);
        bSignup = (Button) findViewById(R.id.bSignup);
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            Intent i = new Intent(LoginActivity.this, InstaActivity.class);
            startActivity(i);
            Toast.makeText(LoginActivity.this, "Automatically logged in!", Toast.LENGTH_LONG).show();
            finish();
        }

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userAttempt = String.valueOf(tvUsername.getText());
                String passAttempt = String.valueOf(tvPassword.getText());
                ParseUser.logInInBackground(userAttempt, passAttempt, new LogInCallback() {
                    public void done(ParseUser user, ParseException e) {
                        if (user != null) {
                            Intent i = new Intent(LoginActivity.this, InstaActivity.class);
                            startActivity(i);
                            Toast.makeText(LoginActivity.this, "Logged in!", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Incorrect Log in/password!", Toast.LENGTH_LONG).show();
                            tvUsername.setText("");
                            tvPassword.setText("");
                        }
                    }
                });
            }
        });

        bSignup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                // Create the ParseUser
                ParseUser user = new ParseUser();
                // Set core properties
                user.setUsername(String.valueOf(tvUsername.getText()));
                user.setPassword(String.valueOf(tvPassword.getText()));
                // Invoke signUpInBackground
                user.signUpInBackground(new SignUpCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            Intent i = new Intent(LoginActivity.this, InstaActivity.class);
                            startActivity(i);
                            Toast.makeText(LoginActivity.this, "Signed up!", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Username taken or some other issue!", Toast.LENGTH_LONG).show();
                            tvUsername.setText("");
                            tvPassword.setText("");
                        }
                    }
                });
            }
        });
    }
}

