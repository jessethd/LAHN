package com.jthd.lahn;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 *
 */
public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button createAccountButton;
    //private Button mLoginWithFacebookButton;

    private String email;
    private String password;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // If a user is cached, skip straight to FeedActivity
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            toHouseSelect();
            Log.d("LoginActivity", "boop");
        }
        setContentView(R.layout.activity_login);
        emailEditText = (EditText)findViewById(R.id.emailEditText);
        passwordEditText = (EditText)findViewById(R.id.passwordEditText);
        loginButton = (Button)findViewById(R.id.loginButton);
        createAccountButton = (Button)findViewById(R.id.createAccountButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logIn();
            }
        });
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toCreateAccount();

            }
        });
        emailEditText.requestFocus();
        // TODO login with facebook button here

    }


    @Override
    protected void onResume(){
        super.onResume();

    }


    /**
     * Logs the user into Parse using the provided information. If username is provided, simply log in with username. If email is provided, lookup
     * associated username and log in. If login completes successfully, launch FeedActivity. If login fails, display relevant failure information.
     */
    private void logIn(){
        // Log out potential current user to prevent invalid session token issues (error 209)
        ParseUser currentUser = ParseUser.getCurrentUser();
        currentUser.logOut();
        email = emailEditText.getText().toString();
        password = passwordEditText.getText().toString();
        View focusView = checkInputs();
        if (focusView != null) {
            // There was an input error; don't attempt to log in, and focus view that is source of error
            focusView.requestFocus();
        } else {
        // Log in with provided email and password
            ParseUser.logInInBackground(email, password, new LogInCallback() {
                public void done(ParseUser user, ParseException e) {
                    if (user != null) {
                        Log.d("LoginActivity", "Login success");
                        toHouseSelect();
                    } else {
                        // Login failed due to validation errors. Don't be specific about why.
                        loginFailure(e);
                    }
                }
            });
        }

    }

    /**
     * Checks the EditText inputs for email/username and password for validity; in this case, ensures that they are not empty. If a field is invalid,
     * displays error message and returns appropriate View for focusing.
     * @return Returns the View that caused the input error.
     */
    private View checkInputs(){
        View focusView = null;

        // check email/username for valid input
        if (TextUtils.isEmpty(email)){
            emailEditText.setError(getString(R.string.error_field_required);
            focusView = emailEditText;
        }

        // check password for valid input
        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError(getString(R.string.error_field_required));
            focusView = passwordEditText;
        }
        return focusView;

    }

    /**
     * Displays error messages and sets focus if Parse login fails.
     * @param e For debugging, the ParseException associated with the login failure.
     */
    private void loginFailure(ParseException e){
        emailEditText.setError("Login failed.");
        emailEditText.requestFocus();
        emailEditText.setText("");
        passwordEditText.setText("");
        //Log.d("LoginActivity", Integer.toString(e.getCode()));
    }

    /**
     * Launches SelectHouseActivity. Flags clear backstack so as to prevent the user from visiting LoginActivity or CreateAccountActivity without signing out.
     */
    private void toHouseSelect(){
        Intent intent = new Intent(LoginActivity.this, SelectHouseActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        LoginActivity.this.startActivity(intent);
    }



    /**
     * Launches CreateAccountActivity.
     */
    private void toCreateAccount(){
        Intent createAccIntent = new Intent(LoginActivity.this, CreateAccountActivity.class);
        LoginActivity.this.startActivity(createAccIntent);
    }



}
