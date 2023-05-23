package com.example.listen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.example.listen.Adapter.RecyclerViewAdapter;
import com.example.listen.Model.UploadAlbum2;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity3 extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    DatabaseReference mDatabase;
    ProgressDialog progressDialog;
    private List<UploadAlbum2> uploads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        recyclerView = findViewById(R.id.recylcerViewOnAlbum2);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        uploads = new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance().getReference("uploads");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                for (DataSnapshot postsnapshot : dataSnapshot.getChildren()) {
                    UploadAlbum2 upload = postsnapshot.getValue(UploadAlbum2.class);
                    uploads.add(upload);
                }
                adapter = new RecyclerViewAdapter(getApplicationContext(),uploads);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}