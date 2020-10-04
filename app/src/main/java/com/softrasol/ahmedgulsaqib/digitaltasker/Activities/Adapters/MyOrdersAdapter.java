package com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Helper.GetTimeAgo;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Models.OrderModel;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Models.WorkRequestModel;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.OrderDetailsActivity;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.ViewOrdersRequestActivity;
import com.softrasol.ahmedgulsaqib.digitaltasker.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.ViewHolder> {

    private Context context;
    private List<OrderModel> list;

    public static List<OrderModel> data = new ArrayList<>();

    public MyOrdersAdapter(Context context, List<OrderModel> list) {

        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.order_items_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final OrderModel model = list.get(position);

        long timeAgo = Long.parseLong(model.getTime_stamp());
        holder.mTxtTimeRemaining.setText(GetTimeAgo.getTimeAgo(timeAgo, context));
        holder.mTxtDescription.setText(model.getDescription());
        holder.mTxtBudget.setText(model.getBudget());

        long startTime = Long.parseLong(model.getTime_stamp());
        long endTime = System.currentTimeMillis();
        long time = Long.parseLong(model.getTime());

        long result = (startTime - endTime) - time;

//        if (result<0){
//            holder.mTxtTimeRemaining.setText("Time Passed");
//        }else {
//            holder.mTxtTimeRemaining.setText(TimeUnit.MILLISECONDS.toHours(result)+"");
//        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.clear();
                data.add(list.get(position));
                context.startActivity(new Intent(context, OrderDetailsActivity.class));
            }
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView mTxtDescription, mTxtBudget, mTxtTimeRemaining;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mTxtDescription = itemView.findViewById(R.id.txt_order_description);
            mTxtBudget = itemView.findViewById(R.id.txt_order_budget);
            mTxtTimeRemaining = itemView.findViewById(R.id.txt_order_time_ramaining);

        }
    }

}

