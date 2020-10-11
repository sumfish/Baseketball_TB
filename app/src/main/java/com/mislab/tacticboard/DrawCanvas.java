package com.mislab.tacticboard;

import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Paint;

import java.util.Vector;

public class DrawCanvas {
    public Canvas canvas;
    private Path path;
    public DrawCanvas(){
        this.canvas=new Canvas();
        this.path=new Path();
    }

    // calculate path from 3 points
    private void RenderPath(Vector<Point> tempCurvePoint){
        // start from P0
        path.moveTo(tempCurvePoint.get(0).x,tempCurvePoint.get(0).y);
        path.quadTo(tempCurvePoint.get(1).x,tempCurvePoint.get(1).y,tempCurvePoint.get(2).x,tempCurvePoint.get(2).y);
    }

    // draw line on the canvas
    public void DrawPath(Vector<Point> tempCurvePoint, Paint painter){
        RenderPath(tempCurvePoint);
        canvas.drawPath(path, painter);
    }

}
