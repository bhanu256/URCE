package com.specialteam.urce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Comment extends AppCompatActivity {

    FirebaseRecyclerAdapter<CommentsData,CommentViewHolder> firebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        Bundle extras = getIntent().getExtras();

        String id = extras.getString("CommentId");

        Query query = FirebaseDatabase.getInstance().getReference().child("Comments").child(id);

        /*DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Comments").child(id);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    System.out.println(dataSnapshot1.getValue());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

        FirebaseRecyclerOptions<CommentsData> options = new FirebaseRecyclerOptions.Builder<CommentsData>()
                .setQuery(query,CommentsData.class)
                .build();

         firebaseRecyclerAdapter= new FirebaseRecyclerAdapter<CommentsData, CommentViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CommentViewHolder commentViewHolder, int i, @NonNull CommentsData commentsData) {
                String name = commentsData.getOname();
                String comment = commentsData.getComment();
                commentViewHolder.Oname.setText(name);
                commentViewHolder.comments.setText(comment);
            }

            @NonNull
            @Override
            public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_adaptor_card,parent,false);
                return new CommentViewHolder(v);
            }
        };

        RecyclerView recyclerView = findViewById(R.id.commentRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseRecyclerAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseRecyclerAdapter.stopListening();
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {

        TextView Oname;
        TextView comments;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            Oname = itemView.findViewById(R.id.commentedName);
            comments = itemView.findViewById(R.id.CommentsBy);
        }
    }
}
