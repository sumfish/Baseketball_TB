package com.mislab.tacticboard;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class IsScreen extends LinearLayout {

    private CircularControl directionControl;
    private ImageButton askIsScreen;
    private TextView title;
    private LinearLayout itself;
    private MainWrap mainwrap;
    private MainFragment mainfrag;
    private Activity activity; //先把context轉型成activity

    public IsScreen(Context context) {
        super(context);
        init(context);
    }

    public IsScreen(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){ //initialization
        View.inflate(context,R.layout.view_is_screen,this);

        askIsScreen = (ImageButton) findViewById(R.id.button_screen);
        askIsScreen.setOnClickListener(ComeOutScreenBar);
        title = (TextView) findViewById(R.id.text_screen);
        itself = findViewById(R.id.layout_screen);
        activity=(Activity) context;
        mainwrap=(MainWrap) activity.getFragmentManager().findFragmentById(R.id.MainWrap_frag); //放路徑元件
        mainfrag=(MainFragment) activity.getFragmentManager().findFragmentById(R.id.Main); //放人物元件

    }

    private OnClickListener ComeOutScreenBar= new OnClickListener() {
        @Override
        public void onClick(View view) { // 跳出控制轉盤
            title.setText("Direction?");
            itself.removeView(askIsScreen);

            //show up the screen bar
            mainwrap.putScreenBar();

            //把原本BUTTON的位置換成轉盤 控制BAR
            directionControl= new CircularControl(getContext());
            LayoutParams circularC= new LayoutParams(120,120);
            circularC.setMargins(10,10,10,1);
            directionControl.setLayoutParams(circularC);
            itself.addView(directionControl);
            directionControl.setOnTouchListener(adjustDirection);

            //加一個確認button
            Button confirm = new Button(getContext());
            LayoutParams confirmC = new LayoutParams(100,70);
            confirm.setText("OK");
            confirmC.gravity=Gravity.CENTER;
            confirmC.bottomMargin=5;
            //confirm.setBackground(getResources().getDrawable(R.drawable.button_background));
            confirm.setLayoutParams(confirmC);
            itself.addView(confirm);
            confirm.setOnClickListener(setBarDirection);

            //讓所有player呈現半透明
            mainfrag.setViewAlpha(100);

        }
    };

    //用轉盤調整screen bar的角度
    private OnTouchListener adjustDirection = new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mainwrap.setScreenBarRotate(directionControl.getDegree());
            return false;
        };
    };

    private OnClickListener setBarDirection=new OnClickListener() {
        @Override
        public void onClick(View view) {
            mainfrag.setViewAlpha(255);
            mainwrap.removeScreenLayout();
            //改變 runbag 裡面最新一動的擋人資訊(pathtype % direction)
            mainfrag.setRunBagScreen(directionControl.getDegree());
        }
    };
}
