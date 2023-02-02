package com.example.sourcecode_timothymendez;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface FileUploadService {

    @GET("")
    Call<ResponseBody> contactServer();

    @Multipart
    @POST("/upload")
    Call<ResponseBody> uploadFile(
            @Part MultipartBody.Part file
    );
}
