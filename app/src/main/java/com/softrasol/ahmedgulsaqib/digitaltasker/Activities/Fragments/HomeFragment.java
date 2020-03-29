package com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Adapters.HomeCategoryAdapter;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Models.HomeCategoryModel;
import com.softrasol.ahmedgulsaqib.digitaltasker.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }

    private RecyclerView mRecyclerView;
    private List<HomeCategoryModel> list;

    //...................................................................................................
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mRecyclerView = view.findViewById(R.id.recycler_view_home_categories);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        list = new ArrayList<>();

        //Filling the list to send to adapter
        homeCategoryItemsList();

        HomeCategoryAdapter adapter = new HomeCategoryAdapter(getContext(), list);
        mRecyclerView.setAdapter(adapter);


        return view;
    }
//...................................................................................................

    private void homeCategoryItemsList() {
        list.add(new HomeCategoryModel("Electrician", R.drawable.ic_electrician));
        list.add(new HomeCategoryModel("Cleaner", R.drawable.ic_cleaner));
        list.add(new HomeCategoryModel("Plumber", R.drawable.ic_plumber));
        list.add(new HomeCategoryModel("Delivery", R.drawable.ic_delivery));
        list.add(new HomeCategoryModel("Handyman", R.drawable.ic_handyman));
        list.add(new HomeCategoryModel("Carpenter", R.drawable.ic_carpenter));
        list.add(new HomeCategoryModel("Labour", R.drawable.ic_labour));
        list.add(new HomeCategoryModel("Ac Repair", R.drawable.ic_repair));
    }
}
