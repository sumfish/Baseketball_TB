package com.example.helloworld;

import java.io.Serializable;
import java.util.Vector;

public class Player implements Serializable{
	private Vector<Integer> road = new Vector();
	private Vector<Vector<Float>> curve = new Vector();
	private Vector<Integer> road_3d = new Vector();
	private Vector<Integer> rotation = new Vector();
	private String PlayerName = new String();
	private int start_index;
	private int start_index_3d;
	

	public void clear_all(){
		road.clear();
		road_3d.clear();
		rotation.clear();
		start_index=0;
		start_index_3d=0;
		
	}
	
	public void setPlayerName(String name){
		PlayerName = name;
	}
	
	public String getPlayerName(){
		return PlayerName;
	}
	
	public int getStartIndex(){
		return start_index;
	}
	
	public int getRoad(int find, int in_start_index){
		start_index=road.indexOf(find,in_start_index);
		return road.indexOf(find,in_start_index);
	}
	
	public int getLastRoad(){
		return road.size()-1;
	}
	
	public int handleGetRoad(int in_index){
		return road.get(in_index);
	}
	
	public void setCurve(Vector<Float> add_object){
		curve.add(add_object);
	}
	
	public int getCurveSize(){
		return curve.size();
	}
	
	public Vector<Float> getCurve(int i){
		return curve.get(i);
	}
	
	public Vector<Vector<Float>> getCmpltCurve(){
		return curve;
	}
	
	
	public void setRoad(int add_object){
		road.add(add_object);
	}
	
	public int getRoadSize(){
		return road.size();
	}
	
	public Vector<Integer> getCmpltRoad(){
		return road;
	}
	
	public Vector<Integer> getCmpltRotate(){
		return rotation;
	}
	//////////////////////////////////////////////////////
	public int getStartIndex_3d(){
		return start_index_3d;
	}
	
	public int getRoad_3d(int find, int in_start_index){
		start_index_3d=road_3d.indexOf(find,in_start_index);
		return road_3d.indexOf(find,in_start_index);
	}
	
	public int getLastRoad_3d(){
		return road_3d.indexOf(road_3d.lastElement());
	}
	
	public int handleGetRoad_3d(int in_index){
		return road_3d.get(in_index);
	}
	
	public void setRoad_3d(int add_object){
		road_3d.add(add_object);
	}
	
	public int getRoadSize_3d(){
		return road_3d.size();
	}
	
	public Vector<Integer> getCmpltRoad_3d(){
		return road_3d;
	}
	
	public void setMyRotation(int input){
		rotation.add(input);
	}
	
	public int getMyRotation(int input){
		return rotation.get(input);
	}
	
	public int getRotation_size(){
		return rotation.size();
	}

}
