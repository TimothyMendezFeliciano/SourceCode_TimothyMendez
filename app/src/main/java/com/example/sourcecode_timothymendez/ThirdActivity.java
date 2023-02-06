package com.example.sourcecode_timothymendez;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ThirdActivity extends AppCompatActivity {

    static final int REQUEST_VIDEO_CAPTURE = 1;
    //    static final int SELECT_VIDEO_REQUEST_CODE = 5;
//    private ArrayList<Uri> selectedVideos = new ArrayList<>();
    private String selectedAction = "";
    private int practiceNumber = 0;
    //    Button uploadVideoButton;
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

//        uploadVideoButton = (Button) findViewById(R.id.uploadVideo);
//        uploadVideoButton.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        selectVideosToUpload();
//                    }
//                }
//        );

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

//    private void selectVideosToUpload() {
//        Intent selectVideoIntent = new Intent().setType("video/*").putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true).setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(selectVideoIntent, "Select Videos"), SELECT_VIDEO_REQUEST_CODE);
//    }

    @SuppressLint("WrongConstant")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_VIDEO_CAPTURE) {
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
//        else if (requestCode == SELECT_VIDEO_REQUEST_CODE) {
//            if (resultCode == RESULT_OK) {
//                if (data.getClipData() != null) {
//                    int count = data.getClipData().getItemCount();
//                    for (int i = 0; i < count; i++) {
//                        selectedVideos.add(data.getClipData().getItemAt(i).getUri());
//                    }
//                } else {
//                    selectedVideos.add(data.getData());
//                }
//
//                String zipFileName = "MENDEZ_TIMOTHY_Gestures.zip";
//                String zipFileLocation = Environment.getExternalStorageDirectory() + "/" + zipFileName;
//                try {
//                    zipFiles(selectedVideos, zipFileLocation);
//                    uploadZippedVideosToDatabase(selectedAction, zipFileLocation);
//                } catch (IOException e) {
//                    helper.Toastyyy(getApplicationContext(), "Error uploading video");
//                    errorLogger.setText(e.getMessage());
//                }
//            } else if (resultCode == RESULT_CANCELED) {
//                helper.Toastyyy(getApplicationContext(), "Video Selection Cancelled");
//            } else {
//                helper.Toastyyy(getApplicationContext(), "Failed to Select Videos");
//            }
//        }
    }

//    private void zipFiles(ArrayList<Uri> videoFiles, String zipFileLocation) throws IOException {
//        ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFileLocation));
//        for (Uri fileUri : videoFiles) {
//            File file = new File(fileUri.getPath());
//            file.setReadable(true);
//            file.setWritable(true);
//            String fileName = helper.getFileName(getContentResolver(), fileUri);
//            FileInputStream fileInputStream = new FileInputStream(file);
//            ZipEntry zipEntry = new ZipEntry(fileName);
//            zipOutputStream.putNextEntry(zipEntry);
//            byte[] bytes = new byte[1024];
//            int length;
//            while ((length = fileInputStream.read(bytes)) >= 0) {
//                zipOutputStream.write(bytes, 0, length);
//            }
//            zipOutputStream.closeEntry();
//            fileInputStream.close();
//        }
//        zipOutputStream.close();
//    }
//
//    private void uploadZippedVideosToDatabase(String selectedAction, String zipFileLocation) {
//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        StorageReference storageReference = storage.getReference();
//        StorageReference zipFileReference = storageReference.child("Mendez_Feliciano_Gestures.zip");
//        Uri zippedFileUri = Uri.fromFile(new File(zipFileLocation));
//
//        UploadTask uploadTask = zipFileReference.putFile(zippedFileUri);
//
//        uploadTask.addOnProgressListener(snapshot -> {
//            previousScreenButton.setEnabled(false);
//            recordVideoButton.setEnabled(false);
//            spinner.setVisibility(View.VISIBLE);
//        });
//
//        uploadTask.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                helper.Toastyyy(getApplicationContext(), "Upload Failed");
//                errorLogger.setText(e.getCause().toString());
//            }
//        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                helper.Toastyyy(getApplicationContext(), "Upload Succesful!");
//                errorLogger.setText("Record another video.");
//                practiceNumber++;
//            }
//        });
//        spinner.setVisibility(View.GONE);
//        recordVideoButton.setEnabled(true);
//        previousScreenButton.setEnabled(true);
//
//    }

    private void uploadVideoToDatabase(String selectedAction, Uri videoUri) {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        StorageReference videosRef = storageRef.child(helper.renameGesture(selectedAction)).child(helper.renameGesture(selectedAction) + "_PRACTICE_" + practiceNumber);

        UploadTask uploadTask = videosRef.putFile(videoUri);

        uploadTask.addOnProgressListener(snapshot -> {
            previousScreenButton.setEnabled(false);
            recordVideoButton.setEnabled(false);
            spinner.setVisibility(View.VISIBLE);
        });

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                helper.Toastyyy(getApplicationContext(), "Upload Failed");
                errorLogger.setText(e.getCause().toString());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                helper.Toastyyy(getApplicationContext(), "Upload Succesful!");
                errorLogger.setText("Record another video.");
                practiceNumber++;
            }
        });
        spinner.setVisibility(View.GONE);
        recordVideoButton.setEnabled(true);
        previousScreenButton.setEnabled(true);
    }

    private Boolean hasFrontCamera() {
        return getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_FRONT
        );
    }

}