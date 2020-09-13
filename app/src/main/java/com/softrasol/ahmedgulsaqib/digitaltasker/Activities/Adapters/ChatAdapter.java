package com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Helper.DatabaseHelper;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Helper.GetTimeAgo;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Helper.Helper;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Models.ChatModel;
import com.softrasol.ahmedgulsaqib.digitaltasker.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private Context context;
    private List<ChatModel> list;
    private String imageUrl;

    static final int MSG_TYPE_LEFT = 0;
    static final int MSG_TYPE_RIGHT = 1;

    public ChatAdapter(Context context, List<ChatModel> list, String imageUrl) {
        this.context = context;
        this.list = list;
        this.imageUrl = imageUrl;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == MSG_TYPE_LEFT){
            View view = LayoutInflater.from(context)
                    .inflate(R.layout.chat_reciever_layout, parent, false);
            return new ViewHolder(view);
        }else{
            View view = LayoutInflater.from(context)
                    .inflate(R.layout.chat_sender_layout, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatModel model = list.get(position);
        holder.mTxtMessage.setText(model.getMessage());

        long dateTimeAgo = Long.parseLong(model.getDate_time());
        holder.mTxtTime.setText(GetTimeAgo.getTimeAgo(dateTimeAgo, context));

        if (!model.getSender_id().equals(DatabaseHelper.Uid)){
            Picasso.get().load(imageUrl)
                    .placeholder(R.drawable.image_profile)
                    .resize(50, 50)
                    .into(holder.mImgReceiverProfile);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView mTxtMessage;
        private TextView mTxtTime;
        private ImageButton mImgIsSeen;
        private CircleImageView mImgReceiverProfile;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
                mTxtMessage = itemView.findViewById(R.id.show_msg);
                mTxtTime = itemView.findViewById(R.id.time);
                mImgIsSeen = itemView.findViewById(R.id.is_seen);
                mImgReceiverProfile = itemView.findViewById(R.id.img_chat_receiver_profile);
        }
    }

    @Override
    public int getItemViewType(int position) {

        if (list.get(position).getSender_id().equals(DatabaseHelper.Uid)){
            return MSG_TYPE_RIGHT;
        }else{
            return MSG_TYPE_LEFT;
        }
    }
}
