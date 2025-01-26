package com.example.bunny;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import pl.droidsonroids.gif.GifImageView;

public class Part2 extends AppCompatActivity {
    final Handler handler = new Handler();
    private MediaPlayer musPlayer;
    ImageView ivNewHighest;
    GameView2 gameView2 = null;

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.part2);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Objects.requireNonNull(getSupportActionBar()).hide();
        musPlayer = MediaPlayer.create(getApplicationContext(), R.raw.estktonibuddoma);
        musPlayer.start();
        gameView2 = new GameView2(this);
        ivNewHighest = findViewById(R.id.gifImageView);
        int points = getIntent().getExtras().getInt("points");
        gameView2.setPoints(points);
    }

    public void startGame(View view) {
        setContentView(gameView2);
        musPlayer.stop();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        gameView2.pause(); // release resources held by the View
    }


}
