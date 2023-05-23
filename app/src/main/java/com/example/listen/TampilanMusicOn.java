package com.example.listen;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class TampilanMusicOn extends AppCompatActivity {
    Button btnnext, btnprev, acak, lyric, playbtn;
    TextView txtsname, txtstart, txtstop,textartis;
    SeekBar seekmusic;
    ImageView imageView, imageBack;
    ImageButton rewindbtn;
    String sname;
    public static final String EXTRA_NAME = "song_name";
    static MediaPlayer mediaPlayer;
    int position;
    ArrayList<String> songPaths;

    Thread updateseekbar;
    int totalDuration;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tampilanmusic);
        textartis = findViewById(R.id.textView4);
        btnnext = findViewById(R.id.btnnext);
        rewindbtn = findViewById(R.id.rewindbtn);
        btnprev = findViewById(R.id.btnprev);
        playbtn = findViewById(R.id.playbtn);
        acak = findViewById(R.id.acak);
        txtsname = findViewById(R.id.txtsname);
        lyric = findViewById(R.id.lyrics);
        txtstart = findViewById(R.id.txtstart);
        seekmusic = findViewById(R.id.seekBar);
        imageView = findViewById(R.id.imageView8);
        imageBack = findViewById(R.id.imageBack4);
        txtstop = findViewById(R.id.txtstop);

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        Intent i = getIntent();
        if (i != null) {
            sname = i.getStringExtra("songTitle");
            String artist = i.getStringExtra("artist");
            int durationInMillis = i.getIntExtra("duration", 0);
            String endTime = createTime(durationInMillis);
            textartis.setSelected(true);
            textartis.setText(artist);
            txtstop.setText(endTime);
            songPaths = i.getStringArrayListExtra("songPaths");
            position = i.getIntExtra("selectedSongIndex", 0);
            txtstop.setSelected(true);
            txtsname.setSelected(true);
            txtsname.setText(sname);

            Uri uri = Uri.parse(songPaths.get(position));
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
        }

        updateseekbar = new Thread() {
            @Override
            public void run() {
                totalDuration = mediaPlayer.getDuration();
                int currentposition = 0;
                while (currentposition < totalDuration) {
                    try {
                        sleep(500);
                        currentposition = mediaPlayer.getCurrentPosition();
                        seekmusic.setProgress(currentposition);
                    } catch (InterruptedException | IllegalStateException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        seekmusic.setMax(mediaPlayer.getDuration());
        updateseekbar.start();
        seekmusic.getProgressDrawable().setColorFilter(getResources().getColor(R.color.Oren), PorterDuff.Mode.MULTIPLY);
        seekmusic.getThumb().setColorFilter(getResources().getColor(R.color.Oren), PorterDuff.Mode.SRC_IN);

        seekmusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        String endTime = createTime(mediaPlayer.getDuration());
        txtstop.setText(endTime);

        final Handler handler = new Handler();
        final int delay = 1000;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String currentTime = createTime(mediaPlayer.getCurrentPosition());
                txtstart.setText(currentTime);
                handler.postDelayed(this, delay);
            }
        }, delay);

        playbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    playbtn.setBackgroundResource(R.drawable.playmusik);
                    mediaPlayer.pause();
                } else {
                    playbtn.setBackgroundResource(R.drawable.pause);
                    mediaPlayer.start();
                }
            }
        });

        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                position = ((position + 1) % songPaths.size());
                Uri u = Uri.parse(songPaths.get(position));
                mediaPlayer = MediaPlayer.create(getApplicationContext(), u);
                sname = songPaths.get(position);
                txtsname.setText(sname);
                mediaPlayer.start();
                playbtn.setBackgroundResource(R.drawable.pause);
                startAnimation(imageView);
                int audioSessionId = mediaPlayer.getAudioSessionId();
            }
        });

        btnprev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                position = ((position - 1) < 0 ? (songPaths.size() - 1) : (position - 1));
                Uri u = Uri.parse(songPaths.get(position));
                mediaPlayer = MediaPlayer.create(getApplicationContext(), u);
                sname = songPaths.get(position);
                txtsname.setText(sname);
                mediaPlayer.start();
                playbtn.setBackgroundResource(R.drawable.pause);
                startAnimation(imageView);
                int audioSessionId = mediaPlayer.getAudioSessionId();
            }
        });

        acak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (acak.getTag().equals("unshuffle")) {
                    acak.setTag("shuffle");
                    acak.setBackgroundResource(R.drawable.acaklagu);
                    Collections.shuffle(songPaths);
                } else {
                    acak.setTag("unshuffle");
                    acak.setBackgroundResource(R.drawable.acaklagu);
                }
            }
        });

        rewindbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 10000);
            }
        });
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        mediaPlayer.stop();
        super.onBackPressed();
    }

    public String createTime(int duration) {
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

    public void startAnimation(View view) {
        // Implement your animation logic here
    }
}