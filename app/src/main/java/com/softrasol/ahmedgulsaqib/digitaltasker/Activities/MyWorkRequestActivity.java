package com.softrasol.ahmedgulsaqib.digitaltasker.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Adapters.MyWorkRequestAdapter;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Helper.DatabaseHelper;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Helper.Helper;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Helper.ProgressDialogClass;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Models.WorkRequestModel;
import com.softrasol.ahmedgulsaqib.digitaltasker.R;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class MyWorkRequestActivity extends AppCompatActivity {

    private List<WorkRequestModel> list = new ArrayList<>();
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_work_request);

        toolbarInflation();
        recyclerViewImplementation();

    }

    private void recyclerViewImplementation() {

        mRecyclerView = findViewById(R.id.recyclerview_mywork_requests);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(MyWorkRequestActivity.this));

        ProgressDialogClass.showProgressBar(MyWorkRequestActivity.this);

        DatabaseHelper.mDatabase.collection("work_requests")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                        list.clear();

                        if ( e != null){
                            Helper.logMessage(e.getMessage());
                            ProgressDialogClass.cancelDialog();
                            return;
                        }

                        if (!queryDocumentSnapshots.isEmpty()){
                            for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots){

                                WorkRequestModel model = snapshot.toObject(WorkRequestModel.class);
                                if (model.getSender_uid().equalsIgnoreCase(DatabaseHelper.Uid)){
                                    list.add(model);
                                }
                                ProgressDialogClass.cancelDialog();
                            }

                            MyWorkRequestAdapter adapter = new MyWorkRequestAdapter(getApplicationContext(), list);
                            mRecyclerView.setAdapter(adapter);

                            if (list.isEmpty()){
                                ProgressDialogClass.cancelDialog();
                            }
                        }
                    }
                });

    }

    private void toolbarInflation() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView textView = toolbar.findViewById(R.id.toolbarText);
        textView.setText("My Work Requests");
        ImageButton mBtnBack = toolbar.findViewById(R.id.btnBack);
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

}

