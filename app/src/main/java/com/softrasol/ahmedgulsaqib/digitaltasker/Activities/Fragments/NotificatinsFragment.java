package com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.softrasol.ahmedgulsaqib.digitaltasker.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificatinsFragment extends Fragment {


    public NotificatinsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notificatins, container, false);
    }

}
