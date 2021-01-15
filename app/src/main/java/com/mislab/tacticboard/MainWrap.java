package com.mislab.tacticboard;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;



/* 處理球場上的路徑編號顯示, 擋拆指示線, 運球指示線, 詢問是否擋人 */

public class MainWrap extends Fragment{

	private ImageView bar;

	public void onAttach(Activity activity){ 
		super.onAttach(activity);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.fragment_main_wrap, container,false);
		
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){ 
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public void onResume(){ 
		super.onResume();
		getActivity().findViewById(R.id.MainWrap_frag).setVisibility(View.VISIBLE);
	}
	
	@Override
	public void onPause(){ 
		super.onPause();
		getActivity().findViewById(R.id.MainWrap_frag).setVisibility(View.GONE);
	}
	
	//region 標示在跑動軌跡旁的數字編號
	public void createPathNumberOnCourt(int pathNumber, int x, int y, int id){
		//addview here
		RelativeLayout mainWrap = (RelativeLayout) getView().findViewById(R.id.mainfrag_wrap);
		
		TextView text = new TextView(getActivity());
		LayoutParams textLayoutParams = new LayoutParams(60,60);
		text.setX(x - 20);
		text.setY(y - 20);
		text.setLayoutParams(textLayoutParams);
		text.setGravity((Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL));
		text.setText(Integer.toString(pathNumber));
		text.setTextSize(20.0f);
		text.setBackgroundResource(R.drawable.path_num_3);
		text.setId(id);
		mainWrap.addView(text);
	}

	// 把screen bar圖片放上fragment
	public void putScreenBar(int nowX, int nowY, int Player, int ID){

		//同步紀錄詢問擋人的時候: player & screen bar ID
		int whichPlayer=Player; //根據ID拿不同顏色的bar
		int recordID=ID;

		RelativeLayout mainWrap = (RelativeLayout) getView().findViewById(R.id.mainfrag_wrap);

		bar = new ImageView(getActivity());
		bar.setScaleX(0.1f);
		bar.setScaleY(0.1f);
		bar.setX(nowX);
		bar.setY(nowY-440);
		Resources resources = getResources();
		int screenBarId = resources.getIdentifier("screen_bar"+String.valueOf(whichPlayer), "drawable", getActivity().getPackageName());
		bar.setBackgroundResource(screenBarId);
		bar.setTag("screen_bar_"+recordID);
		mainWrap.addView(bar);
	}

	public void setScreenBarRotate(float direction){
		bar.setRotation(direction);
	}

	public void setPathNumberText(int searchId , int text){
		TextView tempText = null;
		RelativeLayout mainWrap = (RelativeLayout) getView().findViewById(R.id.mainfrag_wrap);
		if(mainWrap.findViewById(searchId)!=null){
			tempText = (TextView) mainWrap.findViewById(searchId);
		}
		tempText.setText(Integer.toString(text));
		tempText.invalidate();
	}
	
	public void changeTextViewId(int search_id, int change_to_id){
		TextView tempText = null;
		RelativeLayout mainWrap = (RelativeLayout) getView().findViewById(R.id.mainfrag_wrap);
		if(mainWrap.findViewById(search_id)!=null){
			tempText = (TextView) mainWrap.findViewById(search_id);
		}
		tempText.setText(Integer.toString(Integer.parseInt(tempText.getText().toString())-1));
		/*這一定要的*/
		tempText.setId(change_to_id);
		tempText.invalidate();
	}
	
	public void removeTextView(int input_id){
		RelativeLayout mainWrap = (RelativeLayout) getView().findViewById(R.id.mainfrag_wrap);
		TextView tempText = (TextView) mainWrap.findViewById(input_id);
		mainWrap.removeView(tempText);
	}
	
	
	public void clearRecordLayout(){
		RelativeLayout mainWrap = (RelativeLayout) getView().findViewById(R.id.mainfrag_wrap);
		mainWrap.removeAllViews();
		
	}
	//endregion

	//region 標示掩護方向的圖片 (獨立於canvas那邊)
	@SuppressLint("ResourceType")
	public void createScreenBar(int x, int y, int playerId, float direction, int id){
		//addview here
		RelativeLayout mainWrap = (RelativeLayout) getView().findViewById(R.id.mainfrag_wrap);

		ImageView img = new ImageView(getActivity());
		img.setX(x-0);
		img.setY(y-460);
		img.setScaleX(0.1f);
		img.setScaleY(0.1f);
		img.setRotation(direction);
		img.setId(123456);
		Resources resources = getResources();
		int screenBarId = resources.getIdentifier("screen_bar"+String.valueOf(playerId), "drawable", getActivity().getPackageName());
		img.setBackgroundResource(screenBarId);
		img.setTag("screen_bar_"+id);
		mainWrap.addView(img);
	}

	public void removeScreenBar(int remove_id){
		RelativeLayout mainwrap = (RelativeLayout) getView().findViewById(R.id.mainfrag_wrap);
		ImageView imgTmp = (ImageView) mainwrap.findViewWithTag("screen_bar_" + remove_id);
		mainwrap.removeView(imgTmp);
	}

	public void changeScreenBarTag(int search_id, int change_to_id){
		ImageView tempImg = null;
		RelativeLayout mainwrap = (RelativeLayout) getView().findViewById(R.id.mainfrag_wrap);
		if(mainwrap.findViewWithTag("screen_bar_"+search_id)!=null){
			tempImg = (ImageView) mainwrap.findViewWithTag("screen_bar_"+search_id);
			tempImg.setTag("screen_bar_"+change_to_id);
			tempImg.invalidate();
		}

		/*這一定要的*/
		//imgTmp.setTag("screen_bar_"+change_to_id);
		//imgTmp.invalidate();
	}
	//endregion

	//region 標示運球線段
	@SuppressLint("ResourceType")

	public void removeDribbleLine(int remove_id){
		RelativeLayout mainwrap = (RelativeLayout) getView().findViewById(R.id.mainfrag_wrap);
		ImageView imgTmp = (ImageView) mainwrap.findViewWithTag("dribble_line_" + remove_id);
		mainwrap.removeView(imgTmp);

	}

	public void changeDribbleLineTag(int search_id, int change_to_id){
		ImageView tempImg = null;
		RelativeLayout mainwrap = (RelativeLayout) getView().findViewById(R.id.mainfrag_wrap);
		if(mainwrap.findViewWithTag("dribble_line_"+search_id)!=null){
			tempImg = (ImageView) mainwrap.findViewWithTag("dribble_line_"+search_id);
			tempImg.setTag("dribble_line_"+change_to_id);
			tempImg.invalidate();
		}

		/*這一定要的*/
		//imgTmp.setTag("dribble_line_"+change_to_id);
		//imgTmp.invalidate();
	}

	//endregion
}
