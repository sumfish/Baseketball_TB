package com.example.helloworld;

import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import java.util.Vector;

public class Player{//implements Serializable{
	public ImageView image;
	public ImageView arrow;

	public Point initialPosition;
	public int initialRotation;
	public Rect rect;

	private Vector<Integer> road;
	private Vector<Integer> rotation;

	public Player(ImageView image, ImageView arrow){
		this.image = image;
		this.arrow = arrow;
		this.road = new Vector<>();
		this.rotation = new Vector<>();
		this.initialPosition = new Point(-1, -1);
		this.initialRotation = -1;
		this.rect = new Rect();
	}

	public void clearAll(){
		this.road.clear();
		this.rotation.clear();
		this.initialPosition = new Point(-1, -1);
		this.initialRotation = -1;
	}

	public int getRoad(int find, int startIndex){
		return road.indexOf(find,startIndex);
	}
	
	public int getLastRoad(){
		return road.size()-1;
	}
	
	public int handleGetRoad(int in_index){
		return road.get(in_index);
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

	public void setMyRotation(int input){
		rotation.add(input);
	}
	
	public int getMyRotation(int input){
		return rotation.get(input);
	}
	
	public int getRotationSize(){
		return rotation.size();
	}

}
