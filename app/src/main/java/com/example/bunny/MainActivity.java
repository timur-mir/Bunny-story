package com.example.bunny;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    DrawView gameView = null;
    private MediaPlayer musPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Objects.requireNonNull(getSupportActionBar()).hide();
        gameView = new DrawView(this);
        musPlayer = MediaPlayer.create(getApplicationContext(), R.raw.podhodyaschayakompaniya);
        musPlayer.start();
    }

    public void startGame(View view) {
        musPlayer.stop();
        setContentView(gameView);
        gameView.resume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        gameView.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        gameView.pause();
    }
}