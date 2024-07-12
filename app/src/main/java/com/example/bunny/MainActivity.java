package com.example.bunny;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    GameView gameView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Objects.requireNonNull(getSupportActionBar()).hide();
        gameView = new GameView(this);
    }

    public void startGame(View view) {

        setContentView(gameView);
        gameView.resume(this);
    }
    @Override
    public void onPause()
    {
        super.onPause();
      gameView.pause(); // release resources held by the View
    } // end method onPause

}