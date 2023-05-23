package com.example.listen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
public class Koneksi extends AppCompatActivity {
    Button koneksi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_koneksi_alat);
        koneksi = findViewById(R.id.btnkoneksi);
        koneksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Koneksi.this, AturJam.class));
            }
        });

    }
}