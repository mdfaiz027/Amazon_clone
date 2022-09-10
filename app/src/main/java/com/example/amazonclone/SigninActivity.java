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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SigninActivity extends AppCompatActivity {

    EditText email, password;
    private String emailInput, passwordInput;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    ProgressDialog progressDialog;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        getSupportActionBar().hide();

        email = findViewById(R.id.siETEmail);
        password = findViewById(R.id.siETPassword);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        sharedPreferences = getSharedPreferences("LOGIN", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        firebaseAuth = FirebaseAuth.getInstance();


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

    public void navigate_to_signup_page(View view) {
        startActivity(new Intent(SigninActivity.this, SignupActivity.class));

    }

    public void signIn(View view) {
        if (!validateEmail()) {
            return;
        }else if(!validatePassword()){
            return;
        }

        checkUser();
    }

    private void checkUser() {

        progressDialog.show();
        progressDialog.setMessage("loading....");

        emailInput = email.getText().toString();
        passwordInput = password.getText().toString();

        firebaseAuth.signInWithEmailAndPassword(emailInput,passwordInput).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
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
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();

                Toast.makeText(SigninActivity.this, ""+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void navigateToHomeActivity() {
        Intent intent = new Intent(SigninActivity.this, HomeActivity.class);
        startActivity(intent);
        Toast.makeText(this, "Successfully Logged In", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(sharedPreferences.getBoolean("status", false) == true){
            startActivity(new Intent(SigninActivity.this, HomeActivity.class));
        }

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(SigninActivity.this);

        if(account!=null){
            Intent intent = new Intent(SigninActivity.this, HomeActivity.class);
            startActivity(intent);
        }
    }
}