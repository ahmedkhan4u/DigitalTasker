package com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Helper;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class Helper {




    public static void shortToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
    public static void longToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void logMessage(String message){
        Log.d("dxdiag", message);
    }

}
