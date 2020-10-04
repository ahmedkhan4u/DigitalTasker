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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Adapters.MyWorkRequestAdapter;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Adapters.OrdersAdapter;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Helper.DatabaseHelper;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Helper.Helper;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Helper.ProgressDialogClass;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Models.OrderModel;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Models.WorkRequestModel;
import com.softrasol.ahmedgulsaqib.digitaltasker.R;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class ViewOrdersRequestActivity extends AppCompatActivity {

    private List<OrderModel> list = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private WorkRequestModel data = MyWorkRequestAdapter.data.get(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_orders_request);

        toolbarInflation();
        recyclerViewImplementation();


    }

    private void recyclerViewImplementation() {

        mRecyclerView = findViewById(R.id.recyclerview_order_requests);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(ViewOrdersRequestActivity.this));

        ProgressDialogClass.showProgressBar(ViewOrdersRequestActivity.this);

        DatabaseHelper.mDatabase.collection("orders")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        list.clear();

                        if (e != null){
                            Helper.logMessage(e.getMessage());
                            return;
                        }

                        if (!queryDocumentSnapshots.isEmpty()){
                            for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots){

                                OrderModel model = snapshot.toObject(OrderModel.class);
                                if (data.getSender_uid().equalsIgnoreCase(DatabaseHelper.Uid) && data.getUid().equals(model.getRequest_id())
                                        && model.getIs_accepted().equals("false") ){
                                    list.add(model);
                                }

                                ProgressDialogClass.cancelDialog();
                            }

                            OrdersAdapter adapter = new OrdersAdapter(ViewOrdersRequestActivity.this, list);
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
        textView.setText("Work Details");
        ImageButton mBtnBack = toolbar.findViewById(R.id.btnBack);
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

}