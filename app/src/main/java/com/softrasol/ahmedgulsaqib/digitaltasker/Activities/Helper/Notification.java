package com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Helper;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Models.NotificationsModel;

public class Notification {

    public static void sendNotification(final Context context, NotificationsModel model){
        DatabaseHelper.mDatabase.collection("notification").document(model.getUid())
        .set(model).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Helper.shortToast(context, "Notification Sent");
                }else {
                    Helper.logMessage(task.getException().getMessage());
                }
            }
        });
    }
}
