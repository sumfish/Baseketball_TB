package com.mislab.tacticboard;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class SurfaceViewBTB extends SurfaceView implements SurfaceHolder.Callback, Runnable{
    private SurfaceHolder mHolder;

    public SurfaceViewBTB(Context context) {
        super(context);
    }
    
    public SurfaceViewBTB(Context context, AttributeSet attrs){
        this(context, attrs, 0);
    }

    public SurfaceViewBTB(Context context, AttributeSet attrs, int i) {
        super(context, attrs,i);
    }

    // surfaceview的生命週期
    @Override
    public void surfaceCreated(SurfaceHolder holder) {


    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    //thread
    @Override
    public void run(){

    }
}
