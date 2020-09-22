package com.mislab.tacticboard;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
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
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

/*處理球員&球的觸控、儲存路徑，畫出路徑*/

public class MainFragment extends Fragment{
	private int IsTacticPlaying = 0;
	private Long startTime;
	private Long endTime;

	private int selectCategoryId = 13;
	private String selectTacticName = "New_Tactic";
	
	private int totalTime = 15000;// 時間軸的最大值，mySeekBar也要改

	private ImageView removeButton;
	private Vector<ImageView> playersWithBall;

	private boolean firstRecord = true;
	private boolean intersect= false;
	private int intersectName = 0; // 1, 2, 3, 4, 5
	private int intersectNamePre = 0; // 1, 2, 3, 4, 5
	private int initialBallNum = 0; // 1, 2, 3, 4, 5
	private int rotateWhichPlayer; // 1, 2, 3, 4, 5
	//private ImageSelect imageSelect = null;
	private PerspectiveSelect perspectiveSelect = null;

	private PlayerDrawer currentDrawer = new PlayerDrawer(Color.parseColor("#000000"));
	private Vector<PlayerDrawer> playerDrawers;
	private Vector<PlayerDrawer> defenderDrawers;
	private PlayerDrawer ballDrawer;

	private Vector<Player> players;
	private Vector<Player> defenders;
	private Player ball;

	private int seekbarTmpId =0;

	private int whichToRemove =-1;
	private boolean hasQueryDefenderFromServer;
	private boolean hasInvokeCurrentTimeDefender;

    /**畫圖變數**/
	private ImageView circle;// 未知用途XD
	Vector <Bitmap> bitmapVector;
	Bitmap tempBitmap;
	Canvas tempCanvas;

	private Paint transparentPaint;
    /*************************曲線變數************************************/
    private final int N = 3;
	private List<String> currentTimeMaxLen;
	
	Region bitmapRegion;
	
	private Vector<Vector<Float>> curves = new Vector();

    //region 錄製變數
	private boolean recordCheck = false;
	private int seekBarCallbackStartTime;
	private int seekBarCallbackDuration;
	private int seekBarCallbackId;
	private Vector<RunBag> runBags = new Vector();
	private int mainFragSeekBarProgressLow = 0;
	//endregion

    //TODO Socket 變數
	private InetAddress serverAddr;
	private int UDP_SERVER_PORT = 3985;
	DatagramSocket ds = null;
	//AtomicBoolean isRunning=new AtomicBoolean(false);
	/*************************************************************/

	// For screen
	private Vector<Float> previousDirection;
	public boolean isScreenEnable;
	// For dribble
	private Vector<Float> previousDribbleDirection;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_main, container,false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {    
		super.onActivityCreated(savedInstanceState);

		selectCategoryId = 0;
		previousDirection = new Vector<>();
		previousDribbleDirection = new Vector<>();
		isScreenEnable = false;
		hasQueryDefenderFromServer = false;
		//region Initialize Player icons on the view
		Resources resources = getResources();
		players = new Vector<>();
		for(int i=0 ; i<5 ; i++){
			ImageView playerImg = (ImageView)getView().findViewById(resources.getIdentifier("image_p"+(i+1), "id", getActivity().getPackageName()));
			playerImg.setOnTouchListener(playerListener);
			ImageView arrowImg = (ImageView)getView().findViewById(resources.getIdentifier("arrow"+(i+1), "id", getActivity().getPackageName()));
			players.add(new Player(playerImg, arrowImg));
		}
		defenders = new Vector<Player>();
		for(int i=0 ; i<5 ; i++){
			ImageView playerImg = (ImageView)getView().findViewById(resources.getIdentifier("image_d"+(i+1), "id", getActivity().getPackageName()));
			playerImg.setOnTouchListener(playerListener);
			ImageView arrowImg = (ImageView)getView().findViewById(resources.getIdentifier("arrow"+(i+6), "id", getActivity().getPackageName()));
			defenders.add(new Player(playerImg, arrowImg));
		}
		ImageView ballImg = (ImageView)getView().findViewById(resources.getIdentifier("image_ball", "id", getActivity().getPackageName()));
		ballImg.setOnTouchListener(playerListener);
		ball = new Player(ballImg, null);

		playersWithBall = new Vector<>();
		for(int i=0 ; i<5 ; i++){
			ImageView playerImg = (ImageView)getView().findViewById(resources.getIdentifier("image_p"+(i+1)+"withBall", "id", getActivity().getPackageName()));
			playerImg.setOnTouchListener(playerListener);
			playersWithBall.add(playerImg);
		}
		//endregion

		//region Initialize drawing parameters
		playerDrawers = new Vector<>();
		playerDrawers.add(new PlayerDrawer(Color.parseColor("#133C55")));
		playerDrawers.add(new PlayerDrawer(Color.parseColor("#154FB5")));
		playerDrawers.add(new PlayerDrawer(Color.parseColor("#59A5DB")));
		playerDrawers.add(new PlayerDrawer(Color.parseColor("#84D2F6")));
		playerDrawers.add(new PlayerDrawer(Color.parseColor("#DCFFFD")));
		defenderDrawers = new Vector<>();
		defenderDrawers.add(new PlayerDrawer(Color.parseColor("#3c1518")));
		defenderDrawers.add(new PlayerDrawer(Color.parseColor("#69140e")));
		defenderDrawers.add(new PlayerDrawer(Color.parseColor("#a44200")));
		defenderDrawers.add(new PlayerDrawer(Color.parseColor("#d58936")));
		defenderDrawers.add(new PlayerDrawer(Color.parseColor("#FFC82E")));
		ballDrawer = new PlayerDrawer(Color.parseColor("#CC0000"));

		//endregion

		removeButton = (ImageView) getView().findViewById(R.id.rm_button);

		transparentPaint =new Paint();
		transparentPaint.setAntiAlias(true); // 設置畫筆的鋸齒效果。 true是去除。
		transparentPaint.setColor(Color.TRANSPARENT); // 設置透明顏色
		
		bitmapVector = new Vector();
		
		circle=(ImageView) getActivity().findViewById(R.id.circle);

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
		        circle.getViewTreeObserver().removeGlobalOnLayoutListener(this);  
		           
		        rotateWhichPlayer =0;
		        
		        //rm_button.setVisibility(rm_button.VISIBLE);
		        //rm_button.invalidate();
		        removeButton.setVisibility(removeButton.INVISIBLE);
		        removeButton.invalidate();

		        for(int i=0 ; i<5 ; i++)
					playersWithBall.get(i).setVisibility(playersWithBall.get(i).INVISIBLE);
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
	    //For broadcasting the ip to others

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

	public void setUDPIp(InetAddress IP, int port){
		serverAddr = IP;
	    UDP_SERVER_PORT = port;
	}

	/********************************Get corresponding defender from the server******************************/
	public void getDefenderFromServer() throws JSONException, ExecutionException{
		Log.d("debug", "Call get defender from server.");
		JSONObject jsonWrite = new JSONObject();
		// Ball position and five offensive player
		List<List<Float>> position = new ArrayList<List<Float>>();
		for(int i = 0; i < 6; i++)
			position.add(new ArrayList<Float>());
		// Initialize all the position
		Vector<Float> temp = coordinateTransform(ball.initialPosition);
		position.get(0).add(new Float(temp.get(0)));
		position.get(0).add(new Float(temp.get(1)));
		for(int i=1 ; i<6 ; i++){
			temp = coordinateTransform(players.get(i-1).initialPosition);
			position.get(i).add(new Float(temp.get(0)));
			position.get(i).add(new Float(temp.get(1)));
		}

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
						Point tempPos = new Point();
						if(id == 0)
							tempPos = new Point(ball.handleGetRoad(j), ball.handleGetRoad(j+1));
						else
							tempPos = new Point(players.get(id-1).handleGetRoad(j), players.get(id-1).handleGetRoad(j+1));
						Vector<Float> transPos = coordinateTransform(tempPos);
						position.get(id).add(transPos.get(0));
						position.get(id).add(transPos.get(1));
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
			for(int i=0;i<5;i++){
				positions.add(new ArrayList<Integer>());
				defenders.get(i).clearAll();
			}

			JSONObject jsonDefender = new JSONObject(new String(query));
			int timeStep = jsonDefender.getJSONArray("1").length();
			for(int i=0;i<5;i++){
				for(int t=0;t<timeStep;t=t+2){
					float tempX = Float.parseFloat(jsonDefender.getJSONArray(String.valueOf(i+1)).get(t).toString());
					float tempY = Float.parseFloat(jsonDefender.getJSONArray(String.valueOf(i+1)).get(t+1).toString());
					//Log.d("warning", tempX + ", " + tempY);
					Vector<Float> tempPos = reverseCoordinateTransform(new Point((int)tempX, (int)tempY));
					positions.get(i).add(Math.round(tempPos.get(0)));
					positions.get(i).add(Math.round(tempPos.get(1)));
					defenders.get(i).setRoad(Math.round(tempPos.get(0)));
					defenders.get(i).setRoad(Math.round(tempPos.get(1)));
				}
			}
			// Setting the initial position of the defenders
			for(int i=0 ; i<5 ; i++){
				defenders.get(i).initialPosition.x = positions.get(i).get(0);
				defenders.get(i).initialPosition.y = positions.get(i).get(1);
			}
			hasQueryDefenderFromServer = true;

			Log.d("warning", String.valueOf(runBags.size()));

		} catch (JSONException e) {
			e.printStackTrace();
		}
		movePlayersToInitialPosition();
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

	private Vector<Float> coordinateTransform(Point origin){
		int LEFT_TOP_X = 0;
		int LEFT_TOP_Y = 0;
		int RIGHT_TOP_X = 94;
		int RIGHT_TOP_Y = 0;
		int CENTER_X = 47;
		int CENTER_Y = 25;

		float newX = (origin.y - 760.0f) * (CENTER_X - LEFT_TOP_X)/760.0f + CENTER_X;
		float newY = (origin.x - 540.0f) * (25.0f) / 400.0f + CENTER_Y;

		newX = CENTER_X + (CENTER_X - newX);
		newY = newY;
		Vector<Float> newPos = new Vector<Float>();
		newPos.add(newX);
		newPos.add(newY);
		return newPos;
	}

	private Vector<Float> reverseCoordinateTransform(Point origin){
		int LEFT_TOP_X = 0;
		int LEFT_TOP_Y = 0;
		int RIGHT_TOP_X = 94;
		int RIGHT_TOP_Y = 0;
		int CENTER_X = 47;
		int CENTER_Y = 25;

		float newX = 540.0f + (origin.y - CENTER_Y) * 400.0f / 25.0f;
		float newY = CENTER_X - (origin.x - CENTER_X);
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
	public void sendTacticToVR(){
/*
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
*/
	}

	//endregion

	public void changePlayerToNoBall(){
		if(intersectName !=0){
			ImageView currentImage = players.get(intersectName-1).image;
			ImageView currentArrow = players.get(intersectName-1).arrow;
			currentImage.setVisibility(currentImage.VISIBLE);
			if(intersect) currentImage.layout((int) playersWithBall.get(intersectName-1).getX()+30, (int) playersWithBall.get(intersectName-1).getY(),(int) playersWithBall.get(intersectName-1).getX()+30+currentImage.getWidth(), (int) playersWithBall.get(intersectName-1).getY()+currentImage.getHeight());
			currentImage.invalidate();
			playersWithBall.get(intersectName-1).setVisibility(playersWithBall.get(intersectName-1).INVISIBLE);
			playersWithBall.get(intersectName-1).invalidate();
			currentArrow.layout((int)currentImage.getX(), (int)currentImage.getY(), (int)currentImage.getX()+currentImage.getWidth(), (int)currentImage.getY()+currentImage.getHeight());
			currentArrow.invalidate();
		}
	}
	
	public void setIsTacticPlaying(int isExecuting){
		IsTacticPlaying = isExecuting;
	}

	public void setStartTime(int time) {
		seekBarCallbackStartTime = time;
	}

	public void setDuration(int duration) {
		seekBarCallbackDuration = duration;
	}

	public void setSeekBarCallBackId(int id){
		seekBarCallbackId = id;
	}
	public void setRecordCheck(boolean input) {
        /*停止錄製按下之後，畫線的curve就清除重算*/
		if(recordCheck == true && input == false){
			for(int i=0;i<5;i++){
				playerDrawers.get(i).clearCurve();
				defenderDrawers.get(i).clearCurve();
			}
			ballDrawer.clearCurve();
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

	public void clearPaint(){//清除筆跡
		bitmapVector.clear();
		tempBitmap = Bitmap.createBitmap(circle.getWidth(),circle.getHeight(),Bitmap.Config.ARGB_8888);//初始化tempBitmap，指定大小為螢幕大小(大小同circle)
		tempCanvas = new Canvas();
       	tempCanvas = new Canvas(tempBitmap);//畫透明路徑
   		tempCanvas.drawBitmap(tempBitmap, 0, 0, null);
   		tempCanvas.drawCircle(1, 1, 5, transparentPaint);//畫透明路徑
   		circle.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));//把tempBitmap放進circle裡
		for(int i=0;i<5;i++){
			playerDrawers.get(i).clearCurve();
			defenderDrawers.get(i).clearCurve();
		}
		ballDrawer.clearCurve();
		curves.clear();
        //tempCanvas.drawBitmap(Bitmap_vector.get(0), 0, 0, null);
        //circle.setImageDrawable(new BitmapDrawable(getResources(), Bitmap_vector.get(1)));//把tempBitmap放進circle裡
	}
	
	public void clearRecord(){
		initialBallNum =0;
		firstRecord =true;
		IsTacticPlaying =0;
		for(int i=0;i<5;i++){
			players.get(i).clearAll();
			defenders.get(i).clearAll();
		}
		ball.clearAll();
		runBags.clear();
		for(int i=0;i<5;i++){
			playerDrawers.get(i).clearRecord();
			defenderDrawers.get(i).clearRecord();
		}
		ballDrawer.clearRecord();
		seekbarTmpId =0;
		mainFragSeekBarProgressLow =0;
		
		clearPaint();
		TimeLine timefrag = (TimeLine) getActivity().getFragmentManager().findFragmentById(R.id.time_line);
		timefrag.clear_record_layout();
		
		MainWrap mainwrap = (MainWrap) getActivity().getFragmentManager().findFragmentById(R.id.MainWrap_frag);
		mainwrap.clearRecordLayout();
	}
	
	public int lagrange(Vector<Point> curve, float x){
		int y=0;
		float tmpy=0;
	    for (int i=0; i<curve.size(); ++i)
	    {
	        float a = 1, b = 1;
	        for (int j=0; j<curve.size(); ++j)
	        {
	            if (j == i) continue;
	            a *= x - curve.get(j).x;
	            b *= curve.get(i).x - curve.get(j).x;
	        }
	        tmpy=(curve.get(i).y * a / b);
	        y += curve.get(i).y * a / b;
	    }	 
		return y;
	}
	
	public void movePlayersToInitialPosition(){
        /* 先把全部player移到按下錄製鍵時的位置 */
		for(int i=0 ; i<5 ; i++){
			if(players.get(i).initialPosition.x != -1){
				// Offenders
				Player thisPlayer = players.get(i);
				thisPlayer.image.layout(thisPlayer.initialPosition.x, thisPlayer.initialPosition.y, thisPlayer.initialPosition.x + thisPlayer.image.getWidth(), thisPlayer.initialPosition.y + thisPlayer.image.getHeight());
				thisPlayer.arrow.layout(thisPlayer.initialPosition.x, thisPlayer.initialPosition.y, thisPlayer.initialPosition.x + thisPlayer.arrow.getWidth(), thisPlayer.initialPosition.y + thisPlayer.arrow.getHeight());
				thisPlayer.rect = new Rect(thisPlayer.initialPosition.x, thisPlayer.initialPosition.y, thisPlayer.initialPosition.x + thisPlayer.image.getWidth(), thisPlayer.initialPosition.y + thisPlayer.image.getHeight());
				thisPlayer.image.invalidate();
				thisPlayer.arrow.setRotation(thisPlayer.initialRotation);
				thisPlayer.arrow.invalidate();
			}
			if(defenders.get(i).initialPosition.x != -1) {
				Player thisPlayer = defenders.get(i);
				thisPlayer.image.layout(thisPlayer.initialPosition.x, thisPlayer.initialPosition.y, thisPlayer.initialPosition.x + thisPlayer.image.getWidth(), thisPlayer.initialPosition.y + thisPlayer.image.getHeight());
				thisPlayer.arrow.layout(thisPlayer.initialPosition.x, thisPlayer.initialPosition.y, thisPlayer.initialPosition.x + thisPlayer.arrow.getWidth(), thisPlayer.initialPosition.y + thisPlayer.arrow.getHeight());
				thisPlayer.image.invalidate();
				thisPlayer.arrow.setRotation(thisPlayer.initialRotation);
				thisPlayer.arrow.invalidate();
			}
		}
		if(ball.initialPosition.x != -1){
			ball.image.layout(ball.initialPosition.x, ball.initialPosition.y, ball.initialPosition.x + ball.image.getWidth(), ball.initialPosition.y + ball.image.getHeight());
			ball.rect = new Rect(ball.initialPosition.x, ball.initialPosition.y, ball.initialPosition.x + ball.image.getWidth(), ball.initialPosition.y + ball.image.getHeight());
		}
	}

	/**************************************************************************/
    //TODO 儲存、載入戰術(按鈕的判斷在ButtonDraw.java裡面)
	/**************************************************************************/
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void manageTacticDialog(){
	    LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View myv = inflater.inflate(R.layout.dialog_manager_tactic, null);//把xml當成view來用，就能加入alert dialog裡面了

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
                    saveDialog();

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
                    loadDialog();

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btnTacticLoad.setBackgroundResource(R.drawable.btn_load);
                }
                return true;
            }
        });

    }

	public void saveDialog(){
    	
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		final View myv = inflater.inflate(R.layout.dialog_save_strategy, null);//把xml當成view來用，就能加入alert dialog裡面了
		
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

                        //region 2018.07.12 Version.2 以json格式來存檔
						JSONObject save_strategy = new JSONObject();
						try {
							JSONArray tmp_arr = new JSONArray();
							//region Initial_Position
							for(int i=0;i<5;i++){
								tmp_arr.put(players.get(i).initialPosition.x);
								tmp_arr.put(players.get(i).initialPosition.y);
							}

							for(int i=0;i<5;i++){
								tmp_arr.put(defenders.get(i).initialPosition.x);
								tmp_arr.put(defenders.get(i).initialPosition.y);
							}

							tmp_arr.put(ball.initialPosition.x);
							tmp_arr.put(ball.initialPosition.y);

							save_strategy.put("Initial_Position", tmp_arr);
							//endregion
							//region Initial_Rotation
							tmp_arr = new JSONArray();
							for(int i=0;i<5;i++)
								tmp_arr.put(players.get(i).initialRotation);
							for(int i=0;i<5;i++)
								tmp_arr.put(defenders.get(i).initialRotation);

							save_strategy.put("Initial_Rotation", tmp_arr);
							//endregion

							save_strategy.put("Initial_ball_holder", initialBallNum);
							save_strategy.put("Tactic_name", saveName);
							save_strategy.put("Category_id", selectCategoryId);

							//region Player road sequence
							tmp_arr = new JSONArray();
							for(int i=0;i<ball.getRoadSize();i++){
								tmp_arr.put( String.valueOf(ball.handleGetRoad(i) ));
							}
							if(tmp_arr.length() > 0)
								save_strategy.put("B", tmp_arr);
							for(int id=0;id<5;id++){
								tmp_arr = new JSONArray();
								for(int i=0;i<players.get(id).getRoadSize();i++)
									tmp_arr.put( String.valueOf(players.get(id).handleGetRoad(i)));
								if(tmp_arr.length() > 0)
									save_strategy.put("P"+(id+1), tmp_arr);
								tmp_arr = new JSONArray();
								for(int i=0;i<defenders.get(id).getRoadSize();i++)
									tmp_arr.put( String.valueOf(defenders.get(id).handleGetRoad(i)));
								if(tmp_arr.length() > 0)
									save_strategy.put("D"+(id+1), tmp_arr);
								tmp_arr = new JSONArray();
								for(int i=0;i<players.get(id).getRotationSize();i++)
									tmp_arr.put( String.valueOf(players.get(id).getMyRotation(i)));
								if(tmp_arr.length() > 0)
									save_strategy.put("P"+(id+1)+"_Rotation", tmp_arr);
								tmp_arr = new JSONArray();
								for(int i=0;i<defenders.get(id).getRotationSize();i++)
									tmp_arr.put( String.valueOf(defenders.get(id).getMyRotation(i)));
								if(tmp_arr.length() > 0)
									save_strategy.put("D"+(id+1)+"_Rotation", tmp_arr);
							}
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
    
	public void loadDialog(){
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View myv = inflater.inflate(R.layout.dialog_load_strategy, null);//把xml當成view來用，就能加入alert dialog裡面了

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(myv);
        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setLayout(1000,1200);

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
				clearRecord();

				File dir = getActivity().getBaseContext().getExternalFilesDir(null);
				String current_select_tactic = tactic_in_category.get(selectCategoryId).get(new Long(l).intValue()) + "_" + String.valueOf(selectCategoryId)+".json";
				selectTacticName = tactic_in_category.get(selectCategoryId).get(new Long(l).intValue());
				File inFile = new File(dir, current_select_tactic);
				readStrategy(inFile);
				movePlayersToInitialPosition();
				//TODO:應該要改成可以知道現在是使用哪種defender資料
				if(defenders.get(0).initialPosition.x == -1)
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
			String jsonString = null;
			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

			jsonString = Charset.defaultCharset().decode(bb).toString();

			JSONObject saveTacticJson = new JSONObject(jsonString);
			initialBallNum = Integer.valueOf(saveTacticJson.getString("Initial_ball_holder"));

			//region Load initial Position
			JSONArray initialArray = saveTacticJson.getJSONArray("Initial_Position");
			for(int i=0;i<5;i++){
				players.get(i).initialPosition = new Point(Integer.valueOf(initialArray.getString(i*2)), Integer.valueOf(initialArray.getString(i*2+1)));
				defenders.get(i).initialPosition = new Point(Integer.valueOf(initialArray.getString(i*2+10)), Integer.valueOf(initialArray.getString(i*2+10+1)));
			}
			ball.initialPosition = new Point(Integer.valueOf(initialArray.getString(20)), Integer.valueOf(initialArray.getString(21)));
			//endregion

			//region Load Initial Rotation
			JSONArray initialRotationArray = saveTacticJson.getJSONArray("Initial_Rotation");
			for(int i=0;i<5;i++) {
				players.get(i).initialRotation = Integer.valueOf(initialRotationArray.getString(i));
				defenders.get(i).initialRotation = Integer.valueOf(initialRotationArray.getString(i+5));
			}
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
			String jsonString = null;
			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

			jsonString = Charset.defaultCharset().decode(bb).toString();

			JSONObject saveTacticJson = new JSONObject(jsonString);
			for(int i=0;i<5;i++){
				if(saveTacticJson.has("P"+(i+1))){
					JSONArray tmpArray = saveTacticJson.getJSONArray("P"+(i+1));
					if(tmpArray != null) {
						for (int j = 0; j < tmpArray.length(); j++)
							players.get(i).setRoad(Integer.valueOf(tmpArray.getString(j)));
					}
				}
				if(saveTacticJson.has("D"+(i+1))){
					JSONArray tmpArray = saveTacticJson.getJSONArray("D"+(i+1));
					if(tmpArray != null) {
						for (int j = 0; j < tmpArray.length(); j++)
							players.get(i).setRoad(Integer.valueOf(tmpArray.getString(j)));
					}
				}
				if(saveTacticJson.has("P"+(i+1)+"_Rotation")){
					JSONArray tmpArray = saveTacticJson.getJSONArray("P"+(i+1)+"_Rotation");
					if(tmpArray != null) {
						for (int j = 0; j < tmpArray.length(); j++)
							players.get(i).setMyRotation(Integer.valueOf(tmpArray.getString(j)));
					}
				}
				if(saveTacticJson.has("D"+(i+1)+"_Rotation")){
					JSONArray tmpArray = saveTacticJson.getJSONArray("D"+(i+1)+"_Rotation");
					if(tmpArray != null) {
						for (int j = 0; j < tmpArray.length(); j++)
							defenders.get(i).setMyRotation(Integer.valueOf(tmpArray.getString(j)));
					}
				}
			}
			if(saveTacticJson.has("B")){
				JSONArray tmpArray = saveTacticJson.getJSONArray("B");
				if(tmpArray != null) {
					for (int j = 0; j < tmpArray.length(); j++)
						ball.setRoad(Integer.valueOf(tmpArray.getString(j)));
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

			String jsonString = null;
			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

			jsonString = Charset.defaultCharset().decode(bb).toString();

			JSONObject saveTacticJson = new JSONObject(jsonString);
			JSONArray runLine = saveTacticJson.getJSONArray("Runline");
			TimeLine timefrag = (TimeLine) getActivity().getFragmentManager().findFragmentById(R.id.time_line);
			int loadSeekbarTmpId=0;
			int x=0, y=0;
			for(int i=0;i<runLine.length();i++){
				JSONObject tmpRunBag = runLine.getJSONObject(i);
				RunBag tmp = new RunBag();
				tmp.setStartTime(Integer.valueOf(tmpRunBag.getString("start_time")));
				tmp.setHandler(tmpRunBag.getString("handler"));
				tmp.setRoadStart(Integer.valueOf(tmpRunBag.getString("road_start")));
				tmp.setRoadEnd(Integer.valueOf(tmpRunBag.getString("road_end")));
				tmp.setDuration(Integer.valueOf(tmpRunBag.getString("duration")));
				tmp.setBallNum(Integer.valueOf(tmpRunBag.getString("ball_num")));
				tmp.setPathType(Integer.valueOf(tmpRunBag.getString("path_type")));
				tmp.setScreenAngle(Float.valueOf(tmpRunBag.getString("screen_angle")));
				tmp.setDribbleAngle(Float.valueOf(tmpRunBag.getString("dribble_angle")));
				tmp.setDribbleLength(Float.valueOf(tmpRunBag.getString("dribble_length")));

				tmp.setDribbleStartX(Integer.valueOf(tmpRunBag.getString("dribble_start_x")));
				tmp.setDribbleStartY(Integer.valueOf(tmpRunBag.getString("dribble_start_y")));

				runBags.add(tmp);

				//region 對應每讀一個Runbag，就會自動產生對應他順序的seekbar(用來調整時間點的bar條)

				timefrag.setSeekBarId(loadSeekbarTmpId);
				if(tmp.getHandler().equals("P1_Handle")){
					timefrag.createSeekbar(1,loadSeekbarTmpId,tmp.getStartTime(),tmp.getDuration());
					MainWrap mainwrapfrag = (MainWrap) getActivity().getFragmentManager().findFragmentById(R.id.MainWrap_frag);

					// 在player icon所在的位置產生對應的順序標記
					x = players.get(0).getCmpltRoad().get(tmp.getRoadEnd()-1);
					y = players.get(0).getCmpltRoad().get(tmp.getRoadEnd());
					mainwrapfrag.createPathNumOnCourt(loadSeekbarTmpId+1, x, y,loadSeekbarTmpId);

					// 如果這一動有要擋拆
					if(tmp.getPathType() == 1){
						mainwrapfrag.createScreenBar(x, y, 1, tmp.getScreenAngle(), loadSeekbarTmpId);
					}else if(tmp.getPathType() == 2){
						mainwrapfrag.createDribbleLine(tmp.getDribbleStartX(), tmp.getDribbleStartY(), 1, tmp.getDribbleAngle(), tmp.getDribbleLength(), loadSeekbarTmpId);
					}
				}
				else if(tmp.getHandler().equals("P2_Handle")){
					timefrag.createSeekbar(2,loadSeekbarTmpId,tmp.getStartTime(),tmp.getDuration());
					MainWrap mainwrapfrag = (MainWrap) getActivity().getFragmentManager().findFragmentById(R.id.MainWrap_frag);
					x=players.get(1).getCmpltRoad().get(tmp.getRoadEnd()-1);
					y=players.get(1).getCmpltRoad().get(tmp.getRoadEnd());
					mainwrapfrag.createPathNumOnCourt(loadSeekbarTmpId+1, x, y,loadSeekbarTmpId);

					// 如果這一動有要擋拆
					if(tmp.getPathType() == 1){
						mainwrapfrag.createScreenBar(x, y, 2, tmp.getScreenAngle(), loadSeekbarTmpId);
					}else if(tmp.getPathType() == 2){
						mainwrapfrag.createDribbleLine(tmp.getDribbleStartX(), tmp.getDribbleStartY(), 2, tmp.getDribbleAngle(), tmp.getDribbleLength(), loadSeekbarTmpId);
					}
				}
				else if(tmp.getHandler().equals("P3_Handle")){
					timefrag.createSeekbar(3,loadSeekbarTmpId,tmp.getStartTime(),tmp.getDuration());
					MainWrap mainwrapfrag = (MainWrap) getActivity().getFragmentManager().findFragmentById(R.id.MainWrap_frag);
					x=players.get(2).getCmpltRoad().get(tmp.getRoadEnd()-1);
					y=players.get(2).getCmpltRoad().get(tmp.getRoadEnd());
					mainwrapfrag.createPathNumOnCourt(loadSeekbarTmpId+1, x, y,loadSeekbarTmpId);

					// 如果這一動有要擋拆
					if(tmp.getPathType() == 1){
						mainwrapfrag.createScreenBar(x, y, 3, tmp.getScreenAngle(), loadSeekbarTmpId);
					}
					else if(tmp.getPathType() == 2){
						mainwrapfrag.createDribbleLine(tmp.getDribbleStartX(), tmp.getDribbleStartY(), 2, tmp.getDribbleAngle(), tmp.getDribbleLength(), loadSeekbarTmpId);
					}
				}
				else if(tmp.getHandler().equals("P4_Handle")){
					timefrag.createSeekbar(4,loadSeekbarTmpId,tmp.getStartTime(),tmp.getDuration());
					MainWrap mainwrapfrag = (MainWrap) getActivity().getFragmentManager().findFragmentById(R.id.MainWrap_frag);
					x=players.get(3).getCmpltRoad().get(tmp.getRoadEnd()-1);
					y=players.get(3).getCmpltRoad().get(tmp.getRoadEnd());
					mainwrapfrag.createPathNumOnCourt(loadSeekbarTmpId+1, x, y,loadSeekbarTmpId);

					// 如果這一動有要擋拆
					if(tmp.getPathType() == 1){
						mainwrapfrag.createScreenBar(x, y, 3, tmp.getScreenAngle(), loadSeekbarTmpId);
					}else if(tmp.getPathType() == 2){
						mainwrapfrag.createDribbleLine(tmp.getDribbleStartX(), tmp.getDribbleStartY(), 3, tmp.getDribbleAngle(), tmp.getDribbleLength(), loadSeekbarTmpId);
					}
				}
				else if(tmp.getHandler().equals("P5_Handle")){
					timefrag.createSeekbar(5,loadSeekbarTmpId,tmp.getStartTime(),tmp.getDuration());
					MainWrap mainwrapfrag = (MainWrap) getActivity().getFragmentManager().findFragmentById(R.id.MainWrap_frag);
					x=players.get(4).getCmpltRoad().get(tmp.getRoadEnd()-1);
					y=players.get(4).getCmpltRoad().get(tmp.getRoadEnd());
					mainwrapfrag.createPathNumOnCourt(loadSeekbarTmpId+1, x, y,loadSeekbarTmpId);

					// 如果這一動有要擋拆
					if(tmp.getPathType() == 1){
						mainwrapfrag.createScreenBar(x, y, 4, tmp.getScreenAngle(), loadSeekbarTmpId);
					}
					else if(tmp.getPathType() == 2){
						mainwrapfrag.createDribbleLine(tmp.getDribbleStartX(), tmp.getDribbleStartY(), 4, tmp.getDribbleAngle(), tmp.getDribbleLength(), loadSeekbarTmpId);
					}
				}
				else if(tmp.getHandler().equals("B_Handle")){
					timefrag.createSeekbar(6,loadSeekbarTmpId,tmp.getStartTime(),tmp.getDuration());
					MainWrap mainwrapfrag = (MainWrap) getActivity().getFragmentManager().findFragmentById(R.id.MainWrap_frag);
					x=ball.getCmpltRoad().get(tmp.getRoadEnd()-1);
					y=ball.getCmpltRoad().get(tmp.getRoadEnd());
					mainwrapfrag.createPathNumOnCourt(loadSeekbarTmpId+1, x, y,loadSeekbarTmpId);
				}
				else if(tmp.getHandler().equals("D1_Handle")){
					timefrag.createSeekbar(7,loadSeekbarTmpId,tmp.getStartTime(),tmp.getDuration());
					MainWrap mainwrapfrag = (MainWrap) getActivity().getFragmentManager().findFragmentById(R.id.MainWrap_frag);
					x=defenders.get(0).getCmpltRoad().get(tmp.getRoadEnd()-1);
					y=defenders.get(0).getCmpltRoad().get(tmp.getRoadEnd());
					mainwrapfrag.createPathNumOnCourt(loadSeekbarTmpId+1, x, y,loadSeekbarTmpId);
				}
				else if(tmp.getHandler().equals("D2_Handle")){
					timefrag.createSeekbar(8,loadSeekbarTmpId,tmp.getStartTime(),tmp.getDuration());
					MainWrap mainwrapfrag = (MainWrap) getActivity().getFragmentManager().findFragmentById(R.id.MainWrap_frag);
					x=defenders.get(1).getCmpltRoad().get(tmp.getRoadEnd()-1);
					y=defenders.get(1).getCmpltRoad().get(tmp.getRoadEnd());
					mainwrapfrag.createPathNumOnCourt(loadSeekbarTmpId+1, x, y,loadSeekbarTmpId);
				}
				else if(tmp.getHandler().equals("D3_Handle")){
					timefrag.createSeekbar(9,loadSeekbarTmpId,tmp.getStartTime(),tmp.getDuration());
					MainWrap mainwrapfrag = (MainWrap) getActivity().getFragmentManager().findFragmentById(R.id.MainWrap_frag);
					x=defenders.get(2).getCmpltRoad().get(tmp.getRoadEnd()-1);
					y=defenders.get(2).getCmpltRoad().get(tmp.getRoadEnd());
					mainwrapfrag.createPathNumOnCourt(loadSeekbarTmpId+1, x, y,loadSeekbarTmpId);
				}
				else if(tmp.getHandler().equals("D4_Handle")){
					timefrag.createSeekbar(10,loadSeekbarTmpId,tmp.getStartTime(),tmp.getDuration());
					MainWrap mainwrapfrag = (MainWrap) getActivity().getFragmentManager().findFragmentById(R.id.MainWrap_frag);
					x=defenders.get(3).getCmpltRoad().get(tmp.getRoadEnd()-1);
					y=defenders.get(3).getCmpltRoad().get(tmp.getRoadEnd());
					mainwrapfrag.createPathNumOnCourt(loadSeekbarTmpId+1, x, y,loadSeekbarTmpId);
				}
				else if(tmp.getHandler().equals("D5_Handle")){
					timefrag.createSeekbar(11,loadSeekbarTmpId,tmp.getStartTime(),tmp.getDuration());
					MainWrap mainwrapfrag = (MainWrap) getActivity().getFragmentManager().findFragmentById(R.id.MainWrap_frag);
					x=defenders.get(4).getCmpltRoad().get(tmp.getRoadEnd()-1);
					y=defenders.get(4).getCmpltRoad().get(tmp.getRoadEnd());
					mainwrapfrag.createPathNumOnCourt(loadSeekbarTmpId+1, x, y,loadSeekbarTmpId);
				}
				//endregion
				loadSeekbarTmpId++;
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

		movePlayersToInitialPosition();
	}
	/**************************************************************************/
	
	/**************************************************************************/
	public void rotatePlayer(int input){
		if(rotateWhichPlayer == 6){
			ballDrawer.rotation = 0;
		}
		else if(rotateWhichPlayer < 6){
			int id = rotateWhichPlayer-1;
			playerDrawers.get(id).rotation = input;
			currentDrawer.rotation =input;
			players.get(id).arrow.setRotation(input);
			if(recordCheck ==false && runBags.size()==0)
				players.get(id).initialRotation = input;
		}
		else if(rotateWhichPlayer > 6){
			int id = rotateWhichPlayer-7;
			defenderDrawers.get(id).rotation = input;
			currentDrawer.rotation =input;
			defenders.get(id).arrow.setRotation(input);
			if(recordCheck ==false && runBags.size()==0)
				defenders.get(4).initialRotation = input;
		}
	}
	/**************************************************************************/

	public void playButton() {  
		if (runBags.isEmpty()) {
			//Log.e("empty!", String.valueOf(RunLine.size()));
		} else {
            /*先把全部player移到按下錄製鍵時的位置*/
			movePlayersToInitialPosition();
			//Player_change_to_no_ball();
			/****************************/
			
			//////////////////////////////////////// Time
			//////////////////////////////////////// counter///////////////////////////////////////
			new Thread(new Runnable() {// Time counter count on per second 
				@Override
				public void run() { 
					int time = 0;
					int RunLineSize = runBags.size();
					while (time < totalTime && IsTacticPlaying ==1) {
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
				while (i < RunLineSize && IsTacticPlaying ==1) {
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

	protected void play(final int id, final boolean isDribble, final int speed, final Handler play_handler, final int roadStartIndex, final int roadEndIndex) {
		//Log,i("debug","initial_ball_num="+Integer.toString(initial_ball_num));
		new Thread(new Runnable() {  
			@Override
			public void run() {  
				int playK = roadStartIndex;
				while (playK < roadEndIndex - 1 && IsTacticPlaying ==1) {
					try {
						Message m = new Message();
						Bundle b = new Bundle();
						// Bundle可以根據 ("keyName",key_value)的方式，將資料打包，要使用的時候，就可以用"keyName"去取得key_value
						b.putInt("who", id);
						b.putInt("what", playK);
						// 將play_k打包，要取得play_k的值的時候，要用"what"去取得。
						b.putBoolean("isDribble", isDribble);
						m.setData(b);
						// 將Bundle放進Message
						play_handler.sendMessage(m);
						// 把Message傳給handler去處理，handler收到message後，就會執行handleMessage的內容。
						Thread.sleep(speed);
						playK = playK + 2;
					} catch (Throwable t) {

					}
				}
			}
		}).start();
	}

	//region Message Handlers
	Handler offenderHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			int id = msg.getData().getInt("who");
			int sentInt = msg.getData().getInt("what");
			players.get(id).image.layout(players.get(id).handleGetRoad(sentInt), players.get(id).handleGetRoad(sentInt + 1),
					players.get(id).handleGetRoad(sentInt) + players.get(id).image.getWidth(),
					players.get(id).handleGetRoad(sentInt + 1) + players.get(id).image.getHeight());
			//region 因為這一動是運球，所以讓球跟著Player的位置
			boolean isDribble = msg.getData().getBoolean("isDribble");
			if(isDribble)
				ball.image.layout(players.get(id).handleGetRoad(sentInt), players.get(id).handleGetRoad(sentInt + 1),
						players.get(id).handleGetRoad(sentInt) + ball.image.getWidth(),
						players.get(id).handleGetRoad(sentInt + 1) + ball.image.getHeight() );
			//endregion

			players.get(id).arrow.layout(players.get(id).handleGetRoad(sentInt), players.get(id).handleGetRoad(sentInt + 1),
					players.get(id).handleGetRoad(sentInt) + players.get(id).arrow.getWidth(),
					players.get(id).handleGetRoad(sentInt + 1) + players.get(id).arrow.getHeight());

			if(sentInt!=1)
				players.get(id).arrow.setRotation(players.get(id).getMyRotation(sentInt/2));
			else{
				players.get(id).arrow.setRotation(players.get(id).getMyRotation(sentInt));
			}
		}
	};

	Handler defenderHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int id = msg.getData().getInt("who")-6;
			int sentInt = msg.getData().getInt("what");
			defenders.get(id).image.layout(defenders.get(id).handleGetRoad(sentInt), defenders.get(id).handleGetRoad(sentInt + 1),
					defenders.get(id).handleGetRoad(sentInt) + defenders.get(id).image.getWidth(),
					defenders.get(id).handleGetRoad(sentInt + 1) + defenders.get(id).image.getHeight());

			defenders.get(id).arrow.layout(defenders.get(id).handleGetRoad(sentInt), defenders.get(id).handleGetRoad(sentInt + 1),
					defenders.get(id).handleGetRoad(sentInt) + defenders.get(id).arrow.getWidth(),
					defenders.get(id).handleGetRoad(sentInt + 1) + defenders.get(id).arrow.getHeight());

		}
	};

	Handler B_Handle = new Handler() {
		@Override
		public void handleMessage(Message msg) {
		int sentInt = msg.getData().getInt("what");
		ball.image.layout(ball.handleGetRoad(sentInt), ball.handleGetRoad(sentInt + 1),
				ball.handleGetRoad(sentInt) + ball.image.getWidth(), ball.handleGetRoad(sentInt + 1) + ball.image.getHeight());
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
					play(0, isDribble, runBags.get(sentI).getRate(), offenderHandler, runBags.get(sentI).getRoadStart(),
							runBags.get(sentI).getRoadEnd());
				} else if (runBags.get(sentI).getHandler().equals("P2_Handle")) {
					play(1, isDribble, runBags.get(sentI).getRate(), offenderHandler, runBags.get(sentI).getRoadStart(),
							runBags.get(sentI).getRoadEnd());
				} else if (runBags.get(sentI).getHandler().equals("P3_Handle")) {
					play(2, isDribble, runBags.get(sentI).getRate(), offenderHandler, runBags.get(sentI).getRoadStart(),
							runBags.get(sentI).getRoadEnd());
				} else if (runBags.get(sentI).getHandler().equals("P4_Handle")) {
					play(3, isDribble, runBags.get(sentI).getRate(), offenderHandler, runBags.get(sentI).getRoadStart(),
							runBags.get(sentI).getRoadEnd());
				} else if (runBags.get(sentI).getHandler().equals("P5_Handle")) {
					play(4, isDribble, runBags.get(sentI).getRate(), offenderHandler, runBags.get(sentI).getRoadStart(),
							runBags.get(sentI).getRoadEnd());
				} else if (runBags.get(sentI).getHandler().equals("B_Handle")) {
					play(5, isDribble, runBags.get(sentI).getRate(), B_Handle, runBags.get(sentI).getRoadStart(),
							runBags.get(sentI).getRoadEnd());
				}
				else if (runBags.get(sentI).getHandler().equals("D1_Handle")) {
					play(6, isDribble, runBags.get(sentI).getRate(), defenderHandler, runBags.get(sentI).getRoadStart(),
							runBags.get(sentI).getRoadEnd());
				} else if (runBags.get(sentI).getHandler().equals("D2_Handle")) {
					play(7, isDribble, runBags.get(sentI).getRate(), defenderHandler, runBags.get(sentI).getRoadStart(),
							runBags.get(sentI).getRoadEnd());
				} else if (runBags.get(sentI).getHandler().equals("D3_Handle")) {
					play(8, isDribble, runBags.get(sentI).getRate(), defenderHandler, runBags.get(sentI).getRoadStart(),
							runBags.get(sentI).getRoadEnd());
				} else if (runBags.get(sentI).getHandler().equals("D4_Handle")) {
					play(9, isDribble, runBags.get(sentI).getRate(), defenderHandler, runBags.get(sentI).getRoadStart(),
							runBags.get(sentI).getRoadEnd());
				} else if (runBags.get(sentI).getHandler().equals("D5_Handle")) {
					play(10, isDribble, runBags.get(sentI).getRate(), defenderHandler, runBags.get(sentI).getRoadStart(),
							runBags.get(sentI).getRoadEnd());
				}

				if(hasQueryDefenderFromServer && !hasInvokeCurrentTimeDefender){
					Log.d("debug", "rate:" + runBags.get(sentI).getRate());
					play(6, false, 20, defenderHandler, Integer.parseInt(currentTimeMaxLen.get(sentTime/1000))*2, Integer.parseInt(currentTimeMaxLen.get(sentTime/1000+1))*2);
					play(7, false, 20, defenderHandler, Integer.parseInt(currentTimeMaxLen.get(sentTime/1000))*2, Integer.parseInt(currentTimeMaxLen.get(sentTime/1000+1))*2);
					play(8, false, 20, defenderHandler, Integer.parseInt(currentTimeMaxLen.get(sentTime/1000))*2, Integer.parseInt(currentTimeMaxLen.get(sentTime/1000+1))*2);
					play(9, false, 20, defenderHandler, Integer.parseInt(currentTimeMaxLen.get(sentTime/1000))*2, Integer.parseInt(currentTimeMaxLen.get(sentTime/1000+1))*2);
					play(10, false, 20, defenderHandler, Integer.parseInt(currentTimeMaxLen.get(sentTime/1000))*2, Integer.parseInt(currentTimeMaxLen.get(sentTime/1000+1))*2);
				}
			}
		}
	};
	//endregion

	/* When the icon of the player is touched. */
	private OnTouchListener playerListener = new OnTouchListener() {
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
		private float sample_rate;

		private Player currentPlayer;
		String handle_name = new String();
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
				int id = Integer.parseInt(v.getTag().toString());
				if(id == 6){
					//Log,i("debug","B    player ontouch!" );
					currentPlayer = ball;
					rotateWhichPlayer = 6;
					currentDrawer.curveIndex = ballDrawer.curveIndex;
					currentDrawer.tempCurve = ballDrawer.tempCurve;
					currentDrawer.paint = ballDrawer.paint;
					currentDrawer.startIndex = ballDrawer.startIndex;
					handle_name = "B_Handle";
					seekbar_player_Id = 6;
					ball.rect = new Rect((int) event.getX(),my - v.getTop(),(int) event.getX()+ v.getWidth(),my - v.getTop()+v.getHeight());

					TimeLine timefrag = (TimeLine) getActivity().getFragmentManager().findFragmentById(R.id.time_line);
					timefrag.changeLayout(6);
				}
				else if(id < 6){
					//Log,i("debug","P1    player ontouch!" );
					currentPlayer = players.get(id-1);
					currentDrawer.curveIndex = playerDrawers.get(id-1).curveIndex;
					currentDrawer.tempCurve = playerDrawers.get(id-1).tempCurve;
					currentDrawer.rotation = playerDrawers.get(id-1).rotation;
					currentDrawer.paint = playerDrawers.get(id-1).paint;
					currentDrawer.startIndex = playerDrawers.get(id-1).startIndex;
					rotateWhichPlayer = 1;
					handle_name = "P1_Handle";
					seekbar_player_Id = 1;
					players.get(id-1).rect = new Rect((int) event.getX(),my - v.getTop(),(int) event.getX()+ v.getWidth(),my - v.getTop()+v.getHeight());

					TimeLine timefrag = (TimeLine) getActivity().getFragmentManager().findFragmentById(R.id.time_line);
					timefrag.changeLayout(1);
				}
				else if(id > 6){
					//Log,i("debug","D1 ontouch!" );
					currentPlayer = defenders.get(id-7);
					currentDrawer.curveIndex = defenderDrawers.get(id-7).curveIndex;
					currentDrawer.tempCurve = defenderDrawers.get(id-7).tempCurve;
					currentDrawer.rotation = defenderDrawers.get(id-7).rotation;
					currentDrawer.paint= defenderDrawers.get(id-7).paint;
					currentDrawer.startIndex= defenderDrawers.get(id-7).startIndex;
					rotateWhichPlayer =7;
					handle_name="D1_Handle";
					seekbar_player_Id=7;
					//Log,i("debug", "first    P_startIndex="+Integer.toString(P_startIndex));
					//Log,i("debug", "first    D1_startIndex="+Integer.toString(D1_startIndex));
					TimeLine timefrag1 = (TimeLine) getActivity().getFragmentManager().findFragmentById(R.id.time_line);
					timefrag1.changeLayout(7);
				}
				else{
					Log.d("error", "playerListener -> MotionEvent.ACTION_DOWN: Wrong selection");
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
					currentPlayer.setRoad(0); // split positions
					currentPlayer.setMyRotation(-1);
				}

				//Pcanvas.drawCircle(mx - startX + v.getWidth()/2, my - startY + v.getHeight()/2, 20, player_paint);
				line_start_point_x = mx - startX + v.getWidth()/2;
				line_start_point_y = my - startY + v.getHeight()/2;

				return true;


			/*移動圖片***************************************************************************************************/
			case MotionEvent.ACTION_MOVE:// 移動圖片時
				x = mx - startX;
				y = my - startY;
				int id2 = Integer.parseInt(v.getTag().toString());
				if(id2 < 6)
					players.get(id2-1).rect =new Rect(x,y,x+ v.getWidth(),y+v.getHeight());
				else if (id2 == 6){
					ball.rect =new Rect(x,y,x+ v.getWidth(),y+v.getHeight());
					/*這裡是先把目前持有球的player先變回無持球狀態，再接著判斷有沒有intersect，確保當兩個player太接近的時候，會導致player明明無持球卻還是呈現有持球的狀態*/
					changePlayerToNoBall();
					//region 判斷是跟哪個player intersect
					if(Rect.intersects(players.get(0).rect, ball.rect)){
						//Log,i("debug", "P1 Intersects!");
						intersectName =1;
						intersect=true;
						if(playersWithBall.get(0).getVisibility()== playersWithBall.get(0).INVISIBLE){
							playersWithBall.get(0).layout((int)players.get(0).image.getX()-30, (int)players.get(0).image.getY(), (int)players.get(0).image.getX()-30+200, (int)players.get(0).image.getY()+120);
							playersWithBall.get(0).setVisibility(playersWithBall.get(0).VISIBLE);
							playersWithBall.get(0).invalidate();
							players.get(0).image.setVisibility(players.get(0).image.INVISIBLE);
							players.get(0).image.invalidate();
						}
					}
					else if (Rect.intersects(players.get(1).rect, ball.rect)){
						//Log,i("debug", "P2 Intersects!");
						intersectName =2;
						intersect=true;
						if(playersWithBall.get(1).getVisibility()== playersWithBall.get(1).INVISIBLE){
							playersWithBall.get(1).layout((int)players.get(1).image.getX()-30, (int)players.get(1).image.getY(), (int)players.get(1).image.getX()-30+200, (int)players.get(1).image.getY()+120);
							playersWithBall.get(1).setVisibility(playersWithBall.get(1).VISIBLE);
							playersWithBall.get(1).invalidate();
							players.get(1).image.setVisibility(players.get(1).image.INVISIBLE);
							players.get(1).image.invalidate();
						}
					}
					else if (Rect.intersects(players.get(2).rect, ball.rect)){
						//Log,i("debug", "P3 Intersects!");
						intersectName =3;
						intersect=true;
						if(playersWithBall.get(2).getVisibility()== playersWithBall.get(2).INVISIBLE){
							playersWithBall.get(2).layout((int)players.get(2).image.getX()-30, (int)players.get(2).image.getY(), (int)players.get(2).image.getX()-30+200, (int)players.get(2).image.getY()+120);
							playersWithBall.get(2).setVisibility(playersWithBall.get(2).VISIBLE);
							playersWithBall.get(2).invalidate();
							players.get(2).image.setVisibility(players.get(2).image.INVISIBLE);
							players.get(2).image.invalidate();
						}
					}
					else if (Rect.intersects(players.get(3).rect, ball.rect)){
						//Log,i("debug", "P4 Intersects!");
						intersectName =4;
						intersect=true;
						if(playersWithBall.get(3).getVisibility()== playersWithBall.get(3).INVISIBLE){
							playersWithBall.get(3).layout((int)players.get(3).image.getX()-30, (int)players.get(3).image.getY(), (int)players.get(3).image.getX()-30+200, (int)players.get(3).image.getY()+120);
							playersWithBall.get(3).setVisibility(playersWithBall.get(3).VISIBLE);
							playersWithBall.get(3).invalidate();
							players.get(3).image.setVisibility(players.get(3).image.INVISIBLE);
							players.get(3).image.invalidate();
						}
					}
					else if (Rect.intersects(players.get(4).rect, ball.rect)){
						//Log,i("debug", "P5 Intersects!");
						intersectName =5;
						intersect=true;
						if(playersWithBall.get(4).getVisibility()== playersWithBall.get(4).INVISIBLE){
							playersWithBall.get(4).layout((int)players.get(4).image.getX()-30, (int)players.get(4).image.getY(), (int)players.get(4).image.getX()-30+200, (int)players.get(4).image.getY()+120);
							playersWithBall.get(4).setVisibility(playersWithBall.get(4).VISIBLE);
							playersWithBall.get(4).invalidate();
							players.get(4).image.setVisibility(players.get(4).image.INVISIBLE);
							players.get(4).image.invalidate();
						}
					}
					else{
						if(intersectName > 0){
							players.get(intersectName-1).image.setVisibility(players.get(intersectName-1).image.VISIBLE);
							if(intersect){
								////Log,i("debug","intersect=true,player1 should be layout on player_ball's position");
								players.get(intersectName-1).image.layout((int) playersWithBall.get(intersectName-1).getX()+30, (int) playersWithBall.get(intersectName-1).getY(),(int) playersWithBall.get(intersectName-1).getX()+30+players.get(intersectName-1).image.getWidth(), (int) playersWithBall.get(intersectName-1).getY()+players.get(intersectName-1).image.getHeight());
							}
							players.get(intersectName-1).image.invalidate();
							playersWithBall.get(intersectName-1).setVisibility(playersWithBall.get(intersectName-1).INVISIBLE);
							playersWithBall.get(intersectName-1).invalidate();
						}
						intersect=false;
						intersectName = 0;
					}
					//endregion
				}

				//region 當正在繪製軌跡時
				if (recordCheck == true) {
					move_count += 2;
					currentPlayer.setRoad(x);
					currentPlayer.setRoad(y);
					currentPlayer.setMyRotation(currentDrawer.rotation);
					//Log,i("debug", "player setMyRotation");
					currentDrawer.tempCurve.add(currentDrawer.curveIndex, new Point(x, y));
					currentDrawer.curveIndex++;
					P_curve_x.add((float)mx);
					P_curve_y.add((float)my);

					// 每畫三個點
					if(currentDrawer.curveIndex == N){
						boolean isBallHolder = (rotateWhichPlayer == intersectName);
						Log.i("debug", "Touch Event : "+ rotateWhichPlayer +", "+ intersectName);
						Log.i("debug", "sample : "+sample_rate+"  "+ handle_name);
						boolean whetherDraw = ((sample_rate >= 1.0F) && handle_name.equals("B_Handle")) || (!handle_name.equals("B_Handle") && !isBallHolder);

						//region 找到畫出的路徑相對於平板坐標系的旋轉角度，才能知道掩護的線要畫在哪裡的垂直角度上
						float pivot_dir_x = 0.0f;
						float pivot_dir_y = -1.0f;
						float pivot_dir_x2 = 1.0f;
						float pivot_dir_y2 = 0.0f;

						float target_dir_x = currentDrawer.tempCurve.get(2).x - currentDrawer.tempCurve.get(1).x;
						float target_dir_y = currentDrawer.tempCurve.get(2).y - currentDrawer.tempCurve.get(1).y;
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
								Vector<Point> tempCurve = currentDrawer.tempCurve;
								if (tempCurve.get(tmp - 1).x < tempCurve.get(tmp).x) {//  x遞增
									if (tmp == 1 && ((int) tempCurve.get(1).x == (int) tempCurve.get(2).x)) {//第二、三格一樣，給lagrange的陣列不能有x一樣的情況，所以只能給lagrange兩格(一、二格)
										Vector<Point> intermediate = new Vector<>();
										intermediate.add(tempCurve.get(0));
										intermediate.add(tempCurve.get(1));
										for (float tmp_x = tempCurve.get(tmp - 1).x; tmp_x <= tempCurve.get(tmp).x; tmp_x = tmp_x + (float) 0.1) {
											tmp_y = lagrange(intermediate, tmp_x);
											Pcanvas.drawCircle(tmp_x + v.getWidth() / 2, tmp_y + v.getHeight() / 2, 5, currentDrawer.paint);
										}

									} else if (tmp == 2 && ((int) tempCurve.get(0).x == (int) tempCurve.get(1).x)) {//第一、二格一樣
										Vector<Point> intermediate = new Vector<>();
										intermediate.add(tempCurve.get(1));
										intermediate.add(tempCurve.get(2));
										for (float tmp_x = tempCurve.get(tmp - 1).x; tmp_x <= tempCurve.get(tmp).x; tmp_x = tmp_x + (float) 0.1) {
											tmp_y = lagrange(intermediate, tmp_x);
											Pcanvas.drawCircle(tmp_x + v.getWidth() / 2, tmp_y + v.getHeight() / 2, 5, currentDrawer.paint);//black
										}
									} else {
										if (tmp == 1 && (int) tempCurve.get(1).x > (int) tempCurve.get(2).x) {//1<2   2>3
											Vector<Point> intermediate = new Vector<>();
											intermediate.add(tempCurve.get(0));
											intermediate.add(tempCurve.get(1));
											for (float tmp_x = tempCurve.get(tmp - 1).x; tmp_x <= tempCurve.get(tmp).x; tmp_x = tmp_x + (float) 0.1) {
												tmp_y = lagrange(intermediate, tmp_x);
												Pcanvas.drawCircle(tmp_x + v.getWidth() / 2, tmp_y + v.getHeight() / 2, 5, currentDrawer.paint);//black
											}
										} else if (tmp == 2 && ((int) tempCurve.get(0).x > (int) tempCurve.get(1).x)) {//1>2   2<3
											Vector<Point> intermediate = new Vector<>();
											intermediate.add(tempCurve.get(1));
											intermediate.add(tempCurve.get(2));
											for (float tmp_x = tempCurve.get(tmp - 1).x; tmp_x <= tempCurve.get(tmp).x; tmp_x = tmp_x + (float) 0.1) {
												tmp_y = lagrange(intermediate, tmp_x);
												Pcanvas.drawCircle(tmp_x + v.getWidth() / 2, tmp_y + v.getHeight() / 2, 5, currentDrawer.paint);//black
											}
										} else {
											for (float tmp_x = tempCurve.get(tmp - 1).x; tmp_x <= tempCurve.get(tmp).x; tmp_x = tmp_x + (float) 0.1) {
												tmp_y = lagrange(tempCurve, tmp_x);
												Pcanvas.drawCircle(tmp_x + v.getWidth() / 2, tmp_y + v.getHeight() / 2, 5, currentDrawer.paint);//black
											}
										}
									}
								} else if (tempCurve.get(tmp - 1).x > tempCurve.get(tmp).x) {//x遞減
									if (tmp == 1 && ((int) tempCurve.get(1).x == (int) tempCurve.get(2).x)) {//第二、三格一樣，給lagrange的陣列不能有x一樣的情況，所以只能給lagrange兩格(一、二格)
										Vector<Point> intermediate = new Vector<>();
										intermediate.add(tempCurve.get(0));
										intermediate.add(tempCurve.get(1));
										for (float tmp_x = tempCurve.get(tmp - 1).x; tmp_x >= tempCurve.get(tmp).x; tmp_x = tmp_x - (float) 0.1) {
											tmp_y = lagrange(intermediate, tmp_x);
											Pcanvas.drawCircle(tmp_x + v.getWidth() / 2, tmp_y + v.getHeight() / 2, 5, currentDrawer.paint);//red
										}
									} else if (tmp == 2 && ((int) tempCurve.get(0).x == (int) tempCurve.get(1).x)) {//第一、二格一樣
										Vector<Point> intermediate = new Vector<>();
										intermediate.add(tempCurve.get(1));
										intermediate.add(tempCurve.get(2));
										for (float tmp_x = tempCurve.get(tmp - 1).x; tmp_x >= tempCurve.get(tmp).x; tmp_x = tmp_x - (float) 0.1) {
											tmp_y = lagrange(intermediate, tmp_x);
											Pcanvas.drawCircle(tmp_x + v.getWidth() / 2, tmp_y + v.getHeight() / 2, 5, currentDrawer.paint);//red
										}
									}//都不一樣
									else {
										if (tmp == 1 && (int) tempCurve.get(1).x < (int) tempCurve.get(2).x) {//1>2   2<3
											Vector<Point> intermediate = new Vector<>();
											intermediate.add(tempCurve.get(0));
											intermediate.add(tempCurve.get(1));
											for (float tmp_x = tempCurve.get(tmp - 1).x; tmp_x >= tempCurve.get(tmp).x; tmp_x = tmp_x - (float) 0.1) {
												tmp_y = lagrange(intermediate, tmp_x);
												Pcanvas.drawCircle(tmp_x + v.getWidth() / 2, tmp_y + v.getHeight() / 2, 5, currentDrawer.paint);//red
											}
										} else if (tmp == 2 && ((int) tempCurve.get(0).x < (int) tempCurve.get(1).x)) {//1<2   2>3
											Vector<Point> intermediate = new Vector<>();
											intermediate.add(tempCurve.get(1));
											intermediate.add(tempCurve.get(2));
											for (float tmp_x = tempCurve.get(tmp - 1).x; tmp_x >= tempCurve.get(tmp).x; tmp_x = tmp_x - (float) 0.1) {
												tmp_y = lagrange(intermediate, tmp_x);
												Pcanvas.drawCircle(tmp_x + v.getWidth() / 2, tmp_y + v.getHeight() / 2, 5, currentDrawer.paint);//red
											}
										} else {
											for (float tmp_x = tempCurve.get(tmp - 1).x; tmp_x >= tempCurve.get(tmp).x; tmp_x = tmp_x - (float) 0.1) {
												tmp_y = lagrange(tempCurve, tmp_x);
												Pcanvas.drawCircle(tmp_x + v.getWidth() / 2, tmp_y + v.getHeight() / 2, 5, currentDrawer.paint);//red
											}
										}
									}
								} else {//==  line 其中兩格一樣的話，就兩格之間畫直線
									int tmp_x = tempCurve.get(tmp - 1).x;
									if (tempCurve.get(tmp - 1).x > tempCurve.get(tmp).x) {
										for (float tmp_y = tempCurve.get(tmp - 1).y; tmp_y >= tempCurve.get(tmp).y; tmp_y = tmp_y - (float) 0.1) {
											Pcanvas.drawCircle(tmp_x + v.getWidth() / 2, tmp_y + v.getHeight() / 2, 5, currentDrawer.paint);//blue
										}
									} else {
										for (float tmp_y = tempCurve.get(tmp - 1).y; tmp_y <= tempCurve.get(tmp).y; tmp_y = tmp_y + (float) 0.1) {
											Pcanvas.drawCircle(tmp_x + v.getWidth() / 2, tmp_y + v.getHeight() / 2, 5, currentDrawer.paint);//green
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
						currentDrawer.clearCurve();
						currentDrawer.tempCurve.add(currentDrawer.curveIndex, new Point(x, y));
						currentDrawer.curveIndex++;
					}

					tempCanvas.drawBitmap(Pbitmap, 0,0, null);
					Pcanvas.drawBitmap(Pbitmap, 0, 0, transparentPaint);

					circle.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
				}
				//endregion


				if(currentPlayer.arrow != null){
					currentPlayer.arrow.layout(x, y, x + v.getWidth(), y + v.getHeight());
					currentPlayer.arrow.setRotation(currentDrawer.rotation);
					TimeLine timefrag1 = (TimeLine) getActivity().getFragmentManager().findFragmentById(R.id.time_line);
					timefrag1.setCircularSeekBarProgress(currentDrawer.rotation);//為了讓circular seekbar的值也一起變成儲存的狀態，但是因為android好像有bug，所以他不會更新介面上的seekbar的樣子，但值卻是有改過的
					//Log.d("debug", "setCircularSeekBarProgress P1_rotate="+Integer.toString(P1_rotate));
					currentPlayer.arrow.postInvalidate();
				}
				if(intersect==true && v.getTag().toString().equals("6")==false && v.getTag().toString().equals(Integer.toString(intersectName))){
					ball.image.layout(x+110, y+30, x+170, y+90);
				}
				v.layout(x, y, x + v.getWidth(), y + v.getHeight());
				v.postInvalidate();
				////Log,i("debug", "x="+Integer.toString(x));
				////Log,i("debug", "y="+Integer.toString(y));
				return true;

				/*放開圖片***************************************************************************************************/
			case MotionEvent.ACTION_UP://放開照片時
				Log.i("debug", "intersect_name_pre="+Integer.toString(intersectNamePre));
				Log.i("debug", "intersect_name="+Integer.toString(intersectName));

				if(v.getTag().toString().equals("6") && intersect==true){
					if(intersectName > 0){
						if(players.get(intersectName-1).image.getVisibility()==players.get(intersectName-1).image.INVISIBLE){//代表是player1_ball顯示中
							players.get(intersectName-1).arrow.layout((int) playersWithBall.get(intersectName-1).getX(), (int) playersWithBall.get(intersectName-1).getY(), (int) playersWithBall.get(intersectName-1).getX()+ playersWithBall.get(intersectName-1).getWidth(), (int) playersWithBall.get(intersectName-1).getY()+ playersWithBall.get(intersectName-1).getHeight());
						}
						players.get(intersectName-1).arrow.invalidate();
						v.layout((int) players.get(intersectName-1).image.getX()+110-30, (int)players.get(intersectName-1).image.getY()+30, (int) players.get(intersectName-1).image.getX()+170-30,(int)players.get(intersectName-1).image.getY()+90);
						v.invalidate();
					}
				}
				else if(v.getTag().toString().equals("6") && intersect == false){
					if(intersectName > 0){
						if(players.get(intersectName-1).image.getVisibility() == players.get(intersectName-1).image.VISIBLE){
							players.get(intersectName-1).arrow.layout((int)players.get(intersectName-1).image.getX(), (int)players.get(intersectName-1).image.getY(), (int)players.get(intersectName-1).image.getX()+players.get(intersectName-1).image.getWidth(), (int)players.get(intersectName-1).image.getY()+players.get(intersectName-1).image.getHeight());
						}
						players.get(intersectName-1).arrow.invalidate();
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
							currentPlayer.getCmpltRoad().remove(currentPlayer.getRoadSize()-1);
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
						mainwrapfrag.createPathNumOnCourt(runBags.size()+1, x, y, runBags.size());

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
						int maxIndex = 0;
						int maxValue = direction_hist.get(0);

						int driMaxIndex = 0;
						int driMaxValue = dribble_direction_hist.get(0);

						for(int i=1;i<direction_hist.size();i++){
							if(direction_hist.get(i) > maxValue){
								maxValue = direction_hist.get(i);
								maxIndex = i;
							}

							if(dribble_direction_hist.get(i) > driMaxValue){
								driMaxValue = dribble_direction_hist.get(i);
								driMaxIndex = i;
							}
						}

						previousDirection.clear();
						previousDribbleDirection.clear();

						////Log,i("debug", "Screen direction : " + screen_direction);
						////Log,i("debug", "Last direction : "+ last_direction);

						screen_direction = maxIndex*10.0f + 5.0f;

						if(isScreenEnable)
							mainwrapfrag.createScreenBar(x, y, rotateWhichPlayer,  screen_direction, runBags.size());
						//endregion

						last_direction = driMaxIndex*10.0f + 5.0f;
						boolean isBallHolder = (rotateWhichPlayer == intersectName);
						float drawn_length = (float)Math.sqrt( Math.pow(x + v.getWidth()/2 - line_start_point_x, 2) + Math.pow(y + v.getHeight()/2 - line_start_point_y, 2));
						////Log,i("debug","Drawn length: "+ drawn_length);
						drawn_length = drawn_length / 150.0f / 2.0f;

						if(isBallHolder)
							mainwrapfrag.createDribbleLine((int)line_start_point_x, (int)line_start_point_y, rotateWhichPlayer, last_direction, drawn_length, runBags.size());

						Pcanvas.drawCircle((int)line_start_point_x, (int)line_start_point_y, 10, currentDrawer.paint);

						bitmapVector.add(Pbitmap);
						/*//Log,i("debug", "bitmap_size="+Integer.toString(Bitmap_vector.size()));
						//Log,i("debug", "P1_curve_x_size="+Integer.toString(P1.getCmpltCurve().get(tmp).size()));
						//Log,i("debug", "P1_curve_y_size="+Integer.toString(P1.getCmpltCurve().get(tmp+1).size()));*/

						int startIndexTmp = currentDrawer.startIndex;
						////Log,i("debug", "dum_flag = true , P_startIndex ="+Integer.toString(P_startIndex));
						while (currentDrawer.startIndex != -1) {
							RunBag tmp = new RunBag();
							tmp.setStartTime(seekBarCallbackStartTime);
							tmp.setHandler(handle_name);
							tmp.setRoadStart(currentDrawer.startIndex + 1);
							currentDrawer.startIndex += 2;
							currentDrawer.startIndex = currentPlayer.getRoad(0, currentDrawer.startIndex);
							if (currentDrawer.startIndex == -1) {
								startIndexTmp = currentPlayer.getLastRoad();
								tmp.setRoadEnd(startIndexTmp);
							} else {
								tmp.setRoadEnd(currentDrawer.startIndex);
								startIndexTmp = currentDrawer.startIndex;
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
						currentDrawer.startIndex = startIndexTmp + 1;
					}
				}
				else{
					if(runBags.size()==0){
						currentPlayer.initialPosition = new Point(x, y);
					}
				}
				//endregion
				int id3 = Integer.parseInt(v.getTag().toString());
				if(id3 == 6){
					ball= currentPlayer;
					ballDrawer.curveIndex = currentDrawer.curveIndex;
					ballDrawer.tempCurve = currentDrawer.tempCurve;
					ballDrawer.startIndex =currentDrawer.startIndex;
					handle_name="B_Handle";
					ball.initialPosition = currentPlayer.initialPosition;
					seekbar_player_Id=6;
				}
				else if(id3 < 6){
					players.set(id3-1, currentPlayer);
					playerDrawers.get(id3-1).curveIndex = currentDrawer.curveIndex;
					playerDrawers.get(id3-1).tempCurve = currentDrawer.tempCurve;
					playerDrawers.get(id3-1).rotation = currentDrawer.rotation;
					playerDrawers.get(id3-1).startIndex =currentDrawer.startIndex;
					handle_name="P" + String.valueOf(id3) + "_Handle";
					players.get(id3-1).initialPosition = currentPlayer.initialPosition;
					seekbar_player_Id=1;
				}
				else if(id3 > 6){
					defenders.set(id3-7, currentPlayer);
					defenderDrawers.get(id3-7).curveIndex = currentDrawer.curveIndex;
					defenderDrawers.get(id3-7).tempCurve = currentDrawer.tempCurve;
					defenderDrawers.get(id3-7).rotation = currentDrawer.rotation;
					defenderDrawers.get(id3-7).startIndex = currentDrawer.startIndex;
					handle_name="D"+ String.valueOf(id3-6) +"_Handle";
					defenders.get(id3-7).initialPosition = currentPlayer.initialPosition;
					seekbar_player_Id=7;
				}
				else{
					Log.d("error", "playerListener -> MotionEvent.ACTION_UP: Wrong selection");
				}
				intersectNamePre = intersectName;//判斷球有沒有傳給別的player。p.s. --> pre = 上一個拿球的人

				return true;//放開圖片的case
			}//switch觸控動作

			Pbitmap.recycle();
			System.gc();

			return true;
		}//onTouch Event
	};

	public void removeOnePath(int seekbarId){
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
		mainwrap.removeTextView(whichToRemove);
		mainwrap.removeScreenBar(whichToRemove);
		mainwrap.removeDribbleLine(whichToRemove);

		/*remove runline*/
		/*Every component's position in the RunLine should be the same with the TimeLine SeekBarId.*/
		for(int index = whichToRemove +1; index< runBags.size(); index++){
			////Log,i("seekbar", "Change seekbar's ID, search ID(index)="+Integer.toString(index));
			timefrag1.changeSeekBarId(index,index-1);
			mainwrap.changeTextViewId(index, index-1);//change the remained pathnum text and its id
			mainwrap.changeScreenBarTag(index, index-1);
			mainwrap.changeDribbleLineTag(index, index-1);
			runBags.get(index).setTimeLineId(index-1);
		}
		runBags.remove(whichToRemove);
		timefrag1.setRunLineId(runBags.size()-1);
		////Log,i("seekbar", "RunLine.size="+Integer.toString(RunLine.size()));

		/*remove Curves*/
		curves.remove(whichToRemove *2);
		curves.remove(whichToRemove *2);

		sortPathnum();
	}

	public void sortPathnum(){
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
			mainwrap.setPathnumText(list.get(i).getTimeLineId(), show_text);

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