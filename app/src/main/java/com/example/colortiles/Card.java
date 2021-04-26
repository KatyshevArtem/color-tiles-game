package com.example.colortiles;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Card {

    Paint p = new Paint();


    public Card(float x, float y, float width, float height, int color) {
        this.color = color;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    int color;
    float x, y, width, height;
    int nextColor = 0;
    List<Integer> colors = Arrays.asList(
            R.color.tileColorR, R.color.tileColorG,
            R.color.tileColorB);


    public boolean changeColor(float touchX, float touchY) {
        if (touchX >= x && touchX <= x + width && touchY >= y && touchY <= y + height) {
            return true;
        } else return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void draw(Canvas c) {
   
        p.setColor(color);
        c.drawRoundRect(x, y, x + width, y + height, 25, 25, p);
    }


}
