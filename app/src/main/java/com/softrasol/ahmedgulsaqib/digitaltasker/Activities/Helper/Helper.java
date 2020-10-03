package com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Helper;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Helper {

    public static List<String> time_list = new ArrayList<>();

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

    public static List<String> getTimeList() {

        for (int i=1; i<=24; i++){

            time_list.add(i+"hours");

        }

        return time_list;
    }

}
