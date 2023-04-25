package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class addnote extends AppCompatActivity {

    private EditText title,content;
    FloatingActionButton save;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    ProgressBar progressbaraddnote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnote);

        title=findViewById(R.id.titlehere);
        content=findViewById(R.id.contentnote);
        save= findViewById(R.id.savenote);
        progressbaraddnote = findViewById(R.id.progressbarofaddnote);

        Toolbar toolbar = findViewById(R.id.toolbarofaddnote);


        firebaseAuth= FirebaseAuth.getInstance();
        firebaseFirestore= FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();



        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titlehere= title.getText().toString();
                String contenthere= content.getText().toString();

                if(titlehere.isEmpty() || contenthere.isEmpty()){
                    Toast.makeText(addnote.this, "All fields are Require", Toast.LENGTH_SHORT).show();
                }
                else{

                    progressbaraddnote.setVisibility(v.VISIBLE);
                    DocumentReference documentReference = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document();
                    Map<String,Object> notes = new HashMap<>();
                    notes.put("title",titlehere);
                    notes.put("content",contenthere);

                    documentReference.set(notes).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(addnote.this, "Note Saved Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(addnote.this,Notes.class));

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressbaraddnote.setVisibility(v.INVISIBLE);
                            Toast.makeText(addnote.this, "Failed To Create Note", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}