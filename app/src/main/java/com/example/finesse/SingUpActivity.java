package com.example.finesse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.ktx.Firebase;

public class SingUpActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText singUpEmail, singUpPassword;
    private Button singUpButton;
    private TextView loginRedirectText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        auth = FirebaseAuth.getInstance();
        singUpEmail = findViewById(R.id.singUp_email);
        singUpPassword = findViewById(R.id.singUp_password);
        singUpButton = findViewById(R.id.singUpButton);
        loginRedirectText =findViewById(R.id.loginRedirectText);

        singUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String user =  singUpEmail.getText().toString().trim();
               String pass = singUpPassword.getText().toString().trim();

               if(user.isEmpty()){
                   singUpEmail.setError("Email cannot be empty");
               }

               if(pass.isEmpty()){
                  singUpPassword.setError("Password cannot be empty");
               }else{
                   auth.createUserWithEmailAndPassword(user,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {
                           if(task.isSuccessful()){
                               Toast.makeText(SingUpActivity.this, "SingUp successful", Toast.LENGTH_SHORT).show();
                               startActivity(new Intent(SingUpActivity.this, LogInActivity.class));
                           }else{
                               Toast.makeText(SingUpActivity.this, "SingUp failed"+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                           }
                       }
                   });
               }
            }
        });

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(SingUpActivity.this, LogInActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        }
    }
