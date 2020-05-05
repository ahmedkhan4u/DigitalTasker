package com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Models.UserDataModel;
import com.softrasol.ahmedgulsaqib.digitaltasker.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewUsersAdapter extends RecyclerView.Adapter<ViewUsersAdapter.ViewHolder> {

    private Context context;
    private List<UserDataModel> list;

    public ViewUsersAdapter(Context context, List<UserDataModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.view_users_item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        UserDataModel model = list.get(position);

        Picasso.get().load(model.getProfile_img()).resize(80,80)
                .placeholder(R.drawable.image_profile).into(holder.mImgProfile);
        holder.mTxtName.setText(model.getName());
        holder.mTxtPrice.setText(model.getPrice());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private CircleImageView mImgProfile;
        private TextView mTxtName, mTxtPrice, mTxtDistance;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mImgProfile = itemView.findViewById(R.id.img_view_user_profile);
            mTxtName = itemView.findViewById(R.id.txt_view_user_name);
            mTxtPrice = itemView.findViewById(R.id.txt_view_users_price);
            mTxtDistance = itemView.findViewById(R.id.txt_view_users_distance);
        }
    }
}
