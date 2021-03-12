package com.mislab.tacticboard;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;

public class IsScreen extends LinearLayout {

    private CircularControl directionControl;
    private ImageButton screenYes;
    private ImageButton screenNo;
    private TextView title;
    private LinearLayout itself;
    private LinearLayout screenChoose;
    private MainWrap mainwrap;
    private MainWrapScreen mainscreen;
    private MainFragment mainfrag;
    private ButtonDraw mainButton;
    private Activity activity; //先把context轉型成activity
    private Boolean originalIsTimelineShow=false;

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

        screenChoose= (LinearLayout) findViewById(R.id.screen_choose_layout);
        screenYes = (ImageButton) findViewById(R.id.button_screen);
        screenNo = (ImageButton) findViewById(R.id.button_no_screen);
        screenNo.setOnClickListener(noScreenListener);
        screenYes.setOnClickListener(ComeOutScreenBar);
        title = (TextView) findViewById(R.id.text_screen);
        itself = findViewById(R.id.layout_screen);
        activity=(Activity) context;
        mainwrap=(MainWrap) activity.getFragmentManager().findFragmentById(R.id.MainWrap_frag); //放路徑元件
        mainfrag=(MainFragment) activity.getFragmentManager().findFragmentById(R.id.Main); //放人物元件
        mainButton=(ButtonDraw) activity.getFragmentManager().findFragmentById(R.id.ButtonDraw); //放UI上面的按鈕群
        mainscreen=(MainWrapScreen) activity.getFragmentManager().findFragmentById(R.id.MainWrap_screen); //放screen layout

        // 讓TimeLine Fragment隱藏
        if(mainButton.getisTimelineShow()==true){
            mainButton.getTimeLineView().performClick();
            originalIsTimelineShow=true;
        }

    }

    // 移除ask screen layout
    private OnClickListener noScreenListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            mainscreen.removeScreenLayout();
            // 讓TimeLine Fragment出現
            if(mainButton.getisTimelineShow()==false&&originalIsTimelineShow==true){
                mainButton.getTimeLineView().performClick();
            }

        }
    };

    private OnClickListener ComeOutScreenBar = new OnClickListener() {
        @Override
        public void onClick(View view) { // 跳出控制轉盤
            title.setText("Direction?");
            itself.removeView(screenChoose);

            //show up the screen bar
            mainscreen.passInfo2CreateScreenBar();

            //把原本BUTTON的位置換成轉盤 控制BAR
            directionControl= new CircularControl(getContext());
            LayoutParams circularC= new LayoutParams(170,170);
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

            //讓fragment那邊任意點layout以外的UI都會取消screen的設定消失
            mainfrag.setReactForNotScreenFail();

            //限制user只能按ok 按其他地方(人或球view)都不會有反應 但可以按上面那排按鈕
            mainfrag.disableViewOnTouch();

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
            mainscreen.removeScreenLayout();
            //改變 runbag 裡面最新一動的擋人資訊(pathtype % direction)
            mainfrag.setRunBagScreen(directionControl.getDegree());
            // 讓TimeLine Fragment出現
            if(mainButton.getisTimelineShow()==false&&originalIsTimelineShow==true){
                mainButton.getTimeLineView().performClick();
            }
            //設定view回到listen ontouch
            mainfrag.enableViewOnTouch();
        }
    };
}
