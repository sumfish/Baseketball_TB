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
    private int BGx; // 背景圖的寬度
    private int BGy; // 背景圖的高度
    private int FGx; // 前景圖的寬度
    private int FGy; // 前景圖的高度
    private int BG_radius; // 背景圖的半徑
    private int FG_radius; // 前景圖的半徑
    private float degree; // 目前的角度(上 = 0度，右 = 90度，下 = 180度，左 = 270度)
    private int BG_VecX,BG_VecY; // 背景圖0度的向量
    private double BG_dist; // 背景圖0度的向量長度
    Bitmap BG_resource; // 背景圖原檔
    Bitmap BG; // Scaled背景圖
    Bitmap FG_resource; // 前景圖原檔
    Bitmap FG; // Scaled前景圖

    // region 讀圖檔
    public CircularControl(Context context) {
        super(context);
        BG_resource = BitmapFactory.decodeResource(context.getResources(),R.drawable.circular_control_base2);
        FG_resource = BitmapFactory.decodeResource(context.getResources(),R.drawable.circular_control_front2);
    }

    public CircularControl(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        BG_resource = BitmapFactory.decodeResource(context.getResources(),R.drawable.circular_control_base2);
        FG_resource = BitmapFactory.decodeResource(context.getResources(),R.drawable.circular_control_front2);
    }

    public CircularControl(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        BG_resource = BitmapFactory.decodeResource(context.getResources(),R.drawable.circular_control_base2);
        FG_resource = BitmapFactory.decodeResource(context.getResources(),R.drawable.circular_control_front2);

    }
    // endregion

    @Override
    protected  void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int Width = getMeasuredWidth(); // layout裡的寬度
        int Height = getMeasuredHeight(); // layout裡的高度

        FG_radius = (Width>Height)? (int)(Height*0.2) : (int)(Width*0.2); // 看寬度跟高度(取小的那個)來調整前景圖的半徑範圍

        BGx = Width/2; // 背景圖的中心點x座標
        BGy = Height/2; // 背景圖的中心點y座標
        BG_radius = (BGx > BGy) ? BGy : BGx; // 看寬度跟高度(取小的那個)來決定背景的半徑範圍

        FGx = BGx; // 前景與背景中心點要一樣
        FGy = BGy; // 前景與背景中心點要一樣

        BG_VecX = 0; // 正上方的X分量為0
        BG_VecY = -BG_radius; // 正上方的Y分量為 (-背景圖半徑) (Android Y 是向下遞增)

        BG_dist = BG_radius; // 向量長度即為背景圖半徑

        degree=0; // 初始度數為0

        BG = Bitmap.createScaledBitmap(BG_resource,BG_radius*2,BG_radius*2,false); // 根據算出的背景半徑畫背景圖
        FG = Bitmap.createScaledBitmap(FG_resource,FG_radius*2,FG_radius*2,false); // 根據算出的前景半徑畫前景圖
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // BG
        canvas.drawBitmap(BG,BGx-BG_radius,BGy-BG_radius,null); // 畫背景圖

        //FG
        Matrix matrix = new Matrix();
        matrix.postRotate(degree,FG.getWidth()/2,FG.getHeight()/2); // 根據degree設定旋轉矩陣
        matrix.postTranslate(canvas.getWidth()/2-FG.getWidth()/2,canvas.getHeight()/2-FG.getHeight()/2); // 設定平移矩陣，調整前景圖位置，確保維持於中心位置
        canvas.drawBitmap(FG,matrix,null); // apply matrix on FG

        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        int tmpx = (int)event.getX(); // 觸控的x座標
        int tmpy = (int)event.getY(); // 觸控的y座標
        double touch_dis = Math.hypot(tmpx-BGx,tmpy-BGy); // 觸控的位置與背景中心的距離

        /*
        // if touch point out of BG
        if (touch_dis > BG_radius){
            tmpx = (int) (BGx+(BG_radius/touch_dis*(tmpx-BGx))); // 讓x座標調整為原觸控位置與中心同條直線上，距離背景中心為背徑半徑的點上
            tmpy = (int) (BGy+(BG_radius/touch_dis*(tmpy-BGy))); // 讓y座標調整為原觸控位置與中心同條直線上，距離背景中心為背徑半徑的點上
        }
        */

        float vecX = tmpx-BGx; //計算觸控點與中心的向量
        float vecY = tmpy-BGy; //計算觸控點與中心的向量
        float dot = vecX * BG_VecX + vecY * BG_VecY; // dot product
        double dist = Math.sqrt(Math.pow(vecX,2)+Math.pow(vecY,2)); // 計算向量的長度
        dist = dist*BG_dist; // 將兩向量長度相乘
        degree = (float)Math.toDegrees(Math.acos(dot/dist)); // 轉換出cos theta的角度
        if(tmpx<BGx){ // 如果超過180度
            degree = 180+(180-degree); // 轉換出正確度數 (否則會從180倒回0，不會繼續加)
        }

        invalidate(); // 更新畫面
        return true;
    }

    public int getFGx(){
        return FGx;
    } // 取得前景中心x座標

    public int getFGy(){
        return FGy;
    } // 取得前景中心y座標

    public int getBGx(){
        return BGx;
    } // 取得背景中心x座標

    public int getBGy(){
        return BGy;
    } // 取得背景中心y座標

    public int getBG_radius(){
        return BG_radius;
    } // 取得背景半徑

    public float get_degree(){return degree;} // 取得旋轉度數
    public void set_degree(int input){degree = input;} //設定旋轉度數

    // Listener events
    public interface OnControlChangeListener{
        public void OnControlChange(CircularControl view);
    }

    public void setOnControlChangeListener(OnControlChangeListener listener) {
        myControlChangeListener = listener;
    }

}
