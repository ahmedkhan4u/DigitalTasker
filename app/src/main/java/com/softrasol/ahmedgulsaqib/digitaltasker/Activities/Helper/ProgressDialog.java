package com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Helper;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.widget.ProgressBar;

import com.softrasol.ahmedgulsaqib.digitaltasker.R;

public class ProgressDialog {

    public static Dialog dialog;

    public static void showProgressBar(Context context){
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.progress_bar);
        ProgressBar progressBar = dialog.findViewById(R.id.progress_bar);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressBar.setProgress(100);
        //dialog.setCancelable(false);
        dialog.show();
    }

    public static void cancelDialog(){
        dialog.cancel();
    }

}
