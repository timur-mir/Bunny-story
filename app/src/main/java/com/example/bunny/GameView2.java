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
    Bitmap background, background2, ground, vini, stairP;
    Rect rectBackground, rectGround;
    Context context;
    Handler handler;
    final long UPDATE_MILLIS = 20;
    Runnable runnable;
    Paint textPaint = new Paint();
    Paint healthPaint = new Paint();
    float TEXT_SIZE = 60;
    int points = 0;
    float posx = 0;
    float posy = 0;
    int life = 3;
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
    ArrayList<BeeRight> beeRights;
    ArrayList<Potty> pots;
    //ArrayList<Stairs> stAr;
    ArrayList<Explosion> explosions;
    private SoundPool soundPool; // plays sound effects
    private static final int MAX_STREAMS = 5;
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
        //background2 = BitmapFactory.decodeResource(getResources(), R.drawable.forest222n1);
        ground = BitmapFactory.decodeResource(getResources(), R.drawable.forest222n);
        vini = BitmapFactory.decodeResource(getResources(), R.drawable.vini);
      //  stairP = BitmapFactory.decodeResource(getResources(), R.drawable.st5);
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
           BeeRight beeRight= new BeeRight(context);
            beeRights.add(beeRight);
        }
        for (int i = 0; i < 2; i++) {
            Potty honeyPotty = new Potty(context);
            pots.add(honeyPotty);
        }
//        for (int i = 0; i < 1; i++) {
//            Stairs stairEx = new Stairs(context);
//            stAr.add(stairEx);
//        }

    }

    public void resume(Context context) {
        //gamePaused = false;
        initializeSoundEffects(context); // initialize app's SoundPool

//        if (!dialogDisplayed)
//            resetGame(); // start the game
    } // end method resume

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        if((Math.round(viniX)>120&&Math.round(viniX)<235)&&(Math.round(viniY)<850&&Math.round(viniY)>550))


//        {
//            @SuppressLint("DrawAllocation") Intent intent = new Intent(context, Part2.class);
//            intent.putExtra("points", points);
//            context.startActivity(intent);
//            ((Activity) context).finish();
//
//        }
//        if (life < 4 && !openDoor && back < 2) {
          canvas.drawBitmap(background, null, rectBackground, null);
//            canvas.drawBitmap(ground, null, rectGround, null);
//
//        } else if (life > 3 && openDoor && back == 2) {
//            canvas.drawBitmap(background2, null, rectBackground, null);
//            canvas.drawBitmap(ground, null, rectGround, null);
//            canvas.drawBitmap(stairP, 140, 900, null);
//
//        }

//        canvas.drawBitmap(ground, null, rectGround, null);
//        canvas.drawBitmap(stairP,140,900,null);
        canvas.drawBitmap(vini, viniX, viniY, null);
//        if(life==5) {
//            if (140 + stairP.getWidth() >= viniX
//                    && 140 <= viniX + vini.getWidth()
//                    && 900 + stairP.getHeight() >= viniY
//                    && 900 + stairP.getHeight() <= viniY + vini.getHeight()) {
//
//                @SuppressLint("DrawAllocation") Intent intent = new Intent(context, Part2.class);
//                intent.putExtra("points", points);
//                context.startActivity(intent);
//                ((Activity) context).finish();
//            }
//        }

        for (int i = 0; i < beeRights.size(); i++) {
            canvas.drawBitmap(beeRights.get(i).getBee(beeRights.get(i).beeFrame), beeRights.get(i).beeX, beeRights.get(i).beeY, null);
            beeRights.get(i).beeFrame++;
            if (beeRights.get(i).beeFrame> 1) {
                beeRights.get(i).beeFrame= 0;
            }
            beeRights.get(i).beeX+= beeRights.get(i).beeVelocity;
            if (beeRights.get(i).beeX + beeRights.get(i).getBeeWidth() >= dWidth) {
              points += 10;
//                Explosion explosion = new Explosion(context);
//                explosion.explosionX = beeRight.get(i).beer;
//                explosion.explosionY = beeRightstget(i).rinvikY;
//                explosions.add(explosion);
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
            //   pots.get(i).resetPosition();
//            if (pots.get(i).pottyY + pots.get(i).getPottyHeight() >= dHeight - ground.getHeight()) {
//                points += 50;
////                Explosion explosion = new Explosion(context);
////                explosion.explosionX = beeRights.get(i).beer;
////                explosion.explosionY = beeRights.get(i).rinvikY;
////                explosions.add(explosion);
//                pots.get(i).resetPosition();
//            }
        }

        //////////////////////////////////////////////////////////////////////////////////
        for (int i = 0; i < beeRights.size(); i++) {
            if (beeRights.get(i).beeX + beeRights.get(i).getBeeWidth() >= viniX
                    && beeRights.get(i).beeX <= viniX + vini.getWidth()
                    && beeRights.get(i).beeY + beeRights.get(i).getBeeHeight() >= viniY
                    && beeRights.get(i).beeY + beeRights.get(i).getBeeHeight() <= viniY + vini.getHeight()) {
                points -= 10;
                life--;
//                if (life < 4) {
//                    openDoor = false;
//                    back = 1;
//                    viniX = dWidth / 2 - vini.getWidth() / 2;
//                    viniY = dHeight - ground.getHeight() - vini.getHeight();
//
//                }
                beeRights.get(i).resetPosition();
//                if (life==4) {
//                    openDoor=true;
//                    break;
//
//                }
                //  if(life>4) back=1;
                if (soundPool != null) {
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
//        if(openDoor&&life<4){
//            viniX = dWidth / 2 - vini.getWidth() / 2;
//            viniY = dHeight - ground.getHeight() - vini.getHeight();
//            openDoor=false;
//        }
        for (int i = 0; i < pots.size(); i++) {
            if (pots.get(i).pottyX + pots.get(i).getPottyWidth() >= viniX
                    && pots.get(i).pottyX <= viniX + vini.getWidth()
                    && pots.get(i).pottyY + pots.get(i).getPottyHeight() >= viniY
                    && pots.get(i).pottyY + pots.get(i).getPottyHeight() <= viniY + vini.getHeight()) {
                pots.get(i).resetPosition();
               BeeRight beeR= new BeeRight(context);
                beeRights.add(beeR);
                life++;
                points += 100;
//                if (life > 3) {
//                    openDoor = true;
//                    //break;
//
//
//                }
                if (soundPool != null)
                    soundPool.play(MISS_SOUND_ID, volume, volume,
                            SOUND_PRIORITY, 0, 1f);

                pots.get(i).resetPosition();


            }
        }

//        for (int i = 0; i < explosions.size(); i++) {
//            canvas.drawBitmap(explosions.get(i).getExplosion(explosions.get(i).explosionFrame), explosions.get(i).explosionX,
//                    explosions.get(i).explosionY, null);
//            explosions.get(i).explosionFrame++;
//            if (explosions.get(i).explosionFrame > 3) {
//                explosions.remove(i);
//            }
//
//        }
//        viniX = dWidth / 2 - vini.getWidth() / 2;
//        viniY = dHeight - ground.getHeight() - vini.getHeight();
        if (life >= 5) {
            healthPaint.setColor(Color.GREEN);
        }
        if (life ==4 ) {
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
//        posx = viniX;
//        posy = viniY;
//        coord = "" + viniX;
//        coord2 = "" + viniY;

        canvas.drawRect(dWidth - 200, 40, dWidth - 200 + 15 * life, 90, healthPaint);
        canvas.drawText("" + points, 100, TEXT_SIZE, textPaint);
        canvas.drawText("" + life, 260, TEXT_SIZE, textPaint);

        //   canvas.drawText("yrtyry",170,500,textPaint);
//        canvas.drawText(coord, 140, 120, textPaint);
//        canvas.drawText(coord2, 140, 170, textPaint);

        // if((viniX>150&&viniX<170)&&(viniY>495&&viniY<500))


        handler.postDelayed(runnable, UPDATE_MILLIS);
        // canvas.drawBitmap(vini, viniX, viniY, null);
        if(life==10) {
//            if (140 + stairP.getWidth() >= viniX
//                    && 140 <= viniX + vini.getWidth()
//                    && 900 + stairP.getHeight() >= viniY
//                    && 900 + stairP.getHeight() <= viniY + vini.getHeight()) {
//                destroyDrawingCache();
//                // destroyDrawingCache();
//
//                //   canvas.drawBitmap(background, null, rectBackground, null);
                @SuppressLint("DrawAllocation") Intent intent = new Intent(context, MainActivity.class);
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
                if (soundPool != null)
                    soundPool.play(HIT_SOUND_ID, volume, volume,
                            SOUND_PRIORITY, 0, 1f);
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
//                if (life > 3 && openDoor == true) {
//                    back = 2;
//                    //  openDoor=true;
//
//                    viniY = touchY;
//                }
                viniY = touchY;

            }

            {
//                if (action == MotionEvent.ACTION_UP) {
//
//                    if (life > 3 && openDoor == true)
//                    {
//                        back = 2;
//                    //  openDoor=true;
//
//                        viniY = touchY;
//                    }
//                    //  viniY = touchY;
//                                //  viniY = dHeight - ground.getHeight() - vini.getHeight();
//
//                }


            }

        }
        return true;
    }

    private void initializeSoundEffects(Context context) {
        // initialize SoundPool to play the app's three sound effects
        soundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC,
                SOUND_QUALITY);

        // set sound effect volume
        AudioManager manager =
                (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        volume = manager.getStreamVolume(AudioManager.STREAM_MUSIC);

        // create sound map
        soundMap = new HashMap<Integer, Integer>(); // create new HashMap

        // add each sound effect to the SoundPool
        soundMap.put(HIT_SOUND_ID,
                soundPool.load(context, R.raw.sova, SOUND_PRIORITY));
        soundMap.put(MISS_SOUND_ID,
                soundPool.load(context, R.raw.mlrmlr, SOUND_PRIORITY));
        soundMap.put(HELP_SOUND_ID,
                soundPool.load(context, R.raw.spasite, SOUND_PRIORITY));
//        soundMap.put(DISAPPEAR_SOUND_ID,
//                soundPool.load(context, R.raw.hitgull3, SOUND_PRIORITY));
//


    } // end method initializeSoundEffect

    public void pause() {
        soundPool.release(); // release audio resources
        soundPool = null;

    } // en

}


