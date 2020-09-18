package com.example.helloworld;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
//import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

/*處理球員&球的觸控、儲存路徑，畫出路徑*/

public class MainFragment extends Fragment{

	private int before_query_runbag_length;
	private int numFrame;
	private boolean firstTimeQuery = true;
	private int playing=0;
	private Long startTime;
	private Long endTime;

	private int selectCategoryId = 13;
	private String selectTacticName = "New_Tactic";
	
	private int totalTime = 15000;// 時間軸的最大值，mySeekBar也要改

	private ImageView player1, player2, player3, player4, player5, ball;
	private ImageView arrow1, arrow2, arrow3, arrow4, arrow5;
	private ImageView arrow7, arrow8, arrow9, arrow10, arrow11;
	private ImageView removeButton;
	private ImageView player1Ball, player2Ball, player3Ball, player4Ball, player5Ball;
	
	private ImageView defender1, defender2, defender3, defender4, defender5;

	private boolean firstRecord =true;
	private boolean intersect=false;
	private int intersectName =0;
	private int intersectNamePre =0;
	private int initialBallNum =0;
	private int rotateWhichPlayer;
	//private ImageSelect imageSelect = null;
	private PerspectiveSelect perspectiveSelect = null;
	
	Vector<Integer> pInitialPosition = new Vector();
	Vector<Integer> pInitialRotate = new Vector();

	private int pRotate;
	
	private int initialP1Rotate =-1;
	private int initialP2Rotate =-1;
	private int initialP3Rotate =-1;
	private int initialP4Rotate =-1;
	private int initialP5Rotate =-1;
	
	private int p1Rotate;
	private int p2Rotate;
	private int p3Rotate;
	private int p4Rotate;
	private int p5Rotate;
	private int bRotate;
	
	private int initialD1Rotate =-1;
	private int initialD2Rotate =-1;
	private int initialD3Rotate =-1;
	private int initialD4Rotate =-1;
	private int initialD5Rotate =-1;
	
	private int d1Rotate;
	private int d2Rotate;
	private int d3Rotate;
	private int d4Rotate;
	private int d5Rotate;

	private Vector<Player> players;
	private Player P1 = new Player();
	private Player P2 = new Player();
	private Player P3 = new Player();
	private Player P4 = new Player();
	private Player P5 = new Player();
	private Player B = new Player();
	
	private Player D1 = new Player();
	private Player D2 = new Player();
	private Player D3 = new Player();
	private Player D4 = new Player();
	private Player D5 = new Player();
	
	private Rect rcP1 =new Rect();
	private Rect rcP2 =new Rect();
	private Rect rcP3 =new Rect();
	private Rect rcP4 =new Rect();
	private Rect rcP5 =new Rect();
	private Rect rcBall =new Rect();

	private Player p1Recommend = new Player();
	private Player p2Recommend = new Player();
	private Player p3Recommend = new Player();
	private Player p4Recommend = new Player();
	private Player p5Recommend = new Player();
	private Player bRecommend = new Player();
	
	private int p1InitialPositionX = -1;
	private int p2InitialPositionX = -1;
	private int p3InitialPositionX = -1;
	private int p4InitialPositionX = -1;
	private int p5InitialPositionX = -1;
	private int bInitialPositionX = -1;
	private int p1InitialPositionY = -1;
	private int p2InitialPositionY = -1;
	private int p3InitialPositionY = -1;
	private int p4InitialPositionY = -1;
	private int p5InitialPositionY = -1;
	private int bInitialPositionY = -1;
	
	private int d1InitialPositionX = -1;
	private int d2InitialPositionX = -1;
	private int d3InitialPositionX = -1;
	private int d4InitialPositionX = -1;
	private int d5InitialPositionX = -1;
	private int d1InitialPositionY = -1;
	private int d2InitialPositionY = -1;
	private int d3InitialPositionY = -1;
	private int d4InitialPositionY = -1;
	private int d5InitialPositionY = -1;
	
	private int p1StartIndex = 0;
	private int p2StartIndex = 0;
	private int p3StartIndex = 0;
	private int p4StartIndex = 0;
	private int p5StartIndex = 0;
	private int bStartIndex = 0;
	
	private int d1StartIndex = 0;
	private int d2StartIndex = 0;
	private int d3StartIndex = 0;
	private int d4StartIndex = 0;
	private int d5StartIndex = 0;
	
	private int seekbarTmpId =0;

	private int whichToRemove =-1;
	private boolean hasQueryDefenderFromServer;
	private boolean hasInvokeCurrentTimeDefender;

    /**畫圖變數**/
	private ImageView circle;
	private Path p;
	Vector <Bitmap> bitmapVector;
	Bitmap tempBitmap;
	Canvas tempCanvas;
	private Paint player1Paint;
	private Paint player2Paint;
	private Paint player3Paint;
	private Paint player4Paint;
	private Paint player5Paint;
	private Paint ballPaint;
	private Paint d1Paint;
	private Paint d2Paint;
	private Paint d3Paint;
	private Paint d4Paint;
	private Paint d5Paint;
	private Paint transparentPaint;
	private Paint paint;

	/*********/
    /*************************曲線變數************************************/
    private int N = 3;
	private Vector<Integer> p1TempcurveX = new Vector();
	private Vector<Integer> p1TempcurveY = new Vector();
	private int c1Idx =0;
	private Vector<Integer> p2TempcurveX = new Vector();
	private Vector<Integer> p2TempcurveY = new Vector();
	private int c2Idx =0;
	private Vector<Integer> p3TempcurveX = new Vector();
	private Vector<Integer> p3TempcurveY = new Vector();
	private int c3Idx =0;
	private Vector<Integer> p4TempcurveX = new Vector();
	private Vector<Integer> p4TempcurveY = new Vector();
	private int c4Idx =0;
	private Vector<Integer> p5TempcurveX = new Vector();
	private Vector<Integer> p5TempcurveY = new Vector();
	private int c5Idx =0;
	private Vector<Integer> ballTempcurveX = new Vector();
	private Vector<Integer> ballTempcurveY = new Vector();
	private int ballIdx =0;
	private Vector<Integer> d1TempcurveX = new Vector();
	private Vector<Integer> d1TempcurveY = new Vector();
	private int cd1Idx =0;
	private Vector<Integer> d2TempcurveX = new Vector();
	private Vector<Integer> d2TempcurveY = new Vector();
	private int cd2Idx =0;
	private Vector<Integer> d3TempcurveX = new Vector();
	private Vector<Integer> d3TempcurveY = new Vector();
	private int cd3Idx =0;
	private Vector<Integer> d4TempcurveX = new Vector();
	private Vector<Integer> d4TempcurveY = new Vector();
	private int cd4Idx =0;
	private Vector<Integer> d5TempcurveX = new Vector();
	private Vector<Integer> d5TempcurveY = new Vector();
	private int cd5Idx =0;

	private List<String> currentTimeMaxLen;
	
	Region bitmapRegion;
	
	private Vector<Vector<Float>> curves = new Vector();
	private int rmButtonWidth =60;
	private int rmButtonHeight =60;
	private int rmButtonFlag =0;
	/*private int tmp=0;
	private Vector<Float> P2_curve_x = new Vector();
	private Vector<Float> P2_curve_y = new Vector();
	private Vector<Float> P3_curve_x = new Vector();
	private Vector<Float> P3_curve_y = new Vector();
	private Vector<Float> P4_curve_x = new Vector();
	private Vector<Float> P4_curve_y = new Vector();
	private Vector<Float> P5_curve_x = new Vector();
	private Vector<Float> P5_curve_y = new Vector();
	private Vector<Float> Ball_curve_x = new Vector();
	private Vector<Float> Ball_curve_y = new Vector();*/

    //region 錄製變數
	private boolean recordCheck = false;
	private int seekBarCallbackStartTime;
	private int seekBarCallbackDuration;
	private int seekBarCallbackId;
	private Vector<RunBag> runBags = new Vector();
	private int mainFragSeekBarProgressLow = 0;
	//endregion

    //TODO Socket 變數
	private UE4_Packet dataPacket = new UE4_Packet();
	private InetAddress serverAddr;
	private int UDP_SERVER_PORT = 3985;
	DatagramSocket ds = null;
	AtomicBoolean isRunning=new AtomicBoolean(false);
	/*************************************************************/
	private double EDRThresholdPersent = 15;
	private ToggleButton recommendButton;//Toggle的錄製按鈕
	private int recommendSwitch =0; // 0=DTW , 1=EDR
	private TextView recordResultTextview;
	private TextView EDRPathSize;
	private LinearLayout EDRPathSizeLinearLayout;

	// For screen
	private Vector<Float> previousDirection;
	public boolean isScreenEnable;
	// For dribble
	private Vector<Float> previousDribbleDirection;

	private CallbackInterface mCallback;

	public interface CallbackInterface {
		public void runLineInfo(Vector<RunBag> in_RunLine);

		public void p1Info(Player in_P1);

		public void p2Info(Player in_P2);

		public void p3Info(Player in_P3);

		public void p4Info(Player in_P4);

		public void p5Info(Player in_P5);

		public void bInfo(Player in_B);
		
		public void passSeekbar(String player);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {   
		////Log,i("debug", "onCreateView()............");
		return inflater.inflate(R.layout.main_layout, container,false);	
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {    
		super.onActivityCreated(savedInstanceState);

		selectCategoryId = 0;
		previousDirection = new Vector<Float>();
		previousDribbleDirection = new Vector<Float>();
		isScreenEnable = false;
		hasQueryDefenderFromServer = false;
	/*
		Button EDROK = (Button) getView().findViewById(R.id.EDR_button);
		EDROK.setOnClickListener(new OnClickListener(){//"?]?w"
	    	@Override
	    	public void onClick(View v) {
	    		EditText name=(EditText) getView().findViewById(R.id.EDRT);//???o??Jtext??view
	    		String input_edr=name.getText().toString();
	    		double input_edr_threshold = Double.parseDouble(input_edr);
	    		EDR_Threshold_Persent = input_edr_threshold;
	    		//Log,i("reco", "input threshold = "+input_edr_threshold);
	    		////Log,i("reco", "input EDR_Threshold_Persent = "+EDR_Threshold_Persent);
	    	}
	    });

		EDR_path_size_LinearLayout = (LinearLayout) getView().findViewById(R.id.EDR_path_size_LinearLayout);
		EDR_path_size = (TextView) getView().findViewById(R.id.EDR_path_size);
		recommend_button = (ToggleButton) getView().findViewById(R.id.recommend_button);
		recommend_button.setOnClickListener(recommend_button_Listener);
		reco_result_textview = (TextView) getView().findViewById(R.id.reco_result);*/
		
		player1 = (ImageView) getView().findViewById(R.id.player1);
		player1.setOnTouchListener(player_Listener);// 觸控時監聽
		player2 = (ImageView) getView().findViewById(R.id.player2);
		player2.setOnTouchListener(player_Listener);// 觸控時監聽
		player3 = (ImageView) getView().findViewById(R.id.player3);
		player3.setOnTouchListener(player_Listener);// 觸控時監聽
		player4 = (ImageView) getView().findViewById(R.id.player4);
		player4.setOnTouchListener(player_Listener);// 觸控時監聽
		player5 = (ImageView) getView().findViewById(R.id.player5);
		player5.setOnTouchListener(player_Listener);// 觸控時監聽
		ball = (ImageView) getView().findViewById(R.id.ball);
		ball.setOnTouchListener(player_Listener);// 觸控時監聽
		
		player1Ball = (ImageView) getView().findViewById(R.id.player1_ball);
		player1Ball.setOnTouchListener(player_Listener);
		player2Ball = (ImageView) getView().findViewById(R.id.player2_ball);
		player2Ball.setOnTouchListener(player_Listener);
		player3Ball = (ImageView) getView().findViewById(R.id.player3_ball);
		player3Ball.setOnTouchListener(player_Listener);
		player4Ball = (ImageView) getView().findViewById(R.id.player4_ball);
		player4Ball.setOnTouchListener(player_Listener);
		player5Ball = (ImageView) getView().findViewById(R.id.player5_ball);
		player5Ball.setOnTouchListener(player_Listener);
		
		arrow1 = (ImageView) getView().findViewById(R.id.arrow1);
		arrow2 = (ImageView) getView().findViewById(R.id.arrow2);
		arrow3 = (ImageView) getView().findViewById(R.id.arrow3);
		arrow4 = (ImageView) getView().findViewById(R.id.arrow4);
		arrow5 = (ImageView) getView().findViewById(R.id.arrow5);
		arrow7 = (ImageView) getView().findViewById(R.id.arrow7);
		arrow8 = (ImageView) getView().findViewById(R.id.arrow8);
		arrow9 = (ImageView) getView().findViewById(R.id.arrow9);
		arrow10 = (ImageView) getView().findViewById(R.id.arrow10);
		arrow11 = (ImageView) getView().findViewById(R.id.arrow11);
		removeButton = (ImageView) getView().findViewById(R.id.rm_button);

        // Tag: Where to disable the defenders
		defender1 = (ImageView) getView().findViewById(R.id.defender1);
		defender1.setOnTouchListener(player_Listener);// 觸控時監聽
		defender2 = (ImageView) getView().findViewById(R.id.defender2);
		defender2.setOnTouchListener(player_Listener);// 觸控時監聽
		defender3 = (ImageView) getView().findViewById(R.id.defender3);
		defender3.setOnTouchListener(player_Listener);// 觸控時監聽
		defender4 = (ImageView) getView().findViewById(R.id.defender4);
		defender4.setOnTouchListener(player_Listener);// 觸控時監聽
		defender5 = (ImageView) getView().findViewById(R.id.defender5);
		defender5.setOnTouchListener(player_Listener);// 觸控時監聽


		p=new Path();
		
		paint=new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.BLUE);
		paint.setAlpha(100);
		paint.setStyle(Paint.Style.STROKE);
		
		player1Paint =new Paint();
		player1Paint.setAntiAlias(true); // 設置畫筆的鋸齒效果。 true是去除。
		player1Paint.setColor(Color.parseColor("#133C55")); // 設置顏色
		player1Paint.setAlpha(100);
		
		player2Paint =new Paint();
		player2Paint.setAntiAlias(true);
		player2Paint.setColor(Color.TRANSPARENT);
		player2Paint.setColor(Color.parseColor("#154FB5"));
		player2Paint.setAlpha(100);
		
		player3Paint =new Paint();
		player3Paint.setAntiAlias(true);
		player3Paint.setColor(Color.parseColor("#59A5DB"));
		player3Paint.setAlpha(100);

		player4Paint =new Paint();
		player4Paint.setAntiAlias(true);
		player4Paint.setColor(Color.parseColor("#84D2F6"));
		player4Paint.setAlpha(100);
		
		player5Paint =new Paint();
		player5Paint.setAntiAlias(true);
		player5Paint.setColor(Color.parseColor("#DCFFFD"));
		player5Paint.setAlpha(100);
		
		ballPaint =new Paint();
		ballPaint.setAntiAlias(true);
		ballPaint.setColor(Color.TRANSPARENT);
		ballPaint.setColor(Color.parseColor("#CC0000"));
		ballPaint.setAlpha(100);
		
		d1Paint =new Paint();
		d1Paint.setAntiAlias(true); // 設置畫筆的鋸齒效果。 true是去除。
		d1Paint.setColor(Color.parseColor("#3c1518")); // 設置顏色
		d1Paint.setAlpha(100);
		
		d2Paint =new Paint();
		d2Paint.setAntiAlias(true);
		d2Paint.setColor(Color.TRANSPARENT);
		d2Paint.setColor(Color.parseColor("#69140e"));
		d2Paint.setAlpha(100);
		
		d3Paint =new Paint();
		d3Paint.setAntiAlias(true);
		d3Paint.setColor(Color.parseColor("#a44200"));
		d3Paint.setAlpha(100);

		d4Paint =new Paint();
		d4Paint.setAntiAlias(true);
		d4Paint.setColor(Color.parseColor("#d58936"));
		d4Paint.setAlpha(100);
		
		d5Paint =new Paint();
		d5Paint.setAntiAlias(true);
		d5Paint.setColor(Color.parseColor("#FFC82E"));
		d5Paint.setAlpha(100);
		
		transparentPaint =new Paint();
		transparentPaint.setAntiAlias(true); // 設置畫筆的鋸齒效果。 true是去除。
		transparentPaint.setColor(Color.TRANSPARENT); // 設置透明顏色
		
		bitmapVector = new Vector();
		
		circle=(ImageView) getActivity().findViewById(R.id.circle);
		circle.setOnTouchListener(bitmap_ontouch);

        /*獲取元件的長與寬，並初始化tempBitmap，接著先放一次的透明路徑在circle上*/
        ViewTreeObserver vto2 = circle.getViewTreeObserver();
		   vto2.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {  
		       @Override   
		       public void onGlobalLayout() {  
		    	
		       	tempBitmap = Bitmap.createBitmap(circle.getWidth(),circle.getHeight(),Bitmap.Config.ARGB_8888);//初始化tempBitmap，指定大小為螢幕大小(大小同circle)
		       	bitmapRegion = new Region(circle.getLeft(),circle.getTop(),circle.getRight(),circle.getBottom());
		       	
		       	tempCanvas = new Canvas(tempBitmap);//?e?z????|
		   		tempCanvas.drawBitmap(tempBitmap, 0, 0, null);//畫透明路徑
		   		tempCanvas.drawCircle(1, 1, 5, transparentPaint);//畫透明路徑
		   		circle.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));//把tempBitmap放進circle裡
		        ////Log,i("debug", "tempBitmap's width = "+Integer.toString(tempBitmap.getWidth()));
		   		////Log,i("debug", "tempBitmap's height = "+Integer.toString(tempBitmap.getHeight()));
		        circle.getViewTreeObserver().removeGlobalOnLayoutListener(this);  
		           
		        rotateWhichPlayer =0;
		        p1Rotate =0;
		        p2Rotate =0;
		        p3Rotate =0;
		        p4Rotate =0;
		        p5Rotate =0;
		        bRotate =0;
		        d1Rotate =0;
		        d2Rotate =0;
		        d3Rotate =0;
		        d4Rotate =0;
		        d5Rotate =0;
		        
		        //rm_button.setVisibility(rm_button.VISIBLE);
		        //rm_button.invalidate();
		        removeButton.setVisibility(removeButton.INVISIBLE);
		        removeButton.invalidate();
		        
		        player1Ball.setVisibility(player1Ball.INVISIBLE);
		        player1Ball.invalidate();
		        player2Ball.setVisibility(player2Ball.INVISIBLE);
		        player2Ball.invalidate();
		        player3Ball.setVisibility(player3Ball.INVISIBLE);
		        player3Ball.invalidate();
		        player4Ball.setVisibility(player4Ball.INVISIBLE);
		        player4Ball.invalidate();
		        player5Ball.setVisibility(player5Ball.INVISIBLE);
		        player5Ball.invalidate();
		        /*player1.setImageResource(R.drawable.player1_ball);
		        player1.setImageResource(R.drawable.player1);*/
		        
		       }   
		  });
	}
	
	
	@Override
    public void onStart() { 
        super.onStart();

        /*接收socket的thread*/
	    try {
			serverAddr = InetAddress.getByName("192.168.11.104");

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    Log.i("socket", "start");
	    /*
	    Thread background=new Thread(new Runnable() {
            public void run() {
                try {
                	Log.i("socket", "running");
                	String data;
                	byte[] recevieData = new byte[1024];
                	DatagramPacket dp = new DatagramPacket(recevieData, recevieData.length);
            		ds = new DatagramSocket(3983, InetAddress.getByName("0.0.0.0"));
					System.out.println(InetAddress.getByName("0.0.0.0").isMulticastAddress());
                    for (;isRunning.get();) {
                        Thread.sleep(100);
                        ds.receive(dp);
                        data = new String(recevieData, 0, dp.getLength());
                        Log.i("socket", "msg = "+data);
						serverAddr = InetAddress.getByName(data);

						//imageSelect = (ImageSelect) getActivity().getFragmentManager().findFragmentById(R.id.image_select);
						//imageSelect.Mainfrag_Set_UDP_IP(serverAddr, 2044);
						perspectiveSelect = (PerspectiveSelect)getActivity().getFragmentManager().findFragmentById(R.id.perspecitve_select);
						perspectiveSelect.Mainfrag_Set_UDP_IP(serverAddr, 2044);
					}
                }
                catch (Throwable t) {
                    // just end the background thread
                }
            }
        });

        isRunning.set(true);
        background.start();
        */
    }
	
	public void mainfragSetTotalTime(int inputTime){
		totalTime = inputTime;
	}
	
	public void mainfragSetUDPIP(InetAddress IP, int port){
		serverAddr = IP;
	    UDP_SERVER_PORT = port;
	}

	/********************************Get corresponding defender from the server******************************/
	public void mainfragGetDefenderFromServer() throws JSONException, ExecutionException{
		Log.d("debug", "Call get defender from server.");
		JSONObject jsonWrite = new JSONObject();
		// Ball position and five offensive player
		List<List<Float>> position = new ArrayList<List<Float>>();
		for(int i = 0; i < 6; i++)
			position.add(new ArrayList<Float>());
		// Initialize all the position
		Vector<Float> temp = coordinateTransform(bInitialPositionX, bInitialPositionY);
		position.get(0).add(new Float(temp.get(0)));
		position.get(0).add(new Float(temp.get(1)));
		temp = coordinateTransform(p1InitialPositionX, p1InitialPositionY);
		position.get(1).add(new Float(temp.get(0)));
		position.get(1).add(new Float(temp.get(1)));
		temp = coordinateTransform(p2InitialPositionX, p2InitialPositionY);
		position.get(2).add(new Float(temp.get(0)));
		position.get(2).add(new Float(temp.get(1)));
		temp = coordinateTransform(p3InitialPositionX, p3InitialPositionY);
		position.get(3).add(new Float(temp.get(0)));
		position.get(3).add(new Float(temp.get(1)));
		temp = coordinateTransform(p4InitialPositionX, p4InitialPositionY);
		position.get(4).add(new Float(temp.get(0)));
		position.get(4).add(new Float(temp.get(1)));
		temp = coordinateTransform(p5InitialPositionX, p5InitialPositionY);
		position.get(5).add(new Float(temp.get(0)));
		position.get(5).add(new Float(temp.get(1)));

		int maxTime = 0;
		for(int i=0 ; i<runBags.size() ; i++){
			if(runBags.get(i).getStartTime() > maxTime)
				maxTime = runBags.get(i).getStartTime();
		}

		int t = 0;
		currentTimeMaxLen = new ArrayList<String>();
		currentTimeMaxLen.add("0");
		while(t < maxTime+1){
			for(int i=0;i<runBags.size();i++){
				if(runBags.get(i).getStartTime() == t){
					int id = handle2Id(runBags.get(i).getHandler());
					int roadStart = runBags.get(i).getRoadStart();
					int roadEnd = runBags.get(i).getRoadEnd();
					for(int j=roadStart;j<roadEnd;j=j+2){
						int tempX = 0;
						int tempY = 0;
						if(id == 0){
							tempX = B.handleGetRoad(j);
							tempY = B.handleGetRoad(j+1);
						}
						else if(id == 1){
							tempX = P1.handleGetRoad(j);
							tempY = P1.handleGetRoad(j+1);
						}
						else if(id == 2){
							tempX = P2.handleGetRoad(j);
							tempY = P2.handleGetRoad(j+1);
						}
						else if(id == 3){
							tempX = P3.handleGetRoad(j);
							tempY = P3.handleGetRoad(j+1);
						}
						else if(id == 4){
							tempX = P4.handleGetRoad(j);
							tempY = P4.handleGetRoad(j+1);
						}
						else if(id == 5){
							tempX = P5.handleGetRoad(j);
							tempY = P5.handleGetRoad(j+1);
						}
						Vector<Float> tempPos = coordinateTransform(tempX, tempY);
						position.get(id).add(tempPos.get(0));
						position.get(id).add(tempPos.get(1));
					}
				}
			}
			//Log.d("warning", position.get(0).size() + "," + position.get(1).size() + "," + position.get(2).size() + "," + position.get(3).size() + "," + position.get(4).size() + "," + position.get(5).size());
			int currentMaxLen = getCurrentMaxLength(position);
			currentTimeMaxLen.add(String.valueOf(currentMaxLen));
			// currentMaxLen 長度為 [x, y, x, y,...]的一半
			//Log.d("warning", "Current Max Length: " + currentMaxLen);
			for(int i=0 ; i<6 ; i++){
				if(position.get(i).size() != currentMaxLen*2){
					int diff = currentMaxLen*2 - position.get(i).size();
					for(int j=0 ; j <diff ; j=j+2){
						position.get(i).add(position.get(i).get(position.get(i).size()-2));
						position.get(i).add(position.get(i).get(position.get(i).size()-2));
					}
				}
			}
			t = t+1;
		}
		int totalTimeStep = position.get(0).size();
		//Log.d("warning", "totalTimeStep: " + totalTimeStep);

		//Store the data into json type
		// ["time-step"] ["0"] ["1"] ["2"] ["3"] ["4"] ["5"]
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("time-step", String.valueOf(position.get(0).size()/2));

		for(int i=0 ; i<6 ; i++){
			JSONArray array = new JSONArray();
			for(int s=0 ; s<totalTimeStep ; s=s+2){
				JSONArray xy = new JSONArray();
				xy.put(String.valueOf(position.get(i).get(s)));
				xy.put(String.valueOf(position.get(i).get(s+1)));
				array.put(xy);
			}
			jsonObject.put(String.valueOf(i), array);
		}
		JSONArray array = new JSONArray();
		for(int i=0 ; i<currentTimeMaxLen.size() ; i++)
			array.put(currentTimeMaxLen.get(i));
		jsonObject.put("current_time_max_len", array);

		final String result = jsonObject.toString();
		Log.d("warning", String.valueOf(result));
        new TestAsyncTask(getActivity()).execute(result);
	}

	// Store defender trajectories queried from server
	public void onQueryFinished(String query){
		try {
			/*
			 *   ["1"] = [x,y,x,y,...] ["2"] ["3"] ["4"] ["5"]
			 */
			List<List<Integer>> positions = new ArrayList<List<Integer>>();
			for(int i=0;i<5;i++)
				positions.add(new ArrayList<Integer>());
			D1.clear_all();
			D2.clear_all();
			D3.clear_all();
			D4.clear_all();
			D5.clear_all();

			JSONObject jsonDefender = new JSONObject(new String(query));
			int timeStep = jsonDefender.getJSONArray("1").length();
			for(int i=0;i<5;i++){
				for(int t=0;t<timeStep;t=t+2){
					float tempX = Float.parseFloat(jsonDefender.getJSONArray(String.valueOf(i+1)).get(t).toString());
					float tempY = Float.parseFloat(jsonDefender.getJSONArray(String.valueOf(i+1)).get(t+1).toString());
					//Log.d("warning", tempX + ", " + tempY);
					Vector<Float> tempPos = reverseCoordinateTransform(tempX, tempY);
					positions.get(i).add(Math.round(tempPos.get(0)));
					positions.get(i).add(Math.round(tempPos.get(1)));
					switch(i){
						case 0:
							D1.setRoad(Math.round(tempPos.get(0)));
							D1.setRoad(Math.round(tempPos.get(1)));
							break;
						case 1:
							D2.setRoad(Math.round(tempPos.get(0)));
							D2.setRoad(Math.round(tempPos.get(1)));
							break;
						case 2:
							D3.setRoad(Math.round(tempPos.get(0)));
							D3.setRoad(Math.round(tempPos.get(1)));
							break;
						case 3:
							D4.setRoad(Math.round(tempPos.get(0)));
							D4.setRoad(Math.round(tempPos.get(1)));
							break;
						case 4:
							D5.setRoad(Math.round(tempPos.get(0)));
							D5.setRoad(Math.round(tempPos.get(1)));
							break;
					}
				}
			}
			// Setting the initial position of the defenders
			d1InitialPositionX = positions.get(0).get(0);
			d1InitialPositionY = positions.get(0).get(1);
			d2InitialPositionX = positions.get(1).get(0);
			d2InitialPositionY = positions.get(1).get(1);
			d3InitialPositionX = positions.get(2).get(0);
			d3InitialPositionY = positions.get(2).get(1);
			d4InitialPositionX = positions.get(3).get(0);
			d4InitialPositionY = positions.get(3).get(1);
			d5InitialPositionX = positions.get(4).get(0);
			d5InitialPositionY = positions.get(4).get(1);
			hasQueryDefenderFromServer = true;

			Log.d("warning", String.valueOf(runBags.size()));

		} catch (JSONException e) {
			e.printStackTrace();
		}
		playerMoveToStartPosition();
	}

	public class TestAsyncTask extends AsyncTask<String, Void, String> {
		//This three parameters are Params, Progress, Result
		private Context mContext;
		private ProgressDialog mDialog;
		public TestAsyncTask(Context mContext) {
			this.mContext = mContext;
		}

		protected void onPreExecute() {
			mDialog = new ProgressDialog(mContext);
			mDialog.setMessage("Loading...");
			mDialog.setCancelable(false);
			mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mDialog.show();
		}

		protected String doInBackground(String... input){
			Socket clientSocket = null;
			DataInputStream dataInputStream = null;
			DataOutputStream dataOutputStream = null;

			try {
				clientSocket = new Socket("192.168.60.23", 3985);
				dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
				int dataLength = input[0].length();
				Log.d("warning", String.valueOf(dataLength));
				dataOutputStream.writeBytes(String.valueOf(input[0].length()));
				dataOutputStream.writeBytes(input[0]);
				//Log.d("warning", "dos:" + dataOutputStream.size());

				/*
				dataInputStream = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
				BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				int response_size = Integer.parseInt(br.readLine());
				String response = br.readLine();

				byte[] responseArray = new byte[response_size];
				dataInputStream.readFully(responseArray, 0, response_size);

				return new String(responseArray);
				*/
				BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				String response = br.readLine();
				return new String(response);

			} catch (IOException e) {
				e.printStackTrace();
			}
			return "No";
		}

		protected void onPostExecute(String message) {
			onQueryFinished(message);
			mDialog.dismiss();
		}
	}

	private int handle2Id(String handleStr){
		int id = -1;
		if(handleStr.equals("B_Handle"))
			id = 0;
		else if(handleStr.equals("P1_Handle"))
			id = 1;
		else if(handleStr.equals("P2_Handle"))
			id = 2;
		else if(handleStr.equals("P3_Handle"))
			id = 3;
		else if(handleStr.equals("P4_Handle"))
			id = 4;
		else if(handleStr.equals("P5_Handle"))
			id = 5;
		return id;
	}

	private Vector<Float> coordinateTransform(int x, int y){
		int LEFT_TOP_X = 0;
		int LEFT_TOP_Y = 0;
		int RIGHT_TOP_X = 94;
		int RIGHT_TOP_Y = 0;
		int CENTER_X = 47;
		int CENTER_Y = 25;

		float newX = (y - 760.0f) * (CENTER_X - LEFT_TOP_X)/760.0f + CENTER_X;
		float newY = (x - 540.0f) * (25.0f) / 400.0f + CENTER_Y;

		newX = CENTER_X + (CENTER_X - newX);
		newY = newY;
		Vector<Float> newPos = new Vector<Float>();
		newPos.add(newX);
		newPos.add(newY);
		return newPos;
	}

	private Vector<Float> reverseCoordinateTransform(float x, float y){
		int LEFT_TOP_X = 0;
		int LEFT_TOP_Y = 0;
		int RIGHT_TOP_X = 94;
		int RIGHT_TOP_Y = 0;
		int CENTER_X = 47;
		int CENTER_Y = 25;

		float newX = 540.0f + (y - CENTER_Y) * 400.0f / 25.0f;
		float newY = CENTER_X - (x - CENTER_X);
		newY = (newY - CENTER_X) * 760.0f / (CENTER_X - LEFT_TOP_X) + 760.0f;

		Vector<Float> newPos = new Vector<Float>();
		newPos.add(newX);
		newPos.add(newY);
		return newPos;
	}

	private int getCurrentMaxLength(List<List<Float>> position){
		int max = 0;
		for(int i=0 ; i<6 ; i++){
			if(position.get(i).size()/2.0f > max){
				max = (int) ( (position.get(i).size())/2.0f);
			}
		}
		return max;
	}

	/**********************************************UE4***********************************************/
	public void mainfragSendtoUE4(){

		JSONObject tacticPacket = new JSONObject();
		try {
			JSONArray tmpArray = new JSONArray();
			tmpArray.put(p1InitialPositionX);
			tmpArray.put(p1InitialPositionY);
			tmpArray.put(p2InitialPositionX);
			tmpArray.put(p2InitialPositionY);
			tmpArray.put(p3InitialPositionX);
			tmpArray.put(p3InitialPositionY);
			tmpArray.put(p4InitialPositionX);
			tmpArray.put(p4InitialPositionY);
			tmpArray.put(p5InitialPositionX);
			tmpArray.put(p5InitialPositionY);

			tmpArray.put(d1InitialPositionX);
			tmpArray.put(d1InitialPositionY);
			tmpArray.put(d2InitialPositionX);
			tmpArray.put(d2InitialPositionY);
			tmpArray.put(d3InitialPositionX);
			tmpArray.put(d3InitialPositionY);
			tmpArray.put(d4InitialPositionX);
			tmpArray.put(d4InitialPositionY);
			tmpArray.put(d5InitialPositionX);
			tmpArray.put(d5InitialPositionY);

			tmpArray.put(bInitialPositionX);
			tmpArray.put(bInitialPositionY);

			tacticPacket.put("Initial_Position", tmpArray);

			tacticPacket.put("Initial_ball_holder", initialBallNum);

			//region 2018.09.10 For tactic management
			tacticPacket.put("Category_id", selectCategoryId);
			tacticPacket.put("Tactic_name", selectTacticName);
			//endregion

			tmpArray = new JSONArray();
			for(int i=0;i<B.getRoadSize();i++){
				tmpArray.put( String.valueOf(B.handleGetRoad(i) ));
			}
			if(tmpArray.length() > 0)
				tacticPacket.put("B", tmpArray);

			tmpArray = new JSONArray();
			for(int i=0;i<P1.getRoadSize();i++){
				tmpArray.put( String.valueOf(P1.handleGetRoad(i)));
			}
			if(tmpArray.length() > 0)
				tacticPacket.put("P1", tmpArray);

			tmpArray = new JSONArray();
			for(int i=0;i<P2.getRoadSize();i++){
				tmpArray.put(  String.valueOf(P2.handleGetRoad(i)));
			}
			if(tmpArray.length() > 0)
				tacticPacket.put("P2", tmpArray);

			tmpArray = new JSONArray();
			for(int i=0;i<P3.getRoadSize();i++){
				tmpArray.put(  String.valueOf(P3.handleGetRoad(i)));
			}
			if(tmpArray.length() > 0)
				tacticPacket.put("P3", tmpArray);

			tmpArray = new JSONArray();
			for(int i=0;i<P4.getRoadSize();i++){
				tmpArray.put( String.valueOf(P4.handleGetRoad(i)));
			}
			if(tmpArray.length() > 0)
				tacticPacket.put("P4", tmpArray);

			tmpArray = new JSONArray();
			for(int i=0;i<P5.getRoadSize();i++){
				tmpArray.put(String.valueOf(P5.handleGetRoad(i)));
			}
			if(tmpArray.length() > 0)
				tacticPacket.put("P5", tmpArray);

			tmpArray = new JSONArray();
			for(int i=0;i<D1.getRoadSize();i++){
				tmpArray.put(String.valueOf(D1.handleGetRoad(i)));
			}
			if(tmpArray.length() > 0)
				tacticPacket.put("D1", tmpArray);

			tmpArray = new JSONArray();
			for(int i=0;i<D2.getRoadSize();i++){
				tmpArray.put(String.valueOf(D2.handleGetRoad(i)));
			}
			if(tmpArray.length() > 0)
				tacticPacket.put("D2", tmpArray);

			tmpArray = new JSONArray();
			for(int i=0;i<D3.getRoadSize();i++){
				tmpArray.put(String.valueOf(D3.handleGetRoad(i)));
			}
			if(tmpArray.length() > 0)
				tacticPacket.put("D3", tmpArray);

			tmpArray = new JSONArray();
			for(int i=0;i<D4.getRoadSize();i++){
				tmpArray.put(String.valueOf(D4.handleGetRoad(i)));
			}
			if(tmpArray.length() > 0)
				tacticPacket.put("D4", tmpArray);

			tmpArray = new JSONArray();
			for(int i=0;i<D5.getRoadSize();i++){
				tmpArray.put(String.valueOf(D5.handleGetRoad(i)));
			}
			if(tmpArray.length() > 0)
				tacticPacket.put("D5", tmpArray);

		} catch (JSONException e) {
			e.printStackTrace();
		}

		JSONArray runlineAray = new JSONArray();
		try {
			for(int i = 0; i< runBags.size(); i++){
				JSONObject tmpRunbag = new JSONObject();
				tmpRunbag.put("start_time", String.valueOf(runBags.get(i).getStartTime()));
				tmpRunbag.put("duration", String.valueOf(runBags.get(i).getDuration()));
				tmpRunbag.put("handler", runBags.get(i).getHandler());
				tmpRunbag.put("road_start", String.valueOf(runBags.get(i).getRoadStart()));
				tmpRunbag.put("road_end", String.valueOf(runBags.get(i).getRoadEnd()));
				tmpRunbag.put("rate", String.valueOf(runBags.get(i).getRate()));
				tmpRunbag.put("ball_num", String.valueOf(runBags.get(i).getBallNum()));

				tmpRunbag.put("path_type", String.valueOf(runBags.get(i).getPathType()));
				tmpRunbag.put("screen_angle", String.valueOf(runBags.get(i).getPathType()));
				tmpRunbag.put("dribble_angle", String.valueOf(runBags.get(i).getDribbleAngle()));
				tmpRunbag.put("dribble_length", String.valueOf(runBags.get(i).getDribbleLength()));

				runlineAray.put(tmpRunbag);
			}
			tacticPacket.put("RunLine", runlineAray);


		} catch (JSONException e) {
			e.printStackTrace();
		}

		JSONObject jsonTest = new JSONObject();
		try {
			jsonTest.put("RunLine", "123");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		final String tacticString = tacticPacket.toString();

		Thread socketThread = new Thread(new Runnable() {
			@Override
			public void run() {
				Socket clientSocket = null;
				DataOutputStream dataOutputStream = null;
				try {

					Log.d("debug", serverAddr.getHostAddress());
					clientSocket = new Socket(serverAddr.getHostAddress(), 2222);
					dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
					int data_len = tacticString.length();
					//byte[] arr = new byte[] {
					//		(byte) ((data_len >> 24) & 0xFF),
					//		(byte) ((data_len >> 16) & 0xFF),
					//		(byte) ((data_len >> 8) & 0xFF),
					//		(byte) (data_len & 0xFF)};
					//dataOutputStream.write( arr);
					dataOutputStream.write(tacticString.getBytes());
					clientSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		socketThread.start();

		Log.d("debug", "Send to game server!");

	}

	//region 原本socket packet的封包格式
	/*
	private ByteBuffer putPacketIntoSocket(ByteBuffer bBuffer){
		bBuffer.order(ByteOrder.LITTLE_ENDIAN);
		int position=0;
		//P1_Road_size ~ B_Road_size && RunBag_size
		bBuffer.putInt(dataPacket.get_P1_Road_size());
		//Log,i("socket", "P1_Road_size  = "+Integer.toString(dataPacket.get_P1_Road_size()));
		position+=4;
		bBuffer.position(position);
		
		bBuffer.putInt(dataPacket.get_P2_Road_size());
		//Log,i("socket", "P2_Road_size  = "+Integer.toString(dataPacket.get_P2_Road_size()));
		position+=4;
		bBuffer.position(position);
		
		bBuffer.putInt(dataPacket.get_P3_Road_size());
		//Log,i("socket", "P3_Road_size  = "+Integer.toString(dataPacket.get_P3_Road_size()));
		position+=4;
		bBuffer.position(position);
		
		bBuffer.putInt(dataPacket.get_P4_Road_size());
		//Log,i("socket", "P4_Road_size  = "+Integer.toString(dataPacket.get_P4_Road_size()));
		position+=4;
		bBuffer.position(position);
		
		bBuffer.putInt(dataPacket.get_P5_Road_size());
		//Log,i("socket", "P5_Road_size  = "+Integer.toString(dataPacket.get_P5_Road_size()));
		position+=4;
		bBuffer.position(position);
		
		bBuffer.putInt(dataPacket.get_D1_Road_size());
		//Log,i("socket", "D1_Road_size  = "+Integer.toString(dataPacket.get_D1_Road_size()));
		position+=4;
		bBuffer.position(position);
		
		bBuffer.putInt(dataPacket.get_D2_Road_size());
		//Log,i("socket", "D2_Road_size  = "+Integer.toString(dataPacket.get_D2_Road_size()));
		position+=4;
		bBuffer.position(position);
		
		bBuffer.putInt(dataPacket.get_D3_Road_size());
		//Log,i("socket", "D3_Road_size  = "+Integer.toString(dataPacket.get_D3_Road_size()));
		position+=4;
		bBuffer.position(position);
		
		bBuffer.putInt(dataPacket.get_D4_Road_size());
		//Log,i("socket", "D4_Road_size  = "+Integer.toString(dataPacket.get_D4_Road_size()));
		position+=4;
		bBuffer.position(position);
		
		bBuffer.putInt(dataPacket.get_D5_Road_size());
		//Log,i("socket", "D5_Road_size  = "+Integer.toString(dataPacket.get_D5_Road_size()));
		position+=4;
		bBuffer.position(position);
		
		
		bBuffer.putInt(dataPacket.get_B_Road_size());
		//Log,i("socket", "B_Road_size  = "+Integer.toString(dataPacket.get_B_Road_size()));
		position+=4;
		bBuffer.position(position);
		//Rotate size
		
		bBuffer.putInt(dataPacket.get_P1_Rotate_size());
		//Log,i("socket", "P1_Rotate_size  = "+Integer.toString(dataPacket.get_P1_Rotate_size()));
		position+=4;
		bBuffer.position(position);
		bBuffer.putInt(dataPacket.get_P2_Rotate_size());
		//Log,i("socket", "P2_Rotate_size  = "+Integer.toString(dataPacket.get_P2_Rotate_size()));
		position+=4;
		bBuffer.position(position);
		bBuffer.putInt(dataPacket.get_P3_Rotate_size());
		//Log,i("socket", "P3_Rotate_size  = "+Integer.toString(dataPacket.get_P3_Rotate_size()));
		position+=4;
		bBuffer.position(position);
		bBuffer.putInt(dataPacket.get_P4_Rotate_size());
		//Log,i("socket", "P4_Rotate_size  = "+Integer.toString(dataPacket.get_P4_Rotate_size()));
		position+=4;
		bBuffer.position(position);
		bBuffer.putInt(dataPacket.get_P5_Rotate_size());
		//Log,i("socket", "P5_Rotate_size  = "+Integer.toString(dataPacket.get_P5_Rotate_size()));
		position+=4;
		bBuffer.position(position);
		
		bBuffer.putInt(dataPacket.get_D1_Rotate_size());
		//Log,i("socket", "D1_Rotate_size  = "+Integer.toString(dataPacket.get_D1_Rotate_size()));
		position+=4;
		bBuffer.position(position);
		bBuffer.putInt(dataPacket.get_D2_Rotate_size());
		//Log,i("socket", "D2_Rotate_size  = "+Integer.toString(dataPacket.get_D2_Rotate_size()));
		position+=4;
		bBuffer.position(position);
		bBuffer.putInt(dataPacket.get_D3_Rotate_size());
		//Log,i("socket", "D3_Rotate_size  = "+Integer.toString(dataPacket.get_D3_Rotate_size()));
		position+=4;
		bBuffer.position(position);
		bBuffer.putInt(dataPacket.get_D4_Rotate_size());
		//Log,i("socket", "D4_Rotate_size  = "+Integer.toString(dataPacket.get_D4_Rotate_size()));
		position+=4;
		bBuffer.position(position);
		bBuffer.putInt(dataPacket.get_D5_Rotate_size());
		//Log,i("socket", "D5_Rotate_size  = "+Integer.toString(dataPacket.get_D5_Rotate_size()));
		position+=4;
		bBuffer.position(position);
		

		bBuffer.putInt(dataPacket.get_RunBag_size());
		//Log,i("socket", "RunBag_size  = "+Integer.toString(dataPacket.get_RunBag_size()));
		position+=4;
		bBuffer.position(position);
		
		bBuffer.putInt(Total_time/1000);
		//Log,i("socket", "Total_time  = "+Integer.toString(Total_time/1000));
		position+=4;
		bBuffer.position(position);
		
		
		
		//P_Initial_Postion
		for(int i = 0 ; i<dataPacket.get_P_Initial_Position_size();i++){
			bBuffer.putInt(dataPacket.get_P_Initial_Position().get(i));
			position+=4;
			bBuffer.position(position);
			//Log,i("socket", "P_Initial_Position("+Integer.toString(i)+")="+Integer.toString(dataPacket.get_P_Initial_Position().get(i)));
			//Log,i("socket", "buffer position  = "+Integer.toString(bBuffer.position()));
		}
		//P_Initial_Rotate
		for(int i = 0 ; i<dataPacket.get_P_Initial_rotate_size();i++){
			bBuffer.putInt(dataPacket.get_P_Initial_rotate().get(i));
			position+=4;
			bBuffer.position(position);
			//Log,i("socket", "buffer position  = "+Integer.toString(bBuffer.position()));
		}
		//initial_ball_num
		bBuffer.putInt(dataPacket.get_initial_ball_num());
		//Log,i("socket", "initial_ball_num  = "+Integer.toString(dataPacket.get_initial_ball_num()));
		position+=4;
		bBuffer.position(position);
		
		
		//P1_Road
		for(int i = 0 ; i<dataPacket.get_P1_Road_size();i++){
			bBuffer.putInt(dataPacket.get_P1_Road().get(i));
			position+=4;
			bBuffer.position(position);
		}
		for(int i = 0 ; i<dataPacket.get_P2_Road_size();i++){
			bBuffer.putInt(dataPacket.get_P2_Road().get(i));
			position+=4;
			bBuffer.position(position);
		}
		for(int i = 0 ; i<dataPacket.get_P3_Road_size();i++){
			bBuffer.putInt(dataPacket.get_P3_Road().get(i));
			position+=4;
			bBuffer.position(position);
		}
		for(int i = 0 ; i<dataPacket.get_P4_Road_size();i++){
			bBuffer.putInt(dataPacket.get_P4_Road().get(i));
			position+=4;
			bBuffer.position(position);
		}
		for(int i = 0 ; i<dataPacket.get_P5_Road_size();i++){
			bBuffer.putInt(dataPacket.get_P5_Road().get(i));
			position+=4;
			bBuffer.position(position);
		}
		
		for(int i = 0 ; i<dataPacket.get_D1_Road_size();i++){
			bBuffer.putInt(dataPacket.get_D1_Road().get(i));
			position+=4;
			bBuffer.position(position);
		}
		for(int i = 0 ; i<dataPacket.get_D2_Road_size();i++){
			bBuffer.putInt(dataPacket.get_D2_Road().get(i));
			position+=4;
			bBuffer.position(position);
		}
		for(int i = 0 ; i<dataPacket.get_D3_Road_size();i++){
			bBuffer.putInt(dataPacket.get_D3_Road().get(i));
			position+=4;
			bBuffer.position(position);
		}
		for(int i = 0 ; i<dataPacket.get_D4_Road_size();i++){
			bBuffer.putInt(dataPacket.get_D4_Road().get(i));
			position+=4;
			bBuffer.position(position);
		}
		for(int i = 0 ; i<dataPacket.get_D5_Road_size();i++){
			bBuffer.putInt(dataPacket.get_D5_Road().get(i));
			position+=4;
			bBuffer.position(position);
		}
		
		
		for(int i = 0 ; i<dataPacket.get_B_Road_size();i++){
			bBuffer.putInt(dataPacket.get_B_Road().get(i));
			position+=4;
			bBuffer.position(position);
		}
		
		
		//P1_Rotate
		for(int i = 0 ; i<dataPacket.get_P1_Rotate_size();i++){
			bBuffer.putInt(dataPacket.get_P1_Rotate().get(i));
			position+=4;
			bBuffer.position(position);
		}
		for(int i = 0 ; i<dataPacket.get_P2_Rotate_size();i++){
			bBuffer.putInt(dataPacket.get_P2_Rotate().get(i));
			position+=4;
			bBuffer.position(position);
		}
		for(int i = 0 ; i<dataPacket.get_P3_Rotate_size();i++){
			bBuffer.putInt(dataPacket.get_P3_Rotate().get(i));
			position+=4;
			bBuffer.position(position);
		}
		for(int i = 0 ; i<dataPacket.get_P4_Rotate_size();i++){
			bBuffer.putInt(dataPacket.get_P4_Rotate().get(i));
			position+=4;
			bBuffer.position(position);
		}
		for(int i = 0 ; i<dataPacket.get_P5_Rotate_size();i++){
			bBuffer.putInt(dataPacket.get_P5_Rotate().get(i));
			if(bBuffer.position()+4<bBuffer.capacity()){
				position+=4;
				bBuffer.position(position);
			}
			//Log,i("socket", "P5_Rotate("+Integer.toString(i)+")="+Integer.toString(dataPacket.get_P5_Rotate().get(i)));
		}
		
		for(int i = 0 ; i<dataPacket.get_D1_Rotate_size();i++){
			bBuffer.putInt(dataPacket.get_D1_Rotate().get(i));
			position+=4;
			bBuffer.position(position);
		}
		for(int i = 0 ; i<dataPacket.get_D2_Rotate_size();i++){
			bBuffer.putInt(dataPacket.get_D2_Rotate().get(i));
			position+=4;
			bBuffer.position(position);
		}
		for(int i = 0 ; i<dataPacket.get_D3_Rotate_size();i++){
			bBuffer.putInt(dataPacket.get_D3_Rotate().get(i));
			position+=4;
			bBuffer.position(position);
		}
		for(int i = 0 ; i<dataPacket.get_D4_Rotate_size();i++){
			bBuffer.putInt(dataPacket.get_D4_Rotate().get(i));
			position+=4;
			bBuffer.position(position);
		}
		for(int i = 0 ; i<dataPacket.get_D5_Rotate_size();i++){
			bBuffer.putInt(dataPacket.get_D5_Rotate().get(i));
			if(bBuffer.position()+4<bBuffer.capacity()){
				position+=4;
				bBuffer.position(position);
			}
			//Log,i("socket", "D5_Rotate("+Integer.toString(i)+")="+Integer.toString(dataPacket.get_D5_Rotate().get(i)));
		}
		
		//Log,i("socket", "P5_rotate_size="+Integer.toString(P5.getCmpltRotate().size()));
		//RunBag
		Vector<Integer> tmpRunBag=new Vector();
		
		for(int i = 0 ; i<RunLine.size();i++){
			tmpRunBag=RunLine.get(i).parseRunBagToIntVec();
			//Log,i("socket", "------");
			for(int j=0;j<tmpRunBag.size();j++){
				
				bBuffer.putInt(tmpRunBag.get(j));
				//Log,i("socket", "tmpRunBag  = "+Integer.toString(tmpRunBag.get(j)));
				position+=4;
				bBuffer.position(position);
			}
		}
		return bBuffer;
	}

	*/
//endregion

	@Override
	public void onStop() {
        super.onStop();
        isRunning.set(false);
    }
	
	@Override
	public void onResume() {
		//Log.d("debug", "MainFragment onResume!");
		super.onResume();
		
	}

	@Override
	public void onPause() {
		//Log.d("debug", "MainFragment onResume!");
		super.onPause();
	}

	public void onAttach(Activity activity) {
		//Log.d("debug", "MainF onAttach!");
		super.onAttach(activity);

		try {
			mCallback = (CallbackInterface) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + "must implement MainFragment.CallbackInterface!");
		}
	}
 
	public void setPlayerToNoBall(){
		if(intersectName !=0){
			switch(intersectName){
			case 1:
				player1.setVisibility(player1.VISIBLE);
				if(intersect==true){
					////Log,i("debug","intersect=true,player1 should be layout on player_ball's position");
					player1.layout((int) player1Ball.getX()+30, (int) player1Ball.getY(),(int) player1Ball.getX()+30+player1.getWidth(), (int) player1Ball.getY()+player1.getHeight());
				}
				player1.invalidate();
				player1Ball.setVisibility(player1Ball.INVISIBLE);
				player1Ball.invalidate();
				break;
			case 2:
				player2.setVisibility(player2.VISIBLE);
				if(intersect==true){
					////Log,i("debug","intersect=true,player1 should be layout on player_ball's position");
					player2.layout((int) player2Ball.getX()+30, (int) player2Ball.getY(),(int) player2Ball.getX()+30+player2.getWidth(), (int) player2Ball.getY()+player2.getHeight());
				}
				player2.invalidate();
				player2Ball.setVisibility(player2Ball.INVISIBLE);
				player2Ball.invalidate();
				break;
			case 3:
				player3.setVisibility(player3.VISIBLE);
				if(intersect==true){
					////Log,i("debug","intersect=true,player1 should be layout on player_ball's position");
					player3.layout((int) player3Ball.getX()+30, (int) player3Ball.getY(),(int) player3Ball.getX()+30+player3.getWidth(), (int) player3Ball.getY()+player3.getHeight());
				}
				player3.invalidate();
				player3Ball.setVisibility(player3Ball.INVISIBLE);
				player3Ball.invalidate();
				break;
			case 4:
				player4.setVisibility(player4.VISIBLE);
				if(intersect==true){
					////Log,i("debug","intersect=true,player1 should be layout on player_ball's position");
					player4.layout((int) player4Ball.getX()+30, (int) player4Ball.getY(),(int) player4Ball.getX()+30+player4.getWidth(), (int) player4Ball.getY()+player4.getHeight());
				}
				player4.invalidate();
				player4Ball.setVisibility(player4Ball.INVISIBLE);
				player4Ball.invalidate();
				break;
			case 5:
				player5.setVisibility(player5.VISIBLE);
				if(intersect==true){
					////Log,i("debug","intersect=true,player1 should be layout on player_ball's position");
					player5.layout((int) player5Ball.getX()+30, (int) player5Ball.getY(),(int) player5Ball.getX()+30+player5.getWidth(), (int) player5Ball.getY()+player5.getHeight());
				}
				player5.invalidate();
				player5Ball.setVisibility(player5Ball.INVISIBLE);
				player5Ball.invalidate();
				break;
			}
		}
		if(player1.getVisibility()==player1.VISIBLE){
			arrow1.layout((int)player1.getX(), (int)player1.getY(), (int)player1.getX()+player1.getWidth(), (int)player1.getY()+player1.getHeight());
		}
		arrow1.invalidate();
		if(player2.getVisibility()==player2.VISIBLE){
			arrow2.layout((int)player2.getX(), (int)player2.getY(), (int)player2.getX()+player2.getWidth(), (int)player2.getY()+player2.getHeight());
		}
		arrow2.invalidate();
		if(player3.getVisibility()==player3.VISIBLE){
			arrow3.layout((int)player3.getX(), (int)player3.getY(), (int)player3.getX()+player3.getWidth(), (int)player3.getY()+player3.getHeight());
		}
		arrow3.invalidate();
		if(player4.getVisibility()==player4.VISIBLE){
			arrow4.layout((int)player4.getX(), (int)player4.getY(), (int)player4.getX()+player4.getWidth(), (int)player4.getY()+player4.getHeight());
		}
		arrow4.invalidate();
		if(player5.getVisibility()==player5.VISIBLE){
			arrow5.layout((int)player5.getX(), (int)player5.getY(), (int)player5.getX()+player5.getWidth(), (int)player5.getY()+player5.getHeight());
		}
		arrow5.invalidate();
	}
	
	public void setPlaying(int input){
		playing=input;
	}
	
	public void passRunLinePlayerInfo() {//3D??
		mCallback.p1Info(P1);
		mCallback.p2Info(P2);
		mCallback.p3Info(P3);
		mCallback.p4Info(P4);
		mCallback.p5Info(P5);
		mCallback.bInfo(B);
		mCallback.runLineInfo(runBags);

	}

	public void passStartTime(int input) {
		seekBarCallbackStartTime = input;
	}

	public void passDuration(int input) {
		seekBarCallbackDuration = input;
	}

	public void passId(int input){
		seekBarCallbackId = input;
	}
	public void passRecordCheck(boolean input) {
        /*停止錄製按下之後，畫線的curve就清除重算*/
		if(recordCheck == true && input == false){
			p1TempcurveX.clear();
			p1TempcurveY.clear();
			c1Idx =0;
			p2TempcurveX.clear();
			p2TempcurveY.clear();
			c2Idx =0;
			p3TempcurveX.clear();
			p3TempcurveY.clear();
			c3Idx =0;
			p4TempcurveX.clear();
			p4TempcurveY.clear();
			c4Idx =0;
			p5TempcurveX.clear();
			p5TempcurveY.clear();
			c5Idx =0;
			ballTempcurveX.clear();
			ballTempcurveY.clear();
			ballIdx =0;
			
			d1TempcurveX.clear();
			d1TempcurveY.clear();
			cd1Idx =0;
			d2TempcurveX.clear();
			d2TempcurveY.clear();
			cd2Idx =0;
			d3TempcurveX.clear();
			d3TempcurveY.clear();
			cd3Idx =0;
			d4TempcurveX.clear();
			d4TempcurveY.clear();
			cd4Idx =0;
			d5TempcurveX.clear();
			d5TempcurveY.clear();
			cd5Idx =0;
		}
		/**/
		if(recordCheck == false && input==true && firstRecord ==true && intersectName !=0){
			firstRecord =false;
			initialBallNum = intersectName;
			//Log,i("socket", "initial_ball_num="+Integer.toString(initial_ball_num));
		}
		
		
		recordCheck = input;
	}
	public void setSeekBarToRunBag(Vector <Integer> input){
		/**
		 * Vector<Integer> input: 0=Id,1=StartTime,2=Duration
		 * **/
		RunBag tmp = new RunBag();
		tmp= runBags.get(input.get(0));
		tmp.setStartTime(input.get(1));
		tmp.setDuration(input.get(2));
		runBags.set(input.get(0), tmp);
		//Log.d("debug", "Set! "+Integer.toString(input.get(0)));
	}
	
	
	
	public void setMainFragProLow(int Low_in){
		mainFragSeekBarProgressLow =Low_in;
		////Log,i("debug", "MainFrag_set MainFrag_SeekBarProgressLow ="+Integer.toString(MainFrag_SeekBarProgressLow));
	}
	
	public int getMainFragProLow(){
		return mainFragSeekBarProgressLow;
	}
	
	public void clearPaint(){//清除筆跡
		bitmapVector.clear();
		tempBitmap = Bitmap.createBitmap(circle.getWidth(),circle.getHeight(),Bitmap.Config.ARGB_8888);//初始化tempBitmap，指定大小為螢幕大小(大小同circle)
		tempCanvas = new Canvas();
       	tempCanvas = new Canvas(tempBitmap);//畫透明路徑
   		tempCanvas.drawBitmap(tempBitmap, 0, 0, null);
   		tempCanvas.drawCircle(1, 1, 5, transparentPaint);//畫透明路徑
   		circle.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));//把tempBitmap放進circle裡
   		p.reset();
   		c1Idx =0;
		c2Idx =0;
		c3Idx =0;
		c4Idx =0;
		c5Idx =0;
		ballIdx =0;
		p1TempcurveX.clear();
		p1TempcurveY.clear();
		p2TempcurveX.clear();
		p2TempcurveY.clear();
		p3TempcurveX.clear();
		p3TempcurveY.clear();
		p4TempcurveX.clear();
		p4TempcurveY.clear();
		p5TempcurveX.clear();
		p5TempcurveY.clear();
		ballTempcurveX.clear();
		ballTempcurveY.clear();
		cd1Idx =0;
		cd2Idx =0;
		cd3Idx =0;
		cd4Idx =0;
		cd5Idx =0;
		d1TempcurveX.clear();
		d1TempcurveY.clear();
		d2TempcurveX.clear();
		d2TempcurveY.clear();
		d3TempcurveX.clear();
		d3TempcurveY.clear();
		d4TempcurveX.clear();
		d4TempcurveY.clear();
		d5TempcurveX.clear();
		d5TempcurveY.clear();
		curves.clear();
        //tempCanvas.drawBitmap(Bitmap_vector.get(0), 0, 0, null);
        //circle.setImageDrawable(new BitmapDrawable(getResources(), Bitmap_vector.get(1)));//把tempBitmap放進circle裡
	}
	
	public void clear_record(){     
		initialBallNum =0;
		firstRecord =true;
		playing=0;
		P1.clear_all();
		P2.clear_all();
		P3.clear_all();
		P4.clear_all();
		P5.clear_all();
		B.clear_all();
		D1.clear_all();
		D2.clear_all();
		D3.clear_all();
		D4.clear_all();
		D5.clear_all();
		runBags.clear();
		p1StartIndex = 0;
		p2StartIndex = 0;
		p3StartIndex = 0;
		p4StartIndex = 0;
		p5StartIndex = 0;
		bStartIndex = 0;
		d1StartIndex = 0;
		d2StartIndex = 0;
		d3StartIndex = 0;
		d4StartIndex = 0;
		d5StartIndex = 0;
		seekbarTmpId =0;
		mainFragSeekBarProgressLow =0;
		/*P1_Initial_Position_x = -1;
		P2_Initial_Position_x = -1;
		P3_Initial_Position_x = -1;
		P4_Initial_Position_x = -1;
		P5_Initial_Position_x = -1;
		B_Initial_Position_x = -1;
		P1_Initial_Position_y = -1;
		P2_Initial_Position_y = -1;
		P3_Initial_Position_y = -1;
		P4_Initial_Position_y = -1;
		P5_Initial_Position_y = -1;
		B_Initial_Position_y = -1;*/
		
		initialP1Rotate =-1;
		initialP2Rotate =-1;
		initialP3Rotate =-1;
		initialP4Rotate =-1;
		initialP5Rotate =-1;
		initialD1Rotate =-1;
		initialD2Rotate =-1;
		initialD3Rotate =-1;
		initialD4Rotate =-1;
		initialD5Rotate =-1;
		
		clearPaint();
		TimeLine timefrag = (TimeLine) getActivity().getFragmentManager().findFragmentById(R.id.time_line);
		timefrag.clear_record_layout();
		
		MainWrap mainwrap = (MainWrap) getActivity().getFragmentManager().findFragmentById(R.id.MainWrap_frag);
		mainwrap.clear_record_layout();
	}
	
	public int lagrange(Vector<Integer> px,Vector<Integer> py, float x){
		int y=0;
		float tmpy=0;
	    for (int i=0; i<px.size(); ++i)
	    {
	        float a = 1, b = 1;
	        for (int j=0; j<px.size(); ++j)
	        {
	            if (j == i) continue;
	            a *= x - px.get(j);
	            b *= px.get(i) - px.get(j);
	        }
	        tmpy=(py.get(i) * a / b);
	        y += py.get(i) * a / b;
	    }	 
		return y;
	}
	
	public void playerMoveToStartPosition(){
        /*先把全部player移到按下錄製鍵時的位置*/
		
		if(p1InitialPositionX !=-1){
			player1.layout(p1InitialPositionX, p1InitialPositionY, p1InitialPositionX + player1.getWidth(), p1InitialPositionY + player1.getHeight());
			arrow1.layout(p1InitialPositionX, p1InitialPositionY, p1InitialPositionX + arrow1.getWidth(), p1InitialPositionY + arrow1.getHeight());
			rcP1 = new Rect(p1InitialPositionX, p1InitialPositionY, p1InitialPositionX + player1.getWidth(), p1InitialPositionY + player1.getHeight());
			player1.invalidate();
			arrow1.setRotation(initialP1Rotate);
			arrow1.invalidate();
			//Log,i("debug", "P1_initial_pos set!");
		}
		if(p2InitialPositionX !=-1){
			player2.layout(p2InitialPositionX, p2InitialPositionY, p2InitialPositionX + player2.getWidth(), p2InitialPositionY + player2.getHeight());
			arrow2.layout(p2InitialPositionX, p2InitialPositionY, p2InitialPositionX + arrow2.getWidth(), p2InitialPositionY + arrow2.getHeight());
			rcP2 =new Rect(p2InitialPositionX, p2InitialPositionY, p2InitialPositionX + player2.getWidth(), p2InitialPositionY + player2.getHeight());
			player2.invalidate();
			arrow2.setRotation(initialP2Rotate);
			arrow2.invalidate();
			//Log,i("debug", "P2_initial_pos set!");
		}
		if(p3InitialPositionX !=-1){
			player3.layout(p3InitialPositionX, p3InitialPositionY, p3InitialPositionX + player3.getWidth(), p3InitialPositionY + player3.getHeight());
			arrow3.layout(p3InitialPositionX, p3InitialPositionY, p3InitialPositionX + arrow3.getWidth(), p3InitialPositionY + arrow3.getHeight());
			rcP3 = new Rect(p3InitialPositionX, p3InitialPositionY, p3InitialPositionX + player3.getWidth(), p3InitialPositionY + player3.getHeight());
			player3.invalidate();
			arrow3.setRotation(initialP3Rotate);
			arrow3.invalidate();
			//Log,i("debug", "P3_initial_pos set!");
		}
		if(p4InitialPositionX !=-1){
			player4.layout(p4InitialPositionX, p4InitialPositionY, p4InitialPositionX + player4.getWidth(), p4InitialPositionY + player4.getHeight());
			arrow4.layout(p4InitialPositionX, p4InitialPositionY, p4InitialPositionX + arrow4.getWidth(), p4InitialPositionY + arrow4.getHeight());
			rcP4 = new Rect(p4InitialPositionX, p4InitialPositionY, p4InitialPositionX + player4.getWidth(), p4InitialPositionY + player4.getHeight());
			player4.invalidate();
			arrow4.setRotation(initialP4Rotate);
			arrow4.invalidate();
			//Log,i("debug", "P4_initial_pos set!");
		}
		if(p5InitialPositionX !=-1){
			player5.layout(p5InitialPositionX, p5InitialPositionY, p5InitialPositionX + player5.getWidth(), p5InitialPositionY + player5.getHeight());
			arrow5.layout(p5InitialPositionX, p5InitialPositionY, p5InitialPositionX + arrow5.getWidth(), p5InitialPositionY + arrow5.getHeight());
			rcP5 = new Rect(p5InitialPositionX, p5InitialPositionY, p5InitialPositionX + player5.getWidth(), p5InitialPositionY + player5.getHeight());
			player5.invalidate();
			arrow5.setRotation(initialP5Rotate);
			arrow5.invalidate();
			//Log,i("debug", "P5_initial_pos set!");
		}
		if(bInitialPositionX !=-1){
			ball.layout(bInitialPositionX, bInitialPositionY, bInitialPositionX + ball.getWidth(), bInitialPositionY + ball.getHeight());
			rcBall = new Rect(bInitialPositionX, bInitialPositionY, bInitialPositionX + ball.getWidth(), bInitialPositionY + ball.getHeight());
			//Log,i("debug", "Ball_initial_pos set!");
			//Arrow6.layout(P5_Initial_Position_x, P5_Initial_Position_y,P5_Initial_Position_x + Arrow5.getWidth(),P5_Initial_Position_y + Arrow5.getHeight());
		}
		if(d1InitialPositionX !=-1){
			defender1.layout(d1InitialPositionX, d1InitialPositionY, d1InitialPositionX + defender1.getWidth(), d1InitialPositionY + defender1.getHeight());
			arrow7.layout(d1InitialPositionX, d1InitialPositionY, d1InitialPositionX + arrow7.getWidth(), d1InitialPositionY + arrow7.getHeight());
			defender1.invalidate();
			arrow7.setRotation(initialD1Rotate);
			arrow7.invalidate();
			Log.i("debug", "init1: "+ d1InitialPositionX + ", " + d1InitialPositionY);
			//Log.i("debug", "D1_initial_pos set:"+ defender1.getX()+", "+defender1.getY());
		}
		if(d2InitialPositionX !=-1){
			defender2.layout(d2InitialPositionX, d2InitialPositionY, d2InitialPositionX + defender2.getWidth(), d2InitialPositionY + defender2.getHeight());
			arrow8.layout(d2InitialPositionX, d2InitialPositionY, d2InitialPositionX + arrow8.getWidth(), d2InitialPositionY + arrow8.getHeight());
			defender2.invalidate();
			arrow8.setRotation(initialD2Rotate);
			arrow8.invalidate();
			Log.i("debug", "init2: "+ d2InitialPositionX + ", " + d2InitialPositionY);
			//Log,i("debug", "D2_initial_pos set!");
		}
		if(d3InitialPositionX !=-1){
			defender3.layout(d3InitialPositionX, d3InitialPositionY, d3InitialPositionX + defender3.getWidth(), d3InitialPositionY + defender3.getHeight());
			arrow9.layout(d3InitialPositionX, d3InitialPositionY, d3InitialPositionX + arrow9.getWidth(), d3InitialPositionY + arrow9.getHeight());
			defender3.invalidate();
			arrow9.setRotation(initialD3Rotate);
			arrow9.invalidate();
			Log.i("debug", "init3: "+ d3InitialPositionX + ", " + d3InitialPositionY);
			//Log,i("debug", "D3_initial_pos set!");
		}
		if(d4InitialPositionX !=-1){
			defender4.layout(d4InitialPositionX, d4InitialPositionY, d4InitialPositionX + defender4.getWidth(), d4InitialPositionY + defender4.getHeight());
			arrow10.layout(d4InitialPositionX, d4InitialPositionY, d4InitialPositionX + arrow10.getWidth(), d4InitialPositionY + arrow10.getHeight());
			defender4.invalidate();
			arrow10.setRotation(initialD4Rotate);
			arrow10.invalidate();
			Log.i("debug", "init4: "+ d4InitialPositionX + ", " + d4InitialPositionY);
			//Log,i("debug", "D4_initial_pos set!");
		}
		if(d5InitialPositionX !=-1){
			defender5.layout(d5InitialPositionX, d5InitialPositionY, d5InitialPositionX + defender5.getWidth(), d5InitialPositionY + defender5.getHeight());
			arrow11.layout(d5InitialPositionX, d5InitialPositionY, d5InitialPositionX + arrow11.getWidth(), d5InitialPositionY + arrow11.getHeight());
			defender5.invalidate();
			arrow11.setRotation(initialD5Rotate);
			arrow11.invalidate();
			Log.i("debug", "init5: "+ d5InitialPositionX + ", " + d5InitialPositionY);
			//Log,i("debug", "D5_initial_pos set!");
		}
		/*if(initial_ball_num!=0){
			switch(initial_ball_num){
				case 1:
					player1_ball.layout((int)player1.getX()-30, (int)player1.getY(), (int)player1.getX()-30+200, (int)player1.getY()+120);
					player1_ball.setVisibility(player1_ball.VISIBLE);
					player1_ball.invalidate();
					ball.layout((int)player1.getX()+110, (int)player1.getY()+30, (int)player1.getX()+170, (int)player1.getY()+90);
					player1.setVisibility(player1.INVISIBLE);
					player1.invalidate();
					break;
				case 2:
					player2_ball.layout((int)player2.getX()-30, (int)player2.getY(), (int)player2.getX()-30+200, (int)player2.getY()+120);
					player2_ball.setVisibility(player2_ball.VISIBLE);
					player2_ball.invalidate();
					ball.layout((int)player2.getX()+110, (int)player2.getY()+30, (int)player2.getX()+170, (int)player2.getY()+90);
					player2.setVisibility(player2.INVISIBLE);
					player2.invalidate();
					break;
				case 3:
					player3_ball.layout((int)player3.getX()-30, (int)player3.getY(), (int)player3.getX()-30+200, (int)player3.getY()+120);
					player3_ball.setVisibility(player3_ball.VISIBLE);
					player3_ball.invalidate();
					ball.layout((int)player3.getX()+110, (int)player3.getY()+30, (int)player3.getX()+170, (int)player3.getY()+90);
					player3.setVisibility(player3.INVISIBLE);
					player3.invalidate();
					break;
				case 4:
					player4_ball.layout((int)player4.getX()-30, (int)player4.getY(), (int)player4.getX()-30+200, (int)player4.getY()+120);
					player4_ball.setVisibility(player4_ball.VISIBLE);
					player4_ball.invalidate();
					ball.layout((int)player4.getX()+110, (int)player4.getY()+30, (int)player4.getX()+170, (int)player4.getY()+90);
					player4.setVisibility(player4.INVISIBLE);
					player4.invalidate();
					break;
				case 5:
					player5_ball.layout((int)player5.getX()-30, (int)player5.getY(), (int)player5.getX()-30+200, (int)player5.getY()+120);
					player5_ball.setVisibility(player5_ball.VISIBLE);
					player5_ball.invalidate();
					ball.layout((int)player5.getX()+110, (int)player5.getY()+30, (int)player5.getX()+170, (int)player5.getY()+90);
					player5.setVisibility(player5.INVISIBLE);
					player5.invalidate();
					break;
			}
			ball.invalidate();
		}*/
	}	
	
	public void playerChangeToNoBall(){
		if(intersectName !=0){
			switch(intersectName){
			case 1:
				player1.setVisibility(player1.VISIBLE);
				if(intersect==true){
					////Log,i("debug","intersect=true,player1 should be layout on player_ball's position");
					player1.layout((int) player1Ball.getX()+30, (int) player1Ball.getY(),(int) player1Ball.getX()+30+player1.getWidth(), (int) player1Ball.getY()+player1.getHeight());
				}
				player1.invalidate();
				player1Ball.setVisibility(player1Ball.INVISIBLE);
				player1Ball.invalidate();
				arrow1.layout((int)player1.getX(), (int)player1.getY(), (int)player1.getX()+player1.getWidth(), (int)player1.getY()+player1.getHeight());
				arrow1.invalidate();
				break;
			case 2:
				player2.setVisibility(player2.VISIBLE);
				if(intersect==true){
					////Log,i("debug","intersect=true,player1 should be layout on player_ball's position");
					player2.layout((int) player2Ball.getX()+30, (int) player2Ball.getY(),(int) player2Ball.getX()+30+player2.getWidth(), (int) player2Ball.getY()+player2.getHeight());
				}
				player2.invalidate();
				player2Ball.setVisibility(player2Ball.INVISIBLE);
				player2Ball.invalidate();
				arrow2.layout((int)player2.getX(), (int)player2.getY(), (int)player2.getX()+player2.getWidth(), (int)player2.getY()+player2.getHeight());
				arrow2.invalidate();
				break;
			case 3:
				player3.setVisibility(player3.VISIBLE);
				if(intersect==true){
					////Log,i("debug","intersect=true,player1 should be layout on player_ball's position");
					player3.layout((int) player3Ball.getX()+30, (int) player3Ball.getY(),(int) player3Ball.getX()+30+player3.getWidth(), (int) player3Ball.getY()+player3.getHeight());
				}
				player3.invalidate();
				player3Ball.setVisibility(player3Ball.INVISIBLE);
				player3Ball.invalidate();
				arrow3.layout((int)player3.getX(), (int)player3.getY(), (int)player3.getX()+player3.getWidth(), (int)player3.getY()+player3.getHeight());
				arrow3.invalidate();
				break;
			case 4:
				player4.setVisibility(player4.VISIBLE);
				if(intersect==true){
					////Log,i("debug","intersect=true,player1 should be layout on player_ball's position");
					player4.layout((int) player4Ball.getX()+30, (int) player4Ball.getY(),(int) player4Ball.getX()+30+player4.getWidth(), (int) player4Ball.getY()+player4.getHeight());
				}
				player4.invalidate();
				player4Ball.setVisibility(player4Ball.INVISIBLE);
				player4Ball.invalidate();
				arrow4.layout((int)player4.getX(), (int)player4.getY(), (int)player4.getX()+player4.getWidth(), (int)player4.getY()+player4.getHeight());
				arrow4.invalidate();
				break;
			case 5:
				player5.setVisibility(player5.VISIBLE);
				if(intersect==true){
					////Log,i("debug","intersect=true,player1 should be layout on player_ball's position");
					player5.layout((int) player5Ball.getX()+30, (int) player5Ball.getY(),(int) player5Ball.getX()+30+player5.getWidth(), (int) player5Ball.getY()+player5.getHeight());
				}
				player5.invalidate();
				player5Ball.setVisibility(player5Ball.INVISIBLE);
				player5Ball.invalidate();
				arrow5.layout((int)player5.getX(), (int)player5.getY(), (int)player5.getX()+player5.getWidth(), (int)player5.getY()+player5.getHeight());
				arrow5.invalidate();
				break;
			}
		}
	}
	
	
	
	
	//TODO DTW
	/**************************************************************************/
	public void doRecommend(){
		Vector <String> recommend=new Vector();
		recommend=load_Default_Strategies();
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		final View v = inflater.inflate(R.layout.dialog_layout, null);
        //把xml當成view來用，就能加入alert dialog裡面了
		LinearLayout linearLayout=(LinearLayout) v.findViewById(R.id.dialog_linear_layout);
        //取得dialog_layout.xml裡面的dialog_linear_layout，就能夠用來動態加入view了
		LinearLayout.LayoutParams textlp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		textlp.gravity=Gravity.CENTER;
		textlp.topMargin=80;
		LinearLayout.LayoutParams imglp = new LinearLayout.LayoutParams(800,800);
		imglp.gravity=Gravity.CENTER;
		
		
		FilenameFilter namefilter =new FilenameFilter(){ 
            private String[] filter={
                    "txt"
            };
            @Override
            public boolean accept(File dir, String filename){
                for(int i=0;i<filter.length;i++){
                    if(filename.indexOf(filter[i])!=-1)
                        return true;
                }
                return false;
            }
        };
        File dir = getActivity().getBaseContext().getExternalFilesDir(null);
        //String str=filePath.getName();取得檔案夾名稱
        //textFileName.setText(str);
        File[] fileList=dir.listFiles(namefilter);
        String[] list =new String[fileList.length];
        for(int i=0;i<list.length;i++){
            list[i]=fileList[i].getName();
            //Log,i("reco", "filenames = "+list[i]);
        }
        final String[] strategies = list;
		for(int i=0;i<recommend.size();i++){
			TextView text = new TextView(getActivity());
			ImageView img = new ImageView(getActivity());
			Button btn = new Button(getActivity());
			for(int j=0;j<strategies.length;j++){
				if(recommend.get(i).equals(strategies[j])){
					text.setText(strategies[j]);
					String name_no_txt = strategies[j].substring(0, strategies[j].length()-4);
					img.setImageResource(getResources().getIdentifier(name_no_txt, "drawable", getActivity().getPackageName()));
					btn.setText("OK");
					final int tmp =j;
					btn.setOnClickListener(new Button.OnClickListener(){ 
			            @Override
			            public void onClick(View v) {
			                // TODO Auto-generated method stub
			            	File dir = getActivity().getBaseContext().getExternalFilesDir(null);
				        	File inFile = new File(dir, strategies[tmp]);
				        	clear_record();
				        	readStrategy(inFile);
			            }        
			        });  	
				}
			}
			text.setTextSize(30);
			text.setLayoutParams(textlp);
			img.setLayoutParams(imglp);
			linearLayout.addView(text);
			linearLayout.addView(img);
			linearLayout.addView(btn);
		}
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setView(v);
		builder.create().show();
	
	}

	private Vector <String> load_Default_Strategies(){      

		File dir = getActivity().getBaseContext().getExternalFilesDir(null);
		Log.d("warning", String.valueOf(dir));
		File inFile = null;
    	File Files[]= dir.listFiles();
    	Vector <String> output = new Vector();
    	int DTW_Threshold=15;
    	
    	for(int j = 0;j<Files.length;j++){
    		String data = readFromFile(Files[j]);
    		String [] sData=data.split("\n");
    		p1Recommend.clear_all();
			p2Recommend.clear_all();
			p3Recommend.clear_all();
			p4Recommend.clear_all();
			p5Recommend.clear_all();
			bRecommend.clear_all();
    		for(int i =0;i<sData.length;i++){
    		//?sPlayer??road
    			
    			if(sData[i].equals("P1")){
    				i++;
    				while(!sData[i].equals("---")){
    					p1Recommend.setRoad(Integer.parseInt(sData[i]));
    					i++;
    				}
    			}
    			if(sData[i].equals("P2")){
    				i++;
    				while(!sData[i].equals("---")){
    					p2Recommend.setRoad(Integer.parseInt(sData[i]));
    					i++;
    				}
    			}
    			if(sData[i].equals("P3")){
    				i++;
    				while(!sData[i].equals("---")){
    					p3Recommend.setRoad(Integer.parseInt(sData[i]));
    					i++;
    				}
    			}
    			if(sData[i].equals("P4")){
    				i++;
    				while(!sData[i].equals("---")){
    					p4Recommend.setRoad(Integer.parseInt(sData[i]));
    					i++;
    				}
    			}
    			if(sData[i].equals("P5")){
    				i++;
    				while(!sData[i].equals("---")){
    					p5Recommend.setRoad(Integer.parseInt(sData[i]));
    					i++;
    				}
    			}
    			if(sData[i].equals("B")){
    				i++;
    				while(!sData[i].equals("---")){
    					bRecommend.setRoad(Integer.parseInt(sData[i]));
    					i++;
    				}
    			}
    		}//sData's for loop

    		if(recommendSwitch == 0){
    			if(DTW(P1.getCmpltRoad(), p1Recommend.getCmpltRoad())< EDRThresholdPersent ||
        				DTW(P2.getCmpltRoad(), p2Recommend.getCmpltRoad())< EDRThresholdPersent ||
        				DTW(P3.getCmpltRoad(), p3Recommend.getCmpltRoad())< EDRThresholdPersent ||
        				DTW(P4.getCmpltRoad(), p4Recommend.getCmpltRoad())< EDRThresholdPersent ||
        				DTW(P5.getCmpltRoad(), p5Recommend.getCmpltRoad())< EDRThresholdPersent ||
        				DTW(B.getCmpltRoad(), bRecommend.getCmpltRoad())< EDRThresholdPersent){
        			//Log,i("reco", "Maybe Can recommend this strategy!"+Files[j].getName());
        			output.add(Files[j].getName());
        		}
        		Log.d("reco", "DTW = "+Double.toString(DTW(P1.getCmpltRoad(), p1Recommend.getCmpltRoad())));
        		/*
        		if(Files[j].getName().equals("right_up.txt")){
        			reco_result_textview.setText(String.format("%.2f", (DTW(P1.getCmpltRoad(),P1_recommend.getCmpltRoad()))));
        		}*/
        		
            	Log.d("reco", "DTW = "+Double.toString(DTW(P2.getCmpltRoad(), p2Recommend.getCmpltRoad())));
            	Log.d("reco", "DTW = "+Double.toString(DTW(P3.getCmpltRoad(), p3Recommend.getCmpltRoad())));
            	Log.d("reco", "DTW = "+Double.toString(DTW(P4.getCmpltRoad(), p4Recommend.getCmpltRoad())));
            	Log.d("reco", "DTW = "+Double.toString(DTW(P5.getCmpltRoad(), p5Recommend.getCmpltRoad())));
            	Log.d("reco", "DTW = "+Double.toString(DTW(B.getCmpltRoad(), bRecommend.getCmpltRoad())));
            	
    		}
    		else{
    			if(EDR(P1.getCmpltRoad(), p1Recommend.getCmpltRoad())<EDR_threshold(P1.getRoadSize(), p1Recommend.getRoadSize()) ||
        				EDR(P2.getCmpltRoad(), p2Recommend.getCmpltRoad())<EDR_threshold(P2.getRoadSize(), p2Recommend.getRoadSize()) ||
        				EDR(P3.getCmpltRoad(), p3Recommend.getCmpltRoad())<EDR_threshold(P3.getRoadSize(), p3Recommend.getRoadSize()) ||
        				EDR(P4.getCmpltRoad(), p4Recommend.getCmpltRoad())<EDR_threshold(P4.getRoadSize(), p4Recommend.getRoadSize()) ||
        				EDR(P5.getCmpltRoad(), p5Recommend.getCmpltRoad())<EDR_threshold(P5.getRoadSize(), p5Recommend.getRoadSize()) ||
        				EDR(B.getCmpltRoad(), bRecommend.getCmpltRoad())<EDR_threshold(B.getRoadSize(), bRecommend.getRoadSize())){
        			//Log,i("reco", "Maybe Can recommend this strategy!"+Files[j].getName());
        			output.add(Files[j].getName());
        		}
    			
    			Log.d("reco", "P1 size = "+Integer.toString(P1.getRoadSize())+", P1_reco size = "+Integer.toString(p1Recommend.getRoadSize()));
        		Log.d("reco", "EDR = "+Double.toString(EDR(P1.getCmpltRoad(), p1Recommend.getCmpltRoad()))+"EDR_Threshold = "+Double.toString(EDR_threshold(P1.getRoadSize(), p1Recommend.getRoadSize())));
        		/*if(Files[j].getName().equals("right_up.txt")){
        			reco_result_textview.setText(String.format("%.2f", (EDR(P1.getCmpltRoad(),P1_recommend.getCmpltRoad()))));
        			EDR_path_size_LinearLayout.setVisibility(View.VISIBLE);
        			
        			double EDR_result=EDR(P1.getCmpltRoad(),P1_recommend.getCmpltRoad());
        			double persent;
        			int tmp;
        			int tmp_size = P1.getRoadSize();
        			int tmp_reco_size = P1_recommend.getRoadSize();
        			tmp = Math.abs(tmp_size - tmp_reco_size);
        			if(tmp_size < tmp_reco_size){
        				 persent = (EDR_result - tmp);
        			}
        			else{
        				persent = (EDR_result - tmp);
        			}
        			
        			
        			
        			EDR_path_size.setText(String.format("%.2f", persent));
        			
        		}*/
        		Log.d("reco", "P2 size = "+Integer.toString(P2.getRoadSize())+", P2_reco size = "+Integer.toString(p2Recommend.getRoadSize()));
        		Log.d("reco", "EDR = "+Double.toString(EDR(P2.getCmpltRoad(), p2Recommend.getCmpltRoad()))+"EDR_Threshold = "+Double.toString(EDR_threshold(P2.getRoadSize(), p2Recommend.getRoadSize())));
        		Log.d("reco", "P3 size = "+Integer.toString(P3.getRoadSize())+", P3_reco size = "+Integer.toString(p3Recommend.getRoadSize()));
        		Log.d("reco", "EDR = "+Double.toString(EDR(P3.getCmpltRoad(), p3Recommend.getCmpltRoad()))+"EDR_Threshold = "+Double.toString(EDR_threshold(P3.getRoadSize(), p3Recommend.getRoadSize())));
                Log.d("reco", "EDR = "+Double.toString(EDR(P4.getCmpltRoad(), p4Recommend.getCmpltRoad()))+"EDR_Threshold = "+Double.toString(EDR_threshold(P4.getRoadSize(), p4Recommend.getRoadSize())));
                Log.d("reco", "EDR = "+Double.toString(EDR(P5.getCmpltRoad(), p5Recommend.getCmpltRoad()))+"EDR_Threshold = "+Double.toString(EDR_threshold(P5.getRoadSize(), p5Recommend.getRoadSize())));
                Log.d("reco", "EDR = "+Double.toString(EDR(B.getCmpltRoad(), bRecommend.getCmpltRoad()))+"EDR_Threshold = "+Double.toString(EDR_threshold(B.getRoadSize(), bRecommend.getRoadSize())));
                
    		}
    	}//File_Names's for loop
    	return output;
	}
	
	private OnClickListener recommend_button_Listener = new OnClickListener(){//開始/停止錄製
    	@Override
    	public void onClick(View v) {
    		
    		if(recommendButton.isChecked()){
    			recommendSwitch =1;
    		}
    		else{
    			recommendSwitch = 0;
    		}
    	}
    };

	public double DTW(Vector<Integer> road_a,Vector<Integer> road_b){
		
		Vector<Integer> split_a = new Vector();
		Vector<Integer> split_b = new Vector();
		
		Vector<Double> nor_a=new Vector();
		Vector<Double> nor_b=new Vector();
		
		Vector<MyPoint> finish_a = new Vector();
		Vector<MyPoint> finish_b = new Vector();
		
		//save pure road without 0
		if(road_a.size()==0){
			return 100;
		}
		else if(road_b.size()==0){
			return 100;
		}
		else{
			
			int count_0=0;
			for(int i=0;i<road_a.size();i++){
				if(road_a.get(i)!=0){
					split_a.add(road_a.get(i));
				}
				else{
					count_0++;
				}
			}
			
			count_0=0;
			for(int i=0;i<road_b.size();i++){
				if(road_b.get(i)!=0){
					split_b.add(road_b.get(i));
				}
				else{
					count_0++;
				}
			}
			
			nor_a=normalization(split_a);
			nor_b=normalization(split_b);
			
			for(int i=0;i<nor_a.size();i++){
				MyPoint tmp = new MyPoint();
				tmp.x=nor_a.get(i);
				tmp.y=nor_a.get(i+1);
				finish_a.add(tmp);
				i=i+1;
			}
			
			for(int i=0;i<nor_b.size();i++){
				MyPoint tmp = new MyPoint();
				tmp.x=nor_b.get(i);
				tmp.y=nor_b.get(i+1);
				finish_b.add(tmp);
				i=i+1;
			}
			double D[][]=new double[finish_a.size()][finish_b.size()];
			
			for(int i=0;i<finish_a.size();i++){
				for(int j=0;j<finish_b.size();j++){
					if((i-1)<0 || (j-1)<0){
						D[i][j]=Dist(finish_a.get(i).x,finish_a.get(i).y,finish_b.get(j).x,finish_b.get(j).y);
					}
					else{
						D[i][j]=Dist(finish_a.get(i).x,finish_a.get(i).y,finish_b.get(j).x,finish_b.get(j).y)+Math.min(D[i-1][j],Math.min(D[i][j-1],D[i-1][j-1]));
					}
					//Log.d("debug", "D["+Integer.toString(i)+"]["+Integer.toString(j)+"]= "+Double.toString(D[i][j]));
				}
			}
			return D[(finish_a.size())-1][(finish_b.size())-1];
		}
		
	}
	
	public double EDR_threshold(int road1_size,int road2_size){  
		double result=0;
		
		//Log,i("reco", "EDR_Threshold_Persent = "+EDR_Threshold_Persent);
		
		if(road1_size==0 && road2_size==0){
			return 1;
		}
		else if(road1_size==0){
			return road2_size-10;
		}
		else if(road2_size==0){
			return road1_size-10;
		}
			
		if(road1_size==road2_size){
			return road1_size* EDRThresholdPersent;
		}
		else if(road1_size>road2_size){
			double EDR_basic=0;
			EDR_basic = road1_size-road2_size;
			result = EDR_basic+((road1_size-EDR_basic)* EDRThresholdPersent);
		}
		else if(road1_size<road2_size){
			double EDR_basic=0;
			EDR_basic = road2_size-road1_size;
			result = EDR_basic+((road2_size-EDR_basic)* EDRThresholdPersent);
		}
		else{
			//Log,i("reco", "None of the situations?!");
		}
		
		return result;
	}
	
	public double EDR(Vector<Integer> road_a,Vector<Integer> road_b){   
		
		Vector<Integer> split_a = new Vector();
		Vector<Integer> split_b = new Vector();
		
		Vector<Double> nor_a=new Vector();
		Vector<Double> nor_b=new Vector();
		
		Vector<MyPoint> finish_a = new Vector();
		Vector<MyPoint> finish_b = new Vector();
		
		//save pure road without 0
		if(road_a.size()==0 && road_b.size()==0){
			Log.d("Similarity", "Both = 0");
			return 100;
		}
		else if(road_a.size()==0){
			Log.d("Similarity", "road_a size = 0");
			return road_b.size();
		}
		else if(road_b.size()==0){
			Log.d("Similarity", "road_b size = 0");
			return road_a.size();
		}
		else{
			Log.d("Similarity", "Both != 0");
			int count_0=0;
			for(int i=0;i<road_a.size();i++){
				if(road_a.get(i)!=0){
					split_a.add(road_a.get(i));
				}
				else{
					count_0++;
				}
			}
			
			count_0=0;
			for(int i=0;i<road_b.size();i++){
				if(road_b.get(i)!=0){
					split_b.add(road_b.get(i));
				}
				else{
					count_0++;
				}
			}
			
			nor_a=normalization(split_a);
			nor_b=normalization(split_b);
			
			for(int i=0;i<nor_a.size();i++){
				MyPoint tmp = new MyPoint();
				tmp.x=nor_a.get(i);
				tmp.y=nor_a.get(i+1);
				finish_a.add(tmp);
				
				/*******test**********/
				finish_b.add(tmp);
				
				/*********************/
				
				
				i=i+1;
			}
			
			for(int i=0;i<nor_b.size();i++){
				MyPoint tmp = new MyPoint();
				tmp.x=nor_b.get(i);
				tmp.y=nor_b.get(i+1);
				finish_b.add(tmp);
				i=i+1;
			}
			
			double D[][]=new double[finish_a.size()][finish_b.size()];
			int subcost=0;
			double threshold = 0.1;
			for(int i=0;i<finish_a.size();i++){
				for(int j=0;j<finish_b.size();j++){
					if(i==0 && j==0){
						D[i][j]=0;
					}
					else if(i==0){
						D[i][j]=j;
					}
					else if(j==0){
						D[i][j]=i;
					}
				}
			}
			
			
			for(int i=1;i<finish_a.size();i++){
				for(int j=1;j<finish_b.size();j++){
					/*if(i==0 && j==0){
						D[i][j]=0;
					}
					else if(i==0){
						D[i][j]=j;
					}
					else if(j==0){
						D[i][j]=i;
					}*/
						
					if((Math.abs(finish_a.get(i).x-finish_b.get(j).x)<=threshold) && ((Math.abs(finish_a.get(i).y-finish_b.get(j).y)<=threshold))){
						subcost = 0;
						//Log.d("Similarity","Subcost = 0");
					}
					else{
						subcost = 1;
						//Log.d("Similarity","Subcost = 1");
					}
					D[i][j]=+Math.min(D[i-1][j]+1,Math.min(D[i][j-1]+1,D[i-1][j-1]+subcost));
					
					if(j==finish_b.size()-1){
						String tmp ="";
						for(int k=0;k<j+1;k++){
							tmp+=Double.toString(D[i][k]);
							tmp+=" ";
						}
						//Log.d("Similarity","i= "+i+" :"+tmp);
					}
				}
			}
			return D[(finish_a.size())-1][(finish_b.size())-1];
		}
		
	}
	

	public Vector<MyPoint> processSimiRoad(Vector<Integer> inputRoad){

		Vector<Integer> splitRoad = new Vector<Integer>();
		Vector<Double> norRoad = new Vector<Double>();
		Vector<MyPoint> finishRoad = new Vector<MyPoint>();

		int count_0=0;
		for(int i=0;i<inputRoad.size();i++){
			if(inputRoad.get(i)!=0){
				splitRoad.add(inputRoad.get(i));
			}
			else{
				count_0++;
				//Log.d("debug", "count_0 =  "+Integer.toString(count_0));
			}
		}

		norRoad=normalization(splitRoad);
		for(int i=0;i<norRoad.size();i++){
			MyPoint tmp = new MyPoint();
			tmp.x=norRoad.get(i);
			tmp.y=norRoad.get(i+1);
			finishRoad.add(tmp);
			i=i+1;
		}
		return finishRoad;
	}

	public double Do_EDR(Vector<Integer> road_a,Vector<Integer> road_b){

		Vector<MyPoint> input_road_a = new Vector<MyPoint>();
		Vector<MyPoint> input_road_b = new Vector<MyPoint>();

		if(road_a.size()!=0)
			input_road_a = processSimiRoad(road_a);
		if(road_b.size()!=0)
			input_road_b = processSimiRoad(road_b);
		return EDR2(input_road_a,input_road_b);

	}

	public double EDR2(Vector<MyPoint> road_a, Vector<MyPoint> road_b){
				int subcost=0;
				double threshold = 1;
				double result=0;

				if(road_a.size()==0 && road_b.size()==0){
					return 100;
				}
				if(road_a.size()==0){
					return road_b.size();
				}
				else if(road_b.size()==0){
					return road_a.size();
				}
				else{
					Vector<MyPoint> rest_a = new Vector<MyPoint>();
					Vector<MyPoint> rest_b = new Vector<MyPoint>();
					for(int i = 0;i<road_a.size()-1;i++){
						rest_a.add(road_a.get(i));
					}
					for(int i = 0;i<road_b.size()-1;i++){
						rest_b.add(road_b.get(i));
					}
					if((Math.abs(road_a.get(road_a.size()-1).x-road_b.get(road_b.size()-1).x)<=threshold) && ((Math.abs(road_a.get(road_a.size()-1).y-road_b.get(road_b.size()-1).y)<=threshold))){
						subcost = 0;
						//Log.d("reco","Subcost = 0");
					}
					else{
						subcost = 1;
						//Log.d("reco","Subcost = 1");
					}
					result = Math.min(EDR2(rest_a,rest_b)+subcost,Math.min(EDR2(rest_a,road_b)+1, EDR2(road_a,rest_b)+1));
					return result;
				}
	}

	public double Dist(int xi,int yi,int xj,int yj){

		double dist=0;
		int x = Math.abs(xi-xj);
		//Log.d("debug", "abs x =  "+Integer.toString(x));
		int y = Math.abs(yi-yj);
		//Log.d("debug", "abs y =  "+Integer.toString(y));
		
		dist = Math.sqrt((x*x)+(y*y));
		//Log.d("debug", "dist =  "+Double.toString(dist));
		
		return dist;
	}
	
	public double Dist(double xi,double yi,double xj,double yj){

		double dist=0;
		double x = Math.abs(xi-xj);
		//Log.d("debug", "abs x =  "+Integer.toString(x));
		double y = Math.abs(yi-yj);
		//Log.d("debug", "abs y =  "+Integer.toString(y));
		
		dist = Math.sqrt((x*x)+(y*y));
		//Log.d("debug", "dist =  "+Double.toString(dist));
		
		return dist;
	}
	
	public Vector<Double> normalization(Vector<Integer> road){
		int xSum=0;
		int ySum=0;
		float xMean=0;
		float yMean=0;
		int xSumTmp=0;
		int ySumTmp=0;
		double xStandardDeviation=0;
		double yStandardDeviation=0;
		
		Vector<Double> nRoad=new Vector();
		
		/*x*/
		for(int i=0;i<road.size();i=i+2){
			xSum+=road.get(i);
		}
		xMean=xSum/(road.size()/2);
		
		for(int i=0;i<road.size();i=i+2){
			xSumTmp+=(road.get(i)-xMean)*(road.get(i)-xMean);
		}
		xStandardDeviation=Math.sqrt(xSumTmp/(road.size()/2));
		
		/*y*/
		for(int i=1;i<road.size();i=i+2){
			ySum+=road.get(i);
		}
		yMean=ySum/(road.size()/2);
		
		for(int i=1;i<road.size();i=i+2){
			ySumTmp+=(road.get(i)-yMean)*(road.get(i)-yMean);
		}
		yStandardDeviation=Math.sqrt(ySumTmp/(road.size()/2));
		
		
		/*save new normalized road*/
		for(int i=0;i<road.size();i++){
			nRoad.add((road.get(i)-xMean)/xStandardDeviation);
			i=i+1;
			nRoad.add((road.get(i)-yMean)/yStandardDeviation);
		}
		
		
		
		return nRoad;
	}
	
	/**************************************************************************/
    //TODO 儲存、載入戰術(按鈕的判斷在ButtonDraw.java裡面)
	/**************************************************************************/
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void dialogManageTactic(){
	    LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View myv = inflater.inflate(R.layout.manage_tactic_layout, null);//把xml當成view來用，就能加入alert dialog裡面了

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(myv);
        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setLayout(450,400);

        final Button btnTacticSave = (Button)myv.findViewById(R.id.save_strategy_button);
        btnTacticSave.setOnTouchListener(new OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    btnTacticSave.setBackgroundResource(R.drawable.btn_save_clicked);
                    dialog.dismiss();
                    save_dialog();

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btnTacticSave.setBackgroundResource(R.drawable.btn_save);

                }
                return true;
            }
        });

        final Button btnTacticLoad = (Button)myv.findViewById(R.id.load_strategy_button);
        btnTacticLoad.setOnTouchListener(new OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    btnTacticLoad.setBackgroundResource(R.drawable.btn_load_clicked);
                    dialog.dismiss();
                    load_dialog();

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btnTacticLoad.setBackgroundResource(R.drawable.btn_load);
                }
                return true;
            }
        });

    }

	public void save_dialog(){
    	
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		final View myv = inflater.inflate(R.layout.save_to_strategy_layout, null);//把xml當成view來用，就能加入alert dialog裡面了
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setView(myv);
        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setLayout(1000,800);

        Spinner cate_spinner = (Spinner)myv.findViewById(R.id.category_spinner_in_save);
        cate_spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectCategoryId = new Long(l).intValue();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectCategoryId = 0;
            }
        });

    	final Button saveOkButton = (Button) myv.findViewById(R.id.save_strategy_button2);
    	saveOkButton.setOnTouchListener(new OnTouchListener(){//"Button of Enter the Strategy's Name
	        	@TargetApi(Build.VERSION_CODES.M)
				@Override
	        	public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        saveOkButton.setBackgroundResource(R.drawable.btn_save_clicked);

                        EditText name = (EditText) myv.findViewById(R.id.enter_name_text);//取得輸入text的view
                        String saveName = name.getText().toString();

                        //
                        //  Tactic file name :
                        //      [Tactic Name]_[Tactic category Id].json
                        //

                        File dir = getActivity().getBaseContext().getExternalFilesDir(null);
                        selectTacticName = saveName;
                        File outFile = new File(dir, saveName + "_" + selectCategoryId + ".json");


						String tmp = new String();
                        //將資料寫入檔案中，若 package name 為 com.myapp
                        //就會產生 /data/data/com.myapp/files/test.txt 檔案

                        pInitialPosition.clear();
                        pInitialRotate.clear();

                        //region 2018.07.12 Version.2 以json格式來存檔
						JSONObject save_strategy = new JSONObject();
						try {
							JSONArray tmp_arr = new JSONArray();
							//region Initial_Position
							tmp_arr.put(p1InitialPositionX);
							tmp_arr.put(p1InitialPositionY);
							tmp_arr.put(p2InitialPositionX);
							tmp_arr.put(p2InitialPositionY);
							tmp_arr.put(p3InitialPositionX);
							tmp_arr.put(p3InitialPositionY);
							tmp_arr.put(p4InitialPositionX);
							tmp_arr.put(p4InitialPositionY);
							tmp_arr.put(p5InitialPositionX);
							tmp_arr.put(p5InitialPositionY);

							tmp_arr.put(d1InitialPositionX);
							tmp_arr.put(d1InitialPositionY);
							tmp_arr.put(d2InitialPositionX);
							tmp_arr.put(d2InitialPositionY);
							tmp_arr.put(d3InitialPositionX);
							tmp_arr.put(d3InitialPositionY);
							tmp_arr.put(d4InitialPositionX);
							tmp_arr.put(d4InitialPositionY);
							tmp_arr.put(d5InitialPositionX);
							tmp_arr.put(d5InitialPositionY);

							tmp_arr.put(bInitialPositionX);
							tmp_arr.put(bInitialPositionY);

							save_strategy.put("Initial_Position", tmp_arr);
							//endregion
							//region Initial_Rotation
							tmp_arr = new JSONArray();
							tmp_arr.put(initialP1Rotate);
							tmp_arr.put(initialP2Rotate);
							tmp_arr.put(initialP3Rotate);
							tmp_arr.put(initialP4Rotate);
							tmp_arr.put(initialP5Rotate);

							tmp_arr.put(initialD1Rotate);
							tmp_arr.put(initialD2Rotate);
							tmp_arr.put(initialD3Rotate);
							tmp_arr.put(initialD4Rotate);
							tmp_arr.put(initialD5Rotate);

							save_strategy.put("Initial_Rotation", tmp_arr);
							//endregion

							save_strategy.put("Initial_ball_holder", initialBallNum);
							save_strategy.put("Tactic_name", saveName);
							save_strategy.put("Category_id", selectCategoryId);

							//region Player road sequence
							tmp_arr = new JSONArray();
							for(int i=0;i<B.getRoadSize();i++){
								tmp_arr.put( String.valueOf(B.handleGetRoad(i) ));
							}
							if(tmp_arr.length() > 0)
								save_strategy.put("B", tmp_arr);

							tmp_arr = new JSONArray();
							for(int i=0;i<P1.getRoadSize();i++){
								tmp_arr.put( String.valueOf(P1.handleGetRoad(i)));
							}
							if(tmp_arr.length() > 0)
								save_strategy.put("P1", tmp_arr);

							tmp_arr = new JSONArray();
							for(int i=0;i<P2.getRoadSize();i++){
								tmp_arr.put(  String.valueOf(P2.handleGetRoad(i)));
							}
							if(tmp_arr.length() > 0)
								save_strategy.put("P2", tmp_arr);

							tmp_arr = new JSONArray();
							for(int i=0;i<P3.getRoadSize();i++){
								tmp_arr.put(  String.valueOf(P3.handleGetRoad(i)));
							}
							if(tmp_arr.length() > 0)
								save_strategy.put("P3", tmp_arr);

							tmp_arr = new JSONArray();
							for(int i=0;i<P4.getRoadSize();i++){
								tmp_arr.put( String.valueOf(P4.handleGetRoad(i)));
							}
							if(tmp_arr.length() > 0)
								save_strategy.put("P4", tmp_arr);

							tmp_arr = new JSONArray();
							for(int i=0;i<P5.getRoadSize();i++){
								tmp_arr.put(String.valueOf(P5.handleGetRoad(i)));
							}
							if(tmp_arr.length() > 0)
								save_strategy.put("P5", tmp_arr);

							tmp_arr = new JSONArray();
							for(int i=0;i<D1.getRoadSize();i++){
								tmp_arr.put(String.valueOf(D1.handleGetRoad(i)));
							}
							if(tmp_arr.length() > 0)
								save_strategy.put("D1", tmp_arr);

							tmp_arr = new JSONArray();
							for(int i=0;i<D2.getRoadSize();i++){
								tmp_arr.put(String.valueOf(D2.handleGetRoad(i)));
							}
							if(tmp_arr.length() > 0)
								save_strategy.put("D2", tmp_arr);

							tmp_arr = new JSONArray();
							for(int i=0;i<D3.getRoadSize();i++){
								tmp_arr.put(String.valueOf(D3.handleGetRoad(i)));
							}
							if(tmp_arr.length() > 0)
								save_strategy.put("D3", tmp_arr);

							tmp_arr = new JSONArray();
							for(int i=0;i<D4.getRoadSize();i++){
								tmp_arr.put(String.valueOf(D4.handleGetRoad(i)));
							}
							if(tmp_arr.length() > 0)
								save_strategy.put("D4", tmp_arr);

							tmp_arr = new JSONArray();
							for(int i=0;i<D5.getRoadSize();i++){
								tmp_arr.put(String.valueOf(D5.handleGetRoad(i)));
							}
							if(tmp_arr.length() > 0)
								save_strategy.put("D5", tmp_arr);
							//endregion

							//region Player rotation sequence
							tmp_arr = new JSONArray();
							for(int i=0;i<P1.getRotation_size();i++){
								tmp_arr.put( String.valueOf(P1.getMyRotation(i)));
							}
							if(tmp_arr.length() > 0)
								save_strategy.put("P1_Rotation", tmp_arr);

							tmp_arr = new JSONArray();
							for(int i=0;i<P2.getRotation_size();i++){
								tmp_arr.put( String.valueOf(P2.getMyRotation(i)));
							}
							if(tmp_arr.length() > 0)
								save_strategy.put("P2_Rotation", tmp_arr);

							tmp_arr = new JSONArray();
							for(int i=0;i<P3.getRotation_size();i++){
								tmp_arr.put( String.valueOf(P3.getMyRotation(i)));
							}
							if(tmp_arr.length() > 0)
								save_strategy.put("P3_Rotation", tmp_arr);

							tmp_arr = new JSONArray();
							for(int i=0;i<P4.getRotation_size();i++){
								tmp_arr.put( String.valueOf(P4.getMyRotation(i)));
							}
							if(tmp_arr.length() > 0)
								save_strategy.put("P4_Rotation", tmp_arr);

							tmp_arr = new JSONArray();
							for(int i=0;i<P5.getRotation_size();i++){
								tmp_arr.put( String.valueOf(P5.getMyRotation(i)));
							}
							if(tmp_arr.length() > 0)
								save_strategy.put("P5_Rotation", tmp_arr);


							tmp_arr = new JSONArray();
							for(int i=0;i<D1.getRotation_size();i++){
								tmp_arr.put( String.valueOf(D1.getMyRotation(i)));
							}
							if(tmp_arr.length() > 0)
								save_strategy.put("D1_Rotation", tmp_arr);

							tmp_arr = new JSONArray();
							for(int i=0;i<D2.getRotation_size();i++){
								tmp_arr.put( String.valueOf(D2.getMyRotation(i)));
							}
							if(tmp_arr.length() > 0)
								save_strategy.put("D2_Rotation", tmp_arr);

							tmp_arr = new JSONArray();
							for(int i=0;i<D3.getRotation_size();i++){
								tmp_arr.put( String.valueOf(D3.getMyRotation(i)));
							}
							if(tmp_arr.length() > 0)
								save_strategy.put("D3_Rotation", tmp_arr);

							tmp_arr = new JSONArray();
							for(int i=0;i<D4.getRotation_size();i++){
								tmp_arr.put( String.valueOf(D4.getMyRotation(i)));
							}
							if(tmp_arr.length() > 0)
								save_strategy.put("D4_Rotation", tmp_arr);

							tmp_arr = new JSONArray();
							for(int i=0;i<D5.getRotation_size();i++){
								tmp_arr.put( String.valueOf(D5.getMyRotation(i)));
							}
							if(tmp_arr.length() > 0)
								save_strategy.put("D5_Rotation", tmp_arr);
							//endregion

						} catch(JSONException e){
							e.printStackTrace();
						}

						JSONArray runline_array = new JSONArray();
						try {
							for(int i = 0; i< runBags.size(); i++){
								JSONObject tmp_runbag = new JSONObject();

								tmp_runbag.put("start_time", String.valueOf(runBags.get(i).getStartTime()));
								tmp_runbag.put("duration", String.valueOf(runBags.get(i).getDuration()));
								tmp_runbag.put("handler", runBags.get(i).getHandler());
								tmp_runbag.put("road_start", String.valueOf(runBags.get(i).getRoadStart()));
								tmp_runbag.put("road_end", String.valueOf(runBags.get(i).getRoadEnd()));
								tmp_runbag.put("rate", String.valueOf(runBags.get(i).getRate()));
								tmp_runbag.put("ball_num", String.valueOf(runBags.get(i).getBallNum()));

								tmp_runbag.put("path_type", String.valueOf(runBags.get(i).getPathType()));
								tmp_runbag.put("screen_angle", String.valueOf(runBags.get(i).getScreenAngle()));
								tmp_runbag.put("dribble_angle", String.valueOf(runBags.get(i).getDribbleAngle()));
								tmp_runbag.put("dribble_length", String.valueOf(runBags.get(i).getDribbleLength()));

								tmp_runbag.put("dribble_start_x", String.valueOf(runBags.get(i).getDribbleStartX()));
								tmp_runbag.put("dribble_start_y", String.valueOf(runBags.get(i).getDribbleStartY()));

								runline_array.put(tmp_runbag);
							}
							save_strategy.put("Runline", runline_array);
						} catch (JSONException e) {
							e.printStackTrace();
						}

						//endregion

						// region 2018.07.12 JSON寫檔
						try{
							Writer output = null;
							output = new BufferedWriter(new FileWriter(outFile));
							output.write(save_strategy.toString());
							output.close();

						}catch(Exception e){

						}

						//endregion

                        //region Version.1 存檔 (已註解)
/*
                        //還要存Initial_Position
                        if (P1_Initial_Position_x != -1) {
                            P_Initial_Position.add(P1_Initial_Position_x);
                            P_Initial_Position.add(P1_Initial_Position_y);
                            tmp += "P1_Initial_Position\n";
                            tmp += Integer.toString(P1_Initial_Position_x) + "\n";
                            tmp += Integer.toString(P1_Initial_Position_y) + "\n";
                            tmp += "---\n";
                        }
                        if (P2_Initial_Position_x != -1) {
                            P_Initial_Position.add(P2_Initial_Position_x);
                            P_Initial_Position.add(P2_Initial_Position_y);
                            tmp += "P2_Initial_Position\n";
                            tmp += Integer.toString(P2_Initial_Position_x) + "\n";
                            tmp += Integer.toString(P2_Initial_Position_y) + "\n";
                            tmp += "---\n";
                        }
                        if (P3_Initial_Position_x != -1) {
                            P_Initial_Position.add(P3_Initial_Position_x);
                            P_Initial_Position.add(P3_Initial_Position_y);
                            tmp += "P3_Initial_Position\n";
                            tmp += Integer.toString(P3_Initial_Position_x) + "\n";
                            tmp += Integer.toString(P3_Initial_Position_y) + "\n";
                            tmp += "---\n";
                        }
                        if (P4_Initial_Position_x != -1) {
                            P_Initial_Position.add(P4_Initial_Position_x);
                            P_Initial_Position.add(P4_Initial_Position_y);
                            tmp += "P4_Initial_Position\n";
                            tmp += Integer.toString(P4_Initial_Position_x) + "\n";
                            tmp += Integer.toString(P4_Initial_Position_y) + "\n";
                            tmp += "---\n";
                        }
                        if (P5_Initial_Position_x != -1) {
                            P_Initial_Position.add(P5_Initial_Position_x);
                            P_Initial_Position.add(P5_Initial_Position_y);
                            tmp += "P5_Initial_Position\n";
                            tmp += Integer.toString(P5_Initial_Position_x) + "\n";
                            tmp += Integer.toString(P5_Initial_Position_y) + "\n";
                            tmp += "---\n";
                        }

                        if (B_Initial_Position_x != -1) {
                            P_Initial_Position.add(B_Initial_Position_x);
                            P_Initial_Position.add(B_Initial_Position_y);
                            tmp += "B_Initial_Position\n";
                            tmp += Integer.toString(B_Initial_Position_x) + "\n";
                            tmp += Integer.toString(B_Initial_Position_y) + "\n";
                            tmp += "---\n";
                        }

                        if (D1_Initial_Position_x != -1) {
                            P_Initial_Position.add(D1_Initial_Position_x);
                            P_Initial_Position.add(D1_Initial_Position_y);
                            tmp += "D1_Initial_Position\n";
                            tmp += Integer.toString(D1_Initial_Position_x) + "\n";
                            tmp += Integer.toString(D1_Initial_Position_y) + "\n";
                            tmp += "---\n";
                        }
                        if (D2_Initial_Position_x != -1) {
                            P_Initial_Position.add(D2_Initial_Position_x);
                            P_Initial_Position.add(D2_Initial_Position_y);
                            tmp += "D2_Initial_Position\n";
                            tmp += Integer.toString(D2_Initial_Position_x) + "\n";
                            tmp += Integer.toString(D2_Initial_Position_y) + "\n";
                            tmp += "---\n";
                        }
                        if (D3_Initial_Position_x != -1) {
                            P_Initial_Position.add(D3_Initial_Position_x);
                            P_Initial_Position.add(D3_Initial_Position_y);
                            tmp += "D3_Initial_Position\n";
                            tmp += Integer.toString(D3_Initial_Position_x) + "\n";
                            tmp += Integer.toString(D3_Initial_Position_y) + "\n";
                            tmp += "---\n";
                        }
                        if (D4_Initial_Position_x != -1) {
                            P_Initial_Position.add(D4_Initial_Position_x);
                            P_Initial_Position.add(D4_Initial_Position_y);
                            tmp += "D4_Initial_Position\n";
                            tmp += Integer.toString(D4_Initial_Position_x) + "\n";
                            tmp += Integer.toString(D4_Initial_Position_y) + "\n";
                            tmp += "---\n";
                        }
                        if (D5_Initial_Position_x != -1) {
                            P_Initial_Position.add(D5_Initial_Position_x);
                            P_Initial_Position.add(D5_Initial_Position_y);
                            tmp += "D5_Initial_Position\n";
                            tmp += Integer.toString(D5_Initial_Position_x) + "\n";
                            tmp += Integer.toString(D5_Initial_Position_y) + "\n";
                            tmp += "---\n";
                        }


                        if (initial_ball_num != 0) {
                            tmp += "initial_ball_num\n";
                            tmp += Integer.toString(initial_ball_num) + "\n";
                            tmp += "---\n";
                        }

                        if (Initial_P1_rotate != -1) {
                            P_Initial_Rotate.add(Initial_P1_rotate);
                            tmp += "Initial_P1_rotate\n";
                            tmp += Integer.toString(Initial_P1_rotate) + "\n";
                            tmp += "---\n";
                        }
                        if (Initial_P2_rotate != -1) {
                            P_Initial_Rotate.add(Initial_P2_rotate);
                            tmp += "Initial_P2_rotate\n";
                            tmp += Integer.toString(Initial_P2_rotate) + "\n";
                            tmp += "---\n";
                        }
                        if (Initial_P3_rotate != -1) {
                            P_Initial_Rotate.add(Initial_P3_rotate);
                            tmp += "Initial_P3_rotate\n";
                            tmp += Integer.toString(Initial_P3_rotate) + "\n";
                            tmp += "---\n";
                        }
                        if (Initial_P4_rotate != -1) {
                            P_Initial_Rotate.add(Initial_P4_rotate);
                            tmp += "Initial_P4_rotate\n";
                            tmp += Integer.toString(Initial_P4_rotate) + "\n";
                            tmp += "---\n";
                        }
                        if (Initial_P5_rotate != -1) {
                            P_Initial_Rotate.add(Initial_P5_rotate);
                            tmp += "Initial_P5_rotate\n";
                            tmp += Integer.toString(Initial_P5_rotate) + "\n";
                            tmp += "---\n";
                        }


                        if (Initial_D1_rotate != -1) {
                            P_Initial_Rotate.add(Initial_D1_rotate);
                            tmp += "Initial_D1_rotate\n";
                            tmp += Integer.toString(Initial_D1_rotate) + "\n";
                            tmp += "---\n";
                        }
                        if (Initial_D2_rotate != -1) {
                            P_Initial_Rotate.add(Initial_D2_rotate);
                            tmp += "Initial_D2_rotate\n";
                            tmp += Integer.toString(Initial_D2_rotate) + "\n";
                            tmp += "---\n";
                        }
                        if (Initial_D3_rotate != -1) {
                            P_Initial_Rotate.add(Initial_D3_rotate);
                            tmp += "Initial_D3_rotate\n";
                            tmp += Integer.toString(Initial_D3_rotate) + "\n";
                            tmp += "---\n";
                        }
                        if (Initial_D4_rotate != -1) {
                            P_Initial_Rotate.add(Initial_D4_rotate);
                            tmp += "Initial_D4_rotate\n";
                            tmp += Integer.toString(Initial_D4_rotate) + "\n";
                            tmp += "---\n";
                        }
                        if (Initial_D5_rotate != -1) {
                            P_Initial_Rotate.add(Initial_D5_rotate);
                            tmp += "Initial_D5_rotate\n";
                            tmp += Integer.toString(Initial_D5_rotate) + "\n";
                            tmp += "---\n";
                        }

                        //?sroad
                        if (P1.getRoadSize() != 0) {
                            tmp += "P1\n";
                            for (int i = 0; i < P1.getRoadSize(); i++) {
                                tmp += Integer.toString(P1.handleGetRoad(i)) + "\n";
                            }
                            tmp += "---\n";
                        }

                        if (P2.getRoadSize() != 0) {
                            tmp += "P2\n";
                            for (int i = 0; i < P2.getRoadSize(); i++) {
                                tmp += Integer.toString(P2.handleGetRoad(i)) + "\n";
                            }
                            tmp += "---\n";
                        }
                        if (P3.getRoadSize() != 0) {
                            tmp += "P3\n";
                            for (int i = 0; i < P3.getRoadSize(); i++) {
                                tmp += Integer.toString(P3.handleGetRoad(i)) + "\n";
                            }
                            tmp += "---\n";
                        }
                        if (P4.getRoadSize() != 0) {
                            tmp += "P4\n";
                            for (int i = 0; i < P4.getRoadSize(); i++) {
                                tmp += Integer.toString(P4.handleGetRoad(i)) + "\n";
                            }
                            tmp += "---\n";
                        }
                        if (P5.getRoadSize() != 0) {
                            tmp += "P5\n";
                            for (int i = 0; i < P5.getRoadSize(); i++) {
                                tmp += Integer.toString(P5.handleGetRoad(i)) + "\n";
                            }
                            tmp += "---\n";
                        }
                        if (B.getRoadSize() != 0) {
                            tmp += "B\n";
                            for (int i = 0; i < B.getRoadSize(); i++) {
                                tmp += Integer.toString(B.handleGetRoad(i)) + "\n";
                            }
                            tmp += "---\n";
                        }

                        if (D1.getRoadSize() != 0) {
                            tmp += "D1\n";
                            for (int i = 0; i < D1.getRoadSize(); i++) {
                                tmp += Integer.toString(D1.handleGetRoad(i)) + "\n";
                            }
                            tmp += "---\n";
                        }

                        if (D2.getRoadSize() != 0) {
                            tmp += "D2\n";
                            for (int i = 0; i < D2.getRoadSize(); i++) {
                                tmp += Integer.toString(D2.handleGetRoad(i)) + "\n";
                            }
                            tmp += "---\n";
                        }
                        if (D3.getRoadSize() != 0) {
                            tmp += "D3\n";
                            for (int i = 0; i < D3.getRoadSize(); i++) {
                                tmp += Integer.toString(D3.handleGetRoad(i)) + "\n";
                            }
                            tmp += "---\n";
                        }
                        if (D4.getRoadSize() != 0) {
                            tmp += "D4\n";
                            for (int i = 0; i < D4.getRoadSize(); i++) {
                                tmp += Integer.toString(D4.handleGetRoad(i)) + "\n";
                            }
                            tmp += "---\n";
                        }
                        if (D5.getRoadSize() != 0) {
                            tmp += "D5\n";
                            for (int i = 0; i < D5.getRoadSize(); i++) {
                                tmp += Integer.toString(D5.handleGetRoad(i)) + "\n";
                            }
                            tmp += "---\n";
                        }

                        //Rotation
                        if (P1.getRotation_size() != 0) {
                            tmp += "P1_rotation\n";
                            for (int i = 0; i < P1.getRotation_size(); i++) {
                                tmp += Integer.toString(P1.getMyRotation(i)) + "\n";
                            }
                            tmp += "---\n";
                        }

                        if (P2.getRotation_size() != 0) {
                            tmp += "P2_rotation\n";
                            for (int i = 0; i < P2.getRotation_size(); i++) {
                                tmp += Integer.toString(P2.getMyRotation(i)) + "\n";
                            }
                            tmp += "---\n";
                        }
                        if (P3.getRotation_size() != 0) {
                            tmp += "P3_rotation\n";
                            for (int i = 0; i < P3.getRotation_size(); i++) {
                                tmp += Integer.toString(P3.getMyRotation(i)) + "\n";
                            }
                            tmp += "---\n";
                        }
                        if (P4.getRotation_size() != 0) {
                            tmp += "P4_rotation\n";
                            for (int i = 0; i < P4.getRotation_size(); i++) {
                                tmp += Integer.toString(P4.getMyRotation(i)) + "\n";
                            }
                            tmp += "---\n";
                        }
                        if (P5.getRotation_size() != 0) {
                            tmp += "P5_rotation\n";
                            for (int i = 0; i < P5.getRotation_size(); i++) {
                                tmp += Integer.toString(P5.getMyRotation(i)) + "\n";
                            }
                            tmp += "---\n";
                        }

                        if (D1.getRotation_size() != 0) {
                            tmp += "D1_rotation\n";
                            for (int i = 0; i < D1.getRotation_size(); i++) {
                                tmp += Integer.toString(D1.getMyRotation(i)) + "\n";
                            }
                            tmp += "---\n";
                        }

                        if (D2.getRotation_size() != 0) {
                            tmp += "D2_rotation\n";
                            for (int i = 0; i < D2.getRotation_size(); i++) {
                                tmp += Integer.toString(D2.getMyRotation(i)) + "\n";
                            }
                            tmp += "---\n";
                        }
                        if (D3.getRotation_size() != 0) {
                            tmp += "D3_rotation\n";
                            for (int i = 0; i < D3.getRotation_size(); i++) {
                                tmp += Integer.toString(D3.getMyRotation(i)) + "\n";
                            }
                            tmp += "---\n";
                        }
                        if (D4.getRotation_size() != 0) {
                            tmp += "D4_rotation\n";
                            for (int i = 0; i < D4.getRotation_size(); i++) {
                                tmp += Integer.toString(D4.getMyRotation(i)) + "\n";
                            }
                            tmp += "---\n";
                        }
                        if (D5.getRotation_size() != 0) {
                            tmp += "D5_rotation\n";
                            for (int i = 0; i < D5.getRotation_size(); i++) {
                                tmp += Integer.toString(D5.getMyRotation(i)) + "\n";
                            }
                            tmp += "---\n";
                        }


                        //sRunBag
                        tmp += "RunBag\n";
                        for (int i = 0; i < RunLine.size(); i++) {
                            tmp += RunLine.get(i).getRunBagInfo() + "\n";
                            tmp += "---\n";
                        }
                        //writeToFile(outFile, tmp);
*/
                        //endregion ()

                        dialog.dismiss();

                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        saveOkButton.setBackgroundResource(R.drawable.btn_save);
                        dialog.dismiss();
                    }
                    return true;
                }
        	});

    }
	
	public void writeToFile(File fout, String data) {  
	    FileOutputStream osw = null;
	    try {
	        osw = new FileOutputStream(fout);
	        osw.write(data.getBytes());
	        osw.flush();
	    } catch (Exception e) {
	        ;
	    } finally {
	        try {
	            osw.close();
	        } catch (Exception e) {
	            ;
	        }
	    }
	}
    
	public void load_dialog(){
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View myv = inflater.inflate(R.layout.load_from_strategy_layout, null);//把xml當成view來用，就能加入alert dialog裡面了

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(myv);
        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setLayout(1000,1200);

		firstTimeQuery = true;
		FilenameFilter namefilter =new FilenameFilter(){
            private String[] filter={
                    "json"
            };
            @Override
            public boolean accept(File dir, String filename){
                for(int i=0;i<filter.length;i++){
                    if(filename.indexOf(filter[i])!=-1)
                        return true;
                }
                return false;
            }
        };

		File dir = getActivity().getBaseContext().getExternalFilesDir(null);
        File[] fileList = dir.listFiles(namefilter);

        final List<List<String>> tactic_in_category = new ArrayList<List<String>>();
        final List<List<String>> tactic_id_in_category = new ArrayList<List<String>>();
        for(int i=0 ; i<15 ; i++){
			tactic_in_category.add(new ArrayList<String>());
			tactic_id_in_category.add(new ArrayList<String>());
		}

        String[] list = new String[fileList.length];
        String[] tactic_name = new String[fileList.length];
        int[] category_list = new int[fileList.length];

        for(int i=0 ; i<list.length ; i++){
        	//fileList 在目錄底下全部的fileName
            list[i] = fileList[i].getName();

            // Tactic file name : [Tacitc name]_[Tactic category Id].txt
            //      tokens[0] = [Tactic name]
            //      tokens[1] = [Tactic category Id].txt
            //              //ex: 5.txt, 10.txt

            String[] tokens = list[i].split("_");
            tactic_name[i] = tokens[0];

            if(tokens[1].length() == 6){
                category_list[i] = Integer.valueOf(tokens[1].substring(0,1));
            }
            else if (tokens[1].length() == 7){
                category_list[i] = Integer.valueOf(tokens[1].substring(0,2));
            }

			ArrayList target_category = (ArrayList) tactic_in_category.get(category_list[i]);
			target_category.add(tactic_name[i]);

        }

		for(int i=0;i<tactic_in_category.size();i++){

        	ArrayList per_category = (ArrayList) tactic_id_in_category.get(i);
        	for(int j=0;j<tactic_in_category.get(i).size();j++){
				per_category.add(Integer.valueOf(j+1).toString());
			}
		}

        // Set the default category(Category 0) tactic lists
        List<String> liste = new ArrayList<String>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(myv.getContext(), android.R.layout.simple_list_item_1, liste){
        	@Override
			public View getView(int postion, View convertView, ViewGroup parent){
				TextView tv = (TextView) super.getView(postion, convertView, parent);
				tv.setGravity(Gravity.CENTER);

        		return tv;
			}
		};

        final ListView listView_in_load = (ListView)myv.findViewById(R.id.listView_in_load);

		// Set OnItemSelectListener to upate ListView when select new items.
        Spinner spinner_in_load = (Spinner)myv.findViewById(R.id.category_spinner_in_load);
        spinner_in_load.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            //點選了Spinner上面的某個選項(分類)
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
				List<String> tmpList = tactic_in_category.get(new Long(l).intValue());

				TextView testSize = (TextView)myv.findViewById((R.id.testTextView_size));
				testSize.setText(Integer.valueOf(tmpList.size()).toString());

				Log.d("warning", String.valueOf(tmpList.size()));
				selectCategoryId = new Long(l).intValue();
				adapter.clear();
				adapter.addAll(tmpList);
				adapter.notifyDataSetChanged();


				listView_in_load.setAdapter(adapter);
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {

			}
		});

        listView_in_load.setClickable(true);
        listView_in_load.setOnItemClickListener(new ListView.OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				//Log.d("warning", new Long(l).toString());
				//先清除之前的record
				clearPaint();
				clear_record();

				File dir = getActivity().getBaseContext().getExternalFilesDir(null);
				String current_select_tactic = tactic_in_category.get(selectCategoryId).get(new Long(l).intValue()) + "_" + String.valueOf(selectCategoryId)+".json";
				selectTacticName = tactic_in_category.get(selectCategoryId).get(new Long(l).intValue());
				File inFile = new File(dir, current_select_tactic);
				readStrategy(inFile);
				playerMoveToStartPosition();
				//TODO:應該要改成可以知道現在是使用哪種defender資料
				if(d1InitialPositionX == -1)
					hasQueryDefenderFromServer = false;
				else
					hasQueryDefenderFromServer = true;

				dialog.dismiss();
			}
		});

		/*
        final String[] strategies = list;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		 builder.setItems(strategies, new DialogInterface.OnClickListener(){
	         @Override
             //只要你在onClick處理事件內，使用which參數，就可以知道按下陣列裡的哪一個了
	         public void onClick(DialogInterface dialog, int which) {
                //先清除之前的record
	        	clear_paint();
	        	clear_record();
                //取得內部儲存體擺放檔案的目錄
                //預設擺放目錄為 /data/data/[package.name]/
     			File dir = getActivity().getBaseContext().getExternalFilesDir(null);
     			Log.d("debug", "Environment = "+dir.toString());
	        	File inFile = null;
	        	inFile = new File(dir, strategies[which]);
	        	readStrategy(inFile);
     			Player_move_to_start_position();
	          }//OnClick_END
	    });

        AlertDialog about_dialog = builder.create();
        about_dialog.show();
        */
	}
 	
	private String readFromFile(File fin) {  
	    StringBuilder data = new StringBuilder();
	    BufferedReader reader = null;
	    try {
	        reader = new BufferedReader(new FileReader(fin));
	        String line;
	        while ((line = reader.readLine()) != null) {
	            data.append(line);
	            data.append("\n");
	        }
	    } catch (Exception e) {
	        ;
	    } finally {
	        try {
	            reader.close();
	        } catch (Exception e) {
	            ;
	        }
	    }
	    return data.toString();
	}

	private void readStrategy(File fin){
		//region 2018.07.12 讀取json檔
		FileInputStream stream;
		try {
			stream = new FileInputStream(fin);
			String jsonStr = null;
			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

			jsonStr = Charset.defaultCharset().decode(bb).toString();

			pInitialRotate.clear();
			pInitialPosition.clear();

			JSONObject save_tactic_json = new JSONObject(jsonStr);
			initialBallNum = Integer.valueOf(save_tactic_json.getString("Initial_ball_holder"));

			//region Load Initial Position
			JSONArray initial_array = save_tactic_json.getJSONArray("Initial_Position");
			p1InitialPositionX = Integer.valueOf(initial_array.getString(0));
			p1InitialPositionY = Integer.valueOf(initial_array.getString(1));
			p2InitialPositionX = Integer.valueOf(initial_array.getString(2));
			p2InitialPositionY = Integer.valueOf(initial_array.getString(3));
			p3InitialPositionX = Integer.valueOf(initial_array.getString(4));
			p3InitialPositionY = Integer.valueOf(initial_array.getString(5));
			p4InitialPositionX = Integer.valueOf(initial_array.getString(6));
			p4InitialPositionY = Integer.valueOf(initial_array.getString(7));
			p5InitialPositionX = Integer.valueOf(initial_array.getString(8));
			p5InitialPositionY = Integer.valueOf(initial_array.getString(9));

			d1InitialPositionX = Integer.valueOf(initial_array.getString(10));
			d1InitialPositionY = Integer.valueOf(initial_array.getString(11));
			d2InitialPositionX = Integer.valueOf(initial_array.getString(12));
			d2InitialPositionY = Integer.valueOf(initial_array.getString(13));
			d3InitialPositionX = Integer.valueOf(initial_array.getString(14));
			d3InitialPositionY = Integer.valueOf(initial_array.getString(15));
			d4InitialPositionX = Integer.valueOf(initial_array.getString(16));
			d4InitialPositionY = Integer.valueOf(initial_array.getString(17));
			d5InitialPositionX = Integer.valueOf(initial_array.getString(18));
			d5InitialPositionY = Integer.valueOf(initial_array.getString(19));

			bInitialPositionX = Integer.valueOf(initial_array.getString(20));
			bInitialPositionY = Integer.valueOf(initial_array.getString(21));
			//endregion

			//region Load Initial Rotation
			JSONArray initial_array_rotation = save_tactic_json.getJSONArray("Initial_Rotation");
			initialP1Rotate = Integer.valueOf(initial_array_rotation.getString(0));
			initialP2Rotate = Integer.valueOf(initial_array_rotation.getString(1));
			initialP3Rotate = Integer.valueOf(initial_array_rotation.getString(2));
			initialP4Rotate = Integer.valueOf(initial_array_rotation.getString(3));
			initialP5Rotate = Integer.valueOf(initial_array_rotation.getString(4));

			initialD1Rotate = Integer.valueOf(initial_array_rotation.getString(5));
			initialD2Rotate = Integer.valueOf(initial_array_rotation.getString(6));
			initialD3Rotate = Integer.valueOf(initial_array_rotation.getString(7));
			initialD4Rotate = Integer.valueOf(initial_array_rotation.getString(8));
			initialD5Rotate = Integer.valueOf(initial_array_rotation.getString(9));
			//endregion

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		//region Load Player road sequence
		try{
			stream = new FileInputStream(fin);
			String jsonStr = null;
			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

			jsonStr = Charset.defaultCharset().decode(bb).toString();

			JSONObject save_tactic_json = new JSONObject(jsonStr);
			JSONArray tmp_arr = save_tactic_json.getJSONArray("P1");
			if(tmp_arr != null) {
				for (int i = 0; i < tmp_arr.length(); i++) {
					P1.setRoad(Integer.valueOf(tmp_arr.getString(i)));
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		try{
			stream = new FileInputStream(fin);
			String jsonStr = null;
			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

			jsonStr = Charset.defaultCharset().decode(bb).toString();

			JSONObject save_tactic_json = new JSONObject(jsonStr);
			JSONArray tmp_arr = save_tactic_json.getJSONArray("P2");
			if(tmp_arr != null) {
				for (int i = 0; i < tmp_arr.length(); i++) {
					P2.setRoad(Integer.valueOf(tmp_arr.getString(i)));
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		try{
			stream = new FileInputStream(fin);
			String jsonStr = null;
			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

			jsonStr = Charset.defaultCharset().decode(bb).toString();

			JSONObject save_tactic_json = new JSONObject(jsonStr);
			JSONArray tmp_arr = save_tactic_json.getJSONArray("P3");
			if(tmp_arr != null) {
				for (int i = 0; i < tmp_arr.length(); i++) {
					P3.setRoad(Integer.valueOf(tmp_arr.getString(i)));
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		try{
			stream = new FileInputStream(fin);
			String jsonStr = null;
			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

			jsonStr = Charset.defaultCharset().decode(bb).toString();

			JSONObject save_tactic_json = new JSONObject(jsonStr);
			JSONArray tmp_arr = save_tactic_json.getJSONArray("P4");
			if(tmp_arr != null) {
				for (int i = 0; i < tmp_arr.length(); i++) {
					P4.setRoad(Integer.valueOf(tmp_arr.getString(i)));
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		try{
			stream = new FileInputStream(fin);
			String jsonStr = null;
			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

			jsonStr = Charset.defaultCharset().decode(bb).toString();

			JSONObject save_tactic_json = new JSONObject(jsonStr);
			JSONArray tmp_arr = save_tactic_json.getJSONArray("P5");
			if(tmp_arr != null) {
				for (int i = 0; i < tmp_arr.length(); i++) {
					P5.setRoad(Integer.valueOf(tmp_arr.getString(i)));
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		try{
			stream = new FileInputStream(fin);
			String jsonStr = null;
			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

			jsonStr = Charset.defaultCharset().decode(bb).toString();

			JSONObject save_tactic_json = new JSONObject(jsonStr);
			JSONArray tmp_arr = save_tactic_json.getJSONArray("D1");
			if(tmp_arr != null) {
				for (int i = 0; i < tmp_arr.length(); i++) {
					D1.setRoad(Integer.valueOf(tmp_arr.getString(i)));
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		try{
			stream = new FileInputStream(fin);
			String jsonStr = null;
			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

			jsonStr = Charset.defaultCharset().decode(bb).toString();

			JSONObject save_tactic_json = new JSONObject(jsonStr);
			JSONArray tmp_arr = save_tactic_json.getJSONArray("D2");
			if(tmp_arr != null) {
				for (int i = 0; i < tmp_arr.length(); i++) {
					D2.setRoad(Integer.valueOf(tmp_arr.getString(i)));
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		try{
			stream = new FileInputStream(fin);
			String jsonStr = null;
			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

			jsonStr = Charset.defaultCharset().decode(bb).toString();

			JSONObject save_tactic_json = new JSONObject(jsonStr);
			JSONArray tmp_arr = save_tactic_json.getJSONArray("D3");
			if(tmp_arr != null) {
				for (int i = 0; i < tmp_arr.length(); i++) {
					D3.setRoad(Integer.valueOf(tmp_arr.getString(i)));
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		try{
			stream = new FileInputStream(fin);
			String jsonStr = null;
			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

			jsonStr = Charset.defaultCharset().decode(bb).toString();

			JSONObject save_tactic_json = new JSONObject(jsonStr);
			JSONArray tmp_arr = save_tactic_json.getJSONArray("D4");
			if(tmp_arr != null) {
				for (int i = 0; i < tmp_arr.length(); i++) {
					D4.setRoad(Integer.valueOf(tmp_arr.getString(i)));
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		try{
			stream = new FileInputStream(fin);
			String jsonStr = null;
			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

			jsonStr = Charset.defaultCharset().decode(bb).toString();

			JSONObject save_tactic_json = new JSONObject(jsonStr);
			JSONArray tmp_arr = save_tactic_json.getJSONArray("D5");
			if(tmp_arr != null) {
				for (int i = 0; i < tmp_arr.length(); i++) {
					D5.setRoad(Integer.valueOf(tmp_arr.getString(i)));
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		try{
			stream = new FileInputStream(fin);
			String jsonStr = null;
			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

			jsonStr = Charset.defaultCharset().decode(bb).toString();

			JSONObject save_tactic_json = new JSONObject(jsonStr);
			JSONArray tmp_arr = save_tactic_json.getJSONArray("B");
			if(tmp_arr != null) {
				for (int i = 0; i < tmp_arr.length(); i++) {
					B.setRoad(Integer.valueOf(tmp_arr.getString(i)));
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		//endregion

		//region Load Player Rotation
		try{
			stream = new FileInputStream(fin);
			String jsonStr = null;
			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

			jsonStr = Charset.defaultCharset().decode(bb).toString();

			JSONObject save_tactic_json = new JSONObject(jsonStr);
			JSONArray tmp_arr = save_tactic_json.getJSONArray("P1_Rotation");
			if(tmp_arr != null) {
				for (int i = 0; i < tmp_arr.length(); i++) {
					P1.setMyRotation(Integer.valueOf(tmp_arr.getString(i)));
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		try{
			stream = new FileInputStream(fin);
			String jsonStr = null;
			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

			jsonStr = Charset.defaultCharset().decode(bb).toString();

			JSONObject save_tactic_json = new JSONObject(jsonStr);
			JSONArray tmp_arr = save_tactic_json.getJSONArray("P2_Rotation");
			if(tmp_arr != null) {
				for (int i = 0; i < tmp_arr.length(); i++) {
					P2.setMyRotation(Integer.valueOf(tmp_arr.getString(i)));
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		try{
			stream = new FileInputStream(fin);
			String jsonStr = null;
			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

			jsonStr = Charset.defaultCharset().decode(bb).toString();

			JSONObject save_tactic_json = new JSONObject(jsonStr);
			JSONArray tmp_arr = save_tactic_json.getJSONArray("P3_Rotation");
			if(tmp_arr != null) {
				for (int i = 0; i < tmp_arr.length(); i++) {
					P3.setMyRotation(Integer.valueOf(tmp_arr.getString(i)));
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		try{
			stream = new FileInputStream(fin);
			String jsonStr = null;
			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

			jsonStr = Charset.defaultCharset().decode(bb).toString();

			JSONObject save_tactic_json = new JSONObject(jsonStr);
			JSONArray tmp_arr = save_tactic_json.getJSONArray("P4_Rotation");
			if(tmp_arr != null) {
				for (int i = 0; i < tmp_arr.length(); i++) {
					P4.setMyRotation(Integer.valueOf(tmp_arr.getString(i)));
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		try{
			stream = new FileInputStream(fin);
			String jsonStr = null;
			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

			jsonStr = Charset.defaultCharset().decode(bb).toString();

			JSONObject save_tactic_json = new JSONObject(jsonStr);
			JSONArray tmp_arr = save_tactic_json.getJSONArray("P5_Rotation");
			if(tmp_arr != null) {
				for (int i = 0; i < tmp_arr.length(); i++) {
					P5.setMyRotation(Integer.valueOf(tmp_arr.getString(i)));
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		try{
			stream = new FileInputStream(fin);
			String jsonStr = null;
			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

			jsonStr = Charset.defaultCharset().decode(bb).toString();

			JSONObject save_tactic_json = new JSONObject(jsonStr);
			JSONArray tmp_arr = save_tactic_json.getJSONArray("D1_Rotation");
			if(tmp_arr != null) {
				for (int i = 0; i < tmp_arr.length(); i++) {
					D1.setMyRotation(Integer.valueOf(tmp_arr.getString(i)));
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		try{
			stream = new FileInputStream(fin);
			String jsonStr = null;
			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

			jsonStr = Charset.defaultCharset().decode(bb).toString();

			JSONObject save_tactic_json = new JSONObject(jsonStr);
			JSONArray tmp_arr = save_tactic_json.getJSONArray("D2_Rotation");
			if(tmp_arr != null) {
				for (int i = 0; i < tmp_arr.length(); i++) {
					D3.setMyRotation(Integer.valueOf(tmp_arr.getString(i)));
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		try{
			stream = new FileInputStream(fin);
			String jsonStr = null;
			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

			jsonStr = Charset.defaultCharset().decode(bb).toString();

			JSONObject save_tactic_json = new JSONObject(jsonStr);
			JSONArray tmp_arr = save_tactic_json.getJSONArray("D3_Rotation");
			if(tmp_arr != null) {
				for (int i = 0; i < tmp_arr.length(); i++) {
					D3.setMyRotation(Integer.valueOf(tmp_arr.getString(i)));
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		try{
			stream = new FileInputStream(fin);
			String jsonStr = null;
			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

			jsonStr = Charset.defaultCharset().decode(bb).toString();

			JSONObject save_tactic_json = new JSONObject(jsonStr);
			JSONArray tmp_arr = save_tactic_json.getJSONArray("D4_Rotation");
			if(tmp_arr != null) {
				for (int i = 0; i < tmp_arr.length(); i++) {
					D4.setMyRotation(Integer.valueOf(tmp_arr.getString(i)));
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		try{
			stream = new FileInputStream(fin);
			String jsonStr = null;
			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

			jsonStr = Charset.defaultCharset().decode(bb).toString();

			JSONObject save_tactic_json = new JSONObject(jsonStr);
			JSONArray tmp_arr = save_tactic_json.getJSONArray("D5_Rotation");
			if(tmp_arr != null) {
				for (int i = 0; i < tmp_arr.length(); i++) {
					D5.setMyRotation(Integer.valueOf(tmp_arr.getString(i)));
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}


		//endregion

		//region Load Runline

		try {
			stream = new FileInputStream(fin);

			String jsonStr = null;
			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

			jsonStr = Charset.defaultCharset().decode(bb).toString();

			JSONObject save_tactic_json = new JSONObject(jsonStr);
			JSONArray runLine = save_tactic_json.getJSONArray("Runline");
			TimeLine timefrag = (TimeLine) getActivity().getFragmentManager().findFragmentById(R.id.time_line);
			int load_seekbar_tmp_id=0;
			int x=0,y=0;
			for(int i=0;i<runLine.length();i++){
				JSONObject tmp_runBag = runLine.getJSONObject(i);
				RunBag tmp = new RunBag();
				tmp.setStartTime(Integer.valueOf(tmp_runBag.getString("start_time")));
				tmp.setHandler(tmp_runBag.getString("handler"));
				tmp.setRoadStart(Integer.valueOf(tmp_runBag.getString("road_start")));
				tmp.setRoadEnd(Integer.valueOf(tmp_runBag.getString("road_end")));
				tmp.setDuration(Integer.valueOf(tmp_runBag.getString("duration")));
				tmp.setBallNum(Integer.valueOf(tmp_runBag.getString("ball_num")));
				tmp.setPathType(Integer.valueOf(tmp_runBag.getString("path_type")));
				tmp.setScreenAngle(Float.valueOf(tmp_runBag.getString("screen_angle")));
				tmp.setDribbleAngle(Float.valueOf(tmp_runBag.getString("dribble_angle")));
				tmp.setDribbleLength(Float.valueOf(tmp_runBag.getString("dribble_length")));

				tmp.setDribbleStartX(Integer.valueOf(tmp_runBag.getString("dribble_start_x")));
				tmp.setDribbleStartY(Integer.valueOf(tmp_runBag.getString("dribble_start_y")));

				runBags.add(tmp);

				//region 對應每讀一個Runbag，就會自動產生對應他順序的seekbar(用來調整時間點的bar條)

				timefrag.setSeekBarId(load_seekbar_tmp_id);
				if(tmp.getHandler().equals("P1_Handle")){
					timefrag.createSeekbar(1,load_seekbar_tmp_id,tmp.getStartTime(),tmp.getDuration());
					MainWrap mainwrapfrag = (MainWrap) getActivity().getFragmentManager().findFragmentById(R.id.MainWrap_frag);

					// 在player icon所在的位置產生對應的順序標記
					x = P1.getCmpltRoad().get(tmp.getRoadEnd()-1);
					y = P1.getCmpltRoad().get(tmp.getRoadEnd());
					mainwrapfrag.create_path_num_on_court(load_seekbar_tmp_id+1, x, y,load_seekbar_tmp_id);

					// 如果這一動有要擋拆
					if(tmp.getPathType() == 1){
						mainwrapfrag.create_screen_bar(x, y, 1, tmp.getScreenAngle(), load_seekbar_tmp_id);
					}else if(tmp.getPathType() == 2){
						mainwrapfrag.create_dribble_line(tmp.getDribbleStartX(), tmp.getDribbleStartY(), 1, tmp.getDribbleAngle(), tmp.getDribbleLength(), load_seekbar_tmp_id);
					}

				}
				else if(tmp.getHandler().equals("P2_Handle")){
					timefrag.createSeekbar(2,load_seekbar_tmp_id,tmp.getStartTime(),tmp.getDuration());
					MainWrap mainwrapfrag = (MainWrap) getActivity().getFragmentManager().findFragmentById(R.id.MainWrap_frag);
					x=P2.getCmpltRoad().get(tmp.getRoadEnd()-1);
					y=P2.getCmpltRoad().get(tmp.getRoadEnd());
					mainwrapfrag.create_path_num_on_court(load_seekbar_tmp_id+1, x, y,load_seekbar_tmp_id);

					// 如果這一動有要擋拆
					if(tmp.getPathType() == 1){
						mainwrapfrag.create_screen_bar(x, y, 2, tmp.getScreenAngle(), load_seekbar_tmp_id);
					}else if(tmp.getPathType() == 2){
						mainwrapfrag.create_dribble_line(tmp.getDribbleStartX(), tmp.getDribbleStartY(), 2, tmp.getDribbleAngle(), tmp.getDribbleLength(), load_seekbar_tmp_id);
					}
				}
				else if(tmp.getHandler().equals("P3_Handle")){
					timefrag.createSeekbar(3,load_seekbar_tmp_id,tmp.getStartTime(),tmp.getDuration());
					MainWrap mainwrapfrag = (MainWrap) getActivity().getFragmentManager().findFragmentById(R.id.MainWrap_frag);
					x=P3.getCmpltRoad().get(tmp.getRoadEnd()-1);
					y=P3.getCmpltRoad().get(tmp.getRoadEnd());
					mainwrapfrag.create_path_num_on_court(load_seekbar_tmp_id+1, x, y,load_seekbar_tmp_id);

					// 如果這一動有要擋拆
					if(tmp.getPathType() == 1){
						mainwrapfrag.create_screen_bar(x, y, 3, tmp.getScreenAngle(), load_seekbar_tmp_id);
					}
					else if(tmp.getPathType() == 2){
						mainwrapfrag.create_dribble_line(tmp.getDribbleStartX(), tmp.getDribbleStartY(), 2, tmp.getDribbleAngle(), tmp.getDribbleLength(), load_seekbar_tmp_id);
					}
				}
				else if(tmp.getHandler().equals("P4_Handle")){
					timefrag.createSeekbar(4,load_seekbar_tmp_id,tmp.getStartTime(),tmp.getDuration());
					MainWrap mainwrapfrag = (MainWrap) getActivity().getFragmentManager().findFragmentById(R.id.MainWrap_frag);
					x=P4.getCmpltRoad().get(tmp.getRoadEnd()-1);
					y=P4.getCmpltRoad().get(tmp.getRoadEnd());
					mainwrapfrag.create_path_num_on_court(load_seekbar_tmp_id+1, x, y,load_seekbar_tmp_id);

					// 如果這一動有要擋拆
					if(tmp.getPathType() == 1){
						mainwrapfrag.create_screen_bar(x, y, 3, tmp.getScreenAngle(), load_seekbar_tmp_id);
					}else if(tmp.getPathType() == 2){
						mainwrapfrag.create_dribble_line(tmp.getDribbleStartX(), tmp.getDribbleStartY(), 3, tmp.getDribbleAngle(), tmp.getDribbleLength(), load_seekbar_tmp_id);
					}
				}
				else if(tmp.getHandler().equals("P5_Handle")){
					timefrag.createSeekbar(5,load_seekbar_tmp_id,tmp.getStartTime(),tmp.getDuration());
					MainWrap mainwrapfrag = (MainWrap) getActivity().getFragmentManager().findFragmentById(R.id.MainWrap_frag);
					x=P5.getCmpltRoad().get(tmp.getRoadEnd()-1);
					y=P5.getCmpltRoad().get(tmp.getRoadEnd());
					mainwrapfrag.create_path_num_on_court(load_seekbar_tmp_id+1, x, y,load_seekbar_tmp_id);

					// 如果這一動有要擋拆
					if(tmp.getPathType() == 1){
						mainwrapfrag.create_screen_bar(x, y, 4, tmp.getScreenAngle(), load_seekbar_tmp_id);
					}
					else if(tmp.getPathType() == 2){
						mainwrapfrag.create_dribble_line(tmp.getDribbleStartX(), tmp.getDribbleStartY(), 4, tmp.getDribbleAngle(), tmp.getDribbleLength(), load_seekbar_tmp_id);
					}
				}
				else if(tmp.getHandler().equals("B_Handle")){
					timefrag.createSeekbar(6,load_seekbar_tmp_id,tmp.getStartTime(),tmp.getDuration());
					MainWrap mainwrapfrag = (MainWrap) getActivity().getFragmentManager().findFragmentById(R.id.MainWrap_frag);
					x=B.getCmpltRoad().get(tmp.getRoadEnd()-1);
					y=B.getCmpltRoad().get(tmp.getRoadEnd());
					mainwrapfrag.create_path_num_on_court(load_seekbar_tmp_id+1, x, y,load_seekbar_tmp_id);
				}
				else if(tmp.getHandler().equals("D1_Handle")){
					timefrag.createSeekbar(7,load_seekbar_tmp_id,tmp.getStartTime(),tmp.getDuration());
					MainWrap mainwrapfrag = (MainWrap) getActivity().getFragmentManager().findFragmentById(R.id.MainWrap_frag);
					x=D1.getCmpltRoad().get(tmp.getRoadEnd()-1);
					y=D1.getCmpltRoad().get(tmp.getRoadEnd());
					mainwrapfrag.create_path_num_on_court(load_seekbar_tmp_id+1, x, y,load_seekbar_tmp_id);
				}
				else if(tmp.getHandler().equals("D2_Handle")){
					timefrag.createSeekbar(8,load_seekbar_tmp_id,tmp.getStartTime(),tmp.getDuration());
					MainWrap mainwrapfrag = (MainWrap) getActivity().getFragmentManager().findFragmentById(R.id.MainWrap_frag);
					x=D2.getCmpltRoad().get(tmp.getRoadEnd()-1);
					y=D2.getCmpltRoad().get(tmp.getRoadEnd());
					mainwrapfrag.create_path_num_on_court(load_seekbar_tmp_id+1, x, y,load_seekbar_tmp_id);
				}
				else if(tmp.getHandler().equals("D3_Handle")){
					timefrag.createSeekbar(9,load_seekbar_tmp_id,tmp.getStartTime(),tmp.getDuration());
					MainWrap mainwrapfrag = (MainWrap) getActivity().getFragmentManager().findFragmentById(R.id.MainWrap_frag);
					x=D3.getCmpltRoad().get(tmp.getRoadEnd()-1);
					y=D3.getCmpltRoad().get(tmp.getRoadEnd());
					mainwrapfrag.create_path_num_on_court(load_seekbar_tmp_id+1, x, y,load_seekbar_tmp_id);
				}
				else if(tmp.getHandler().equals("D4_Handle")){
					timefrag.createSeekbar(10,load_seekbar_tmp_id,tmp.getStartTime(),tmp.getDuration());
					MainWrap mainwrapfrag = (MainWrap) getActivity().getFragmentManager().findFragmentById(R.id.MainWrap_frag);
					x=D4.getCmpltRoad().get(tmp.getRoadEnd()-1);
					y=D4.getCmpltRoad().get(tmp.getRoadEnd());
					mainwrapfrag.create_path_num_on_court(load_seekbar_tmp_id+1, x, y,load_seekbar_tmp_id);
				}
				else if(tmp.getHandler().equals("D5_Handle")){
					timefrag.createSeekbar(11,load_seekbar_tmp_id,tmp.getStartTime(),tmp.getDuration());
					MainWrap mainwrapfrag = (MainWrap) getActivity().getFragmentManager().findFragmentById(R.id.MainWrap_frag);
					x=D5.getCmpltRoad().get(tmp.getRoadEnd()-1);
					y=D5.getCmpltRoad().get(tmp.getRoadEnd());
					mainwrapfrag.create_path_num_on_court(load_seekbar_tmp_id+1, x, y,load_seekbar_tmp_id);
				}
				//endregion
				load_seekbar_tmp_id++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		//endregion

		//TODO:讀取戰術後把 戰術跑動路徑也畫上去

		//endregion

		//region Version.1 讀檔 (已註解)
		/*
        //讀取 /data/data/com.myapp/檔名.txt 檔案內容
			String data = readFromFile(fin);
			String [] sData = data.split("\n");
			for(int i =0;i<sData.length;i++){
				Log.d("debug", "data["+i+"] ="+sData[i]);
			P_Initial_Rotate.clear();
			P_Initial_Position.clear();
                //先處理Initial position
				if(sData[i].equals("initial_ball_num")){
					initial_ball_num = Integer.parseInt(sData[i+1]);
					dataPacket.set_initial_ball_num(initial_ball_num);
					i=i+2;
					Log.d("debug", "initial_ball_num saved.");
				}
				if(sData[i].equals("Initial_P1_rotate")){
					Initial_P1_rotate=Integer.parseInt(sData[i+1]);
					P_Initial_Rotate.add(Initial_P1_rotate);
					i=i+2;
					Log.d("debug", "Initial_P1_rotate saved.");
				}
				if(sData[i].equals("Initial_P2_rotate")){
					Initial_P2_rotate=Integer.parseInt(sData[i+1]);
					P_Initial_Rotate.add(Initial_P2_rotate);
					i=i+2;
					Log.d("debug", "Initial_P2_rotate saved.");
				}
				if(sData[i].equals("Initial_P3_rotate")){
					Initial_P3_rotate=Integer.parseInt(sData[i+1]);
					P_Initial_Rotate.add(Initial_P3_rotate);
					i=i+2;
					Log.d("debug", "Initial_P3_rotate saved.");
				}
				if(sData[i].equals("Initial_P4_rotate")){
					Initial_P4_rotate=Integer.parseInt(sData[i+1]);
					P_Initial_Rotate.add(Initial_P4_rotate);
					i=i+2;
					Log.d("debug", "Initial_P4_rotate saved.");
				}
				if(sData[i].equals("Initial_P5_rotate")){
					Initial_P5_rotate=Integer.parseInt(sData[i+1]);
					P_Initial_Rotate.add(Initial_P5_rotate);
					i=i+2;
					Log.d("debug", "Initial_P5_rotate saved.");
				}
				

				if(sData[i].equals("Initial_D1_rotate")){
					Initial_D1_rotate=Integer.parseInt(sData[i+1]);
					P_Initial_Rotate.add(Initial_D1_rotate);
					i=i+2;
					Log.d("debug", "Initial_D1_rotate saved.");
				}
				if(sData[i].equals("Initial_D2_rotate")){
					Initial_D2_rotate=Integer.parseInt(sData[i+1]);
					P_Initial_Rotate.add(Initial_D2_rotate);
					i=i+2;
					Log.d("debug", "Initial_D2_rotate saved.");
				}
				if(sData[i].equals("Initial_D3_rotate")){
					Initial_D3_rotate=Integer.parseInt(sData[i+1]);
					P_Initial_Rotate.add(Initial_D3_rotate);
					i=i+2;
					Log.d("debug", "Initial_D3_rotate saved.");
				}
				if(sData[i].equals("Initial_D4_rotate")){
					Initial_D4_rotate=Integer.parseInt(sData[i+1]);
					P_Initial_Rotate.add(Initial_D4_rotate);
					i=i+2;
					Log.d("debug", "Initial_D4_rotate saved.");
				}
				if(sData[i].equals("Initial_D5_rotate")){
					Initial_D5_rotate=Integer.parseInt(sData[i+1]);
					P_Initial_Rotate.add(Initial_D5_rotate);
					i=i+2;
					Log.d("debug", "Initial_D5_rotate saved.");
				}
				
				
				if(sData[i].equals("P1_Initial_Position")){
					P1_Initial_Position_x=Integer.parseInt(sData[i+1]);
					P1_Initial_Position_y=Integer.parseInt(sData[i+2]);
					P_Initial_Position.add(P1_Initial_Position_x);
					P_Initial_Position.add(P1_Initial_Position_y);
					i=i+3;
					Log.d("debug", "P1 initial saved.");
				}
				if(sData[i].equals("P2_Initial_Position")){
					P2_Initial_Position_x=Integer.parseInt(sData[i+1]);
					P2_Initial_Position_y=Integer.parseInt(sData[i+2]);
					P_Initial_Position.add(P2_Initial_Position_x);
					P_Initial_Position.add(P2_Initial_Position_y);
					i=i+3;
					Log.d("debug", "P2 initial saved.");
				}
				if(sData[i].equals("P3_Initial_Position")){
					P3_Initial_Position_x=Integer.parseInt(sData[i+1]);
					P3_Initial_Position_y=Integer.parseInt(sData[i+2]);
					P_Initial_Position.add(P3_Initial_Position_x);
					P_Initial_Position.add(P3_Initial_Position_y);
					i=i+3;
					Log.d("debug", "P3 initial saved.");
				}
				if(sData[i].equals("P4_Initial_Position")){
					P4_Initial_Position_x=Integer.parseInt(sData[i+1]);
					P4_Initial_Position_y=Integer.parseInt(sData[i+2]);
					P_Initial_Position.add(P4_Initial_Position_x);
					P_Initial_Position.add(P4_Initial_Position_y);
					i=i+3;
					Log.d("debug", "P4 initial saved.");
				}
				if(sData[i].equals("P5_Initial_Position")){
					P5_Initial_Position_x=Integer.parseInt(sData[i+1]);
					P5_Initial_Position_y=Integer.parseInt(sData[i+2]);
					P_Initial_Position.add(P5_Initial_Position_x);
					P_Initial_Position.add(P5_Initial_Position_y);
					i=i+3;
					Log.d("debug", "P5 initial saved.");
				}
				if(sData[i].equals("B_Initial_Position")){
					B_Initial_Position_x=Integer.parseInt(sData[i+1]);
					B_Initial_Position_y=Integer.parseInt(sData[i+2]);
					P_Initial_Position.add(B_Initial_Position_x);
					P_Initial_Position.add(B_Initial_Position_y);
					i=i+3;
					Log.d("debug", "B initial saved.");
				}
				

				if(sData[i].equals("D1_Initial_Position")){
					D1_Initial_Position_x=Integer.parseInt(sData[i+1]);
					D1_Initial_Position_y=Integer.parseInt(sData[i+2]);
					P_Initial_Position.add(D1_Initial_Position_x);
					P_Initial_Position.add(D1_Initial_Position_y);
					i=i+3;
					Log.d("debug", "D1 initial saved.");
				}
				if(sData[i].equals("D2_Initial_Position")){
					D2_Initial_Position_x=Integer.parseInt(sData[i+1]);
					D2_Initial_Position_y=Integer.parseInt(sData[i+2]);
					P_Initial_Position.add(D2_Initial_Position_x);
					P_Initial_Position.add(D2_Initial_Position_y);
					i=i+3;
					Log.d("debug", "D2 initial saved.");
				}
				if(sData[i].equals("D3_Initial_Position")){
					D3_Initial_Position_x=Integer.parseInt(sData[i+1]);
					D3_Initial_Position_y=Integer.parseInt(sData[i+2]);
					P_Initial_Position.add(D3_Initial_Position_x);
					P_Initial_Position.add(D3_Initial_Position_y);
					i=i+3;
					Log.d("debug", "D3 initial saved.");
				}
				if(sData[i].equals("D4_Initial_Position")){
					D4_Initial_Position_x=Integer.parseInt(sData[i+1]);
					D4_Initial_Position_y=Integer.parseInt(sData[i+2]);
					P_Initial_Position.add(D4_Initial_Position_x);
					P_Initial_Position.add(D4_Initial_Position_y);
					i=i+3;
					Log.d("debug", "D4 initial saved.");
				}
				if(sData[i].equals("D5_Initial_Position")){
					D5_Initial_Position_x=Integer.parseInt(sData[i+1]);
					D5_Initial_Position_y=Integer.parseInt(sData[i+2]);
					P_Initial_Position.add(D5_Initial_Position_x);
					P_Initial_Position.add(D5_Initial_Position_y);
					i=i+3;
					Log.d("debug", "D5 initial saved.");
				}

                //存Player的road
				if(sData[i].equals("P1")){
					i++;
					while(!sData[i].equals("---")){
						P1.setRoad(Integer.parseInt(sData[i]));
						P1.setRoad_3d(Integer.parseInt(sData[i]));
						
						P1_recommend.setRoad(Integer.parseInt(sData[i]));
						
						i++;
					}
					
					Log.d("debug", "P1 road saved. Size="+Integer.toString(P1.getRoadSize())+"....."+Integer.toString(P1.getRoadSize_3d()));
				}
				if(sData[i].equals("P2")){
					i++;
					while(!sData[i].equals("---")){
						P2.setRoad(Integer.parseInt(sData[i]));
						P2.setRoad_3d(Integer.parseInt(sData[i]));
						
						P2_recommend.setRoad(Integer.parseInt(sData[i]));
						i++;
					}
					Log.d("debug", "P2 road saved. Size="+Integer.toString(P2.getRoadSize())+"....."+Integer.toString(P2.getRoadSize_3d()));
				}
				if(sData[i].equals("P3")){
					i++;
					while(!sData[i].equals("---")){
						P3.setRoad(Integer.parseInt(sData[i]));
						P3.setRoad_3d(Integer.parseInt(sData[i]));
						P3_recommend.setRoad(Integer.parseInt(sData[i]));
						i++;
					}
					Log.d("debug", "P3 road saved. Size="+Integer.toString(P3.getRoadSize())+"....."+Integer.toString(P3.getRoadSize_3d()));
				}
				if(sData[i].equals("P4")){
					i++;
					while(!sData[i].equals("---")){
						P4.setRoad(Integer.parseInt(sData[i]));
						P4.setRoad_3d(Integer.parseInt(sData[i]));
						P4_recommend.setRoad(Integer.parseInt(sData[i]));
						i++;
					}
					Log.d("debug", "P4 road saved. Size="+Integer.toString(P4.getRoadSize())+"....."+Integer.toString(P4.getRoadSize_3d()));
				}
				if(sData[i].equals("P5")){
					i++;
					while(!sData[i].equals("---")){
						P5.setRoad(Integer.parseInt(sData[i]));
						P5.setRoad_3d(Integer.parseInt(sData[i]));
						P5_recommend.setRoad(Integer.parseInt(sData[i]));
						i++;
					}
					Log.d("debug", "P5 road saved. Size="+Integer.toString(P5.getRoadSize())+"....."+Integer.toString(P5.getRoadSize_3d()));
				}
				if(sData[i].equals("B")){
					i++;
					while(!sData[i].equals("---")){
						B.setRoad(Integer.parseInt(sData[i]));
						B.setRoad_3d(Integer.parseInt(sData[i]));
						B_recommend.setRoad(Integer.parseInt(sData[i]));
						i++;
					}
					Log.d("debug", "B road saved. Size="+Integer.toString(B.getRoadSize())+"....."+Integer.toString(B.getRoadSize_3d()));
				}
				
				if(sData[i].equals("D1")){
					i++;
					while(!sData[i].equals("---")){
						D1.setRoad(Integer.parseInt(sData[i]));
						D1.setRoad_3d(Integer.parseInt(sData[i]));
						
						i++;
					}
					
					Log.d("debug", "D1 road saved. Size="+Integer.toString(D1.getRoadSize())+"....."+Integer.toString(D1.getRoadSize_3d()));
				}
				if(sData[i].equals("D2")){
					i++;
					while(!sData[i].equals("---")){
						D2.setRoad(Integer.parseInt(sData[i]));
						D2.setRoad_3d(Integer.parseInt(sData[i]));
						
						i++;
					}
					Log.d("debug", "D2 road saved. Size="+Integer.toString(D2.getRoadSize())+"....."+Integer.toString(D2.getRoadSize_3d()));
				}
				if(sData[i].equals("D3")){
					i++;
					while(!sData[i].equals("---")){
						D3.setRoad(Integer.parseInt(sData[i]));
						D3.setRoad_3d(Integer.parseInt(sData[i]));
						i++;
					}
					Log.d("debug", "D3 road saved. Size="+Integer.toString(D3.getRoadSize())+"....."+Integer.toString(D3.getRoadSize_3d()));
				}
				if(sData[i].equals("D4")){
					i++;
					while(!sData[i].equals("---")){
						D4.setRoad(Integer.parseInt(sData[i]));
						D4.setRoad_3d(Integer.parseInt(sData[i]));
						i++;
					}
					Log.d("debug", "D4 road saved. Size="+Integer.toString(D4.getRoadSize())+"....."+Integer.toString(D4.getRoadSize_3d()));
				}
				if(sData[i].equals("D5")){
					i++;
					while(!sData[i].equals("---")){
						D5.setRoad(Integer.parseInt(sData[i]));
						D5.setRoad_3d(Integer.parseInt(sData[i]));
						i++;
					}
					Log.d("debug", "D5 road saved. Size="+Integer.toString(D5.getRoadSize())+"....."+Integer.toString(D5.getRoadSize_3d()));
				}

                //存player的rotation
				if(sData[i].equals("P1_rotation")){
					i++;
					while(!sData[i].equals("---")){
						P1.setMyRotation(Integer.parseInt(sData[i]));
						
						P1_recommend.setMyRotation(Integer.parseInt(sData[i]));
						
						i++;
					}
					//Log.d("debug", "P1 road saved. Size="+Integer.toString(P1.getRoadSize())+"....."+Integer.toString(P1.getRoadSize_3d()));
				}
				if(sData[i].equals("P2_rotation")){
					i++;
					while(!sData[i].equals("---")){
						P2.setMyRotation(Integer.parseInt(sData[i]));
						
						P2_recommend.setMyRotation(Integer.parseInt(sData[i]));
						i++;
					}
				}
				if(sData[i].equals("P3_rotation")){
					i++;
					while(!sData[i].equals("---")){
						P3.setMyRotation(Integer.parseInt(sData[i]));
						P3_recommend.setMyRotation(Integer.parseInt(sData[i]));
						i++;
					}
				}
				if(sData[i].equals("P4_rotation")){
					i++;
					while(!sData[i].equals("---")){
						P4.setMyRotation(Integer.parseInt(sData[i]));
						P4_recommend.setMyRotation(Integer.parseInt(sData[i]));
						i++;
					}
				}
				if(sData[i].equals("P5_rotation")){
					i++;
					while(!sData[i].equals("---")){
						P5.setMyRotation(Integer.parseInt(sData[i]));
						P5_recommend.setMyRotation(Integer.parseInt(sData[i]));
						i++;
					}
				}
				
				if(sData[i].equals("D1_rotation")){
					i++;
					while(!sData[i].equals("---")){
						D1.setMyRotation(Integer.parseInt(sData[i]));
						i++;
					}
					//Log.d("debug", "D1 road saved. Size="+Integer.toString(D1.getRoadSize())+"....."+Integer.toString(D1.getRoadSize_3d()));
				}
				if(sData[i].equals("D2_rotation")){
					i++;
					while(!sData[i].equals("---")){
						D2.setMyRotation(Integer.parseInt(sData[i]));
						i++;
					}
				}
				if(sData[i].equals("D3_rotation")){
					i++;
					while(!sData[i].equals("---")){
						D3.setMyRotation(Integer.parseInt(sData[i]));
						i++;
					}
				}
				if(sData[i].equals("D4_rotation")){
					i++;
					while(!sData[i].equals("---")){
						D4.setMyRotation(Integer.parseInt(sData[i]));
						i++;
					}
				}
				if(sData[i].equals("D5_rotation")){
					i++;
					while(!sData[i].equals("---")){
						D5.setMyRotation(Integer.parseInt(sData[i]));
						i++;
					}
				}



                //存Curve
				if(sData[i].equals("P1_curve")){
					i++;
					while(!sData[i].equals("---")){
						//P1.setCurve(Float.parseFloat(sData[i]));
						//P1_recommend.setCurve(Float.parseFloat(sData[i]));
						i++;
					}
					Log.d("debug", "P1 curve saved. Size="+Integer.toString(P1.getCurveSize())+".....");
				}
				if(sData[i].equals("P2_curve")){
					i++;
					while(!sData[i].equals("---")){
						//P2.setCurve(Float.parseFloat(sData[i]));
						//P2_recommend.setCurve(Float.parseFloat(sData[i]));
						i++;
					}
					Log.d("debug", "P2 curve saved. Size="+Integer.toString(P1.getCurveSize())+".....");
				}
				if(sData[i].equals("P3_curve")){
					i++;
					while(!sData[i].equals("---")){
						//P3.setCurve(Float.parseFloat(sData[i]));
						//P3_recommend.setCurve(Float.parseFloat(sData[i]));
						i++;
					}
					Log.d("debug", "P3 curve saved. Size="+Integer.toString(P3.getCurveSize())+".....");
				}
				if(sData[i].equals("P4_curve")){
					i++;
					while(!sData[i].equals("---")){
						//P4.setCurve(Float.parseFloat(sData[i]));
						//P4_recommend.setCurve(Float.parseFloat(sData[i]));
						i++;
					}
					Log.d("debug", "P4 curve saved. Size="+Integer.toString(P4.getCurveSize())+".....");
				}
				if(sData[i].equals("P5_curve")){
					i++;
					while(!sData[i].equals("---")){
						//P5.setCurve(Float.parseFloat(sData[i]));
						//P5_recommend.setCurve(Float.parseFloat(sData[i]));
						i++;
					}
					Log.d("debug", "P5 curve saved. Size="+Integer.toString(P5.getCurveSize())+".....");
				}
				if(sData[i].equals("B_curve")){
					i++;
					while(!sData[i].equals("---")){
						//B.setCurve(Float.parseFloat(sData[i]));
						//B_recommend.setCurve(Float.parseFloat(sData[i]));
						i++;
					}
					Log.d("debug", "B curve saved. Size="+Integer.toString(B.getCurveSize())+".....");
				}
			//?sRunBag
				TimeLine timefrag = (TimeLine) getActivity().getFragmentManager().findFragmentById(R.id.time_line);
				if(sData[i].equals("RunBag")){
					int load_seekbar_tmp_id=0;
					int x=0,y=0;
					i++;
					while(i<sData.length){
						while(!sData[i].equals("---")){
							RunBag tmp = new RunBag();
							tmp.setStartTime(Integer.parseInt(sData[i]));
							i++;
							tmp.setHandler(sData[i]);
							i++;
							tmp.setRoadStart(Integer.parseInt(sData[i]));
							i++;
							tmp.setRoadEnd(Integer.parseInt(sData[i]));
							i++;
							tmp.setDuration(Integer.parseInt(sData[i]));
							i++;
							tmp.setBall_num(Integer.parseInt(sData[i]));
							i++;
							tmp.setTimeLineId(load_seekbar_tmp_id);
							RunLine.add(tmp);

							
							
							timefrag.setSeekBarId(load_seekbar_tmp_id);
							if(tmp.getHandler().equals("P1_Handle")){
    							timefrag.createSeekbar(1,load_seekbar_tmp_id,tmp.getStartTime(),tmp.getDuration());
    							MainWrap mainwrapfrag = (MainWrap) getActivity().getFragmentManager().findFragmentById(R.id.MainWrap_frag);
    							x=P1.getCmpltRoad().get(tmp.getRoadEnd()-1);
    							y=P1.getCmpltRoad().get(tmp.getRoadEnd());
    							mainwrapfrag.create_path_num_on_court(load_seekbar_tmp_id+1, x, y,load_seekbar_tmp_id);
							}
							else if(tmp.getHandler().equals("P2_Handle")){
								timefrag.createSeekbar(2,load_seekbar_tmp_id,tmp.getStartTime(),tmp.getDuration());
    							MainWrap mainwrapfrag = (MainWrap) getActivity().getFragmentManager().findFragmentById(R.id.MainWrap_frag);
    							x=P2.getCmpltRoad().get(tmp.getRoadEnd()-1);
    							y=P2.getCmpltRoad().get(tmp.getRoadEnd());
    							mainwrapfrag.create_path_num_on_court(load_seekbar_tmp_id+1, x, y,load_seekbar_tmp_id);
							}
							else if(tmp.getHandler().equals("P3_Handle")){
								timefrag.createSeekbar(3,load_seekbar_tmp_id,tmp.getStartTime(),tmp.getDuration());
    							MainWrap mainwrapfrag = (MainWrap) getActivity().getFragmentManager().findFragmentById(R.id.MainWrap_frag);
    							x=P3.getCmpltRoad().get(tmp.getRoadEnd()-1);
    							y=P3.getCmpltRoad().get(tmp.getRoadEnd());
    							mainwrapfrag.create_path_num_on_court(load_seekbar_tmp_id+1, x, y,load_seekbar_tmp_id);
							}
							else if(tmp.getHandler().equals("P4_Handle")){
								timefrag.createSeekbar(4,load_seekbar_tmp_id,tmp.getStartTime(),tmp.getDuration());
    							MainWrap mainwrapfrag = (MainWrap) getActivity().getFragmentManager().findFragmentById(R.id.MainWrap_frag);
    							x=P4.getCmpltRoad().get(tmp.getRoadEnd()-1);
    							y=P4.getCmpltRoad().get(tmp.getRoadEnd());
    							mainwrapfrag.create_path_num_on_court(load_seekbar_tmp_id+1, x, y,load_seekbar_tmp_id);
							}
							else if(tmp.getHandler().equals("P5_Handle")){
								timefrag.createSeekbar(5,load_seekbar_tmp_id,tmp.getStartTime(),tmp.getDuration());
    							MainWrap mainwrapfrag = (MainWrap) getActivity().getFragmentManager().findFragmentById(R.id.MainWrap_frag);
    							x=P5.getCmpltRoad().get(tmp.getRoadEnd()-1);
    							y=P5.getCmpltRoad().get(tmp.getRoadEnd());
    							mainwrapfrag.create_path_num_on_court(load_seekbar_tmp_id+1, x, y,load_seekbar_tmp_id);
							}
							else if(tmp.getHandler().equals("B_Handle")){
								timefrag.createSeekbar(6,load_seekbar_tmp_id,tmp.getStartTime(),tmp.getDuration());
    							MainWrap mainwrapfrag = (MainWrap) getActivity().getFragmentManager().findFragmentById(R.id.MainWrap_frag);
    							x=B.getCmpltRoad().get(tmp.getRoadEnd()-1);
    							y=B.getCmpltRoad().get(tmp.getRoadEnd());
    							mainwrapfrag.create_path_num_on_court(load_seekbar_tmp_id+1, x, y,load_seekbar_tmp_id);
							}
							else if(tmp.getHandler().equals("D1_Handle")){
    							timefrag.createSeekbar(7,load_seekbar_tmp_id,tmp.getStartTime(),tmp.getDuration());
    							MainWrap mainwrapfrag = (MainWrap) getActivity().getFragmentManager().findFragmentById(R.id.MainWrap_frag);
    							x=D1.getCmpltRoad().get(tmp.getRoadEnd()-1);
    							y=D1.getCmpltRoad().get(tmp.getRoadEnd());
    							mainwrapfrag.create_path_num_on_court(load_seekbar_tmp_id+1, x, y,load_seekbar_tmp_id);
							}
							else if(tmp.getHandler().equals("D2_Handle")){
								timefrag.createSeekbar(8,load_seekbar_tmp_id,tmp.getStartTime(),tmp.getDuration());
    							MainWrap mainwrapfrag = (MainWrap) getActivity().getFragmentManager().findFragmentById(R.id.MainWrap_frag);
    							x=D2.getCmpltRoad().get(tmp.getRoadEnd()-1);
    							y=D2.getCmpltRoad().get(tmp.getRoadEnd());
    							mainwrapfrag.create_path_num_on_court(load_seekbar_tmp_id+1, x, y,load_seekbar_tmp_id);
							}
							else if(tmp.getHandler().equals("D3_Handle")){
								timefrag.createSeekbar(9,load_seekbar_tmp_id,tmp.getStartTime(),tmp.getDuration());
    							MainWrap mainwrapfrag = (MainWrap) getActivity().getFragmentManager().findFragmentById(R.id.MainWrap_frag);
    							x=D3.getCmpltRoad().get(tmp.getRoadEnd()-1);
    							y=D3.getCmpltRoad().get(tmp.getRoadEnd());
    							mainwrapfrag.create_path_num_on_court(load_seekbar_tmp_id+1, x, y,load_seekbar_tmp_id);
							}
							else if(tmp.getHandler().equals("D4_Handle")){
								timefrag.createSeekbar(10,load_seekbar_tmp_id,tmp.getStartTime(),tmp.getDuration());
    							MainWrap mainwrapfrag = (MainWrap) getActivity().getFragmentManager().findFragmentById(R.id.MainWrap_frag);
    							x=D4.getCmpltRoad().get(tmp.getRoadEnd()-1);
    							y=D4.getCmpltRoad().get(tmp.getRoadEnd());
    							mainwrapfrag.create_path_num_on_court(load_seekbar_tmp_id+1, x, y,load_seekbar_tmp_id);
							}
							else if(tmp.getHandler().equals("D5_Handle")){
								timefrag.createSeekbar(11,load_seekbar_tmp_id,tmp.getStartTime(),tmp.getDuration());
    							MainWrap mainwrapfrag = (MainWrap) getActivity().getFragmentManager().findFragmentById(R.id.MainWrap_frag);
    							x=D5.getCmpltRoad().get(tmp.getRoadEnd()-1);
    							y=D5.getCmpltRoad().get(tmp.getRoadEnd());
    							mainwrapfrag.create_path_num_on_court(load_seekbar_tmp_id+1, x, y,load_seekbar_tmp_id);
							}
							
							
							
							load_seekbar_tmp_id++;
    				}
						i++;
					}
					//Log.d("debug", "DTW = "+Double.toString(DTW(P2.getCmpltRoad(),P2.getCmpltRoad())));
					Log.d("debug", "RunBag saved. Size="+Integer.toString(RunLine.size()));
				}
			}//Read data END
			
			dataPacket.set_P_Initial_Position(P_Initial_Position);
			dataPacket.set_P_Initial_rotate(P_Initial_Rotate);
			dataPacket.set_P1_Road(P1.getCmpltRoad());
			dataPacket.set_P2_Road(P2.getCmpltRoad());
			dataPacket.set_P3_Road(P3.getCmpltRoad());
			dataPacket.set_P4_Road(P4.getCmpltRoad());
			dataPacket.set_P5_Road(P5.getCmpltRoad());
			dataPacket.set_B_Road(B.getCmpltRoad());
			dataPacket.set_D1_Road(D1.getCmpltRoad());
			dataPacket.set_D2_Road(D2.getCmpltRoad());
			dataPacket.set_D3_Road(D3.getCmpltRoad());
			dataPacket.set_D4_Road(D4.getCmpltRoad());
			dataPacket.set_D5_Road(D5.getCmpltRoad());
			dataPacket.set_P1_Rotate(P1.getCmpltRotate());
			dataPacket.set_P2_Rotate(P2.getCmpltRotate());
			dataPacket.set_P3_Rotate(P3.getCmpltRotate());
			dataPacket.set_P4_Rotate(P4.getCmpltRotate());
			dataPacket.set_D5_Rotate(D5.getCmpltRotate());
			dataPacket.set_D1_Rotate(D1.getCmpltRotate());
			dataPacket.set_D2_Rotate(D2.getCmpltRotate());
			dataPacket.set_D3_Rotate(D3.getCmpltRotate());
			dataPacket.set_D4_Rotate(D4.getCmpltRotate());
			dataPacket.set_D5_Rotate(D5.getCmpltRotate());
			*/
			//endregion

		playerMoveToStartPosition();
	}
	/**************************************************************************/
	
	/**************************************************************************/
	public void rotatePlayer(int input){     
			switch(rotateWhichPlayer){
			case 1:
				//Log,i("debug", "P1_rotate ="+Integer.toString(input));
				p1Rotate =input;
				pRotate =input;
				arrow1.setRotation(input);
				if(recordCheck ==false && runBags.size()==0){
					initialP1Rotate = input;
				}
				break;
			case 2:
				p2Rotate =input;
				pRotate =input;
				arrow2.setRotation(input);
				if(recordCheck ==false && runBags.size()==0){
					initialP2Rotate = input;
				}
				break;
			case 3:
				p3Rotate =input;
				pRotate =input;
				arrow3.setRotation(input);
				if(recordCheck ==false && runBags.size()==0){
					initialP3Rotate = input;
				}
				break;
			case 4:
				p4Rotate =input;
				pRotate =input;
				arrow4.setRotation(input);
				if(recordCheck ==false && runBags.size()==0){
					initialP4Rotate = input;
				}
				break;
			case 5:
				p5Rotate =input;
				pRotate =input;
				arrow5.setRotation(input);
				if(recordCheck ==false && runBags.size()==0){
					initialP5Rotate = input;
				}
				break;
			case 6:
				bRotate =input;
				break;
			case 7:
				//Log,i("debug", "D1_rotate ="+Integer.toString(input));
				d1Rotate =input;
				pRotate =input;
				arrow7.setRotation(input);
				if(recordCheck ==false && runBags.size()==0){
					initialD1Rotate = input;
				}
				break;
			case 8:
				d2Rotate =input;
				pRotate =input;
				arrow8.setRotation(input);
				if(recordCheck ==false && runBags.size()==0){
					initialD2Rotate = input;
				}
				break;
			case 9:
				d3Rotate =input;
				pRotate =input;
				arrow9.setRotation(input);
				if(recordCheck ==false && runBags.size()==0){
					initialD3Rotate = input;
				}
				break;
			case 10:
				d4Rotate =input;
				pRotate =input;
				arrow10.setRotation(input);
				if(recordCheck ==false && runBags.size()==0){
					initialD4Rotate = input;
				}
				break;
			case 11:
				d5Rotate =input;
				pRotate =input;
				arrow11.setRotation(input);
				if(recordCheck ==false && runBags.size()==0){
					initialD5Rotate = input;
				}
				break;
			default:
				//Log,i("debug", "rotate_which_player error!!");
			}
	}
	/**************************************************************************/

	public void playButton() {  
		if (runBags.isEmpty()) {
			//Log.e("empty!", String.valueOf(RunLine.size()));
		} else {
            /*先把全部player移到按下錄製鍵時的位置*/
			playerMoveToStartPosition();
			//Player_change_to_no_ball();
			/****************************/
			
			//////////////////////////////////////// Time
			//////////////////////////////////////// counter///////////////////////////////////////
			new Thread(new Runnable() {// Time counter count on per second 
				@Override
				public void run() { 
					int time = 0;
					int RunLineSize = runBags.size();
					while (time < totalTime && playing==1) {
						try {
							Log.e("time = ", String.valueOf(time));
							// do RunLine here!!
							////// check each road's start time in
							// RunLine///////
							checkRunLine(time, RunLineSize);
							hasInvokeCurrentTimeDefender = false;
							Thread.sleep(1000);
							time = time + 1000;

						} catch (Throwable t) {
						}
					}
				}
			}).start();
		}
	}

	protected void checkRunLine(final int in_time, final int in_RunLineSize) { 
		new Thread(new Runnable() { 
			@Override
			public void run() { 
				int time = in_time;
				int RunLineSize = in_RunLineSize;
				int i = 0;
				while (i < RunLineSize && playing==1) {
					try {
						Message m = new Message();
						Bundle b = new Bundle();
						b.putInt("what", i);// 將play_k打包成msg
						b.putInt("time", time);
						m.setData(b);
						RunLineCheck_Handle.sendMessage(m);
						i = i + 1;
					} catch (Throwable t) {
					}
				}
			}
		}).start();
	}

	protected void play(final boolean isDribble, final int speed, final Handler play_handler, final int roadStartIndex, final int roadEndIndex) {
		//Log,i("debug","initial_ball_num="+Integer.toString(initial_ball_num));
		new Thread(new Runnable() {  
			@Override
			public void run() {  
				int play_k = roadStartIndex;
				while (play_k < roadEndIndex - 1 && playing==1) {
					try {
						Message m = new Message();
						Bundle b = new Bundle();
						// Bundle可以根據 ("keyName",key_value)的方式，將資料打包，要使用的時候，就可以用"keyName"去取得key_value
						b.putInt("what", play_k);
						// 將play_k打包，要取得play_k的值的時候，要用"what"去取得。
						b.putBoolean("isDribble", isDribble);
						m.setData(b);
						// 將Bundle放進Message
						play_handler.sendMessage(m);
						// 把Message傳給handler去處理，handler收到message後，就會執行handleMessage的內容。
						Thread.sleep(speed);
						play_k = play_k + 2;
					} catch (Throwable t) {
					}
				}
			}
		}).start();
	}

	Handler P1_Handle = new Handler() {    
		@Override
		public void handleMessage(Message msg) {   
			int sentInt = msg.getData().getInt("what");
			player1.layout(P1.handleGetRoad(sentInt), P1.handleGetRoad(sentInt + 1),
							P1.handleGetRoad(sentInt) + player1.getWidth(),
							P1.handleGetRoad(sentInt + 1) + player1.getHeight());
			//region 因為這一動是運球，所以讓球跟著Player的位置
			boolean isDribble = msg.getData().getBoolean("isDribble");
			if(isDribble)
				ball.layout(P1.handleGetRoad(sentInt), P1.handleGetRoad(sentInt + 1),
					P1.handleGetRoad(sentInt) + ball.getWidth(),
					   P1.handleGetRoad(sentInt + 1) + ball.getHeight() );
			//endregion

			arrow1.layout(P1.handleGetRoad(sentInt), P1.handleGetRoad(sentInt + 1),
					P1.handleGetRoad(sentInt) + arrow1.getWidth(),
					P1.handleGetRoad(sentInt + 1) + arrow1.getHeight());
			
			if(sentInt!=1){
				arrow1.setRotation(P1.getMyRotation(sentInt/2));
			}
			else{
				arrow1.setRotation(P1.getMyRotation(sentInt));
			}


			
			
		}
	};

	Handler P2_Handle = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int sentInt = msg.getData().getInt("what");
			player2.layout(P2.handleGetRoad(sentInt), P2.handleGetRoad(sentInt + 1),
							P2.handleGetRoad(sentInt) + player2.getWidth(),
							P2.handleGetRoad(sentInt + 1) + player2.getHeight());

			//region 因為這一動是運球，所以讓球跟著Player的位置
			boolean isDribble = msg.getData().getBoolean("isDribble");
			if(isDribble)
				ball.layout(P2.handleGetRoad(sentInt), P2.handleGetRoad(sentInt + 1),
						P2.handleGetRoad(sentInt) + ball.getWidth(),
						P2.handleGetRoad(sentInt + 1) + ball.getHeight() );
			//endregion

			arrow2.layout(P2.handleGetRoad(sentInt), P2.handleGetRoad(sentInt + 1),
					P2.handleGetRoad(sentInt) + arrow2.getWidth(),
					P2.handleGetRoad(sentInt + 1) + arrow2.getHeight());
			
			if(sentInt!=1){
				arrow2.setRotation(P2.getMyRotation(sentInt/2));
			}
			else{
				arrow2.setRotation(P2.getMyRotation(sentInt));
			}
		}
	};

	Handler P3_Handle = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int sentInt = msg.getData().getInt("what");
			player3.layout(P3.handleGetRoad(sentInt), P3.handleGetRoad(sentInt + 1),
							P3.handleGetRoad(sentInt) + player3.getWidth(),
							P3.handleGetRoad(sentInt + 1) + player3.getHeight());

			//region 因為這一動是運球，所以讓球跟著Player的位置
			boolean isDribble = msg.getData().getBoolean("isDribble");
			if(isDribble)
				ball.layout(P3.handleGetRoad(sentInt), P3.handleGetRoad(sentInt + 1),
						P3.handleGetRoad(sentInt) + ball.getWidth(),
						P3.handleGetRoad(sentInt + 1) + ball.getHeight() );
			//endregion

			arrow3.layout(P3.handleGetRoad(sentInt), P3.handleGetRoad(sentInt + 1),
					P3.handleGetRoad(sentInt) + arrow3.getWidth(),
					P3.handleGetRoad(sentInt + 1) + arrow3.getHeight());
			
			if(sentInt!=1){
				arrow3.setRotation(P3.getMyRotation(sentInt/2));
			}
			else{
				arrow3.setRotation(P3.getMyRotation(sentInt));
			}
		}
	};

	Handler P4_Handle = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int sentInt = msg.getData().getInt("what");
			player4.layout(P4.handleGetRoad(sentInt), P4.handleGetRoad(sentInt + 1),
							P4.handleGetRoad(sentInt) + player4.getWidth(),
							P4.handleGetRoad(sentInt + 1) + player4.getHeight());

			//region 因為這一動是運球，所以讓球跟著Player的位置
			boolean isDribble = msg.getData().getBoolean("isDribble");
			if(isDribble)
				ball.layout(P4.handleGetRoad(sentInt), P4.handleGetRoad(sentInt + 1),
						P4.handleGetRoad(sentInt) + ball.getWidth(),
						P4.handleGetRoad(sentInt + 1) + ball.getHeight() );
			//endregion

			arrow4.layout(P4.handleGetRoad(sentInt), P4.handleGetRoad(sentInt + 1),
					P4.handleGetRoad(sentInt) + arrow4.getWidth(),
					P4.handleGetRoad(sentInt + 1) + arrow4.getHeight());
			
			if(sentInt!=1){
				arrow4.setRotation(P4.getMyRotation(sentInt/2));
			}
			else{
				arrow4.setRotation(P4.getMyRotation(sentInt));
			}
		}
	};

	Handler P5_Handle = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int sentInt = msg.getData().getInt("what");
			player5.layout(P5.handleGetRoad(sentInt), P5.handleGetRoad(sentInt + 1),
							P5.handleGetRoad(sentInt) + player5.getWidth(),
							P5.handleGetRoad(sentInt + 1) + player5.getHeight());

			//region 因為這一動是運球，所以讓球跟著Player的位置
			boolean isDribble = msg.getData().getBoolean("isDribble");
			if(isDribble)
				ball.layout(P5.handleGetRoad(sentInt), P5.handleGetRoad(sentInt + 1),
						P5.handleGetRoad(sentInt) + ball.getWidth(),
						P5.handleGetRoad(sentInt + 1) + ball.getHeight() );
			//endregion

			arrow5.layout(P5.handleGetRoad(sentInt), P5.handleGetRoad(sentInt + 1),
					P5.handleGetRoad(sentInt) + arrow5.getWidth(),
					P5.handleGetRoad(sentInt + 1) + arrow5.getHeight());
			
			if(sentInt!=1){
				arrow5.setRotation(P5.getMyRotation(sentInt/2));
			}
			else{
				arrow5.setRotation(P5.getMyRotation(sentInt));
			}
		}
	};

	Handler B_Handle = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int sentInt = msg.getData().getInt("what");
			ball.layout(B.handleGetRoad(sentInt), B.handleGetRoad(sentInt + 1),
					B.handleGetRoad(sentInt) + ball.getWidth(), B.handleGetRoad(sentInt + 1) + ball.getHeight());
		}
	};
	
	Handler D1_Handle = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int sentInt = msg.getData().getInt("what");
			defender1.layout(D1.handleGetRoad(sentInt), D1.handleGetRoad(sentInt + 1),
							D1.handleGetRoad(sentInt) + defender1.getWidth(),
							D1.handleGetRoad(sentInt + 1) + defender1.getHeight());
			
			arrow7.layout(D1.handleGetRoad(sentInt), D1.handleGetRoad(sentInt + 1),
					D1.handleGetRoad(sentInt) + arrow7.getWidth(),
					D1.handleGetRoad(sentInt + 1) + arrow7.getHeight());
			/*
			if(sentInt!=1){
				arrow7.setRotation(D1.getMyRotation(sentInt/2));
			}
			else{
				arrow7.setRotation(D1.getMyRotation(sentInt));
			}
			*/
		}
	};
	
	Handler D2_Handle = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int sentInt = msg.getData().getInt("what");
			defender2.layout(D2.handleGetRoad(sentInt), D2.handleGetRoad(sentInt + 1),
							D2.handleGetRoad(sentInt) + defender2.getWidth(),
							D2.handleGetRoad(sentInt + 1) + defender2.getHeight());
			
			arrow8.layout(D2.handleGetRoad(sentInt), D2.handleGetRoad(sentInt + 1),
					D2.handleGetRoad(sentInt) + arrow8.getWidth(),
					D2.handleGetRoad(sentInt + 1) + arrow8.getHeight());
			/*
			if(sentInt!=1){
				arrow8.setRotation(D2.getMyRotation(sentInt/2));
			}
			else{
				arrow8.setRotation(D2.getMyRotation(sentInt));
			}
			
			 */
		}
	};
	
	Handler D3_Handle = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int sentInt = msg.getData().getInt("what");
			defender3.layout(D3.handleGetRoad(sentInt), D3.handleGetRoad(sentInt + 1),
							D3.handleGetRoad(sentInt) + defender3.getWidth(),
							D3.handleGetRoad(sentInt + 1) + defender3.getHeight());
			
			arrow9.layout(D3.handleGetRoad(sentInt), D3.handleGetRoad(sentInt + 1),
					D3.handleGetRoad(sentInt) + arrow9.getWidth(),
					D3.handleGetRoad(sentInt + 1) + arrow9.getHeight());
			/*
			if(sentInt!=1){
				arrow9.setRotation(D3.getMyRotation(sentInt/2));
			}
			else{
				arrow9.setRotation(D3.getMyRotation(sentInt));
			}

			 */
		}
	};
	
	Handler D4_Handle = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int sentInt = msg.getData().getInt("what");
			defender4.layout(D4.handleGetRoad(sentInt), D4.handleGetRoad(sentInt + 1),
							D4.handleGetRoad(sentInt) + defender4.getWidth(),
							D4.handleGetRoad(sentInt + 1) + defender4.getHeight());
			
			arrow10.layout(D4.handleGetRoad(sentInt), D4.handleGetRoad(sentInt + 1),
					D4.handleGetRoad(sentInt) + arrow10.getWidth(),
					D4.handleGetRoad(sentInt + 1) + arrow10.getHeight());
			/*
			if(sentInt!=1){
				arrow10.setRotation(D4.getMyRotation(sentInt/2));
			}
			else{
				arrow10.setRotation(D4.getMyRotation(sentInt));
			}

			 */
		}
	};

	Handler D5_Handle = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int sentInt = msg.getData().getInt("what");
			defender5.layout(D5.handleGetRoad(sentInt), D5.handleGetRoad(sentInt + 1),
							D5.handleGetRoad(sentInt) + defender5.getWidth(),
							D5.handleGetRoad(sentInt + 1) + defender5.getHeight());
			
			arrow11.layout(D5.handleGetRoad(sentInt), D5.handleGetRoad(sentInt + 1),
					D5.handleGetRoad(sentInt) + arrow11.getWidth(),
					D5.handleGetRoad(sentInt + 1) + arrow11.getHeight());
			/*
			if(sentInt!=1){
				arrow11.setRotation(D5.getMyRotation(sentInt/2));
			}
			else{
				arrow11.setRotation(D5.getMyRotation(sentInt));
			}

			 */
		}
	};
	
	Handler RunLineCheck_Handle = new Handler() {    

		@Override
		public void handleMessage(Message msg) {   

			int sentI = msg.getData().getInt("what");
			int sentTime = msg.getData().getInt("time");
			if (runBags.get(sentI).getStartTime() * 1000 == sentTime) {
				boolean isDribble = (runBags.get(sentI).getPathType() == 2);
				if ( runBags.get(sentI).getHandler().equals("P1_Handle")) {
					play(isDribble, runBags.get(sentI).getRate(), P1_Handle, runBags.get(sentI).getRoadStart(),
							runBags.get(sentI).getRoadEnd());
				} else if (runBags.get(sentI).getHandler().equals("P2_Handle")) {
					play(isDribble, runBags.get(sentI).getRate(), P2_Handle, runBags.get(sentI).getRoadStart(),
							runBags.get(sentI).getRoadEnd());
				} else if (runBags.get(sentI).getHandler().equals("P3_Handle")) {
					play(isDribble, runBags.get(sentI).getRate(), P3_Handle, runBags.get(sentI).getRoadStart(),
							runBags.get(sentI).getRoadEnd());
				} else if (runBags.get(sentI).getHandler().equals("P4_Handle")) {
					play(isDribble, runBags.get(sentI).getRate(), P4_Handle, runBags.get(sentI).getRoadStart(),
							runBags.get(sentI).getRoadEnd());
				} else if (runBags.get(sentI).getHandler().equals("P5_Handle")) {
					play(isDribble, runBags.get(sentI).getRate(), P5_Handle, runBags.get(sentI).getRoadStart(),
							runBags.get(sentI).getRoadEnd());
				} else if (runBags.get(sentI).getHandler().equals("B_Handle")) {
					play(isDribble, runBags.get(sentI).getRate(), B_Handle, runBags.get(sentI).getRoadStart(),
							runBags.get(sentI).getRoadEnd());
				}
				else if (runBags.get(sentI).getHandler().equals("D1_Handle")) {
					play(isDribble, runBags.get(sentI).getRate(), D1_Handle, runBags.get(sentI).getRoadStart(),
							runBags.get(sentI).getRoadEnd());
				} else if (runBags.get(sentI).getHandler().equals("D2_Handle")) {
					play(isDribble, runBags.get(sentI).getRate(), D2_Handle, runBags.get(sentI).getRoadStart(),
							runBags.get(sentI).getRoadEnd());
				} else if (runBags.get(sentI).getHandler().equals("D3_Handle")) {
					play(isDribble, runBags.get(sentI).getRate(), D3_Handle, runBags.get(sentI).getRoadStart(),
							runBags.get(sentI).getRoadEnd());
				} else if (runBags.get(sentI).getHandler().equals("D4_Handle")) {
					play(isDribble, runBags.get(sentI).getRate(), D4_Handle, runBags.get(sentI).getRoadStart(),
							runBags.get(sentI).getRoadEnd());
				} else if (runBags.get(sentI).getHandler().equals("D5_Handle")) {
					play(isDribble, runBags.get(sentI).getRate(), D5_Handle, runBags.get(sentI).getRoadStart(),
							runBags.get(sentI).getRoadEnd());
				}

				if(hasQueryDefenderFromServer && !hasInvokeCurrentTimeDefender){
					Log.d("debug", "rate:" + runBags.get(sentI).getRate());
					play(false, 20, D1_Handle, Integer.parseInt(currentTimeMaxLen.get(sentTime/1000))*2, Integer.parseInt(currentTimeMaxLen.get(sentTime/1000+1))*2);
					play(false, 20, D2_Handle, Integer.parseInt(currentTimeMaxLen.get(sentTime/1000))*2, Integer.parseInt(currentTimeMaxLen.get(sentTime/1000+1))*2);
					play(false, 20, D3_Handle, Integer.parseInt(currentTimeMaxLen.get(sentTime/1000))*2, Integer.parseInt(currentTimeMaxLen.get(sentTime/1000+1))*2);
					play(false, 20, D4_Handle, Integer.parseInt(currentTimeMaxLen.get(sentTime/1000))*2, Integer.parseInt(currentTimeMaxLen.get(sentTime/1000+1))*2);
					play(false, 20, D5_Handle, Integer.parseInt(currentTimeMaxLen.get(sentTime/1000))*2, Integer.parseInt(currentTimeMaxLen.get(sentTime/1000+1))*2);
				}
			}
		}
	};

	//TODO Bitmap_ontouch
	private OnTouchListener bitmap_ontouch = new OnTouchListener() {         

			private int mx, my; // 圖片被拖曳的X ,Y軸距離長度
			private int startX, startY; // 原本圖片存在的X,Y軸位置
			private int x, y; // 最終的顯示位置
			private int last_mx,last_my;
			private float tmp_y;
			@Override
			public boolean onTouch(View v, MotionEvent event) {      
				/*mx = (int) (event.getRawX());
				my = (int) (event.getRawY());
				switch (event.getAction()) { // ?P?_???????@
				case MotionEvent.ACTION_DOWN:// ???U?????
					
					int flag = 0;
					int sensitive = 20;
					int rm_button_area = 50;
					
					if(rm_button_flag==1 && mx < last_mx+rm_button_area && mx > last_mx-rm_button_area){
						if(rm_button_flag==1 && my < last_my+rm_button_area && my > last_my-rm_button_area){
							////Log,i("debug", "click on rm_button");
						}
						else{
							////Log,i("debug", "On other area.");
							rm_button_flag=0;
							rm_button.layout(0,0,1,1);
							rm_button.invalidate();
							////Log,i("debug", "rm_button_flag="+Integer.toString(rm_button_flag));
						}
					}
					else{
						////Log,i("debug", "On other area.");
						rm_button_flag=0;
						rm_button.layout(0,0,1,1);
						rm_button.invalidate();
						////Log,i("debug", "rm_button_flag="+Integer.toString(rm_button_flag));
					
					
						for(int Curves_idx=0;Curves_idx<Curves.size();Curves_idx+=2){//Curves ?s???Ox,y ??H?@???n??2
							for(int path_curve_idx = 0;path_curve_idx<Curves.get(Curves_idx).size()-2;path_curve_idx++){//?]???C???|?????????O???F?nplace rm_button????A??H??????????
								
								if(mx < Curves.get(Curves_idx).get(path_curve_idx)+sensitive && mx > Curves.get(Curves_idx).get(path_curve_idx)-sensitive){
									if(my < Curves.get(Curves_idx+1).get(path_curve_idx)+sensitive && my > Curves.get(Curves_idx+1).get(path_curve_idx)-sensitive){
										////Log,i("debug", "Match!!");
										////Log,i("debug", "path_x.size = "+Integer.toString(Curves.get(Curves_idx).size()));
										which_to_remove=Curves_idx/2;
										
										
										//????rm_button
										float fx = Curves.get(Curves_idx).get(Curves.get(Curves_idx).size()-1);
										float fy = Curves.get(Curves_idx+1).get(Curves.get(Curves_idx+1).size()-1);
										float flx = Curves.get(Curves_idx).get(Curves.get(Curves_idx).size()-2);
										float fly = Curves.get(Curves_idx+1).get(Curves.get(Curves_idx+1).size()-2);
										
										x= (int) fx;
										y= (int) fy-20;
										last_mx = (int) flx;
										last_my = (int) fly-20;
										rm_button.setVisibility(rm_button.VISIBLE);
										rm_button.setOnClickListener(rm_button_Listener);
										rm_button.layout(x, y, x+rm_button_width, y+rm_button_height);
										rm_button.invalidate();
										rm_button_flag=1;
										flag=1;
										break;
									}
									else{
										////Log,i("debug", "rm_button_flag="+Integer.toString(rm_button_flag));
										////Log,i("debug", "mx = "+Integer.toString(mx));
										////Log,i("debug", "my = "+Integer.toString(my));
										////Log,i("debug", "last_mx = "+Integer.toString(last_mx));
										////Log,i("debug", "last_my = "+Integer.toString(last_my));
										
									}
								}
							}
							if(flag!=0){//?�Z???X?j??
								flag=0;
								break;
							}	
						}
					}
					
				break;
				case MotionEvent.ACTION_MOVE:
					
				break;
				case MotionEvent.ACTION_UP:
					
				break;
				
				}*/
				return false;
			}
		};
		
		
		/*When the icon of the player is touched.*/
		private OnTouchListener player_Listener = new OnTouchListener() {                        
			
			private int mx, my; // 圖片被拖曳的X ,Y軸距離長度
			private int startX, startY; // 原本圖片存在的X,Y軸位置
			private int x, y; // 最終的顯示位置
			private float tmp_y;
			private int move_count;
			private boolean dum_flag;
			private Bitmap Pbitmap;
			private Canvas Pcanvas;
			private Vector<Float> P_curve_x;
			private Vector<Float> P_curve_y;
			private Player player;
			private int c_idx;
			private float sample_rate;
			private Vector<Integer> P_tempcurve_x;
			private Vector<Integer> P_tempcurve_y;
			//private int P_rotate;
			private Paint player_paint;
			private ImageView Arrow;
			private int P_startIndex;
			String handle_name=new String();
			private int P_Initial_Position_x;
			private int P_Initial_Position_y;
			private int seekbar_player_Id;

			//region 用來取得掩護時圖片角度的變數
			private float screen_direction;
			private float last_direction;
			//endregion

			//region 用來畫運球路徑的
			private float line_start_point_x;
			private float line_start_point_y;
			//endregion
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				// TouchEvent(P1,paint,mx,my,startX,startY,v,event,"P1");
				mx = (int) (event.getRawX());
				my = (int) (event.getRawY());

				////Log,i("debug","Position  x= "+Integer.toString(mx) );
				////Log,i("debug","Position  y= "+Integer.toString(my) );
				
				switch (event.getAction()) { // 判斷觸控的動作
				case MotionEvent.ACTION_DOWN:// 按下圖片時
					sample_rate = 0.0f;
					if(v.getTag().toString().equals("1")){
						//Log,i("debug","P1    player ontouch!" );
						player = P1;
						c_idx = c1Idx;
						P_tempcurve_x = p1TempcurveX;
						P_tempcurve_y = p1TempcurveY;
						pRotate = p1Rotate;
						player_paint = player1Paint;
						rotateWhichPlayer = 1;
						Arrow = arrow1;
						P_startIndex = p1StartIndex;
						handle_name = "P1_Handle";
						P_Initial_Position_x = p1InitialPositionX;
						P_Initial_Position_y = p1InitialPositionY;
						seekbar_player_Id = 1;
						rcP1 = new Rect((int) event.getX(),my - v.getTop(),(int) event.getX()+ v.getWidth(),my - v.getTop()+v.getHeight());

						TimeLine timefrag1 = (TimeLine) getActivity().getFragmentManager().findFragmentById(R.id.time_line);
						timefrag1.changeLayout(1);
						
						//Log,i("debug", "first    P_startIndex="+Integer.toString(P_startIndex));
						//Log,i("debug", "first    P1_startIndex="+Integer.toString(P1_startIndex));
					}
					else if (v.getTag().toString().equals("2")){
						//Log,i("debug","P2 player Ontouch!" );
						player=P2;
						c_idx= c2Idx;
						P_tempcurve_x = p2TempcurveX;
						P_tempcurve_y = p2TempcurveY;
						rotateWhichPlayer =2;
						pRotate = p2Rotate;
						player_paint= player2Paint;
						Arrow= arrow2;
						P_startIndex= p2StartIndex;
						handle_name="P2_Handle";
						P_Initial_Position_x= p2InitialPositionX;
						P_Initial_Position_y= p2InitialPositionY;
						seekbar_player_Id=2;
						//Log,i("debug", "first    P_startIndex="+Integer.toString(P_startIndex));
						//Log,i("debug", "first    P2_startIndex="+Integer.toString(P2_startIndex));
						rcP2 =new Rect((int) event.getX(),my - v.getTop(),(int) event.getX()+ v.getWidth(),my - v.getTop()+v.getHeight());
						
						TimeLine timefrag1 = (TimeLine) getActivity().getFragmentManager().findFragmentById(R.id.time_line);
						timefrag1.changeLayout(2);
					}
					else if (v.getTag().toString().equals("3")){
						//Log,i("debug","P3    player ontouch!" );
						player=P3;
						c_idx= c3Idx;
						P_tempcurve_x = p3TempcurveX;
						P_tempcurve_y = p3TempcurveY;
						pRotate = p3Rotate;
						player_paint= player3Paint;
						rotateWhichPlayer =3;
						Arrow= arrow3;
						P_startIndex= p3StartIndex;
						handle_name="P3_Handle";
						P_Initial_Position_x= p3InitialPositionX;
						P_Initial_Position_y= p3InitialPositionY;
						seekbar_player_Id=3;
						rcP3 =new Rect((int) event.getX(),my - v.getTop(),(int) event.getX()+ v.getWidth(),my - v.getTop()+v.getHeight());
						//Log,i("debug", "first    P_startIndex="+Integer.toString(P_startIndex));
						//Log,i("debug", "first    P3_startIndex="+Integer.toString(P3_startIndex));
						
						TimeLine timefrag1 = (TimeLine) getActivity().getFragmentManager().findFragmentById(R.id.time_line);
						timefrag1.changeLayout(3);
					}
					else if (v.getTag().toString().equals("4")){
						//Log,i("debug","P4    player ontouch!" );
						player=P4;
						c_idx= c4Idx;
						P_tempcurve_x = p4TempcurveX;
						P_tempcurve_y = p4TempcurveY;
						pRotate = p4Rotate;
						player_paint= player4Paint;
						rotateWhichPlayer =4;
						Arrow= arrow4;
						P_startIndex= p4StartIndex;
						handle_name="P4_Handle";
						P_Initial_Position_x= p4InitialPositionX;
						P_Initial_Position_y= p4InitialPositionY;
						seekbar_player_Id=4;
						rcP4 =new Rect((int) event.getX(),my - v.getTop(),(int) event.getX()+ v.getWidth(),my - v.getTop()+v.getHeight());
						//Log,i("debug", "first    P_startIndex="+Integer.toString(P_startIndex));
						//Log,i("debug", "first    P4_startIndex="+Integer.toString(P4_startIndex));
						
						TimeLine timefrag1 = (TimeLine) getActivity().getFragmentManager().findFragmentById(R.id.time_line);
						timefrag1.changeLayout(4);
					}
					else if (v.getTag().toString().equals("5")){
						//Log,i("debug","P5    player ontouch!" );
						player=P5;
						c_idx= c5Idx;
						P_tempcurve_x = p5TempcurveX;
						P_tempcurve_y = p5TempcurveY;
						pRotate = p5Rotate;
						player_paint= player5Paint;
						rotateWhichPlayer =5;
						Arrow= arrow5;
						P_startIndex= p5StartIndex;
						handle_name="P5_Handle";
						P_Initial_Position_x= p5InitialPositionX;
						P_Initial_Position_y= p5InitialPositionY;
						seekbar_player_Id=5;
						rcP5 =new Rect((int) event.getX(),my - v.getTop(),(int) event.getX()+ v.getWidth(),my - v.getTop()+v.getHeight());
						//Log,i("debug", "first    P_startIndex="+Integer.toString(P_startIndex));
						//Log,i("debug", "first    P5_startIndex="+Integer.toString(P5_startIndex));
						
						TimeLine timefrag1 = (TimeLine) getActivity().getFragmentManager().findFragmentById(R.id.time_line);
						timefrag1.changeLayout(5);
					}
					else if (v.getTag().toString().equals("6")){
						//Log,i("debug","B    player ontouch!" );
						player=B;
						rotateWhichPlayer = 6;
						c_idx= ballIdx;
						P_tempcurve_x = ballTempcurveX;
						P_tempcurve_y = ballTempcurveY;
						pRotate = 0;
						player_paint = ballPaint;
						Arrow=null;
						P_startIndex = bStartIndex;
						handle_name = "B_Handle";
						P_Initial_Position_x = bInitialPositionX;
						P_Initial_Position_y = bInitialPositionY;
						seekbar_player_Id = 6;
						rcBall =new Rect((int) event.getX(),my - v.getTop(),(int) event.getX()+ v.getWidth(),my - v.getTop()+v.getHeight());
						//Log,i("debug", "first    P_startIndex="+Integer.toString(P_startIndex));
						//Log,i("debug", "first    B_startIndex="+Integer.toString(B_startIndex));
						TimeLine timefrag1 = (TimeLine) getActivity().getFragmentManager().findFragmentById(R.id.time_line);
						timefrag1.changeLayout(6);
					}
					else if(v.getTag().toString().equals("D1")){
						//Log,i("debug","D1 ontouch!" );
						player=D1;
						c_idx= cd1Idx;
						P_tempcurve_x = d1TempcurveX;
						P_tempcurve_y = d1TempcurveY;
						pRotate = d1Rotate;
						player_paint= d1Paint;
						rotateWhichPlayer =7;
						Arrow= arrow7;
						P_startIndex= d1StartIndex;
						handle_name="D1_Handle";
						P_Initial_Position_x= d1InitialPositionX;
						P_Initial_Position_y= d1InitialPositionY;
						seekbar_player_Id=7;
						//Log,i("debug", "first    P_startIndex="+Integer.toString(P_startIndex));
						//Log,i("debug", "first    D1_startIndex="+Integer.toString(D1_startIndex));
						TimeLine timefrag1 = (TimeLine) getActivity().getFragmentManager().findFragmentById(R.id.time_line);
						timefrag1.changeLayout(7);
					}
					else if(v.getTag().toString().equals("D2")){
						//Log,i("debug","D2 ontouch!" );
						player=D2;
						c_idx= cd2Idx;
						P_tempcurve_x = d2TempcurveX;
						P_tempcurve_y = d2TempcurveY;
						pRotate = d2Rotate;
						player_paint= d2Paint;
						rotateWhichPlayer =8;
						Arrow= arrow8;
						P_startIndex= d2StartIndex;
						handle_name="D2_Handle";
						P_Initial_Position_x= d2InitialPositionX;
						P_Initial_Position_y= d2InitialPositionY;
						seekbar_player_Id=8;
						//Log,i("debug", "first    P_startIndex="+Integer.toString(P_startIndex));
						//Log,i("debug", "first    D2_startIndex="+Integer.toString(D2_startIndex));
						TimeLine timefrag1 = (TimeLine) getActivity().getFragmentManager().findFragmentById(R.id.time_line);
						timefrag1.changeLayout(8);
					}
					else if(v.getTag().toString().equals("D3")){
						//Log,i("debug","D3 ontouch!" );
						player=D3;
						c_idx= cd3Idx;
						P_tempcurve_x = d3TempcurveX;
						P_tempcurve_y = d3TempcurveY;
						pRotate = d3Rotate;
						player_paint= d3Paint;
						rotateWhichPlayer =9;
						Arrow= arrow9;
						P_startIndex= d3StartIndex;
						handle_name="D3_Handle";
						P_Initial_Position_x= d3InitialPositionX;
						P_Initial_Position_y= d3InitialPositionY;
						seekbar_player_Id=9;
						//Log,i("debug", "first    P_startIndex="+Integer.toString(P_startIndex));
						//Log,i("debug", "first    D3_startIndex="+Integer.toString(D3_startIndex));
						TimeLine timefrag1 = (TimeLine) getActivity().getFragmentManager().findFragmentById(R.id.time_line);
						timefrag1.changeLayout(9);
					}
					else if(v.getTag().toString().equals("D4")){
						//Log,i("debug","D4 ontouch!" );
						player=D4;
						c_idx= cd4Idx;
						P_tempcurve_x = d4TempcurveX;
						P_tempcurve_y = d4TempcurveY;
						pRotate = d4Rotate;
						player_paint= d4Paint;
						rotateWhichPlayer =10;
						Arrow= arrow10;
						P_startIndex= d4StartIndex;
						handle_name="D4_Handle";
						P_Initial_Position_x= d4InitialPositionX;
						P_Initial_Position_y= d4InitialPositionY;
						seekbar_player_Id=10;
						//Log,i("debug", "first    P_startIndex="+Integer.toString(P_startIndex));
						//Log,i("debug", "first    D4_startIndex="+Integer.toString(D4_startIndex));
						TimeLine timefrag1 = (TimeLine) getActivity().getFragmentManager().findFragmentById(R.id.time_line);
						timefrag1.changeLayout(10);
					}
					else if(v.getTag().toString().equals("D5")){
						//Log,i("debug","D5 ontouch!" );
						player=D5;
						c_idx= cd5Idx;
						P_tempcurve_x = d5TempcurveX;
						P_tempcurve_y = d5TempcurveY;
						pRotate = d5Rotate;
						player_paint= d5Paint;
						rotateWhichPlayer =11;
						Arrow= arrow11;
						P_startIndex= d5StartIndex;
						handle_name="D5_Handle";
						P_Initial_Position_x= d5InitialPositionX;
						P_Initial_Position_y= d5InitialPositionY;
						seekbar_player_Id=11;
						//Log,i("debug", "first    P_startIndex="+Integer.toString(P_startIndex));
						//Log,i("debug", "first    D5_startIndex="+Integer.toString(D5_startIndex));
						TimeLine timefrag1 = (TimeLine) getActivity().getFragmentManager().findFragmentById(R.id.time_line);
						timefrag1.changeLayout(11);
					}
					else{
						//Log,i("debug", "noooooooooo");
					}
					
					startTime = System.currentTimeMillis();
					move_count = 1;
					dum_flag = false;
					
					Pbitmap = Bitmap.createBitmap(circle.getWidth(),circle.getHeight(),Bitmap.Config.ARGB_8888);//初始化tempBitmap，指定大小為螢幕大小(大小同circle)
					Pcanvas = new Canvas();
					Pcanvas = new Canvas(Pbitmap);//把P1canvas畫的東西畫到P1bitmap上面

					
					P_curve_x = new Vector();
					P_curve_y = new Vector();
					
					startX = (int) event.getX();
					startY = my - v.getTop();
					if (recordCheck == true) {
						player.setRoad(0); // split positions
						player.setRoad3D(0);
						player.setMyRotation(-1);
					}

					//Pcanvas.drawCircle(mx - startX + v.getWidth()/2, my - startY + v.getHeight()/2, 20, player_paint);
					line_start_point_x = mx - startX + v.getWidth()/2;
					line_start_point_y = my - startY + v.getHeight()/2;

					return true;


                /*移動圖片***************************************************************************************************/
				case MotionEvent.ACTION_MOVE:// 移動圖片時
					x = mx - startX;
					y = my - startY;
					
					//x, y, x + v.getWidth(), y + v.getHeight()
					if(v.getTag().toString().equals("1")){
						rcP1 =new Rect(x,y,x+ v.getWidth(),y+v.getHeight());
					}
					else if(v.getTag().toString().equals("2")){
						rcP2 =new Rect(x,y,x+ v.getWidth(),y+v.getHeight());
					}
					else if(v.getTag().toString().equals("3")){
						rcP3 =new Rect(x,y,x+ v.getWidth(),y+v.getHeight());
					}
					else if(v.getTag().toString().equals("4")){
						rcP4 =new Rect(x,y,x+ v.getWidth(),y+v.getHeight());
					}
					else if(v.getTag().toString().equals("5")){
						rcP5 =new Rect(x,y,x+ v.getWidth(),y+v.getHeight());
					}
					else if(v.getTag().toString().equals("6")){
						rcBall =new Rect(x,y,x+ v.getWidth(),y+v.getHeight());

                        //region 這裡是先把目前持有球的player先變回無持球狀態，再接著判斷有沒有intersect，確保當兩個player太接近的時候，會導致player明明無持球卻還是呈現有持球的狀態*/
						playerChangeToNoBall();
                        //endregion

                        //region 判斷是跟哪個player intersect
						if(Rect.intersects(rcP1, rcBall)){
							//Log,i("debug", "P1 Intersects!");
							intersectName =1;
							intersect=true;
							if(player1Ball.getVisibility()== player1Ball.INVISIBLE){
								player1Ball.layout((int)player1.getX()-30, (int)player1.getY(), (int)player1.getX()-30+200, (int)player1.getY()+120);
								player1Ball.setVisibility(player1Ball.VISIBLE);
								player1Ball.invalidate();
								player1.setVisibility(player1.INVISIBLE);
								player1.invalidate();
							}
						}
						else if (Rect.intersects(rcP2, rcBall)){
							//Log,i("debug", "P2 Intersects!");
							intersectName =2;
							intersect=true;
							if(player2Ball.getVisibility()== player2Ball.INVISIBLE){
								player2Ball.layout((int)player2.getX()-30, (int)player2.getY(), (int)player2.getX()-30+200, (int)player2.getY()+120);
								player2Ball.setVisibility(player2Ball.VISIBLE);
								player2Ball.invalidate();
								player2.setVisibility(player2.INVISIBLE);
								player2.invalidate();
							}
						}
						else if (Rect.intersects(rcP3, rcBall)){
							//Log,i("debug", "P3 Intersects!");
							intersectName =3;
							intersect=true;
							if(player3Ball.getVisibility()== player3Ball.INVISIBLE){
								player3Ball.layout((int)player3.getX()-30, (int)player3.getY(), (int)player3.getX()-30+200, (int)player3.getY()+120);
								player3Ball.setVisibility(player3Ball.VISIBLE);
								player3Ball.invalidate();
								player3.setVisibility(player3.INVISIBLE);
								player3.invalidate();
							}
						}
						else if (Rect.intersects(rcP4, rcBall)){
							//Log,i("debug", "P4 Intersects!");
							intersectName =4;
							intersect=true;
							if(player4Ball.getVisibility()== player4Ball.INVISIBLE){
								player4Ball.layout((int)player4.getX()-30, (int)player4.getY(), (int)player4.getX()-30+200, (int)player4.getY()+120);
								player4Ball.setVisibility(player4Ball.VISIBLE);
								player4Ball.invalidate();
								player4.setVisibility(player4.INVISIBLE);
								player4.invalidate();
							}
						}
						else if (Rect.intersects(rcP5, rcBall)){
							//Log,i("debug", "P5 Intersects!");
							intersectName =5;
							intersect=true;
							if(player5Ball.getVisibility()== player5Ball.INVISIBLE){
								player5Ball.layout((int)player5.getX()-30, (int)player5.getY(), (int)player5.getX()-30+200, (int)player5.getY()+120);
								player5Ball.setVisibility(player5Ball.VISIBLE);
								player5Ball.invalidate();
								player5.setVisibility(player5.INVISIBLE);
								player5.invalidate();
							}
						}
						else{
							switch (intersectName){
								case 1:
									player1.setVisibility(player1.VISIBLE);
									if(intersect==true){
										////Log,i("debug","intersect=true,player1 should be layout on player_ball's position");
										player1.layout((int) player1Ball.getX()+30, (int) player1Ball.getY(),(int) player1Ball.getX()+30+player1.getWidth(), (int) player1Ball.getY()+player1.getHeight());
									}
									player1.invalidate();
									player1Ball.setVisibility(player1Ball.INVISIBLE);
									player1Ball.invalidate();
									break;
								case 2:
									player2.setVisibility(player2.VISIBLE);
									if(intersect==true){
										////Log,i("debug","intersect=true,player1 should be layout on player_ball's position");
										player2.layout((int) player2Ball.getX()+30, (int) player2Ball.getY(),(int) player2Ball.getX()+30+player2.getWidth(), (int) player2Ball.getY()+player2.getHeight());
									}
									player2.invalidate();
									player2Ball.setVisibility(player2Ball.INVISIBLE);
									player2Ball.invalidate();
									break;
								case 3:
									player3.setVisibility(player3.VISIBLE);
									if(intersect==true){
										////Log,i("debug","intersect=true,player1 should be layout on player_ball's position");
										player3.layout((int) player3Ball.getX()+30, (int) player3Ball.getY(),(int) player3Ball.getX()+30+player3.getWidth(), (int) player3Ball.getY()+player3.getHeight());
									}
									player3.invalidate();
									player3Ball.setVisibility(player3Ball.INVISIBLE);
									player3Ball.invalidate();
									break;
								case 4:
									player4.setVisibility(player4.VISIBLE);
									if(intersect==true){
										////Log,i("debug","intersect=true,player1 should be layout on player_ball's position");
										player4.layout((int) player4Ball.getX()+30, (int) player4Ball.getY(),(int) player4Ball.getX()+30+player4.getWidth(), (int) player4Ball.getY()+player4.getHeight());
									}
									player4.invalidate();
									player4Ball.setVisibility(player4Ball.INVISIBLE);
									player4Ball.invalidate();
									break;
								case 5:
									player5.setVisibility(player5.VISIBLE);
									if(intersect==true){
										////Log,i("debug","intersect=true,player1 should be layout on player_ball's position");
										player5.layout((int) player5Ball.getX()+30, (int) player5Ball.getY(),(int) player5Ball.getX()+30+player5.getWidth(), (int) player5Ball.getY()+player5.getHeight());
									}
									player5.invalidate();
									player5Ball.setVisibility(player5Ball.INVISIBLE);
									player5Ball.invalidate();
									break;
							}

							intersect=false;
							intersectName = 0;
						}
						//endregion
					}


					//region 當正在繪製軌跡時
					if (recordCheck == true) {
						move_count += 2;
						player.setRoad(x);
						player.setRoad(y);
						player.setRoad3D((int) event.getRawX());
						player.setRoad3D((int) event.getRawY());
						player.setMyRotation(pRotate);
						//Log,i("debug", "player setMyRotation");
						
						P_tempcurve_x.add(c_idx, x);
						P_tempcurve_y.add(c_idx, y);
						c_idx++;
						
						P_curve_x.add((float)mx);
						P_curve_y.add((float)my);

						// 每畫三個點
						if(c_idx == N){
							boolean isBallHolder = (rotateWhichPlayer == intersectName);
							Log.i("debug", "Touch Event : "+ rotateWhichPlayer +", "+ intersectName);
							Log.i("debug", "sample : "+sample_rate+"  "+ handle_name);
							boolean whetherDraw = ((sample_rate >= 1.0F) && handle_name.equals("B_Handle")) || (!handle_name.equals("B_Handle") && !isBallHolder);

							//region 找到畫出的路徑相對於平板坐標系的旋轉角度，才能知道掩護的線要畫在哪裡的垂直角度上
							float pivot_dir_x = 0.0f;
							float pivot_dir_y = -1.0f;
							float pivot_dir_x2 = 1.0f;
							float pivot_dir_y2 = 0.0f;

							float target_dir_x = P_tempcurve_x.get(2) - P_tempcurve_x.get(1);
							float target_dir_y = P_tempcurve_y.get(2) - P_tempcurve_y.get(1);
							float dot_of_two = pivot_dir_x*target_dir_x + pivot_dir_y*target_dir_y;
							float pivot_length = 1.0f;
							float target_length = (float)Math.sqrt(Math.pow(target_dir_x, 2) + Math.pow(target_dir_y, 2));
							double cos_val = dot_of_two/target_length/pivot_length;
							screen_direction = 180.0f * (float)Math.acos(cos_val)/3.1415f;

							//region 用來計算掩護橫線應該畫的方向
							float degree_threshold = 10f;
							if( Math.abs(screen_direction) < degree_threshold){
								screen_direction = 90.0f;
							}
							else if( Math.abs(screen_direction - 90.0f) < degree_threshold){
								screen_direction = 0.0f;
							}
							else if(Math.abs(screen_direction - 180.0f) < degree_threshold){
								screen_direction = 90.0f;
							}
							else {

								//   |
								// --| +-
								//------->x
								//   |
								// -+| ++
								//   V

								// 如果是在四相中的任何一相(不包含90度 180度 270度)
								if ((target_dir_x > 0.0f) && (target_dir_y < 0.0f)) {
									screen_direction = 180.0f - screen_direction;

								} else if ((target_dir_x > 0.0f) && (target_dir_y > 0.0f)) {
									screen_direction = 180.0f - screen_direction;

								} else if ((target_dir_x < 0.0f) && (target_dir_y > 0.0f)) {
									screen_direction = -(180.0f - screen_direction);

								} else if ((target_dir_x < 0.0f) && (target_dir_y < 0.0f)) {
									screen_direction = screen_direction;
								}
							}
							//endregion

							//region 用來計算運球圖片應該放的角度(根據球員前進的路線)
							float dot_of_two2 = pivot_dir_x2*target_dir_x + pivot_dir_y2*target_dir_y;
							double cos_val2 = dot_of_two2/target_length/pivot_length;


							last_direction = 180.0f * (float)Math.acos(cos_val2)/3.1415f;
							////Log,i("debug", "target direction: "+ target_dir_x + ", " + target_dir_y);

							if(Math.abs(last_direction) < degree_threshold){
								last_direction = 0.0f;
							}
							else if(Math.abs(last_direction - 90.0f) < degree_threshold){
								if(target_dir_y < 0.0f){
									last_direction = 270.0f;
								}
								else{
									last_direction = 90.0f;
								}
							}
							else if(Math.abs(last_direction - 180.0f) < degree_threshold){
								last_direction = 180.0f;
							}
							else{
								//   |
								// --| +-
								//------->x (這個方向是基準)
								//   |
								// -+| ++
								//   V

								// 如果是在四相中的任何一相(不包含90度 180度 270度)
								if ((target_dir_x > 0.0f) && (target_dir_y < 0.0f)) {
									last_direction = 360.0f - last_direction;

								} else if ((target_dir_x > 0.0f) && (target_dir_y > 0.0f)) {
									last_direction = last_direction;

								} else if ((target_dir_x < 0.0f) && (target_dir_y > 0.0f)) {
									last_direction = last_direction;

								} else if ((target_dir_x < 0.0f) && (target_dir_y < 0.0f)) {
									last_direction = 360.f - last_direction;
								}
							}
							//endregion

							previousDirection.add(screen_direction);
							previousDribbleDirection.add(last_direction);
							//endregion

							if( whetherDraw) {
								//region 內插出三點的曲線
								for (int tmp = 1; tmp < N; tmp++) {
									if (P_tempcurve_x.get(tmp - 1) < P_tempcurve_x.get(tmp)) {// x遞增
										if (tmp == 1 && ((int) P_tempcurve_x.get(1) == (int) P_tempcurve_x.get(2))) {//第二、三格一樣，給lagrange的陣列不能有x一樣的情況，所以只能給lagrange兩格(一、二格)
											Vector<Integer> tmp_curve_x = new Vector(), tmp_curve_y = new Vector();
											tmp_curve_x.add(P_tempcurve_x.get(0));
											tmp_curve_x.add(P_tempcurve_x.get(1));
											tmp_curve_y.add(P_tempcurve_y.get(0));
											tmp_curve_y.add(P_tempcurve_y.get(1));

											for (float tmp_x = P_tempcurve_x.get(tmp - 1); tmp_x <= P_tempcurve_x.get(tmp); tmp_x = tmp_x + (float) 0.1) {
												tmp_y = lagrange(tmp_curve_x, tmp_curve_y, tmp_x);
												Pcanvas.drawCircle(tmp_x + v.getWidth() / 2, tmp_y + v.getHeight() / 2, 5, player_paint);
											}

										} else if (tmp == 2 && ((int) P_tempcurve_x.get(0) == (int) P_tempcurve_x.get(1))) {//第一、二格一樣
											Vector<Integer> tmp_curve_x = new Vector(), tmp_curve_y = new Vector();
											tmp_curve_x.add(P_tempcurve_x.get(1));
											tmp_curve_x.add(P_tempcurve_x.get(2));
											tmp_curve_y.add(P_tempcurve_y.get(1));
											tmp_curve_y.add(P_tempcurve_y.get(2));
											for (float tmp_x = P_tempcurve_x.get(tmp - 1); tmp_x <= P_tempcurve_x.get(tmp); tmp_x = tmp_x + (float) 0.1) {
												tmp_y = lagrange(tmp_curve_x, tmp_curve_y, tmp_x);
												Pcanvas.drawCircle(tmp_x + v.getWidth() / 2, tmp_y + v.getHeight() / 2, 5, player_paint);//black
											}
										} else {
											if (tmp == 1 && (int) P_tempcurve_x.get(1) > (int) P_tempcurve_x.get(2)) {//1<2   2>3
												Vector<Integer> tmp_curve_x = new Vector(), tmp_curve_y = new Vector();
												tmp_curve_x.add(P_tempcurve_x.get(0));
												tmp_curve_x.add(P_tempcurve_x.get(1));
												tmp_curve_y.add(P_tempcurve_y.get(0));
												tmp_curve_y.add(P_tempcurve_y.get(1));
												for (float tmp_x = P_tempcurve_x.get(tmp - 1); tmp_x <= P_tempcurve_x.get(tmp); tmp_x = tmp_x + (float) 0.1) {
													tmp_y = lagrange(tmp_curve_x, tmp_curve_y, tmp_x);
													Pcanvas.drawCircle(tmp_x + v.getWidth() / 2, tmp_y + v.getHeight() / 2, 5, player_paint);//black
												}
											} else if (tmp == 2 && ((int) P_tempcurve_x.get(0) > (int) P_tempcurve_x.get(1))) {//1>2   2<3
												Vector<Integer> tmp_curve_x = new Vector(), tmp_curve_y = new Vector();
												tmp_curve_x.add(P_tempcurve_x.get(1));
												tmp_curve_x.add(P_tempcurve_x.get(2));
												tmp_curve_y.add(P_tempcurve_y.get(1));
												tmp_curve_y.add(P_tempcurve_y.get(2));
												for (float tmp_x = P_tempcurve_x.get(tmp - 1); tmp_x <= P_tempcurve_x.get(tmp); tmp_x = tmp_x + (float) 0.1) {
													tmp_y = lagrange(tmp_curve_x, tmp_curve_y, tmp_x);
													Pcanvas.drawCircle(tmp_x + v.getWidth() / 2, tmp_y + v.getHeight() / 2, 5, player_paint);//black
												}
											} else {
												for (float tmp_x = P_tempcurve_x.get(tmp - 1); tmp_x <= P_tempcurve_x.get(tmp); tmp_x = tmp_x + (float) 0.1) {
													tmp_y = lagrange(P_tempcurve_x, P_tempcurve_y, tmp_x);
													Pcanvas.drawCircle(tmp_x + v.getWidth() / 2, tmp_y + v.getHeight() / 2, 5, player_paint);//black
												}
											}
										}
									} else if (P_tempcurve_x.get(tmp - 1) > P_tempcurve_x.get(tmp)) {//x遞減
										if (tmp == 1 && ((int) P_tempcurve_x.get(1) == (int) P_tempcurve_x.get(2))) {//第二、三格一樣，給lagrange的陣列不能有x一樣的情況，所以只能給lagrange兩格(一、二格)
											Vector<Integer> tmp_curve_x = new Vector(), tmp_curve_y = new Vector();
											tmp_curve_x.add(P_tempcurve_x.get(0));
											tmp_curve_x.add(P_tempcurve_x.get(1));
											tmp_curve_y.add(P_tempcurve_y.get(0));
											tmp_curve_y.add(P_tempcurve_y.get(1));
											for (float tmp_x = P_tempcurve_x.get(tmp - 1); tmp_x >= P_tempcurve_x.get(tmp); tmp_x = tmp_x - (float) 0.1) {
												tmp_y = lagrange(tmp_curve_x, tmp_curve_y, tmp_x);
												Pcanvas.drawCircle(tmp_x + v.getWidth() / 2, tmp_y + v.getHeight() / 2, 5, player_paint);//red
											}
										} else if (tmp == 2 && ((int) P_tempcurve_x.get(0) == (int) P_tempcurve_x.get(1))) {//第一、二格一樣
											Vector<Integer> tmp_curve_x = new Vector(), tmp_curve_y = new Vector();
											tmp_curve_x.add(P_tempcurve_x.get(1));
											tmp_curve_x.add(P_tempcurve_x.get(2));
											tmp_curve_y.add(P_tempcurve_y.get(1));
											tmp_curve_y.add(P_tempcurve_y.get(2));
											for (float tmp_x = P_tempcurve_x.get(tmp - 1); tmp_x >= P_tempcurve_x.get(tmp); tmp_x = tmp_x - (float) 0.1) {
												tmp_y = lagrange(tmp_curve_x, tmp_curve_y, tmp_x);
												Pcanvas.drawCircle(tmp_x + v.getWidth() / 2, tmp_y + v.getHeight() / 2, 5, player_paint);//red
											}
										}//都不一樣
										else {
											if (tmp == 1 && (int) P_tempcurve_x.get(1) < (int) P_tempcurve_x.get(2)) {//1>2   2<3
												Vector<Integer> tmp_curve_x = new Vector(), tmp_curve_y = new Vector();
												tmp_curve_x.add(P_tempcurve_x.get(0));
												tmp_curve_x.add(P_tempcurve_x.get(1));
												tmp_curve_y.add(P_tempcurve_y.get(0));
												tmp_curve_y.add(P_tempcurve_y.get(1));
												for (float tmp_x = P_tempcurve_x.get(tmp - 1); tmp_x >= P_tempcurve_x.get(tmp); tmp_x = tmp_x - (float) 0.1) {
													tmp_y = lagrange(tmp_curve_x, tmp_curve_y, tmp_x);
													Pcanvas.drawCircle(tmp_x + v.getWidth() / 2, tmp_y + v.getHeight() / 2, 5, player_paint);//red
												}
											} else if (tmp == 2 && ((int) P_tempcurve_x.get(0) < (int) P_tempcurve_x.get(1))) {//1<2   2>3
												Vector<Integer> tmp_curve_x = new Vector(), tmp_curve_y = new Vector();
												tmp_curve_x.add(P_tempcurve_x.get(1));
												tmp_curve_x.add(P_tempcurve_x.get(2));
												tmp_curve_y.add(P_tempcurve_y.get(1));
												tmp_curve_y.add(P_tempcurve_y.get(2));
												for (float tmp_x = P_tempcurve_x.get(tmp - 1); tmp_x >= P_tempcurve_x.get(tmp); tmp_x = tmp_x - (float) 0.1) {
													tmp_y = lagrange(tmp_curve_x, tmp_curve_y, tmp_x);
													Pcanvas.drawCircle(tmp_x + v.getWidth() / 2, tmp_y + v.getHeight() / 2, 5, player_paint);//red
												}
											} else {
												for (float tmp_x = P_tempcurve_x.get(tmp - 1); tmp_x >= P_tempcurve_x.get(tmp); tmp_x = tmp_x - (float) 0.1) {
													tmp_y = lagrange(P_tempcurve_x, P_tempcurve_y, tmp_x);
													Pcanvas.drawCircle(tmp_x + v.getWidth() / 2, tmp_y + v.getHeight() / 2, 5, player_paint);//red
												}
											}
										}
									} else {//==  line 其中兩格一樣的話，就兩格之間畫直線
										int tmp_x = P_tempcurve_x.get(tmp - 1);
										if (P_tempcurve_y.get(tmp - 1) > P_tempcurve_y.get(tmp)) {
											for (float tmp_y = P_tempcurve_y.get(tmp - 1); tmp_y >= P_tempcurve_y.get(tmp); tmp_y = tmp_y - (float) 0.1) {
												Pcanvas.drawCircle(tmp_x + v.getWidth() / 2, tmp_y + v.getHeight() / 2, 5, player_paint);//blue
											}
										} else {
											for (float tmp_y = P_tempcurve_y.get(tmp - 1); tmp_y <= P_tempcurve_y.get(tmp); tmp_y = tmp_y + (float) 0.1) {
												Pcanvas.drawCircle(tmp_x + v.getWidth() / 2, tmp_y + v.getHeight() / 2, 5, player_paint);//green
											}
										}
									}
								}

								//endregion
								sample_rate = 0.0f;
							}
							else if(handle_name.equals("B_Handle")){
								sample_rate = sample_rate + 1.0f;
							}
							c_idx=0;
							P_tempcurve_x.clear();
							P_tempcurve_y.clear();
							P_tempcurve_x.add(c_idx, x);
							P_tempcurve_y.add(c_idx, y);
							c_idx++;

						}
						
						tempCanvas.drawBitmap(Pbitmap, 0,0, null);
						Pcanvas.drawBitmap(Pbitmap, 0, 0, transparentPaint);

						circle.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
					}
					//endregion

					
					if(Arrow!=null){
						Arrow.layout(x, y, x + v.getWidth(), y + v.getHeight());
						Arrow.setRotation(pRotate);
						TimeLine timefrag1 = (TimeLine) getActivity().getFragmentManager().findFragmentById(R.id.time_line);
						timefrag1.setCircularSeekBarProgress(pRotate);//為了讓circular seekbar的值也一起變成儲存的狀態，但是因為android好像有bug，所以他不會更新介面上的seekbar的樣子，但值卻是有改過的
						//Log.d("debug", "setCircularSeekBarProgress P1_rotate="+Integer.toString(P1_rotate));
						Arrow.postInvalidate();
					}	
					if(intersect==true && v.getTag().toString().equals("6")==false && v.getTag().toString().equals(Integer.toString(intersectName))){
						ball.layout(x+110, y+30, x+170, y+90);
					}
					v.layout(x, y, x + v.getWidth(), y + v.getHeight());
					v.postInvalidate();
					////Log,i("debug", "x="+Integer.toString(x));
					////Log,i("debug", "y="+Integer.toString(y));
					return true;

                    /*放開圖片***************************************************************************************************/
                    case MotionEvent.ACTION_UP://放開照片時
					////Log,i("debug", "intersect_name_pre="+Integer.toString(intersect_name_pre));
					////Log,i("debug", "intersect_name="+Integer.toString(intersect_name));

					if(v.getTag().toString().equals("6")&&intersect==true){
						switch(intersectName){
							case 1:
								if(player1.getVisibility()==player1.INVISIBLE){//代表是player1_ball顯示中
									arrow1.layout((int) player1Ball.getX(), (int) player1Ball.getY(), (int) player1Ball.getX()+ player1Ball.getWidth(), (int) player1Ball.getY()+ player1Ball.getHeight());
								}
								arrow1.invalidate();
								v.layout((int) player1.getX()+110-30, (int)player1.getY()+30, (int) player1.getX()+170-30,(int)player1.getY()+90);
								break;
							case 2:
								if(player2.getVisibility()==player2.INVISIBLE){//代表是player2_ball顯示中
									arrow2.layout((int) player2Ball.getX(), (int) player2Ball.getY(), (int) player2Ball.getX()+ player2Ball.getWidth(), (int) player2Ball.getY()+ player2Ball.getHeight());
								}
								arrow2.invalidate();
								v.layout((int) player2.getX()+110-30, (int)player2.getY()+30, (int) player2.getX()+170-30,(int)player2.getY()+90);
								break;
							case 3:
								if(player3.getVisibility()==player3.INVISIBLE){//代表是player3_ball顯示中
									arrow3.layout((int) player3Ball.getX(), (int) player3Ball.getY(), (int) player3Ball.getX()+ player3Ball.getWidth(), (int) player3Ball.getY()+ player3Ball.getHeight());
								}
								arrow3.invalidate();
								v.layout((int) player3.getX()+110-30, (int)player3.getY()+30, (int) player3.getX()+170-30,(int)player3.getY()+90);
								break;
							case 4:
								if(player4.getVisibility()==player4.INVISIBLE){//代表是player4_ball顯示中
									arrow4.layout((int) player4Ball.getX(), (int) player4Ball.getY(), (int) player4Ball.getX()+ player4Ball.getWidth(), (int) player4Ball.getY()+ player4Ball.getHeight());
								}
								arrow4.invalidate();
								v.layout((int) player4.getX()+110-30, (int)player4.getY()+30, (int) player4.getX()+170-30,(int)player4.getY()+90);	
								break;
							case 5:
								if(player5.getVisibility()==player5.INVISIBLE){//代表是player5_ball顯示中
									arrow5.layout((int) player5Ball.getX(), (int) player5Ball.getY(), (int) player5Ball.getX()+ player5Ball.getWidth(), (int) player5Ball.getY()+ player5Ball.getHeight());
								}
								arrow5.invalidate();
								v.layout((int) player5.getX()+110-30, (int)player5.getY()+30, (int) player5.getX()+170-30,(int)player5.getY()+90);
								break;
						
						}
						v.invalidate();
					}
					else if(v.getTag().toString().equals("6") && intersect == false){
						switch(intersectName){
							case 1:
								if(player1.getVisibility() == player1.VISIBLE){
									arrow1.layout((int)player1.getX(), (int)player1.getY(), (int)player1.getX()+player1.getWidth(), (int)player1.getY()+player1.getHeight());
								}
								arrow1.invalidate();
								break;
							case 2:
								if(player2.getVisibility() == player2.VISIBLE){
									arrow2.layout((int)player2.getX(), (int)player2.getY(), (int)player2.getX()+player2.getWidth(), (int)player2.getY()+player2.getHeight());
								}
								arrow2.invalidate();
								break;
							case 3:
								if(player3.getVisibility() == player3.VISIBLE){
									arrow3.layout((int)player3.getX(), (int)player3.getY(), (int)player3.getX()+player3.getWidth(), (int)player3.getY()+player3.getHeight());
								}
								arrow3.invalidate();
								break;
							case 4:
								if(player4.getVisibility() == player4.VISIBLE){
									arrow4.layout((int)player4.getX(), (int)player4.getY(), (int)player4.getX()+player4.getWidth(), (int)player4.getY()+player4.getHeight());
								}
								arrow4.invalidate();
								break;
							case 5:
								if(player5.getVisibility() == player5.VISIBLE){
									arrow5.layout((int)player5.getX(), (int)player5.getY(), (int)player5.getX()+player5.getWidth(), (int)player5.getY()+player5.getHeight());
								}
								arrow5.invalidate();
								break;
						}
					}

					//region 防呆，畫的時間太短的話，不會採用
					if (recordCheck == true) {
						endTime=System.currentTimeMillis();
						////Log,i("debug", "touch time ="+Long.toString(endTime-startTime));
						////Log,i("debug", "move_count="+Integer.toString(move_count));
						if(endTime-startTime>250){
							dum_flag=true;
							//Log,i("debug", "dum_flag=true");
						}
						else{
							////Log,i("debug", "dum_flag=falseeeeeeeee");
							dum_flag=false;
							tempBitmap = Bitmap.createBitmap(circle.getWidth(),circle.getHeight(),Bitmap.Config.ARGB_8888);//初始化tempBitmap，指定大小為螢幕大小(大小同circle)
				    		tempCanvas = new Canvas();
				           	tempCanvas = new Canvas(tempBitmap);//畫透明路徑
				           	for(int tmp = 0; tmp< bitmapVector.size(); tmp++){
				           			tempCanvas.drawBitmap(bitmapVector.get(tmp), 0, 0, null);
				           	}
				    		circle.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));//把tempBitmap放進circle裡
							for(int i=0;i<move_count;i++){
								player.getCmpltRoad().remove(player.getRoadSize()-1);
								player.getCmpltRoad_3d().remove(player.getRoadSize_3d()-1);
							}	
						}
						
						if(dum_flag == true){
							TimeLine timefrag = (TimeLine) getActivity().getFragmentManager().findFragmentById(R.id.time_line);
							////Log,i("debug", "Test for run into this block.");
							
							P_curve_x.add((float)x);
							P_curve_y.add((float)y);
							curves.add(P_curve_x);
							curves.add(P_curve_y);


							///call MainWrap 's function
							MainWrap mainwrapfrag = (MainWrap) getActivity().getFragmentManager().findFragmentById(R.id.MainWrap_frag);
							mainwrapfrag.create_path_num_on_court(runBags.size()+1, x, y, runBags.size());

							//region 用來畫出掩護的線

							// 依照每5度為一個bin來統計方向趨勢
							// 根據過去的所計算出來的方向來統計
							// 把最後最有可能的方向趨勢作為掩護screen_bar要旋轉的方向

							// direction_hist 用來統計最有可能的方向趨勢，每5度一個bin
							Vector<Integer> direction_hist, dribble_direction_hist;
							direction_hist = new Vector<Integer>();
							dribble_direction_hist = new Vector<Integer>();
							for(int i=0 ; i<72 ; i++){
								direction_hist.add(0);
								dribble_direction_hist.add(0);
							}

							// 統計最近5個線段的方向趨勢
							int prev_dir_length = previousDirection.size();
							int sample_length = (prev_dir_length > 5)?5:prev_dir_length;

							for(int i=1 ; i<sample_length ; i++){
								float angle_trans = previousDirection.get(prev_dir_length - i);
								float dri_angle_trans = previousDribbleDirection.get(prev_dir_length - i);
								// 前面設的previous_direction的方向有些是負的
								// 如果不把她轉成正的，vector取到的index就會是負的
								if(previousDirection.get(prev_dir_length - i) < 0.0f){
									angle_trans = 360.0f + angle_trans;
								}
								if(previousDribbleDirection.get(prev_dir_length - i) < 0.0f){
									dri_angle_trans = 360.0f + angle_trans;
								}

								int prev_hist_value = direction_hist.get( Math.round(angle_trans/10));
								direction_hist.set( Math.round(angle_trans/10) , prev_hist_value+1);

								int dri_prev_hist_value = dribble_direction_hist.get(Math.round(dri_angle_trans/10));
								dribble_direction_hist.set(Math.round(dri_angle_trans/10), dri_prev_hist_value+1);
							}

							// 找出統計出來最大的方向趨勢
							int max_index = 0;
							int max_value = direction_hist.get(0);

							int dri_max_index = 0;
							int dri_max_value = dribble_direction_hist.get(0);

							for(int i=1;i<direction_hist.size();i++){
								if(direction_hist.get(i) > max_value){
									max_value = direction_hist.get(i);
									max_index = i;
								}

								if(dribble_direction_hist.get(i) > dri_max_value){
									dri_max_value = dribble_direction_hist.get(i);
									dri_max_index = i;
								}
							}

							previousDirection.clear();
							previousDribbleDirection.clear();

							////Log,i("debug", "Screen direction : " + screen_direction);
							////Log,i("debug", "Last direction : "+ last_direction);

							screen_direction = max_index*10.0f + 5.0f;

							if(isScreenEnable)
								mainwrapfrag.create_screen_bar(x, y, rotateWhichPlayer,  screen_direction, runBags.size());
							//endregion

							last_direction = dri_max_index*10.0f + 5.0f;
							boolean isBallHolder = (rotateWhichPlayer == intersectName);
							float drawn_length = (float)Math.sqrt( Math.pow(x + v.getWidth()/2 - line_start_point_x, 2) + Math.pow(y + v.getHeight()/2 - line_start_point_y, 2));
							////Log,i("debug","Drawn length: "+ drawn_length);
							drawn_length = drawn_length / 150.0f / 2.0f;

							if(isBallHolder)
								mainwrapfrag.create_dribble_line((int)line_start_point_x, (int)line_start_point_y, rotateWhichPlayer, last_direction, drawn_length, runBags.size());

							Pcanvas.drawCircle((int)line_start_point_x, (int)line_start_point_y, 10, player_paint);

							bitmapVector.add(Pbitmap);
							/*//Log,i("debug", "bitmap_size="+Integer.toString(Bitmap_vector.size()));
							//Log,i("debug", "P1_curve_x_size="+Integer.toString(P1.getCmpltCurve().get(tmp).size()));
							//Log,i("debug", "P1_curve_y_size="+Integer.toString(P1.getCmpltCurve().get(tmp+1).size()));*/
							
							int startIndexTmp = P_startIndex;
							////Log,i("debug", "dum_flag = true , P_startIndex ="+Integer.toString(P_startIndex));
							while (P_startIndex != -1) {
								RunBag tmp = new RunBag();
								tmp.setStartTime(seekBarCallbackStartTime);
								tmp.setHandler(handle_name);
								tmp.setRoadStart(P_startIndex + 1);
								P_startIndex += 2;
								P_startIndex = player.getRoad(0, P_startIndex);
								if (P_startIndex == -1) {
									startIndexTmp = player.getLastRoad();
									tmp.setRoadEnd(startIndexTmp);
								} else {
									tmp.setRoadEnd(P_startIndex);
									startIndexTmp = P_startIndex;
								}
								tmp.setDuration(seekBarCallbackDuration);
								tmp.setTimeLineId(runBags.size());
								
								if(intersectNamePre == intersectName){
									tmp.setBallNum(0);
								}
								else{
									tmp.setBallNum(intersectName);
								}

								//region 20180712 在Runbag中加入掩護及運球的資訊
								if(isBallHolder){
									tmp.setPathType(2);
									tmp.setDribbleAngle(last_direction);
									tmp.setDribbleLength(drawn_length);
									tmp.setDribbleStartX((int)line_start_point_x);
									tmp.setDribbleStartY((int)line_start_point_y);
								}
								else if(isScreenEnable){
									tmp.setPathType(1);
									tmp.setScreenAngle(screen_direction);
								}
								else{
									tmp.setPathType(0);
								}
								//endregion

								runBags.add(tmp);
								timefrag.setRunLineId(runBags.size()-1);
								timefrag.setSeekBarId(runBags.size()-1);
								
								seekbarTmpId++;
								timefrag.setSeekBarProgressLow(mainFragSeekBarProgressLow);
								mainFragSeekBarProgressLow++;
								timefrag.createSeekbar(seekbar_player_Id);
							}
							P_startIndex = startIndexTmp + 1;
						}
					}
					else{
						if(runBags.size()==0){
							P_Initial_Position_x=x;
							P_Initial_Position_y=y;
						}
					}
					//endregion
					
					if(v.getTag().toString().equals("1")){
						P1=player;
						c1Idx =c_idx;
						p1TempcurveX = P_tempcurve_x;
						p1TempcurveY = P_tempcurve_y;
						p1Rotate = pRotate;
						p1StartIndex =P_startIndex;
						handle_name="P1_Handle";
						p1InitialPositionX =P_Initial_Position_x;
						p1InitialPositionY =P_Initial_Position_y;
						seekbar_player_Id=1;
						////Log,i("debug", "P_startIndex="+Integer.toString(P_startIndex));
						////Log,i("debug", "P1_startIndex="+Integer.toString(P1_startIndex));
						////Log,i("debug", "P1_RoadSize="+Integer.toString(P1.getRoadSize()));
					}
					else if (v.getTag().toString().equals("2")){
						P2=player;
						c2Idx =c_idx;
						p2TempcurveX = P_tempcurve_x;
						p2TempcurveY = P_tempcurve_y;
						p2Rotate = pRotate;
						p2StartIndex =P_startIndex;
						handle_name="P2_Handle";
						p2InitialPositionX =P_Initial_Position_x;
						p2InitialPositionY =P_Initial_Position_y;
						seekbar_player_Id=2;
						////Log,i("debug", "P_startIndex="+Integer.toString(P_startIndex));
						////Log,i("debug", "P2_startIndex="+Integer.toString(P2_startIndex));
					}
					else if (v.getTag().toString().equals("3")){
						P3=player;
						c3Idx =c_idx;
						p3TempcurveX = P_tempcurve_x;
						p3TempcurveY = P_tempcurve_y;
						p3Rotate = pRotate;
						p3StartIndex =P_startIndex;
						handle_name="P3_Handle";
						p3InitialPositionX =P_Initial_Position_x;
						p3InitialPositionY =P_Initial_Position_y;
						seekbar_player_Id=3;
						////Log,i("debug", "P_startIndex="+Integer.toString(P_startIndex));
						////Log,i("debug", "P3_startIndex="+Integer.toString(P3_startIndex));
					}
					else if (v.getTag().toString().equals("4")){
						P4=player;
						c4Idx =c_idx;
						p4TempcurveX = P_tempcurve_x;
						p4TempcurveY = P_tempcurve_y;
						p4Rotate = pRotate;
						p4StartIndex =P_startIndex;
						handle_name="P4_Handle";
						p4InitialPositionX =P_Initial_Position_x;
						p4InitialPositionY =P_Initial_Position_y;
						seekbar_player_Id=4;
						////Log,i("debug", "P_startIndex="+Integer.toString(P_startIndex));
						////Log,i("debug", "P4_startIndex="+Integer.toString(P4_startIndex));
					}
					else if (v.getTag().toString().equals("5")){
						P5=player;
						c5Idx =c_idx;
						p5TempcurveX = P_tempcurve_x;
						p5TempcurveY = P_tempcurve_y;
						p5Rotate = pRotate;
						p5StartIndex =P_startIndex;
						handle_name="P5_Handle";
						p5InitialPositionX =P_Initial_Position_x;
						p5InitialPositionY =P_Initial_Position_y;
						seekbar_player_Id=5;
						////Log,i("debug", "P_startIndex="+Integer.toString(P_startIndex));
						////Log,i("debug", "P5_startIndex="+Integer.toString(P5_startIndex));
					}
					else if (v.getTag().toString().equals("6")){
						B=player;
						ballIdx =c_idx;
						ballTempcurveX = P_tempcurve_x;
						ballTempcurveY = P_tempcurve_y;
						bRotate = pRotate;
						bStartIndex =P_startIndex;
						handle_name="B_Handle";
						bInitialPositionX =P_Initial_Position_x;
						bInitialPositionY =P_Initial_Position_y;
						seekbar_player_Id=6;
						////Log,i("debug", "P_startIndex="+Integer.toString(P_startIndex));
						////Log,i("debug", "B_startIndex="+Integer.toString(B_startIndex));
					}
					else if (v.getTag().toString().equals("D1")){
						D1=player;
						cd1Idx =c_idx;
						d1TempcurveX = P_tempcurve_x;
						d1TempcurveY = P_tempcurve_y;
						d1Rotate = pRotate;
						d1StartIndex =P_startIndex;
						handle_name="D1_Handle";
						d1InitialPositionX =P_Initial_Position_x;
						d1InitialPositionY =P_Initial_Position_y;
						seekbar_player_Id=7;
						////Log,i("debug", "P_startIndex="+Integer.toString(P_startIndex));
						////Log,i("debug", "D1_startIndex="+Integer.toString(D1_startIndex));
						////Log,i("debug", "D1_Roadsize="+Integer.toString(D1.getRoadSize()));
					}
					else if (v.getTag().toString().equals("D2")){
						D2=player;
						cd2Idx =c_idx;
						d2TempcurveX = P_tempcurve_x;
						d2TempcurveY = P_tempcurve_y;
						d2Rotate = pRotate;
						d2StartIndex =P_startIndex;
						handle_name="D2_Handle";
						d2InitialPositionX =P_Initial_Position_x;
						d2InitialPositionY =P_Initial_Position_y;
						seekbar_player_Id=8;
						////Log,i("debug", "P_startIndex="+Integer.toString(P_startIndex));
						////Log,i("debug", "D2_startIndex="+Integer.toString(D2_startIndex));
					}
					else if (v.getTag().toString().equals("D3")){
						D3=player;
						cd3Idx =c_idx;
						d3TempcurveX = P_tempcurve_x;
						d3TempcurveY = P_tempcurve_y;
						d3Rotate = pRotate;
						d3StartIndex =P_startIndex;
						handle_name="D3_Handle";
						d3InitialPositionX =P_Initial_Position_x;
						d3InitialPositionY =P_Initial_Position_y;
						seekbar_player_Id=9;
						////Log,i("debug", "P_startIndex="+Integer.toString(P_startIndex));
						////Log,i("debug", "D3_startIndex="+Integer.toString(D3_startIndex));
					}
					else if (v.getTag().toString().equals("D4")){
						D4=player;
						cd4Idx =c_idx;
						d4TempcurveX = P_tempcurve_x;
						d4TempcurveY = P_tempcurve_y;
						d4Rotate = pRotate;
						d4StartIndex =P_startIndex;
						handle_name="D4_Handle";
						d4InitialPositionX =P_Initial_Position_x;
						d4InitialPositionY =P_Initial_Position_y;
						seekbar_player_Id=10;
						////Log,i("debug", "P_startIndex="+Integer.toString(P_startIndex));
						////Log,i("debug", "D4_startIndex="+Integer.toString(D4_startIndex));
					}
					else if (v.getTag().toString().equals("D5")){
						D5=player;
						cd5Idx =c_idx;
						d5TempcurveX = P_tempcurve_x;
						d5TempcurveY = P_tempcurve_y;
						d5Rotate = pRotate;
						d5StartIndex =P_startIndex;
						handle_name="D5_Handle";
						d5InitialPositionX =P_Initial_Position_x;
						d5InitialPositionY =P_Initial_Position_y;
						seekbar_player_Id=11;
						////Log,i("debug", "P_startIndex="+Integer.toString(P_startIndex));
						////Log,i("debug", "D5_startIndex="+Integer.toString(D5_startIndex));
					}
					else{
						////Log,i("debug", "noooooooooo");
					}

					intersectNamePre = intersectName;//判斷球有沒有傳給別的player。p.s. --> pre = 上一個拿球的人
					
					return true;//放開圖片的case
				}//switch觸控動作

				Pbitmap.recycle();
				System.gc();

				return true;
			}//onTouch Event
		};
		 
		
		
		private OnClickListener rm_button_Listener = new OnClickListener(){//"刪除路徑"
	    	@Override
	    	public void onClick(View v) {      
	    		/*remove bitmap*/
	    		/*tempBitmap = Bitmap.createBitmap(circle.getWidth(),circle.getHeight(),Bitmap.Config.ARGB_8888);//??l??tempBitmap?A???w?j?p??????j?p(?j?p?Pcircle)
	    		tempCanvas = new Canvas();
	           	tempCanvas = new Canvas(tempBitmap);//?e?z????|
	           	//Log,i("seekbar", "which_to_remove="+Integer.toString(which_to_remove));
	           	for(int tmp=0;tmp<Bitmap_vector.size();tmp++){
	           		if(tmp!=which_to_remove){
	           			tempCanvas.drawBitmap(Bitmap_vector.get(tmp), 0, 0, null);
	           		}
	           	}
	           	Bitmap_vector.remove(which_to_remove);
	    		circle.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));//??tempBitmap??icircle??
	       		*/
	    		/*remove timeline*/
	    		/*TimeLine timefrag1 = (TimeLine) getActivity().getFragmentManager().findFragmentById(R.id.time_line);
	    		//Log,i("seekbar", "Remove the RunLine.get"+Integer.toString(which_to_remove)+".getTimeLineId ="+Integer.toString(RunLine.get(which_to_remove).getTimeLineId()));
	    		timefrag1.remove_one_timeline(RunLine.get(which_to_remove).getTimeLineId());
	    		*/
	    		/*remove runline*/
	    		/*Every component's position in the RunLine should be the same with the TimeLine SeekBarId.*/
	    		/*for(int index=which_to_remove+1;index<RunLine.size();index++){
	    			//Log,i("seekbar", "Change seekbar's ID, search ID(index)="+Integer.toString(index));
	    			timefrag1.changeSeekBarId(index,index-1);
	    			RunLine.get(index).setTimeLineId(index-1);
	    		}
	    		RunLine.remove(which_to_remove);
	    		timefrag1.setRunLineId(RunLine.size()-1);
	    		//Log,i("seekbar", "RunLine.size="+Integer.toString(RunLine.size()));
	    		*/
	    		/*remove Curves*/
	    		/*Curves.remove(which_to_remove*2);
	    		Curves.remove(which_to_remove*2);
	    		*/
	    		/*hide rm_button*/
	    		/*rm_button.setVisibility(rm_button.INVISIBLE);
				rm_button.layout(1, 1, 1+rm_button_width, 1+rm_button_height);
				rm_button.invalidate();
	    		*/
	    		////Log,i("debug", "rm_button_onclick!!");
	    	}
	    };

	    
	    public void mainfragRemoveOnePath(int seekbarId){
	    	whichToRemove = seekbarId;
	    	/*remove bitmap*/
    		tempBitmap = Bitmap.createBitmap(circle.getWidth(),circle.getHeight(),Bitmap.Config.ARGB_8888);//初始化tempBitmap，指定大小為螢幕大小(大小同circle)
    		tempCanvas = new Canvas();
           	tempCanvas = new Canvas(tempBitmap);//畫透明路徑
           	////Log,i("seekbar", "which_to_remove="+Integer.toString(which_to_remove));
           	for(int tmp = 0; tmp< bitmapVector.size(); tmp++){
           		if(tmp!= whichToRemove){
           			tempCanvas.drawBitmap(bitmapVector.get(tmp), 0, 0, null);
           		}
           	}
           	bitmapVector.remove(whichToRemove);
    		circle.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));//把tempBitmap放進circle裡
       		
    		/*remove timeline*/
    		TimeLine timefrag1 = (TimeLine) getActivity().getFragmentManager().findFragmentById(R.id.time_line);
    		////Log,i("seekbar", "Remove the RunLine.get"+Integer.toString(which_to_remove)+".getTimeLineId ="+Integer.toString(RunLine.get(which_to_remove).getTimeLineId()));
    		timefrag1.remove_one_timeline(runBags.get(whichToRemove).getTimeLineId());

    		/*remove pathnum on the court*/
    		MainWrap mainwrap = (MainWrap) getActivity().getFragmentManager().findFragmentById(R.id.MainWrap_frag);
    		mainwrap.remove_textView(whichToRemove);
    		mainwrap.remove_screen_bar(whichToRemove);
    		mainwrap.remove_dribble_line(whichToRemove);

    		/*remove runline*/
    		/*Every component's position in the RunLine should be the same with the TimeLine SeekBarId.*/
    		for(int index = whichToRemove +1; index< runBags.size(); index++){
    			////Log,i("seekbar", "Change seekbar's ID, search ID(index)="+Integer.toString(index));
    			timefrag1.changeSeekBarId(index,index-1);
    			mainwrap.change_textView_id(index, index-1);//change the remained pathnum text and its id
				mainwrap.change_screen_bar_tag(index, index-1);
				mainwrap.change_dribble_line_tag(index, index-1);
    			runBags.get(index).setTimeLineId(index-1);
    		}
    		runBags.remove(whichToRemove);
    		timefrag1.setRunLineId(runBags.size()-1);
    		////Log,i("seekbar", "RunLine.size="+Integer.toString(RunLine.size()));
    		
    		
    		/*remove Curves*/
    		curves.remove(whichToRemove *2);
    		curves.remove(whichToRemove *2);
    		

    		mainfragSortPathnum();
	    }
	    
	    
	    public void mainfragSortPathnum(){
	    	ArrayList<RunBag> list = new ArrayList<RunBag>();
	    	for(int i = 0; i< runBags.size(); i++){
	    		list.add(runBags.get(i));
	    	}
	    	Collections.sort(list, new Comparator<RunBag>(){
	    		@Override
	    		public int compare(RunBag r1,RunBag r2){
	    			if (r1.getStartTime() > r2.getStartTime())
	    				return 1;
	    			if (r1.getStartTime() < r2.getStartTime())
	    				return -1;
	    			return 0;
	    		}
	    	});
	    	
	    	////Log,i("seekbar", "sort!");
	    	
	    	TimeLine timefrag = (TimeLine) getActivity().getFragmentManager().findFragmentById(R.id.time_line);
	    	MainWrap mainwrap = (MainWrap) getActivity().getFragmentManager().findFragmentById(R.id.MainWrap_frag);
	    	
	    	int show_text=1;
	    	
	    	for(int i =0;i<list.size();i++){
		    	////Log,i("seekbar", "i = "+Integer.toString(i)+"  TimeLine id ="+list.get(i).getTimeLineId()+"  start_time = "+Integer.toString(list.get(i).getStartTime()));
		    	
		    	timefrag.set_pathnum_text(list.get(i).getTimeLineId(), show_text);
	    		mainwrap.set_pathnum_text(list.get(i).getTimeLineId(), show_text);
		    	
		    	if(i==list.size()-1){
                    //設定
		    		break;
		    	}
		    	else if(list.get(i).getStartTime()!=list.get(i+1).getStartTime()){
                    //設定完，show_text++
		    		show_text++;
		    	}
		    	else{
		    		
		    	}
	    	}
	    }
}
