package com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Helper.DatabaseHelper;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Helper.GetTimeAgo;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Models.WorkRequestModel;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.MyWorkRequestActivity;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.ViewOrdersRequestActivity;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.WorkRequestDetailsActivity;
import com.softrasol.ahmedgulsaqib.digitaltasker.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyWorkRequestAdapter extends RecyclerView.Adapter<MyWorkRequestAdapter.ViewHolder> {

    private Context context;
    private List<WorkRequestModel> list;

    public static List<WorkRequestModel> data = new ArrayList<>();

    public MyWorkRequestAdapter(Context context, List<WorkRequestModel> list) {

        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.view_work_requests_items_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final WorkRequestModel model = list.get(position);

        long timeAgo = Long.parseLong(model.getTime_stamp());
        holder.mTxtTime.setText(GetTimeAgo.getTimeAgo(timeAgo, context));
        holder.mTxtTitle.setText(model.getTitle());
        holder.mTxtDescription.setText(model.getDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.clear();
                data.add(list.get(position));
                context.startActivity(new Intent(context, ViewOrdersRequestActivity.class));
            }
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView mTxtDescription, mTxtTitle, mTxtTime;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mTxtDescription = itemView.findViewById(R.id.txt_workrequest_details);
            mTxtTitle = itemView.findViewById(R.id.txt_workrequest_title);
            mTxtTime = itemView.findViewById(R.id.txt_workrequest_time);

        }
    }

}
