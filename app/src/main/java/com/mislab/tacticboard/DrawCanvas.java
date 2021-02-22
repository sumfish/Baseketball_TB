package com.mislab.tacticboard;

import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Paint;
import android.util.Log;

import java.util.Stack;
import java.util.Vector;

public class DrawCanvas {
    public Canvas canvas;
    private Path path;

    private DashPathEffect dashEffect; //虛線效果
    private DashPathEffect lineEffect; //實線效果

    // arrow
    double[] arr_1,arr_2; //箭頭的向量
    double x3,y3,x4,y4; //箭頭三角形底部的兩個點
    double vx,vy,d; //依照角度計算箭頭的線應該會畫在甚麼座標上
    private static double H = 22; // 箭頭高度
    private static double L = 12; // 箭頭底邊的一半
    private double arrowAngle = Math.atan(L / H);
    private double arrowLen = Math.sqrt(L * L + H * H);

    //zigzag
    float x1,x2,y1,y2,dx,dy,mx,my,distance;
    private int zigzagCount; //用在判斷鋸齒向量要正還是負
    private static float zigDistance=25.0f; //畫鋸齒線時在兩點間每隔多少距離內插一個點
    private static float zigLength=12.5f; //zigzag的大小長度
    int lgnoreforstraight=4; //留4個點(zigzag&last)不要畫 在最後畫直線
    Vector<Point>zigzag=new Vector<>(); //讓直線變成zigzag(計算要內插的點)
    int previousCount=0;
    int interCount=0;


    public DrawCanvas(){
        this.canvas=new Canvas();
        this.path=new Path();
        this.dashEffect=new DashPathEffect(new float[]{20,20},0);
        this.lineEffect=new DashPathEffect(new float[]{0,0},0);
        this.zigzagCount=0;
        Log.d("canvas","create canvas");
    }

    // calculate path from 3 points
    private void renderCurvePath(Vector<Point> tempCurvePoint){
        // start from P0
        path.moveTo(tempCurvePoint.get(0).x,tempCurvePoint.get(0).y);
        path.quadTo(tempCurvePoint.get(1).x,tempCurvePoint.get(1).y,tempCurvePoint.get(2).x,tempCurvePoint.get(2).y);
    }

    // draw curve line on the canvas
    public void drawCurvePath(Vector<Point> tempCurvePoint, Paint painter, boolean isReDraw){
        renderCurvePath(tempCurvePoint);
        painter.setPathEffect(lineEffect);
        if(!isReDraw){
            painter.setAlpha(255);
        }
        canvas.drawPath(path, painter);
        painter.setAlpha(150);
        path.rewind();
    }

    // draw dash line between two points
    public void drawStraightLine(Point startP, Point endP, Paint painter){
        painter.setPathEffect(dashEffect);
        path.moveTo(startP.x,startP.y);
        path.lineTo(endP.x, endP.y);
        canvas.drawPath(path,painter);
        path.rewind();
    }

    //給線的兩點座標畫箭頭
    public void drawArrow(Point startP, Point endP, Paint painter){
        arr_1 = rotateVec(endP.x - startP.x, endP.y - startP.y, arrowAngle, arrowLen);
        arr_2 = rotateVec(endP.x - startP.x, endP.y - startP.y, -arrowAngle, arrowLen);
        x3=startP.x+arr_1[0];
        y3=startP.y+arr_1[1];
        x4=startP.x+arr_2[0];
        y4=startP.y+arr_2[1];

        path.moveTo(startP.x,startP.y);
        path.lineTo((float)x3, (float)y3);
        path.lineTo((float)x4, (float)y4);
        path.close(); //會形成三角形

        painter.setStyle(Paint.Style.FILL_AND_STROKE);//有設這個三角形才會填滿
        painter.setPathEffect(lineEffect);
        canvas.drawPath(path,painter);
        painter.setStyle(Paint.Style.STROKE);//設回原本的畫筆筆觸
        path.rewind();

    }

    //依照角度計算箭頭的線應該會畫在甚麼座標上
    private double[] rotateVec(int px, int py, double angle, double newLength){
        double vector[] = new double[2];
        //點的旋轉
        vx = px * Math.cos(angle) - py * Math.sin(angle);
        vy = px * Math.sin(angle) + py * Math.cos(angle);
        d = Math.sqrt(vx * vx + vy * vy);
        vx = vx / d * newLength;
        vy = vy / d * newLength;
        vector[0] = vx;
        vector[1] = vy;
        return vector;
    }

    //給一條曲線軌跡畫zigzag鋸齒線
    public void drawZigzag(Vector<Point> tempCurvePoint, Paint painter){
        Vector<Point>zigzagP=createZigzag(interpolation(tempCurvePoint));

        path.moveTo(zigzagP.get(0).x,zigzagP.get(0).y);
        if(zigzagP.size()>4){ //點的數量可以畫zigzag(前五個點0-4會畫直線)
            for (int i=4; i<zigzagP.size()-lgnoreforstraight; i++){ //i=2跳過第一個zigzag點可先形成直線
                path.lineTo(zigzagP.get(i).x,zigzagP.get(i).y);
            }
            //畫最後一點直接連成尾巴直線
            path.lineTo(tempCurvePoint.get(tempCurvePoint.size()-1).x,tempCurvePoint.get(tempCurvePoint.size()-1).y);
            canvas.drawPath(path,painter);
            path.rewind();
        }
    }

    //計算zigzag鋸齒線的點
    private Vector<Point> createZigzag(Vector<Point> tempCurvePoint){
        zigzag.clear();
        for(int i=0; i<tempCurvePoint.size(); i++){
            if(i==0){
                zigzag.add(tempCurvePoint.get(i));
                continue;
            }
            //perpendicular direction 要在兩點的垂直方向取折點座標，所以先算vector (dx,dy) to (dy,-dx)
            x1=tempCurvePoint.get(i-1).x;
            y1=tempCurvePoint.get(i-1).y;
            x2=tempCurvePoint.get(i).x;
            y2=tempCurvePoint.get(i).y;

            mx=(x1+x2)/2;
            my=(y1+y2)/2;
            dx=x2-x1;
            dy=y2-y1;
            distance=(float)Math.sqrt(dx*dx+dy*dy);

            // 一正一負的方向-->可以畫成zigzag
            if(zigzagCount%2==0){
                mx += (dy/distance*zigLength);
                my -= (dx/distance*zigLength);
            } else{
                mx -= (dy/distance*zigLength);
                my += (dx/distance*zigLength);
            }
            zigzag.add(new Point((int)mx,(int)my));
            zigzag.add(tempCurvePoint.get(i));
            zigzagCount++;
        }
        return zigzag;
    }

    //兩點之間等距離內插(resample)
    private Vector<Point> interpolation(Vector<Point> curvePoint){
        Vector<Point>interPoints=new Vector<>();
        interCount=0;
        previousCount=1; //點太密 可能會跳過一些點去sample要內插的兩點
        for(int i=0; i<curvePoint.size(); i++) {
            if (i == 0) {
                interPoints.add(curvePoint.get(i));
                continue;
            }

            //兩點之間等距內插
            x1 = curvePoint.get(i - previousCount).x;
            y1 = curvePoint.get(i - previousCount).y;
            x2 = curvePoint.get(i).x;
            y2 = curvePoint.get(i).y;
            dx=x2-x1;
            dy=y2-y1;

            distance=(float)Math.sqrt(dx*dx+dy*dy);
            interCount= (int)(distance/zigDistance)+1;
            if(distance<zigDistance){ //兩點很近不用內插(不會紀錄太近的下一個點)
                previousCount++;
                continue;
            } else{
                for (int k=1; k<interCount; k++){ //不算x1，所以k從1開始
                    interPoints.add(new Point((int)(x1+k*dx/interCount),(int)(y1+k*dy/interCount)));
                }
                interPoints.add(curvePoint.get(i));
                previousCount=1;//變回跟前一點sample
            }
        }
        return interPoints;
    }

}
