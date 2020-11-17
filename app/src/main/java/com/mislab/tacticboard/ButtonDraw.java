package com.mislab.tacticboard;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;
import org.json.JSONException;

/*按鈕的抽屜*/
/*存按鈕們的地方*/
/*顯示時間軸、隱藏時間軸、開始錄製、清除筆跡、清除路徑*/

public class ButtonDraw extends Fragment {
	
	private MainFragment mainFragment;
	
	private boolean buttonDrawRecordcheck = false;//檢查目前有沒有在錄製的狀態，預設為"沒有"
	private ToggleButton record;//Toggle的錄製按鈕
	
	private TimeLine timeline = null;
	//private ImageSelect imageSelect = null;
	private PerspectiveSelect perspectiveSelect = null;
	private Button btnImage = null;
	private Button btnTimeline = null;

	private boolean isTimelineShow = true;
	private boolean isImageSelect = false;
	private boolean isScreenEnable = false;

	private TextView advanced_text = null;

	public interface CallbackInterface{//連接MainActivity，告訴MainActivity現在有沒有在錄製狀態
		public void setRecordCheck(boolean in_recordcheck);
		public void setClean();
		
	}
	
	private CallbackInterface mCallback;//是一個用來call setRecordCheck的媒介，作用是用來設定in_recordcheck
	
	public void onAttach(Activity activity){
		super.onAttach(activity);
		
		try{
			mCallback = (CallbackInterface) activity;
		}catch(ClassCastException e){
			throw new ClassCastException (activity.toString()+"must implement ButtonDraw.CallbackInterface!");
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState){
		
		return inflater.inflate(R.layout.fragment_button_draw, container,false);
		
	}
	
	//@TargetApi(Build.VERSION_CODES.M)
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
        /*
        // Request the GAN defender
		Button def = (Button)getView().findViewById(R.id.defbutton);
        def.setOnTouchListener(defListener);
		*/

		final Button play = (Button) getView().findViewById(R.id.playbutton);
        //play.setOnClickListener(playListener);//播放
        play.setTextSize(TypedValue.COMPLEX_UNIT_PX, 20);
		play.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN) {
					play.setBackgroundResource(R.drawable.icon_play_click);
					mainFragment.changePlayerToNoBall();
					mainFragment.setIsTacticPlaying(1);
					mainFragment.playButton();
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					play.setBackgroundResource(R.drawable.icon_play);
				}
				return true;
			}
		});

		btnTimeline = (Button) getView().findViewById(R.id.button02);
		btnTimeline.setOnClickListener(btn2Listener);
		btnTimeline.setTextSize(TypedValue.COMPLEX_UNIT_PX, 20);
		btnTimeline.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN) {
					isTimelineShow = !isTimelineShow;
					if(isTimelineShow){
						btnTimeline.setBackgroundResource(R.drawable.icon_hide_timeline);
						getActivity().findViewById(R.id.time_line).setVisibility(View.VISIBLE);
						FragmentManager fragmentManager =getFragmentManager();
						FragmentTransaction transaction = fragmentManager.beginTransaction();
						timeline = (TimeLine) fragmentManager.findFragmentById(R.id.time_line);

						getActivity().findViewById(R.id.image_select).setVisibility(View.GONE);
						btnImage.setBackgroundResource(R.drawable.icon_image);
						isImageSelect = !isImageSelect;

						transaction.show(timeline);
						transaction.commit();
						fragmentManager.executePendingTransactions();
					}
					else{
						btnTimeline.setBackgroundResource(R.drawable.icon_show_timeline);
						FragmentManager fragmentManager =getFragmentManager();
						timeline = (TimeLine) fragmentManager.findFragmentById(R.id.time_line);
						if (null != timeline){
							getActivity().findViewById(R.id.time_line).setVisibility(View.GONE);
							FragmentTransaction fragTran = fragmentManager.beginTransaction();
							fragTran.hide(timeline);
							fragTran.commit();
							fragmentManager.executePendingTransactions();
						}
					}
				}
				return true;
			}
		});

		btnImage = (Button) getView().findViewById(R.id.btn_image);
		btnImage.setOnClickListener(btn_imageListener);
		btnImage.setTextSize(TypedValue.COMPLEX_UNIT_PX, 20);

        record = (ToggleButton) getView().findViewById(R.id.recordbutton);
        record.setOnClickListener(recordListener);
        record.setTextSize(TypedValue.COMPLEX_UNIT_PX, 20);
        
        Button buttonClear = (Button) getView().findViewById(R.id.button_clear);
        buttonClear.setOnClickListener(clearListener);
        buttonClear.setTextSize(TypedValue.COMPLEX_UNIT_PX, 20);

        // 新增undo按鈕
		Button buttonUndo = (Button) getView().findViewById(R.id.button_undo);
		buttonUndo.setOnClickListener(undoListener);

        Button buttonLoad = (Button) getView().findViewById(R.id.button_strategies);
        buttonLoad.setOnClickListener(strategies);
        buttonLoad.setTextSize(TypedValue.COMPLEX_UNIT_PX, 20);

        final Button buttonScreen = (Button) getView().findViewById(R.id.btn_screen);
        buttonScreen.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				isScreenEnable = !isScreenEnable;
				mainFragment.isScreenEnable =isScreenEnable;
				if(isScreenEnable){
					buttonScreen.setBackgroundResource(R.drawable.screen);

				}
				else{
					buttonScreen.setBackgroundResource(R.drawable.screen_disable);
				}
			}
		});

        /*
        Button button_settings = (Button) getView().findViewById(R.id.button_settings);
        button_settings.setOnClickListener(settings);
        button_settings.setTextSize(TypedValue.COMPLEX_UNIT_PX, 20);
        */

        /*Button button_DTW = (Button) getView().findViewById(R.id.button_DTW);
        button_DTW.setOnClickListener(DTW);
        button_DTW.setTextSize(TypedValue.COMPLEX_UNIT_PX, 20);*/
        
        final Button buttonSendtoUE4 = (Button) getView().findViewById(R.id.button_SendtoUE4);
        buttonSendtoUE4.setOnClickListener(sendtoVR);
        buttonSendtoUE4.setTextSize(TypedValue.COMPLEX_UNIT_PX,20);
		buttonSendtoUE4.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN) {
					buttonSendtoUE4.setBackgroundResource(R.drawable.icon_connect_click);
					MainFragment mainfrag =(MainFragment) getActivity().getFragmentManager().findFragmentById(R.id.Main);
					mainfrag.sendTacticToVR();

				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					buttonSendtoUE4.setBackgroundResource(R.drawable.icon_connect_ue4);
				}
				return true;
			}
		});

		final Button buttonGetDefenderTraj = (Button) getView().findViewById(R.id.defender);
		buttonGetDefenderTraj.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN) {
					buttonSendtoUE4.setBackgroundResource(R.drawable.icon_connect_click);
					MainFragment mainfrag =(MainFragment) getActivity().getFragmentManager().findFragmentById(R.id.Main);
					try {
						mainfrag.getDefenderFromServer();
					} catch (JSONException e) {
						e.printStackTrace();
					} catch (ExecutionException e) {
						e.printStackTrace();
					}
				} else if (event.getAction() == MotionEvent.ACTION_UP) {

				}
				return true;
			}
		});

        mainFragment = (MainFragment) getFragmentManager().findFragmentById(R.id.Main);
        
	}
	
	@Override
	public void onResume(){
		super.onResume();
		getActivity().findViewById(R.id.ButtonDraw).setVisibility(View.VISIBLE);
	}
	
	@Override
	public void onPause(){
		super.onPause();
		getActivity().findViewById(R.id.ButtonDraw).setVisibility(View.GONE);
	}

    private View.OnTouchListener defListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                try {
                    mainFragment.getDefenderFromServer();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
					e.printStackTrace();
				}
			}else if(motionEvent.getAction() == MotionEvent.ACTION_UP){

            }
            return  true;
        }
    };

	private OnClickListener btn_imageListener = new OnClickListener() {
		@Override
		public void onClick(View view) {
			/*
			isImageSelect = !isImageSelect;
			if(isImageSelect){
				btn_image.setBackgroundResource(R.drawable.icon_hide_image);
				getActivity().findViewById(R.id.image_select).setVisibility(View.VISIBLE);
				FragmentManager fragmentManager =getFragmentManager();
				FragmentTransaction transaction = fragmentManager.beginTransaction();
				imageSelect = (ImageSelect) fragmentManager.findFragmentById(R.id.image_select);

				getActivity().findViewById(R.id.time_line).setVisibility(View.GONE);
				btn_timeline.setBackgroundResource(R.drawable.icon_show_timeline);
				isTimelineShow = !isTimelineShow;

				transaction.show(imageSelect);
				transaction.commit();
				fragmentManager.executePendingTransactions();
			}
			else{
				btn_image.setBackgroundResource(R.drawable.icon_image);
				FragmentManager fragmentManager =getFragmentManager();
				imageSelect = (ImageSelect) fragmentManager.findFragmentById(R.id.image_select);
				if (null != imageSelect){
					getActivity().findViewById(R.id.image_select).setVisibility(View.GONE);
					FragmentTransaction fragTran = fragmentManager.beginTransaction();
					fragTran.hide(imageSelect);
					fragTran.commit();
					fragmentManager.executePendingTransactions();
				}

			}
			*/
			isImageSelect = !isImageSelect;
			if(isImageSelect){
				btnImage.setBackgroundResource(R.drawable.icon_hide_image);
				getActivity().findViewById(R.id.perspecitve_select).setVisibility(View.VISIBLE);
				FragmentManager fragmentManager =getFragmentManager();
				FragmentTransaction transaction = fragmentManager.beginTransaction();
				perspectiveSelect = (PerspectiveSelect) fragmentManager.findFragmentById(R.id.perspecitve_select);

				getActivity().findViewById(R.id.time_line).setVisibility(View.GONE);
				btnTimeline.setBackgroundResource(R.drawable.icon_show_timeline);
				isTimelineShow = !isTimelineShow;

				transaction.show(perspectiveSelect);
				transaction.commit();
				fragmentManager.executePendingTransactions();
			}
			else{
				btnImage.setBackgroundResource(R.drawable.icon_image);
				FragmentManager fragmentManager =getFragmentManager();
				perspectiveSelect = (PerspectiveSelect) fragmentManager.findFragmentById(R.id.perspecitve_select);
				if (null != perspectiveSelect){
					getActivity().findViewById(R.id.perspecitve_select).setVisibility(View.GONE);
					FragmentTransaction fragTran = fragmentManager.beginTransaction();
					fragTran.hide(perspectiveSelect);
					fragTran.commit();
					fragmentManager.executePendingTransactions();
				}

			}

		}
	};

	private OnClickListener playListener = new OnClickListener(){//????
    	@Override
    	public void onClick(View v) {
	////////////////////////Save each road(RunBag) and add into RunLine////////////////////////
    		mainFragment.changePlayerToNoBall();
    		mainFragment.setIsTacticPlaying(1);
    		mainFragment.playButton();
    	}
    };
	
	
	
    private OnClickListener btn2Listener = new OnClickListener(){//"??????b"
    	@Override
    	public void onClick(View v) {//"顯示時間軸"
			getActivity().findViewById(R.id.time_line).setVisibility(View.VISIBLE);
			FragmentManager fragmentManager =getFragmentManager();
			FragmentTransaction transaction = fragmentManager.beginTransaction();
			timeline = (TimeLine) fragmentManager.findFragmentById(R.id.time_line);
			transaction.show(timeline);
			transaction.commit();
			fragmentManager.executePendingTransactions();
    	}
    };
    
    private OnClickListener btn3Listener = new OnClickListener(){//"???????b"
    	@Override
    	public void onClick(View v) {//"隱藏時間軸"
	    	FragmentManager fragmentManager =getFragmentManager();
	    	timeline = (TimeLine) fragmentManager.findFragmentById(R.id.time_line);
	    	if (null != timeline){
	    		getActivity().findViewById(R.id.time_line).setVisibility(View.GONE);
	    		FragmentTransaction fragTran = fragmentManager.beginTransaction();
	    		fragTran.hide(timeline);
	    		fragTran.commit();
	    		fragmentManager.executePendingTransactions();
	    		return;
	    	}
    	}
    };
    
    private OnClickListener recordListener = new OnClickListener(){//?}?l/??????s
    	@Override
    	public void onClick(View v) {//開始/停止錄製
    		
    		if(record.isChecked()){
    			buttonDrawRecordcheck = true;
	    		mCallback.setRecordCheck(buttonDrawRecordcheck);//透過mCallback來從這裡設定MainActivity裡面recordcheck的值
    		}
    		else{
    			buttonDrawRecordcheck = false;
	    		mCallback.setRecordCheck(buttonDrawRecordcheck);//透過mCallback來從這裡設定MainActivity裡面recordcheck的值
    		}
    	}
    };

    private OnClickListener undoListener = new OnClickListener() {
		@Override
		public void onClick(View view) {//undo鍵
			MainFragment mainfrag =(MainFragment) getActivity().getFragmentManager().findFragmentById(R.id.Main);
			if(mainfrag.getRunBags()==0) {
				Log.d("undo","can not undo (have no runbag)");
				return;
			}
			mainfrag.undoRecord();
			mainfrag.undoPaint();
			if(mainfrag.getRunBags()==0){ //上面undo執行完會拿掉一個
				record.setChecked(false);
				mCallback.setRecordCheck(false);
				mCallback.setClean();
			}

		}
	};

    private OnClickListener clearListener = new OnClickListener(){//"?M??..."
    	@Override
    	public void onClick(View v) {//"清除..."

			MainFragment mainfrag =(MainFragment) getActivity().getFragmentManager().findFragmentById(R.id.Main);
			mainfrag.clearPaint();
			mainfrag.clearRecord();
			record.setChecked(false);
			mCallback.setRecordCheck(false);
			mCallback.setClean();

			//region 清楚分兩種的原始版本(已註解) / 我還沒搞清楚兩個clean的差別
			/*
			final String[] strategies = {"Clear all","Clear path"};
    		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

    		builder.setItems(strategies, new DialogInterface.OnClickListener(){
    	         @Override
				 //只要你在onClick處理事件內，使用which參數，就可以知道按下陣列裡的哪一個了
    	         public void onClick(DialogInterface dialog, int which) {
    	                // TODO Auto-generated method stub
    	        	 	if(which==0){//?M??????
    	        	 		mCallback.setClean();
    	        	 	}
    	        	 	else if (which==1){//?M?????|
    	        	 		MainFragment mainfrag =(MainFragment) getActivity().getFragmentManager().findFragmentById(R.id.Main);
    	            		mainfrag.clear_paint();
    	            		mainfrag.clear_record();
    	            		record.setChecked(false);
    	            		mCallback.setRecordCheck(false);
    	        	 	}
    	          }
    	    });
            AlertDialog about_dialog = builder.create();
            about_dialog.show();
            */
			//endregion
    	}
    };
    
    
    
    private OnClickListener strategies = new OnClickListener(){//"??N"
    	@Override
    	public void onClick(View v) {
			mainFragment.manageTacticDialog();
    	}
    };

    
    private OnClickListener sendtoVR = new OnClickListener(){//"SendtoUE4"
    	@Override
    	public void onClick(View v) {
		//MainFragment mainfrag =(MainFragment) getActivity().getFragmentManager().findFragmentById(R.id.Main);
		//mainfrag.Mainfrag_SendtoUE4();
		//Button Btn_sendToVR = (Button) getView().findViewById(R.id.button_SendtoUE4);
		//Btn_sendToVR.setBackgroundResource(R.drawable.icon_connect_click);
    	}
    };

    private void setUDP(){
    	LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View v = inflater.inflate(R.layout.dialog_set_udp, null);
    	AlertDialog.Builder UDPDialog = new AlertDialog.Builder(getActivity());
    	UDPDialog.setTitle("--- TCP ?]?w ---");
    	   UDPDialog.setView(v);
    	   
    	   UDPDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
    	    // do something when the button is clicked
    	    public void onClick(DialogInterface arg0, int arg1) {
	    	    EditText UDPIP = (EditText) (v.findViewById(R.id.edit_UDP_IP));
	    	    Log.i("socket", "IP:"+UDPIP.getText().toString());
	    	    InetAddress outIP=null;
	    	    try {
					outIP = InetAddress.getByName(UDPIP.getText().toString());
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	    	
	    	    	
	    	    EditText UDPPort = (EditText) (v.findViewById(R.id.edit_UDP_Port));
	    	    Log.i("socket", "Port:"+UDPPort.getText().toString());
	    	    int outPort = Integer.parseInt(UDPPort.getText().toString());
	    	    
	    	    MainFragment mainfrag =(MainFragment) getActivity().getFragmentManager().findFragmentById(R.id.Main);
	    	    mainfrag.setUDPIP(outIP, outPort);
    	    }
    	    });
    	   UDPDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
    	          // do something when the button is clicked
    	    public void onClick(DialogInterface arg0, int arg1) {
    	    //...
    	     }
    	    });
    	   UDPDialog.show();
    }
	
}
