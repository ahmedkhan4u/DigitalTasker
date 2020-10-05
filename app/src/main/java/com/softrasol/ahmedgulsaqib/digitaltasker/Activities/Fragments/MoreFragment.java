package com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.MyWorkRequestActivity;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.PhoneAuthActivity;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.TransactionsActivity;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.ViewOrdersActivity;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.WorkRequestsActivity;
import com.softrasol.ahmedgulsaqib.digitaltasker.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoreFragment extends Fragment {


    public MoreFragment() {
        // Required empty public constructor
    }

    private View mView;
    private TextView mLogout;
    private TextView mTxtWorkRequests, mTxtViewOrders;
    private TextView mTxtViewRequests, mTxtTransactions;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_more, container, false);

        widgetsInflation();
        logoutClick();
        workRequestsClick();
        viewOrdersClick();
        viewRequestsClick();
        transactionsClick();

        return mView;
    }

    private void transactionsClick() {
        startActivity(new Intent(getActivity(), TransactionsActivity.class));
    }

    private void viewRequestsClick() {

        mTxtViewRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MyWorkRequestActivity.class));
            }
        });
    }

    private void viewOrdersClick() {
        mTxtViewOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ViewOrdersActivity.class));
            }
        });
    }

    private void workRequestsClick() {
        mTxtWorkRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), WorkRequestsActivity.class));
            }
        });
    }

    private void logoutClick() {
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutDialog();
            }
        });
    }

    private void logoutDialog() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setTitle("Alert!");
        builder1.setMessage("Are you sure you want to exit.");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(getActivity(), PhoneAuthActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private void widgetsInflation() {
        mLogout = mView.findViewById(R.id.logout);
        mTxtWorkRequests = mView.findViewById(R.id.txt_work_request);
        mTxtViewOrders = mView.findViewById(R.id.txt_view_orders);
        mTxtViewRequests = mView.findViewById(R.id.txt_view_requests);

    }

}
