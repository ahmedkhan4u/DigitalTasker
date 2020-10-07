package com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Adapters.MyOrdersAdapter;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Helper.DatabaseHelper;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Helper.Helper;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Models.OrderModel;
import com.softrasol.ahmedgulsaqib.digitaltasker.R;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class CompletedOrdersFragment extends Fragment {

    public CompletedOrdersFragment() {
        // Required empty public constructor
    }

    private View mView;
    private List<OrderModel> list = new ArrayList<>();
    private RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_completed_orders, container, false);

        mRecyclerView = mView.findViewById(R.id.recycelrview_orders);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        getCompletedOrdersFromFirestore();

        return mView;
    }

    private void getCompletedOrdersFromFirestore() {

        DatabaseHelper.mDatabase.collection("orders").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                list.clear();
                if ( e!= null ){
                    Helper.logMessage(e.getMessage());
                    return;
                }

                if (!queryDocumentSnapshots.isEmpty()){
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots){

                        OrderModel model = snapshot.toObject(OrderModel.class);
                        if (model.getReciever_id().equalsIgnoreCase(DatabaseHelper.Uid)
                                && model.getIs_accepted().equalsIgnoreCase("true")
                                && model.getStatus().equalsIgnoreCase("Completed")
                        )
                        {
                            list.add(model);
                        }

                        if (model.getSender_id().equalsIgnoreCase(DatabaseHelper.Uid)
                                && model.getIs_accepted().equalsIgnoreCase("true")
                                && model.getStatus().equalsIgnoreCase("Completed")
                        ){
                            list.add(model);
                        }

                    }

                    MyOrdersAdapter adapter = new MyOrdersAdapter(getActivity(), list);
                    mRecyclerView.setAdapter(adapter);
                }
            }
        });
    }
}