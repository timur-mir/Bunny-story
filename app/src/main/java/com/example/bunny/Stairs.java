package com.example.bunny;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

public class Stairs {
    Bitmap stairs[] = new Bitmap[1];
    int stairsFrame = 0;
    int stairsX,stairsY,stairsVelocity;
    Random random;
    public Stairs(Context context){
        stairs[0]= BitmapFactory.decodeResource(context.getResources(),R.drawable.st);

        random = new Random();
        resetPosition();

    }
    public Bitmap getRinvik(int stairsFrame) {
        return stairs[stairsFrame];

    }
    public int getStairWidth(){
        return stairs[0].getWidth();

    }
    public int getStairHeight(){
        return stairs[0].getHeight();
    }
    public void resetPosition() {
        stairsX = 120;
        stairsY = 800;
      //  stairsVelocity=5+random.nextInt(16);
    }
}
