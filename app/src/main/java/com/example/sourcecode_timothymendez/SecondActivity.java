package com.example.sourcecode_timothymendez;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

public class SecondActivity extends AppCompatActivity {

    private String selectedAction = "";
//    private DatabaseHelper databaseHelper;
    private int practiceNumber = 0;
    static final int MAXIMUM_VIDEOS = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
//        databaseHelper = new DatabaseHelper(this);

        Bundle extras = getIntent().getExtras();

        Intent transitionToScreen1Intent = new Intent(this, MainActivity.class);
        Intent transitionToScreen3Intent = new Intent(this, ThirdActivity.class);

        Button previousScreenButton = (Button) findViewById(R.id.backScreen1);
        previousScreenButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(transitionToScreen1Intent);
                    }
                }
        );

        Button nextScreenButton = (Button) findViewById(R.id.practiceButton);
        nextScreenButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(transitionToScreen3Intent);
                    }
                }
        );

        if (extras == null) {
//            previousScreenButton.performClick();
        } else {
            selectedAction = extras.getString("selectedAction");
            transitionToScreen3Intent.putExtra("selectedAction", selectedAction);
        }
//        Cursor dataCursor = databaseHelper.getData(databaseHelper.renameGesture(selectedAction));
//
//        while (dataCursor.moveToNext()) {
//            practiceNumber = dataCursor.getColumnIndexOrThrow(databaseHelper.getColumnPracticeNumber());
//        }
//        dataCursor.close();

        TextView textView = findViewById(R.id.textView2);
        textView.setText("Remaining" + (MAXIMUM_VIDEOS - practiceNumber) + " attempts for" + selectedAction);

        VideoView videoView = (VideoView) findViewById(R.id.videoView);
        videoView.setVideoPath("android.resource://" + getPackageName() + "/" + findVideo(selectedAction));
        videoView.start();

        Button replayVideoButton = (Button) findViewById(R.id.replayVideoButton);
        replayVideoButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        videoView.start();
                    }
                }
        );

        TextView videoName = (TextView) findViewById(R.id.videoName);
    }

    private int findVideo(String selectedAction) {
        int videoFile = 0;
        // find the video in accordance with the selected action
        switch (selectedAction) {
            case "0":
                videoFile = R.raw.h0;
                break;
            case "1":
                videoFile = R.raw.h1;
                ;
                break;
            case "2":
                videoFile = R.raw.h2;
                break;
            case "3":
                videoFile = R.raw.h3;
                break;
            case "4":
                videoFile = R.raw.h4;
                break;
            case "5":
                videoFile = R.raw.h5;
                break;
            case "6":
                videoFile = R.raw.h6;
                break;
            case "7":
                videoFile = R.raw.h7;
                break;
            case "8":
                videoFile = R.raw.h8;
                ;
                break;
            case "9":
                videoFile = R.raw.h9;
                break;
            case "Turn On Light":
                videoFile = R.raw.lighton;
                break;
            case "Turn Off Light":
                videoFile = R.raw.lightoff;
                break;
            case "Turn On Fan":
                videoFile = R.raw.fanon;
                break;
            case "Turn Off Fan":
                videoFile = R.raw.fanoff;
                break;
            case "Increase Fan Speed":
                videoFile = R.raw.increasefanspeed;
                break;
            case "Decrease Fan Speed":
                videoFile = R.raw.decreasefanspeed;
                break;
            case "Set Thermostat to Specified Temperature":
                videoFile = R.raw.setthermo;
                break;
            default:
                videoFile = 0;
                break;
        }

        return videoFile;
    }

}