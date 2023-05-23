package com.example.listen;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
public class BuatPlaylist extends AppCompatActivity {

    private Button buttonBuat;

    private Button buttonKeluar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buat_playlist);

        buttonBuat = findViewById(R.id.Buat);
        buttonKeluar = findViewById(R.id.Keluar);

        buttonBuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        buttonKeluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}