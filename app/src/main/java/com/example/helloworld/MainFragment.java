package com.example.helloworld;

import java.io.BufferedInputStream;
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
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import com.example.helloworld.TimeLine.CallbackInterface;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
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
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

/*處理球員&球的觸控、儲存路徑，畫出路徑*/

public class MainFragment extends Fragment{

	private int before_query_runbag_length;
	private int num_frame;
	private boolean firsttimeQuery = true;
	private int playing=0;
	private Long startTime;
	private Long endTime;

	private int select_category_id = 13;
	private String select_tactic_name = "New_Tactic";
	
	private int Total_time = 15000;// 時間軸的最大值，mySeekBar也要改

	private ImageView player1, player2, player3, player4, player5, ball;
	private ImageView Arrow1,Arrow2,Arrow3,Arrow4,Arrow5;
	private ImageView Arrow7,Arrow8,Arrow9,Arrow10,Arrow11;
	private ImageView rm_button;
	private ImageView player1_ball,player2_ball,player3_ball,player4_ball,player5_ball;
	
	private ImageView defender1,defender2,defender3,defender4,defender5;

	private boolean first_record=true;
	private boolean intersect=false;
	private int intersect_name=0;
	private int intersect_name_pre=0;
	private int initial_ball_num=0;
	private int rotate_which_player;
	//private ImageSelect imageSelect = null;
	private PerspectiveSelect perspectiveSelect = null;
	
	Vector<Integer> P_Initial_Position = new Vector();
	Vector<Integer> P_Initial_Rotate = new Vector();

	private int P_rotate;
	
	private int Initial_P1_rotate=-1;
	private int Initial_P2_rotate=-1;
	private int Initial_P3_rotate=-1;
	private int Initial_P4_rotate=-1;
	private int Initial_P5_rotate=-1;
	
	private int P1_rotate;
	private int P2_rotate;
	private int P3_rotate;
	private int P4_rotate;
	private int P5_rotate;
	private int B_rotate;
	
	private int Initial_D1_rotate=-1;
	private int Initial_D2_rotate=-1;
	private int Initial_D3_rotate=-1;
	private int Initial_D4_rotate=-1;
	private int Initial_D5_rotate=-1;
	
	private int D1_rotate;
	private int D2_rotate;
	private int D3_rotate;
	private int D4_rotate;
	private int D5_rotate;
	
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
	
	private Rect rc_p1=new Rect(); 
	private Rect rc_p2=new Rect(); 
	private Rect rc_p3=new Rect(); 
	private Rect rc_p4=new Rect(); 
	private Rect rc_p5=new Rect(); 
	private Rect rc_ball=new Rect();

	private Player P1_recommend = new Player();
	private Player P2_recommend = new Player();
	private Player P3_recommend = new Player();
	private Player P4_recommend = new Player();
	private Player P5_recommend = new Player();
	private Player B_recommend = new Player();
	
	private int P1_Initial_Position_x = -1;
	private int P2_Initial_Position_x = -1;
	private int P3_Initial_Position_x = -1;
	private int P4_Initial_Position_x = -1;
	private int P5_Initial_Position_x = -1;
	private int B_Initial_Position_x = -1;
	private int P1_Initial_Position_y = -1;
	private int P2_Initial_Position_y = -1;
	private int P3_Initial_Position_y = -1;
	private int P4_Initial_Position_y = -1;
	private int P5_Initial_Position_y = -1;
	private int B_Initial_Position_y = -1;
	
	private int D1_Initial_Position_x = -1;
	private int D2_Initial_Position_x = -1;
	private int D3_Initial_Position_x = -1;
	private int D4_Initial_Position_x = -1;
	private int D5_Initial_Position_x = -1;
	private int D1_Initial_Position_y = -1;
	private int D2_Initial_Position_y = -1;
	private int D3_Initial_Position_y = -1;
	private int D4_Initial_Position_y = -1;
	private int D5_Initial_Position_y = -1;
	
	private int P1_startIndex = 0;
	private int P2_startIndex = 0;
	private int P3_startIndex = 0;
	private int P4_startIndex = 0;
	private int P5_startIndex = 0;
	private int B_startIndex = 0;
	
	private int D1_startIndex = 0;
	private int D2_startIndex = 0;
	private int D3_startIndex = 0;
	private int D4_startIndex = 0;
	private int D5_startIndex = 0;
	
	private int seekbar_tmp_id=0;

	private int which_to_remove=-1;


    /**畫圖變數**/
	private ImageView circle;
	private Path p;
	Vector <Bitmap> Bitmap_vector;
	Bitmap tempBitmap;
	Canvas tempCanvas;
	private Paint player1_paint;
	private Paint player2_paint;
	private Paint player3_paint;
	private Paint player4_paint;
	private Paint player5_paint;
	private Paint ball_paint;
	private Paint d1_paint;
	private Paint d2_paint;
	private Paint d3_paint;
	private Paint d4_paint;
	private Paint d5_paint;
	private Paint transparent_paint;
	private Paint paint;

	/*********/
    /*************************曲線變數************************************/
    private int N = 3;
	private Vector<Integer> P1_tempcurve_x = new Vector();
	private Vector<Integer> P1_tempcurve_y = new Vector();
	private int c1_idx=0;
	private Vector<Integer> P2_tempcurve_x = new Vector();
	private Vector<Integer> P2_tempcurve_y = new Vector();
	private int c2_idx=0;
	private Vector<Integer> P3_tempcurve_x = new Vector();
	private Vector<Integer> P3_tempcurve_y = new Vector();
	private int c3_idx=0;
	private Vector<Integer> P4_tempcurve_x = new Vector();
	private Vector<Integer> P4_tempcurve_y = new Vector();
	private int c4_idx=0;
	private Vector<Integer> P5_tempcurve_x = new Vector();
	private Vector<Integer> P5_tempcurve_y = new Vector();
	private int c5_idx=0;
	private Vector<Integer> Ball_tempcurve_x = new Vector();
	private Vector<Integer> Ball_tempcurve_y = new Vector();
	private int Ball_idx=0;
	private Vector<Integer> D1_tempcurve_x = new Vector();
	private Vector<Integer> D1_tempcurve_y = new Vector();
	private int cd1_idx=0;
	private Vector<Integer> D2_tempcurve_x = new Vector();
	private Vector<Integer> D2_tempcurve_y = new Vector();
	private int cd2_idx=0;
	private Vector<Integer> D3_tempcurve_x = new Vector();
	private Vector<Integer> D3_tempcurve_y = new Vector();
	private int cd3_idx=0;
	private Vector<Integer> D4_tempcurve_x = new Vector();
	private Vector<Integer> D4_tempcurve_y = new Vector();
	private int cd4_idx=0;
	private Vector<Integer> D5_tempcurve_x = new Vector();
	private Vector<Integer> D5_tempcurve_y = new Vector();
	private int cd5_idx=0;
	
	
	Region Bitmap_Region;
	
	private Vector<Vector<Float>> Curves = new Vector();
	private int rm_button_width=60;
	private int rm_button_height=60;
	private int rm_button_flag=0;
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
	private boolean recordcheck = false;
	private int seekBarCallbackStartTime;
	private int seekBarCallbackDuration;
	private int seekBarCallbackId;
	private Vector<RunBag> RunLine = new Vector();
	private int MainFrag_SeekBarProgressLow = 0;
	//endregion

    //TODO Socket 變數
	private UE4_Packet dataPacket = new UE4_Packet();
	private InetAddress serverAddr;
	private int UDP_SERVER_PORT = 2222;
	DatagramSocket ds = null;
	AtomicBoolean isRunning=new AtomicBoolean(false);
	/*************************************************************/
	private double EDR_Threshold_Persent = 15;
	private ToggleButton recommend_button;//Toggle的錄製按鈕
	private int recommend_switch=0; // 0=DTW , 1=EDR
	private TextView reco_result_textview;
	private TextView EDR_path_size;
	private LinearLayout EDR_path_size_LinearLayout;

	// For screen
	private Vector<Float> previous_direction;
	public boolean isScreenEnable;
	// For dribble
	private Vector<Float> previous_dribble_direction;

	private CallbackInterface mCallback;

	public interface CallbackInterface {
		public void RunLineInfo(Vector<RunBag> in_RunLine);

		public void P1Info(Player in_P1);

		public void P2Info(Player in_P2);

		public void P3Info(Player in_P3);

		public void P4Info(Player in_P4);

		public void P5Info(Player in_P5);

		public void BInfo(Player in_B);
		
		public void pass_seekbar(String player);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {   
		////Log,i("debug", "onCreateView()............");
		return inflater.inflate(R.layout.main_layout, container,false);	
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {    
		super.onActivityCreated(savedInstanceState);

		select_category_id = 0;
		previous_direction = new Vector<Float>();
		previous_dribble_direction = new Vector<Float>();
		isScreenEnable = false;

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
		
		player1_ball = (ImageView) getView().findViewById(R.id.player1_ball);
		player1_ball.setOnTouchListener(player_Listener);
		player2_ball = (ImageView) getView().findViewById(R.id.player2_ball);
		player2_ball.setOnTouchListener(player_Listener);
		player3_ball = (ImageView) getView().findViewById(R.id.player3_ball);
		player3_ball.setOnTouchListener(player_Listener);
		player4_ball = (ImageView) getView().findViewById(R.id.player4_ball);
		player4_ball.setOnTouchListener(player_Listener);
		player5_ball = (ImageView) getView().findViewById(R.id.player5_ball);
		player5_ball.setOnTouchListener(player_Listener);
		
		Arrow1 = (ImageView) getView().findViewById(R.id.arrow1);
		Arrow2 = (ImageView) getView().findViewById(R.id.arrow2);
		Arrow3 = (ImageView) getView().findViewById(R.id.arrow3);
		Arrow4 = (ImageView) getView().findViewById(R.id.arrow4);
		Arrow5 = (ImageView) getView().findViewById(R.id.arrow5);
		Arrow7 = (ImageView) getView().findViewById(R.id.arrow7);
		Arrow8 = (ImageView) getView().findViewById(R.id.arrow8);
		Arrow9 = (ImageView) getView().findViewById(R.id.arrow9);
		Arrow10 = (ImageView) getView().findViewById(R.id.arrow10);
		Arrow11 = (ImageView) getView().findViewById(R.id.arrow11);
		rm_button = (ImageView) getView().findViewById(R.id.rm_button);

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
		
		player1_paint=new Paint();
		player1_paint.setAntiAlias(true); // 設置畫筆的鋸齒效果。 true是去除。
		player1_paint.setColor(Color.parseColor("#133C55")); // 設置顏色
		player1_paint.setAlpha(100);
		
		player2_paint=new Paint();
		player2_paint.setAntiAlias(true);
		player2_paint.setColor(Color.TRANSPARENT);
		player2_paint.setColor(Color.parseColor("#154FB5"));
		player2_paint.setAlpha(100);
		
		player3_paint=new Paint();
		player3_paint.setAntiAlias(true);
		player3_paint.setColor(Color.parseColor("#59A5DB"));
		player3_paint.setAlpha(100);

		player4_paint=new Paint();
		player4_paint.setAntiAlias(true);
		player4_paint.setColor(Color.parseColor("#84D2F6"));
		player4_paint.setAlpha(100);
		
		player5_paint=new Paint();
		player5_paint.setAntiAlias(true);
		player5_paint.setColor(Color.parseColor("#DCFFFD"));
		player5_paint.setAlpha(100);
		
		ball_paint=new Paint();
		ball_paint.setAntiAlias(true);
		ball_paint.setColor(Color.TRANSPARENT);
		ball_paint.setColor(Color.parseColor("#CC0000"));
		ball_paint.setAlpha(100);
		
		d1_paint=new Paint();
		d1_paint.setAntiAlias(true); // 設置畫筆的鋸齒效果。 true是去除。
		d1_paint.setColor(Color.parseColor("#3c1518")); // 設置顏色
		d1_paint.setAlpha(100);
		
		d2_paint=new Paint();
		d2_paint.setAntiAlias(true);
		d2_paint.setColor(Color.TRANSPARENT);
		d2_paint.setColor(Color.parseColor("#69140e"));
		d2_paint.setAlpha(100);
		
		d3_paint=new Paint();
		d3_paint.setAntiAlias(true);
		d3_paint.setColor(Color.parseColor("#a44200"));
		d3_paint.setAlpha(100);

		d4_paint=new Paint();
		d4_paint.setAntiAlias(true);
		d4_paint.setColor(Color.parseColor("#d58936"));
		d4_paint.setAlpha(100);
		
		d5_paint=new Paint();
		d5_paint.setAntiAlias(true);
		d5_paint.setColor(Color.parseColor("#FFC82E"));
		d5_paint.setAlpha(100);
		
		transparent_paint=new Paint();
		transparent_paint.setAntiAlias(true); // 設置畫筆的鋸齒效果。 true是去除。
		transparent_paint.setColor(Color.TRANSPARENT); // 設置透明顏色
		
		Bitmap_vector = new Vector();
		
		circle=(ImageView) getActivity().findViewById(R.id.circle);
		circle.setOnTouchListener(bitmap_ontouch);

        /*獲取元件的長與寬，並初始化tempBitmap，接著先放一次的透明路徑在circle上*/
        ViewTreeObserver vto2 = circle.getViewTreeObserver();
		   vto2.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {  
		       @Override   
		       public void onGlobalLayout() {  
		    	
		       	tempBitmap = Bitmap.createBitmap(circle.getWidth(),circle.getHeight(),Bitmap.Config.ARGB_8888);//初始化tempBitmap，指定大小為螢幕大小(大小同circle)
		       	Bitmap_Region = new Region(circle.getLeft(),circle.getTop(),circle.getRight(),circle.getBottom());
		       	
		       	tempCanvas = new Canvas(tempBitmap);//?e?z????|
		   		tempCanvas.drawBitmap(tempBitmap, 0, 0, null);//畫透明路徑
		   		tempCanvas.drawCircle(1, 1, 5, transparent_paint);//畫透明路徑
		   		circle.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));//把tempBitmap放進circle裡
		        ////Log,i("debug", "tempBitmap's width = "+Integer.toString(tempBitmap.getWidth()));
		   		////Log,i("debug", "tempBitmap's height = "+Integer.toString(tempBitmap.getHeight()));
		        circle.getViewTreeObserver().removeGlobalOnLayoutListener(this);  
		           
		        rotate_which_player=0;
		        P1_rotate=0;
		        P2_rotate=0;
		        P3_rotate=0;
		        P4_rotate=0;
		        P5_rotate=0;
		        B_rotate=0;
		        D1_rotate=0;
		        D2_rotate=0;
		        D3_rotate=0;
		        D4_rotate=0;
		        D5_rotate=0;
		        
		        //rm_button.setVisibility(rm_button.VISIBLE);
		        //rm_button.invalidate();
		        rm_button.setVisibility(rm_button.INVISIBLE);
		        rm_button.invalidate();
		        
		        player1_ball.setVisibility(player1_ball.INVISIBLE);
		        player1_ball.invalidate();
		        player2_ball.setVisibility(player2_ball.INVISIBLE);
		        player2_ball.invalidate();
		        player3_ball.setVisibility(player3_ball.INVISIBLE);
		        player3_ball.invalidate();
		        player4_ball.setVisibility(player4_ball.INVISIBLE);
		        player4_ball.invalidate();
		        player5_ball.setVisibility(player5_ball.INVISIBLE);
		        player5_ball.invalidate();
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
			serverAddr = InetAddress.getByName("192.168.60.7");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    Log.i("socket", "start");
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
        
    }
	
	public void Mainfrag_Set_Total_time(int inputTime){
		Total_time = inputTime;
	}
	
	public void Mainfrag_Set_UDP_IP(InetAddress IP,int port){   
		serverAddr = IP;
	    UDP_SERVER_PORT = port;
	}

	/********************************Get corresponding defender from the server******************************/
	public void Mainfrag_GetDefenderFromServer() throws JSONException, ExecutionException{
		Log.d("debug", "Call get defender");
		set_playing(0);
		D1.clear_all();
		D2.clear_all();
		D3.clear_all();
		D4.clear_all();
		D5.clear_all();

		if(!firsttimeQuery) {
			int line_size = RunLine.size();
			for(int i=1;i<=5;i++)
				RunLine.remove(line_size-i);
		}
		firsttimeQuery = false;


		JSONObject json_write = new JSONObject();
		List<List<Integer>> position = new ArrayList<List<Integer>>();
        /* For five offensive player and the ball position*/
        for(int i = 0; i < 6; i++)  {
            position.add(new ArrayList<Integer>());
        }

        /*Initialize all the position*/
        position.get(0).add(new Integer(P1_Initial_Position_x));
        position.get(0).add(new Integer(P1_Initial_Position_y));
        position.get(1).add(new Integer(P2_Initial_Position_x));
        position.get(1).add(new Integer(P2_Initial_Position_y));
        position.get(2).add(new Integer(P3_Initial_Position_x));
        position.get(2).add(new Integer(P3_Initial_Position_y));
        position.get(3).add(new Integer(P4_Initial_Position_x));
        position.get(3).add(new Integer(P4_Initial_Position_y));
        position.get(4).add(new Integer(P5_Initial_Position_x));
        position.get(4).add(new Integer(P5_Initial_Position_y));
        position.get(5).add(new Integer(B_Initial_Position_x));
        position.get(5).add(new Integer(B_Initial_Position_y));

        /* See the RunLine and add sequential position */
        int prev_timing = 0;
		int [] edited = new int[6];
		for(int i = 0;i<RunLine.size();i++){

			String tmpRunLine = RunLine.get(i).getRunBagInfo();
            Log.d("debug", RunLine.get(i).getRunBagInfo());
            String[] separated = tmpRunLine.split("\n");
            int timing = Integer.parseInt(separated[0]);
            /*Need to append last position of the player
            * Because they didn't move in this timing.*/
			if(timing != prev_timing){
				List tmp_edited = new ArrayList();
				for(int j=0;j<6;j++)
					tmp_edited.add(edited[j]);
				int max = (int) Collections.max(tmp_edited);
				Log.d("warning", String.valueOf(max));
				for(int j=0;j<6;j++) {
					if(edited[j] < max) {
						Log.d("warning", "Need to add.");
						int last_index = position.get(j).size() - 2;
						for (int k = 0;k < max - edited[j];k++){
							position.get(j).add(position.get(j).get(last_index));
							position.get(j).add(position.get(j).get(last_index+1));
						}
					}
				}
				for(int j=0;j<6;j++){
					edited[j] = 0;
				}
			}

            String whoHandle = separated[1];
            int start_index = Integer.parseInt(separated[2]);
            int end_index = Integer.parseInt(separated[3]);

            if(whoHandle.equals("P1_Handle")){
                edited[0] = (end_index - start_index+1)/2;
                for(int j=start_index;j<end_index+1;j++) {
                    position.get(0).add(P1.handleGetRoad(j));
                }
            }else if (whoHandle.equals("P2_Handle")){
                edited[1] = (end_index - start_index+1)/2;
                for(int j=start_index;j<end_index+1;j++) {
                    position.get(1).add(P2.handleGetRoad(j));
                }
            }else if (whoHandle.equals("P3_Handle")){
                edited[2] = (end_index - start_index+1)/2;
                for(int j=start_index;j<end_index+1;j++) {
                    position.get(2).add(P3.handleGetRoad(j));
                }
            }else if (whoHandle.equals("P4_Handle")){
                edited[3] = (end_index - start_index+1)/2;
                for(int j=start_index;j<end_index+1;j++) {
                    position.get(3).add(P4.handleGetRoad(j));
                }
            }else if (whoHandle.equals("P5_Handle")){
                edited[4] = (end_index - start_index+1)/2;
                for(int j=start_index;j<end_index+1;j++) {
                    position.get(4).add(P5.handleGetRoad(j));
                }
            }else if (whoHandle.equals("B_Handle")){
                edited[5] = (end_index - start_index+1)/2;
                for(int j=start_index;j<end_index+1;j++) {
                    position.get(5).add(B.handleGetRoad(j));
                }
            }
            Log.d("warning", "edited:"+edited[0]+","+edited[1]+","+edited[2]+","+edited[3]+","+edited[4]+","+edited[5]);
            Log.d("warning", "path:"+position.get(0).size()+","+position.get(1).size()+","+position.get(2).size()+","+position.get(3).size()+","+position.get(4).size()+","+position.get(5).size());
			Log.d("warning", "Rotation size:" + String.valueOf(P1.getRoadSize()));

            prev_timing = timing;
        }
		List tmp_edited = new ArrayList();
		for(int j=0;j<6;j++)
			tmp_edited.add(edited[j]);
		int max = (int) Collections.max(tmp_edited);
		Log.d("warning", String.valueOf(max));
		for(int j=0;j<6;j++) {
			if(edited[j] < max) {
				Log.d("warning", "Need to add.");
				int last_index = position.get(j).size() - 2;
				for (int k = 0;k < max - edited[j];k++){
					position.get(j).add(position.get(j).get(last_index));
					position.get(j).add(position.get(j).get(last_index+1));
				}
			}
		}


        Log.d("warning", "path:"+position.get(0).size()+","+position.get(1).size()+","+position.get(2).size()+","+position.get(3).size()+","+position.get(4).size()+","+position.get(5).size());

        num_frame = position.get(0).size();
		//num_frame represents double the size of frames because there are both x and y coordination
        // Transfer the position coordination
        for(int i = 0;i < num_frame;i=i+2){
            for(int j = 0;j < 6;j++){
                position.get(j).set(i, new Integer( (int)Math.round(  ((float)position.get(j).get(i).intValue())/1500.0*94.0 - 10.0) ) );
                position.get(j).set(i+1, new Integer( (int)Math.round(94.0 - ((float)position.get(j).get(i+1).intValue())/940.0*50.0 ) ));
            }
        }

        for ( int i = 0; i< num_frame ;i=i+2) {
            JSONArray pos = new JSONArray();
            pos.put( String.valueOf(position.get(5).get(i+1) ));//Ball x position in frame i
            pos.put( String.valueOf(position.get(5).get(i) ) );//Ball y position in frame i
            pos.put("0");
            for(int j=0;j<5;j++){
                pos.put( String.valueOf( position.get(j).get(i+1) ) );//Player j x position in frame i
                pos.put( String.valueOf( position.get(j).get(i) ) );//Player j y position in frame i
            }
            json_write.put("f" + Integer.valueOf(i/2), pos);
        }

        JSONObject json_test = new JSONObject();
        JSONArray arr = new JSONArray();
        for(int i=0;i<13;i++){
            arr.put(String.valueOf(i));
        }
        for(int i=0;i<500;i++) {
            json_test.put("f"+i, arr);
        }

        final String result = json_write.toString();
        Log.d("warning", result);
		new TestAsyncTask(getActivity()).execute(result);
	}

	public void onQueryFinished(String query){
		try {
			JSONObject json_defender = new JSONObject(new String(query));

			JSONArray initial_pos = json_defender.getJSONArray("f0");
			//Coordination transformation to the tablet
			int[] tmp_pos = new int[10];
			for(int i=0;i<10;i=i+2){
				tmp_pos[i] = (int) Math.round( (Float.parseFloat( initial_pos.get(i+1).toString() ) + 10.0)*1500/94 );
				tmp_pos[i+1] = Math.round( (94 - Float.parseFloat( initial_pos.get(i).toString()))*940/50 );
			}

			// Setting the initial position of every defender from the json key "f0"
			D1_Initial_Position_x =  tmp_pos[0];
			D1_Initial_Position_y =  tmp_pos[1];

			D2_Initial_Position_x =  tmp_pos[2];
			D2_Initial_Position_y =  tmp_pos[3];

			D3_Initial_Position_x =  tmp_pos[4];
			D3_Initial_Position_y =  tmp_pos[5];

			D4_Initial_Position_x =  tmp_pos[6];
			D4_Initial_Position_y =  tmp_pos[7];

			D5_Initial_Position_x =  tmp_pos[8];
			D5_Initial_Position_y =  tmp_pos[9];


			for(int i=0;i<num_frame;i=i+2){
				JSONArray curr_pos = json_defender.getJSONArray("f"+Math.round(i/2));
				for(int j=0;j<10;j=j+2){
					tmp_pos[j] = (int) Math.round( (Float.parseFloat( curr_pos.get(j+1).toString() ) + 10.0)*1500/94 );
					tmp_pos[j+1] = Math.round( (94 - Float.parseFloat( curr_pos.get(j).toString()))*940/50 );
				}
				D1.setRoad(tmp_pos[0]);
				D1.setRoad(tmp_pos[1]);
				D2.setRoad(tmp_pos[2]);
				D2.setRoad(tmp_pos[3]);
				D3.setRoad(tmp_pos[4]);
				D3.setRoad(tmp_pos[5]);
				D4.setRoad(tmp_pos[6]);
				D4.setRoad(tmp_pos[7]);
				D5.setRoad(tmp_pos[8]);
				D5.setRoad(tmp_pos[9]);

				D1.setMyRotation(0);
				D1.setMyRotation(0);
				D2.setMyRotation(0);
				D2.setMyRotation(0);
				D3.setMyRotation(0);
				D3.setMyRotation(0);
				D4.setMyRotation(0);
				D4.setMyRotation(0);
				D5.setMyRotation(0);
				D5.setMyRotation(0);
			}
			Log.d("warning", String.valueOf(RunLine.size()));
			// Get max time in the current Runbag
			int size_runbag = RunLine.size();
			before_query_runbag_length = size_runbag;
			List <Integer> tmp_last_timing = new ArrayList<Integer>();
			for(int i=0;i<size_runbag;i++) {
				tmp_last_timing.add(RunLine.get(i).getStartTime() + RunLine.get(i).getDuration());
			}
			int max_timing = Collections.max(tmp_last_timing);
			int [] max_dilta_index = new int[max_timing+1];
			for (int t=0;t<max_timing;t++){
				int max = 0;
				for(int j=0;j<RunLine.size();j++){
					if(t ==  RunLine.get(j).getStartTime()){
						int curr_dilta = (RunLine.get(j).getRoadEnd() - RunLine.get(j).getRoadStart()) + 1;
						if(curr_dilta > max ){
							max = curr_dilta;
						}
					}
				}
				max_dilta_index[t+1] = max;
			}

			max_dilta_index [0] += 0;
			for(int i=1;i<max_dilta_index.length;i++){
				max_dilta_index [i] = max_dilta_index [i] + max_dilta_index [i-1];
			}

			for(int t=0 ; t<max_timing ; t++){
				for(int def=0;def<5;def++){
					RunBag tmp_bag = new RunBag();
					tmp_bag.setStartTime(t);
					tmp_bag.setRoadStart( max_dilta_index[t]);
					tmp_bag.setRoadEnd( max_dilta_index[t+1]);
					tmp_bag.setDuration(1);
					tmp_bag.setHandler("D" + (def+1) + "_Handle");
					RunLine.add(tmp_bag);
				}
			}

			/*
			RunBag[] runBag = new RunBag[5];
			for(int i=0;i<5;i++){
				RunBag tmp_bag = new RunBag();
				tmp_bag.setStartTime(0);
				tmp_bag.setRoadStart(0);
				tmp_bag.setRoadEnd(D1.getRoadSize());
				tmp_bag.setDuration(max_timing+3);
				switch (i){
					case 0:
						tmp_bag.setHandler("D1_Handle");
						break;
					case 1:
						tmp_bag.setHandler("D2_Handle");
						break;
					case 2:
						tmp_bag.setHandler("D3_Handle");
						break;
					case 3:
						tmp_bag.setHandler("D4_Handle");
						break;
					case 4:
						tmp_bag.setHandler("D5_Handle");
						break;
				}
				runBag[i] = tmp_bag;
			}
			for(int i=0;i<5;i++)
				RunLine.add(runBag[i]);
			*/

			Log.d("warning", String.valueOf(RunLine.size()));

		} catch (JSONException e) {
			e.printStackTrace();
		}
		Player_move_to_start_position();
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
				clientSocket = new Socket("192.168.60.82", 5000);
				dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
				int data_len = input[0].length();

				byte[] arr = new byte[] {
						(byte) ((data_len >> 24) & 0xFF),
						(byte) ((data_len >> 16) & 0xFF),
						(byte) ((data_len >> 8) & 0xFF),
						(byte) (data_len & 0xFF)};
				dataOutputStream.write( arr);
				dataOutputStream.write(input[0].getBytes());

				dataInputStream = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
				byte[] sizeArray = new byte[4];
				int response_size;
				dataInputStream.readFully(sizeArray, 0, 4);
				response_size = ByteBuffer.wrap(sizeArray).getInt();

				byte[] responseArray = new byte[response_size];
				dataInputStream.readFully(responseArray, 0, response_size);


				return new String(responseArray);
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

	/**********************************************UE4***********************************************/
	public void Mainfrag_SendtoUE4(){

		JSONObject tactic_packet = new JSONObject();
		try {
			JSONArray tmp_arr = new JSONArray();
			tmp_arr.put(P1_Initial_Position_x);
			tmp_arr.put(P1_Initial_Position_y);
			tmp_arr.put(P2_Initial_Position_x);
			tmp_arr.put(P2_Initial_Position_y);
			tmp_arr.put(P3_Initial_Position_x);
			tmp_arr.put(P3_Initial_Position_y);
			tmp_arr.put(P4_Initial_Position_x);
			tmp_arr.put(P4_Initial_Position_y);
			tmp_arr.put(P5_Initial_Position_x);
			tmp_arr.put(P5_Initial_Position_y);

			tmp_arr.put(D1_Initial_Position_x);
			tmp_arr.put(D1_Initial_Position_y);
			tmp_arr.put(D2_Initial_Position_x);
			tmp_arr.put(D2_Initial_Position_y);
			tmp_arr.put(D3_Initial_Position_x);
			tmp_arr.put(D3_Initial_Position_y);
			tmp_arr.put(D4_Initial_Position_x);
			tmp_arr.put(D4_Initial_Position_y);
			tmp_arr.put(D5_Initial_Position_x);
			tmp_arr.put(D5_Initial_Position_y);

			tmp_arr.put(B_Initial_Position_x);
			tmp_arr.put(B_Initial_Position_y);

			tactic_packet.put("Initial_Position", tmp_arr);

			tactic_packet.put("Initial_ball_holder", initial_ball_num);

			//region 2018.09.10 For tactic management
			tactic_packet.put("Category_id", select_category_id);
			tactic_packet.put("Tactic_name", select_tactic_name);
			//endregion

			tmp_arr = new JSONArray();
			for(int i=0;i<B.getRoadSize();i++){
				tmp_arr.put( String.valueOf(B.handleGetRoad(i) ));
			}
			if(tmp_arr.length() > 0)
				tactic_packet.put("B", tmp_arr);

			tmp_arr = new JSONArray();
			for(int i=0;i<P1.getRoadSize();i++){
				tmp_arr.put( String.valueOf(P1.handleGetRoad(i)));
			}
			if(tmp_arr.length() > 0)
				tactic_packet.put("P1", tmp_arr);

			tmp_arr = new JSONArray();
			for(int i=0;i<P2.getRoadSize();i++){
				tmp_arr.put(  String.valueOf(P2.handleGetRoad(i)));
			}
			if(tmp_arr.length() > 0)
				tactic_packet.put("P2", tmp_arr);

			tmp_arr = new JSONArray();
			for(int i=0;i<P3.getRoadSize();i++){
				tmp_arr.put(  String.valueOf(P3.handleGetRoad(i)));
			}
			if(tmp_arr.length() > 0)
				tactic_packet.put("P3", tmp_arr);

			tmp_arr = new JSONArray();
			for(int i=0;i<P4.getRoadSize();i++){
				tmp_arr.put( String.valueOf(P4.handleGetRoad(i)));
			}
			if(tmp_arr.length() > 0)
				tactic_packet.put("P4", tmp_arr);

			tmp_arr = new JSONArray();
			for(int i=0;i<P5.getRoadSize();i++){
				tmp_arr.put(String.valueOf(P5.handleGetRoad(i)));
			}
			if(tmp_arr.length() > 0)
				tactic_packet.put("P5", tmp_arr);

			tmp_arr = new JSONArray();
			for(int i=0;i<D1.getRoadSize();i++){
				tmp_arr.put(String.valueOf(D1.handleGetRoad(i)));
			}
			if(tmp_arr.length() > 0)
				tactic_packet.put("D1", tmp_arr);

			tmp_arr = new JSONArray();
			for(int i=0;i<D2.getRoadSize();i++){
				tmp_arr.put(String.valueOf(D2.handleGetRoad(i)));
			}
			if(tmp_arr.length() > 0)
				tactic_packet.put("D2", tmp_arr);

			tmp_arr = new JSONArray();
			for(int i=0;i<D3.getRoadSize();i++){
				tmp_arr.put(String.valueOf(D3.handleGetRoad(i)));
			}
			if(tmp_arr.length() > 0)
				tactic_packet.put("D3", tmp_arr);

			tmp_arr = new JSONArray();
			for(int i=0;i<D4.getRoadSize();i++){
				tmp_arr.put(String.valueOf(D4.handleGetRoad(i)));
			}
			if(tmp_arr.length() > 0)
				tactic_packet.put("D4", tmp_arr);

			tmp_arr = new JSONArray();
			for(int i=0;i<D5.getRoadSize();i++){
				tmp_arr.put(String.valueOf(D5.handleGetRoad(i)));
			}
			if(tmp_arr.length() > 0)
				tactic_packet.put("D5", tmp_arr);

		} catch (JSONException e) {
			e.printStackTrace();
		}

		JSONArray runline_aray = new JSONArray();
		try {
			for(int i=0;i<RunLine.size();i++){
				JSONObject tmp_runbag = new JSONObject();
				tmp_runbag.put("start_time", String.valueOf(RunLine.get(i).getStartTime()));
				tmp_runbag.put("duration", String.valueOf(RunLine.get(i).getDuration()));
				tmp_runbag.put("handler", RunLine.get(i).getHandler());
				tmp_runbag.put("road_start", String.valueOf(RunLine.get(i).getRoadStart()));
				tmp_runbag.put("road_end", String.valueOf(RunLine.get(i).getRoadEnd()));
				tmp_runbag.put("rate", String.valueOf(RunLine.get(i).getRate()));
				tmp_runbag.put("ball_num", String.valueOf(RunLine.get(i).getBall_num()));

				tmp_runbag.put("path_type", String.valueOf(RunLine.get(i).getPath_type()));
				tmp_runbag.put("screen_angle", String.valueOf(RunLine.get(i).getPath_type()));
				tmp_runbag.put("dribble_angle", String.valueOf(RunLine.get(i).getDribble_angle()));
				tmp_runbag.put("dribble_length", String.valueOf(RunLine.get(i).getDribble_length()));

				runline_aray.put(tmp_runbag);
			}
			tactic_packet.put("RunLine", runline_aray);


		} catch (JSONException e) {
			e.printStackTrace();
		}

		JSONObject json_test = new JSONObject();
		try {
			json_test.put("RunLine", "123");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		final String tactic_string = tactic_packet.toString();

		Thread socket_thread = new Thread(new Runnable() {
			@Override
			public void run() {
				Socket clientSocket = null;
				DataOutputStream dataOutputStream = null;

				try {

					Log.d("debug", serverAddr.getHostAddress());
					clientSocket = new Socket(serverAddr.getHostAddress(), 2222);
					dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
					int data_len = tactic_string.length();
					//byte[] arr = new byte[] {
					//		(byte) ((data_len >> 24) & 0xFF),
					//		(byte) ((data_len >> 16) & 0xFF),
					//		(byte) ((data_len >> 8) & 0xFF),
					//		(byte) (data_len & 0xFF)};
					//dataOutputStream.write( arr);
					dataOutputStream.write(tactic_string.getBytes());
					clientSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		socket_thread.start();

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
 
	public void set_player_to_no_ball(){ 
		if(intersect_name!=0){
			switch(intersect_name){
			case 1:
				player1.setVisibility(player1.VISIBLE);
				if(intersect==true){
					////Log,i("debug","intersect=true,player1 should be layout on player_ball's position");
					player1.layout((int)player1_ball.getX()+30, (int)player1_ball.getY(),(int)player1_ball.getX()+30+player1.getWidth(), (int)player1_ball.getY()+player1.getHeight());
				}
				player1.invalidate();
				player1_ball.setVisibility(player1_ball.INVISIBLE);
				player1_ball.invalidate();
				break;
			case 2:
				player2.setVisibility(player2.VISIBLE);
				if(intersect==true){
					////Log,i("debug","intersect=true,player1 should be layout on player_ball's position");
					player2.layout((int)player2_ball.getX()+30, (int)player2_ball.getY(),(int)player2_ball.getX()+30+player2.getWidth(), (int)player2_ball.getY()+player2.getHeight());
				}
				player2.invalidate();
				player2_ball.setVisibility(player2_ball.INVISIBLE);
				player2_ball.invalidate();
				break;
			case 3:
				player3.setVisibility(player3.VISIBLE);
				if(intersect==true){
					////Log,i("debug","intersect=true,player1 should be layout on player_ball's position");
					player3.layout((int)player3_ball.getX()+30, (int)player3_ball.getY(),(int)player3_ball.getX()+30+player3.getWidth(), (int)player3_ball.getY()+player3.getHeight());
				}
				player3.invalidate();
				player3_ball.setVisibility(player3_ball.INVISIBLE);
				player3_ball.invalidate();
				break;
			case 4:
				player4.setVisibility(player4.VISIBLE);
				if(intersect==true){
					////Log,i("debug","intersect=true,player1 should be layout on player_ball's position");
					player4.layout((int)player4_ball.getX()+30, (int)player4_ball.getY(),(int)player4_ball.getX()+30+player4.getWidth(), (int)player4_ball.getY()+player4.getHeight());
				}
				player4.invalidate();
				player4_ball.setVisibility(player4_ball.INVISIBLE);
				player4_ball.invalidate();
				break;
			case 5:
				player5.setVisibility(player5.VISIBLE);
				if(intersect==true){
					////Log,i("debug","intersect=true,player1 should be layout on player_ball's position");
					player5.layout((int)player5_ball.getX()+30, (int)player5_ball.getY(),(int)player5_ball.getX()+30+player5.getWidth(), (int)player5_ball.getY()+player5.getHeight());
				}
				player5.invalidate();
				player5_ball.setVisibility(player5_ball.INVISIBLE);
				player5_ball.invalidate();
				break;
			}
		}
		if(player1.getVisibility()==player1.VISIBLE){
			Arrow1.layout((int)player1.getX(), (int)player1.getY(), (int)player1.getX()+player1.getWidth(), (int)player1.getY()+player1.getHeight());
		}
		Arrow1.invalidate();
		if(player2.getVisibility()==player2.VISIBLE){
			Arrow2.layout((int)player2.getX(), (int)player2.getY(), (int)player2.getX()+player2.getWidth(), (int)player2.getY()+player2.getHeight());
		}
		Arrow2.invalidate();
		if(player3.getVisibility()==player3.VISIBLE){
			Arrow3.layout((int)player3.getX(), (int)player3.getY(), (int)player3.getX()+player3.getWidth(), (int)player3.getY()+player3.getHeight());
		}
		Arrow3.invalidate();
		if(player4.getVisibility()==player4.VISIBLE){
			Arrow4.layout((int)player4.getX(), (int)player4.getY(), (int)player4.getX()+player4.getWidth(), (int)player4.getY()+player4.getHeight());
		}
		Arrow4.invalidate();
		if(player5.getVisibility()==player5.VISIBLE){
			Arrow5.layout((int)player5.getX(), (int)player5.getY(), (int)player5.getX()+player5.getWidth(), (int)player5.getY()+player5.getHeight());
		}
		Arrow5.invalidate();
	}
	
	public void set_playing(int input){
		playing=input;
	}
	
	public void pass_RunLine_Player_Info() {//3D??
		mCallback.P1Info(P1);
		mCallback.P2Info(P2);
		mCallback.P3Info(P3);
		mCallback.P4Info(P4);
		mCallback.P5Info(P5);
		mCallback.BInfo(B);
		mCallback.RunLineInfo(RunLine);

	}

	public void pass_start_time(int input) {
		seekBarCallbackStartTime = input;
	}

	public void pass_duration(int input) {
		seekBarCallbackDuration = input;
	}

	public void pass_id(int input){
		seekBarCallbackId = input;
	}
	public void pass_recordcheck(boolean input) {
        /*停止錄製按下之後，畫線的curve就清除重算*/
		if(recordcheck == true && input == false){
			P1_tempcurve_x.clear();
			P1_tempcurve_y.clear();
			c1_idx=0;
			P2_tempcurve_x.clear();
			P2_tempcurve_y.clear();
			c2_idx=0;
			P3_tempcurve_x.clear();
			P3_tempcurve_y.clear();
			c3_idx=0;
			P4_tempcurve_x.clear();
			P4_tempcurve_y.clear();
			c4_idx=0;
			P5_tempcurve_x.clear();
			P5_tempcurve_y.clear();
			c5_idx=0;
			Ball_tempcurve_x.clear();
			Ball_tempcurve_y.clear();
			Ball_idx=0;
			
			D1_tempcurve_x.clear();
			D1_tempcurve_y.clear();
			cd1_idx=0;
			D2_tempcurve_x.clear();
			D2_tempcurve_y.clear();
			cd2_idx=0;
			D3_tempcurve_x.clear();
			D3_tempcurve_y.clear();
			cd3_idx=0;
			D4_tempcurve_x.clear();
			D4_tempcurve_y.clear();
			cd4_idx=0;
			D5_tempcurve_x.clear();
			D5_tempcurve_y.clear();
			cd5_idx=0;
		}
		/**/
		if(recordcheck == false && input==true && first_record==true && intersect_name!=0){
			first_record=false;
			initial_ball_num=intersect_name;
			//Log,i("socket", "initial_ball_num="+Integer.toString(initial_ball_num));
		}
		
		
		recordcheck = input;
	}
	public void set_seekBar_to_RunBag(Vector <Integer> input){
		/**
		 * Vector<Integer> input: 0=Id,1=StartTime,2=Duration
		 * **/
		RunBag tmp = new RunBag();
		tmp=RunLine.get(input.get(0));
		tmp.setStartTime(input.get(1));
		tmp.setDuration(input.get(2));
		RunLine.set(input.get(0), tmp);
		//Log.d("debug", "Set! "+Integer.toString(input.get(0)));
	}
	
	
	
	public void setMainFragProLow(int Low_in){
		MainFrag_SeekBarProgressLow=Low_in;
		////Log,i("debug", "MainFrag_set MainFrag_SeekBarProgressLow ="+Integer.toString(MainFrag_SeekBarProgressLow));
	}
	
	public int getMainFragProLow(){
		return MainFrag_SeekBarProgressLow;
	}
	
	public void clear_paint(){//清除筆跡
		Bitmap_vector.clear();
		tempBitmap = Bitmap.createBitmap(circle.getWidth(),circle.getHeight(),Bitmap.Config.ARGB_8888);//初始化tempBitmap，指定大小為螢幕大小(大小同circle)
		tempCanvas = new Canvas();
       	tempCanvas = new Canvas(tempBitmap);//畫透明路徑
   		tempCanvas.drawBitmap(tempBitmap, 0, 0, null);
   		tempCanvas.drawCircle(1, 1, 5, transparent_paint);//畫透明路徑
   		circle.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));//把tempBitmap放進circle裡
   		p.reset();
   		c1_idx=0;
		c2_idx=0;
		c3_idx=0;
		c4_idx=0;
		c5_idx=0;
		Ball_idx=0;
		P1_tempcurve_x.clear();
		P1_tempcurve_y.clear();
		P2_tempcurve_x.clear();
		P2_tempcurve_y.clear();
		P3_tempcurve_x.clear();
		P3_tempcurve_y.clear();
		P4_tempcurve_x.clear();
		P4_tempcurve_y.clear();
		P5_tempcurve_x.clear();
		P5_tempcurve_y.clear();
		Ball_tempcurve_x.clear();
		Ball_tempcurve_y.clear();
		cd1_idx=0;
		cd2_idx=0;
		cd3_idx=0;
		cd4_idx=0;
		cd5_idx=0;
		D1_tempcurve_x.clear();
		D1_tempcurve_y.clear();
		D2_tempcurve_x.clear();
		D2_tempcurve_y.clear();
		D3_tempcurve_x.clear();
		D3_tempcurve_y.clear();
		D4_tempcurve_x.clear();
		D4_tempcurve_y.clear();
		D5_tempcurve_x.clear();
		D5_tempcurve_y.clear();
		Curves.clear();
        //tempCanvas.drawBitmap(Bitmap_vector.get(0), 0, 0, null);
        //circle.setImageDrawable(new BitmapDrawable(getResources(), Bitmap_vector.get(1)));//把tempBitmap放進circle裡
	}
	
	public void clear_record(){     
		initial_ball_num=0;
		first_record=true;
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
		RunLine.clear();
		P1_startIndex = 0;
		P2_startIndex = 0;
		P3_startIndex = 0;
		P4_startIndex = 0;
		P5_startIndex = 0;
		B_startIndex = 0;
		D1_startIndex = 0;
		D2_startIndex = 0;
		D3_startIndex = 0;
		D4_startIndex = 0;
		D5_startIndex = 0;
		seekbar_tmp_id=0;
		MainFrag_SeekBarProgressLow=0;
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
		
		Initial_P1_rotate=-1;
		Initial_P2_rotate=-1;
		Initial_P3_rotate=-1;
		Initial_P4_rotate=-1;
		Initial_P5_rotate=-1;
		Initial_D1_rotate=-1;
		Initial_D2_rotate=-1;
		Initial_D3_rotate=-1;
		Initial_D4_rotate=-1;
		Initial_D5_rotate=-1;
		
		clear_paint();
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
	
	public void Player_move_to_start_position(){
        /*先把全部player移到按下錄製鍵時的位置*/
		
		if(P1_Initial_Position_x!=-1){
			player1.layout(P1_Initial_Position_x, P1_Initial_Position_y,P1_Initial_Position_x + player1.getWidth(),P1_Initial_Position_y + player1.getHeight());
			Arrow1.layout(P1_Initial_Position_x, P1_Initial_Position_y,P1_Initial_Position_x + Arrow1.getWidth(),P1_Initial_Position_y + Arrow1.getHeight());
			rc_p1 = new Rect(P1_Initial_Position_x, P1_Initial_Position_y,P1_Initial_Position_x + player1.getWidth(),P1_Initial_Position_y + player1.getHeight());
			player1.invalidate();
			Arrow1.setRotation(Initial_P1_rotate);
			Arrow1.invalidate();
			//Log,i("debug", "P1_initial_pos set!");
		}
		if(P2_Initial_Position_x!=-1){
			player2.layout(P2_Initial_Position_x, P2_Initial_Position_y,P2_Initial_Position_x + player2.getWidth(),P2_Initial_Position_y + player2.getHeight());
			Arrow2.layout(P2_Initial_Position_x, P2_Initial_Position_y,P2_Initial_Position_x + Arrow2.getWidth(),P2_Initial_Position_y + Arrow2.getHeight());
			rc_p2=new Rect(P2_Initial_Position_x, P2_Initial_Position_y,P2_Initial_Position_x + player2.getWidth(),P2_Initial_Position_y + player2.getHeight());
			player2.invalidate();
			Arrow2.setRotation(Initial_P2_rotate);
			Arrow2.invalidate();
			//Log,i("debug", "P2_initial_pos set!");
		}
		if(P3_Initial_Position_x!=-1){
			player3.layout(P3_Initial_Position_x, P3_Initial_Position_y,P3_Initial_Position_x + player3.getWidth(),P3_Initial_Position_y + player3.getHeight());
			Arrow3.layout(P3_Initial_Position_x, P3_Initial_Position_y,P3_Initial_Position_x + Arrow3.getWidth(),P3_Initial_Position_y + Arrow3.getHeight());
			rc_p3 = new Rect(P3_Initial_Position_x, P3_Initial_Position_y,P3_Initial_Position_x + player3.getWidth(),P3_Initial_Position_y + player3.getHeight());
			player3.invalidate();
			Arrow3.setRotation(Initial_P3_rotate);
			Arrow3.invalidate();
			//Log,i("debug", "P3_initial_pos set!");
		}
		if(P4_Initial_Position_x!=-1){
			player4.layout(P4_Initial_Position_x, P4_Initial_Position_y,P4_Initial_Position_x + player4.getWidth(),P4_Initial_Position_y + player4.getHeight());
			Arrow4.layout(P4_Initial_Position_x, P4_Initial_Position_y,P4_Initial_Position_x + Arrow4.getWidth(),P4_Initial_Position_y + Arrow4.getHeight());
			rc_p4 = new Rect(P4_Initial_Position_x, P4_Initial_Position_y,P4_Initial_Position_x + player4.getWidth(),P4_Initial_Position_y + player4.getHeight());
			player4.invalidate();
			Arrow4.setRotation(Initial_P4_rotate);
			Arrow4.invalidate();
			//Log,i("debug", "P4_initial_pos set!");
		}
		if(P5_Initial_Position_x!=-1){
			player5.layout(P5_Initial_Position_x, P5_Initial_Position_y,P5_Initial_Position_x + player5.getWidth(),P5_Initial_Position_y + player5.getHeight());
			Arrow5.layout(P5_Initial_Position_x, P5_Initial_Position_y,P5_Initial_Position_x + Arrow5.getWidth(),P5_Initial_Position_y + Arrow5.getHeight());
			rc_p5 = new Rect(P5_Initial_Position_x, P5_Initial_Position_y,P5_Initial_Position_x + player5.getWidth(),P5_Initial_Position_y + player5.getHeight());
			player5.invalidate();
			Arrow5.setRotation(Initial_P5_rotate);
			Arrow5.invalidate();
			//Log,i("debug", "P5_initial_pos set!");
		}
		if(B_Initial_Position_x!=-1){
			ball.layout(B_Initial_Position_x, B_Initial_Position_y,B_Initial_Position_x + ball.getWidth(),B_Initial_Position_y + ball.getHeight());
			rc_ball = new Rect(B_Initial_Position_x, B_Initial_Position_y,B_Initial_Position_x + ball.getWidth(),B_Initial_Position_y + ball.getHeight());
			//Log,i("debug", "Ball_initial_pos set!");
			//Arrow6.layout(P5_Initial_Position_x, P5_Initial_Position_y,P5_Initial_Position_x + Arrow5.getWidth(),P5_Initial_Position_y + Arrow5.getHeight());
		}
		if(D1_Initial_Position_x!=-1){
			defender1.layout(D1_Initial_Position_x, D1_Initial_Position_y,D1_Initial_Position_x + defender1.getWidth(),D1_Initial_Position_y + defender1.getHeight());
			Arrow7.layout(D1_Initial_Position_x, D1_Initial_Position_y,D1_Initial_Position_x + Arrow7.getWidth(),D1_Initial_Position_y + Arrow7.getHeight());
			defender1.invalidate();
			Arrow7.setRotation(Initial_D1_rotate);
			Arrow7.invalidate();
			//Log,i("debug", "D1_initial_pos set!");
		}
		if(D2_Initial_Position_x!=-1){
			defender2.layout(D2_Initial_Position_x, D2_Initial_Position_y,D2_Initial_Position_x + defender2.getWidth(),D2_Initial_Position_y + defender2.getHeight());
			Arrow8.layout(D2_Initial_Position_x, D2_Initial_Position_y,D2_Initial_Position_x + Arrow8.getWidth(),D2_Initial_Position_y + Arrow8.getHeight());
			defender2.invalidate();
			Arrow8.setRotation(Initial_D2_rotate);
			Arrow8.invalidate();
			//Log,i("debug", "D2_initial_pos set!");
		}
		if(D3_Initial_Position_x!=-1){
			defender3.layout(D3_Initial_Position_x, D3_Initial_Position_y,D3_Initial_Position_x + defender3.getWidth(),D3_Initial_Position_y + defender3.getHeight());
			Arrow9.layout(D3_Initial_Position_x, D3_Initial_Position_y,D3_Initial_Position_x + Arrow9.getWidth(),D3_Initial_Position_y + Arrow9.getHeight());
			defender3.invalidate();
			Arrow9.setRotation(Initial_D3_rotate);
			Arrow9.invalidate();
			//Log,i("debug", "D3_initial_pos set!");
		}
		if(D4_Initial_Position_x!=-1){
			defender4.layout(D4_Initial_Position_x, D4_Initial_Position_y,D4_Initial_Position_x + defender4.getWidth(),D4_Initial_Position_y + defender4.getHeight());
			Arrow10.layout(D4_Initial_Position_x, D4_Initial_Position_y,D4_Initial_Position_x + Arrow10.getWidth(),D4_Initial_Position_y + Arrow10.getHeight());
			defender4.invalidate();
			Arrow10.setRotation(Initial_D4_rotate);
			Arrow10.invalidate();
			//Log,i("debug", "D4_initial_pos set!");
		}
		if(D5_Initial_Position_x!=-1){
			defender5.layout(D5_Initial_Position_x, D5_Initial_Position_y,D5_Initial_Position_x + defender5.getWidth(),D5_Initial_Position_y + defender5.getHeight());
			Arrow11.layout(D5_Initial_Position_x, D5_Initial_Position_y,D5_Initial_Position_x + Arrow11.getWidth(),D5_Initial_Position_y + Arrow11.getHeight());
			defender5.invalidate();
			Arrow11.setRotation(Initial_D5_rotate);
			Arrow11.invalidate();
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
	
	public void Player_change_to_no_ball(){
		if(intersect_name!=0){
			switch(intersect_name){
			case 1:
				player1.setVisibility(player1.VISIBLE);
				if(intersect==true){
					////Log,i("debug","intersect=true,player1 should be layout on player_ball's position");
					player1.layout((int)player1_ball.getX()+30, (int)player1_ball.getY(),(int)player1_ball.getX()+30+player1.getWidth(), (int)player1_ball.getY()+player1.getHeight());
				}
				player1.invalidate();
				player1_ball.setVisibility(player1_ball.INVISIBLE);
				player1_ball.invalidate();
				Arrow1.layout((int)player1.getX(), (int)player1.getY(), (int)player1.getX()+player1.getWidth(), (int)player1.getY()+player1.getHeight());
				Arrow1.invalidate();
				break;
			case 2:
				player2.setVisibility(player2.VISIBLE);
				if(intersect==true){
					////Log,i("debug","intersect=true,player1 should be layout on player_ball's position");
					player2.layout((int)player2_ball.getX()+30, (int)player2_ball.getY(),(int)player2_ball.getX()+30+player2.getWidth(), (int)player2_ball.getY()+player2.getHeight());
				}
				player2.invalidate();
				player2_ball.setVisibility(player2_ball.INVISIBLE);
				player2_ball.invalidate();
				Arrow2.layout((int)player2.getX(), (int)player2.getY(), (int)player2.getX()+player2.getWidth(), (int)player2.getY()+player2.getHeight());
				Arrow2.invalidate();
				break;
			case 3:
				player3.setVisibility(player3.VISIBLE);
				if(intersect==true){
					////Log,i("debug","intersect=true,player1 should be layout on player_ball's position");
					player3.layout((int)player3_ball.getX()+30, (int)player3_ball.getY(),(int)player3_ball.getX()+30+player3.getWidth(), (int)player3_ball.getY()+player3.getHeight());
				}
				player3.invalidate();
				player3_ball.setVisibility(player3_ball.INVISIBLE);
				player3_ball.invalidate();
				Arrow3.layout((int)player3.getX(), (int)player3.getY(), (int)player3.getX()+player3.getWidth(), (int)player3.getY()+player3.getHeight());
				Arrow3.invalidate();
				break;
			case 4:
				player4.setVisibility(player4.VISIBLE);
				if(intersect==true){
					////Log,i("debug","intersect=true,player1 should be layout on player_ball's position");
					player4.layout((int)player4_ball.getX()+30, (int)player4_ball.getY(),(int)player4_ball.getX()+30+player4.getWidth(), (int)player4_ball.getY()+player4.getHeight());
				}
				player4.invalidate();
				player4_ball.setVisibility(player4_ball.INVISIBLE);
				player4_ball.invalidate();
				Arrow4.layout((int)player4.getX(), (int)player4.getY(), (int)player4.getX()+player4.getWidth(), (int)player4.getY()+player4.getHeight());
				Arrow4.invalidate();
				break;
			case 5:
				player5.setVisibility(player5.VISIBLE);
				if(intersect==true){
					////Log,i("debug","intersect=true,player1 should be layout on player_ball's position");
					player5.layout((int)player5_ball.getX()+30, (int)player5_ball.getY(),(int)player5_ball.getX()+30+player5.getWidth(), (int)player5_ball.getY()+player5.getHeight());
				}
				player5.invalidate();
				player5_ball.setVisibility(player5_ball.INVISIBLE);
				player5_ball.invalidate();
				Arrow5.layout((int)player5.getX(), (int)player5.getY(), (int)player5.getX()+player5.getWidth(), (int)player5.getY()+player5.getHeight());
				Arrow5.invalidate();
				break;
			}
		}
	}
	
	
	
	
	//TODO DTW
	/**************************************************************************/
	public void Do_Recommend(){     
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
    	File inFile = null;
    	File Files[]= dir.listFiles();
    	Vector <String> output = new Vector();
    	int DTW_Threshold=15;
    	
    	for(int j = 0;j<Files.length;j++){
    		String data = readFromFile(Files[j]);
    		String [] sData=data.split("\n");
    		P1_recommend.clear_all();
			P2_recommend.clear_all();
			P3_recommend.clear_all();
			P4_recommend.clear_all();
			P5_recommend.clear_all();
			B_recommend.clear_all();
    		for(int i =0;i<sData.length;i++){
    		//?sPlayer??road
    			
    			if(sData[i].equals("P1")){
    				i++;
    				while(!sData[i].equals("---")){
    					P1_recommend.setRoad(Integer.parseInt(sData[i]));
    					i++;
    				}
    			}
    			if(sData[i].equals("P2")){
    				i++;
    				while(!sData[i].equals("---")){
    					P2_recommend.setRoad(Integer.parseInt(sData[i]));
    					i++;
    				}
    			}
    			if(sData[i].equals("P3")){
    				i++;
    				while(!sData[i].equals("---")){
    					P3_recommend.setRoad(Integer.parseInt(sData[i]));
    					i++;
    				}
    			}
    			if(sData[i].equals("P4")){
    				i++;
    				while(!sData[i].equals("---")){
    					P4_recommend.setRoad(Integer.parseInt(sData[i]));
    					i++;
    				}
    			}
    			if(sData[i].equals("P5")){
    				i++;
    				while(!sData[i].equals("---")){
    					P5_recommend.setRoad(Integer.parseInt(sData[i]));
    					i++;
    				}
    			}
    			if(sData[i].equals("B")){
    				i++;
    				while(!sData[i].equals("---")){
    					B_recommend.setRoad(Integer.parseInt(sData[i]));
    					i++;
    				}
    			}
    		}//sData's for loop

    		if(recommend_switch == 0){
    			if(DTW(P1.getCmpltRoad(),P1_recommend.getCmpltRoad())<EDR_Threshold_Persent  ||
        				DTW(P2.getCmpltRoad(),P2_recommend.getCmpltRoad())<EDR_Threshold_Persent||
        				DTW(P3.getCmpltRoad(),P3_recommend.getCmpltRoad())<EDR_Threshold_Persent ||
        				DTW(P4.getCmpltRoad(),P4_recommend.getCmpltRoad())<EDR_Threshold_Persent ||
        				DTW(P5.getCmpltRoad(),P5_recommend.getCmpltRoad())<EDR_Threshold_Persent ||
        				DTW(B.getCmpltRoad(),B_recommend.getCmpltRoad())<EDR_Threshold_Persent){
        			//Log,i("reco", "Maybe Can recommend this strategy!"+Files[j].getName());
        			output.add(Files[j].getName());
        		}
        		Log.d("reco", "DTW = "+Double.toString(DTW(P1.getCmpltRoad(),P1_recommend.getCmpltRoad())));
        		/*
        		if(Files[j].getName().equals("right_up.txt")){
        			reco_result_textview.setText(String.format("%.2f", (DTW(P1.getCmpltRoad(),P1_recommend.getCmpltRoad()))));
        		}*/
        		
            	Log.d("reco", "DTW = "+Double.toString(DTW(P2.getCmpltRoad(),P2_recommend.getCmpltRoad())));
            	Log.d("reco", "DTW = "+Double.toString(DTW(P3.getCmpltRoad(),P3_recommend.getCmpltRoad())));
            	Log.d("reco", "DTW = "+Double.toString(DTW(P4.getCmpltRoad(),P4_recommend.getCmpltRoad())));
            	Log.d("reco", "DTW = "+Double.toString(DTW(P5.getCmpltRoad(),P5_recommend.getCmpltRoad())));
            	Log.d("reco", "DTW = "+Double.toString(DTW(B.getCmpltRoad(),B_recommend.getCmpltRoad())));
            	
    		}
    		else{
    			if(EDR(P1.getCmpltRoad(),P1_recommend.getCmpltRoad())<EDR_threshold(P1.getRoadSize(),P1_recommend.getRoadSize()) ||
        				EDR(P2.getCmpltRoad(),P2_recommend.getCmpltRoad())<EDR_threshold(P2.getRoadSize(),P2_recommend.getRoadSize()) ||
        				EDR(P3.getCmpltRoad(),P3_recommend.getCmpltRoad())<EDR_threshold(P3.getRoadSize(),P3_recommend.getRoadSize()) ||
        				EDR(P4.getCmpltRoad(),P4_recommend.getCmpltRoad())<EDR_threshold(P4.getRoadSize(),P4_recommend.getRoadSize()) ||
        				EDR(P5.getCmpltRoad(),P5_recommend.getCmpltRoad())<EDR_threshold(P5.getRoadSize(),P5_recommend.getRoadSize()) ||
        				EDR(B.getCmpltRoad(),B_recommend.getCmpltRoad())<EDR_threshold(B.getRoadSize(),B_recommend.getRoadSize())){
        			//Log,i("reco", "Maybe Can recommend this strategy!"+Files[j].getName());
        			output.add(Files[j].getName());
        		}
    			
    			Log.d("reco", "P1 size = "+Integer.toString(P1.getRoadSize())+", P1_reco size = "+Integer.toString(P1_recommend.getRoadSize()));
        		Log.d("reco", "EDR = "+Double.toString(EDR(P1.getCmpltRoad(),P1_recommend.getCmpltRoad()))+"EDR_Threshold = "+Double.toString(EDR_threshold(P1.getRoadSize(),P1_recommend.getRoadSize())));
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
        		Log.d("reco", "P2 size = "+Integer.toString(P2.getRoadSize())+", P2_reco size = "+Integer.toString(P2_recommend.getRoadSize()));
        		Log.d("reco", "EDR = "+Double.toString(EDR(P2.getCmpltRoad(),P2_recommend.getCmpltRoad()))+"EDR_Threshold = "+Double.toString(EDR_threshold(P2.getRoadSize(),P2_recommend.getRoadSize())));
        		Log.d("reco", "P3 size = "+Integer.toString(P3.getRoadSize())+", P3_reco size = "+Integer.toString(P3_recommend.getRoadSize()));
        		Log.d("reco", "EDR = "+Double.toString(EDR(P3.getCmpltRoad(),P3_recommend.getCmpltRoad()))+"EDR_Threshold = "+Double.toString(EDR_threshold(P3.getRoadSize(),P3_recommend.getRoadSize())));
                Log.d("reco", "EDR = "+Double.toString(EDR(P4.getCmpltRoad(),P4_recommend.getCmpltRoad()))+"EDR_Threshold = "+Double.toString(EDR_threshold(P4.getRoadSize(),P4_recommend.getRoadSize())));
                Log.d("reco", "EDR = "+Double.toString(EDR(P5.getCmpltRoad(),P5_recommend.getCmpltRoad()))+"EDR_Threshold = "+Double.toString(EDR_threshold(P5.getRoadSize(),P5_recommend.getRoadSize())));
                Log.d("reco", "EDR = "+Double.toString(EDR(B.getCmpltRoad(),B_recommend.getCmpltRoad()))+"EDR_Threshold = "+Double.toString(EDR_threshold(B.getRoadSize(),B_recommend.getRoadSize())));
                
    		}
    		
    			
    		
    		
    		
    		
    		
    	}//File_Names's for loop
    	return output;
	}
	
	private OnClickListener recommend_button_Listener = new OnClickListener(){//開始/停止錄製
    	@Override
    	public void onClick(View v) {
    		
    		if(recommend_button.isChecked()){
    			recommend_switch=1;
    		}
    		else{
    			recommend_switch = 0;
    		}
    	}
    };

	public double DTW(Vector<Integer> road_a,Vector<Integer> road_b){
		
		Vector<Integer> split_a = new Vector();
		Vector<Integer> split_b = new Vector();
		
		Vector<Double> nor_a=new Vector();
		Vector<Double> nor_b=new Vector();
		
		Vector<myPoint> finish_a = new Vector();
		Vector<myPoint> finish_b = new Vector();
		
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
				myPoint tmp = new myPoint();
				tmp.x=nor_a.get(i);
				tmp.y=nor_a.get(i+1);
				finish_a.add(tmp);
				i=i+1;
			}
			
			for(int i=0;i<nor_b.size();i++){
				myPoint tmp = new myPoint();
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
			return road1_size*EDR_Threshold_Persent;
		}
		else if(road1_size>road2_size){
			double EDR_basic=0;
			EDR_basic = road1_size-road2_size;
			result = EDR_basic+((road1_size-EDR_basic)*EDR_Threshold_Persent);
		}
		else if(road1_size<road2_size){
			double EDR_basic=0;
			EDR_basic = road2_size-road1_size;
			result = EDR_basic+((road2_size-EDR_basic)*EDR_Threshold_Persent);
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
		
		Vector<myPoint> finish_a = new Vector();
		Vector<myPoint> finish_b = new Vector();
		
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
				myPoint tmp = new myPoint();
				tmp.x=nor_a.get(i);
				tmp.y=nor_a.get(i+1);
				finish_a.add(tmp);
				
				/*******test**********/
				finish_b.add(tmp);
				
				/*********************/
				
				
				i=i+1;
			}
			
			for(int i=0;i<nor_b.size();i++){
				myPoint tmp = new myPoint();
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
	

public Vector<myPoint> process_simi_road(Vector<Integer> input_road){
	
	Vector<Integer> split_road = new Vector<Integer>();
	Vector<Double> nor_road = new Vector<Double>();
	Vector<myPoint> finish_road = new Vector<myPoint>();
	
	int count_0=0;
	for(int i=0;i<input_road.size();i++){
		if(input_road.get(i)!=0){
			split_road.add(input_road.get(i));
		}
		else{
			count_0++;
			//Log.d("debug", "count_0 =  "+Integer.toString(count_0));
		}
	}
	
	nor_road=normalization(split_road);
	
	for(int i=0;i<nor_road.size();i++){
		myPoint tmp = new myPoint();
		tmp.x=nor_road.get(i);
		tmp.y=nor_road.get(i+1);
		finish_road.add(tmp);
		i=i+1;
	}
	
	
	return finish_road;
}
	
public double Do_EDR(Vector<Integer> road_a,Vector<Integer> road_b){
	
	Vector<myPoint> input_road_a = new Vector<myPoint>();
	Vector<myPoint> input_road_b = new Vector<myPoint>();
	
	if(road_a.size()!=0)
		input_road_a = process_simi_road(road_a);
	if(road_b.size()!=0)
		input_road_b = process_simi_road(road_b);
	return EDR2(input_road_a,input_road_b);

}

public double EDR2(Vector<myPoint> road_a,Vector<myPoint> road_b){ 
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
				Vector<myPoint> rest_a = new Vector<myPoint>();
				Vector<myPoint> rest_b = new Vector<myPoint>();
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
		int x_sum=0;
		int y_sum=0;
		float x_mean=0;
		float y_mean=0;
		int x_sum_tmp=0;
		int y_sum_tmp=0;
		double x_standard_deviation=0;
		double y_standard_deviation=0;
		
		Vector<Double> n_road=new Vector();
		
		/*x*/
		for(int i=0;i<road.size();i=i+2){
			x_sum+=road.get(i);
		}
		x_mean=x_sum/(road.size()/2);
		
		for(int i=0;i<road.size();i=i+2){
			x_sum_tmp+=(road.get(i)-x_mean)*(road.get(i)-x_mean);
		}
		x_standard_deviation=Math.sqrt(x_sum_tmp/(road.size()/2));
		
		/*y*/
		for(int i=1;i<road.size();i=i+2){
			y_sum+=road.get(i);
		}
		y_mean=y_sum/(road.size()/2);
		
		for(int i=1;i<road.size();i=i+2){
			y_sum_tmp+=(road.get(i)-y_mean)*(road.get(i)-y_mean);
		}
		y_standard_deviation=Math.sqrt(y_sum_tmp/(road.size()/2));
		
		
		/*save new normalized road*/
		for(int i=0;i<road.size();i++){
			n_road.add((road.get(i)-x_mean)/x_standard_deviation);
			i=i+1;
			n_road.add((road.get(i)-y_mean)/y_standard_deviation);
		}
		
		
		
		return n_road;
	}
	
	/**************************************************************************/
    //TODO 儲存、載入戰術(按鈕的判斷在ButtonDraw.java裡面)
	/**************************************************************************/
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void Dialog_manage_tactic(){
	    LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View myv = inflater.inflate(R.layout.manage_tactic_layout, null);//把xml當成view來用，就能加入alert dialog裡面了

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(myv);
        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setLayout(450,400);

        final Button btn_tactic_save = (Button)myv.findViewById(R.id.save_strategy_button);
        btn_tactic_save.setOnTouchListener(new OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    btn_tactic_save.setBackgroundResource(R.drawable.btn_save_clicked);
                    dialog.dismiss();
                    save_dialog();

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btn_tactic_save.setBackgroundResource(R.drawable.btn_save);

                }
                return true;
            }
        });

        final Button btn_tactic_load = (Button)myv.findViewById(R.id.load_strategy_button);
        btn_tactic_load.setOnTouchListener(new OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    btn_tactic_load.setBackgroundResource(R.drawable.btn_load_clicked);
                    dialog.dismiss();
                    load_dialog();

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btn_tactic_load.setBackgroundResource(R.drawable.btn_load);
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
                select_category_id = new Long(l).intValue();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                select_category_id = 0;
            }
        });

    	final Button save_ok_button = (Button) myv.findViewById(R.id.save_strategy_button2);
    	save_ok_button.setOnTouchListener(new OnTouchListener(){//"Button of Enter the Strategy's Name
	        	@TargetApi(Build.VERSION_CODES.M)
				@Override
	        	public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        save_ok_button.setBackgroundResource(R.drawable.btn_save_clicked);

                        EditText name = (EditText) myv.findViewById(R.id.enter_name_text);//取得輸入text的view
                        String save_name = name.getText().toString();

                        //
                        //  Tactic file name :
                        //      [Tactic Name]_[Tactic category Id].json
                        //

                        File dir = getActivity().getBaseContext().getExternalFilesDir(null);
                        select_tactic_name = save_name;
                        File outFile = new File(dir, save_name + "_" + select_category_id + ".json");


						String tmp = new String();
                        //將資料寫入檔案中，若 package name 為 com.myapp
                        //就會產生 /data/data/com.myapp/files/test.txt 檔案

                        P_Initial_Position.clear();
                        P_Initial_Rotate.clear();

                        //region 2018.07.12 Version.2 以json格式來存檔
						JSONObject save_strategy = new JSONObject();
						try {
							JSONArray tmp_arr = new JSONArray();
							//region Initial_Position
							tmp_arr.put(P1_Initial_Position_x);
							tmp_arr.put(P1_Initial_Position_y);
							tmp_arr.put(P2_Initial_Position_x);
							tmp_arr.put(P2_Initial_Position_y);
							tmp_arr.put(P3_Initial_Position_x);
							tmp_arr.put(P3_Initial_Position_y);
							tmp_arr.put(P4_Initial_Position_x);
							tmp_arr.put(P4_Initial_Position_y);
							tmp_arr.put(P5_Initial_Position_x);
							tmp_arr.put(P5_Initial_Position_y);

							tmp_arr.put(D1_Initial_Position_x);
							tmp_arr.put(D1_Initial_Position_y);
							tmp_arr.put(D2_Initial_Position_x);
							tmp_arr.put(D2_Initial_Position_y);
							tmp_arr.put(D3_Initial_Position_x);
							tmp_arr.put(D3_Initial_Position_y);
							tmp_arr.put(D4_Initial_Position_x);
							tmp_arr.put(D4_Initial_Position_y);
							tmp_arr.put(D5_Initial_Position_x);
							tmp_arr.put(D5_Initial_Position_y);

							tmp_arr.put(B_Initial_Position_x);
							tmp_arr.put(B_Initial_Position_y);

							save_strategy.put("Initial_Position", tmp_arr);
							//endregion
							//region Initial_Rotation
							tmp_arr = new JSONArray();
							tmp_arr.put(Initial_P1_rotate);
							tmp_arr.put(Initial_P2_rotate);
							tmp_arr.put(Initial_P3_rotate);
							tmp_arr.put(Initial_P4_rotate);
							tmp_arr.put(Initial_P5_rotate);

							tmp_arr.put(Initial_D1_rotate);
							tmp_arr.put(Initial_D2_rotate);
							tmp_arr.put(Initial_D3_rotate);
							tmp_arr.put(Initial_D4_rotate);
							tmp_arr.put(Initial_D5_rotate);

							save_strategy.put("Initial_Rotation", tmp_arr);
							//endregion

							save_strategy.put("Initial_ball_holder", initial_ball_num);
							save_strategy.put("Tactic_name", save_name);
							save_strategy.put("Category_id", select_category_id);

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
							for(int i=0;i<RunLine.size();i++){
								JSONObject tmp_runbag = new JSONObject();

								tmp_runbag.put("start_time", String.valueOf(RunLine.get(i).getStartTime()));
								tmp_runbag.put("duration", String.valueOf(RunLine.get(i).getDuration()));
								tmp_runbag.put("handler", RunLine.get(i).getHandler());
								tmp_runbag.put("road_start", String.valueOf(RunLine.get(i).getRoadStart()));
								tmp_runbag.put("road_end", String.valueOf(RunLine.get(i).getRoadEnd()));
								tmp_runbag.put("rate", String.valueOf(RunLine.get(i).getRate()));
								tmp_runbag.put("ball_num", String.valueOf(RunLine.get(i).getBall_num()));

								tmp_runbag.put("path_type", String.valueOf(RunLine.get(i).getPath_type()));
								tmp_runbag.put("screen_angle", String.valueOf(RunLine.get(i).getScreen_angle()));
								tmp_runbag.put("dribble_angle", String.valueOf(RunLine.get(i).getDribble_angle()));
								tmp_runbag.put("dribble_length", String.valueOf(RunLine.get(i).getDribble_length()));

								tmp_runbag.put("dribble_start_x", String.valueOf(RunLine.get(i).getDribble_start_x()));
								tmp_runbag.put("dribble_start_y", String.valueOf(RunLine.get(i).getDribble_start_y()));

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
                        save_ok_button.setBackgroundResource(R.drawable.btn_save);
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

		firsttimeQuery = true;
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
				select_category_id = new Long(l).intValue();
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
				clear_paint();
				clear_record();

				File dir = getActivity().getBaseContext().getExternalFilesDir(null);
				String current_select_tactic = tactic_in_category.get(select_category_id).get(new Long(l).intValue()) + "_" + String.valueOf(select_category_id)+".json";
				select_tactic_name = tactic_in_category.get(select_category_id).get(new Long(l).intValue());
				File inFile = new File(dir, current_select_tactic);
				readStrategy(inFile);
				Player_move_to_start_position();

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

			P_Initial_Rotate.clear();
			P_Initial_Position.clear();

			JSONObject save_tactic_json = new JSONObject(jsonStr);
			initial_ball_num = Integer.valueOf(save_tactic_json.getString("Initial_ball_holder"));

			//region Load Initial Position
			JSONArray initial_array = save_tactic_json.getJSONArray("Initial_Position");
			P1_Initial_Position_x = Integer.valueOf(initial_array.getString(0));
			P1_Initial_Position_y = Integer.valueOf(initial_array.getString(1));
			P2_Initial_Position_x = Integer.valueOf(initial_array.getString(2));
			P2_Initial_Position_y = Integer.valueOf(initial_array.getString(3));
			P3_Initial_Position_x = Integer.valueOf(initial_array.getString(4));
			P3_Initial_Position_y = Integer.valueOf(initial_array.getString(5));
			P4_Initial_Position_x = Integer.valueOf(initial_array.getString(6));
			P4_Initial_Position_y = Integer.valueOf(initial_array.getString(7));
			P5_Initial_Position_x = Integer.valueOf(initial_array.getString(8));
			P5_Initial_Position_y = Integer.valueOf(initial_array.getString(9));

			D1_Initial_Position_x = Integer.valueOf(initial_array.getString(10));
			D1_Initial_Position_y = Integer.valueOf(initial_array.getString(11));
			D2_Initial_Position_x = Integer.valueOf(initial_array.getString(12));
			D2_Initial_Position_y = Integer.valueOf(initial_array.getString(13));
			D3_Initial_Position_x = Integer.valueOf(initial_array.getString(14));
			D3_Initial_Position_y = Integer.valueOf(initial_array.getString(15));
			D4_Initial_Position_x = Integer.valueOf(initial_array.getString(16));
			D4_Initial_Position_y = Integer.valueOf(initial_array.getString(17));
			D5_Initial_Position_x = Integer.valueOf(initial_array.getString(18));
			D5_Initial_Position_y = Integer.valueOf(initial_array.getString(19));

			B_Initial_Position_x = Integer.valueOf(initial_array.getString(20));
			B_Initial_Position_y = Integer.valueOf(initial_array.getString(21));
			//endregion

			//region Load Initial Rotation
			JSONArray initial_array_rotation = save_tactic_json.getJSONArray("Initial_Rotation");
			Initial_P1_rotate = Integer.valueOf(initial_array_rotation.getString(0));
			Initial_P2_rotate = Integer.valueOf(initial_array_rotation.getString(1));
			Initial_P3_rotate = Integer.valueOf(initial_array_rotation.getString(2));
			Initial_P4_rotate = Integer.valueOf(initial_array_rotation.getString(3));
			Initial_P5_rotate = Integer.valueOf(initial_array_rotation.getString(4));

			Initial_D1_rotate = Integer.valueOf(initial_array_rotation.getString(5));
			Initial_D2_rotate = Integer.valueOf(initial_array_rotation.getString(6));
			Initial_D3_rotate = Integer.valueOf(initial_array_rotation.getString(7));
			Initial_D4_rotate = Integer.valueOf(initial_array_rotation.getString(8));
			Initial_D5_rotate = Integer.valueOf(initial_array_rotation.getString(9));
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
				tmp.setBall_num(Integer.valueOf(tmp_runBag.getString("ball_num")));
				tmp.setPath_type(Integer.valueOf(tmp_runBag.getString("path_type")));
				tmp.setScreen_angle(Float.valueOf(tmp_runBag.getString("screen_angle")));
				tmp.setDribble_angle(Float.valueOf(tmp_runBag.getString("dribble_angle")));
				tmp.setDribble_length(Float.valueOf(tmp_runBag.getString("dribble_length")));

				tmp.setDribble_start_x(Integer.valueOf(tmp_runBag.getString("dribble_start_x")));
				tmp.setDribble_start_y(Integer.valueOf(tmp_runBag.getString("dribble_start_y")));

				RunLine.add(tmp);

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
					if(tmp.getPath_type() == 1){
						mainwrapfrag.create_screen_bar(x, y, 1, tmp.getScreen_angle(), load_seekbar_tmp_id);
					}else if(tmp.getPath_type() == 2){
						mainwrapfrag.create_dribble_line(tmp.getDribble_start_x(), tmp.getDribble_start_y(), 1, tmp.getDribble_angle(), tmp.getDribble_length(), load_seekbar_tmp_id);
					}

				}
				else if(tmp.getHandler().equals("P2_Handle")){
					timefrag.createSeekbar(2,load_seekbar_tmp_id,tmp.getStartTime(),tmp.getDuration());
					MainWrap mainwrapfrag = (MainWrap) getActivity().getFragmentManager().findFragmentById(R.id.MainWrap_frag);
					x=P2.getCmpltRoad().get(tmp.getRoadEnd()-1);
					y=P2.getCmpltRoad().get(tmp.getRoadEnd());
					mainwrapfrag.create_path_num_on_court(load_seekbar_tmp_id+1, x, y,load_seekbar_tmp_id);

					// 如果這一動有要擋拆
					if(tmp.getPath_type() == 1){
						mainwrapfrag.create_screen_bar(x, y, 2, tmp.getScreen_angle(), load_seekbar_tmp_id);
					}else if(tmp.getPath_type() == 2){
						mainwrapfrag.create_dribble_line(tmp.getDribble_start_x(), tmp.getDribble_start_y(), 2, tmp.getDribble_angle(), tmp.getDribble_length(), load_seekbar_tmp_id);
					}
				}
				else if(tmp.getHandler().equals("P3_Handle")){
					timefrag.createSeekbar(3,load_seekbar_tmp_id,tmp.getStartTime(),tmp.getDuration());
					MainWrap mainwrapfrag = (MainWrap) getActivity().getFragmentManager().findFragmentById(R.id.MainWrap_frag);
					x=P3.getCmpltRoad().get(tmp.getRoadEnd()-1);
					y=P3.getCmpltRoad().get(tmp.getRoadEnd());
					mainwrapfrag.create_path_num_on_court(load_seekbar_tmp_id+1, x, y,load_seekbar_tmp_id);

					// 如果這一動有要擋拆
					if(tmp.getPath_type() == 1){
						mainwrapfrag.create_screen_bar(x, y, 3, tmp.getScreen_angle(), load_seekbar_tmp_id);
					}
					else if(tmp.getPath_type() == 2){
						mainwrapfrag.create_dribble_line(tmp.getDribble_start_x(), tmp.getDribble_start_y(), 2, tmp.getDribble_angle(), tmp.getDribble_length(), load_seekbar_tmp_id);
					}
				}
				else if(tmp.getHandler().equals("P4_Handle")){
					timefrag.createSeekbar(4,load_seekbar_tmp_id,tmp.getStartTime(),tmp.getDuration());
					MainWrap mainwrapfrag = (MainWrap) getActivity().getFragmentManager().findFragmentById(R.id.MainWrap_frag);
					x=P4.getCmpltRoad().get(tmp.getRoadEnd()-1);
					y=P4.getCmpltRoad().get(tmp.getRoadEnd());
					mainwrapfrag.create_path_num_on_court(load_seekbar_tmp_id+1, x, y,load_seekbar_tmp_id);

					// 如果這一動有要擋拆
					if(tmp.getPath_type() == 1){
						mainwrapfrag.create_screen_bar(x, y, 3, tmp.getScreen_angle(), load_seekbar_tmp_id);
					}else if(tmp.getPath_type() == 2){
						mainwrapfrag.create_dribble_line(tmp.getDribble_start_x(), tmp.getDribble_start_y(), 3, tmp.getDribble_angle(), tmp.getDribble_length(), load_seekbar_tmp_id);
					}
				}
				else if(tmp.getHandler().equals("P5_Handle")){
					timefrag.createSeekbar(5,load_seekbar_tmp_id,tmp.getStartTime(),tmp.getDuration());
					MainWrap mainwrapfrag = (MainWrap) getActivity().getFragmentManager().findFragmentById(R.id.MainWrap_frag);
					x=P5.getCmpltRoad().get(tmp.getRoadEnd()-1);
					y=P5.getCmpltRoad().get(tmp.getRoadEnd());
					mainwrapfrag.create_path_num_on_court(load_seekbar_tmp_id+1, x, y,load_seekbar_tmp_id);

					// 如果這一動有要擋拆
					if(tmp.getPath_type() == 1){
						mainwrapfrag.create_screen_bar(x, y, 4, tmp.getScreen_angle(), load_seekbar_tmp_id);
					}
					else if(tmp.getPath_type() == 2){
						mainwrapfrag.create_dribble_line(tmp.getDribble_start_x(), tmp.getDribble_start_y(), 4, tmp.getDribble_angle(), tmp.getDribble_length(), load_seekbar_tmp_id);
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

		Player_move_to_start_position();
	}
	/**************************************************************************/
	
	/**************************************************************************/
	public void rotatePlayer(int input){     
			switch(rotate_which_player){
			case 1:
				//Log,i("debug", "P1_rotate ="+Integer.toString(input));
				P1_rotate=input;
				P_rotate=input;
				Arrow1.setRotation(input);
				if(recordcheck==false && RunLine.size()==0){
					Initial_P1_rotate = input;
				}
				break;
			case 2:
				P2_rotate=input;
				P_rotate=input;
				Arrow2.setRotation(input);
				if(recordcheck==false && RunLine.size()==0){
					Initial_P2_rotate = input;
				}
				break;
			case 3:
				P3_rotate=input;
				P_rotate=input;
				Arrow3.setRotation(input);
				if(recordcheck==false && RunLine.size()==0){
					Initial_P3_rotate = input;
				}
				break;
			case 4:
				P4_rotate=input;
				P_rotate=input;
				Arrow4.setRotation(input);
				if(recordcheck==false && RunLine.size()==0){
					Initial_P4_rotate = input;
				}
				break;
			case 5:
				P5_rotate=input;
				P_rotate=input;
				Arrow5.setRotation(input);
				if(recordcheck==false && RunLine.size()==0){
					Initial_P5_rotate = input;
				}
				break;
			case 6:
				B_rotate=input;
				break;
			case 7:
				//Log,i("debug", "D1_rotate ="+Integer.toString(input));
				D1_rotate=input;
				P_rotate=input;
				Arrow7.setRotation(input);
				if(recordcheck==false && RunLine.size()==0){
					Initial_D1_rotate = input;
				}
				break;
			case 8:
				D2_rotate=input;
				P_rotate=input;
				Arrow8.setRotation(input);
				if(recordcheck==false && RunLine.size()==0){
					Initial_D2_rotate = input;
				}
				break;
			case 9:
				D3_rotate=input;
				P_rotate=input;
				Arrow9.setRotation(input);
				if(recordcheck==false && RunLine.size()==0){
					Initial_D3_rotate = input;
				}
				break;
			case 10:
				D4_rotate=input;
				P_rotate=input;
				Arrow10.setRotation(input);
				if(recordcheck==false && RunLine.size()==0){
					Initial_D4_rotate = input;
				}
				break;
			case 11:
				D5_rotate=input;
				P_rotate=input;
				Arrow11.setRotation(input);
				if(recordcheck==false && RunLine.size()==0){
					Initial_D5_rotate = input;
				}
				break;
			default:
				//Log,i("debug", "rotate_which_player error!!");
			}
	}
	/**************************************************************************/

	public void playButton() {  
		if (RunLine.isEmpty()) {
			//Log.e("empty!", String.valueOf(RunLine.size()));
		} else {
            /*先把全部player移到按下錄製鍵時的位置*/
			Player_move_to_start_position();
			//Player_change_to_no_ball();
			/****************************/
			
			//////////////////////////////////////// Time
			//////////////////////////////////////// counter///////////////////////////////////////
			new Thread(new Runnable() {// Time counter count on per second 
				@Override
				public void run() { 
					int time = 0;
					int RunLineSize = RunLine.size();
					while (time < Total_time && playing==1) {
						try {
							//Log.e("time = ", String.valueOf(time));
							// do RunLine here!!
							////// check each road's start time in
							// RunLine///////
							checkRunLine(time, RunLineSize);
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

	protected void play(final boolean isDribble, final int speed, final Handler play_handler, final int in_k, final int in_j) {
		//Log,i("debug","initial_ball_num="+Integer.toString(initial_ball_num));
		new Thread(new Runnable() {  
			@Override
			public void run() {  
				int play_k = in_k;
				while (play_k < in_j - 1 && playing==1) {
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

			Arrow1.layout(P1.handleGetRoad(sentInt), P1.handleGetRoad(sentInt + 1),
					P1.handleGetRoad(sentInt) + Arrow1.getWidth(),
					P1.handleGetRoad(sentInt + 1) + Arrow1.getHeight());
			
			if(sentInt!=1){
				Arrow1.setRotation(P1.getMyRotation(sentInt/2));
			}
			else{
				Arrow1.setRotation(P1.getMyRotation(sentInt));
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

			Arrow2.layout(P2.handleGetRoad(sentInt), P2.handleGetRoad(sentInt + 1),
					P2.handleGetRoad(sentInt) + Arrow2.getWidth(),
					P2.handleGetRoad(sentInt + 1) + Arrow2.getHeight());
			
			if(sentInt!=1){
				Arrow2.setRotation(P2.getMyRotation(sentInt/2));
			}
			else{
				Arrow2.setRotation(P2.getMyRotation(sentInt));
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

			Arrow3.layout(P3.handleGetRoad(sentInt), P3.handleGetRoad(sentInt + 1),
					P3.handleGetRoad(sentInt) + Arrow3.getWidth(),
					P3.handleGetRoad(sentInt + 1) + Arrow3.getHeight());
			
			if(sentInt!=1){
				Arrow3.setRotation(P3.getMyRotation(sentInt/2));
			}
			else{
				Arrow3.setRotation(P3.getMyRotation(sentInt));
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

			Arrow4.layout(P4.handleGetRoad(sentInt), P4.handleGetRoad(sentInt + 1),
					P4.handleGetRoad(sentInt) + Arrow4.getWidth(),
					P4.handleGetRoad(sentInt + 1) + Arrow4.getHeight());
			
			if(sentInt!=1){
				Arrow4.setRotation(P4.getMyRotation(sentInt/2));
			}
			else{
				Arrow4.setRotation(P4.getMyRotation(sentInt));
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

			Arrow5.layout(P5.handleGetRoad(sentInt), P5.handleGetRoad(sentInt + 1),
					P5.handleGetRoad(sentInt) + Arrow5.getWidth(),
					P5.handleGetRoad(sentInt + 1) + Arrow5.getHeight());
			
			if(sentInt!=1){
				Arrow5.setRotation(P5.getMyRotation(sentInt/2));
			}
			else{
				Arrow5.setRotation(P5.getMyRotation(sentInt));
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
			
			Arrow7.layout(D1.handleGetRoad(sentInt), D1.handleGetRoad(sentInt + 1),
					D1.handleGetRoad(sentInt) + Arrow7.getWidth(),
					D1.handleGetRoad(sentInt + 1) + Arrow7.getHeight());
			
			if(sentInt!=1){
				Arrow7.setRotation(D1.getMyRotation(sentInt/2));
			}
			else{
				Arrow7.setRotation(D1.getMyRotation(sentInt));
			}
		}
	};
	
	Handler D2_Handle = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int sentInt = msg.getData().getInt("what");
			defender2.layout(D2.handleGetRoad(sentInt), D2.handleGetRoad(sentInt + 1),
							D2.handleGetRoad(sentInt) + defender2.getWidth(),
							D2.handleGetRoad(sentInt + 1) + defender2.getHeight());
			
			Arrow8.layout(D2.handleGetRoad(sentInt), D2.handleGetRoad(sentInt + 1),
					D2.handleGetRoad(sentInt) + Arrow8.getWidth(),
					D2.handleGetRoad(sentInt + 1) + Arrow8.getHeight());
			
			if(sentInt!=1){
				Arrow8.setRotation(D2.getMyRotation(sentInt/2));
			}
			else{
				Arrow8.setRotation(D2.getMyRotation(sentInt));
			}
		}
	};
	
	Handler D3_Handle = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int sentInt = msg.getData().getInt("what");
			defender3.layout(D3.handleGetRoad(sentInt), D3.handleGetRoad(sentInt + 1),
							D3.handleGetRoad(sentInt) + defender3.getWidth(),
							D3.handleGetRoad(sentInt + 1) + defender3.getHeight());
			
			Arrow9.layout(D3.handleGetRoad(sentInt), D3.handleGetRoad(sentInt + 1),
					D3.handleGetRoad(sentInt) + Arrow9.getWidth(),
					D3.handleGetRoad(sentInt + 1) + Arrow9.getHeight());
			
			if(sentInt!=1){
				Arrow9.setRotation(D3.getMyRotation(sentInt/2));
			}
			else{
				Arrow9.setRotation(D3.getMyRotation(sentInt));
			}
		}
	};
	
	Handler D4_Handle = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int sentInt = msg.getData().getInt("what");
			defender4.layout(D4.handleGetRoad(sentInt), D4.handleGetRoad(sentInt + 1),
							D4.handleGetRoad(sentInt) + defender4.getWidth(),
							D4.handleGetRoad(sentInt + 1) + defender4.getHeight());
			
			Arrow10.layout(D4.handleGetRoad(sentInt), D4.handleGetRoad(sentInt + 1),
					D4.handleGetRoad(sentInt) + Arrow10.getWidth(),
					D4.handleGetRoad(sentInt + 1) + Arrow10.getHeight());
			
			if(sentInt!=1){
				Arrow10.setRotation(D4.getMyRotation(sentInt/2));
			}
			else{
				Arrow10.setRotation(D4.getMyRotation(sentInt));
			}
		}
	};

	Handler D5_Handle = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int sentInt = msg.getData().getInt("what");
			defender5.layout(D5.handleGetRoad(sentInt), D5.handleGetRoad(sentInt + 1),
							D5.handleGetRoad(sentInt) + defender5.getWidth(),
							D5.handleGetRoad(sentInt + 1) + defender5.getHeight());
			
			Arrow11.layout(D5.handleGetRoad(sentInt), D5.handleGetRoad(sentInt + 1),
					D5.handleGetRoad(sentInt) + Arrow11.getWidth(),
					D5.handleGetRoad(sentInt + 1) + Arrow11.getHeight());
			
			if(sentInt!=1){
				Arrow11.setRotation(D5.getMyRotation(sentInt/2));
			}
			else{
				Arrow11.setRotation(D5.getMyRotation(sentInt));
			}
		}
	};
	
	Handler RunLineCheck_Handle = new Handler() {    

		@Override
		public void handleMessage(Message msg) {   

			int sentI = msg.getData().getInt("what");
			int sentTime = msg.getData().getInt("time");
			if (RunLine.get(sentI).getStartTime() * 1000 == sentTime) {
				boolean isDribble = (RunLine.get(sentI).getPath_type() == 2);
				if ( RunLine.get(sentI).getHandler().equals("P1_Handle")) {
					play(isDribble,RunLine.get(sentI).getRate(), P1_Handle, RunLine.get(sentI).getRoadStart(),
							RunLine.get(sentI).getRoadEnd());
				} else if (RunLine.get(sentI).getHandler().equals("P2_Handle")) {
					play(isDribble,RunLine.get(sentI).getRate(), P2_Handle, RunLine.get(sentI).getRoadStart(),
							RunLine.get(sentI).getRoadEnd());
				} else if (RunLine.get(sentI).getHandler().equals("P3_Handle")) {
					play(isDribble,RunLine.get(sentI).getRate(), P3_Handle, RunLine.get(sentI).getRoadStart(),
							RunLine.get(sentI).getRoadEnd());
				} else if (RunLine.get(sentI).getHandler().equals("P4_Handle")) {
					play(isDribble,RunLine.get(sentI).getRate(), P4_Handle, RunLine.get(sentI).getRoadStart(),
							RunLine.get(sentI).getRoadEnd());
				} else if (RunLine.get(sentI).getHandler().equals("P5_Handle")) {
					play(isDribble,RunLine.get(sentI).getRate(), P5_Handle, RunLine.get(sentI).getRoadStart(),
							RunLine.get(sentI).getRoadEnd());
				} else if (RunLine.get(sentI).getHandler().equals("B_Handle")) {
					play(isDribble,RunLine.get(sentI).getRate(), B_Handle, RunLine.get(sentI).getRoadStart(),
							RunLine.get(sentI).getRoadEnd());
				}
				else if (RunLine.get(sentI).getHandler().equals("D1_Handle")) {
					play(isDribble,RunLine.get(sentI).getRate(), D1_Handle, RunLine.get(sentI).getRoadStart(),
							RunLine.get(sentI).getRoadEnd());
				} else if (RunLine.get(sentI).getHandler().equals("D2_Handle")) {
					play(isDribble,RunLine.get(sentI).getRate(), D2_Handle, RunLine.get(sentI).getRoadStart(),
							RunLine.get(sentI).getRoadEnd());
				} else if (RunLine.get(sentI).getHandler().equals("D3_Handle")) {
					play(isDribble,RunLine.get(sentI).getRate(), D3_Handle, RunLine.get(sentI).getRoadStart(),
							RunLine.get(sentI).getRoadEnd());
				} else if (RunLine.get(sentI).getHandler().equals("D4_Handle")) {
					play(isDribble,RunLine.get(sentI).getRate(), D4_Handle, RunLine.get(sentI).getRoadStart(),
							RunLine.get(sentI).getRoadEnd());
				} else if (RunLine.get(sentI).getHandler().equals("D5_Handle")) {
					play(isDribble,RunLine.get(sentI).getRate(), D5_Handle, RunLine.get(sentI).getRoadStart(),
							RunLine.get(sentI).getRoadEnd());
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
						c_idx = c1_idx;
						P_tempcurve_x = P1_tempcurve_x;
						P_tempcurve_y = P1_tempcurve_y;
						P_rotate = P1_rotate;
						player_paint = player1_paint;
						rotate_which_player = 1;
						Arrow = Arrow1;
						P_startIndex = P1_startIndex;
						handle_name = "P1_Handle";
						P_Initial_Position_x = P1_Initial_Position_x;
						P_Initial_Position_y = P1_Initial_Position_y;
						seekbar_player_Id = 1;
						rc_p1 = new Rect((int) event.getX(),my - v.getTop(),(int) event.getX()+ v.getWidth(),my - v.getTop()+v.getHeight());

						TimeLine timefrag1 = (TimeLine) getActivity().getFragmentManager().findFragmentById(R.id.time_line);
						timefrag1.changeLayout(1);
						
						//Log,i("debug", "first    P_startIndex="+Integer.toString(P_startIndex));
						//Log,i("debug", "first    P1_startIndex="+Integer.toString(P1_startIndex));
					}
					else if (v.getTag().toString().equals("2")){
						//Log,i("debug","P2 player Ontouch!" );
						player=P2;
						c_idx=c2_idx;
						P_tempcurve_x = P2_tempcurve_x;
						P_tempcurve_y = P2_tempcurve_y;
						rotate_which_player=2;
						P_rotate=P2_rotate;
						player_paint=player2_paint;
						Arrow=Arrow2;
						P_startIndex=P2_startIndex;
						handle_name="P2_Handle";
						P_Initial_Position_x=P2_Initial_Position_x;
						P_Initial_Position_y=P2_Initial_Position_y;
						seekbar_player_Id=2;
						//Log,i("debug", "first    P_startIndex="+Integer.toString(P_startIndex));
						//Log,i("debug", "first    P2_startIndex="+Integer.toString(P2_startIndex));
						rc_p2=new Rect((int) event.getX(),my - v.getTop(),(int) event.getX()+ v.getWidth(),my - v.getTop()+v.getHeight());
						
						TimeLine timefrag1 = (TimeLine) getActivity().getFragmentManager().findFragmentById(R.id.time_line);
						timefrag1.changeLayout(2);
					}
					else if (v.getTag().toString().equals("3")){
						//Log,i("debug","P3    player ontouch!" );
						player=P3;
						c_idx=c3_idx;
						P_tempcurve_x = P3_tempcurve_x;
						P_tempcurve_y = P3_tempcurve_y;
						P_rotate=P3_rotate;
						player_paint=player3_paint;
						rotate_which_player=3;
						Arrow=Arrow3;
						P_startIndex=P3_startIndex;
						handle_name="P3_Handle";
						P_Initial_Position_x=P3_Initial_Position_x;
						P_Initial_Position_y=P3_Initial_Position_y;
						seekbar_player_Id=3;
						rc_p3=new Rect((int) event.getX(),my - v.getTop(),(int) event.getX()+ v.getWidth(),my - v.getTop()+v.getHeight());
						//Log,i("debug", "first    P_startIndex="+Integer.toString(P_startIndex));
						//Log,i("debug", "first    P3_startIndex="+Integer.toString(P3_startIndex));
						
						TimeLine timefrag1 = (TimeLine) getActivity().getFragmentManager().findFragmentById(R.id.time_line);
						timefrag1.changeLayout(3);
					}
					else if (v.getTag().toString().equals("4")){
						//Log,i("debug","P4    player ontouch!" );
						player=P4;
						c_idx=c4_idx;
						P_tempcurve_x = P4_tempcurve_x;
						P_tempcurve_y = P4_tempcurve_y;
						P_rotate=P4_rotate;
						player_paint=player4_paint;
						rotate_which_player=4;
						Arrow=Arrow4;
						P_startIndex=P4_startIndex;
						handle_name="P4_Handle";
						P_Initial_Position_x=P4_Initial_Position_x;
						P_Initial_Position_y=P4_Initial_Position_y;
						seekbar_player_Id=4;
						rc_p4=new Rect((int) event.getX(),my - v.getTop(),(int) event.getX()+ v.getWidth(),my - v.getTop()+v.getHeight());
						//Log,i("debug", "first    P_startIndex="+Integer.toString(P_startIndex));
						//Log,i("debug", "first    P4_startIndex="+Integer.toString(P4_startIndex));
						
						TimeLine timefrag1 = (TimeLine) getActivity().getFragmentManager().findFragmentById(R.id.time_line);
						timefrag1.changeLayout(4);
					}
					else if (v.getTag().toString().equals("5")){
						//Log,i("debug","P5    player ontouch!" );
						player=P5;
						c_idx=c5_idx;
						P_tempcurve_x = P5_tempcurve_x;
						P_tempcurve_y = P5_tempcurve_y;
						P_rotate=P5_rotate;
						player_paint=player5_paint;
						rotate_which_player=5;
						Arrow=Arrow5;
						P_startIndex=P5_startIndex;
						handle_name="P5_Handle";
						P_Initial_Position_x=P5_Initial_Position_x;
						P_Initial_Position_y=P5_Initial_Position_y;
						seekbar_player_Id=5;
						rc_p5=new Rect((int) event.getX(),my - v.getTop(),(int) event.getX()+ v.getWidth(),my - v.getTop()+v.getHeight());
						//Log,i("debug", "first    P_startIndex="+Integer.toString(P_startIndex));
						//Log,i("debug", "first    P5_startIndex="+Integer.toString(P5_startIndex));
						
						TimeLine timefrag1 = (TimeLine) getActivity().getFragmentManager().findFragmentById(R.id.time_line);
						timefrag1.changeLayout(5);
					}
					else if (v.getTag().toString().equals("6")){
						//Log,i("debug","B    player ontouch!" );
						player=B;
						rotate_which_player = 6;
						c_idx=Ball_idx;
						P_tempcurve_x = Ball_tempcurve_x;
						P_tempcurve_y = Ball_tempcurve_y;
						P_rotate = 0;
						player_paint = ball_paint;
						Arrow=null;
						P_startIndex = B_startIndex;
						handle_name = "B_Handle";
						P_Initial_Position_x = B_Initial_Position_x;
						P_Initial_Position_y = B_Initial_Position_y;
						seekbar_player_Id = 6;
						rc_ball=new Rect((int) event.getX(),my - v.getTop(),(int) event.getX()+ v.getWidth(),my - v.getTop()+v.getHeight());
						//Log,i("debug", "first    P_startIndex="+Integer.toString(P_startIndex));
						//Log,i("debug", "first    B_startIndex="+Integer.toString(B_startIndex));
						TimeLine timefrag1 = (TimeLine) getActivity().getFragmentManager().findFragmentById(R.id.time_line);
						timefrag1.changeLayout(6);
					}
					else if(v.getTag().toString().equals("D1")){
						//Log,i("debug","D1 ontouch!" );
						player=D1;
						c_idx=cd1_idx;
						P_tempcurve_x = D1_tempcurve_x;
						P_tempcurve_y = D1_tempcurve_y;
						P_rotate=D1_rotate;
						player_paint=d1_paint;
						rotate_which_player=7;
						Arrow=Arrow7;
						P_startIndex=D1_startIndex;
						handle_name="D1_Handle";
						P_Initial_Position_x=D1_Initial_Position_x;
						P_Initial_Position_y=D1_Initial_Position_y;
						seekbar_player_Id=7;
						//Log,i("debug", "first    P_startIndex="+Integer.toString(P_startIndex));
						//Log,i("debug", "first    D1_startIndex="+Integer.toString(D1_startIndex));
						TimeLine timefrag1 = (TimeLine) getActivity().getFragmentManager().findFragmentById(R.id.time_line);
						timefrag1.changeLayout(7);
					}
					else if(v.getTag().toString().equals("D2")){
						//Log,i("debug","D2 ontouch!" );
						player=D2;
						c_idx=cd2_idx;
						P_tempcurve_x = D2_tempcurve_x;
						P_tempcurve_y = D2_tempcurve_y;
						P_rotate=D2_rotate;
						player_paint=d2_paint;
						rotate_which_player=8;
						Arrow=Arrow8;
						P_startIndex=D2_startIndex;
						handle_name="D2_Handle";
						P_Initial_Position_x=D2_Initial_Position_x;
						P_Initial_Position_y=D2_Initial_Position_y;
						seekbar_player_Id=8;
						//Log,i("debug", "first    P_startIndex="+Integer.toString(P_startIndex));
						//Log,i("debug", "first    D2_startIndex="+Integer.toString(D2_startIndex));
						TimeLine timefrag1 = (TimeLine) getActivity().getFragmentManager().findFragmentById(R.id.time_line);
						timefrag1.changeLayout(8);
					}
					else if(v.getTag().toString().equals("D3")){
						//Log,i("debug","D3 ontouch!" );
						player=D3;
						c_idx=cd3_idx;
						P_tempcurve_x = D3_tempcurve_x;
						P_tempcurve_y = D3_tempcurve_y;
						P_rotate=D3_rotate;
						player_paint=d3_paint;
						rotate_which_player=9;
						Arrow=Arrow9;
						P_startIndex=D3_startIndex;
						handle_name="D3_Handle";
						P_Initial_Position_x=D3_Initial_Position_x;
						P_Initial_Position_y=D3_Initial_Position_y;
						seekbar_player_Id=9;
						//Log,i("debug", "first    P_startIndex="+Integer.toString(P_startIndex));
						//Log,i("debug", "first    D3_startIndex="+Integer.toString(D3_startIndex));
						TimeLine timefrag1 = (TimeLine) getActivity().getFragmentManager().findFragmentById(R.id.time_line);
						timefrag1.changeLayout(9);
					}
					else if(v.getTag().toString().equals("D4")){
						//Log,i("debug","D4 ontouch!" );
						player=D4;
						c_idx=cd4_idx;
						P_tempcurve_x = D4_tempcurve_x;
						P_tempcurve_y = D4_tempcurve_y;
						P_rotate=D4_rotate;
						player_paint=d4_paint;
						rotate_which_player=10;
						Arrow=Arrow10;
						P_startIndex=D4_startIndex;
						handle_name="D4_Handle";
						P_Initial_Position_x=D4_Initial_Position_x;
						P_Initial_Position_y=D4_Initial_Position_y;
						seekbar_player_Id=10;
						//Log,i("debug", "first    P_startIndex="+Integer.toString(P_startIndex));
						//Log,i("debug", "first    D4_startIndex="+Integer.toString(D4_startIndex));
						TimeLine timefrag1 = (TimeLine) getActivity().getFragmentManager().findFragmentById(R.id.time_line);
						timefrag1.changeLayout(10);
					}
					else if(v.getTag().toString().equals("D5")){
						//Log,i("debug","D5 ontouch!" );
						player=D5;
						c_idx=cd5_idx;
						P_tempcurve_x = D5_tempcurve_x;
						P_tempcurve_y = D5_tempcurve_y;
						P_rotate=D5_rotate;
						player_paint=d5_paint;
						rotate_which_player=11;
						Arrow=Arrow11;
						P_startIndex=D5_startIndex;
						handle_name="D5_Handle";
						P_Initial_Position_x=D5_Initial_Position_x;
						P_Initial_Position_y=D5_Initial_Position_y;
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
					if (recordcheck == true) {
						player.setRoad(0); // split positions
						player.setRoad_3d(0);
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
						rc_p1=new Rect(x,y,x+ v.getWidth(),y+v.getHeight());
					}
					else if(v.getTag().toString().equals("2")){
						rc_p2=new Rect(x,y,x+ v.getWidth(),y+v.getHeight());
					}
					else if(v.getTag().toString().equals("3")){
						rc_p3=new Rect(x,y,x+ v.getWidth(),y+v.getHeight());
					}
					else if(v.getTag().toString().equals("4")){
						rc_p4=new Rect(x,y,x+ v.getWidth(),y+v.getHeight());
					}
					else if(v.getTag().toString().equals("5")){
						rc_p5=new Rect(x,y,x+ v.getWidth(),y+v.getHeight());
					}
					else if(v.getTag().toString().equals("6")){
						rc_ball=new Rect(x,y,x+ v.getWidth(),y+v.getHeight());

                        //region 這裡是先把目前持有球的player先變回無持球狀態，再接著判斷有沒有intersect，確保當兩個player太接近的時候，會導致player明明無持球卻還是呈現有持球的狀態*/
						Player_change_to_no_ball();
                        //endregion

                        //region 判斷是跟哪個player intersect
						if(Rect.intersects(rc_p1, rc_ball)){
							//Log,i("debug", "P1 Intersects!");
							intersect_name=1;
							intersect=true;
							if(player1_ball.getVisibility()==player1_ball.INVISIBLE){
								player1_ball.layout((int)player1.getX()-30, (int)player1.getY(), (int)player1.getX()-30+200, (int)player1.getY()+120);
								player1_ball.setVisibility(player1_ball.VISIBLE);
								player1_ball.invalidate();
								player1.setVisibility(player1.INVISIBLE);
								player1.invalidate();
							}
						}
						else if (Rect.intersects(rc_p2, rc_ball)){
							//Log,i("debug", "P2 Intersects!");
							intersect_name=2;
							intersect=true;
							if(player2_ball.getVisibility()==player2_ball.INVISIBLE){
								player2_ball.layout((int)player2.getX()-30, (int)player2.getY(), (int)player2.getX()-30+200, (int)player2.getY()+120);
								player2_ball.setVisibility(player2_ball.VISIBLE);
								player2_ball.invalidate();
								player2.setVisibility(player2.INVISIBLE);
								player2.invalidate();
							}
						}
						else if (Rect.intersects(rc_p3, rc_ball)){
							//Log,i("debug", "P3 Intersects!");
							intersect_name=3;
							intersect=true;
							if(player3_ball.getVisibility()==player3_ball.INVISIBLE){
								player3_ball.layout((int)player3.getX()-30, (int)player3.getY(), (int)player3.getX()-30+200, (int)player3.getY()+120);
								player3_ball.setVisibility(player3_ball.VISIBLE);
								player3_ball.invalidate();
								player3.setVisibility(player3.INVISIBLE);
								player3.invalidate();
							}
						}
						else if (Rect.intersects(rc_p4, rc_ball)){
							//Log,i("debug", "P4 Intersects!");
							intersect_name=4;
							intersect=true;
							if(player4_ball.getVisibility()==player4_ball.INVISIBLE){
								player4_ball.layout((int)player4.getX()-30, (int)player4.getY(), (int)player4.getX()-30+200, (int)player4.getY()+120);
								player4_ball.setVisibility(player4_ball.VISIBLE);
								player4_ball.invalidate();
								player4.setVisibility(player4.INVISIBLE);
								player4.invalidate();
							}
						}
						else if (Rect.intersects(rc_p5, rc_ball)){
							//Log,i("debug", "P5 Intersects!");
							intersect_name=5;
							intersect=true;
							if(player5_ball.getVisibility()==player5_ball.INVISIBLE){
								player5_ball.layout((int)player5.getX()-30, (int)player5.getY(), (int)player5.getX()-30+200, (int)player5.getY()+120);
								player5_ball.setVisibility(player5_ball.VISIBLE);
								player5_ball.invalidate();
								player5.setVisibility(player5.INVISIBLE);
								player5.invalidate();
							}
						}
						else{
							switch (intersect_name){
								case 1:
									player1.setVisibility(player1.VISIBLE);
									if(intersect==true){
										////Log,i("debug","intersect=true,player1 should be layout on player_ball's position");
										player1.layout((int)player1_ball.getX()+30, (int)player1_ball.getY(),(int)player1_ball.getX()+30+player1.getWidth(), (int)player1_ball.getY()+player1.getHeight());
									}
									player1.invalidate();
									player1_ball.setVisibility(player1_ball.INVISIBLE);
									player1_ball.invalidate();
									break;
								case 2:
									player2.setVisibility(player2.VISIBLE);
									if(intersect==true){
										////Log,i("debug","intersect=true,player1 should be layout on player_ball's position");
										player2.layout((int)player2_ball.getX()+30, (int)player2_ball.getY(),(int)player2_ball.getX()+30+player2.getWidth(), (int)player2_ball.getY()+player2.getHeight());
									}
									player2.invalidate();
									player2_ball.setVisibility(player2_ball.INVISIBLE);
									player2_ball.invalidate();
									break;
								case 3:
									player3.setVisibility(player3.VISIBLE);
									if(intersect==true){
										////Log,i("debug","intersect=true,player1 should be layout on player_ball's position");
										player3.layout((int)player3_ball.getX()+30, (int)player3_ball.getY(),(int)player3_ball.getX()+30+player3.getWidth(), (int)player3_ball.getY()+player3.getHeight());
									}
									player3.invalidate();
									player3_ball.setVisibility(player3_ball.INVISIBLE);
									player3_ball.invalidate();
									break;
								case 4:
									player4.setVisibility(player4.VISIBLE);
									if(intersect==true){
										////Log,i("debug","intersect=true,player1 should be layout on player_ball's position");
										player4.layout((int)player4_ball.getX()+30, (int)player4_ball.getY(),(int)player4_ball.getX()+30+player4.getWidth(), (int)player4_ball.getY()+player4.getHeight());
									}
									player4.invalidate();
									player4_ball.setVisibility(player4_ball.INVISIBLE);
									player4_ball.invalidate();
									break;
								case 5:
									player5.setVisibility(player5.VISIBLE);
									if(intersect==true){
										////Log,i("debug","intersect=true,player1 should be layout on player_ball's position");
										player5.layout((int)player5_ball.getX()+30, (int)player5_ball.getY(),(int)player5_ball.getX()+30+player5.getWidth(), (int)player5_ball.getY()+player5.getHeight());
									}
									player5.invalidate();
									player5_ball.setVisibility(player5_ball.INVISIBLE);
									player5_ball.invalidate();
									break;
							}

							intersect=false;
							intersect_name = 0;
						}
						//endregion
					}


					//region 當正在繪製軌跡時
					if (recordcheck == true) {
						move_count += 2;
						player.setRoad(x);
						player.setRoad(y);
						player.setRoad_3d((int) event.getRawX());
						player.setRoad_3d((int) event.getRawY());
						player.setMyRotation(P_rotate);
						//Log,i("debug", "player setMyRotation");
						
						P_tempcurve_x.add(c_idx, x);
						P_tempcurve_y.add(c_idx, y);
						c_idx++;
						
						P_curve_x.add((float)mx);
						P_curve_y.add((float)my);

						// 每畫三個點
						if(c_idx == N){
							boolean isBallHolder = (rotate_which_player == intersect_name);
							Log.i("debug", "Touch Event : "+rotate_which_player+", "+intersect_name);
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

							previous_direction.add(screen_direction);
							previous_dribble_direction.add(last_direction);
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
						Pcanvas.drawBitmap(Pbitmap, 0, 0, transparent_paint);

						circle.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
					}
					//endregion

					
					if(Arrow!=null){
						Arrow.layout(x, y, x + v.getWidth(), y + v.getHeight());
						Arrow.setRotation(P_rotate);
						TimeLine timefrag1 = (TimeLine) getActivity().getFragmentManager().findFragmentById(R.id.time_line);
						timefrag1.setCircularSeekBarProgress(P_rotate);//為了讓circular seekbar的值也一起變成儲存的狀態，但是因為android好像有bug，所以他不會更新介面上的seekbar的樣子，但值卻是有改過的
						//Log.d("debug", "setCircularSeekBarProgress P1_rotate="+Integer.toString(P1_rotate));
						Arrow.postInvalidate();
					}	
					if(intersect==true && v.getTag().toString().equals("6")==false && v.getTag().toString().equals(Integer.toString(intersect_name))){
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
						switch(intersect_name){
							case 1:
								if(player1.getVisibility()==player1.INVISIBLE){//代表是player1_ball顯示中
									Arrow1.layout((int)player1_ball.getX(), (int)player1_ball.getY(), (int)player1_ball.getX()+player1_ball.getWidth(), (int)player1_ball.getY()+player1_ball.getHeight());
								}
								Arrow1.invalidate();
								v.layout((int) player1.getX()+110-30, (int)player1.getY()+30, (int) player1.getX()+170-30,(int)player1.getY()+90);
								break;
							case 2:
								if(player2.getVisibility()==player2.INVISIBLE){//代表是player2_ball顯示中
									Arrow2.layout((int)player2_ball.getX(), (int)player2_ball.getY(), (int)player2_ball.getX()+player2_ball.getWidth(), (int)player2_ball.getY()+player2_ball.getHeight());
								}
								Arrow2.invalidate();
								v.layout((int) player2.getX()+110-30, (int)player2.getY()+30, (int) player2.getX()+170-30,(int)player2.getY()+90);
								break;
							case 3:
								if(player3.getVisibility()==player3.INVISIBLE){//代表是player3_ball顯示中
									Arrow3.layout((int)player3_ball.getX(), (int)player3_ball.getY(), (int)player3_ball.getX()+player3_ball.getWidth(), (int)player3_ball.getY()+player3_ball.getHeight());
								}
								Arrow3.invalidate();
								v.layout((int) player3.getX()+110-30, (int)player3.getY()+30, (int) player3.getX()+170-30,(int)player3.getY()+90);
								break;
							case 4:
								if(player4.getVisibility()==player4.INVISIBLE){//代表是player4_ball顯示中
									Arrow4.layout((int)player4_ball.getX(), (int)player4_ball.getY(), (int)player4_ball.getX()+player4_ball.getWidth(), (int)player4_ball.getY()+player4_ball.getHeight());
								}
								Arrow4.invalidate();
								v.layout((int) player4.getX()+110-30, (int)player4.getY()+30, (int) player4.getX()+170-30,(int)player4.getY()+90);	
								break;
							case 5:
								if(player5.getVisibility()==player5.INVISIBLE){//代表是player5_ball顯示中
									Arrow5.layout((int)player5_ball.getX(), (int)player5_ball.getY(), (int)player5_ball.getX()+player5_ball.getWidth(), (int)player5_ball.getY()+player5_ball.getHeight());
								}
								Arrow5.invalidate();
								v.layout((int) player5.getX()+110-30, (int)player5.getY()+30, (int) player5.getX()+170-30,(int)player5.getY()+90);
								break;
						
						}
						v.invalidate();
					}
					else if(v.getTag().toString().equals("6") && intersect == false){
						switch(intersect_name){
							case 1:
								if(player1.getVisibility() == player1.VISIBLE){
									Arrow1.layout((int)player1.getX(), (int)player1.getY(), (int)player1.getX()+player1.getWidth(), (int)player1.getY()+player1.getHeight());
								}
								Arrow1.invalidate();
								break;
							case 2:
								if(player2.getVisibility() == player2.VISIBLE){
									Arrow2.layout((int)player2.getX(), (int)player2.getY(), (int)player2.getX()+player2.getWidth(), (int)player2.getY()+player2.getHeight());
								}
								Arrow2.invalidate();
								break;
							case 3:
								if(player3.getVisibility() == player3.VISIBLE){
									Arrow3.layout((int)player3.getX(), (int)player3.getY(), (int)player3.getX()+player3.getWidth(), (int)player3.getY()+player3.getHeight());
								}
								Arrow3.invalidate();
								break;
							case 4:
								if(player4.getVisibility() == player4.VISIBLE){
									Arrow4.layout((int)player4.getX(), (int)player4.getY(), (int)player4.getX()+player4.getWidth(), (int)player4.getY()+player4.getHeight());
								}
								Arrow4.invalidate();
								break;
							case 5:
								if(player5.getVisibility() == player5.VISIBLE){
									Arrow5.layout((int)player5.getX(), (int)player5.getY(), (int)player5.getX()+player5.getWidth(), (int)player5.getY()+player5.getHeight());
								}
								Arrow5.invalidate();
								break;
						}
					}

					//region 防呆，畫的時間太短的話，不會採用
					if (recordcheck == true) {
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
				           	for(int tmp=0;tmp<Bitmap_vector.size();tmp++){
				           			tempCanvas.drawBitmap(Bitmap_vector.get(tmp), 0, 0, null);
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
							Curves.add(P_curve_x);
							Curves.add(P_curve_y);


							///call MainWrap 's function
							MainWrap mainwrapfrag = (MainWrap) getActivity().getFragmentManager().findFragmentById(R.id.MainWrap_frag);
							mainwrapfrag.create_path_num_on_court(RunLine.size()+1, x, y,RunLine.size());

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
							int prev_dir_length = previous_direction.size();
							int sample_length = (prev_dir_length > 5)?5:prev_dir_length;

							for(int i=1 ; i<sample_length ; i++){
								float angle_trans = previous_direction.get(prev_dir_length - i);
								float dri_angle_trans = previous_dribble_direction.get(prev_dir_length - i);
								// 前面設的previous_direction的方向有些是負的
								// 如果不把她轉成正的，vector取到的index就會是負的
								if(previous_direction.get(prev_dir_length - i) < 0.0f){
									angle_trans = 360.0f + angle_trans;
								}
								if(previous_dribble_direction.get(prev_dir_length - i) < 0.0f){
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

							previous_direction.clear();
							previous_dribble_direction.clear();

							////Log,i("debug", "Screen direction : " + screen_direction);
							////Log,i("debug", "Last direction : "+ last_direction);

							screen_direction = max_index*10.0f + 5.0f;

							if(isScreenEnable)
								mainwrapfrag.create_screen_bar(x, y, rotate_which_player,  screen_direction, RunLine.size());
							//endregion

							last_direction = dri_max_index*10.0f + 5.0f;
							boolean isBallHolder = (rotate_which_player == intersect_name);
							float drawn_length = (float)Math.sqrt( Math.pow(x + v.getWidth()/2 - line_start_point_x, 2) + Math.pow(y + v.getHeight()/2 - line_start_point_y, 2));
							////Log,i("debug","Drawn length: "+ drawn_length);
							drawn_length = drawn_length / 150.0f / 2.0f;

							if(isBallHolder)
								mainwrapfrag.create_dribble_line((int)line_start_point_x, (int)line_start_point_y, rotate_which_player, last_direction, drawn_length, RunLine.size());

							Pcanvas.drawCircle((int)line_start_point_x, (int)line_start_point_y, 10, player_paint);

							Bitmap_vector.add(Pbitmap);
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
								tmp.setTimeLineId(RunLine.size());
								
								if(intersect_name_pre == intersect_name){
									tmp.setBall_num(0);
								}
								else{
									tmp.setBall_num(intersect_name);
								}

								//region 20180712 在Runbag中加入掩護及運球的資訊
								if(isBallHolder){
									tmp.setPath_type(2);
									tmp.setDribble_angle(last_direction);
									tmp.setDribble_length(drawn_length);
									tmp.setDribble_start_x((int)line_start_point_x);
									tmp.setDribble_start_y((int)line_start_point_y);
								}
								else if(isScreenEnable){
									tmp.setPath_type(1);
									tmp.setScreen_angle(screen_direction);
								}
								else{
									tmp.setPath_type(0);
								}
								//endregion

								RunLine.add(tmp);
								timefrag.setRunLineId(RunLine.size()-1);
								timefrag.setSeekBarId(RunLine.size()-1);
								
								seekbar_tmp_id++;
								timefrag.setSeekBarProgressLow(MainFrag_SeekBarProgressLow);
								MainFrag_SeekBarProgressLow++;
								timefrag.createSeekbar(seekbar_player_Id);
							}
							P_startIndex = startIndexTmp + 1;
						}
					}
					else{
						if(RunLine.size()==0){
							P_Initial_Position_x=x;
							P_Initial_Position_y=y;
						}
					}
					//endregion
					
					if(v.getTag().toString().equals("1")){
						P1=player;
						c1_idx=c_idx;
						P1_tempcurve_x = P_tempcurve_x;
						P1_tempcurve_y = P_tempcurve_y;
						P1_rotate=P_rotate;
						P1_startIndex=P_startIndex;
						handle_name="P1_Handle";
						P1_Initial_Position_x=P_Initial_Position_x;
						P1_Initial_Position_y=P_Initial_Position_y;
						seekbar_player_Id=1;
						////Log,i("debug", "P_startIndex="+Integer.toString(P_startIndex));
						////Log,i("debug", "P1_startIndex="+Integer.toString(P1_startIndex));
						////Log,i("debug", "P1_RoadSize="+Integer.toString(P1.getRoadSize()));
					}
					else if (v.getTag().toString().equals("2")){
						P2=player;
						c2_idx=c_idx;
						P2_tempcurve_x = P_tempcurve_x;
						P2_tempcurve_y = P_tempcurve_y;
						P2_rotate=P_rotate;
						P2_startIndex=P_startIndex;
						handle_name="P2_Handle";
						P2_Initial_Position_x=P_Initial_Position_x;
						P2_Initial_Position_y=P_Initial_Position_y;
						seekbar_player_Id=2;
						////Log,i("debug", "P_startIndex="+Integer.toString(P_startIndex));
						////Log,i("debug", "P2_startIndex="+Integer.toString(P2_startIndex));
					}
					else if (v.getTag().toString().equals("3")){
						P3=player;
						c3_idx=c_idx;
						P3_tempcurve_x = P_tempcurve_x;
						P3_tempcurve_y = P_tempcurve_y;
						P3_rotate=P_rotate;
						P3_startIndex=P_startIndex;
						handle_name="P3_Handle";
						P3_Initial_Position_x=P_Initial_Position_x;
						P3_Initial_Position_y=P_Initial_Position_y;
						seekbar_player_Id=3;
						////Log,i("debug", "P_startIndex="+Integer.toString(P_startIndex));
						////Log,i("debug", "P3_startIndex="+Integer.toString(P3_startIndex));
					}
					else if (v.getTag().toString().equals("4")){
						P4=player;
						c4_idx=c_idx;
						P4_tempcurve_x = P_tempcurve_x;
						P4_tempcurve_y = P_tempcurve_y;
						P4_rotate=P_rotate;
						P4_startIndex=P_startIndex;
						handle_name="P4_Handle";
						P4_Initial_Position_x=P_Initial_Position_x;
						P4_Initial_Position_y=P_Initial_Position_y;
						seekbar_player_Id=4;
						////Log,i("debug", "P_startIndex="+Integer.toString(P_startIndex));
						////Log,i("debug", "P4_startIndex="+Integer.toString(P4_startIndex));
					}
					else if (v.getTag().toString().equals("5")){
						P5=player;
						c5_idx=c_idx;
						P5_tempcurve_x = P_tempcurve_x;
						P5_tempcurve_y = P_tempcurve_y;
						P5_rotate=P_rotate;
						P5_startIndex=P_startIndex;
						handle_name="P5_Handle";
						P5_Initial_Position_x=P_Initial_Position_x;
						P5_Initial_Position_y=P_Initial_Position_y;
						seekbar_player_Id=5;
						////Log,i("debug", "P_startIndex="+Integer.toString(P_startIndex));
						////Log,i("debug", "P5_startIndex="+Integer.toString(P5_startIndex));
					}
					else if (v.getTag().toString().equals("6")){
						B=player;
						Ball_idx=c_idx;
						Ball_tempcurve_x = P_tempcurve_x;
						Ball_tempcurve_y = P_tempcurve_y;
						B_rotate=P_rotate;
						B_startIndex=P_startIndex;
						handle_name="B_Handle";
						B_Initial_Position_x=P_Initial_Position_x;
						B_Initial_Position_y=P_Initial_Position_y;
						seekbar_player_Id=6;
						////Log,i("debug", "P_startIndex="+Integer.toString(P_startIndex));
						////Log,i("debug", "B_startIndex="+Integer.toString(B_startIndex));
					}
					else if (v.getTag().toString().equals("D1")){
						D1=player;
						cd1_idx=c_idx;
						D1_tempcurve_x = P_tempcurve_x;
						D1_tempcurve_y = P_tempcurve_y;
						D1_rotate=P_rotate;
						D1_startIndex=P_startIndex;
						handle_name="D1_Handle";
						D1_Initial_Position_x=P_Initial_Position_x;
						D1_Initial_Position_y=P_Initial_Position_y;
						seekbar_player_Id=7;
						////Log,i("debug", "P_startIndex="+Integer.toString(P_startIndex));
						////Log,i("debug", "D1_startIndex="+Integer.toString(D1_startIndex));
						////Log,i("debug", "D1_Roadsize="+Integer.toString(D1.getRoadSize()));
					}
					else if (v.getTag().toString().equals("D2")){
						D2=player;
						cd2_idx=c_idx;
						D2_tempcurve_x = P_tempcurve_x;
						D2_tempcurve_y = P_tempcurve_y;
						D2_rotate=P_rotate;
						D2_startIndex=P_startIndex;
						handle_name="D2_Handle";
						D2_Initial_Position_x=P_Initial_Position_x;
						D2_Initial_Position_y=P_Initial_Position_y;
						seekbar_player_Id=8;
						////Log,i("debug", "P_startIndex="+Integer.toString(P_startIndex));
						////Log,i("debug", "D2_startIndex="+Integer.toString(D2_startIndex));
					}
					else if (v.getTag().toString().equals("D3")){
						D3=player;
						cd3_idx=c_idx;
						D3_tempcurve_x = P_tempcurve_x;
						D3_tempcurve_y = P_tempcurve_y;
						D3_rotate=P_rotate;
						D3_startIndex=P_startIndex;
						handle_name="D3_Handle";
						D3_Initial_Position_x=P_Initial_Position_x;
						D3_Initial_Position_y=P_Initial_Position_y;
						seekbar_player_Id=9;
						////Log,i("debug", "P_startIndex="+Integer.toString(P_startIndex));
						////Log,i("debug", "D3_startIndex="+Integer.toString(D3_startIndex));
					}
					else if (v.getTag().toString().equals("D4")){
						D4=player;
						cd4_idx=c_idx;
						D4_tempcurve_x = P_tempcurve_x;
						D4_tempcurve_y = P_tempcurve_y;
						D4_rotate=P_rotate;
						D4_startIndex=P_startIndex;
						handle_name="D4_Handle";
						D4_Initial_Position_x=P_Initial_Position_x;
						D4_Initial_Position_y=P_Initial_Position_y;
						seekbar_player_Id=10;
						////Log,i("debug", "P_startIndex="+Integer.toString(P_startIndex));
						////Log,i("debug", "D4_startIndex="+Integer.toString(D4_startIndex));
					}
					else if (v.getTag().toString().equals("D5")){
						D5=player;
						cd5_idx=c_idx;
						D5_tempcurve_x = P_tempcurve_x;
						D5_tempcurve_y = P_tempcurve_y;
						D5_rotate=P_rotate;
						D5_startIndex=P_startIndex;
						handle_name="D5_Handle";
						D5_Initial_Position_x=P_Initial_Position_x;
						D5_Initial_Position_y=P_Initial_Position_y;
						seekbar_player_Id=11;
						////Log,i("debug", "P_startIndex="+Integer.toString(P_startIndex));
						////Log,i("debug", "D5_startIndex="+Integer.toString(D5_startIndex));
					}
					else{
						////Log,i("debug", "noooooooooo");
					}

					intersect_name_pre = intersect_name;//判斷球有沒有傳給別的player。p.s. --> pre = 上一個拿球的人
					
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

	    
	    public void Mainfrag_remove_one_path(int seekbarId){  
	    	which_to_remove = seekbarId;
	    	/*remove bitmap*/
    		tempBitmap = Bitmap.createBitmap(circle.getWidth(),circle.getHeight(),Bitmap.Config.ARGB_8888);//初始化tempBitmap，指定大小為螢幕大小(大小同circle)
    		tempCanvas = new Canvas();
           	tempCanvas = new Canvas(tempBitmap);//畫透明路徑
           	////Log,i("seekbar", "which_to_remove="+Integer.toString(which_to_remove));
           	for(int tmp=0;tmp<Bitmap_vector.size();tmp++){
           		if(tmp!=which_to_remove){
           			tempCanvas.drawBitmap(Bitmap_vector.get(tmp), 0, 0, null);
           		}
           	}
           	Bitmap_vector.remove(which_to_remove);
    		circle.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));//把tempBitmap放進circle裡
       		
    		/*remove timeline*/
    		TimeLine timefrag1 = (TimeLine) getActivity().getFragmentManager().findFragmentById(R.id.time_line);
    		////Log,i("seekbar", "Remove the RunLine.get"+Integer.toString(which_to_remove)+".getTimeLineId ="+Integer.toString(RunLine.get(which_to_remove).getTimeLineId()));
    		timefrag1.remove_one_timeline(RunLine.get(which_to_remove).getTimeLineId());

    		/*remove pathnum on the court*/
    		MainWrap mainwrap = (MainWrap) getActivity().getFragmentManager().findFragmentById(R.id.MainWrap_frag);
    		mainwrap.remove_textView(which_to_remove);
    		mainwrap.remove_screen_bar(which_to_remove);
    		mainwrap.remove_dribble_line(which_to_remove);

    		/*remove runline*/
    		/*Every component's position in the RunLine should be the same with the TimeLine SeekBarId.*/
    		for(int index=which_to_remove+1;index<RunLine.size();index++){
    			////Log,i("seekbar", "Change seekbar's ID, search ID(index)="+Integer.toString(index));
    			timefrag1.changeSeekBarId(index,index-1);
    			mainwrap.change_textView_id(index, index-1);//change the remained pathnum text and its id
				mainwrap.change_screen_bar_tag(index, index-1);
				mainwrap.change_dribble_line_tag(index, index-1);
    			RunLine.get(index).setTimeLineId(index-1);
    		}
    		RunLine.remove(which_to_remove);
    		timefrag1.setRunLineId(RunLine.size()-1);
    		////Log,i("seekbar", "RunLine.size="+Integer.toString(RunLine.size()));
    		
    		
    		/*remove Curves*/
    		Curves.remove(which_to_remove*2);
    		Curves.remove(which_to_remove*2);
    		

    		Mainfrag_sort_pathnum();
	    }
	    
	    
	    public void Mainfrag_sort_pathnum(){
	    	ArrayList<RunBag> list = new ArrayList<RunBag>();
	    	for(int i = 0 ; i<RunLine.size();i++){
	    		list.add(RunLine.get(i));
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
