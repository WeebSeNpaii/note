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
import com.google.firebase.auth.FirebaseAuth;

public class forgotpass extends AppCompatActivity {

  private TextView goback;
  private TextView recover;
  private EditText regmail;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpass);

        goback = findViewById(R.id.goback);
        recover =findViewById(R.id.recover);
        regmail=findViewById(R.id.loginemail3);
        firebaseAuth=FirebaseAuth.getInstance();

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(forgotpass.this,MainActivity.class);
                startActivity(intent);
            }
        });

        recover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String register = regmail.getText().toString().trim();
                if(register.isEmpty()) {
                    Toast.makeText(forgotpass.this, "Enter Your Mail", Toast.LENGTH_SHORT).show();
                }
                else{
                    firebaseAuth.sendPasswordResetEmail(register).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(forgotpass.this, "Mail Sent, You can recover your password!", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(forgotpass.this,MainActivity.class));
                            }
                            else{
                                Toast.makeText(forgotpass.this, "Account Not Exist", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });





    }
}