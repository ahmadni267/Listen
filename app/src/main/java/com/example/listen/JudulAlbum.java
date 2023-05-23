package com.example.listen;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JudulAlbum extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView textViewJudulAlbum;
    private List<UploadSong> songsList;
    static MediaPlayer mediaPlayer;
    private SongAdapter adapter;
    private ImageView imageViewAlbum, imageView5;
    private static ArrayList<String> songPaths;
    private String albumId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_judul_album);
        FirebaseApp.initializeApp(this);

        recyclerView = findViewById(R.id.recylerMusikAlbum);
        textViewJudulAlbum = findViewById(R.id.textView6);
        imageView5 = findViewById(R.id.imageView5);
        imageViewAlbum = findViewById(R.id.imgAlbumOnn);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        songsList = new ArrayList<>();
        adapter = new SongAdapter(songsList);
        recyclerView.setAdapter(adapter);
        Intent intent = getIntent();
        albumId = intent.getStringExtra("albumId");

        String albumTitle = intent.getStringExtra("name");
        textViewJudulAlbum.setText(albumTitle);
        String albumImageUrl = intent.getStringExtra("url");
        Glide.with(this)
                .load(albumImageUrl)
                .into(imageViewAlbum);

        DatabaseReference songsReference = FirebaseDatabase.getInstance().getReference("uploads");
        Query songsQuery = songsReference.orderByChild("albumId").equalTo(albumId);

        songsQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                UploadSong upload = dataSnapshot.getValue(UploadSong.class);
                if (upload != null) {
                    songsList.add(upload);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                // Handle child changed event if needed
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                // Handle child removed event if needed
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                // Handle child moved event if needed
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error if needed
            }
        });

        imageView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {
        private List<UploadSong> songs;

        public SongAdapter(List<UploadSong> songs) {
            this.songs = songs;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_musik, null);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            UploadSong song = songs.get(position);
            holder.songTitle.setText(song.getSongTitle());
            holder.songArtist.setText(song.getArtist());

            int durationInMillis = Integer.parseInt(song.getSongDuration());
            String durationText = createTime(durationInMillis);
            holder.songDuration.setText(song.getSongDuration());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mediaPlayer != null) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                    }

                    String songUrl = song.getUrl();
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                    try {
                        mediaPlayer.setDataSource(songUrl);
                        mediaPlayer.prepareAsync();
                        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                mp.start();
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        private String createTime(int duration) {
            String time = "";
            int min = duration / 1000 / 60;
            int sec = duration / 1000 % 60;
            time += min + ":";
            if (sec < 10) {
                time += "0";
            }
            time += sec;
            return time;
        }


        @Override
        public int getItemCount() {
            return songs.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView songTitle;
            private TextView songArtist;
            private TextView songDuration;
            private TextView songNumber;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                songTitle = itemView.findViewById(R.id.txtsongname);
                songArtist = itemView.findViewById(R.id.ArtisLagu);
                songDuration = itemView.findViewById(R.id.DurasiLagu);
                songNumber = itemView.findViewById(R.id.textNomor);
            }
        }

    }
}