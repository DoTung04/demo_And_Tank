package com.example.demo;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    ImageButton btnFire;
    View2Player view2Player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnFire = findViewById(R.id.btnFire);
        view2Player = findViewById(R.id.viewEz);

        btnFire.setOnClickListener(v -> {
            if (view2Player.getTank1().canFire()) {
                view2Player.getTank1().fire(view2Player);
                view2Player.invalidate();
            }
        });

    }
}