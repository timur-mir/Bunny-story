package com.example.bunny;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

public class BeeRight {
    Bitmap bees[] = new Bitmap[2];
    int beeFrame = 0;
    int beeX,beeY,beeVelocity;
    Random random;

    public BeeRight(Context context){
        bees[0]= BitmapFactory.decodeResource(context.getResources(),R.drawable.bee1);
        bees[1]= BitmapFactory.decodeResource(context.getResources(),R.drawable.bee11);

        random = new Random();
        resetPosition();

    }
    public Bitmap getBee(int beeFrame) {
        return bees[beeFrame];

    }
    public int getBeeWidth(){
        return bees[0].getWidth();

    }
    public int getBeeHeight(){
        return bees[0].getHeight();
    }
    public void resetPosition() {
      //  beeX = random.nextInt(GameView.dWidth-getBeeWidth());
       // beeY = -200+random.nextInt(600)*-1;
        beeY= random.nextInt(GameView2.dHeight-getBeeHeight());
        beeX = random.nextInt(2);
       beeVelocity=4+random.nextInt(12);
    }
}
