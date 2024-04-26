package com.example.logsignsql;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    // Waktu tampilan splash screen (dalam milidetik)
    private static final int SPLASH_TIME_OUT = 5000; // 3 detik

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Mengatur tampilan splash screen untuk beberapa waktu kemudian
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Setelah waktu tertentu, pindah ke layar berikutnya
                Intent homeIntent = new Intent(SplashScreen.this, LoginActivity.class);
                startActivity(homeIntent);
                finish(); // Menutup aktivitas splash screen agar tidak kembali saat tombol kembali ditekan
            }
        }, SPLASH_TIME_OUT);
    }
}
