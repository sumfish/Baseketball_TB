package com.mislab.tacticboard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by MediaCore on 2018/2/12.
 */

public class CircularControl extends View {

    private OnControlChangeListener myControlChangeListener; // 開一個Listener接口
    private int bgWidth; // 背景圖的寬度
    private int bgHeight; // 背景圖的高度
    private int fgWidth; // 前景圖的寬度
    private int fgHeight; // 前景圖的高度
    private int bgRadius; // 背景圖的半徑
    private int fgRadius; // 前景圖的半徑
    private float degree; // 目前的角度(上 = 0度，右 = 90度，下 = 180度，左 = 270度)
    private int bgVecX, bgVecY; // 背景圖0度的向量
    private double bgDist; // 背景圖0度的向量長度
    private Bitmap bgResource; // 背景圖原檔
    private Bitmap bg; // Scaled背景圖
    private Bitmap fgResource; // 前景圖原檔
    private Bitmap fg; // Scaled前景圖

    // region 讀圖檔
    public CircularControl(Context context) {
        super(context);
        bgResource = BitmapFactory.decodeResource(context.getResources(),R.drawable.circular_control_base2);
        fgResource = BitmapFactory.decodeResource(context.getResources(),R.drawable.circular_control_front2);
    }

    public CircularControl(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        bgResource = BitmapFactory.decodeResource(context.getResources(),R.drawable.circular_control_base2);
        fgResource = BitmapFactory.decodeResource(context.getResources(),R.drawable.circular_control_front2);
    }

    public CircularControl(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        bgResource = BitmapFactory.decodeResource(context.getResources(),R.drawable.circular_control_base2);
        fgResource = BitmapFactory.decodeResource(context.getResources(),R.drawable.circular_control_front2);

    }
    // endregion

    @Override
    protected  void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth(); // layout裡的寬度
        int height = getMeasuredHeight(); // layout裡的高度

        fgRadius = (width>height)? (int)(height*0.2) : (int)(width*0.2); // 看寬度跟高度(取小的那個)來調整前景圖的半徑範圍

        bgWidth = width/2; // 背景圖的中心點x座標
        bgHeight = height/2; // 背景圖的中心點y座標
        bgRadius = (bgWidth > bgHeight) ? bgHeight : bgWidth; // 看寬度跟高度(取小的那個)來決定背景的半徑範圍

        fgWidth = bgWidth; // 前景與背景中心點要一樣
        fgHeight = bgHeight; // 前景與背景中心點要一樣

        bgVecX = 0; // 正上方的X分量為0
        bgVecY = -bgRadius; // 正上方的Y分量為 (-背景圖半徑) (Android Y 是向下遞增)

        bgDist = bgRadius; // 向量長度即為背景圖半徑

        degree=0; // 初始度數為0

        bg = Bitmap.createScaledBitmap(bgResource, bgRadius *2, bgRadius *2,false); // 根據算出的背景半徑畫背景圖
        fg = Bitmap.createScaledBitmap(fgResource, fgRadius *2, fgRadius *2,false); // 根據算出的前景半徑畫前景圖
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //background
        canvas.drawBitmap(bg, bgWidth - bgRadius, bgHeight - bgRadius,null); // 畫背景圖

        //foreground
        Matrix matrix = new Matrix();
        matrix.postRotate(degree, fg.getWidth()/2, fg.getHeight()/2); // 根據degree設定旋轉矩陣
        matrix.postTranslate(canvas.getWidth()/2- fg.getWidth()/2,canvas.getHeight()/2- fg.getHeight()/2); // 設定平移矩陣，調整前景圖位置，確保維持於中心位置
        canvas.drawBitmap(fg,matrix,null); // apply matrix on FG

        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        int tempX = (int)event.getX(); // 觸控的x座標
        int tempY = (int)event.getY(); // 觸控的y座標
        double touchDistance = Math.hypot(tempX- bgWidth,tempY- bgHeight); // 觸控的位置與背景中心的距離

        /*
        // if touch point out of BG
        if (touch_dis > BG_radius){
            tmpx = (int) (BGx+(BG_radius/touch_dis*(tmpx-BGx))); // 讓x座標調整為原觸控位置與中心同條直線上，距離背景中心為背徑半徑的點上
            tmpy = (int) (BGy+(BG_radius/touch_dis*(tmpy-BGy))); // 讓y座標調整為原觸控位置與中心同條直線上，距離背景中心為背徑半徑的點上
        }
        */

        float vecX = tempX- bgWidth; //計算觸控點與中心的向量
        float vecY = tempY- bgHeight; //計算觸控點與中心的向量
        float dot = vecX * bgVecX + vecY * bgVecY; // dot product
        double dist = Math.sqrt(Math.pow(vecX,2)+Math.pow(vecY,2)); // 計算向量的長度
        dist = dist * bgDist; // 將兩向量長度相乘
        degree = (float)Math.toDegrees(Math.acos(dot/dist)); // 轉換出cos theta的角度
        if(tempX< bgWidth){ // 如果超過180度
            degree = 180+(180-degree); // 轉換出正確度數 (否則會從180倒回0，不會繼續加)
        }

        invalidate(); // 更新畫面
        return true;
    }

    public int getFgWidth(){
        return fgWidth;
    } // 取得前景中心x座標

    public int getFgHeight(){
        return fgHeight;
    } // 取得前景中心y座標

    public int getBgWidth(){
        return bgWidth;
    } // 取得背景中心x座標

    public int getBgHeight(){
        return bgHeight;
    } // 取得背景中心y座標

    public int getBgRadius(){
        return bgRadius;
    } // 取得背景半徑

    public float getDegree(){return degree;} // 取得旋轉度數
    public void setDegree(int input){degree = input;} //設定旋轉度數

    // Listener events
    public interface OnControlChangeListener{
        public void OnControlChange(CircularControl view);
    }

    public void setOnControlChangeListener(OnControlChangeListener listener) {
        myControlChangeListener = listener;
    }

}
