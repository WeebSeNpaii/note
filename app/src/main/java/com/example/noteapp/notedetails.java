package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class notedetails extends AppCompatActivity {
   private TextView titlenotedetail,contentnotedetail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notedetails);

        titlenotedetail = findViewById(R.id.titleofnotedetail);
        contentnotedetail= findViewById(R.id.contentofnotedetail);





        Intent data = getIntent();


        titlenotedetail.setText(data.getStringExtra("title"));
        contentnotedetail.setText(data.getStringExtra("content"));
    }


    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}