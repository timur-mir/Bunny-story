package com.example.bunny;

import static java.lang.Math.abs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

public class Potty {
    Bitmap potty[] = new Bitmap[3];
    int pottyFrame = 0;
    int pottyX, pottyY, pottyVelocity;
    Random random;

    public Potty(Context context) {
        potty[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.honey11);
        potty[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.honey12);
        potty[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.honey13);
        random = new Random();
        resetPosition();

    }

    public Bitmap getPotty(int pottyFrame) {
        return potty[pottyFrame];

    }

    public int getPottyWidth() {
        return potty[0].getWidth();

    }

    public int getPottyHeight() {
        return potty[0].getHeight();
    }

    public void resetPosition() {
        if(DrawView.dWidth>0) {
            pottyX = random.nextInt(DrawView.dWidth - getPottyHeight());
            pottyY = -200 + random.nextInt(600) * -1;
            pottyVelocity = 5 + random.nextInt(10);
        }
        else
        {
            pottyX = random.nextInt(GameView2.dWidth - getPottyHeight());
            pottyY = -200 + random.nextInt(600) * -1;
            pottyVelocity = 5 + random.nextInt(10);
        }
    }

}
