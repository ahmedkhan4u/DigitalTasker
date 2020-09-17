package com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Helper;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Models.NotificationsModel;

public class Notification {

    public static boolean status;

    public static void sendNotification(final Context context, NotificationsModel model){
        DatabaseHelper.mDatabase.collection("notifications").document(model.getUid())
        .set(model).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Helper.shortToast(context, "Notification Sent");
                }else {
                    Helper.shortToast(context, "Error Occured");
                    Helper.logMessage(task.getException().getMessage());
                }
            }
        });
    }

    public static void postComplaint(final Context context, NotificationsModel model) {


        DatabaseHelper.mDatabase.collection("complaints").document(model.getUid())
                .set(model).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Helper.shortToast(context, "Complaint Posted");
                    status = true;
                } else {
                    Helper.shortToast(context, "Error Occured");
                    Helper.logMessage(task.getException().getMessage());
                    status = false;
                }
            }
        });

    }

}
