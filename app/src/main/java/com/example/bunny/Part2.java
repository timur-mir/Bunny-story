package com.example.bunny;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Part2 extends AppCompatActivity {
    private SoundPool soundPool1; // plays sound effects
    private static final int MAX_STREAMS = 5;
    private static final int SOUND_QUALITY = 100;
    private int volume; // sound effect volume
    private Map<Integer, Integer> soundMap1;
    private static final int SOUND_PRIORITY1 = 1;
    private static final int HOUSE_SOUND_ID = 1;
    //    TextView tvPoints;
//    TextView tvHighest;
//    SharedPreferences sharedPreferences;
//    ImageView ivNewHighest;
    GameView2 gameView2 = null;

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.part2);
        initializeSoundEffects(this);

//        soundPool1.play(HOUSE_SOUND_ID, volume, volume,
//                SOUND_PRIORITY1, 0, 1f);


//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        Objects.requireNonNull(getSupportActionBar()).hide();
        gameView2 = new GameView2(this);

    }

    @Override
    public void onStart() {

        super.onStart();
//        soundPool1.play(HOUSE_SOUND_ID, volume, volume,
//                SOUND_PRIORITY1, 0, 1f);
    }

    public void startGame(View view) {

        setContentView(gameView2);
        gameView2.resume(this);
    }

    private void initializeSoundEffects(Context context) {
        // initialize SoundPool to play the app's three sound effects
        soundPool1 = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC,
                SOUND_QUALITY);

        // set sound effect volume
        AudioManager manager1 =
                (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        volume = manager1.getStreamVolume(AudioManager.STREAM_MUSIC);

        // create sound map
        soundMap1 = new HashMap<Integer, Integer>(); // creatЪe new HashMap
        soundMap1.put(HOUSE_SOUND_ID,
                soundPool1.load(context, R.raw.podhodyaschayakompaniya, SOUND_PRIORITY1));
    }

    @Override
    public void onPause() {
        super.onPause();
        gameView2.pause(); // release resources held by the View
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        soundPool1.release(); // release audio resources
        soundPool1 = null;
    }
}
