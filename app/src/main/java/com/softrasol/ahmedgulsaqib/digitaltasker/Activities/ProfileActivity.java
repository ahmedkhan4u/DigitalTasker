package com.softrasol.ahmedgulsaqib.digitaltasker.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Helper.DatabaseHelper;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Models.UserDataModel;
import com.softrasol.ahmedgulsaqib.digitaltasker.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private CircleImageView mImgProfile;
    private TextView mTxtName, mTxtEmail, mTxtPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        widgetsInflation();
        toolbarInflation();
        getUserDataFromFirestore();


    }

    private void getUserDataFromFirestore() {

        DatabaseHelper.mDatabase.collection("users").document(DatabaseHelper.Uid)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){

                    if (task.getResult().exists()){

                        UserDataModel model = task.getResult().toObject(UserDataModel.class);
                        mTxtName.setText(model.getName());
                        mTxtEmail.setText(model.getEmail());
                        mTxtPhone.setText(model.getPhone());
                        Picasso.get().load(model.getProfile_img()).placeholder(R.drawable.image_profile)
                                .resize(80, 80).into(mImgProfile);
                    }

                }
            }
        });

    }

    private void widgetsInflation() {

        mImgProfile = findViewById(R.id.img_profile);
        mTxtName = findViewById(R.id.txt_profile_name);
        mTxtEmail = findViewById(R.id.txt_profile_email);
        mTxtPhone = findViewById(R.id.txt_profile_phone);

    }


    private void toolbarInflation() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView textView = toolbar.findViewById(R.id.toolbarText);
        textView.setText("Profile");
        ImageButton mBtnBack = toolbar.findViewById(R.id.btnBack);
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

}