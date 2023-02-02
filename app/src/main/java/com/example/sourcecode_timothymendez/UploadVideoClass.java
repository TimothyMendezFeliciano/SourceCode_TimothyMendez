package com.example.sourcecode_timothymendez;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.provider.MediaStore;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UploadVideoClass extends Service {

    private FileUploadService fileUploadService;

    private void setupRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
//                TODO: REPLACE WITH URL FROM SERVER
                .baseUrl("http://192.168.0.20:5000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        fileUploadService = retrofit.create(FileUploadService.class);
    }

    public UploadVideoClass() {
        setupRetrofit();
    }

    public void ContactServer() {
        Call<ResponseBody> serverCall = fileUploadService.contactServer();
        serverCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void UploadVideo(Uri videoUri) {
        File videoFile = new File(getFilePath(videoUri));
        RequestBody requestFile = RequestBody.create(MediaType.parse("video/mp4"), videoFile);
        MultipartBody.Part body = MultipartBody.Part.createFormData("video", videoFile.getName(), requestFile);

        Call<ResponseBody> serverCall = fileUploadService.uploadFile(body);
        serverCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private String getFilePath(Uri videoUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = getContentResolver().query(videoUri, proj, null, null, null);
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(columnIndex);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}