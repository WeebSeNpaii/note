package com.example.noteapp;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class NoteAdapter extends FirestoreRecyclerAdapter<firebasemodel,NoteAdapter.NoteViewHolder> {
    Context context;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;

    public NoteAdapter(@NonNull FirestoreRecyclerOptions<firebasemodel> options, Context context) {
        super(options);
        this.context=context;

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore= FirebaseFirestore.getInstance();
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull firebasemodel notes) {
         holder.titletextview.setText(notes.getTitle());
         holder.contenttextview.setText(notes.getContent());
        String docId = this.getSnapshots().getSnapshot(position).getId();

           holder.itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   Intent intent = new Intent(view.getContext(),notedetails.class);
                    intent.putExtra("title", notes.getTitle());
                    intent.putExtra("content",notes.getContent());

                    intent.putExtra("noteId",docId);
                   view.getContext().startActivity(intent);
               }
           });

             ImageView popupbutton = holder.itemView.findViewById(R.id.menuButton);



          popupbutton.setOnClickListener(new View.OnClickListener() {


              @Override
              public void onClick(View view) {
                  PopupMenu popupMenu = new PopupMenu(view.getContext(),view);



                  popupMenu.getMenu().add("Edit").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                      @Override
                      public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                          Intent intent = new Intent(view.getContext(),editnote.class);
                          intent.putExtra("title",notes.getTitle());
                          intent.putExtra("content",notes.getContent());
                          intent.putExtra("noteId",docId);
                        view.getContext().startActivity(intent);
                          return false;
                      }
                  });

                  popupMenu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                      @Override
                      public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                          DocumentReference documentReference = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document(docId);
                          documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                              @Override
                              public void onSuccess(Void unused) {
                                  Toast.makeText(context, "Note Deleted", Toast.LENGTH_SHORT).show();
                              }
                          }).addOnFailureListener(new OnFailureListener() {
                              @Override
                              public void onFailure(@NonNull Exception e) {
                                  Toast.makeText(context, "Failed To Delete", Toast.LENGTH_SHORT).show();
                              }
                          });
                          return false;
                      }
                  });
                  popupMenu.show();
              }
          });


    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclernoteitem,parent,false);
        return new NoteViewHolder(view);
    }

    class NoteViewHolder extends RecyclerView.ViewHolder{

        TextView titletextview,contenttextview;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            titletextview= itemView.findViewById(R.id.notetitletext);
            contenttextview=itemView.findViewById(R.id.contentmaster);

        }
    }
}
