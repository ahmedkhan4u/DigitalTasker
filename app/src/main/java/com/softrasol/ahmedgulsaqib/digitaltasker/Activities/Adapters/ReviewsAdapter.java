package com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Helper.DatabaseHelper;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Helper.GetTimeAgo;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Models.RatingsModel;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Models.WorkRequestModel;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.WorkRequestDetailsActivity;
import com.softrasol.ahmedgulsaqib.digitaltasker.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    private Context context;
    private List<RatingsModel> list;

    public static List<WorkRequestModel> data = new ArrayList<>();
    public static String name, image;

    public ReviewsAdapter(Context context, List<RatingsModel> list) {

        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.ratings_reviews_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final RatingsModel model = list.get(position);

        long timeAgo = Long.parseLong(model.getTime_stamp());
        holder.mTxtTime.setText(GetTimeAgo.getTimeAgo(timeAgo, context));
        holder.mTxtComment.setText(model.getComment());
        holder.mTxtRating.setText(Float.parseFloat(model.getRating())+"");
        holder.mRatingBar.setRating(Float.parseFloat(model.getRating()));

        getUserDetailsFromFirestoreDb(model, holder);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                data.clear();

//                Intent intent = new Intent(context, WorkRequestDetailsActivity.class);
//                data.add(list.get(position));
//                context.startActivity(intent);

            }
        });



    }

    private void getUserDetailsFromFirestoreDb(RatingsModel model, final ViewHolder holder) {

        DatabaseHelper.mDatabase.collection("users").document(model.getSender_uid())
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
        private TextView mTxtName, mTxtComment, mTxtTime, mTxtRating;
        private RatingBar mRatingBar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mImgProfile = itemView.findViewById(R.id.review_profileImage);
            mTxtName = itemView.findViewById(R.id.review_personName);
            mTxtComment = itemView.findViewById(R.id.review_personComment);
            mTxtTime = itemView.findViewById(R.id.review_timeAgo);
            mRatingBar = itemView.findViewById(R.id.review_ratingBar);
            mTxtRating = itemView.findViewById(R.id.review_rating);

        }
    }
}

