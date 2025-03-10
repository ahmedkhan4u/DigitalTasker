package com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.AboutUsActivity;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.MyWorkRequestActivity;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.PhoneAuthActivity;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.PrivacyPolicyActivity;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.ProfileActivity;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.SearchUserActivity;
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
    private TextView mTxtWorkRequests, mTxtViewOrders, mTxtProfile, mTxtSearch;
    private TextView mTxtViewRequests, mTxtTransactions, mTxtPrivacyPolicy, mTxtAboutUs, mTxtSupport;

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
        privacyPolicyClick();
        aboutUsClick();
        profileClick();
        supportClick();
        searchClick();

        return mView;
    }

    private void searchClick() {

        mTxtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchUserActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }

    private void supportClick() {

        mTxtSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
                intent.putExtra(Intent.EXTRA_SUBJECT, "Help And Support");
                intent.putExtra(Intent.EXTRA_TEXT, "Type Description Here");
                intent.setData(Uri.parse("mailto:ahmedkhan871871@gmail.com")); // or just "mailto:" for blank
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
                startActivity(intent);
            }
        });

    }

    private void profileClick() {
        mTxtProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ProfileActivity.class));
            }
        });
    }

    private void aboutUsClick() {

        mTxtAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AboutUsActivity.class));
            }
        });

    }

    private void privacyPolicyClick() {
        mTxtPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PrivacyPolicyActivity.class));
            }
        });
    }

    private void transactionsClick() {
        mTxtTransactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), TransactionsActivity.class));
            }
        });
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
        mTxtTransactions = mView.findViewById(R.id.txt_transaction_click);
        mTxtAboutUs = mView.findViewById(R.id.txt_about_us);
        mTxtPrivacyPolicy = mView.findViewById(R.id.txt_privacy_policy);
        mTxtProfile = mView.findViewById(R.id.txt_profile);
        mTxtSupport = mView.findViewById(R.id.txt_Support);
        mTxtSearch = mView.findViewById(R.id.txt_search);
    }

}
