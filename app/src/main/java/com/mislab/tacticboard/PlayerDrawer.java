package com.mislab.tacticboard;

import android.graphics.Paint;
import android.graphics.Point;

import java.util.Vector;

public class PlayerDrawer {
    /* 為了方便先都用public, 雖然這樣不好XD*/
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
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10f); //畫筆粗細

        curveIndex = 0;
        tempCurve = new Vector<Point>();

    }

    public void clearRecord(){
        startIndex = 0;
    }

    public void clearCurve(){
        curveIndex = 0;
        tempCurve.clear();
    }
}
