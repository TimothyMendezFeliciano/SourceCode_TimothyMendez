package com.example.sourcecode_timothymendez;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ThirdActivity extends AppCompatActivity {

    static final int REQUEST_VIDEO_CAPTURE = 1;
    static final int SELECT_VIDEO_REQUEST_CODE = 5;
    private String selectedAction = "";
    private int practiceNumber = 0;
    Button uploadVideoButton;
    Button recordVideoButton;
    Button previousScreenButton;
    TextView errorLogger;
    ProgressBar spinner;
    Helper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        helper = new Helper();
        errorLogger = (TextView) findViewById(R.id.errorLogger);
        spinner = (ProgressBar) findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);

        if (!hasFrontCamera()) {
            helper.Toastyyy(getApplicationContext(), "Does Not Have Front Camera");
            return;
        }

        Bundle extras = getIntent().getExtras();

        Intent transitionToScreen2Intent = new Intent(this, SecondActivity.class);

        previousScreenButton = (Button) findViewById(R.id.returnScreen2);
        previousScreenButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(transitionToScreen2Intent);
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

        uploadVideoButton = (Button) findViewById(R.id.uploadVideo);
        uploadVideoButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectVideoToUpload();
                    }
                }
        );

        if (extras != null) {
            selectedAction = extras.getString("selectedAction");
            transitionToScreen2Intent.putExtra("selectedAction", selectedAction);
        }

        dispatchTakeVideoIntent(selectedAction);
    }

    private void dispatchTakeVideoIntent(String selectedAction) {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        takeVideoIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
        takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 5);
        takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, helper.renameGesture(selectedAction) + "_PRACTICE_" + practiceNumber + ".mp4");
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        } else {
            helper.Toastyyy(getApplicationContext(), "Error opening camera");
        }
    }

    private void selectVideoToUpload() {
        Intent selectVideoIntent = new Intent().setType("video/*").putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false).setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(selectVideoIntent, "Select Video"), SELECT_VIDEO_REQUEST_CODE);
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_VIDEO_CAPTURE || requestCode == SELECT_VIDEO_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri videoUri = data.getData();
                uploadVideoToDatabase(selectedAction, videoUri);
                practiceNumber++;
            } else if (resultCode == RESULT_CANCELED) {
                helper.Toastyyy(getApplicationContext(), "Video Cancelled");
            } else {
                helper.Toastyyy(getApplicationContext(), "Failed to Record Video");
            }
        }
    }


    private void uploadVideoToDatabase(String selectedAction, Uri videoUri) {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        StorageReference videosRef = storageRef.child(helper.renameGesture(selectedAction)).child(helper.renameGesture(selectedAction) + "_PRACTICE_" + practiceNumber);

        UploadTask uploadTask = videosRef.putFile(videoUri);

        uploadTask.addOnProgressListener(snapshot -> {
            previousScreenButton.setEnabled(false);
            recordVideoButton.setEnabled(false);
            uploadVideoButton.setEnabled(false);
            spinner.setVisibility(View.VISIBLE);
        });

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                helper.Toastyyy(getApplicationContext(), "Upload Failed");
                errorLogger.setText(e.getCause().toString());
                spinner.setVisibility(View.GONE);
                recordVideoButton.setEnabled(true);
                previousScreenButton.setEnabled(true);
                uploadVideoButton.setEnabled(true);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                helper.Toastyyy(getApplicationContext(), "Upload Succesful!");
                errorLogger.setText("Record another video.");
                practiceNumber++;
                spinner.setVisibility(View.GONE);
                recordVideoButton.setEnabled(true);
                previousScreenButton.setEnabled(true);
                uploadVideoButton.setEnabled(true);
            }
        });

    }

    private Boolean hasFrontCamera() {
        return getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_FRONT
        );
    }

}