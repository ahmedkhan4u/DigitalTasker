package com.softrasol.ahmedgulsaqib.digitaltasker.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Adapters.ReviewsAdapter;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Helper.DatabaseHelper;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Helper.Helper;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Models.RatingsModel;
import com.softrasol.ahmedgulsaqib.digitaltasker.R;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class ReviewsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private List<RatingsModel> list = new ArrayList<>();
    String mUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        mUid = getIntent().getStringExtra("uid");

        toolbarInflation();
        recyclerImplementation();


    }

    private void recyclerImplementation() {

        mRecyclerView = findViewById(R.id.recycelrview_ratings);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        DatabaseHelper.mDatabase.collection("reviews").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                list.clear();
                if ( e != null ){
                    Helper.logMessage(e.getMessage());
                    return;
                }

                if (!queryDocumentSnapshots.isEmpty()){

                    for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots){

                        RatingsModel model = snapshot.toObject(RatingsModel.class);
                        if (model.getReciever_uid().equalsIgnoreCase(mUid)){
                            list.add(model);
                        }

                    }

                    ReviewsAdapter adapter = new ReviewsAdapter(ReviewsActivity.this, list);
                    mRecyclerView.setAdapter(adapter);

                    if (list.isEmpty()){
                        Helper.shortToast(getApplicationContext(), "No Reviews Found");
                    }
                }

            }
        });


    }

    private void toolbarInflation() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView textView = toolbar.findViewById(R.id.toolbarText);
        textView.setText("Reviews");
        ImageButton mBtnBack = toolbar.findViewById(R.id.btnBack);
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}