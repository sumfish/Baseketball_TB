package com.example.helloworld;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.Vector;

public class PlayerDrawer {
    public int startIndex;
    public int rotation;
    public Paint paint;
    public int curveIndex;
    public Vector<Point> tempCurve;

    public PlayerDrawer(int colorHash){
        startIndex = 0;
        rotation = 0;
        paint = new Paint();
        paint.setAntiAlias(true); // 設置畫筆的鋸齒效果。 true是去除。
        paint.setColor(colorHash);  // 設置顏色
        paint.setAlpha(100);

        curveIndex = 0;
        tempCurve = new Vector<>();
    }

    public void clearRecord(){
        startIndex = 0;
    }

    public void clearCurve(){
        curveIndex = 0;
        tempCurve.clear();
    }
}
