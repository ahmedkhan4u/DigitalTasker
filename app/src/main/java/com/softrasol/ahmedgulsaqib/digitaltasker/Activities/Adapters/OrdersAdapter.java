package com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Helper.DatabaseHelper;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Helper.GetTimeAgo;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Helper.Helper;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Helper.Notification;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Models.NotificationsModel;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Models.OrderModel;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Models.WorkRequestModel;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.PhoneAuthActivity;
import com.softrasol.ahmedgulsaqib.digitaltasker.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {

    private Context context;
    private List<OrderModel> list;

    public static List<WorkRequestModel> data = new ArrayList<>();
    public static String name, image;

    public OrdersAdapter(Context context, List<OrderModel> list) {

        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.my_oders_users_requests_items_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final OrderModel model = list.get(position);

        long timeAgo = Long.parseLong(model.getTime_stamp());
        holder.mTxtTime.setText(GetTimeAgo.getTimeAgo(timeAgo, context));
        holder.mTxtTimeRequired.setText(TimeUnit.MILLISECONDS.toHours(Long.parseLong(model.getTime()))+"");
        holder.mTxtDescription.setText(model.getDescription());
        holder.mTxtBudget.setText(model.getBudget());
        getUserDetailsFromFirestoreDb(model, holder);



        holder.mBtnAcceptOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                acceptOffer(model);

            }
        });

        holder.mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelOrderRequestDialog(model);
            }
        });

    }

    private void acceptOffer(final OrderModel model) {

        Map map = new HashMap<>();
        map.put("is_accepted", "true");
        map.put("time_stamp", System.currentTimeMillis()+"");
        map.put("status", "Pending");

        DatabaseHelper.mDatabase.collection("orders").document(model.getUid())
                .update(map).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()){
                    Helper.shortToast(context, "Order Accepted");

                    DatabaseHelper.mDatabase.collection("work_requests").document(model.getRequest_id())
                            .delete();

                    String uniqueKey = DatabaseHelper.mDatabase.
                            collection("notifications").document().getId();

                    NotificationsModel model1 = new NotificationsModel("Order Accepted",
                            "Your order request has accepted by the buyer", System.currentTimeMillis()+"",
                            DatabaseHelper.Uid, model.getSender_id(), "false", "order", uniqueKey);

                    Notification.sendNotification(context, model1);

                }
            }
        });

    }

    private void getUserDetailsFromFirestoreDb(OrderModel model, final ViewHolder holder) {

        DatabaseHelper.mDatabase.collection("users").document(model.getSender_id())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()){
                    if (task.getResult().exists()){

                        name = task.getResult().get("name").toString();
                        image = task.getResult().get("profile_img").toString();

                        holder.mTxtName.setText(name);
                        Picasso.get().load(image)
                                .resize(60, 60)
                                .placeholder(R.drawable.image_profile).into(holder.mImgProfile);

                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private CircleImageView mImgProfile;
        private TextView mTxtName, mTxtTimeRequired, mTxtTime, mTxtBudget, mTxtDescription;
        private Button mBtnCancel, mBtnAcceptOffer;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mImgProfile = itemView.findViewById(R.id.img_order_profile);
            mTxtName = itemView.findViewById(R.id.txt_order_name);
            mTxtTimeRequired = itemView.findViewById(R.id.txt_order_time_required);
            mTxtTime = itemView.findViewById(R.id.txt_order_timeago);
            mTxtBudget = itemView.findViewById(R.id.txt_order_budget);
            mTxtDescription = itemView.findViewById(R.id.txt_order_description);
            mBtnCancel = itemView.findViewById(R.id.btn_order_cancel);
            mBtnAcceptOffer = itemView.findViewById(R.id.btn_order_accept);
        }
    }

    private void cancelOrderRequestDialog(final OrderModel model) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setTitle("Alert!");
        builder1.setMessage("Are you want to cancel request.");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DatabaseHelper.mDatabase.collection("orders").document(model.getUid())
                                .delete();
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }


}