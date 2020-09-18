package com.example.helloworld;

public class MyPoint {
	
	public double x;
    public double y;

    public MyPoint() {}

    public MyPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public MyPoint(MyPoint src) {
        this.x = src.x;
        this.y = src.y;
    }
}
