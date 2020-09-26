package com.mislab.tacticboard;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Tsai on 2018/3/3.
 */

public class PerspectiveSelect extends Fragment {
    private InetAddress serverAddr;
    private Button[] btnList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            serverAddr = InetAddress.getByName("192.168.11.127");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return inflater.inflate(R.layout.fragment_perspective_select, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().findViewById(R.id.perspecitve_select).setVisibility(View.GONE);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        btnList[0] = (Button) getView().findViewById(R.id.btn_select_3PP);
        btnList[1] = (Button) getView().findViewById(R.id.btn_select_1PP_1);
        btnList[2] = (Button) getView().findViewById(R.id.btn_select_1PP_2);
        btnList[3] = (Button) getView().findViewById(R.id.btn_select_1PP_3);
        btnList[4] = (Button) getView().findViewById(R.id.btn_select_1PP_4);
        btnList[5] = (Button) getView().findViewById(R.id.btn_select_1PP_5);
        btnList = new Button[6];
        for(int i=0 ; i<6 ; i++)
            setOnClick(btnList[i], i);
    }

    private void setOnClick(final Button btn, final int id){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject packet = new JSONObject();
                try {
                    packet.put("Perspective", id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final String stringPacket = packet.toString();

                Thread socket_thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Socket clientSocket = null;
                        DataOutputStream dataOutputStream = null;

                        try {
                            clientSocket = new Socket(serverAddr, 2044);
                            dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
                            Log.d("debug", "Bytes size:"+stringPacket.length());

                            dataOutputStream.write(stringPacket.getBytes());
                            clientSocket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                socket_thread.start();
                Log.d("debug", "Send to Server!");
            }
        });
    }
}

