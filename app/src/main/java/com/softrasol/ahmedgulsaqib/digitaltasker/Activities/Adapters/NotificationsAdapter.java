package com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Helper.DatabaseHelper;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Helper.GetTimeAgo;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Helper.Helper;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.HomeActivity;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Models.NotificationsModel;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Models.UserDataModel;
import com.softrasol.ahmedgulsaqib.digitaltasker.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {

    private List<NotificationsModel> list;
    private Context context;
    private List<UserDataModel> userList = new ArrayList<>();

    public NotificationsAdapter(Context context, List<NotificationsModel> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.notifications_items_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final NotificationsModel model = list.get(position);

        getProfileImage(holder, model);

        holder.mTxtTitle.setText(model.getTitle());
        holder.mTxtBody.setText(model.getBody());
        try {
            long time_stamp = Long.parseLong(model.getTime_stamp());
            holder.mTxtDateTime.setText(GetTimeAgo.getTimeAgo(time_stamp, context));
        }catch (Exception e){}


    }

    private void getProfileImage(final ViewHolder holder, NotificationsModel model) {

       if (!model.getSender_uid().equalsIgnoreCase("admin")) {
           DatabaseHelper.mDatabase.collection("users")
                   .document(model.getSender_uid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
               @Override
               public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                   UserDataModel model = task.getResult().toObject(UserDataModel.class);

                   Picasso.get().load(model.getProfile_img()).placeholder(R.drawable.image_profile)
                           .resize(50, 50).into(holder.mImgProfile);

               }
           });

       }
    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private CircleImageView mImgProfile;
        private TextView mTxtTitle, mTxtBody, mTxtDateTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mImgProfile = itemView.findViewById(R.id.img_notif_profile);
            mTxtTitle = itemView.findViewById(R.id.txt_notif_title);
            mTxtBody = itemView.findViewById(R.id.txt_notif_body);
            mTxtDateTime = itemView.findViewById(R.id.txt_notif_time);

        }
    }
}
