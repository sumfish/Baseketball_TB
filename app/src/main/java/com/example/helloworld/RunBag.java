package com.example.helloworld;

import java.io.Serializable;
import java.util.Vector;

public class RunBag implements Serializable{
	private int start_time;
	private int duration;
	private String handler;
	private int road_start;
	private int road_end;
	private int road_size;
	private int rate;
	private int timeline_id;
	private int ball_num;

	//region 用來表示掩護及運球
	private int path_type; //0:無球跑動 1:結束動作為掩護 2:運球
	private float screen_angle;
	private float dribble_angle;
	private float dribble_length;
	private int dribble_start_x;
	private int dribble_start_y;
	//endregion

	
	public RunBag(int in_time , int in_duration,String in_handler , int in_start , int in_end){
		start_time = in_time;
		duration = in_duration;
		handler = in_handler;
		road_start = in_start;
		road_end = in_end;
		rate = duration/(road_size);
		ball_num=0;
	}
	
	public RunBag(){
		start_time = -1;
		duration = -1;
		handler = "null";
		road_start = -1;
		road_end = -1;
		rate = -1;
		ball_num=-1;
	}
	
	public Vector<Integer> parseRunBagToIntVec(){
		Vector<Integer> output = new Vector();
		
		/*tmp=Integer.toString(start_time)+"\n";
		tmp+=handler+"\n";
		tmp+=Integer.toString(road_start)+"\n";
		tmp+=Integer.toString(road_end)+"\n";
		tmp+=Integer.toString(duration)+"\n";
		tmp+=Integer.toString(ball_num);*/
		if(start_time!=-1)
			output.add(start_time);
		else
			return null;
		///////////////////////////////////////////
		if(handler.equals("null")==false){
			if(handler.equals("P1_Handle")){
				output.add(0);
			}
			else if(handler.equals("P2_Handle")){
				output.add(1);
			}
			else if(handler.equals("P3_Handle")){
				output.add(2);
			}
			else if(handler.equals("P4_Handle")){
				output.add(3);
			}
			else if(handler.equals("P5_Handle")){
				output.add(4);
			}
			else if(handler.equals("B_Handle")){
				output.add(5);
			}
			else if(handler.equals("D1_Handle")){
				output.add(6);
			}
			else if(handler.equals("D2_Handle")){
				output.add(7);
			}
			else if(handler.equals("D3_Handle")){
				output.add(8);
			}
			else if(handler.equals("D4_Handle")){
				output.add(9);
			}
			else if(handler.equals("D5_Handle")){
				output.add(10);
			}
		}
		else
			return null;
		///////////////////////////////////////////
		if(road_start!=-1)
			output.add(road_start);
		else
			return null;
		///////////////////////////////////////////
		if(road_end!=-1)
			output.add(road_end);
		else
			return null;
		///////////////////////////////////////////
		if(duration!=-1)
			output.add(duration);
		else
			return null;
		///////////////////////////////////////////
		if(ball_num!=-1)
			output.add(ball_num);
		else
			return null;

		return output;
	}
	
	
	
	public String getRunBagInfo(){
		
		String tmp = new String();
		tmp=Integer.toString(start_time)+"\n";
		tmp+=handler+"\n";
		tmp+=Integer.toString(road_start)+"\n";
		tmp+=Integer.toString(road_end)+"\n";
		tmp+=Integer.toString(duration)+"\n";
		tmp+=Integer.toString(ball_num);
		return tmp;
		
	}
	
	public void setBall_num(int input_num){
		ball_num=input_num;
	}
	
	public int getBall_num(){
		return ball_num;
	}
	
	public void setTimeLineId(int input_id){
		timeline_id=input_id;
	}
	
	public int getTimeLineId(){
		return timeline_id;
	}
	
	public int getStartTime(){
		return start_time;
	}
	
	public void setStartTime(int in_time){
		start_time = in_time;
	}
	
	public String getHandler(){
		return handler;
	}
	
	public void setHandler(String in_handler){
		handler = in_handler;
	}
	
	public int getRoadStart(){
		return road_start;
	}
	
	public void setRoadStart(int in_start){
		road_start = in_start;
	}
	
	public int getRoadEnd(){
		return road_end;
	}
	
	public void setRoadEnd(int in_end){
		road_end = in_end;
	}
	
	public int getDuration(){
		return duration;
	}
	
	public void setDuration(int in_duration){
		duration = in_duration;
		rate = ((duration*1000)/(road_end - road_start));
	}
	
	public int getRate(){
		return rate;
	}


	public int getPath_type() {
		return path_type;
	}

	public void setPath_type(int path_type) {
		this.path_type = path_type;
	}

	public float getScreen_angle() {
		return screen_angle;
	}

	public void setScreen_angle(float screen_angle) {
		this.screen_angle = screen_angle;
	}

	public float getDribble_angle() {
		return dribble_angle;
	}

	public void setDribble_angle(float dribble_angle) {
		this.dribble_angle = dribble_angle;
	}

	public float getDribble_length() {
		return dribble_length;
	}

	public void setDribble_length(float dribble_length) {
		this.dribble_length = dribble_length;
	}

	public int getDribble_start_x() {
		return dribble_start_x;
	}

	public void setDribble_start_x(int dribble_start_x) {
		this.dribble_start_x = dribble_start_x;
	}

	public int getDribble_start_y() {
		return dribble_start_y;
	}

	public void setDribble_start_y(int dribble_start_y) {
		this.dribble_start_y = dribble_start_y;
	}
}
