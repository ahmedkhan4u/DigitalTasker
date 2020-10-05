package com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Fragments;


import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Adapters.HomeCategoryAdapter;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Helper.DatabaseHelper;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Helper.Helper;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Models.HomeCategoryModel;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Models.RatingsModel;
import com.softrasol.ahmedgulsaqib.digitaltasker.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

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
        checkForUnReviewed();

        return view;
    }

    private void checkForUnReviewed() {

        DatabaseHelper.mDatabase.collection("reviews").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if ( e!= null){
                    Helper.logMessage(e.getMessage());
                    return;
                }

                if (!queryDocumentSnapshots.isEmpty()){

                    for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots){

                        RatingsModel model = snapshot.toObject(RatingsModel.class);
                        if (model.getReciever_uid().equalsIgnoreCase(DatabaseHelper.Uid)
                        && model.getIs_reviewed().equalsIgnoreCase("false")){
                            showRatingDialog(model);
                        }

                    }

                }

            }
        });

    }

    private void showRatingDialog(final RatingsModel model) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.ratings_reviews_dialog);

        final RatingBar ratingBar = dialog.findViewById(R.id.rating_bar);
        final EditText mTxtComment = dialog.findViewById(R.id.txt_review);
        Button mBtnSubmit = dialog.findViewById(R.id.btn_submit_review);


        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(mTxtComment.getText().toString().trim())){
                    mTxtComment.setError("Required");
                    mTxtComment.requestFocus();
                    return;
                }

                Map map = new HashMap();
                map.put("comment", mTxtComment.getText().toString().trim());
                map.put("time_stamp", System.currentTimeMillis()+"");
                map.put("rating", ratingBar.getRating()+"");
                map.put("is_reviewed", "true");

                DatabaseHelper.mDatabase.collection("reviews").document(model.getUid())
                        .update(map).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()){
                            Helper.shortToast(getActivity(), "Thanks for your review");
                            dialog.cancel();
                        }else {
                            Helper.shortToast(getActivity(), task.getException().getMessage());
                            dialog.cancel();
                        }
                    }
                });

            }
        });

        dialog.show();
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
