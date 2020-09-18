package com.example.helloworld;
import java.util.Vector;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import org.opencv.android.OpenCVLoader;


/*中繼站的角色*/
/*連接各個元件然後負責幫忙傳遞訊息到正確的元件上面*/
/*播放、切換自由移動、切換3D環場*/

public class MainActivity extends ActionBarActivity implements TimeLine.CallbackInterface ,ButtonDraw.CallbackInterface,MainFragment.CallbackInterface{

	private MainFragment mainfrag;
	private TimeLine timefrag;
	private int seekBarCallbackStartTime;
	private int seekBarCallbackDuration;
	private int seekBarCallbackId;
	private boolean recordcheck;//檢查是否按下錄製鍵
	private Vector<RunBag> RunLine;
	private Player P1,P2,P3,P4,P5,B;
	private String seekbar_player;

	public void runLineInfo(Vector<RunBag> in_RunLine){
		RunLine=in_RunLine;
	}
	public void p1Info(Player in_P1){
		P1=in_P1;
	}
	public void p2Info(Player in_P2){
		P2=in_P2;
	}
	public void p3Info(Player in_P3){
		P3=in_P3;
	}
	public void p4Info(Player in_P4){
		P4=in_P4;
	}
	public void p5Info(Player in_P5){
		P5=in_P5;
	}
	public void bInfo(Player in_B){
		B=in_B;
	}
	
	public void passSeekbar(String player){//從MainFragment傳過來的移動了哪一個球員，該把seekbar綁在該球員上
		seekbar_player=player;
	}

	public void seekBarStartTime(int startTime){//接收從Timeline傳過來的時間
		seekBarCallbackStartTime = startTime;
		mainfrag.passStartTime(startTime);
	}
	
	public void seekBarDuration(int duration){//接收從Timeline傳過來的持續時間
		seekBarCallbackDuration = duration;
		mainfrag.passDuration(duration);
	}
	public void seekBarId(int id){
		seekBarCallbackId = id;
		mainfrag.passId(id);
	}
	////////////////////////////Timeline的時間(startTime,duration)要傳給main_fragment///////////////////////////
	
	public void setRecordCheck(boolean in_recordcheck){//in_recordcheck是在ButtonDraw裡面，透過interface的方式，使ButtonDraw可以呼叫這裡的function，進而設定recordcheck的值
		recordcheck = in_recordcheck;
		mainfrag.passRecordCheck(in_recordcheck);
	}
	///////////////////////////recordcheck要傳給main_fragment/////////////////////////////////////////////////
	
	public void setClean(){
		mainfrag.clearPaint();
	}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		if (!OpenCVLoader.initDebug()) {
			Log.e("OpenCV","Unable to load OpenCV");
		} else {
			Log.e("OpenCV","OpenCV has loaded!");
		}

        Log.d("debug", "MainActivity onCreate!");
        mainfrag = (MainFragment) getFragmentManager().findFragmentById(R.id.Main);
    }
}
