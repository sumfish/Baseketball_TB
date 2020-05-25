package com.example.helloworld;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

/**
 * Created by Tsai on 2018/3/3.
 */

public class PerspectiveSelect extends Fragment {
    private InetAddress serverAddr;
    private Button btn_3PP, btn_1, btn_2, btn_3, btn_4, btn_5;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            serverAddr = InetAddress.getByName("192.168.11.127");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return inflater.inflate(R.layout.perspecitve_layout, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().findViewById(R.id.perspecitve_select).setVisibility(View.GONE);
    }

    public void Mainfrag_Set_UDP_IP(InetAddress IP,int port){
        serverAddr = IP;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        btn_3PP = (Button) getView().findViewById(R.id.btn_select_3PP);
        btn_3PP.setOnClickListener(btn_3PP_Listener);

        btn_1 = (Button) getView().findViewById(R.id.btn_select_1PP_1);
        btn_1.setOnClickListener(btn_1_Listener);

        btn_2 = (Button) getView().findViewById(R.id.btn_select_1PP_2);
        btn_2.setOnClickListener(btn_2_Listener);

        btn_3 = (Button) getView().findViewById(R.id.btn_select_1PP_3);
        btn_3.setOnClickListener(btn_3_Listener);

        btn_4 = (Button) getView().findViewById(R.id.btn_select_1PP_4);
        btn_4.setOnClickListener(btn_4_Listener);

        btn_5 = (Button) getView().findViewById(R.id.btn_select_1PP_5);
        btn_5.setOnClickListener(btn_5_Listener);

    }

    private View.OnClickListener btn_3PP_Listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            JSONObject packet = new JSONObject();
            try {
                packet.put("Perspective", 0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            final String string_packet = packet.toString();

            Thread socket_thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Socket clientSocket = null;
                    DataOutputStream dataOutputStream = null;

                    try {
                        clientSocket = new Socket(serverAddr, 2044);
                        dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
                        Log.d("debug", "Bytes size:"+string_packet.length());

                        dataOutputStream.write(string_packet.getBytes());
                        clientSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            socket_thread.start();
            Log.d("debug", "Send to Server!");
        }
    };

    private View.OnClickListener btn_1_Listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            JSONObject packet = new JSONObject();
            try {
                packet.put("Perspective", 1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            final String string_packet = packet.toString();
            Log.d("debug", string_packet);
            Thread socket_thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Socket clientSocket = null;
                    DataOutputStream dataOutputStream = null;

                    try {
                        clientSocket = new Socket(serverAddr, 2044);
                        dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
                        Log.d("debug", "Bytes size:"+string_packet.length());

                        dataOutputStream.write(string_packet.getBytes());
                        clientSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            socket_thread.start();
            Log.d("debug", "Send to Server!");
        }
    };

    private View.OnClickListener btn_2_Listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            JSONObject packet = new JSONObject();
            try {
                packet.put("Perspective", 2);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            final String string_packet = packet.toString();

            Thread socket_thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Socket clientSocket = null;
                    DataOutputStream dataOutputStream = null;

                    try {
                        clientSocket = new Socket(serverAddr, 2044);
                        dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
                        Log.d("debug", "Bytes size:"+string_packet.length());

                        dataOutputStream.write(string_packet.getBytes());
                        clientSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            socket_thread.start();
            Log.d("debug", "Send to Server!");
        }
    };

    private View.OnClickListener btn_3_Listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            JSONObject packet = new JSONObject();
            try {
                packet.put("Perspective", 3);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            final String string_packet = packet.toString();

            Thread socket_thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Socket clientSocket = null;
                    DataOutputStream dataOutputStream = null;

                    try {
                        clientSocket = new Socket(serverAddr, 2044);
                        dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
                        Log.d("debug", "Bytes size:"+string_packet.length());

                        dataOutputStream.write(string_packet.getBytes());
                        clientSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            socket_thread.start();
            Log.d("debug", "Send to Server!");
        }
    };

    private View.OnClickListener btn_4_Listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            JSONObject packet = new JSONObject();
            try {
                packet.put("Perspective", 4);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            final String string_packet = packet.toString();

            Thread socket_thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Socket clientSocket = null;
                    DataOutputStream dataOutputStream = null;

                    try {
                        clientSocket = new Socket(serverAddr, 2044);
                        dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
                        Log.d("debug", "Bytes size:"+string_packet.length());

                        dataOutputStream.write(string_packet.getBytes());
                        clientSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            socket_thread.start();
            Log.d("debug", "Send to Server!");
        }
    };

    private View.OnClickListener btn_5_Listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            JSONObject packet = new JSONObject();
            try {
                packet.put("Perspective", 5);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            final String string_packet = packet.toString();

            Thread socket_thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Socket clientSocket = null;
                    DataOutputStream dataOutputStream = null;

                    try {
                        clientSocket = new Socket(serverAddr, 2044);
                        dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
                        Log.d("debug", "Bytes size:"+string_packet.length());

                        dataOutputStream.write(string_packet.getBytes());
                        clientSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            socket_thread.start();
            Log.d("debug", "Send to Server!");
        }
    };



}

