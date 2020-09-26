package com.mislab.tacticboard;

import java.io.Serializable;

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
	
	public void setBallNum(int number){
		ballNum =number;
	}
	
	public int getBallNum(){
		return ballNum;
	}
	
	public void setTimeLineId(int id){
		timelineId =id;
	}
	
	public int getTimeLineId(){
		return timelineId;
	}
	
	public int getStartTime(){
		return startTime;
	}
	
	public void setStartTime(int time){
		startTime = time;
	}
	
	public String getHandler(){
		return handler;
	}
	
	public void setHandler(String handler){
		this.handler = handler;
	}
	
	public int getRoadStart(){
		return roadStart;
	}
	
	public void setRoadStart(int roadStart){
		this.roadStart = roadStart;
	}
	
	public int getRoadEnd(){
		return roadEnd;
	}
	
	public void setRoadEnd(int roadEnd){
		this.roadEnd = roadEnd;
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
