package com.example.sourcecode_timothymendez;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.widget.Toast;

public class Helper {

    public Helper() {

    }

    public void Toastyyy(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }


    public String renameGesture(String selectedAction) {
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

    @SuppressLint("Range")
    public String getFileName(ContentResolver contentResolver, Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = contentResolver.query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

}
