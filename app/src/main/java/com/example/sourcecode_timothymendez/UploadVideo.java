package com.example.sourcecode_timothymendez;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class UploadVideo extends Service {
    public UploadVideo() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}