package com.example.logsignsql;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Button buttonLihatData = findViewById(R.id.buttonLihatData);
        Button buttonInputData = findViewById(R.id.buttonInputData);
        Button buttonInformasi = findViewById(R.id.buttonInformasi);
        Button buttonLogout = findViewById(R.id.buttonLogout);

        buttonLihatData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Membuat intent untuk memulai aktivitas LihatDataActivity
                Intent intent = new Intent(MainActivity2.this, ListView.class);
                startActivity(intent);
            }
        });

        buttonInputData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Membuat intent untuk memulai aktivitas InputDataActivity
                Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                startActivity(intent);
            }
        });

        buttonInformasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Membuat intent untuk memulai aktivitas InformasiActivity
                Intent intent = new Intent(MainActivity2.this, Information.class);
                startActivity(intent);
            }
        });

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Membuat intent untuk memulai aktivitas InformasiActivity
                Intent intent = new Intent(MainActivity2.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}