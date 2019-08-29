package com.example.sba_project.GameRoomPkg;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.sba_project.R;
import com.example.sba_project.Userdata.ExtendedMyUserData;
import com.example.sba_project.Userdata.UserDataManager;
import com.example.sba_project.Util.Text_detec;
import com.google.firebase.database.FirebaseDatabase;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.FileNotFoundException;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class Game_Play_Frag extends Fragment
        implements SensorEventListener {
    private static final int IMAGE_PICK_CAMERA_CODE = 100;
    private static final int CAMERA_REQUEST_CODE = 200;
    static final int TEXT_DETEC = 1001;

    public static final int TEXT_DETEC_RESULT_OK = 3000;

    private Uri image_uri = null;
    private TextView scoreView = null;
    private Chronometer chronometer;
    private SensorManager sensorManager;
    private Sensor stepCountSensor;
    private GameUser _GameUser;
    private String _CategoryName = "";
    TextView calories;
    TextView nickname;
    boolean isRunning;
    float kcal;
    SensorEvent tmpEvent = null;
    public static float Minus = 0.f;

    public Game_Play_Frag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_game__play_, container, false);
//        return inflater.inflate(R.layout.fragment_game__play_, container, false);

        calories = (TextView)rootView.findViewById(R.id.calorie);
        sensorManager = (SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);
        stepCountSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(stepCountSensor == null) {
            Toast.makeText(getContext(), "No Step Detect Sensor", Toast.LENGTH_SHORT).show();
        }

        rootView.findViewById(R.id.btn_exit).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UpdateUserData();
                getActivity().finish();
            }
        });

        rootView.findViewById(R.id.inputScore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickCamera();
            }
        });

        rootView.findViewById(R.id.profile_image2);

        Glide.with(getActivity()).load(UserDataManager.getInstance().getCurUserData().PhotoUrl).into((ImageView)rootView.findViewById(R.id.profile_image2));

        nickname = rootView.findViewById(R.id.nickname2);

        scoreView = rootView.findViewById(R.id.score);

        chronometer = (Chronometer)rootView.findViewById(R.id.chronometer);
        chronometer.stop();

        return rootView;
    }

    private void UpdateUserData(){
        class GamePlayData{
            public int kcal = 0;
            public int score = 0;
            public GamePlayData(){}
            public GamePlayData(int kcal, int score){
                this.kcal = kcal;
                this.score = score;
            }
        }

        GamePlayData newData = new GamePlayData(Math.round(kcal) ,Integer.parseInt(scoreView.getText().toString().trim()));

        String date[] = _GameUser.Date.split(" ");

        FirebaseDatabase.getInstance().getReference().child("Game").child("uid").child(UserDataManager.getInstance().getCurUserData().uID).child(_CategoryName ).child(date[0]).child(date[1]).setValue(newData);
    }

    public void StartPlay(final GameUser _user, final String CategoryName){
        _GameUser = _user;

        nickname.setText(_GameUser.nickName);
        _CategoryName = CategoryName;
        isRunning = true;
        calories.setText("0kcal");

        chronometer.start();

        if(tmpEvent != null){
            Minus = tmpEvent.values[0];
        }
    }

    private void pickCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "NewPic"); //title of the picture
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image To text"); //description
        image_uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TEXT_DETEC:
                if(resultCode == TEXT_DETEC_RESULT_OK){
                    String resultStr = data.getStringExtra("Result");
                    scoreView.setText(resultStr);
                    //UserDataManager.getInstance().getGameRoom().UpdateUserScore(Integer.parseInt(resultStr));
                }
                break;
            case IMAGE_PICK_CAMERA_CODE: {
                if (resultCode == RESULT_OK) {
                    //get drawable bitmap for text recognition

                    CropImage.activity(image_uri)
                            .setGuidelines(CropImageView.Guidelines.ON) //enable image guidlines
                            .start(getContext(), this);
                }
            }
            break;
            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE: {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    Uri resultUri = result.getUri(); //get image uri

                    Intent intent = new Intent(getContext(), Text_detec.class);
                    intent.putExtra("uri", resultUri);
                    startActivityForResult(intent, TEXT_DETEC);
                }
            }
            break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        UpdateUserData();
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, stepCountSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType()==Sensor.TYPE_STEP_COUNTER && isRunning == true){
            if(tmpEvent == null)
                tmpEvent = event;
            kcal = (int)(event.values[0] - Minus)/30;
            calories.setText(String.valueOf((int)kcal+"kcal"));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
