package com.example.sourcecode_timothymendez;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class ThirdActivity extends AppCompatActivity {

    static final int REQUEST_VIDEO_CAPTURE = 1;
    private String selectedAction = "";
    private int practiceNumber = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        Bundle extras = getIntent().getExtras();

        Intent transitionToScreen2Intent = new Intent(this, SecondActivity.class);

        if (hasCamera()) {

        }

        Button previousScreenButton = (Button) findViewById(R.id.returnScreen2);
        previousScreenButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(transitionToScreen2Intent);
                    }
                }
        );

        if (extras == null) {
            previousScreenButton.performClick();
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
                practiceNumber++;
            } else if (resultCode == RESULT_CANCELED) {
                Toastyyy("Video Cancelled");
            } else {
                Toastyyy("Failed to Record Video");
            }
        }
    }

    private void dispatchTakeVideoIntent(String selectedAction) {
        File practiceVideo = new File(
                Environment.getExternalStorageDirectory().getAbsolutePath()
                        + "/" + selectedAction + "_PRACTICE_" + practiceNumber + "Mendez" + ".mp4"
        );
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        Uri videoUri = Uri.fromFile(practiceVideo);
        takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        } else {
            Toastyyy("Error openening camera");
        }
    }

    private Boolean hasCamera() {
        return getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_ANY
        );
    }

    private void Toastyyy(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}