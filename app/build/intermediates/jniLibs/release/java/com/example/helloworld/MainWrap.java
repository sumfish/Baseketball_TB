package com.example.helloworld;

import com.example.helloworld.CircularSeekBar;
import com.example.helloworld.R;
import com.example.helloworld.TimeLine.CallbackInterface;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;



/*處理court上的路徑編號顯示*/

public class MainWrap extends Fragment{

	public void onAttach(Activity activity){ 
		super.onAttach(activity);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState){ 
		
		return inflater.inflate(R.layout.main_wrap, container,false);
		
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
	public void create_path_num_on_court(int pathnum,int x, int y,int id){
		//addview here
		RelativeLayout mainwrap = (RelativeLayout) getView().findViewById(R.id.mainfrag_wrap);
		
		TextView text = new TextView(getActivity());
		LayoutParams textlp = new LayoutParams(60,60);
		text.setX(x - 20);
		text.setY(y - 20);
		text.setLayoutParams(textlp);
		text.setGravity((Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL));
		text.setText(Integer.toString(pathnum));
		text.setTextSize(20.0f);
		text.setBackgroundResource(R.drawable.path_num_3);
		text.setId(id);
		mainwrap.addView(text);


	}
	
	public void set_pathnum_text(int search_id , int show_text){
		TextView texttmp = null;
		RelativeLayout mainwrap = (RelativeLayout) getView().findViewById(R.id.mainfrag_wrap);
		if(mainwrap.findViewById(search_id)!=null){
			texttmp = (TextView) mainwrap.findViewById(search_id);
		}
		texttmp.setText(Integer.toString(show_text));
		/*這一定要的*/
		texttmp.invalidate();
	}
	
	public void change_textView_id(int search_id, int change_to_id){
		TextView texttmp = null;
		RelativeLayout mainwrap = (RelativeLayout) getView().findViewById(R.id.mainfrag_wrap);
		if(mainwrap.findViewById(search_id)!=null){
			texttmp = (TextView) mainwrap.findViewById(search_id);
		}
		texttmp.setText(Integer.toString(Integer.parseInt(texttmp.getText().toString())-1));
		/*這一定要的*/
		texttmp.setId(change_to_id);
		texttmp.invalidate();
	}
	
	public void remove_textView(int input_id){
		RelativeLayout mainwrap = (RelativeLayout) getView().findViewById(R.id.mainfrag_wrap);
		TextView texttmp = (TextView) mainwrap.findViewById(input_id);
		mainwrap.removeView(texttmp);
	}
	
	
	public void clear_record_layout(){
		RelativeLayout mainwrap = (RelativeLayout) getView().findViewById(R.id.mainfrag_wrap);
		mainwrap.removeAllViews();
		
	}
	//endregion

	//region 標示掩護方向的圖片
	@SuppressLint("ResourceType")
	public void create_screen_bar(int x, int y, int player, float direction, int id){
		//addview here
		RelativeLayout mainwrap = (RelativeLayout) getView().findViewById(R.id.mainfrag_wrap);

		ImageView img = new ImageView(getActivity());
		img.setX(x-0);
		img.setY(y-460);
		img.setScaleX(0.1f);
		img.setScaleY(0.1f);
		img.setRotation(direction);
		img.setId(123456);
		switch(player){
			case 1:
				img.setBackgroundResource(R.drawable.screen_bar1);
				break;
			case 2:
				img.setBackgroundResource(R.drawable.screen_bar2);
				break;
			case 3:
				img.setBackgroundResource(R.drawable.screen_bar3);
				break;
			case 4:
				img.setBackgroundResource(R.drawable.screen_bar4);
				break;
			case 5:
				img.setBackgroundResource(R.drawable.screen_bar5);
				break;
		}
		img.setTag("screen_bar_"+id);
		mainwrap.addView(img);
	}

	public void remove_screen_bar(int remove_id){
		RelativeLayout mainwrap = (RelativeLayout) getView().findViewById(R.id.mainfrag_wrap);
		ImageView imgTmp = (ImageView) mainwrap.findViewWithTag("screen_bar_" + remove_id);
		mainwrap.removeView(imgTmp);
	}

	public void change_screen_bar_tag(int search_id, int change_to_id){
		ImageView imgTmp = null;
		RelativeLayout mainwrap = (RelativeLayout) getView().findViewById(R.id.mainfrag_wrap);
		if(mainwrap.findViewWithTag("screen_bar_"+search_id)!=null){
			imgTmp = (ImageView) mainwrap.findViewWithTag("screen_bar_"+search_id);
			imgTmp.setTag("screen_bar_"+change_to_id);
			imgTmp.invalidate();
		}

		/*這一定要的*/
		//imgTmp.setTag("screen_bar_"+change_to_id);
		//imgTmp.invalidate();
	}
	//endregion

	//region 標示運球線段
	@SuppressLint("ResourceType")
	public void create_dribble_line(int x, int y, int player, float direction, float length, int id){
		RelativeLayout mainwrap = (RelativeLayout) getView().findViewById(R.id.mainfrag_wrap);
		ImageView img = new ImageView(getActivity());
		img.setX(x); //+ 60.0f);
		img.setY(y); //+ 60.0f);

		img.setPivotX(0);
		img.setPivotY(0);
		img.setRotation(direction);

		img.setId(789123);
		img.setScaleX(length);
		switch(player){
			case 1:
				img.setBackgroundResource(R.drawable.zigzag_line1);
				break;
			case 2:
				img.setBackgroundResource(R.drawable.zigzag_line2);
				break;
			case 3:
				img.setBackgroundResource(R.drawable.zigzag_line3);
				break;
			case 4:
				img.setBackgroundResource(R.drawable.zigzag_line4);
				break;
			case 5:
				img.setBackgroundResource(R.drawable.zigzag_line5);
				break;
		}

		img.setTag("dribble_line_"+id);
		mainwrap.addView(img);
	}

	public void remove_dribble_line(int remove_id){
		RelativeLayout mainwrap = (RelativeLayout) getView().findViewById(R.id.mainfrag_wrap);
		ImageView imgTmp = (ImageView) mainwrap.findViewWithTag("dribble_line_" + remove_id);
		mainwrap.removeView(imgTmp);

	}

	public void change_dribble_line_tag(int search_id, int change_to_id){
		ImageView imgTmp = null;
		RelativeLayout mainwrap = (RelativeLayout) getView().findViewById(R.id.mainfrag_wrap);
		if(mainwrap.findViewWithTag("dribble_line_"+search_id)!=null){
			imgTmp = (ImageView) mainwrap.findViewWithTag("dribble_line_"+search_id);
			imgTmp.setTag("dribble_line_"+change_to_id);
			imgTmp.invalidate();
		}

		/*這一定要的*/
		//imgTmp.setTag("dribble_line_"+change_to_id);
		//imgTmp.invalidate();
	}

	//endregion
}
