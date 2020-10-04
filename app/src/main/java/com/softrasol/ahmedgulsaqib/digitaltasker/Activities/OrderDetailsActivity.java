package com.softrasol.ahmedgulsaqib.digitaltasker.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Adapters.MyOrdersAdapter;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Helper.DatabaseHelper;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Helper.Helper;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Models.OrderModel;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Models.UserDataModel;
import com.softrasol.ahmedgulsaqib.digitaltasker.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

public class OrderDetailsActivity extends AppCompatActivity {

    private CircleImageView mImgProfile;
    private TextView mTxtName, mTxtBudget, mTxtAddress, mTxtDescription, mTxtTimeRequired;

    private OrderModel list = MyOrdersAdapter.data.get(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        toolbarInflation();
        widgetsInflation();

        getDetailsFromFirestore();

    }

    private void getDetailsFromFirestore() {

        DatabaseHelper.mDatabase.collection("orders")
                .document(list.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                if (e!=null){
                    Helper.logMessage(e.getMessage());
                    return;
                }

                if (documentSnapshot.exists()){

                    final OrderModel model = documentSnapshot.toObject(OrderModel.class);

                    mTxtBudget.setText("Budget: "+model.getBudget());
                    mTxtDescription.setText("Description: "+model.getDescription());

                    CountDownTimer timer = new CountDownTimer(Long.parseLong(model.getTime_stamp()), 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            long seconds = millisUntilFinished / 1000;
                            long minutes = seconds / 60;
                            long hours = minutes / 60;
                            long days = hours / 24;
                            String time = days + ":" + hours % 24 + ":" + minutes % 60 + ":" + seconds % 60;
                            mTxtTimeRequired.setText(time);
                        }

                        @Override
                        public void onFinish() {

                        }
                    };
                    timer.start();


                    if (list.getReciever_id().equalsIgnoreCase(DatabaseHelper.Uid)){
                        getUserDetailsFromFirestore(list.getSender_id());
                    }else {
                        getUserDetailsFromFirestore(list.getReciever_id());
                    }

                }

            }
        });

    }

    private void getUserDetailsFromFirestore(String id) {




        DatabaseHelper.mDatabase.collection("users").document(id).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                if (e != null){
                    Helper.logMessage(e.getMessage());
                    return;
                }

                if (documentSnapshot.exists()){

                    UserDataModel model = documentSnapshot.toObject(UserDataModel.class);

                    Picasso.get().load(model.getProfile_img())
                            .resize(60, 60)
                            .placeholder(R.drawable.image_profile).into(mImgProfile);

                    mTxtName.setText("Name: "+model.getName());
                    mTxtAddress.setText("Address: "+model.getAddress());
                }

            }
        });

    }

    private void widgetsInflation() {

        mImgProfile = findViewById(R.id.img_orderdetail_profile);
        mTxtName = findViewById(R.id.txt_orderdetail_name);
        mTxtBudget = findViewById(R.id.txt_orderdetails_budget);
        mTxtAddress = findViewById(R.id.txt_orderdetail_address);
        mTxtDescription = findViewById(R.id.txt_orderdetails_description);
        mTxtTimeRequired = findViewById(R.id.txt_orderdetail_time_required);

    }

    private void toolbarInflation() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView textView = toolbar.findViewById(R.id.toolbarText);
        textView.setText("Work Requests");
        ImageButton mBtnBack = toolbar.findViewById(R.id.btnBack);
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}