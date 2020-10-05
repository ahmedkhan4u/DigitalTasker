package com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Adapters;

import android.content.Context;
import android.content.DialogInterface;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Helper.DatabaseHelper;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Helper.GetTimeAgo;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Helper.Helper;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Helper.Notification;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Models.NotificationsModel;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Models.OrderModel;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Models.WorkRequestModel;
import com.softrasol.ahmedgulsaqib.digitaltasker.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class TrasactionsAdapter extends RecyclerView.Adapter<TrasactionsAdapter.ViewHolder> {

    private Context context;
    private List<OrderModel> list;

    public static List<WorkRequestModel> data = new ArrayList<>();
    public static String name, image;

    public TrasactionsAdapter(Context context, List<OrderModel> list) {

        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.transaction_items_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final OrderModel model = list.get(position);

        long timeAgo = Long.parseLong(model.getTime_stamp());
        holder.mTxtTime.setText(GetTimeAgo.getTimeAgo(timeAgo, context));

        if (model.getReciever_id().equalsIgnoreCase(DatabaseHelper.Uid)){
            holder.mTxtPrice.setText("PKR "+model.getBudget());
        }else {
            holder.mTxtPrice.setText("PKR -"+model.getBudget());
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView mTxtPrice, mTxtTime;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mTxtPrice = itemView.findViewById(R.id.txt_transactions_price);
            mTxtTime = itemView.findViewById(R.id.txt_transactions_time);



        }
    }
}