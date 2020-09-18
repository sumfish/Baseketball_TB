package com.example.helloworld;

import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


public class MultiActivity extends ActionBarActivity{
	ImageView img1, img2,img3,img4,img5,imghcourt,ball;
	int check_court;
	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multi_layout);
        Button button = (Button)findViewById(R.id.button02);
        Button button_half = (Button)findViewById(R.id.half);
        button_half.setTextSize(TypedValue.COMPLEX_UNIT_PX, 20);
        button.setTextSize(TypedValue.COMPLEX_UNIT_PX, 20);
        button.setOnClickListener(btnListener);
        button_half.setOnClickListener(halfListener);
        check_court=0;
        img1 = (ImageView)findViewById(R.id.player1);
        img2 = (ImageView)findViewById(R.id.player2);
        img3 = (ImageView)findViewById(R.id.player3);
        img4 = (ImageView)findViewById(R.id.player4);
        img5 = (ImageView)findViewById(R.id.player5);
        imghcourt = (ImageView)findViewById(R.id.h_court);
        imghcourt.setVisibility(View.GONE);
        ball = (ImageView)findViewById(R.id.ball);
        img1.setOnTouchListener(imgListener);
        img2.setOnTouchListener(imgListener);
        img3.setOnTouchListener(imgListener);
        img4.setOnTouchListener(imgListener);
        img5.setOnTouchListener(imgListener);
        ball.setOnTouchListener(imgListener);
    }
	
	private OnClickListener btnListener = new OnClickListener(){
    	@Override
    	public void onClick(View v){
    		Intent intent = new Intent();
    		intent.setClass(MultiActivity.this,MainActivity.class);
    		startActivity(intent);
    		MultiActivity.this.finish();
    	}
    };
    
	private OnClickListener halfListener = new OnClickListener(){
    	@Override
    	public void onClick(View v){
    		if(check_court==0){
    			RelativeLayout multimain = (RelativeLayout) findViewById(R.id.main);
        		Button court_btn = (Button) findViewById(R.id.half);
        		imghcourt.setVisibility(View.VISIBLE);
        		
        		multimain.setBackgroundResource(R.drawable.black);
    			court_btn.setText("����");
        			
        		check_court=1;
    		}
    		else if (check_court == 1){
    			RelativeLayout multimain = (RelativeLayout) findViewById(R.id.main);
        		Button court_btn = (Button) findViewById(R.id.half);
        		imghcourt = (ImageView)findViewById(R.id.h_court);
        		imghcourt.setVisibility(View.GONE);
        		
        		multimain.setBackgroundResource(R.drawable.court);
    			court_btn.setText("�b��");
    			
    			check_court=0;
    		}
    		
    	}
    };
    
    private OnTouchListener imgListener = new OnTouchListener() {
	    private float x, y;    // �쥻�Ϥ��s�b��X,Y�b��m
	    private int mx, my; // �Ϥ��Q�즲��X ,Y�b�Z������
	    @Override
	    public boolean onTouch(View v, MotionEvent event) {
	          // Log.e("View", v.toString());
	          switch (event.getAction()) {          //�P�_Ĳ�����ʧ@
		          case MotionEvent.ACTION_DOWN:// ���U�Ϥ���
		                x = event.getX();                  //Ĳ����X�b��m
		                y = event.getY();                  //Ĳ����Y�b��m
		                break;
		          case MotionEvent.ACTION_MOVE:// ���ʹϤ���
		               //getX()�G�O�����e����(View)���y��
		               //getRawX()�G�O����۹���ܿù����W�����y��
		               mx = (int) (event.getRawX() - x);
		               my = (int) (event.getRawY() - y); 
		               my = my - 105;
		               v.layout(mx, my, mx + v.getWidth(), my + v.getHeight());
		               break;
	           }
	          return true;
	   }
    };

}
