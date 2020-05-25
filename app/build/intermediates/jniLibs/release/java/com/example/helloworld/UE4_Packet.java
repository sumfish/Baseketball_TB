package com.example.helloworld;

import java.util.Vector;

public class UE4_Packet {
	
	private Vector<Integer> P_Initial_Position;
	private Integer initial_ball_num;
	private Vector<Integer> P_Initial_rotate;
	private Vector<Integer> P1_Road;
	private Vector<Integer> P2_Road;
	private Vector<Integer> P3_Road;
	private Vector<Integer> P4_Road;
	private Vector<Integer> P5_Road;
	private Vector<Integer> B_Road;
	private Vector<Integer> D1_Road;
	private Vector<Integer> D2_Road;
	private Vector<Integer> D3_Road;
	private Vector<Integer> D4_Road;
	private Vector<Integer> D5_Road;
	private Vector<Integer> P1_Rotate;
	private Vector<Integer> P2_Rotate;
	private Vector<Integer> P3_Rotate;
	private Vector<Integer> P4_Rotate;
	private Vector<Integer> P5_Rotate;
	private Vector<Integer> D1_Rotate;
	private Vector<Integer> D2_Rotate;
	private Vector<Integer> D3_Rotate;
	private Vector<Integer> D4_Rotate;
	private Vector<Integer> D5_Rotate;
	private Vector<Vector<Integer>> RunBag;
	
	public UE4_Packet(){
		initial_ball_num=-1;
		P_Initial_Position = new Vector();
		P_Initial_rotate = new Vector();
		P1_Road = new Vector();
		P2_Road = new Vector();
		P3_Road = new Vector();
		P4_Road = new Vector();
		P5_Road = new Vector();
		B_Road = new Vector();
		D1_Road = new Vector();
		D2_Road = new Vector();
		D3_Road = new Vector();
		D4_Road = new Vector();
		D5_Road = new Vector();
		P1_Rotate = new Vector();
		P2_Rotate = new Vector();
		P3_Rotate = new Vector();
		P4_Rotate = new Vector();
		P5_Rotate = new Vector();
		D1_Rotate = new Vector();
		D2_Rotate = new Vector();
		D3_Rotate = new Vector();
		D4_Rotate = new Vector();
		D5_Rotate = new Vector();
		RunBag = new Vector();
	}
	/*******************************   P_Initial_Position   *********************************************************/
	public int set_P_Initial_Position(Vector<Integer> input){
		P_Initial_Position = input;
		return P_Initial_Position.size();
	}
	
	public int get_P_Initial_Position_size(){
		return P_Initial_Position.size();
	}
	
	public Vector<Integer> get_P_Initial_Position(){
		return P_Initial_Position;
	}
	/******************************     initial_ball_num   **********************************************************/
	public int set_initial_ball_num(Integer input){
		initial_ball_num = input;
		return 4;
	}
	
	public int get_initial_ball_num(){
		return initial_ball_num;
	}
	/******************************     P_Initial_rotate   **********************************************************/
	public int set_P_Initial_rotate(Vector<Integer> input){
		P_Initial_rotate = input;
		return P_Initial_rotate.size();
	}
	
	public int get_P_Initial_rotate_size(){
		return P_Initial_rotate.size();
	}
	
	public Vector<Integer> get_P_Initial_rotate(){
		return P_Initial_rotate;
	}
	/******************************       Player's Road     **********************************************************/
	public int set_P1_Road(Vector<Integer> input){
		P1_Road = input;
		return P1_Road.size();
	}
	
	public int get_P1_Road_size(){
		return P1_Road.size();
	}
	
	public Vector<Integer> get_P1_Road(){
		return P1_Road;
	}
	/****************************************************************************************************************/
	public int set_P2_Road(Vector<Integer> input){
		P2_Road = input;
		return P2_Road.size();
	}
	
	public int get_P2_Road_size(){
		return P2_Road.size();
	}
	
	public Vector<Integer> get_P2_Road(){
		return P2_Road;
	}
	/****************************************************************************************************************/
	public int set_P3_Road(Vector<Integer> input){
		P3_Road = input;
		return P3_Road.size();
	}
	
	public int get_P3_Road_size(){
		return P3_Road.size();
	}
	
	public Vector<Integer> get_P3_Road(){
		return P3_Road;
	}
	/****************************************************************************************************************/
	public int set_P4_Road(Vector<Integer> input){
		P4_Road = input;
		return P4_Road.size();
	}
	
	public int get_P4_Road_size(){
		return P4_Road.size();
	}
	
	public Vector<Integer> get_P4_Road(){
		return P4_Road;
	}
	/****************************************************************************************************************/
	public int set_P5_Road(Vector<Integer> input){
		P5_Road = input;
		return P5_Road.size();
	}
	
	public int get_P5_Road_size(){
		return P5_Road.size();
	}
	
	public Vector<Integer> get_P5_Road(){
		return P5_Road;
	}
	/****************************************************************************************************************/
	public int set_B_Road(Vector<Integer> input){
		B_Road = input;
		return B_Road.size();
	}
	
	public int get_B_Road_size(){
		return B_Road.size();
	}
	
	public Vector<Integer> get_B_Road(){
		return B_Road;
	}
	/******************************       Defender's Road     **********************************************************/
	public int set_D1_Road(Vector<Integer> input){
		D1_Road = input;
		return D1_Road.size();
	}
	
	public int get_D1_Road_size(){
		return D1_Road.size();
	}
	
	public Vector<Integer> get_D1_Road(){
		return D1_Road;
	}
	/****************************************************************************************************************/
	public int set_D2_Road(Vector<Integer> input){
		D2_Road = input;
		return D2_Road.size();
	}
	
	public int get_D2_Road_size(){
		return D2_Road.size();
	}
	
	public Vector<Integer> get_D2_Road(){
		return D2_Road;
	}
	/****************************************************************************************************************/
	public int set_D3_Road(Vector<Integer> input){
		D3_Road = input;
		return D3_Road.size();
	}
	
	public int get_D3_Road_size(){
		return D3_Road.size();
	}
	
	public Vector<Integer> get_D3_Road(){
		return D3_Road;
	}
	/****************************************************************************************************************/
	public int set_D4_Road(Vector<Integer> input){
		D4_Road = input;
		return D4_Road.size();
	}
	
	public int get_D4_Road_size(){
		return D4_Road.size();
	}
	
	public Vector<Integer> get_D4_Road(){
		return D4_Road;
	}
	/****************************************************************************************************************/
	public int set_D5_Road(Vector<Integer> input){
		D5_Road = input;
		return D5_Road.size();
	}
	
	public int get_D5_Road_size(){
		return D5_Road.size();
	}
	
	public Vector<Integer> get_D5_Road(){
		return D5_Road;
	}
	/*****************************      Player's Rotate    ***********************************************************/
	public int set_P1_Rotate(Vector<Integer> input){
		P1_Rotate = input;
		return P1_Rotate.size();
	}
	
	public int get_P1_Rotate_size(){
		return P1_Rotate.size();
	}
	
	public Vector<Integer> get_P1_Rotate(){
		return P1_Rotate;
	}
	/****************************************************************************************************************/
	public int set_P2_Rotate(Vector<Integer> input){
		P2_Rotate = input;
		return P2_Rotate.size();
	}
	
	public int get_P2_Rotate_size(){
		return P2_Rotate.size();
	}
	
	public Vector<Integer> get_P2_Rotate(){
		return P2_Rotate;
	}
	/****************************************************************************************************************/
	public int set_P3_Rotate(Vector<Integer> input){
		P3_Rotate = input;
		return P3_Rotate.size();
	}
	
	public int get_P3_Rotate_size(){
		return P3_Rotate.size();
	}
	
	public Vector<Integer> get_P3_Rotate(){
		return P3_Rotate;
	}
	/****************************************************************************************************************/
	public int set_P4_Rotate(Vector<Integer> input){
		P4_Rotate = input;
		return P4_Rotate.size();
	}
	
	public int get_P4_Rotate_size(){
		return P4_Rotate.size();
	}
	
	public Vector<Integer> get_P4_Rotate(){
		return P4_Rotate;
	}
	/****************************************************************************************************************/
	public int set_P5_Rotate(Vector<Integer> input){
		P5_Rotate = input;
		return P5_Rotate.size();
	}
	
	public int get_P5_Rotate_size(){
		return P5_Rotate.size();
	}
	
	public Vector<Integer> get_P5_Rotate(){
		return P5_Rotate;
	}
	/*****************************      Defender's Rotate    ***********************************************************/
	public int set_D1_Rotate(Vector<Integer> input){
		D1_Rotate = input;
		return D1_Rotate.size();
	}
	
	public int get_D1_Rotate_size(){
		return D1_Rotate.size();
	}
	
	public Vector<Integer> get_D1_Rotate(){
		return D1_Rotate;
	}
	/****************************************************************************************************************/
	public int set_D2_Rotate(Vector<Integer> input){
		D2_Rotate = input;
		return D2_Rotate.size();
	}
	
	public int get_D2_Rotate_size(){
		return D2_Rotate.size();
	}
	
	public Vector<Integer> get_D2_Rotate(){
		return D2_Rotate;
	}
	/****************************************************************************************************************/
	public int set_D3_Rotate(Vector<Integer> input){
		D3_Rotate = input;
		return D3_Rotate.size();
	}
	
	public int get_D3_Rotate_size(){
		return D3_Rotate.size();
	}
	
	public Vector<Integer> get_D3_Rotate(){
		return D3_Rotate;
	}
	/****************************************************************************************************************/
	public int set_D4_Rotate(Vector<Integer> input){
		D4_Rotate = input;
		return D4_Rotate.size();
	}
	
	public int get_D4_Rotate_size(){
		return D4_Rotate.size();
	}
	
	public Vector<Integer> get_D4_Rotate(){
		return D4_Rotate;
	}
	/****************************************************************************************************************/
	public int set_D5_Rotate(Vector<Integer> input){
		D5_Rotate = input;
		return D5_Rotate.size();
	}
	
	public int get_D5_Rotate_size(){
		return D5_Rotate.size();
	}
	
	public Vector<Integer> get_D5_Rotate(){
		return D5_Rotate;
	}
	/******************************         RunBag          **********************************************************/
	public int set_RunBag(Vector<Vector <Integer>> input){
		RunBag = input;
		return RunBag.size();
	}
	
	public int get_RunBag_size(){
		return RunBag.size();
	}
	
	public Vector<Vector <Integer>> get_RunBag(){
		return RunBag;
	}
	
	
}
