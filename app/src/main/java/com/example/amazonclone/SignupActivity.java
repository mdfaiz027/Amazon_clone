package com.example.amazonclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {

    EditText name, email, password, re_enter_password;
    private String nameInput, emailInput, passwordInput, re_enter_passwordInput;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    ProgressDialog progressDialog;

    FirebaseAuth firebaseAuth;


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

        sharedPreferences = getSharedPreferences("LOGIN", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        firebaseAuth = FirebaseAuth.getInstance();


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

        createUser();

    }

    private void createUser() {

        progressDialog.show();
        progressDialog.setMessage("Creating user....");

        emailInput = email.getText().toString();
        passwordInput = password.getText().toString();

        firebaseAuth.createUserWithEmailAndPassword(emailInput,passwordInput).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();

                if(!task.isSuccessful()){
                    //Toast.makeText(getContext(), ""+task.getException(), Toast.LENGTH_SHORT).show();
                }else{
                    editor.putString("usermail", emailInput);
                    editor.putBoolean("status", true);
                    editor.commit();

                    navigateToHomeActivity();
                    Toast.makeText(SignupActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();

                Toast.makeText(SignupActivity.this, ""+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToHomeActivity() {
        Intent intent = new Intent(SignupActivity.this, HomeActivity.class);
        startActivity(intent);
    }


    private boolean validateName() {

        nameInput = name.getText().toString();

        if (nameInput.isEmpty()) {
            name.setError("Field can not be empty");
            return false;
        }else if(nameInput.matches("^[\\p{L} .'-]+$")){
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