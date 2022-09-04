package com.example.amazonclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {

    EditText name, email, password, re_enter_password;
    private String nameInput, emailInput, passwordInput, re_enter_passwordInput;

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[@#$%^&+=])" +     // at least 1 special character
                    "(?=\\S+$)" +            // no white spaces
                    ".{8,}" +                // at least 4 characters
                    "$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        getSupportActionBar().hide();

        name = findViewById(R.id.suETName);
        email = findViewById(R.id.suETEmail);
        password = findViewById(R.id.suETPassword);
        re_enter_password = findViewById(R.id.suETRePassword);


    }

    public void createAccount(View view) {

        if (!validateName()) {
            return;
        }else if(!validateEmail()){
            return;
        }else if(!validatePassword()){
            return;
        }else if(!validateReEnterPassword()){
            return;
        }

    }

    private boolean validateName() {

        nameInput = name.getText().toString();

        if(nameInput.matches("^[\\p{L} .'-]+$")){
            return true;
        }else{
            name.setError("Enter a valid name!");
            return false;
        }

    }

    private boolean validateEmail() {

        // Extract input from EditText
        emailInput = email.getText().toString().trim();

        // if the email input field is empty
        if (emailInput.isEmpty()) {
            email.setError("Field can not be empty");
            return false;
        }

        // Matching the input email to a predefined email pattern
        else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            email.setError("Please enter a valid email address");
            return false;
        } else {
            email.setError(null);
            return true;
        }

    }

    private boolean validatePassword() {

        passwordInput = password.getText().toString().trim();
        // if password field is empty
        // it will display error message "Field can not be empty"
        if (passwordInput.isEmpty()) {
            password.setError("Field can not be empty");
            return false;
        }

        // it will display an error message "Must contain at least 6 characters"
        else if (passwordInput.length() < 6) {
            password.setError("Must contain at least 6 characters");
            return false;
        } else {
            password.setError(null);
            return true;
        }

    }

    private boolean validateReEnterPassword() {

        re_enter_passwordInput = re_enter_password.getText().toString().trim();

        if (!passwordInput.equals(re_enter_passwordInput)){
            re_enter_password.setError("Password does not match");
            return false;
        }else{
            re_enter_password.setError(null);
            return true;
        }
    }


    public void goToSigninPage(View view) {
        startActivity(new Intent(SignupActivity.this, SigninActivity.class));
    }
}