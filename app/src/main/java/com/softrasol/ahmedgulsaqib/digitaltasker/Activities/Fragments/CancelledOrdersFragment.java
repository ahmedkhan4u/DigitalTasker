package com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.softrasol.ahmedgulsaqib.digitaltasker.R;

public class CancelledOrdersFragment extends Fragment {

    public CancelledOrdersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cancelled_orders, container, false);
    }
}