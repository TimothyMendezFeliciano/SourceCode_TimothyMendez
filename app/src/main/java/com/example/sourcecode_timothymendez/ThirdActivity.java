package com.example.sourcecode_timothymendez;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.util.List;

public class ThirdActivity extends AppCompatActivity {

    static final int REQUEST_VIDEO_CAPTURE = 1;
    static final int SELECT_VIDEO = 3;
    private String selectedAction = "";
    private int practiceNumber = 1;
    private int videoSelected = 0;
    private String[] selectedPath = new String[3];
    static final int MAXIMUM_VIDEOS = 3;
    Button addVideoButton;
    Button recordVideoButton;
    ListView videoPreview;

    // TODO: LIMIT VIDEO TO 5 SECONDS

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        if (!hasFrontCamera()) {
            Toastyyy("Does Not Have Front Camera");
            return;
        }

        videoPreview = (ListView) findViewById(R.id.videoPreview);

        Bundle extras = getIntent().getExtras();

        Intent transitionToScreen2Intent = new Intent(this, SecondActivity.class);

        Button previousScreenButton = (Button) findViewById(R.id.returnScreen2);
        previousScreenButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(transitionToScreen2Intent);
                    }
                }
        );

        addVideoButton = (Button) findViewById(R.id.addVideo);
        addVideoButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setType("video/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select a Video"), SELECT_VIDEO);
                    }
                }
        );

        recordVideoButton = (Button) findViewById(R.id.recordVideo);
        recordVideoButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dispatchTakeVideoIntent(selectedAction);
                    }
                }
        );

        if (extras == null) {
//            previousScreenButton.performClick();
        } else {
            selectedAction = extras.getString("selectedAction");
            transitionToScreen2Intent.putExtra("selectedAction", selectedAction);
        }

        dispatchTakeVideoIntent(selectedAction);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_VIDEO_CAPTURE) {
            if (resultCode == RESULT_OK) {
                // TODO: Upload Video to Server. Can be done by opening Video Gallery.
                Toastyyy("Video Recorded. Practice #" + practiceNumber);
                Toastyyy(data.getDataString());
                practiceNumber++;
            } else if (resultCode == RESULT_CANCELED) {
                Toastyyy("Video Cancelled");
            } else {
                Toastyyy("Failed to Record Video");
            }
        }

        if (requestCode == SELECT_VIDEO) {
//            if (videoSelected >= MAXIMUM_VIDEOS) return;
            if (resultCode == RESULT_OK) {
                Uri selectedVideoUri = data.getData();
                Toastyyy(selectedVideoUri.toString());
//                TODO: This getFilePath method is buggy. Must write new one to get video.
//                selectedPath[videoSelected] = getFilePath(selectedVideoUri);
                selectedPath[videoSelected] = selectedVideoUri.toString();
                videoSelected++;
                for (int i = 0; i <= videoSelected; i++) {
                    TextView textViewForVideoPreview = new TextView(this);
                    textViewForVideoPreview.setText(selectedPath[i]);
                    videoPreview.addHeaderView(textViewForVideoPreview);
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toastyyy("Video Selection Canceled");
            } else {
                Toastyyy("Failed to Select Video");
            }
        }
    }

    private void dispatchTakeVideoIntent(String selectedAction) {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, selectedAction + "_PRACTICE_" + practiceNumber + "Mendez" + ".mp4");
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        } else {
            Toastyyy("Error openening camera");
        }
    }

    private Boolean hasFrontCamera() {
        return getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_FRONT
        );
    }

    private void Toastyyy(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private String getFilePath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);

        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null
        );
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(MediaStore.Video.Media.DATA);
        if (!(columnIndex >= 0)) {
            Toastyyy("Column Index Not Bigger or Equal 0");
            return "";
        }
        String path = cursor.getString(columnIndex);
        cursor.close();

        return path;
    }
}