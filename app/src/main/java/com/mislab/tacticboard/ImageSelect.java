package com.mislab.tacticboard;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
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
import java.nio.ByteBuffer;

/**
 * Created by Tsai on 2018/3/3.
 */

public class ImageSelect extends Fragment {
    public static final int SELECT_PIC_BY_PICK_PHOTO = 2;
    public  static Bitmap [] selectedImage = new Bitmap[5];
    public static boolean [] hasSelected = new boolean[5];
    public static int [] imageWidth = new int[5];
    public static int [] imageHeight = new int[5];
    private static Bitmap [] originImage;
    private Button btnImageSelectPg, btnImageSelectSg, btnImageSelectSf, btnImageSelectPf, btnImageSelectC;
    private Button btn_send_bitmap;
    private int currentSelectImage = 0; // 0 - 4

    private InetAddress serverAddr;
    private int UDP_SERVER_PORT = 3983;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image_select, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        originImage = new Bitmap[5];
        getActivity().findViewById(R.id.image_select).setVisibility(View.GONE);
    }

    public void Mainfrag_Set_UDP_IP(InetAddress IP,int port){
        serverAddr = IP;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btnImageSelectPg = (Button) getView().findViewById(R.id.btn_select_pg);
        btnImageSelectPg.setOnClickListener(btn_image_select_pg_Listener);

        btnImageSelectSg = (Button) getView().findViewById(R.id.btn_select_sg);
        btnImageSelectSg.setOnClickListener(btn_image_select_sg_Listener);

        btnImageSelectSf = (Button) getView().findViewById(R.id.btn_select_sf);
        btnImageSelectSf.setOnClickListener(btn_image_select_sf_Listener);

        btnImageSelectPf = (Button) getView().findViewById(R.id.btn_select_pf);
        btnImageSelectPf.setOnClickListener(btn_image_select_pf_Listener);

        btnImageSelectC = (Button) getView().findViewById(R.id.btn_select_c);
        btnImageSelectC.setOnClickListener(btn_image_select_c_Listener);

        btn_send_bitmap = (Button)getView().findViewById(R.id.btn_send);
        btn_send_bitmap.setOnClickListener(btn_send_bitmap_Listener);
    }

    private View.OnClickListener btn_image_select_pg_Listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            currentSelectImage = 0;
            startActivityForResult(intent, SELECT_PIC_BY_PICK_PHOTO);
        }
    };

    private View.OnClickListener btn_image_select_sg_Listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            currentSelectImage = 1;
            startActivityForResult(intent, SELECT_PIC_BY_PICK_PHOTO);
        }
    };

    private View.OnClickListener btn_image_select_sf_Listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            currentSelectImage = 2;
            startActivityForResult(intent, SELECT_PIC_BY_PICK_PHOTO);
        }
    };

    private View.OnClickListener btn_image_select_pf_Listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            currentSelectImage = 3;
            startActivityForResult(intent, SELECT_PIC_BY_PICK_PHOTO);
        }
    };

    private View.OnClickListener btn_image_select_c_Listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            currentSelectImage = 4;
            startActivityForResult(intent, SELECT_PIC_BY_PICK_PHOTO);
        }
    };

    private View.OnClickListener btn_send_bitmap_Listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            JSONObject packet = new JSONObject();
            for(int i=0;i<5;i++){
                String out_string;
                if(hasSelected[i]){
                    byte [] out_array = BitmapToByte(selectedImage[i]);
                    out_string = BytesToString(out_array);
                }else{
                    out_string = "";
                }
                try {
                    packet.put(String.valueOf(i), out_string);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            JSONArray setting_array = new JSONArray();

            for(int i=0;i<5;i++)
                setting_array.put(hasSelected[i]);

            try {
                packet.put("setting", setting_array);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            for(int i=0;i<5;i++){
                JSONArray size_array = new JSONArray();
                size_array.put(imageWidth[i]);
                size_array.put(imageHeight[i]);
                try {
                    packet.put(i+"_size", size_array);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            /*
            byte [] out_array = BitmapToByte(selectedImage[currentSelectImage]);
            final String out_string = BytesToString(out_array);
            JSONObject packet = new JSONObject();

            try {
                packet.put("image", out_string);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            */
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
            Log.d("debug", "Send Image to Server!");
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("debug", "requestCode" + requestCode);

        if (requestCode == SELECT_PIC_BY_PICK_PHOTO) {
            try {

                final Uri imageUri = data.getData();
                final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                originImage[currentSelectImage] = BitmapFactory.decodeStream(imageStream);
                Bitmap testImage = originImage[currentSelectImage].copy(Bitmap.Config.ARGB_8888, true);
                selectedImage[currentSelectImage] = testImage;

                // Rotate the image by 270 degree
                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(selectedImage[currentSelectImage],selectedImage[currentSelectImage].getWidth(),selectedImage[currentSelectImage].getHeight(),true);
                Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap , 0, 0, scaledBitmap .getWidth(), scaledBitmap .getHeight(), matrix, true);
                selectedImage[currentSelectImage] = rotatedBitmap;

                // Utilize OpenCV for face detection and clip the original image to 500*500 (only face part)
                Mat originalMat = new Mat (selectedImage[currentSelectImage].getWidth(), selectedImage[currentSelectImage].getHeight(), CvType.CV_8UC1);
                Mat grayMat = new Mat (selectedImage[currentSelectImage].getWidth(), selectedImage[currentSelectImage].getHeight(), CvType.CV_8UC1);
                Utils.bitmapToMat(selectedImage[currentSelectImage], originalMat);

                Imgproc.cvtColor(originalMat, grayMat, Imgproc.COLOR_RGB2GRAY);

                InputStream is = getResources().openRawResource(R.raw.haarcascade_frontalface_alt);
                File cascadeDir = getActivity().getDir("cascade", Context.MODE_PRIVATE);
                File mCascadeFile = new File(cascadeDir, "haarcascade_frontalface_alt.xml");
                try {
                    FileOutputStream os = new FileOutputStream(mCascadeFile);


                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = is.read(buffer)) != -1) {
                        os.write(buffer, 0, bytesRead);
                    }
                    is.close();
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                cascadeDir.delete();
                CascadeClassifier cascadeClassifier = new CascadeClassifier(mCascadeFile.getAbsolutePath());

                // There exist one face in the image
                Mat ROIimage = new Mat();
                Rect faceClip = new Rect();
                Bitmap testBmp = selectedImage[currentSelectImage];
                boolean hasDetectFace = false;
                int degree_count = 0;
                MatOfRect faces = new MatOfRect();
                if(cascadeClassifier != null){
                    cascadeClassifier.detectMultiScale(grayMat,faces, 1.2, 2, 2, new Size(100, 100), new Size());
                }
                Rect[] facesArray = faces.toArray();



                if(facesArray.length > 0){
                    faceClip = new Rect(new Point(facesArray[0].tl().x-100, facesArray[0].tl().y-100), new Size(facesArray[0].width+200, facesArray[0].height+200));
                    //Core.rectangle(originalMat, faceClip.tl(), faceClip.br(), new Scalar(0, 255, 0, 255), 5);
                    //Core.rectangle(originalMat, facesArray[0].tl(), facesArray[0].br(), new Scalar(0, 255, 0, 255), 5);

                    originalMat.submat(faceClip).copyTo(ROIimage);
                    testBmp = Bitmap.createBitmap(faceClip.width, faceClip.height, Bitmap.Config.ARGB_8888);
                    Utils.matToBitmap(ROIimage, testBmp);
                }
                else{
                    testBmp = selectedImage[currentSelectImage];
                }

                Log.d("debug", "bmp:"+selectedImage[currentSelectImage].getWidth()+","+selectedImage[currentSelectImage].getHeight());
                Log.d("debug", "Config"+selectedImage[currentSelectImage].getConfig());

                switch (currentSelectImage){
                    case 0:
                        btnImageSelectPg.setBackgroundDrawable(new BitmapDrawable(getResources(), testBmp));
                        break;
                    case 1:
                        btnImageSelectSg.setBackgroundDrawable(new BitmapDrawable(getResources(), testBmp));
                        break;
                    case 2:
                        btnImageSelectSf.setBackgroundDrawable(new BitmapDrawable(getResources(), testBmp));
                        break;
                    case 3:
                        btnImageSelectPf.setBackgroundDrawable(new BitmapDrawable(getResources(), testBmp));
                        break;
                    case 4:
                        btnImageSelectC.setBackgroundDrawable(new BitmapDrawable(getResources(), testBmp));
                        break;

                }
                hasSelected[currentSelectImage] = true;
                imageWidth[currentSelectImage] = faceClip.width;
                imageHeight[currentSelectImage] = faceClip.height;
                //Whether to send full image
                selectedImage[currentSelectImage] = testBmp;
                /*
                Mat tmpMat = new Mat (selectedImage[currentSelectImage].getWidth(), selectedImage[currentSelectImage].getHeight(), CvType.CV_8UC1);
                Utils.bitmapToMat(selectedImage[currentSelectImage], tmpMat);
                Mat desMat = new Mat();
                Imgproc.pyrDown(tmpMat, desMat);
                Bitmap finalBmp = selectedImage[currentSelectImage];
                Utils.matToBitmap(desMat, finalBmp);
                selectedImage[currentSelectImage] = finalBmp;
                */
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    public synchronized static byte[] MyBitmapToByte(Bitmap bitmap) {
/*
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
*/
        ByteBuffer byteBuffer = ByteBuffer.allocate(bitmap.getByteCount());
        bitmap.copyPixelsToBuffer(byteBuffer);
        return byteBuffer.array();

    }

    public synchronized static byte[] BitmapToByte(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public synchronized static String BytesToString(byte[] imagedata){
        return Base64.encodeToString(imagedata, Base64.DEFAULT);
    }

    public synchronized static Bitmap DrawableToBitmap(Drawable drawable){
        return (((android.graphics.drawable.BitmapDrawable) drawable).getBitmap());
    }

}

