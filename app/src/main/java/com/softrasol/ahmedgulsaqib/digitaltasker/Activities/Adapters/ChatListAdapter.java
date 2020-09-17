package com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.ChatsActivity;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Models.UserDataModel;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.ViewUserDetailsActivity;
import com.softrasol.ahmedgulsaqib.digitaltasker.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {

    private Context context;
    private List<UserDataModel> list;

    public ChatListAdapter(Context context, List<UserDataModel> list) {

        this.context = context;
        this.list = list;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.chat_users_items_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final UserDataModel model = list.get(position);

        Picasso.get().load(model.getProfile_img()).resize(80,80)
                .placeholder(R.drawable.image_profile).into(holder.mImgProfile);
        holder.mTxtName.setText(model.getName());
        holder.mTxtCategory.setText(model.getCategory());

//        if (model.getStatus().equals("online")){
//            holder.mImgStatus.setVisibility(View.VISIBLE);
//        }else {
//            holder.mImgStatus.setVisibility(View.GONE);
//        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("reciever_uid", model.getUid());
                intent.putExtra("name", model.getName());
                intent.putExtra("image_url", model.getProfile_img());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private CircleImageView mImgProfile;
        private TextView mTxtName, mTxtCategory;
        private ImageView mImgStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mImgProfile = itemView.findViewById(R.id.img_chat_user_profile);
            mTxtName = itemView.findViewById(R.id.txt_chat_person_name);
            mTxtCategory = itemView.findViewById(R.id.txt_chat_user_cat);
            mImgStatus = itemView.findViewById(R.id.img_online_status);

        }
    }

}
