package com.mislab.tacticboard;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
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
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

	private ImageView removeButton;
	private Vector<ImageView> playersWithBall;

	private boolean firstRecord = true;
	private boolean intersect= false;
	private int intersectId = 0; // 1, 2, 3, 4, 5
	private int preIntersectId = 0; // 1, 2, 3, 4, 5
	private int actionMovePreIntersectId = 0;
	private int initialBallHolderId = 0; // 1, 2, 3, 4, 5
	private int rotateWhichPlayer; // 1, 2, 3, 4, 5
	private PerspectiveSelect perspectiveSelect = null;

	private PlayerDrawer currentDrawer = new PlayerDrawer(Color.parseColor("#000000"));
	private static Vector<PlayerDrawer> playerDrawers;
	private static Vector<PlayerDrawer> defenderDrawers;
	private PlayerDrawer ballDrawer;

	private static Vector<Player> players;
	private static Vector<Player> defenders;
	private Player ball;

	private int seekbarTmpId =0;

	private int whichToRemove =-1;
	private boolean hasQueryDefenderFromServer;
	private boolean hasInvokeCurrentTimeDefender;

    /**畫圖變數**/
	private ImageView circle;
	private Vector <Bitmap> bitmapVector;
	private Bitmap tempBitmap;
	private Canvas tempCanvas;
	private Region bitmapRegion;

	private Paint transparentPaint;
    /*************************曲線變數************************************/
    private final int N = 3;
	private List<String> currentTimeMaxLen;

    //region 錄製變數
	private boolean isRecording = false;
	private int seekBarCallbackStartTime;
	private int seekBarCallbackDuration;
	private int seekBarCallbackId;
	private Vector<RunBag> runBags = new Vector();
	private int mainFragSeekBarProgressLow = 0;
	//endregion

    //Socket 變數
	private InetAddress serverAddr;
	private int UDP_SERVER_PORT = 3985;
	/*************************************************************/

	// draw軌跡
	private static Vector<Point> tempCurve;//&& !isBallHolder

	// for undo ball alert
	private boolean undoAlert=false;

	//觸碰到圖片的動畫參數
	private Animation zoomAnimation;
	private Animation zoomAnimationDribbleBall;

	//screen layout timeline
	private Boolean originalIsTimelineShow=false;
	//screen layout disable view ontouch event
	private Boolean disableOntouch=false;

	// 紀錄timeline count
	private int timelineIndex=0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_main, container,false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {    
		super.onActivityCreated(savedInstanceState);

		zoomAnimation = AnimationUtils.loadAnimation(getActivity(),R.anim.zoom);
		zoomAnimationDribbleBall = AnimationUtils.loadAnimation(getActivity(),R.anim.zoom_dribble_ball);

		selectCategoryId = 0;
		hasQueryDefenderFromServer = false;
		//region Initialize Player icons on the view
		Resources resources = getResources();
		players = new Vector<Player>();
		for(int i=0 ; i<5 ; i++){
			ImageView playerImg = (ImageView)getView().findViewById(resources.getIdentifier("image_p"+(i+1), "id", getActivity().getPackageName()));
			playerImg.setOnTouchListener(playerListener);
			ImageView arrowImg = (ImageView)getView().findViewById(resources.getIdentifier("arrow"+(i+1), "id", getActivity().getPackageName()));
			players.add(new Player(playerImg, arrowImg)); //player id 0-4
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

		playersWithBall = new Vector<ImageView>();
		for(int i=0 ; i<5 ; i++){
			ImageView playerImg = (ImageView)getView().findViewById(resources.getIdentifier("image_p"+(i+1)+"withBall", "id", getActivity().getPackageName()));
			playerImg.setOnTouchListener(playerListener);
			playersWithBall.add(playerImg);
		}
		//endregion

		//region Initialize drawing parameters
		playerDrawers = new Vector<PlayerDrawer>();
		playerDrawers.add(new PlayerDrawer(Color.parseColor("#133C55")));
		playerDrawers.add(new PlayerDrawer(Color.parseColor("#154FB5")));
		playerDrawers.add(new PlayerDrawer(Color.parseColor("#59A5DB")));
		playerDrawers.add(new PlayerDrawer(Color.parseColor("#84D2F6")));
		playerDrawers.add(new PlayerDrawer(Color.parseColor("#DCFFFD")));

		defenderDrawers = new Vector<PlayerDrawer>();
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
		
		circle = (ImageView) getActivity().findViewById(R.id.circle); //主要畫布

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
			//  todo Auto-generated catch block

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

	public void setUDPIP(InetAddress ip, int port){
		serverAddr = ip;
	    UDP_SERVER_PORT = port;
	}

	//region Get corresponding defender from the server
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
	//endregion

	/**********************************************VR***********************************************/


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
	public void setRecording(boolean input) {
        /*停止錄製按下之後，畫線的curve就清除重算*/
		if(isRecording == true && input == false){
			for(int i=0;i<5;i++){
				playerDrawers.get(i).clearCurve();
				defenderDrawers.get(i).clearCurve();
			}
			ballDrawer.clearCurve();
		}
		/*開始錄製的鍵 intersect在的位置就是持球者*/
		if(isRecording == false && input==true && firstRecord ==true && intersectId !=0){
			firstRecord =false;
			initialBallHolderId = preIntersectId;
			Log.d("socket", "initial_ball_num="+Integer.toString(initialBallHolderId));
		}
		
		
		isRecording = input;
	}
	public void setSeekBarToRunBag(Vector <Integer> input){
		/**
		 * Vector<Integer> input: 0=Id,1=StartTime,2=Duration
		 * **/
		RunBag tmp = new RunBag();
		tmp= runBags.get(input.get(0));
		tmp.setStartTime(input.get(1));
		tmp.setDuration(input.get(2));
		runBags.set(input.get(0), tmp); //改變某第幾筆資料
		//Log.d("debug", "Set! "+Integer.toString(input.get(0)));
	}

	// 從view的ID反查現在runbag的index
	public int queryForRunbagIndex(int timelineID){

		int result = -1;
		for (int i=0;i<runBags.size();i++){
			if(runBags.get(i).getTimeLineId()==timelineID){
				return i;
			}
		}
		return result ;
	}

	public void setMainFragProLow(int Low_in){
		mainFragSeekBarProgressLow =Low_in;
		////Log,i("debug", "MainFrag_set MainFrag_SeekBarProgressLow ="+Integer.toString(MainFrag_SeekBarProgressLow));
	}

	public int getRunBags(){ //undo那邊防呆用
		return runBags.size();
	}

	// 如果undo的是中間的path 比這個pathNum大的其他pathNum都要-1
	public void adjustAllPathNum(int pathID){
		for (int i=0; i<runBags.size(); i++){
			if (runBags.get(i).getPathIndex()>pathID){
				runBags.get(i).setPathIndex(runBags.get(i).getPathIndex()-1);
			}
		}
	}

	public void undoPaint(){//清除上一筆畫畫筆跡
		//region bitmap
		tempBitmap = Bitmap.createBitmap(circle.getWidth(),circle.getHeight(),Bitmap.Config.ARGB_8888);//初始化tempBitmap，指定大小為螢幕大小(大小同circle)
		tempCanvas = new Canvas(tempBitmap);

		// remove Dbitmap
		// runbag上一動的path
		int pathNum= runBags.get(runBags.size()-1).getPathIndex();
		bitmapVector.remove(pathNum);
		if(!bitmapVector.isEmpty()) {
			for (int tmp = 0; tmp < bitmapVector.size(); tmp++) {
				tempCanvas.drawBitmap(bitmapVector.get(tmp), 0, 0, null);
			}
		} else{
			Log.d("Undo","have no bitmapvector");
			//initialBallHolderId =0;
			firstRecord =true;//跟著井井寫
			IsTacticPlaying =0;//跟著井井寫
		}

		// 調整其他runbag的pathNum
		adjustAllPathNum(pathNum);

		circle.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
		for(int i=0;i<5;i++){
			playerDrawers.get(i).clearCurve();
			defenderDrawers.get(i).clearCurve();
		}
		ballDrawer.clearCurve();
		//endregion


	}

	public void undoRecord(){

		int timefragIndex=0;
		int ballholder;
		Player undoPlayer;
		PlayerDrawer undoDrawer;

		//從runbag上一動的資訊去移動image的位置
		int lastEndIndex = runBags.get(runBags.size()-1).getRoadStart(); //下一個的起頭是上一個的終點
		String handler = runBags.get(runBags.size()-1).getHandler(); //which to move back
		//Log.d("undo","undo which player:"+handler);

		if(handler.equals("B_Handle")){ //球的移動

			undoPlayer=ball;
			undoDrawer=ballDrawer;

			if(runBags.size()==1){ //上一動是最初拿球的人
				ballholder=initialBallHolderId;
				preIntersectId=ballholder; //重新開始錄製那邊要用的參數
				Log.d("undo","initial ballholder ID"+String.valueOf(ballholder));
			}else{
				ballholder=runBags.get(runBags.size()-2).getBallNum(); //要返回的上一動球在誰身上 (球要回到某人手上
				Log.d("undo","last ballholder ID"+String.valueOf(ballholder));
			}

			//讓原持球者回到無持球狀態 (undo完會再改為正確intersectid)
			intersectId=runBags.get(runBags.size()-1).getBallNum(); //現在這一動球在誰身上
			Log.d("undo","undo pass ball playerID is"+String.valueOf(intersectId));
			intersect=false; // 在undo時 球不會有intersection問題(changePlayerToNoBall()裡面的判斷式)
			changePlayerToNoBall();

			//讓上一動持球者拿球 這一動的回到原本狀態
			playersWithBall.get(ballholder-1).layout((int)players.get(ballholder-1).image.getX()-30, (int)players.get(ballholder-1).image.getY(),
					(int)players.get(ballholder-1).image.getX()-30+200, (int)players.get(ballholder-1).image.getY()+120);
			playersWithBall.get(ballholder-1).setVisibility(playersWithBall.get(ballholder-1).VISIBLE);
			players.get(ballholder-1).arrow.layout((int) playersWithBall.get(ballholder-1).getX(), (int) playersWithBall.get(ballholder-1).getY(),
					(int) playersWithBall.get(ballholder-1).getX()+ playersWithBall.get(ballholder-1).getWidth(), (int) playersWithBall.get(ballholder-1).getY()+ playersWithBall.get(ballholder-1).getHeight());
			players.get(ballholder-1).image.setVisibility(players.get(ballholder-1).image.INVISIBLE);
			ball.image.layout((int) players.get(ballholder -1).image.getX()+110-30, (int)players.get(ballholder -1).image.getY()+30,
					(int) players.get(ballholder -1).image.getX()+170-30,(int)players.get(ballholder -1).image.getY()+90);

			timefragIndex=6; //ball
			intersectId=ballholder; //讓intersectid回到undo完的這個人身上
			intersect=true;

		}else{ //現在這個持球者或無持球者的移動(球無轉移)
			int which= handler.charAt(1)-48; //char to int

			boolean isdribble=(runBags.get(runBags.size()-1).getPathType()==2);
			// Log.d("player","Undo player is:"+String.valueOf(which)+" ,Is dribble"+isdribble);

			if(handler.charAt(0)=='P'){ //Player
				undoPlayer=players.get(which-1);
				undoDrawer=playerDrawers.get(which-1);

				if(isdribble){

					// person //這邊要debug一下!!!!!!!!!!!!!!!!!
					players.get(which-1).image.layout(players.get(which-1).handleGetRoad(lastEndIndex),players.get(which-1).handleGetRoad(lastEndIndex+1),
							players.get(which-1).handleGetRoad(lastEndIndex)+players.get(which-1).image.getWidth(),
							players.get(which-1).handleGetRoad(lastEndIndex+1)+players.get(which-1).image.getHeight());
					playersWithBall.get(which-1).layout((int)players.get(which-1).image.getX(), (int)players.get(which-1).image.getY(),
							(int)players.get(which-1).image.getX()+200, (int)players.get(which-1).image.getY()+120);
					ball.image.layout((int) players.get(which -1).image.getX()+110, (int)players.get(which -1).image.getY()+30,
							(int) players.get(which -1).image.getX()+170,(int)players.get(which -1).image.getY()+90);

					// arrow
					players.get(which-1).arrow.layout((int)playersWithBall.get(which-1).getX(),(int)playersWithBall.get(which-1).getY(),
							(int)playersWithBall.get(which-1).getX()+playersWithBall.get(which-1).getWidth(),
							(int)playersWithBall.get(which-1).getY()+playersWithBall.get(which-1).getHeight());

					players.get(which-1).image.setVisibility(players.get(which-1).image.INVISIBLE);
					playersWithBall.get(which-1).setVisibility(playersWithBall.get(which-1).VISIBLE);

				}else{
					// person
					players.get(which-1).image.layout(players.get(which-1).handleGetRoad(lastEndIndex),players.get(which-1).handleGetRoad(lastEndIndex+1),
							players.get(which-1).handleGetRoad(lastEndIndex)+players.get(which-1).image.getWidth(),
							players.get(which-1).handleGetRoad(lastEndIndex+1)+players.get(which-1).image.getHeight());
					// arrow
					players.get(which-1).arrow.layout(players.get(which-1).handleGetRoad(lastEndIndex),players.get(which-1).handleGetRoad(lastEndIndex+1),
							players.get(which-1).handleGetRoad(lastEndIndex)+players.get(which-1).arrow.getWidth(),
							players.get(which-1).handleGetRoad(lastEndIndex+1)+players.get(which-1).arrow.getHeight());
				}

				players.get(which-1).image.invalidate();
				players.get(which-1).arrow.invalidate();

				//重新定義現在player的rec
				//players.get(which-1).rect =new Rect(players.get(which-1).handleGetRoad(lastEndIndex),players.get(which-1).handleGetRoad(lastEndIndex+1),
				//		players.get(which-1).handleGetRoad(lastEndIndex)+ players.get(which-1).image.getWidth(),players.get(which-1).handleGetRoad(lastEndIndex+1)+players.get(which-1).image.getHeight());
				players.get(which-1).setRect(players.get(which-1).handleGetRoad(lastEndIndex),players.get(which-1).handleGetRoad(lastEndIndex+1),
						players.get(which-1).handleGetRoad(lastEndIndex)+ players.get(which-1).image.getWidth(),players.get(which-1).handleGetRoad(lastEndIndex+1)+players.get(which-1).image.getHeight());
				timefragIndex=which;
			}else{ //Defender
				undoPlayer=defenders.get(which-1);
				undoDrawer=defenderDrawers.get(which-1);

				// person
				defenders.get(which-1).image.layout(defenders.get(which-1).handleGetRoad(lastEndIndex),defenders.get(which-1).handleGetRoad(lastEndIndex+1),
						defenders.get(which-1).handleGetRoad(lastEndIndex)+defenders.get(which-1).image.getWidth(),
						defenders.get(which-1).handleGetRoad(lastEndIndex+1)+defenders.get(which-1).image.getHeight());
				// arrow
				defenders.get(which-1).arrow.layout(defenders.get(which-1).handleGetRoad(lastEndIndex),defenders.get(which-1).handleGetRoad(lastEndIndex+1),
						defenders.get(which-1).handleGetRoad(lastEndIndex)+defenders.get(which-1).arrow.getWidth(),
						defenders.get(which-1).handleGetRoad(lastEndIndex+1)+defenders.get(which-1).arrow.getHeight());
				//defenders.get(which-1).image.invalidate();
				//defenders.get(which-1).arrow.invalidate();

				timefragIndex=which+6;
			}

		}
		// time layout 顯示這一動狀況
		TimeLine timefrag = (TimeLine) getActivity().getFragmentManager().findFragmentById(R.id.time_line);
		timefrag.changeLayout(timefragIndex);

		//region remove timeline 要知道上一個record是誰
		int timelineIndex=runBags.get(runBags.size()-1).getTimeLineId();
		Log.d("undo","remove timelineindex:"+Integer.toString(timelineIndex));
		timefrag.removeOneTimeline(timelineIndex);
		timefrag.setRunlineId(runBags.size()-1); //還沒有很仔細看

		//移動rangeslider的下界
		int drawStart=runBags.get(runBags.size()-1).getStartTime();
		mainFragSeekBarProgressLow=drawStart; //seekbar位置調整
		//endregion

		/**** TODO ***/
		/*remove pathnumber and screenbar and screenLayout on the court*/
		MainWrap mainwrap = (MainWrap) getActivity().getFragmentManager().findFragmentById(R.id.MainWrap_frag);
		MainWrapScreen mainWrapScreen = (MainWrapScreen) getActivity().getFragmentManager().findFragmentById(R.id.MainWrap_screen);
		mainwrap.removeTextView(timelineIndex);
		mainwrap.removeScreenBar(timelineIndex);
		mainWrapScreen.removeScreenLayout();

		//移除player的record
		/*
		Log.d("undo","play's road:"+String.valueOf(undoPlayer.getCmpltRoad()));
		Log.d("undo","play's road start index:"+String.valueOf(runBags.get(runBags.size()-1).getRoadStart()));
		Log.d("undo","play's road end index:"+String.valueOf(runBags.get(runBags.size()-1).getRoadEnd()));
		*/
		undoPlayer.undoARoad(runBags.get(runBags.size()-1).getRoadEnd()-runBags.get(runBags.size()-1).getRoadStart()+1+1); //多一個1是因為要消掉每筆戰術之間的0
		undoDrawer.setStartIndex(runBags.get(runBags.size()-1).getRoadStart()-1);
		runBags.remove(runBags.size()-1);
		Log.d("undo","after undo play's road:"+String.valueOf(undoPlayer.getCmpltRoad()));

		//region screen layout
		//如果在screen layout出現時按undo 要讓view變回不透明
		setViewAlpha(255);
		enableViewOnTouch();
		//endregion
	}

	public void clearPaint(){//清除全部筆跡
		bitmapVector.clear();
		tempBitmap = Bitmap.createBitmap(circle.getWidth(),circle.getHeight(),Bitmap.Config.ARGB_8888);//初始化tempBitmap，指定大小為螢幕大小(大小同circle)
       	tempCanvas = new Canvas(tempBitmap);//畫透明路徑
   		tempCanvas.drawBitmap(tempBitmap, 0, 0, null);
   		circle.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));//把tempBitmap放進circle裡
		for(int i=0;i<5;i++){
			playerDrawers.get(i).clearCurve();
			defenderDrawers.get(i).clearCurve();
		}
		ballDrawer.clearCurve();
		//curves.clear();
	}
	
	public void clearRecord(){
		initialBallHolderId =0;
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
		timefrag.clearRecordLayout();
		
		MainWrap mainwrap = (MainWrap) getActivity().getFragmentManager().findFragmentById(R.id.MainWrap_frag);
		mainwrap.clearRecordLayout();

		/*remove pathnumber and screenbar and screenLayout on the court*/
		MainWrapScreen mainWrapScreen = (MainWrapScreen) getActivity().getFragmentManager().findFragmentById(R.id.MainWrap_screen);
		mainWrapScreen.removeScreenLayout();

		//region screen layout
		//如果在screen layout出現時按undo 要讓view變回不透明
		setViewAlpha(255);
		//view的ontouch
		enableViewOnTouch();
		//endregion
	}

	public void changePlayerToNoBall(){
		if(intersectId !=0){ //有球員持球
			ImageView currentImage = players.get(intersectId -1).image;
			ImageView currentArrow = players.get(intersectId -1).arrow;
			currentImage.setVisibility(currentImage.VISIBLE);
			//看起來還是有拿球的感覺
			if(intersect){
				currentImage.layout((int) playersWithBall.get(intersectId -1).getX()+30, (int) playersWithBall.get(intersectId -1).getY(),(int) playersWithBall.get(intersectId -1).getX()+30+currentImage.getWidth(), (int) playersWithBall.get(intersectId -1).getY()+currentImage.getHeight());
			}
			currentImage.invalidate();
			playersWithBall.get(intersectId -1).setVisibility(playersWithBall.get(intersectId -1).INVISIBLE);
			currentArrow.layout((int)currentImage.getX(), (int)currentImage.getY(), (int)currentImage.getX()+currentImage.getWidth(), (int)currentImage.getY()+currentImage.getHeight());
			currentArrow.invalidate();
		}
	}
	public void  movePlayersToInitialPosition(){
        /* 先把全部player移到按下錄製鍵時的位置 */
		for(int i=0 ; i<5 ; i++){
			if(players.get(i).initialPosition.x != -1){
				// Offenders
				Player thisPlayer = players.get(i);
				thisPlayer.image.layout(thisPlayer.initialPosition.x, thisPlayer.initialPosition.y, thisPlayer.initialPosition.x + thisPlayer.image.getWidth(), thisPlayer.initialPosition.y + thisPlayer.image.getHeight());
				thisPlayer.arrow.layout(thisPlayer.initialPosition.x, thisPlayer.initialPosition.y, thisPlayer.initialPosition.x + thisPlayer.arrow.getWidth(), thisPlayer.initialPosition.y + thisPlayer.arrow.getHeight());
				//thisPlayer.rect = new Rect(thisPlayer.initialPosition.x, thisPlayer.initialPosition.y, thisPlayer.initialPosition.x + thisPlayer.image.getWidth(), thisPlayer.initialPosition.y + thisPlayer.image.getHeight());
				//先註解
				//thisPlayer.setRect(thisPlayer.initialPosition.x, thisPlayer.initialPosition.y, thisPlayer.initialPosition.x + thisPlayer.image.getWidth(), thisPlayer.initialPosition.y + thisPlayer.image.getHeight());
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
			//ball.rect = new Rect(ball.initialPosition.x, ball.initialPosition.y, ball.initialPosition.x + ball.image.getWidth(), ball.initialPosition.y + ball.image.getHeight());
			//先註解
			//ball.setRect(ball.initialPosition.x, ball.initialPosition.y, ball.initialPosition.x + ball.image.getWidth(), ball.initialPosition.y + ball.image.getHeight());
		}
	}

	//region Store and restore Tactics
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
	        	//@TargetApi(Build.VERSION_CODES.M)
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
							JSONArray tmpArr = new JSONArray();
							//region Initial_Position
							for(int i=0;i<5;i++){
								tmpArr.put(players.get(i).initialPosition.x);
								tmpArr.put(players.get(i).initialPosition.y);
							}

							for(int i=0;i<5;i++){
								tmpArr.put(defenders.get(i).initialPosition.x);
								tmpArr.put(defenders.get(i).initialPosition.y);
							}

							tmpArr.put(ball.initialPosition.x);
							tmpArr.put(ball.initialPosition.y);

							save_strategy.put("Initial_Position", tmpArr);
							//endregion
							//region Initial_Rotation
							tmpArr = new JSONArray();
							for(int i=0;i<5;i++)
								tmpArr.put(players.get(i).initialRotation);
							for(int i=0;i<5;i++)
								tmpArr.put(defenders.get(i).initialRotation);

							save_strategy.put("Initial_Rotation", tmpArr);
							//endregion

							save_strategy.put("Initial_ball_holder", initialBallHolderId);
							save_strategy.put("Tactic_name", saveName);
							save_strategy.put("Category_id", selectCategoryId);

							//region Player road sequence
							tmpArr = new JSONArray();
							for(int i=0;i<ball.getRoadSize();i++){
								tmpArr.put( String.valueOf(ball.handleGetRoad(i) ));
							}
							if(tmpArr.length() > 0)
								save_strategy.put("B", tmpArr);
							for(int id=0;id<5;id++){
								tmpArr = new JSONArray();
								for(int i=0;i<players.get(id).getRoadSize();i++)
									tmpArr.put( String.valueOf(players.get(id).handleGetRoad(i)));
								if(tmpArr.length() > 0)
									save_strategy.put("P"+(id+1), tmpArr);
								tmpArr = new JSONArray();
								for(int i=0;i<defenders.get(id).getRoadSize();i++)
									tmpArr.put( String.valueOf(defenders.get(id).handleGetRoad(i)));
								if(tmpArr.length() > 0)
									save_strategy.put("D"+(id+1), tmpArr);
								tmpArr = new JSONArray();
								for(int i=0;i<players.get(id).getRotationSize();i++)
									tmpArr.put( String.valueOf(players.get(id).getMyRotation(i)));
								if(tmpArr.length() > 0)
									save_strategy.put("P"+(id+1)+"_Rotation", tmpArr);
								tmpArr = new JSONArray();
								for(int i=0;i<defenders.get(id).getRotationSize();i++)
									tmpArr.put( String.valueOf(defenders.get(id).getMyRotation(i)));
								if(tmpArr.length() > 0)
									save_strategy.put("D"+(id+1)+"_Rotation", tmpArr);
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
            } else if (tokens[1].length() == 7){
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
			initialBallHolderId = Integer.valueOf(saveTacticJson.getString("Initial_ball_holder"));

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

				runBags.add(tmp);

				//region 對應每讀一個Runbag，就會自動產生對應他順序的seekbar(用來調整時間點的bar條)

				timefrag.setSeekBarId(loadSeekbarTmpId);
				if(tmp.getHandler().equals("P1_Handle")){
					timefrag.createSeekbar(1,loadSeekbarTmpId,tmp.getStartTime(),tmp.getDuration());
					MainWrap mainwrapfrag = (MainWrap) getActivity().getFragmentManager().findFragmentById(R.id.MainWrap_frag);

					// 在player icon所在的位置產生對應的順序標記
					x = players.get(0).getCmpltRoad().get(tmp.getRoadEnd()-1);
					y = players.get(0).getCmpltRoad().get(tmp.getRoadEnd());
					mainwrapfrag.createPathNumberOnCourt(loadSeekbarTmpId+1, x, y,loadSeekbarTmpId);

					// 如果這一動有要擋拆
					if(tmp.getPathType() == 1){
						mainwrapfrag.createScreenBar(x, y, 1, tmp.getScreenAngle(), loadSeekbarTmpId);
					}
				} else if(tmp.getHandler().equals("P2_Handle")){
					timefrag.createSeekbar(2,loadSeekbarTmpId,tmp.getStartTime(),tmp.getDuration());
					MainWrap mainwrapfrag = (MainWrap) getActivity().getFragmentManager().findFragmentById(R.id.MainWrap_frag);
					x=players.get(1).getCmpltRoad().get(tmp.getRoadEnd()-1);
					y=players.get(1).getCmpltRoad().get(tmp.getRoadEnd());
					mainwrapfrag.createPathNumberOnCourt(loadSeekbarTmpId+1, x, y,loadSeekbarTmpId);

					if(tmp.getPathType() == 1){
						mainwrapfrag.createScreenBar(x, y, 2, tmp.getScreenAngle(), loadSeekbarTmpId);
					}
				} else if(tmp.getHandler().equals("P3_Handle")){
					timefrag.createSeekbar(3,loadSeekbarTmpId,tmp.getStartTime(),tmp.getDuration());
					MainWrap mainwrapfrag = (MainWrap) getActivity().getFragmentManager().findFragmentById(R.id.MainWrap_frag);
					x=players.get(2).getCmpltRoad().get(tmp.getRoadEnd()-1);
					y=players.get(2).getCmpltRoad().get(tmp.getRoadEnd());
					mainwrapfrag.createPathNumberOnCourt(loadSeekbarTmpId+1, x, y,loadSeekbarTmpId);

					if(tmp.getPathType() == 1){
						mainwrapfrag.createScreenBar(x, y, 3, tmp.getScreenAngle(), loadSeekbarTmpId);
					}
				} else if(tmp.getHandler().equals("P4_Handle")){
					timefrag.createSeekbar(4,loadSeekbarTmpId,tmp.getStartTime(),tmp.getDuration());
					MainWrap mainwrapfrag = (MainWrap) getActivity().getFragmentManager().findFragmentById(R.id.MainWrap_frag);
					x=players.get(3).getCmpltRoad().get(tmp.getRoadEnd()-1);
					y=players.get(3).getCmpltRoad().get(tmp.getRoadEnd());
					mainwrapfrag.createPathNumberOnCourt(loadSeekbarTmpId+1, x, y,loadSeekbarTmpId);

					if(tmp.getPathType() == 1){
						mainwrapfrag.createScreenBar(x, y, 3, tmp.getScreenAngle(), loadSeekbarTmpId);
					}
				} else if(tmp.getHandler().equals("P5_Handle")){
					timefrag.createSeekbar(5,loadSeekbarTmpId,tmp.getStartTime(),tmp.getDuration());
					MainWrap mainwrapfrag = (MainWrap) getActivity().getFragmentManager().findFragmentById(R.id.MainWrap_frag);
					x=players.get(4).getCmpltRoad().get(tmp.getRoadEnd()-1);
					y=players.get(4).getCmpltRoad().get(tmp.getRoadEnd());
					mainwrapfrag.createPathNumberOnCourt(loadSeekbarTmpId+1, x, y,loadSeekbarTmpId);

					if(tmp.getPathType() == 1){
						mainwrapfrag.createScreenBar(x, y, 4, tmp.getScreenAngle(), loadSeekbarTmpId);
					}
				} else if(tmp.getHandler().equals("B_Handle")){
					timefrag.createSeekbar(6,loadSeekbarTmpId,tmp.getStartTime(),tmp.getDuration());
					MainWrap mainwrapfrag = (MainWrap) getActivity().getFragmentManager().findFragmentById(R.id.MainWrap_frag);
					x=ball.getCmpltRoad().get(tmp.getRoadEnd()-1);
					y=ball.getCmpltRoad().get(tmp.getRoadEnd());
					mainwrapfrag.createPathNumberOnCourt(loadSeekbarTmpId+1, x, y,loadSeekbarTmpId);
				} else if(tmp.getHandler().equals("D1_Handle")){
					timefrag.createSeekbar(7,loadSeekbarTmpId,tmp.getStartTime(),tmp.getDuration());
					MainWrap mainwrapfrag = (MainWrap) getActivity().getFragmentManager().findFragmentById(R.id.MainWrap_frag);
					x=defenders.get(0).getCmpltRoad().get(tmp.getRoadEnd()-1);
					y=defenders.get(0).getCmpltRoad().get(tmp.getRoadEnd());
					mainwrapfrag.createPathNumberOnCourt(loadSeekbarTmpId+1, x, y,loadSeekbarTmpId);
				} else if(tmp.getHandler().equals("D2_Handle")){
					timefrag.createSeekbar(8,loadSeekbarTmpId,tmp.getStartTime(),tmp.getDuration());
					MainWrap mainwrapfrag = (MainWrap) getActivity().getFragmentManager().findFragmentById(R.id.MainWrap_frag);
					x=defenders.get(1).getCmpltRoad().get(tmp.getRoadEnd()-1);
					y=defenders.get(1).getCmpltRoad().get(tmp.getRoadEnd());
					mainwrapfrag.createPathNumberOnCourt(loadSeekbarTmpId+1, x, y,loadSeekbarTmpId);
				} else if(tmp.getHandler().equals("D3_Handle")){
					timefrag.createSeekbar(9,loadSeekbarTmpId,tmp.getStartTime(),tmp.getDuration());
					MainWrap mainwrapfrag = (MainWrap) getActivity().getFragmentManager().findFragmentById(R.id.MainWrap_frag);
					x=defenders.get(2).getCmpltRoad().get(tmp.getRoadEnd()-1);
					y=defenders.get(2).getCmpltRoad().get(tmp.getRoadEnd());
					mainwrapfrag.createPathNumberOnCourt(loadSeekbarTmpId+1, x, y,loadSeekbarTmpId);
				} else if(tmp.getHandler().equals("D4_Handle")){
					timefrag.createSeekbar(10,loadSeekbarTmpId,tmp.getStartTime(),tmp.getDuration());
					MainWrap mainwrapfrag = (MainWrap) getActivity().getFragmentManager().findFragmentById(R.id.MainWrap_frag);
					x=defenders.get(3).getCmpltRoad().get(tmp.getRoadEnd()-1);
					y=defenders.get(3).getCmpltRoad().get(tmp.getRoadEnd());
					mainwrapfrag.createPathNumberOnCourt(loadSeekbarTmpId+1, x, y,loadSeekbarTmpId);
				} else if(tmp.getHandler().equals("D5_Handle")){
					timefrag.createSeekbar(11,loadSeekbarTmpId,tmp.getStartTime(),tmp.getDuration());
					MainWrap mainwrapfrag = (MainWrap) getActivity().getFragmentManager().findFragmentById(R.id.MainWrap_frag);
					x=defenders.get(4).getCmpltRoad().get(tmp.getRoadEnd()-1);
					y=defenders.get(4).getCmpltRoad().get(tmp.getRoadEnd());
					mainwrapfrag.createPathNumberOnCourt(loadSeekbarTmpId+1, x, y,loadSeekbarTmpId);
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

		//讀取戰術後把戰術跑動路徑也畫上去

		//endregion

		movePlayersToInitialPosition();
	}
	/**************************************************************************/
	//endregion

	public void rotatePlayer(int input){
		if(rotateWhichPlayer == 6){
			ballDrawer.rotation = 0;
		} else if(rotateWhichPlayer < 6){
			int id = rotateWhichPlayer-1;
			playerDrawers.get(id).rotation = input;
			currentDrawer.rotation =input;
			players.get(id).arrow.setRotation(input);
			if(isRecording ==false && runBags.size()==0)
				players.get(id).initialRotation = input;
		} else if(rotateWhichPlayer > 6){
			int id = rotateWhichPlayer-7;
			defenderDrawers.get(id).rotation = input;
			currentDrawer.rotation =input;
			defenders.get(id).arrow.setRotation(input);
			if(isRecording ==false && runBags.size()==0)
				defenders.get(4).initialRotation = input;
		}
	}

	public void playButton() {
		if (runBags.isEmpty()) {
			Log.d("play button","empty!");
		} else {
			TimeLine timefrag = (TimeLine) getActivity().getFragmentManager().findFragmentById(R.id.time_line);
			//取得現在軌跡在timeline的最大起始值
			final int maxTimePoint = timefrag.getsetSeekBarProgressLow() * 1000;
			/*先把全部player移到按下錄製鍵時的位置*/
			movePlayersToInitialPosition();
			//將軌跡畫布清空
			tempBitmap = Bitmap.createBitmap(circle.getWidth(),circle.getHeight(),Bitmap.Config.ARGB_8888);
			tempCanvas = new Canvas(tempBitmap);
			circle.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
			Log.d("play button","ok!");
			//////////////////////////////////////// Time counter///////////////////////////////////////
			new Thread(new Runnable() {// Time counter count on per second
				@Override
				public void run() {
					int time = 0;
					int RunLineSize = runBags.size();
					while (time <= maxTimePoint && IsTacticPlaying == 1) {
						try {
							Log.e("thread time = ", String.valueOf(time));
							// do RunLine here!!
							////// check each road's start time in
							// RunLine///////
							checkRunLine(time, RunLineSize);
							hasInvokeCurrentTimeDefender = false;  ////what?
							Thread.sleep(1000);
							time = time + 1000;

						} catch (Throwable t) {
						}
					}
					/**TODO: for reset 持球者**/
					withBall.sendEmptyMessage(0);
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
				while (i < RunLineSize && IsTacticPlaying ==1) { //check此時間點是不是某筆戰術的起始時間 (可能有多筆，所以所有i(<runlinesize)都要呼叫)
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

	/** region Message Handlers
	* 在UI上用handler更新
	**/

	//play button後呼叫 主要重新調整持球者狀態-->player to playwithball icon
	Handler withBall =new Handler(){
		@Override
		public void handleMessage(Message msg){
			if(intersectId==0){
				return;
			}

			int whoWithBall=intersectId-1;
			if (playersWithBall.get(whoWithBall).getVisibility() == playersWithBall.get(whoWithBall).INVISIBLE) {
				players.get(whoWithBall).image.setVisibility(players.get(whoWithBall).image.INVISIBLE);
				playersWithBall.get(whoWithBall).layout((int) players.get(whoWithBall).image.getX() - 30, (int) players.get(whoWithBall).image.getY(), (int) players.get(whoWithBall).image.getX() - 30 + 200, (int) players.get(whoWithBall).image.getY() + 120);
				playersWithBall.get(whoWithBall).setVisibility(playersWithBall.get(whoWithBall).VISIBLE);
				players.get(whoWithBall).arrow.layout((int) players.get(whoWithBall).image.getX() - 30, (int) players.get(whoWithBall).image.getY(), (int) players.get(whoWithBall).image.getX() - 30 + 200, (int) players.get(whoWithBall).image.getY() + 120);
				Log.d("play","adjust withBallPlayer status: player "+(whoWithBall+1));
			}
			ball.image.layout((int) players.get(whoWithBall).image.getX()+110-30, (int)players.get(whoWithBall).image.getY()+30, (int) players.get(whoWithBall).image.getX()+110-30+ball.image.getWidth(), (int)players.get(whoWithBall).image.getY()+30+ball.image.getHeight());
		}
	};

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

			//讓球放在player圖片旁邊(不是withBallPlayer)
			if(isDribble) {
				ball.image.layout(players.get(id).handleGetRoad(sentInt) + 70, players.get(id).handleGetRoad(sentInt + 1) + 30,
						players.get(id).handleGetRoad(sentInt) + 70 + ball.image.getWidth(),
						players.get(id).handleGetRoad(sentInt + 1) + 30 + ball.image.getHeight());
			}
			//endregion
			players.get(id).arrow.layout(players.get(id).handleGetRoad(sentInt), players.get(id).handleGetRoad(sentInt + 1),
					players.get(id).handleGetRoad(sentInt) + players.get(id).arrow.getWidth(),
					players.get(id).handleGetRoad(sentInt + 1) + players.get(id).arrow.getHeight());

			if(sentInt!=1) {
				players.get(id).arrow.setRotation(players.get(id).getMyRotation(sentInt / 2));
			}else{
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

				//畫上此動軌跡
				/**
				 * todo:調整軌跡深淺?
				 */
				tempCanvas.drawBitmap(bitmapVector.get(runBags.get(sentI).getPathIndex()),0,0,null);

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
					//如果這動是球的話,ID要傳入最後球在誰手上,播放時會稍微修正每動最後的位置
					play(runBags.get(sentI).getBallNum(), isDribble, runBags.get(sentI).getRate(), B_Handle, runBags.get(sentI).getRoadStart(),
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

	private OnTouchListener reactForNotScreen = new OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			MainWrapScreen mainWrapScreen = (MainWrapScreen) getActivity().getFragmentManager().findFragmentById(R.id.MainWrap_screen);
			ButtonDraw mainButton = (ButtonDraw) getActivity().getFragmentManager().findFragmentById(R.id.ButtonDraw);
			int[] margin = mainWrapScreen.getScreenLayoutPosition();
			if(!(motionEvent.getX()>margin[0]&&motionEvent.getX()<margin[2]&&motionEvent.getY()>margin[1]&&motionEvent.getY()<margin[3])){
				mainWrapScreen.removeScreenLayout();
				setViewAlpha(255);
				if(originalIsTimelineShow==true&&mainButton.getisTimelineShow()==false) mainButton.getTimeLineView().performClick(); //跑出timeline
				circle.setOnTouchListener(null);
			}
			return false;
		}
	};

	public void setReactForNotScreenFail(){
		circle.setOnTouchListener(null);
	}

	//在user調好screen bar角度後 設定runbag擋人資訊
	public void setRunBagScreen(float direction){
		Log.d("check screen",runBags.get(runBags.size()-1).getHandler());
		//Log.d("check screen","ORIGINAL:"+Integer.valueOf(runBags.get(runBags.size()-1).getPathType()));
		runBags.get(runBags.size()-1).setPathType(1);
		runBags.get(runBags.size()-1).setScreenAngle(direction);
		//Log.d("check screen","After setting:"+Integer.valueOf(runBags.get(runBags.size()-1).getPathType()));
	}

	//拿掉or復原10個球員和球view的touch listener
	public void disableViewOnTouch(){ ;
		disableOntouch=true;
	}

	public void enableViewOnTouch(){
		disableOntouch=false;
	}

	/* When the icon of the player is touched. */
	private OnTouchListener playerListener = new OnTouchListener() {
		private int mx, my; // 圖片被拖曳的X ,Y軸距離長度
		private int startX, startY; // 原本圖片存在的X,Y軸位置
		private int x, y; // 最終的顯示位置
		private int move_count;
		private boolean dum_flag;
		private Bitmap Dbitmap; //每次畫的暫存bitmap
		private DrawCanvas Dcanvas;
		private Player currentPlayer;
		private int seekbar_player_Id;
		String handle_name = new String();

		@Override
		public boolean onTouch(View v, MotionEvent event) { // v是事件來源物件, e是儲存有觸控資訊的物件
			if(disableOntouch==true) return false; //return false 就不會走後面的action move跟up
			//Log.d("move intersect","touch");

			int id = Integer.parseInt(v.getTag().toString());
			TimeLine timefrag = (TimeLine) getActivity().getFragmentManager().findFragmentById(R.id.time_line);
			MainWrap mainwrapfrag = (MainWrap) getActivity().getFragmentManager().findFragmentById(R.id.MainWrap_frag);
			ButtonDraw mainButton= (ButtonDraw) getActivity().getFragmentManager().findFragmentById(R.id.ButtonDraw);

			mx = (int) (event.getRawX()); //get相對於屏幕邊界的距離  getx()是相對於自己元件的邊界距離
			my = (int) (event.getRawY());

			switch (event.getAction()) { // 判斷觸控的動作
			case MotionEvent.ACTION_DOWN:// 按下圖片時

				if(id == 6){ //ball
					currentPlayer = ball;
					rotateWhichPlayer = 6;    /////////////////// what
					currentDrawer.curveIndex = ballDrawer.curveIndex;
					currentDrawer.tempCurve = ballDrawer.tempCurve;
					currentDrawer.paint = ballDrawer.paint;
					currentDrawer.startIndex = ballDrawer.startIndex;
					handle_name = "B_Handle";
					seekbar_player_Id = 6; ////////////////////////////////
					//ball.rect = new Rect((int) event.getX(),my - v.getTop(),(int) event.getX()+ v.getWidth(),my - v.getTop()+v.getHeight());
					ball.setRect((int) event.getX(),my - v.getTop(),(int) event.getX()+ v.getWidth(),my - v.getTop()+v.getHeight());
					ball.image.startAnimation(zoomAnimation);

					timefrag.changeLayout(6);
				} else if(id < 6){ //player進攻者(藍色衣服)
					currentPlayer = players.get(id-1);
					currentDrawer.curveIndex = playerDrawers.get(id-1).curveIndex;
					currentDrawer.tempCurve = playerDrawers.get(id-1).tempCurve;
					currentDrawer.rotation = playerDrawers.get(id-1).rotation;
					currentDrawer.paint = playerDrawers.get(id-1).paint;
					currentDrawer.startIndex = playerDrawers.get(id-1).startIndex;
					rotateWhichPlayer = id;
					handle_name = "P"+Integer.toString(id)+"_Handle";
					seekbar_player_Id = id;
					//players.get(id-1).rect = new Rect((int) event.getX(),my - v.getTop(),(int) event.getX()+ v.getWidth(),my - v.getTop()+v.getHeight())

					if(undoAlert!=true){ //遇到undo alert時，在還沒undo之前沒辦法修改其他player icon的rect
						players.get(id-1).setRect((int) event.getX(),my - v.getTop(),(int) event.getX()+ v.getWidth(),my - v.getTop()+v.getHeight());
					}

					timefrag.changeLayout(id);

					if(intersectId==id){ //user移動的是帶球者
						ball.image.startAnimation(zoomAnimationDribbleBall);
						playersWithBall.get(id-1).startAnimation(zoomAnimation);
					}else{ //無持球者
						currentPlayer.image.startAnimation(zoomAnimation);
					}

				} else if(id > 6){ //defender(黃色衣服)
					currentPlayer = defenders.get(id-7);
					currentDrawer.curveIndex = defenderDrawers.get(id-7).curveIndex;
					currentDrawer.tempCurve = defenderDrawers.get(id-7).tempCurve;
					currentDrawer.rotation = defenderDrawers.get(id-7).rotation;
					currentDrawer.paint= defenderDrawers.get(id-7).paint;
					currentDrawer.startIndex= defenderDrawers.get(id-7).startIndex;
					rotateWhichPlayer =id;
					handle_name = "D"+Integer.toString(id-6)+"_Handle";
					seekbar_player_Id=id;
					//Log,i("debug", "first    P_startIndex="+Integer.toString(P_startIndex));
					//Log,i("debug", "first    D1_startIndex="+Integer.toString(D1_startIndex));
					timefrag.changeLayout(id);
					currentPlayer.image.startAnimation(zoomAnimation);
				} else{
					Log.d("error", "playerListener -> MotionEvent.ACTION_DOWN: Wrong selection");
				}

				//如果球在地上，會彈出alertDialog指示user
				if(intersectId==0&&isRecording==true){
					if(runBags.size()>0) {
						if (runBags.get(runBags.size() - 1).getHandler() == "B_Handle") {// 上一動移動球到地上，下一動還想要繼續換戰術的話，會強制undo
							undoAlert = true;
							showAlertDialogButton("ball_in_court");
							return false;
						}
					}

					if(id==6){// 球還沒被放上球場，強制user要畫球之前，要先按unrecording
						showAlertDialogButton("ball_out_of_court");
						return false;
					}
				}
				undoAlert=false;

				startTime = System.currentTimeMillis();
				move_count = 1;
				dum_flag = false; ////////////////////////////

				startX = (int) event.getX();
				startY = my - v.getTop();
				if (isRecording == true) {
					currentPlayer.setRoad(0); // split positions
					currentPlayer.setMyRotation(-1);
					Dbitmap = Bitmap.createBitmap(circle.getWidth(),circle.getHeight(),Bitmap.Config.ARGB_8888);//初始化tempBitmap，指定大小為螢幕大小(大小同circle)
					Dcanvas = new DrawCanvas();
					Dcanvas.canvas = new Canvas(Dbitmap);
				}
				return true;

			/*移動圖片***************************************************************************************************/
			case MotionEvent.ACTION_MOVE:// 移動圖片時
				//圖片的左上角座標
				x = mx - startX;
				y = my - startY;
				//if(isAlert==true&&id2==6) return true; //alert dialog when passing ball
				if(id < 6) {
					//players.get(id2-1).rect =new Rect(x,y,x+ v.getWidth(),y+v.getHeight());
					players.get(id - 1).setRect(x, y, x + v.getWidth(), y + v.getHeight());
				} else if (id == 6){ //人會吸住球的部分(球移動)
					ball.setRect(x,y,x+ v.getWidth(),y+v.getHeight());
					/*這裡是先把目前持有球的player先變回無持球狀態，再接著判斷有沒有intersect，確保當兩個player太接近的時候，會導致player明明無持球卻還是呈現有持球的狀態*/
					changePlayerToNoBall();

					//region 判斷是跟哪個player intersect
					if(Rect.intersects(players.get(0).getRect(), ball.getRect())){
						intersectId =1;
						intersect=true;
						//持球圖片的設定
						if(playersWithBall.get(0).getVisibility()== playersWithBall.get(0).INVISIBLE){ //VISIBLE 0 //INVISIBILITY 4
							players.get(0).image.setVisibility(players.get(0).image.INVISIBLE);
							playersWithBall.get(0).layout((int)players.get(0).image.getX()-30, (int)players.get(0).image.getY(), (int)players.get(0).image.getX()-30+200, (int)players.get(0).image.getY()+120);
							playersWithBall.get(0).setVisibility(playersWithBall.get(0).VISIBLE);
						}
					} else if (Rect.intersects(players.get(1).getRect(), ball.getRect())){
						intersectId =2;
						intersect=true;
						if(playersWithBall.get(1).getVisibility()== playersWithBall.get(1).INVISIBLE){
							players.get(1).image.setVisibility(players.get(1).image.INVISIBLE);
							playersWithBall.get(1).layout((int)players.get(1).image.getX()-30, (int)players.get(1).image.getY(), (int)players.get(1).image.getX()-30+200, (int)players.get(1).image.getY()+120);
							playersWithBall.get(1).setVisibility(playersWithBall.get(1).VISIBLE);
						}
					} else if (Rect.intersects(players.get(2).getRect(), ball.getRect())){
						intersectId =3;
						intersect=true;
						if(playersWithBall.get(2).getVisibility()== playersWithBall.get(2).INVISIBLE){
							players.get(2).image.setVisibility(players.get(2).image.INVISIBLE);
							playersWithBall.get(2).layout((int)players.get(2).image.getX()-30, (int)players.get(2).image.getY(), (int)players.get(2).image.getX()-30+200, (int)players.get(2).image.getY()+120);
							playersWithBall.get(2).setVisibility(playersWithBall.get(2).VISIBLE);
						}
					} else if (Rect.intersects(players.get(3).getRect(), ball.getRect())){
						intersectId =4;
						intersect=true;
						if(playersWithBall.get(3).getVisibility()== playersWithBall.get(3).INVISIBLE){
							players.get(3).image.setVisibility(players.get(3).image.INVISIBLE);
							playersWithBall.get(3).layout((int)players.get(3).image.getX()-30, (int)players.get(3).image.getY(), (int)players.get(3).image.getX()-30+200, (int)players.get(3).image.getY()+120);
							playersWithBall.get(3).setVisibility(playersWithBall.get(3).VISIBLE);
						}
					} else if (Rect.intersects(players.get(4).getRect(), ball.getRect())){
						intersectId =5;
						intersect=true;
						if(playersWithBall.get(4).getVisibility()== playersWithBall.get(4).INVISIBLE){ //setvisibility會呼叫invalidate
							players.get(4).image.setVisibility(players.get(4).image.INVISIBLE);
							playersWithBall.get(4).layout((int)players.get(4).image.getX()-30, (int)players.get(4).image.getY(), (int)players.get(4).image.getX()-30+200, (int)players.get(4).image.getY()+120);
							playersWithBall.get(4).setVisibility(playersWithBall.get(4).VISIBLE);
						}
					} else{
						//球在移動中 沒有在任何人手上的狀況
						intersect=false;
						intersectId = 0;
					}
					//endregion
					actionMovePreIntersectId=intersectId;
					//Log.d("preintersect",String.valueOf(actionMovePreIntersectId));
				}

				//region 當正在繪製軌跡時
				if (isRecording == true) {
					move_count += 2; ////////////為甚麼是2 XY
					currentPlayer.setRoad(x);
					currentPlayer.setRoad(y);
					currentPlayer.setMyRotation(currentDrawer.rotation);
					currentDrawer.tempCurve.add(currentDrawer.curveIndex, new Point(x, y)); //畫畫時會調到中間
					currentDrawer.curveIndex++;

					// 每畫三個點
					if(currentDrawer.curveIndex == N) { //N為3
						//Log.i("debug", "Touch Event : " + rotateWhichPlayer + ", " + intersectId);

						//將point調整到圖片的正中心
						tempCurve = currentDrawer.tempCurve;
						for (int k = 0; k< tempCurve.size(); k++){
							tempCurve.get(k).x+=v.getWidth()/2;
							tempCurve.get(k).y+=v.getHeight()/2;
						}
						Dcanvas.drawCurvePath(tempCurve, currentDrawer.paint, false);
						tempCanvas.drawBitmap(Dbitmap, 0, 0, null); //Dbitmap 一定要放 user才會邊畫邊看到線條

						currentDrawer.clearCurve();
						currentDrawer.tempCurve.add(currentDrawer.curveIndex, new Point(x, y));
						currentDrawer.curveIndex++;
					}
					//tempCanvas.drawBitmap(Dbitmap, 0, 0, null); //Dbitmap 一定要放 user才會邊畫邊看到線條
				}
				//endregion

				//讓arrow黏住人
				if(currentPlayer.arrow != null){
					currentPlayer.arrow.layout(x, y, x + v.getWidth(), y + v.getHeight());
					currentPlayer.arrow.setRotation(currentDrawer.rotation);
					TimeLine timefrag1 = (TimeLine) getActivity().getFragmentManager().findFragmentById(R.id.time_line);
					timefrag1.setCircularSeekBarProgress(currentDrawer.rotation);//為了讓circular seekbar的值也一起變成儲存的狀態，但是因為android好像有bug，所以他不會更新介面上的seekbar的樣子，但值卻是有改過的
					currentPlayer.arrow.postInvalidate();
				}

				//讓球黏住人
				if(intersect==true && v.getTag().toString().equals("6")==false && v.getTag().toString().equals(Integer.toString(intersectId))){
					//ball.image.layout(x+110, y+30, x+170, y+90);
					ball.image.layout(x+110,y+30, x+110+ball.image.getWidth(),y+30+ball.image.getHeight());
				}
				v.layout(x, y, x + v.getWidth(), y + v.getHeight());
				v.postInvalidate();
				return true;

				/*放開圖片***************************************************************************************************/
			case MotionEvent.ACTION_UP:
				Log.i("debug", "intersect_name_pre="+Integer.toString(preIntersectId));
				Log.i("debug", "intersect_name="+Integer.toString(intersectId));

				//if(isAlert==true) return false; //球噴在地上時不能畫軌跡
				//if(isAlert==true&&v.getTag().toString().equals("6")) return true; //alert dialog when passing ball
				// region 調整圖片顯示球的狀態
				if(v.getTag().toString().equals("6") && intersect==true){
					if(intersectId > 0){
						if(players.get(intersectId -1).image.getVisibility()==players.get(intersectId -1).image.INVISIBLE){//代表是player1_ball顯示中
							players.get(intersectId -1).arrow.layout((int) playersWithBall.get(intersectId -1).getX(), (int) playersWithBall.get(intersectId -1).getY(), (int) playersWithBall.get(intersectId -1).getX()+ playersWithBall.get(intersectId -1).getWidth(), (int) playersWithBall.get(intersectId -1).getY()+ playersWithBall.get(intersectId -1).getHeight());
						}
						players.get(intersectId -1).arrow.invalidate();
						//v.layout((int) players.get(intersectId -1).image.getX()+110-30, (int)players.get(intersectId -1).image.getY()+30, (int) players.get(intersectId -1).image.getX()+170-30,(int)players.get(intersectId -1).image.getY()+90);
						v.layout((int) players.get(intersectId -1).image.getX()+110-30, (int)players.get(intersectId -1).image.getY()+30, (int) players.get(intersectId -1).image.getX()+110-30+v.getWidth(), (int)players.get(intersectId -1).image.getY()+30+v.getHeight());
						v.invalidate();
					}
				} else if(v.getTag().toString().equals("6") && intersect == false){
					if(intersectId > 0){
						if(players.get(intersectId -1).image.getVisibility() == players.get(intersectId -1).image.VISIBLE){
							players.get(intersectId -1).arrow.layout((int)players.get(intersectId -1).image.getX(), (int)players.get(intersectId -1).image.getY(), (int)players.get(intersectId -1).image.getX()+players.get(intersectId -1).image.getWidth(), (int)players.get(intersectId -1).image.getY()+players.get(intersectId -1).image.getHeight());
						}
						players.get(intersectId -1).arrow.invalidate();
					}
				}
				//endregion

				//region 防呆，畫的時間太短的話，不會採用
				if (isRecording == true) {

					// 移動距離太短或是時間太短的話不會採用
					endTime=System.currentTimeMillis();

					if(endTime-startTime>150){
						//先看時間再看距離(因為時間太短sample到的點就不夠了-->會bang)
						int drawStartIndex=currentPlayer.findLastValueIndex(0);
						int drawEndIndex=currentPlayer.getRoadSize();
						Point drawEnd= new Point(currentPlayer.handleGetRoad( drawEndIndex- 2), currentPlayer.handleGetRoad(drawEndIndex-1));
						Point drawStart= new Point(currentPlayer.handleGetRoad(drawStartIndex+1), currentPlayer.handleGetRoad(drawStartIndex+2));

						double Dis= Math.sqrt(Math.pow(drawEnd.x-drawStart.x,2)+Math.pow(drawStart.y-drawEnd.y,2));
						Log.d("not draw","Distance of drawing:"+String.valueOf(Dis));

						if(Dis>50) dum_flag=true;
						else dum_flag=false;
					} else{ //這一筆畫畫不會採計
						dum_flag=false;
					}

					// undo回上一動原本user畫的曲線(筆觸太短的防呆或是正常畫畫都要這步)
					tempBitmap = Bitmap.createBitmap(circle.getWidth(),circle.getHeight(),Bitmap.Config.ARGB_8888);//初始化tempBitmap，指定大小為螢幕大小(大小同circle)
					tempCanvas = new Canvas();
					tempCanvas = new Canvas(tempBitmap);//畫透明路徑

					for(int tmp = 0; tmp< bitmapVector.size(); tmp++){
						tempCanvas.drawBitmap(bitmapVector.get(tmp), 0, 0, null);
					}
					//bitmap--->drawble
					circle.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));//把tempBitmap放進circle裡

					boolean isBallHolder = (rotateWhichPlayer == intersectId)&&(handle_name.charAt(0)=='P'); //是進攻者
					if(dum_flag == true){

						int B_start_index=currentPlayer.findLastValueIndex(0);
						int B_end_index=currentPlayer.getRoadSize();

						// region 重新畫畫

						//做新的Dbitmap
						Dbitmap = Bitmap.createBitmap(circle.getWidth(),circle.getHeight(),Bitmap.Config.ARGB_8888);
						Dcanvas = new DrawCanvas();
						Dcanvas.canvas = new Canvas(Dbitmap);

						// region 重劃筆觸軌跡
						//利用起終點重畫球的虛線
						if(handle_name.equals("B_Handle")){
							// get point
							Point B_start_point= new Point(currentPlayer.handleGetRoad(B_start_index+1),currentPlayer.handleGetRoad(B_start_index+2));
							Point B_end_point= new Point(currentPlayer.handleGetRoad(B_end_index-8),currentPlayer.handleGetRoad(B_end_index-7)); //////不畫最後2點

							//將point調整到圖片的正中心
							B_end_point.x+=v.getWidth()/2;
							B_start_point.x+=v.getWidth()/2;
							B_end_point.y+=v.getHeight()/2;
							B_start_point.y+=v.getHeight()/2;
							Dcanvas.drawStraightLine(B_start_point,B_end_point,currentDrawer.paint);
						} else if(isBallHolder){
							Vector<Point> tempPoints= new Vector<>();

							//拿一整段曲線的點 B_start_index是0(路線分隔)，所以要加1
							for (int i=B_start_index+1; i<B_end_index-7; i+=2){//////不畫最後3點
								//將point調整到圖片的正中心
								tempPoints.add(new Point(currentPlayer.handleGetRoad(i)+v.getWidth()/2,currentPlayer.handleGetRoad(i+1)+v.getHeight()/2));
							}
							ballDrawer.clearCurve(); //把之前球的位置清掉
							Dcanvas.drawZigzag(tempPoints,currentDrawer.paint);
						} else{ //重劃曲線
							Vector<Point> tempPoints= new Vector<>();
							int count=0;
							//拿一整段曲線的點 B_start_index是0(路線分隔)，所以要加1
							for (int i=B_start_index+1; i<B_end_index-7; i+=2){//////不畫最後3點
								//將point調整到圖片的正中心
								tempPoints.add(new Point(currentPlayer.handleGetRoad(i)+v.getWidth()/2,currentPlayer.handleGetRoad(i+1)+v.getHeight()/2));
								count++;
								if(count==3){
									Dcanvas.drawCurvePath(tempPoints,currentDrawer.paint,true);
									//Log.d("draw","draw curve"+String.valueOf(i));
									tempPoints.clear();
									tempPoints.add(new Point(currentPlayer.handleGetRoad(i)+v.getWidth()/2,currentPlayer.handleGetRoad(i+1)+v.getHeight()/2));
									count=1;
								}
							}
						}
						// endregion

						// region 畫箭頭
						// 拿sample到的最後一個點跟倒數第五個點
						Point B_end_point = new Point(currentPlayer.handleGetRoad(B_end_index - 8), currentPlayer.handleGetRoad(B_end_index - 7)); //倒數4 (不畫最後3點)
						Point B_start_point = new Point(currentPlayer.handleGetRoad(B_end_index - 12), currentPlayer.handleGetRoad(B_end_index - 11)); //倒數6

						if(handle_name.equals("B_Handle")){ //調整一下讓傳球線的箭頭不要太歪(畫直線)
							B_start_point= new Point(currentPlayer.handleGetRoad(B_start_index+1),currentPlayer.handleGetRoad(B_start_index+2));
						}

						// 將point調整到圖片的正中心
						B_end_point.x += v.getWidth() / 2;
						B_start_point.x += v.getWidth() / 2;
						B_end_point.y += v.getHeight() / 2;
						B_start_point.y += v.getHeight() / 2;

						Dcanvas.drawArrow(B_end_point,B_start_point,currentDrawer.paint);
						tempCanvas.drawBitmap(Dbitmap, 0, 0, null); // 將這次user畫的bitmap畫到global的tempbitmap
						// endregion

						// endregion

						//region 跳出問 無球跑動的人 是不是擋人的dynamic layout
						if(!isBallHolder&&(handle_name.charAt(0)=='P')){
							originalIsTimelineShow=mainButton.getisTimelineShow();
							circle.setOnTouchListener(reactForNotScreen); //如果碰UI其他位置 會讓screen layout消失
							MainWrapScreen mainScreen = (MainWrapScreen) getActivity().getFragmentManager().findFragmentById(R.id.MainWrap_screen);
							mainScreen.createIsScreenLayout(x,y,rotateWhichPlayer,timelineIndex); /****/
						}
						//end region

						// 很重要 存每一動的Dbitmap
						bitmapVector.add(Dbitmap);
						int startIndexTmp = currentDrawer.startIndex;

						// 存每一動的戰術資訊(runbag)
						while (currentDrawer.startIndex != -1) {
							RunBag tmp = new RunBag();
							tmp.setStartTime(seekBarCallbackStartTime);
							tmp.setHandler(handle_name);
							tmp.setRoadStart(currentDrawer.startIndex + 1); //避掉0
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
							tmp.setTimeLineId(timelineIndex); /**需要不重複的數**/
							tmp.setBallNum(intersectId);
							tmp.setPathIndex(bitmapVector.size()-1);

							//region 20180712 在Runbag中加入掩護的資訊
							// 20210107 如果是擋人的話 會在按下screen layout confirm button時再去runbag更改
							if(isBallHolder){
								tmp.setPathType(2);
							}else{
								tmp.setPathType(0);
							}
							//endregion

							runBags.add(tmp);
							timefrag.setRunlineId(runBags.size()-1);
							//timefrag.setSeekBarId(runBags.size()-1);
							timefrag.setSeekBarId(timelineIndex);

							seekbarTmpId++;
							timefrag.setSeekBarProgressLow(mainFragSeekBarProgressLow);
							mainFragSeekBarProgressLow++;
							timefrag.createSeekbar(seekbar_player_Id); //在誰的timeline區塊增加

							///call MainWrap 's function
							mainwrapfrag.createPathNumberOnCourt(runBags.size(), x, y, timelineIndex);

							timelineIndex++;
						}
						currentDrawer.startIndex = startIndexTmp + 1;
					}else{

						Log.d("debug", "dum_flag=falseeeeeeeee");
						for(int i=0;i<move_count;i++){
							currentPlayer.getCmpltRoad().remove(currentPlayer.getRoadSize()-1);
						}

						//把圖片放回之前有效軌跡的末端  --> current player起點
						currentDrawer.clearCurve(); //清空軌跡裡的點
						ImageView playWithBall=null; //場上還沒有球然後移動人
						if(isBallHolder){
							playWithBall=playersWithBall.get(intersectId-1);
						}else if(preIntersectId!=0){ //移動球
							playWithBall=playersWithBall.get(preIntersectId-1);
						}
						backToLastPosition(currentPlayer,isBallHolder,playWithBall);

					}
				}
				else{ //還沒record時 紀錄user放人物的位置
					if(runBags.size()==0){ //預設user會放好所有物件後才開始畫畫
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
				} else if(id3 < 6){
					players.set(id3-1, currentPlayer);
					playerDrawers.get(id3-1).curveIndex = currentDrawer.curveIndex;
					playerDrawers.get(id3-1).tempCurve = currentDrawer.tempCurve;
					playerDrawers.get(id3-1).rotation = currentDrawer.rotation;
					playerDrawers.get(id3-1).startIndex =currentDrawer.startIndex;
					handle_name="P" + String.valueOf(id3) + "_Handle";
					players.get(id3-1).initialPosition = currentPlayer.initialPosition;
					seekbar_player_Id=1;
				} else if(id3 > 6){
					defenders.set(id3-7, currentPlayer);
					defenderDrawers.get(id3-7).curveIndex = currentDrawer.curveIndex;
					defenderDrawers.get(id3-7).tempCurve = currentDrawer.tempCurve;
					defenderDrawers.get(id3-7).rotation = currentDrawer.rotation;
					defenderDrawers.get(id3-7).startIndex = currentDrawer.startIndex;
					handle_name="D"+ String.valueOf(id3-6) +"_Handle";
					defenders.get(id3-7).initialPosition = currentPlayer.initialPosition;
					seekbar_player_Id=7;
				} else{
					Log.d("error", "playerListener -> MotionEvent.ACTION_UP: Wrong selection");
				}
				preIntersectId = intersectId;//留著undo那邊記錄用

				return true;//放開圖片的case
			}//switch觸控動作

			//Dbitmap.recycle();
			System.gc();

			return true;
		}//onTouch Event
	};

	//讓無效軌跡的移動者(view)可以回歸前一筆有效軌跡最後的位置
	private void backToLastPosition(Player current, Boolean isDribble, ImageView playWithBall){

		int lastX=current.getRoadSize()==0?current.initialPosition.x:current.handleGetRoad(current.getRoadSize()-2);
		int lastY=current.getRoadSize()==0?current.initialPosition.y:current.handleGetRoad(current.getRoadSize()-1);

		if(isDribble){ //有持球者 還要調整球的位置

			ball.image.layout(lastX+110,lastY+30, lastX+110+ball.image.getWidth(),lastY+30+ball.image.getHeight());
			playWithBall.layout(lastX,lastY,lastX+200,lastY+120);
			current.arrow.layout(lastX,lastY,lastX+playWithBall.getWidth(),lastY+playWithBall.getHeight());

		}else{ //球或無持球者

			if(current.arrow!=null){ //非球的狀況
				current.image.layout(lastX,lastY,lastX+current.image.getWidth(),lastY+current.image.getHeight());
				current.arrow.layout(lastX,lastY,lastX+current.image.getWidth(),lastY+current.image.getHeight());
				current.arrow.setRotation(currentDrawer.rotation);
				TimeLine timefrag1 = (TimeLine) getActivity().getFragmentManager().findFragmentById(R.id.time_line);
				timefrag1.setCircularSeekBarProgress(currentDrawer.rotation);//為了讓circular seekbar的值也一起變成儲存的狀態，但是因為android好像有bug，所以他不會更新介面上的seekbar的樣子，但值卻是有改過的
				current.arrow.postInvalidate();
				//重新set rect
				current.setRect(lastX,lastY,lastX+current.image.getWidth(),lastY+current.image.getHeight());
			}else{//球
				if(!intersect){ //球噴太遠的狀況 (有intersect的話 motion_up那裏會調整好)
					players.get(preIntersectId-1).image.setVisibility(View.INVISIBLE);
					players.get(preIntersectId-1).arrow.layout((int)playWithBall.getX(),(int)playWithBall.getY(),(int)playWithBall.getX()+playWithBall.getWidth(),(int)playWithBall.getY()+playWithBall.getHeight());
					current.image.layout((int)playWithBall.getX()+110,(int)playWithBall.getY()+30,(int)playWithBall.getX()+110+current.image.getWidth(),(int)playWithBall.getY()+30+current.image.getHeight());
					playWithBall.setVisibility(View.VISIBLE);

					//調整intersect參數
					intersect=true;
					intersectId=preIntersectId;
				}
			}
		}
	}

	//噴在地上的球不能繼續畫的警示
	//@TargetApi(Build.VERSION_CODES.O)
	public void showAlertDialogButton(String type){
		//create an alert builder
		AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
		//builder.setTitle("-- Alert --");

		//set the custom layout 動態載入使用inflater
		View v;
		if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
			v =getLayoutInflater().inflate(R.layout.dialog_undo_alert,null);
		}else{
			LayoutInflater layoutInflater =LayoutInflater.from(getActivity());
			v =layoutInflater.inflate(R.layout.dialog_undo_alert,null);
		}
		builder.setView(v);

		TextView alertText = v.findViewById(R.id.textAlert);
		ImageView alertImg = v.findViewById(R.id.button_undoimg);

		if(type.equals("ball_out_of_court")){
			alertText.setText("Please press the on-recoding button!");
			alertImg.setImageResource(R.drawable.icon_record_on);
		}else if(type.equals("ball_in_court")){
			alertText.setText("Please press the undo button!");
			alertImg.setImageResource(R.drawable.icon_undo);
		}


		//add a button
		Button backButton = v.findViewById(R.id.button_alert_back_to_main);

		final AlertDialog dialog=builder.create(); //final不能重新賦值
		dialog.show();
		dialog.getWindow().setLayout(650,430);

		backButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dialog.dismiss();
			}
		});
	}

	public void removeOnePath(int seekbarId){
		whichToRemove = seekbarId;
		/*remove bitmap*/
		tempBitmap = Bitmap.createBitmap(circle.getWidth(),circle.getHeight(),Bitmap.Config.ARGB_8888);//初始化tempBitmap，指定大小為螢幕大小(大小同circle)
		tempCanvas = new Canvas();
		tempCanvas = new Canvas(tempBitmap);//畫透明路徑
		for(int tmp = 0; tmp< bitmapVector.size(); tmp++){
			if(tmp!= whichToRemove){
				tempCanvas.drawBitmap(bitmapVector.get(tmp), 0, 0, null);
			}
		}
		bitmapVector.remove(whichToRemove);
		circle.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));//把tempBitmap放進circle裡

		/*remove timeline*/
		TimeLine timefrag1 = (TimeLine) getActivity().getFragmentManager().findFragmentById(R.id.time_line);
		timefrag1.removeOneTimeline(runBags.get(whichToRemove).getTimeLineId());

		/*remove pathnum on the court*/
		MainWrap mainwrap = (MainWrap) getActivity().getFragmentManager().findFragmentById(R.id.MainWrap_frag);
		mainwrap.removeTextView(whichToRemove);
		mainwrap.removeScreenBar(whichToRemove);

		/*remove runline*/
		/*Every component's position in the RunLine should be the same with the TimeLine SeekBarId.*/
		for(int index = whichToRemove +1; index< runBags.size(); index++){
			timefrag1.changeSeekBarId(index,index-1);
			mainwrap.changeTextViewId(index, index-1);//change the remained pathnum text and its id
			mainwrap.changeScreenBarTag(index, index-1);
			mainwrap.changeDribbleLineTag(index, index-1);
			runBags.get(index).setTimeLineId(index-1);
		}
		runBags.remove(whichToRemove);
		timefrag1.setRunlineId(runBags.size()-1);

		sortPathNumber();
	}

	//讓人物和球view都呈現半透明/不透明
	public void setViewAlpha(int value){
		for (int i=0;i<players.size();i++){
			players.get(i).image.setImageAlpha(value);
			players.get(i).arrow.setImageAlpha(value);
			defenders.get(i).image.setImageAlpha(value);
			defenders.get(i).arrow.setImageAlpha(value);
			playersWithBall.get(i).setImageAlpha(value);
		}
		ball.image.setImageAlpha(value);
	}

	public void sortPathNumber(){

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

		// sort runbag(一定會有順序之分) 上面的list若starttime一樣則會有一樣的ID
		Collections.sort(runBags,new Comparator<RunBag>(){
			@Override
			public int compare(RunBag r1,RunBag r2){
				if (r1.getStartTime() > r2.getStartTime()) {
					return 1; // +1 : o1>o2
				}else if (r1.getStartTime() < r2.getStartTime()){
					return -1;
				}else{
					if(r1.getDuration()>r2.getDuration()){
						return 1;
					}else{
						return -1;
					}
				}
			}
		});

		TimeLine timefrag = (TimeLine) getActivity().getFragmentManager().findFragmentById(R.id.time_line);
		MainWrap mainwrap = (MainWrap) getActivity().getFragmentManager().findFragmentById(R.id.MainWrap_frag);

		int show_text=1;

		for(int i =0;i<list.size();i++){
			////Log,i("seekbar", "i = "+Integer.toString(i)+"  TimeLine id ="+list.get(i).getTimeLineId()+"  start_time = "+Integer.toString(list.get(i).getStartTime()));

			timefrag.setPathNumberText(list.get(i).getTimeLineId(), show_text);
			mainwrap.setPathNumberText(list.get(i).getTimeLineId(), show_text);

			if(i==list.size()-1){
				//設定
				break;
			} else if(list.get(i).getStartTime()!=list.get(i+1).getStartTime()){
				//設定完，show_text++
				show_text++;
			}
		}
	}
}
