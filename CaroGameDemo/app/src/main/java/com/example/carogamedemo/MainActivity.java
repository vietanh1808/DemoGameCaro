package com.example.carogamedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.carogamedemo.views.CustomView;

public class MainActivity extends AppCompatActivity {
    Button btnPvP_Game, btnPvC_Game, btnSetting_Game, btnGuide_Game, btnExit_Game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CustomView customView = new CustomView(this);

        mapping();

        btnPvP_Game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PvPinGame.class));
            }
        });
        btnPvC_Game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PvCinGame.class));
            }
        });
    }

    private void mapping() {
        btnPvP_Game = findViewById(R.id.btnPvP_Game);
        btnPvC_Game = findViewById(R.id.btnPvC_Game);
        btnSetting_Game = findViewById(R.id.btnSetting_Game);
        btnGuide_Game = findViewById(R.id.btnGuide_Game);
        btnExit_Game = findViewById(R.id.btnExit_Game);
    }
}