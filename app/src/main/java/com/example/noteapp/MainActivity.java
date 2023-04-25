package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private TextView login;
    private TextView signin;
    private TextView forgot;
    ProgressBar progressBarofmain;

    private FirebaseAuth firebaseAuth;

       @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        forgot = findViewById(R.id.forgotpass);

      signin = findViewById(R.id.signin);
      email = findViewById(R.id.loginemail);
      password = findViewById(R.id.loginpassword);
      login = findViewById(R.id.mainlogin);
      firebaseAuth= FirebaseAuth.getInstance();
      progressBarofmain = findViewById(R.id.progressbarmain);
      FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser!=null){
            finish();
            startActivity(new Intent(MainActivity.this,Notes.class));
        }

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,forgotpass.class);
                startActivity(intent);
            }
        });


           signin.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent = new Intent(MainActivity.this,SignIn.class);
                   startActivity(intent);
               }
           });

           login.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   String mail = email.getText().toString().trim();
                   String pass = password.getText().toString().trim();

                   if(mail.isEmpty() || pass.isEmpty()){
                       Toast.makeText(MainActivity.this, "Mention all details above", Toast.LENGTH_SHORT).show();
                   }
                   else{

                       progressBarofmain.setVisibility(v.VISIBLE);
                       firebaseAuth.signInWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                           @Override
                           public void onComplete(@NonNull Task<AuthResult> task) {
                               if(task.isSuccessful()){
                                   checkmailverification();
                               }
                               else{
                                   Toast.makeText(MainActivity.this, "Account Doesn't Exist", Toast.LENGTH_SHORT).show();
                                   progressBarofmain.setVisibility(v.INVISIBLE);
                               }
                           }
                       });
                   }
               }
           });

       }
       private void checkmailverification(){
           FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
           if(firebaseUser.isEmailVerified()==true){
               Toast.makeText(this, "Logged In", Toast.LENGTH_SHORT).show();
               finish();
               startActivity(new Intent(MainActivity.this,Notes.class));
           }
           else{
               progressBarofmain.setVisibility(View.INVISIBLE);
               Toast.makeText(this, "Verify Your Email First", Toast.LENGTH_SHORT).show();
           }
       }
}