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


    public static String getMonth(int month){

        switch (month){

            case 1:
                return "Jan";

            case 2:
                return "Feb";

            case 3:
                return "Mar";

            case 4:
                return "Apr";

            case 5:
                return "May";

            case 6:
                return "Jun";

            case 7:
                return "Jul";

            case 8:
                return "Aug";

            case 9:
                return "Sep";

            case 10:
                return "Oct";

            case 11:
                return "Nov";

            case 12:
                return "Dec";

            default:
                return "";
        }
    }

}
