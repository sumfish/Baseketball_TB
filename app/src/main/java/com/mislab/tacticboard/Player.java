package com.mislab.tacticboard;

import android.graphics.Point;
import android.graphics.Rect;
import android.widget.ImageView;

import java.util.Vector;

public class Player {
	public ImageView image;
	public ImageView arrow;

	public Point initialPosition;
	public int initialRotation;
	public Rect rect;

	private Vector<Integer> road;
	private Vector<Integer> rotation;

	public Player(ImageView image, ImageView arrow) {
		this.image = image;
		this.arrow = arrow;
		this.road = new Vector<Integer>();
		this.rotation = new Vector<Integer>();
		this.initialPosition = new Point(-1, -1);
		this.initialRotation = -1;
		this.rect = new Rect(0,0,0,0);
	}

	//指定rect大小(用於計算跟球的intersect)
	public void setRect(int l, int t, int r, int b){
		rect.set(l,t,r,b);
	}

	public void clearAll() {
		this.road.clear();
		this.rotation.clear();
		this.initialPosition = new Point(-1, -1);
		this.initialRotation = -1;
	}

	public int getRoad(int find, int startIndex) {
		return road.indexOf(find, startIndex);
	}

	public int getLastRoad() {
		return road.size() - 1;
	}

	public int handleGetRoad(int in_index) { //
		return road.get(in_index);
	}

	public void setRoad(int add_object) {
		road.add(add_object);
	}

	public int getRoadSize() {
		return road.size();
	}

	public Vector<Integer> getCmpltRoad() {
		return road;
	}

	public void setMyRotation(int input) {
		rotation.add(input);
	}

	public int getMyRotation(int input) {
		return rotation.get(input);
	}

	public int getRotationSize() {
		return rotation.size();
	}

	///////////
	public int findLastValueIndex(int value) {
		return road.lastIndexOf(value);
	}
	/// undo時直接拿掉上一筆資料
	public void undoARoad(int count) {
		for (int i=0;i<count;i++){
			road.remove(getRoadSize()-1);
			//rotation.remove(getLastRoad()-1);
		}
		for (int i=0;i<count/2+1;i++){
			rotation.remove(getRotationSize()-1);
		}
	}
}