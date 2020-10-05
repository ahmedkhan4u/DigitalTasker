package com.softrasol.ahmedgulsaqib.digitaltasker.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Adapters.TrasactionsAdapter;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Helper.DatabaseHelper;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Models.OrderModel;
import com.softrasol.ahmedgulsaqib.digitaltasker.R;

import java.util.ArrayList;
import java.util.List;

public class TransactionsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private TextView mTxtTotalTransactions;
    private List<OrderModel> list = new ArrayList<>();
    private double total = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        toolbarInflation();
        mRecyclerView = findViewById(R.id.recyclerview_transactions);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mTxtTotalTransactions = findViewById(R.id.txt_transaction_price);

        DatabaseHelper.mDatabase.collection("orders").orderBy("time_stamp", Query.Direction.ASCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){


                            if (!task.getResult().isEmpty()){

                                for (QueryDocumentSnapshot snapshot : task.getResult()){
                                    OrderModel model = snapshot.toObject(OrderModel.class);
                                    if (model.getReciever_id().equalsIgnoreCase(DatabaseHelper.Uid)
                                    || model.getSender_id().equalsIgnoreCase(DatabaseHelper.Uid)
                                            && model.getStatus().equalsIgnoreCase("Completed")
                                    ){
                                        list.add(model);

                                        if (model.getReciever_id().equalsIgnoreCase(DatabaseHelper.Uid)){
                                            total = total + Double.parseDouble(model.getBudget());
                                            mTxtTotalTransactions.setText(total+"");
                                        }

                                    }
                                }
                                TrasactionsAdapter adapter = new TrasactionsAdapter(TransactionsActivity.this, list);
                                mRecyclerView.setAdapter(adapter);
                            }

                        }
                    }
                });



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