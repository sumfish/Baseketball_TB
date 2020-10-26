package com.mislab.tacticboard;

import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Paint;

import java.util.Vector;

public class DrawCanvas {
    public Canvas canvas;
    private Path path;
    private DashPathEffect DashEffect; //虛線效果
    private DashPathEffect LineEffect; //實線效果

    private static double H = 22; // 箭頭高度
    private static double L = 12; // 箭頭底邊的一半
    private double arrowrad = Math.atan(L / H);
    private double arraow_len = Math.sqrt(L * L + H * H);

    private int ZigzagCount; //用在判斷鋸齒向量要正還是負
    private static float zigDistance=50.0f; //畫鋸齒線時在兩點間每隔多少距離內插一個點
    private static float zigLength=25.0f; //zigzag的大小長度

    public DrawCanvas(){
        this.canvas=new Canvas();
        this.path=new Path();
        this.DashEffect=new DashPathEffect(new float[]{20,20},0);
        this.LineEffect=new DashPathEffect(new float[]{0,0},0);
        this.ZigzagCount=0;
    }

    // calculate path from 3 points
    private void RenderCurvePath(Vector<Point> tempCurvePoint){
        // start from P0
        path.moveTo(tempCurvePoint.get(0).x,tempCurvePoint.get(0).y);
        path.quadTo(tempCurvePoint.get(1).x,tempCurvePoint.get(1).y,tempCurvePoint.get(2).x,tempCurvePoint.get(2).y);
    }

    // draw curve line on the canvas
    public void DrawCurvePath(Vector<Point> tempCurvePoint, Paint painter){
        RenderCurvePath(tempCurvePoint);
        painter.setPathEffect(LineEffect);
        canvas.drawPath(path, painter);
        path.rewind();
    }

    // draw dash line between two points
    public void DrawStraightLine(Point startP, Point endP, Paint painter){
        painter.setPathEffect(DashEffect);
        path.moveTo(startP.x,startP.y);
        path.lineTo(endP.x, endP.y);
        canvas.drawPath(path,painter);
        path.rewind();
    }

    //給線的兩點座標畫箭頭
    public void DrawArrow(Point startP, Point endP, Paint painter){
        double[] arr_1 = rotateVec(endP.x - startP.x, endP.y - startP.y, arrowrad, arraow_len);
        double[] arr_2 = rotateVec(endP.x - startP.x, endP.y - startP.y, -arrowrad, arraow_len);
        double x3=startP.x+arr_1[0];
        double y3=startP.y+arr_1[1];
        double x4=startP.x+arr_2[0];
        double y4=startP.y+arr_2[1];

        path.moveTo(startP.x,startP.y);
        path.lineTo((float)x3, (float)y3);
        path.lineTo((float)x4, (float)y4);
        path.close(); //會形成三角形

        painter.setStyle(Paint.Style.FILL_AND_STROKE);//有設這個三角形才會填滿
        canvas.drawPath(path,painter);
        painter.setStyle(Paint.Style.STROKE);//設回原本的畫筆筆觸
        path.rewind();

    }

    //依照角度計算箭頭的線應該會畫在甚麼座標上
    public double[] rotateVec(int px, int py, double angle, double newlength){
        double vector[] = new double[2];
        //點的旋轉
        double vx = px * Math.cos(angle) - py * Math.sin(angle);
        double vy = px * Math.sin(angle) + py * Math.cos(angle);
        double d = Math.sqrt(vx * vx + vy * vy);
        vx = vx / d * newlength;
        vy = vy / d * newlength;
        vector[0] = vx;
        vector[1] = vy;
        return vector;
    }

    //給一條曲線軌跡畫zigzag鋸齒線
    public void DrawZigzag(Vector<Point> tempCurvePoint, Paint painter){
        Vector<Point>zigzagP=CreateZigzag(interpolation(tempCurvePoint));
        int lgnoreforstraight=2; //留2個點(zigzag&last)不要畫 在最後畫直線

        path.moveTo(zigzagP.get(0).x,zigzagP.get(0).y);
        for (int i=2; i<zigzagP.size()-lgnoreforstraight; i++){ //i=2跳過第一個zigzag點可先形成直線
            path.lineTo(zigzagP.get(i).x,zigzagP.get(i).y);
        }
        //畫最後一點直接連成尾巴直線
        path.lineTo(tempCurvePoint.get(tempCurvePoint.size()-1).x,tempCurvePoint.get(tempCurvePoint.size()-1).y);
        canvas.drawPath(path,painter);
        path.rewind();
    }

    //計算zigzag鋸齒線的點
    public Vector<Point> CreateZigzag(Vector<Point> tempCurvePoint){
        Vector<Point>zigzag=new Vector<>();
        float x1,x2,y1,y2,dx,dy,mx,my,distance;
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

            if(ZigzagCount/2==0){
                mx += (dy/distance*zigLength);
                my -= (dx/distance*zigLength);
            }
            else{
                mx -= (dy/distance*zigLength);
                my += (dx/distance*zigLength);
            }
            zigzag.add(new Point((int)mx,(int)my));
            zigzag.add(tempCurvePoint.get(i));
        }
        return zigzag;
    }

    //兩點之間等距離內插(resample)
    public Vector<Point> interpolation(Vector<Point> CurvePoint){
        Vector<Point>interPoints=new Vector<>();
        float x1,x2,y1,y2,dx,dy;
        double distance;
        int interCount=0;
        int previousCount=1; //點太密 可能會跳過一些點去sample要內插的兩點
        for(int i=0; i<CurvePoint.size(); i++) {
            if (i == 0) {
                interPoints.add(CurvePoint.get(i));
                continue;
            }

            //兩點之間等距內插
            x1 = CurvePoint.get(i - previousCount).x;
            y1 = CurvePoint.get(i - previousCount).y;
            x2 = CurvePoint.get(i).x;
            y2 = CurvePoint.get(i).y;
            dx=x2-x1;
            dy=y2-y1;

            distance=Math.sqrt(dx*dx+dy*dy);
            interCount= (int)(distance/zigDistance)+1;
            if(distance<zigDistance){ //兩點很近不用內插(不會紀錄太近的下一個點)
                previousCount++;
                continue;
            }
            else{
                for (int k=1; k<interCount; k++){ //不算x1，所以k從1開始
                    interPoints.add(new Point((int)(x1+k*dx/interCount),(int)(y1+k*dy/interCount)));
                }
                interPoints.add(CurvePoint.get(i));
                previousCount=1;//變回跟前一點sample
            }
        }
        return interPoints;
    }

    //塗掉User畫傳球線時的曲線筆觸
    public void EraseBallCurvePath(){

    }
}
