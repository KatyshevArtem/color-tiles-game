package com.example.colortiles;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class TilesView extends View {

    final int PAUSE_LENGTH = 1;
    int displayWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    int openedCards = 0;

    float tmpWidth = displayWidth / 5;
    float tmpHeight = displayWidth / 5;
    float tmpX = tmpWidth / 3;
    float tmpY = tmpWidth / 3;

    ArrayList<Card> cards = new ArrayList<>();
    List<Integer> colors;

    boolean isOnPauseNow = false;

    Card firstCard = null;
    int counter = 0;
    int currentIndex;
    int nextColor;

    int redCount;
    int blueCount;
    int greenCount;

    public TilesView(Context context) {
        super(context);
    }

    public TilesView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setColorsAndTiles();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Card c : cards) {
            c.draw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int x = (int) event.getX();
        int y = (int) event.getY();
        //      Log.d("myTag", "onTouchEvent: " + cards.get(3).color);
        if (event.getAction() == MotionEvent.ACTION_DOWN && !isOnPauseNow) {
            for (Card c : cards) {
                if (c.changeColor(x, y)) {

                   //Неоптимально реализован выбор нового цвета 
                    for (int i = 0; i < 3; i++) {
                        if (c.color == colors.get(i)) {
                            nextColor = (i + 1) % 3; 
                        }
                    }
                    currentIndex = cards.indexOf(c);

                    c.color = colors.get(nextColor);
                    redrawLine(currentIndex);
                    invalidate();
                    PauseTask task = new PauseTask();
                    task.execute();
                    return true;


                }
            }


        }


        invalidate();
        return true;
    }

    class PauseTask extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... integers) {

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            firstCard = cards.get(0);
            for (int i = 0; i < cards.size(); i++) {
                if (cards.get(i).color == firstCard.color) counter++;
                if (cards.get(i).color == colors.get(0)) redCount ++;
                if (cards.get(i).color == colors.get(1)) greenCount ++;
                if (cards.get(i).color == colors.get(2)) blueCount ++;
            }
            Log.d("myTag", "onPostExecute: " + counter);
            if (counter == 16) {
                Toast toast = Toast.makeText(getContext(),
                        "Победа!", Toast.LENGTH_SHORT);
                toast.show();
                newGame();
            }
            counter = 0;
        }

    }


    public void setColorsAndTiles() {
        colors = Arrays.asList(
                getResources().getColor(R.color.tileColorR), getResources().getColor(R.color.tileColorG),
                getResources().getColor(R.color.tileColorB));


        Collections.shuffle(colors);
        int cnt = 0;
        for (int i = 0; i < 16; i++) {

            cards.add(new Card(tmpX, tmpY, tmpWidth, tmpHeight, colors.get(cnt)));
            cnt++;
            if (cnt == 3) {
                cnt = 0;
                Collections.shuffle(colors);
            }

            tmpX += tmpWidth + tmpWidth / 10;
            if (((i + 1) % 4) == 0) {
                tmpX = tmpWidth / 3;
                tmpY += tmpWidth + tmpWidth / 10;
            }

        }
        tmpWidth = displayWidth / 5;
        tmpHeight = displayWidth / 5;
        tmpX = tmpWidth / 3;
        tmpY = tmpWidth / 3;
    }


    public void newGame() {

        cards.clear();
        setColorsAndTiles();
        invalidate();
    }

    public void redrawLine(int currentIndex) {
        ArrayList<Integer> nextIndexes = new ArrayList<>();
        int x = currentIndex / 4;
        int y = currentIndex % 4;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (i == x || j == y) {
                    nextIndexes.add(i * 4 + j);
                }
            }
        }
        for (Integer n : nextIndexes) {
            
            for (int i = 0; i < 3; i++) {
                if (cards.get(n).color == colors.get(i)) {
                    nextColor = (i + 1) % 3; 
                }
            }
            cards.get(n).color = colors.get(nextColor);
        }

        nextIndexes.clear();
    }

}
