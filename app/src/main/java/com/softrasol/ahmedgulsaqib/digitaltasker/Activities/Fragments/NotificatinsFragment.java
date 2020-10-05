package com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Adapters.NotificationsAdapter;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Helper.DatabaseHelper;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Helper.Helper;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Helper.Notification;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Models.NotificationsModel;
import com.softrasol.ahmedgulsaqib.digitaltasker.R;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificatinsFragment extends Fragment {


    public NotificatinsFragment() {
        // Required empty public constructor
    }

    private View mView;
    private RecyclerView mRecyclerView;
    private List<NotificationsModel> list = new ArrayList<>();
    private TextView mTxtNotification;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_notificatins, container, false);

        mRecyclerView = mView.findViewById(R.id.recyclerview_notifications);
        mTxtNotification = mView.findViewById(R.id.txt_notifications);

        getNotificationFromFirestore();

        return mView;
    }

    private void getNotificationFromFirestore() {

        DatabaseHelper.mDatabase.collection("notifications").
                addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                list.clear();

                if ( e != null){
                    Helper.logMessage("Notificaiton Exception" + e.getMessage());
                    return;
                }

                if (!queryDocumentSnapshots.isEmpty()){
                    for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots){

                        NotificationsModel model = snapshot.toObject(NotificationsModel.class);
                        if (model.getReciever_uid().equalsIgnoreCase(DatabaseHelper.Uid)){
                            list.add(model);
                        }

                    }

                    if (!list.isEmpty()){
                        mTxtNotification.setVisibility(View.GONE);
                    }

                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    NotificationsAdapter adapter = new NotificationsAdapter(getActivity(), list);
                    mRecyclerView.setAdapter(adapter);
                }
            }
        });
    }
}
