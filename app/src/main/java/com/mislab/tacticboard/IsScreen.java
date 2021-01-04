package com.mislab.tacticboard;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class IsScreen extends LinearLayout {

    ImageButton askIsScreen;

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

        this.askIsScreen = (ImageButton) findViewById(R.id.button_screen);
        this.askIsScreen.setOnClickListener(ComeOutScreenBar);
    }

    public void setButtonPosition(int x, int y){
        this.askIsScreen.layout(x,y,x+this.askIsScreen.getWidth(),y+this.askIsScreen.getHeight());
    }
    private OnClickListener ComeOutScreenBar= new OnClickListener() {
        @Override
        public void onClick(View view) {
            askIsScreen.setVisibility(askIsScreen.INVISIBLE);
            Log.d("is screen","show bar");
            //把原本BUTTON的位置換成轉盤 控制BAR
        }
    };
}
