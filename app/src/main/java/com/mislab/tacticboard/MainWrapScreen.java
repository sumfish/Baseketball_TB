package com.mislab.tacticboard;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MainWrapScreen extends Fragment {

    private MainWrap mainWrap;
    private IsScreen ask;

    //同步紀錄詢問擋人的時候: player & screen bar ID
    private int whichPlayer; //根據ID拿不同顏色的bar
    private int recordID;

    private int nowX,nowY;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_screen, container,false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        nowX=0;
        nowY=0;
        whichPlayer=0;
        recordID=0;
    }

    // 放詢問是否擋人的layout
    public void createIsScreenLayout(int x,int y, int rotateWhich, int id){
        whichPlayer=rotateWhich;
        recordID=id;
        nowX=x;
        nowY=y;

        RelativeLayout mainWrapScreen = (RelativeLayout) getView().findViewById(R.id.mainfrag_screen);
        ask = new IsScreen(getActivity());
        LinearLayout.LayoutParams screenLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ask.setX(nowX+100);
        ask.setY(nowY+90);
        ask.setLayoutParams(screenLayoutParams);
        mainWrapScreen.addView(ask);
    }

    public void removeScreenLayout(){
        RelativeLayout mainWrapScreen = (RelativeLayout) getView().findViewById(R.id.mainfrag_screen);
        mainWrapScreen.removeView(ask);
    }

    public int[] getScreenLayoutPosition(){
        return new int[]{nowX+100,nowY+90,nowX+100+ask.getWidth(),nowY+90+ask.getHeight()};
    }

    // call the put screen bar fun in mainwrap (in different layout)
    public void passInfo2CreateScreenBar(){
        mainWrap=(MainWrap)getActivity().getFragmentManager().findFragmentById(R.id.MainWrap_frag);
        mainWrap.putScreenBar(nowX,nowY,whichPlayer,recordID);
    }

}
