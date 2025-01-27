package com.example.bunny;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GameView2 extends View {
    Bitmap background, ground, vini;
    Rect rectBackground, rectGround;
    Context context;
    Handler handler;
    final long UPDATE_MILLIS = 20;
    Runnable runnable;
    Paint textPaint = new Paint();
    Paint healthPaint = new Paint();
    float TEXT_SIZE = 60;
    int points = 0;
    int life = 2;
    static int dWidth, dHeight;
    Random random;
    float viniX, viniY;
    float oldX;
    float oldViniX;
    ArrayList<BeeRight> beeRights;
    ArrayList<Potty> pots;
    ArrayList<Explosion> explosions;
    private SoundPool soundPool;
    private static final int MAX_STREAMS = 3;
    private static final int SOUND_QUALITY = 100;
    private int volume; // sound effect volume
    private Map<Integer, Integer> soundMap; // maps ID to soundpool
    private static final int HIT_SOUND_ID = 1;
    private static final int MISS_SOUND_ID = 2;
    private static final int HELP_SOUND_ID = 3;
    private static final int SOUND_PRIORITY = 1;


    public GameView2(Context context) {
        super(context);
        this.context = context;
        background = BitmapFactory.decodeResource(getResources(), R.drawable.backgroundpart2);
        ground = BitmapFactory.decodeResource(getResources(), R.drawable.forest222n);
        vini = BitmapFactory.decodeResource(getResources(), R.drawable.vini);
        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        dWidth = size.x;
        dHeight = size.y;
        rectBackground = new Rect(0, 0, dWidth, dHeight);
        rectGround = new Rect(0, dHeight - ground.getHeight(), dWidth, dHeight);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };
        textPaint.setColor(Color.rgb(100, 165, 250));
        textPaint.setTextSize(TEXT_SIZE);
        textPaint.setTextAlign(Paint.Align.LEFT);
        //textPaint.setTypeface(ResourcesCompat.getFont(context,R.font))
        healthPaint.setColor(Color.CYAN);
        random = new Random();
        viniX = dWidth / 2 - vini.getWidth() / 2;
        viniY = dHeight - ground.getHeight() - vini.getHeight();

        beeRights = new ArrayList<>();
        pots = new ArrayList<>();
        explosions = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            BeeRight beeRight = new BeeRight(context);
            beeRights.add(beeRight);
        }
        for (int i = 0; i < 2; i++) {
            Potty honeyPotty = new Potty(context);
            pots.add(honeyPotty);
        }

        initializeSoundEffects(context);
    }
    void setPoints(Integer pointsLevel1){
        points=pointsLevel1;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(background, null, rectBackground, null);
        canvas.drawBitmap(vini, viniX, viniY, null);


        for (int i = 0; i < beeRights.size(); i++) {
            canvas.drawBitmap(beeRights.get(i).getBee(beeRights.get(i).beeFrame), beeRights.get(i).beeX, beeRights.get(i).beeY, null);
            beeRights.get(i).beeFrame++;
            if (beeRights.get(i).beeFrame > 1) {
                beeRights.get(i).beeFrame = 0;
            }
            beeRights.get(i).beeX += beeRights.get(i).beeVelocity;
            if (beeRights.get(i).beeX + beeRights.get(i).getBeeWidth() >= dWidth) {
                points += 10;
                beeRights.get(i).resetPosition();
            }
        }
        for (int i = 0; i < pots.size(); i++) {
            canvas.drawBitmap(pots.get(i).getPotty(pots.get(i).pottyFrame), pots.get(i).pottyX, pots.get(i).pottyY, null);
            pots.get(i).pottyFrame++;
            if (pots.get(i).pottyFrame > 2) {
                pots.get(i).pottyFrame = 0;
            }
            pots.get(i).pottyY += pots.get(i).pottyVelocity;

        }

        //////////////////////////////////////////////////////////////////////////////////
        for (int i = 0; i < beeRights.size(); i++) {
            if (beeRights.get(i).beeX + beeRights.get(i).getBeeWidth() >= viniX
                    && beeRights.get(i).beeX <= viniX + vini.getWidth()
                    && beeRights.get(i).beeY + beeRights.get(i).getBeeHeight() >= viniY
                    && beeRights.get(i).beeY + beeRights.get(i).getBeeHeight() <= viniY + vini.getHeight()) {
                points -= 10;
                life--;

                beeRights.get(i).resetPosition();

                if (soundPool != null&&life<2) {
                    soundPool.play(HELP_SOUND_ID, volume, volume,
                            SOUND_PRIORITY, 0, 1f);
                }
                if (life <1) {
                    Intent intent = new Intent(context, GameOver.class);
                    intent.putExtra("points", points);
                    context.startActivity(intent);
                    ((Activity) context).finish();
                }
            }
        }
        for (int i = 0; i < pots.size(); i++) {
            if (pots.get(i).pottyX + pots.get(i).getPottyWidth() >= viniX
                    && pots.get(i).pottyX <= viniX + vini.getWidth()
                    && pots.get(i).pottyY + pots.get(i).getPottyHeight() >= viniY
                    && pots.get(i).pottyY + pots.get(i).getPottyHeight() <= viniY + vini.getHeight()) {
                pots.get(i).resetPosition();
                BeeRight beeR = new BeeRight(context);
                beeRights.add(beeR);
                life++;
                points += 100;
                if (soundPool != null&&life%2==0)
                    soundPool.play(MISS_SOUND_ID, volume, volume,
                            SOUND_PRIORITY, 0, 1f);

                pots.get(i).resetPosition();


            }
        }

        if (life >= 5) {
            healthPaint.setColor(Color.GREEN);
        }
        if (life == 4) {
            healthPaint.setColor(Color.MAGENTA);
        }
        if (life == 3) {
            healthPaint.setColor(Color.WHITE);
        }
        if (life == 2) {
            healthPaint.setColor(Color.YELLOW);
        } else if (life == 1) {
            healthPaint.setColor(Color.RED);
        }

        canvas.drawRect(dWidth - 200, 40, dWidth - 200 + 15 * life, 90, healthPaint);
        canvas.drawText("" + points, 100, TEXT_SIZE, textPaint);
        canvas.drawText("" + life, 260, TEXT_SIZE, textPaint);

        handler.postDelayed(runnable, UPDATE_MILLIS);
        if (life >7) {
            Intent intent = new Intent(context, GameOver.class);
            intent.putExtra("points", points);
            context.startActivity(intent);
            ((Activity) context).finish();
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();
        if (touchY >= viniY || touchY <= viniY) {
            int action = event.getAction();

            if (action == MotionEvent.ACTION_DOWN) {

                oldX = event.getX();
                oldViniX = viniX;
            }

            if (action == MotionEvent.ACTION_MOVE) {
                float shift = oldX - touchX;
                float newViniX = oldViniX - shift;
                if (newViniX <= 0)
                    viniX = 0;
                else if (newViniX >= dWidth - vini.getWidth())
                    viniX = dWidth - vini.getWidth();
                else
                    viniX = newViniX;

                viniY = touchY;

            }

            {

            }

        }
        return true;
    }

    private void initializeSoundEffects(Context context) {

        soundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC,
                SOUND_QUALITY);

        AudioManager manager =
                (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        volume = manager.getStreamVolume(AudioManager.STREAM_MUSIC);

        soundMap = new HashMap<Integer, Integer>(); // create new HashMap

        soundMap.put(HIT_SOUND_ID,
                soundPool.load(context, R.raw.yatuchka, SOUND_PRIORITY));
        soundMap.put(MISS_SOUND_ID,
                soundPool.load(context, R.raw.nepravpchelyi, SOUND_PRIORITY));
        soundMap.put(HELP_SOUND_ID,
                soundPool.load(context, R.raw.spasite, SOUND_PRIORITY));



    }

    @Override
    protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld) {
        super.onSizeChanged(xNew, yNew, xOld, yOld);
        if (soundPool != null)
            soundPool.play(HIT_SOUND_ID, volume, volume,
                    SOUND_PRIORITY, 0, 1f);
    }

    public void pause() {
       soundPool.release();
     soundPool = null;

    }

}


