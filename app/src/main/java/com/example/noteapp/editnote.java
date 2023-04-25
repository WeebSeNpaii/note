package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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

public class editnote extends AppCompatActivity {

    Intent data;
    EditText titleofnote,contentofnote;
    FloatingActionButton savednote;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editnote);

        titleofnote = findViewById(R.id.edittitleofnote);
        contentofnote = findViewById(R.id.editcontentnote);
        savednote = findViewById(R.id.saveeditnote);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        data = getIntent();




       savednote.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
              String newtitle = titleofnote.getText().toString();
              String newcontent = contentofnote.getText().toString();

              if(newtitle.isEmpty() || newcontent.isEmpty()){
                  Toast.makeText(editnote.this, "Something is empty", Toast.LENGTH_SHORT).show();
                  return;
              }
              else{
                  DocumentReference documentReference = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document(data.getStringExtra("noteId"));
                  Map<String,Object> note = new HashMap<>();
                  note.put("title",newtitle);
                  note.put("content",newcontent);
                  Intent intent = new Intent(editnote.this,Notes.class);
                  startActivity(intent);
                  documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                      @Override
                      public void onSuccess(Void unused) {
                          Toast.makeText(editnote.this, "Note Updated", Toast.LENGTH_SHORT).show();
                      }
                  }).addOnFailureListener(new OnFailureListener() {
                      @Override
                      public void onFailure(@NonNull Exception e) {
                          Toast.makeText(editnote.this, "Failed Updation", Toast.LENGTH_SHORT).show();
                      }
                  });
              }
           }
       });

        String notetitle= data.getStringExtra("title");
        String notecontent= data.getStringExtra("content");
        contentofnote.setText(notecontent);
        titleofnote.setText(notetitle);

    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}