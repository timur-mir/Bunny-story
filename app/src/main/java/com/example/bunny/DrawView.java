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
import android.view.SurfaceView;
import android.view.SurfaceHolder;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class DrawView extends SurfaceView implements SurfaceHolder.Callback {
    private DrawThread drawThread;
    /////////////////////////////////////////
    Bitmap background, background2, background3, ground, vini, stairP,pyatochok;
    Rect rectBackground, rectGround;
    Context context;
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
    float pyatochokX, pyatochokY;
    float stx, sty;
    float oldX;
    float oldViniX;
    ArrayList<Rinvik> rinviks;
    ArrayList<Potty> pots;
    ArrayList<Explosion> explosions;
    private SoundPool soundPool; // plays sound effects
    private static final int MAX_STREAMS = 3;
    private static final int SOUND_QUALITY = 100;
    private int volume; // sound effect volume
    private Map<Integer, Integer> soundMap; // maps ID to soundpool
    private static final int SOVA_SOUND_ID = 1;
    private static final int GLORY_POTTY_SOUND_ID = 2;
    private static final int HELP_SOUND_ID = 3;
    private static final int SOUND_PRIORITY = 1;

    //////////////////////////////////////////
    public DrawView(Context context) {
        super(context);
        this.context=context;
        getHolder().addCallback(this);
        background = BitmapFactory.decodeResource(getResources(), R.drawable.fnew);
        background2 = BitmapFactory.decodeResource(getResources(), R.drawable.forest222n1);
        background3 = BitmapFactory.decodeResource(getResources(), R.drawable.forest222n12);
        ground = BitmapFactory.decodeResource(getResources(), R.drawable.trground);
        vini = BitmapFactory.decodeResource(getResources(), R.drawable.vini);
        pyatochok=BitmapFactory.decodeResource(getResources(), R.drawable.pyatochok2);
        stairP = BitmapFactory.decodeResource(getResources(), R.drawable.st5);
        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        dWidth = size.x;
        dHeight = size.y;
        rectBackground = new Rect(0, 0, dWidth, dHeight);
        rectGround = new Rect(0, dHeight - ground.getHeight(), dWidth, dHeight);
        textPaint.setColor(Color.rgb(100, 165, 250));
        textPaint.setTextSize(TEXT_SIZE);
        textPaint.setTextAlign(Paint.Align.LEFT);
        //textPaint.setTypeface(ResourcesCompat.getFont(context,R.font))
        healthPaint.setColor(Color.CYAN);
        random = new Random();

        viniX = dWidth / 2 - vini.getWidth() / 2;
        viniY = dHeight - ground.getHeight() - vini.getHeight();

//        explosion = new Explosion(context);
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

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        drawThread = new DrawThread(getHolder());
        drawThread.setRunning(true);
        drawThread.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        boolean retry = true;
        drawThread.setRunning(false);
        while (retry) {
            try {
                drawThread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }

    class DrawThread extends Thread {
        private boolean running = false;
        private SurfaceHolder surfaceHolder;

        public DrawThread(SurfaceHolder surfaceHolder) {
            this.surfaceHolder = surfaceHolder;
        }

        public void setRunning(boolean running) {
            this.running = running;
        }

        @Override
        public void run() {
            Canvas canvas;
            while (running) {
                canvas = null;

                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    try {
                    canvas = surfaceHolder.lockCanvas(null);
                    if (canvas == null)
                        continue;
                    if (life < 4 && !openDoor && back < 2) {

                        canvas.drawBitmap(background, null, rectBackground, null);
                        canvas.drawBitmap(ground, null, rectGround, null);

                    } else if (life > 3 && openDoor && back == 2) {

                        canvas.drawBitmap(background3, null, rectBackground, null);
                        canvas.drawBitmap(ground, null, rectGround, null);
                        canvas.drawBitmap(pyatochok, 60, 700, null);
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
                        if (rinviks.get(i).rinvikY + rinviks.get(i).getRinvikheight() >
                                dHeight - ground.getHeight())
                        {
                            Explosion  explosion = new Explosion(context);
                            points += 10;
                            explosion.explosionX = rinviks.get(i).rinvikX;
                            explosion.explosionY = rinviks.get(i).rinvikY;
                            explosions.add(explosion);
                            rinviks.get(i).resetPosition();
                        }
                    }
                    // Взаимодействие бочонков и земли
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
                    // Взаимодействие винни и шишок
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
                                @SuppressLint("DrawAllocation")
                                Intent intent = new Intent(MyApplication.getInstance(), GameOver.class);
                                intent.putExtra("points", points);
                                context.startActivity(intent);
                                ((Activity) context).finish();
                            }
                        }
                    }
                    // Взаимодействие винни и горшочков
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

                    //////////////////////////////////////////

                    for (int i = 0; i < ((explosions.size())); i++) {
                        canvas.drawBitmap(explosions.get(i).getExplosion(explosions.get(i).explosionFrame), explosions.get(i).explosionX,
                                explosions.get(i).explosionY, null);
                        explosions.get(i).explosionFrame++;
                        if (explosions.get(i).explosionFrame > 3) {
                            explosions.remove(i);
                        }

                    }
                    explosions.clear();
                    //////////////////////////////////////////

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
                    coord = "" +viniX;
                    coord2 = "" + viniY;

                    canvas.drawRect(dWidth - 200, 30, dWidth - 200 + 15 * life, 80, healthPaint);
                    canvas.drawText("" + points, 100, TEXT_SIZE, textPaint);
                    canvas.drawText("" + life, 260, TEXT_SIZE, textPaint);
                    // Взаимодействие винни и лестницы
                    if(life>=5) {
                        if (240 + pyatochok.getWidth()>= viniX
                                && 240 <= viniX + vini.getWidth()
                                && 600+ pyatochok.getHeight() >= viniY
                                && 600+ pyatochok.getHeight()  <= viniY + vini.getHeight()) {
                            Intent intent = new Intent(context, Part2.class);
                            intent.putExtra("points", points);
                            context.startActivity(intent);
                            ((Activity) context).finish();
                        }
                    }

                } finally {
                    if (canvas != null) {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
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
}
