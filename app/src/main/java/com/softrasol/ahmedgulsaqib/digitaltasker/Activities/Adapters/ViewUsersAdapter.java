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
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Models.UserDataModel;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.ViewUserDetailsActivity;
import com.softrasol.ahmedgulsaqib.digitaltasker.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewUsersAdapter extends RecyclerView.Adapter<ViewUsersAdapter.ViewHolder> {

    private Context context;
    private List<UserDataModel> list;
    private double currentLat, currentLng;

    public ViewUsersAdapter(Context context, List<UserDataModel> list, double currentLat,
                            double currentLng) {

        this.context = context;
        this.list = list;
        this.currentLat = currentLat;
        this.currentLng = currentLng;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.view_users_item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final UserDataModel model = list.get(position);

        Picasso.get().load(model.getProfile_img()).resize(80,80)
                .placeholder(R.drawable.image_profile).into(holder.mImgProfile);
        holder.mTxtName.setText(model.getName());
        holder.mTxtPrice.setText("Per Day Price : "+model.getPrice()+" Rs");

//        if (model.getStatus().equals("online")){
//            holder.mImgStatus.setVisibility(View.VISIBLE);
//        }else {
//            holder.mImgStatus.setVisibility(View.GONE);
//        }


        LatLng latLng1 = new LatLng(Double.parseDouble(model.getLat()),
                Double.parseDouble(model.getLng()));

        LatLng latLng2 = new LatLng(currentLat,currentLng);

        final double distance = CalculationByDistance(latLng1, latLng2);
        int distanceInKm = (int) distance;

        holder.mTxtDistance.setText("Distance : "+(distanceInKm)+" Km away");


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ViewUserDetailsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("uid", model.getUid());
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
        private TextView mTxtName, mTxtPrice, mTxtDistance;
        private ImageView mImgStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mImgProfile = itemView.findViewById(R.id.img_view_user_profile);
            mTxtName = itemView.findViewById(R.id.txt_view_user_name);
            mTxtPrice = itemView.findViewById(R.id.txt_view_users_price);
            mTxtDistance = itemView.findViewById(R.id.txt_view_users_distance);
            mImgStatus =  itemView.findViewById(R.id.img_online_status);
        }
    }

    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return Radius * c;
    }
}
