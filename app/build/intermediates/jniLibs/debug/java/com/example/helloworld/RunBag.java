package com.example.helloworld;

import java.io.Serializable;
import java.util.Vector;

public class RunBag implements Serializable{
	private int startTime;
	private int duration;
	private String handler;
	private int roadStart;
	private int roadEnd;
	private int roadSize;
	private int rate;
	private int timelineId;
	private int ballNum;

	//region 用來表示掩護及運球
	private int pathType; //0:無球跑動 1:結束動作為掩護 2:運球
	private float screenAngle;
	private float dribbleAngle;
	private float dribbleLength;
	private int dribbleStartX;
	private int dribbleStartY;
	//endregion

	
	public RunBag(int time, int duration, String handler, int start, int end){
		startTime = time;
		this.duration = duration;
		this.handler = handler;
		roadStart = start;
		roadEnd = end;
		rate = this.duration /(roadSize);
		ballNum =0;
	}
	
	public RunBag(){
		startTime = -1;
		duration = -1;
		handler = "null";
		roadStart = -1;
		roadEnd = -1;
		rate = -1;
		ballNum =-1;
	}
	
	public Vector<Integer> parseRunBagToIntVec(){
		Vector<Integer> output = new Vector();
		/*tmp=Integer.toString(start_time)+"\n";
		tmp+=handler+"\n";
		tmp+=Integer.toString(road_start)+"\n";
		tmp+=Integer.toString(road_end)+"\n";
		tmp+=Integer.toString(duration)+"\n";
		tmp+=Integer.toString(ball_num);*/
		if(startTime !=-1)
			output.add(startTime);
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
		if(roadStart !=-1)
			output.add(roadStart);
		else
			return null;
		///////////////////////////////////////////
		if(roadEnd !=-1)
			output.add(roadEnd);
		else
			return null;
		///////////////////////////////////////////
		if(duration!=-1)
			output.add(duration);
		else
			return null;
		///////////////////////////////////////////
		if(ballNum !=-1)
			output.add(ballNum);
		else
			return null;

		return output;
	}

	public String getRunBagInfo(){
		String tmp = new String();
		tmp = Integer.toString(startTime)+"\n";
		tmp += handler+"\n";
		tmp += Integer.toString(roadStart)+"\n";
		tmp += Integer.toString(roadEnd)+"\n";
		tmp += Integer.toString(duration)+"\n";
		tmp += Integer.toString(ballNum);
		return tmp;
		
	}
	
	public void setBallNum(int input_num){
		ballNum =input_num;
	}
	
	public int getBallNum(){
		return ballNum;
	}
	
	public void setTimeLineId(int input_id){
		timelineId =input_id;
	}
	
	public int getTimeLineId(){
		return timelineId;
	}
	
	public int getStartTime(){
		return startTime;
	}
	
	public void setStartTime(int in_time){
		startTime = in_time;
	}
	
	public String getHandler(){
		return handler;
	}
	
	public void setHandler(String in_handler){
		handler = in_handler;
	}
	
	public int getRoadStart(){
		return roadStart;
	}
	
	public void setRoadStart(int in_start){
		roadStart = in_start;
	}
	
	public int getRoadEnd(){
		return roadEnd;
	}
	
	public void setRoadEnd(int in_end){
		roadEnd = in_end;
	}
	
	public int getDuration(){
		return duration;
	}
	
	public void setDuration(int duration){
		this.duration = duration;
		rate = ((this.duration *1000)/(roadEnd - roadStart));
	}
	
	public int getRate(){
		return rate;
	}


	public int getPathType() {
		return pathType;
	}

	public void setPathType(int pathType) {
		this.pathType = pathType;
	}

	public float getScreenAngle() {
		return screenAngle;
	}

	public void setScreenAngle(float screenAngle) {
		this.screenAngle = screenAngle;
	}

	public float getDribbleAngle() {
		return dribbleAngle;
	}

	public void setDribbleAngle(float dribbleAngle) {
		this.dribbleAngle = dribbleAngle;
	}

	public float getDribbleLength() {
		return dribbleLength;
	}

	public void setDribbleLength(float dribbleLength) {
		this.dribbleLength = dribbleLength;
	}

	public int getDribbleStartX() {
		return dribbleStartX;
	}

	public void setDribbleStartX(int dribbleStartX) {
		this.dribbleStartX = dribbleStartX;
	}

	public int getDribbleStartY() {
		return dribbleStartY;
	}

	public void setDribbleStartY(int dribbleStartY) {
		this.dribbleStartY = dribbleStartY;
	}
}
