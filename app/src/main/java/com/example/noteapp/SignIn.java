package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class SignIn extends AppCompatActivity {

    private TextView wanttologin;
    private TextView signup;
    private EditText mail;
    private EditText pass;
     private  FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        wanttologin = findViewById(R.id.forgotpass2);
        signup = findViewById(R.id.signup);
        mail=findViewById(R.id.loginemail2);
        pass=findViewById(R.id.loginpassword2);

        firebaseAuth = FirebaseAuth.getInstance();

        wanttologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignIn.this,MainActivity.class);
                startActivity(intent);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = mail.getText().toString().trim();
                String password = pass.getText().toString().trim();
                if(email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(SignIn.this, "Mention all details above", Toast.LENGTH_SHORT).show();
                }
                else if(password.length()<7){
                    Toast.makeText(SignIn.this, "Password Should Be Greater Than 7 Digits", Toast.LENGTH_SHORT).show();
                }
                else{
                    firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(SignIn.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                sendEmailVerification();
                            }
                        }
                    });
                }
            }
        });
    }

    private void sendEmailVerification(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser!=null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(SignIn.this, "Verification Email is sent, Verify and Login Again", Toast.LENGTH_SHORT).show();
                    firebaseAuth.signOut();
                    finish();
                    startActivity(new Intent(SignIn.this,MainActivity.class));
                }
            });


        }
        else{
            Toast.makeText(this, "Failed to Sent Email Verification", Toast.LENGTH_SHORT).show();
        }
    }
}