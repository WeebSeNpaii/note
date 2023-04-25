package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.Query;

import java.util.Objects;

public class Notes extends AppCompatActivity {


    FloatingActionButton add;
    NoteAdapter noteAdapter;

   private   FirebaseAuth firebaseAuth;
    RecyclerView recyclerView;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        Objects.requireNonNull(getSupportActionBar()).setTitle("All Notes");
        firebaseAuth = FirebaseAuth.getInstance();
          recyclerView=findViewById(R.id.recycleview);
          firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
          firebaseFirestore= FirebaseFirestore.getInstance();

        add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Notes.this, addnote.class);
                startActivity(intent);
            }
        });
        setuprecyclerview();

    }

 void setuprecyclerview(){
        Query query = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").orderBy("title",Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<firebasemodel> options = new FirestoreRecyclerOptions.Builder<firebasemodel>().setQuery(query,firebasemodel.class).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
       noteAdapter = new NoteAdapter(options,this);
       recyclerView.setAdapter(noteAdapter);
 }

    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        noteAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        noteAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.logout:
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(Notes.this,MainActivity.class));

        }
        return super.onOptionsItemSelected(item);
    }
}