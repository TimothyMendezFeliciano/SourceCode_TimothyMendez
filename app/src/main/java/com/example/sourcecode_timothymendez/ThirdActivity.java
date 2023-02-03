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
import android.widget.Toast;

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
    private String selectedAction = "";
    private int practiceNumber = 1;
    static final int MAXIMUM_VIDEOS = 3;
    Button recordVideoButton;
    Button previousScreenButton;
    TextView errorLogger;
    ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        errorLogger = (TextView) findViewById(R.id.errorLogger);
        spinner = (ProgressBar) findViewById(R.id.progressBar);

        spinner.setVisibility(View.GONE);

        if (!hasFrontCamera()) {
            Toastyyy("Does Not Have Front Camera");
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

        if (extras != null) {
            selectedAction = extras.getString("selectedAction");
            transitionToScreen2Intent.putExtra("selectedAction", selectedAction);
        }

        // TODO: Create Service call to verify only 3 videos per action are saved.
        if (practiceNumber >= MAXIMUM_VIDEOS) {
            Toastyyy("Only 3 videos pero action");
            return;
        }
        dispatchTakeVideoIntent(selectedAction);
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_VIDEO_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Uri videoUri = data.getData();
                uploadVideoToDatabase(selectedAction, videoUri);

            } else if (resultCode == RESULT_CANCELED) {
                Toastyyy("Video Cancelled");
            } else {
                Toastyyy("Failed to Record Video");
            }
        }
    }

    private void dispatchTakeVideoIntent(String selectedAction) {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 5);
        takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, renameGesture(selectedAction) + "_PRACTICE_" + practiceNumber + ".mp4");
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        } else {
            Toastyyy("Error openening camera");
        }
    }

    private void uploadVideoToDatabase(String selectedAction, Uri videoUri) {
        Toastyyy(videoUri.getPath());

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        StorageReference videosRef = storageRef.child("videos/" + renameGesture(selectedAction) + "_PRACTICE_" + practiceNumber);

        UploadTask uploadTask = videosRef.putFile(videoUri);

        uploadTask.addOnProgressListener(snapshot -> {
            previousScreenButton.setEnabled(false);
            recordVideoButton.setEnabled(false);
            spinner.setVisibility(View.VISIBLE);
        });

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toastyyy("Upload Failed");
                errorLogger.setText(e.getCause().toString());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toastyyy("Upload Succesful!");
                errorLogger.setText("Record another video. " + (MAXIMUM_VIDEOS - practiceNumber) + " remaining.");
            }
        });
        spinner.setVisibility(View.GONE);
        recordVideoButton.setEnabled(true);
        previousScreenButton.setEnabled(true);
    }

//    private File renameFile(Uri videoUri, int practiceNumber) throws IOException {
//        File originalFile = new File(getFilePath(videoUri));
//        String newFileName = renameGesture(selectedAction) + "_PRACTICE_" + practiceNumber;
//        File renamedFile = new File(originalFile.getParent(), newFileName);
//
////        return new File(
////                Files.move(
////                        originalFile.toPath(),
////                        renamedFile.toPath(),
////                        REPLACE_EXISTING
////                ).toString()
////        );
//
//        if (originalFile.renameTo(renamedFile)) {
//            Toastyyy("Rename Succesful!");
//            return originalFile;
//        } else {
//            Toastyyy("Unable to rename file");
//            return renamedFile;
//        }
//    }
//
//    private String getFilePath(Uri videoUri) {
//        Cursor cursor = null;
//        try {
//            String[] proj = {MediaStore.Images.Media.DATA};
//            cursor = getContentResolver().query(videoUri, proj, null, null, null);
//            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//            cursor.moveToFirst();
//            return cursor.getString(columnIndex);
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//        }
//    }

    private Boolean hasFrontCamera() {
        return getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_FRONT
        );
    }

    private void Toastyyy(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private String renameGesture(String selectedAction) {
        switch (selectedAction) {
            case "0":
                return "Num0";
            case "1":
                return "Num1";
            case "2":
                return "Num2";
            case "3":
                return "Num3";
            case "4":
                return "Num4";
            case "5":
                return "Num5";
            case "6":
                return "Num6";
            case "7":
                return "Num7";
            case "8":
                return "Num8";
            case "9":
                return "Num9";
            case "Turn On Light":
                return "LightOn";
            case "Turn Off Light":
                return "LightOff";
            case "Turn On Fan":
                return "FanOn";
            case "Turn Off Fan":
                return "FanOff";
            case "Increase Fan Speed":
                return "FanUp";
            case "Decrease Fan Speed":
                return "FanDown";
            case "Set Thermostat to Specified Temperature":
                return "SetThermo";
            default:
                return "";
        }
    }

}