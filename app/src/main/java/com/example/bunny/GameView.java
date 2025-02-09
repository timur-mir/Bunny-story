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
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.appbar.AppBarLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GameView extends View {
    Bitmap background, background2, background3, ground, vini, stairP;
    Rect rectBackground, rectGround;
    Context context;
    Boolean gameExit=false;
    Handler handler;
    long UPDATE_MILLIS = 50;
    AudioManager manager;
    Runnable runnable;
    Paint textPaint = new Paint();
    Paint healthPaint = new Paint();
    float TEXT_SIZE = 60;
    int points = 0;
    float posx = 0;
    float posy = 0;
    int life = 1;
    int back = 1;
    boolean openDoor = false;
    static int dWidth, dHeight;
    String coord = "";
    String coord2 = "";
    Random random;
    float viniX, viniY;
    float stx, sty;
    float oldX;
    float oldViniX;
    ArrayList<Rinvik> rinviks;
    ArrayList<Potty> pots;
    //ArrayList<Stairs> stAr;
    ArrayList<Explosion> explosions;
    private SoundPool soundPool; // plays sound effects
    private static final int MAX_STREAMS =3;
    private static final int SOUND_QUALITY = 100;
    private int volume; // sound effect volume
    private Map<Integer, Integer> soundMap; // maps ID to soundpool
    private static final int SOVA_SOUND_ID = 1;
    private static final int GLORY_POTTY_SOUND_ID = 2;
    private static final int HELP_SOUND_ID = 3;
    private static final int SOUND_PRIORITY = 1;


    public GameView(Context context) {
        super(context);
        this.context = context;
        background = BitmapFactory.decodeResource(getResources(), R.drawable.fnew);
        background2 = BitmapFactory.decodeResource(getResources(), R.drawable.forest222n1);
        background3 = BitmapFactory.decodeResource(getResources(), R.drawable.forest222n12);
        ground = BitmapFactory.decodeResource(getResources(), R.drawable.trground);
        vini = BitmapFactory.decodeResource(getResources(), R.drawable.vini);
        stairP = BitmapFactory.decodeResource(getResources(), R.drawable.st5);
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

        rinviks = new ArrayList<>();
        pots = new ArrayList<>();
        explosions = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            Rinvik rinvik = new Rinvik(context);
            rinviks.add(rinvik);
        }
        for (int i = 0; i < 1; i++) {
            Potty honeyPotty = new Potty(context);
            pots.add(honeyPotty);
        }


    }

    public void resume(Context context) {
        //gamePaused = false;
        initializeSoundEffects(context); // initialize app's SoundPool
    } // end method resume

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (life < 4 && !openDoor && back < 2) {

            canvas.drawBitmap(background, null, rectBackground, null);
            canvas.drawBitmap(ground, null, rectGround, null);

        } else if (life > 3 && openDoor && back == 2) {

            canvas.drawBitmap(background3, null, rectBackground, null);
            canvas.drawBitmap(ground, null, rectGround, null);
        }

        canvas.drawBitmap(vini, viniX, viniY, null);

        // Взаимодействие шишек и земли

        for (int i = 0; i < rinviks.size(); i++) {
            canvas.drawBitmap(rinviks.get(i).getRinvik(rinviks.get(i).rinvikFrame), rinviks.get(i).rinvikX, rinviks.get(i).rinvikY, null);
            rinviks.get(i).rinvikFrame++;
            if (rinviks.get(i).rinvikFrame > 2) {
                rinviks.get(i).rinvikFrame = 0;
            }
            rinviks.get(i).rinvikY += rinviks.get(i).rinvikVelocity;
            if (rinviks.get(i).rinvikY + rinviks.get(i).getRinvikheight() >= dHeight - ground.getHeight()) {
                points += 10;
                Explosion explosion = new Explosion(context);
                explosion.explosionX = rinviks.get(i).rinvikX;
                explosion.explosionY = rinviks.get(i).rinvikY;
                explosions.add(explosion);
                rinviks.get(i).resetPosition();
            }
        }
        for (int i = 0; i < pots.size(); i++) {
            canvas.drawBitmap(pots.get(i).getPotty(pots.get(i).pottyFrame), pots.get(i).pottyX, pots.get(i).pottyY, null);
            pots.get(i).pottyFrame++;
            if (pots.get(i).pottyFrame > 2) {
                pots.get(i).pottyFrame = 0;
            }
            pots.get(i).pottyY += pots.get(i).pottyVelocity;
            if (pots.get(i).pottyY + pots.get(i).getPottyHeight() >= dHeight - ground.getHeight()) {
                points += 50;
                pots.get(i).resetPosition();
            }
        }

        //////////////////////////////////////////////////////////////////////////////////
        for (int i = 0; i < rinviks.size(); i++) {
            if (rinviks.get(i).rinvikX + rinviks.get(i).getRinvikWidth() >= viniX
                    && rinviks.get(i).rinvikX <= viniX + vini.getWidth()
                    && rinviks.get(i).rinvikY + rinviks.get(i).getRinvikheight() >= viniY
                    && rinviks.get(i).rinvikY + rinviks.get(i).getRinvikheight() <= viniY + vini.getHeight()) {
                life--;
                if (life < 4) {
                    openDoor = false;
                    back = 1;
                    viniX = dWidth / 2 - vini.getWidth() / 2;
                    viniY = dHeight - ground.getHeight() - vini.getHeight();

                }
                rinviks.get(i).resetPosition();


                if (soundPool != null&&life<1) {
                    soundPool.play(HELP_SOUND_ID, volume, volume,
                            SOUND_PRIORITY, 0, 1f);
                }
                if (life == 0) {
                    @SuppressLint("DrawAllocation") Intent intent = new Intent(context, GameOver.class);
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
                life++;
                Rinvik rinvik = new Rinvik(context);
                rinviks.add(rinvik);
                if (life > 3) {
                    openDoor = true;
                    //break;
                }
                if (soundPool != null)
                    soundPool.play(GLORY_POTTY_SOUND_ID, volume, volume,
                            SOUND_PRIORITY, 0, 1f);

                pots.get(i).resetPosition();


            }
        }

        for (int i = 0; i < explosions.size(); i++) {
            canvas.drawBitmap(explosions.get(i).getExplosion(explosions.get(i).explosionFrame), explosions.get(i).explosionX,
                    explosions.get(i).explosionY, null);
            explosions.get(i).explosionFrame++;
            if (explosions.get(i).explosionFrame > 3) {
                explosions.remove(i);
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
        posx = viniX;
        posy = viniY;
        coord = "" + viniX;
        coord2 = "" + viniY;

        canvas.drawRect(dWidth - 200, 30, dWidth - 200 + 15 * life, 80, healthPaint);
        canvas.drawText("" + points, 100, TEXT_SIZE, textPaint);
        canvas.drawText("" + life, 260, TEXT_SIZE, textPaint);

        handler.postDelayed(runnable, UPDATE_MILLIS);

        if(life>=5) {
            if (130 + stairP.getWidth() >= viniX
                    && 130 <= viniX + vini.getWidth()
                    && 630 + stairP.getHeight() >= viniY
                    && 630 + stairP.getHeight() <= viniY + vini.getHeight()) {
                       destroyDrawingCache();
                       setWillNotDraw(true);
                Intent intent = new Intent(context, Part2.class);
                intent.putExtra("points", points);
                context.startActivity(intent);
                ((Activity) context).finish();
                            }
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
                if (life > 3 && openDoor == true) {
                    back = 2;
                    viniY = touchY;
                }
            }

            {

            }
        }
        return true;
    }



    @Override
    protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld) {
        super.onSizeChanged(xNew, yNew, xOld, yOld);
        soundPool.play(SOVA_SOUND_ID, volume, volume,
                SOUND_PRIORITY, 0, 1f);
    }

    private void initializeSoundEffects(Context context) {
        soundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC,
                SOUND_QUALITY);
        manager =
                (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        volume = manager.getStreamVolume(AudioManager.STREAM_MUSIC);

        soundMap = new HashMap<Integer, Integer>(); // create new HashMap

        soundMap.put(SOVA_SOUND_ID,
                soundPool.load(context, R.raw.sova, SOUND_PRIORITY));
        soundMap.put(GLORY_POTTY_SOUND_ID,
                soundPool.load(context, R.raw.glorypotty, SOUND_PRIORITY));
        soundMap.put(HELP_SOUND_ID,
                soundPool.load(context, R.raw.spasite, SOUND_PRIORITY));
    }
    public void pause() {
        soundPool.release();
        soundPool = null;
    }
}


