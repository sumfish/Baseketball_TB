package com.example.helloworld;

import java.io.Serializable;
import java.util.Vector;

public class Player implements Serializable{
	private Vector<Integer> road = new Vector();
	private Vector<Vector<Float>> curve = new Vector();
	private Vector<Integer> road3D = new Vector();
	private Vector<Integer> rotation = new Vector();
	private String playerName = new String();
	private int startIndex;
	private int startIndex3D;
	

	public void clear_all(){
		road.clear();
		road3D.clear();
		rotation.clear();
		startIndex =0;
		startIndex3D =0;
	}
	
	public void setPlayerName(String name){
		playerName = name;
	}
	
	public String getPlayerName(){
		return playerName;
	}
	
	public int getStartIndex(){
		return startIndex;
	}
	
	public int getRoad(int find, int in_start_index){
		startIndex =road.indexOf(find,in_start_index);
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
		return startIndex3D;
	}
	
	public int getRoad_3d(int find, int in_start_index){
		startIndex3D = road3D.indexOf(find,in_start_index);
		return road3D.indexOf(find,in_start_index);
	}
	
	public int getLastRoad_3d(){
		return road3D.indexOf(road3D.lastElement());
	}
	
	public int handleGetRoad_3d(int in_index){
		return road3D.get(in_index);
	}
	
	public void setRoad3D(int add_object){
		road3D.add(add_object);
	}
	
	public int getRoadSize_3d(){
		return road3D.size();
	}
	
	public Vector<Integer> getCmpltRoad_3d(){
		return road3D;
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
