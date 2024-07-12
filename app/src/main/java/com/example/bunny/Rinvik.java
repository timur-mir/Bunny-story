package com.example.bunny;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

public class Rinvik {
    Bitmap rinvik[] = new Bitmap[3];
    int rinvikFrame = 0;
    int rinvikX,rinvikY,rinvikVelocity;
    Random random;

    public Rinvik(Context context){
        rinvik[0]= BitmapFactory.decodeResource(context.getResources(),R.drawable.rinvik0);
        rinvik[1]= BitmapFactory.decodeResource(context.getResources(),R.drawable.rinvik1);
        rinvik[2]= BitmapFactory.decodeResource(context.getResources(),R.drawable.rinvik2);
        random = new Random();
        resetPosition();

    }
    public Bitmap getRinvik(int rinvikFrame) {
        return rinvik[rinvikFrame];

    }
    public int getRinvikWidth(){
        return rinvik[0].getWidth();

    }
    public int getRinvikheight(){
        return rinvik[0].getHeight();
    }
    public void resetPosition() {
        rinvikX = random.nextInt(GameView.dWidth-getRinvikWidth());
        rinvikY = -200+random.nextInt(600)*-1;
        rinvikVelocity=5+random.nextInt(16);
    }

}
