package com.mislab.tacticboard;
import java.util.Vector;
//import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import org.opencv.android.OpenCVLoader;
import androidx.appcompat.app.AppCompatActivity;


/*中繼站的角色*/
/*連接各個元件然後負責幫忙傳遞訊息到正確的元件上面*/
/*播放、切換自由移動、切換3D環場*/

public class MainActivity extends AppCompatActivity implements TimeLine.CallbackInterface ,ButtonDraw.CallbackInterface{

	private MainFragment mainfrag;
	private TimeLine timefrag;
	private int seekBarCallbackStartTime;
	private int seekBarCallbackDuration;
	private int seekBarCallbackId;
	private boolean isRecording;//檢查是否按下錄製鍵

	public void seekBarStartTime(int startTime){//接收從Timeline傳過來的時間
		seekBarCallbackStartTime = startTime;
		mainfrag.setStartTime(startTime);
	}

	public void seekBarDuration(int duration){//接收從Timeline傳過來的持續時間
		seekBarCallbackDuration = duration;
		mainfrag.setDuration(duration);
	}
	public void seekBarId(int id){
		seekBarCallbackId = id;
		mainfrag.setSeekBarCallBackId(id);
	}
	////////////////////////////Timeline的時間(startTime,duration)要傳給main_fragment///////////////////////////

	public void setRecordCheck(boolean isRecording){//in_recordcheck是在ButtonDraw裡面，透過interface的方式，使ButtonDraw可以呼叫這裡的function，進而設定recordcheck的值
		this.isRecording = isRecording;
		mainfrag.setRecording(isRecording);
	}
	///////////////////////////recordcheck要傳給mainFragment/////////////////////////////////////////////////
	
	public void setClean(){
		mainfrag.clearPaint();
	}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		if (!OpenCVLoader.initDebug())
			Log.e("OpenCV","Unable to load OpenCV");
		else
			Log.e("OpenCV","OpenCV has loaded!");


        Log.d("debug", "MainActivity onCreate!");
        mainfrag = (MainFragment) getFragmentManager().findFragmentById(R.id.Main);
    }
}
