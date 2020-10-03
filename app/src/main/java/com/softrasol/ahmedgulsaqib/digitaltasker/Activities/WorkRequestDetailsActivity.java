package com.softrasol.ahmedgulsaqib.digitaltasker.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Adapters.WorkRequestAdapter;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Helper.DatabaseHelper;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Helper.Helper;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Models.OrderModel;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Models.WorkRequestModel;
import com.softrasol.ahmedgulsaqib.digitaltasker.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class WorkRequestDetailsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ImageView mImgProfile;
    private TextView mTxtName, mTxtTitle, mTxtDetails, mTxtTimeRequired, mTxtAddress, mTxtBudget;

    private Button mBtnSendOffer;
    private WorkRequestModel list = WorkRequestAdapter.data.get(0);
    private String name, image;
    private String time = "";
    String timeInMillis = "";

    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_request_details);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        progressDialog = new ProgressDialog(WorkRequestDetailsActivity.this);

        toolbarInflation();
        widgetsInflation();
        addDataToFields();
        getNameImageFromFirestore();
        sendOfferClick();

    }

    private void sendOfferClick() {
        mBtnSendOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final BottomSheetDialog dialog = new BottomSheetDialog(WorkRequestDetailsActivity.this);
                dialog.setContentView(R.layout.send_offer_bottom_sheet);

                Spinner mSpinner = dialog.findViewById(R.id.spinner);
                final TextInputEditText mTxtBudget = dialog.findViewById(R.id.txt_sendoffer_budget);
                final TextInputEditText mTxtDescription = dialog.findViewById(R.id.txt_sendoffer_desc);
                Button mBtnSendOffer = dialog.findViewById(R.id.btn_send_offer);
                Button mBtnCancel = dialog.findViewById(R.id.btn_cancel_offer);


                ArrayAdapter adapter = new ArrayAdapter(getBaseContext(),
                        android.R.layout.simple_spinner_dropdown_item, Helper.getTimeList());

                mSpinner.setAdapter(adapter);

                mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        time = Helper.getTimeList().get(position).substring(0,1);
                        timeInMillis = TimeUnit.HOURS.toMillis(Long.parseLong(time))+"";
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                mBtnSendOffer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        validateFields(dialog, mTxtBudget, mTxtDescription);
                    }
                });

                mBtnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                dialog.show();
            }
        });
    }

    private void validateFields(BottomSheetDialog dialog, TextInputEditText mTxtBudget, TextInputEditText mTxtDescription) {

        String budget = mTxtBudget.getText().toString().trim();
        String description = mTxtDescription.getText().toString().trim();

        if (budget.isEmpty()){
            mTxtBudget.setError("Required");
            mTxtBudget.requestFocus();
            return;
        }

        if (description.isEmpty()){
            mTxtDescription.setError("Required");
            mTxtDescription.requestFocus();
            return;
        }

        saveDataToFirestoreDatabase(dialog, budget, description);

    }

    private void saveDataToFirestoreDatabase(final BottomSheetDialog dialog, String budget, String description) {

        progressDialog.setTitle("Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String uniqueId = DatabaseHelper.mDatabase.collection("orders").document().getId();


        OrderModel model = new OrderModel(timeInMillis, budget, description, DatabaseHelper.Uid,
                list.getSender_uid(), list.getUid(), uniqueId, System.currentTimeMillis()+"",
                "false", "status");

        DatabaseHelper.mDatabase.collection("orders").document(uniqueId)
                .set(model).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Helper.shortToast(getBaseContext(), "Request Sent");
                    sentRequestSavedToFirestore();
                    dialog.cancel();
                    progressDialog.cancel();
                }else {
                    Helper.shortToast(getBaseContext(), task.getException().getMessage());
                    progressDialog.cancel();
                }
            }
        });

    }

    private void sentRequestSavedToFirestore() {

        Map map = new HashMap();
        map.put("post_id",list.getUid());
        map.put("uid", DatabaseHelper.Uid);

        DatabaseHelper.mDatabase.collection("send_requests")
                .add(map).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()){

                }
            }
        });



    }

    private void getNameImageFromFirestore() {

        DatabaseHelper.mDatabase.collection("users").document(list.getSender_uid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()){
                    if (task.getResult().exists()){

                        name = task.getResult().get("name").toString();
                        image = task.getResult().get("profile_img").toString();

                        mTxtName.setText(name);
                        Picasso.get().load(image)
                                .resize(500, 500)
                                .placeholder(R.drawable.image_profile).into(mImgProfile);

                    }
                }
            }
        });

    }

    private void addDataToFields() {

        mTxtTitle.setText(list.getTitle());
        mTxtDetails.setText(list.getDescription());
        mTxtBudget.setText(list.getPrice());
        mTxtAddress.setText(list.getAddress());
        mTxtTimeRequired.setText(list.getDate_time());

    }

    private void widgetsInflation() {

        mImgProfile = findViewById(R.id.img_workrequest_profile);
        mTxtName = findViewById(R.id.txt_workrequest_name);
        mTxtTitle = findViewById(R.id.txt_workrequest_title);
        mTxtDetails = findViewById(R.id.txt_workrequest_details);
        mTxtTimeRequired = findViewById(R.id.txt_workrequest_time_required);
        mTxtAddress = findViewById(R.id.txt_workrequest_address);
        mTxtBudget = findViewById(R.id.txt_workrequest_budget);
        mBtnSendOffer = findViewById(R.id.btn_send_offer);
    }

    public void PostComplaintClick(View view) {
    }

    public void ContactSellerClick(View view) {

        Intent intent = new Intent(getApplicationContext(), ChatsActivity.class);
        intent.putExtra("reciever_uid", list.getSender_uid());
        intent.putExtra("name", name);
        intent.putExtra("image_url", image);
        startActivity(intent);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        LatLng kohatLatLng = new LatLng(33.5612824,71.3974918);
//        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
//                kohatLatLng, 10);

        LatLng latLng = new LatLng(Double.parseDouble(list.getLat()),
                Double.parseDouble(list.getLng()));
        mMap.addMarker(new MarkerOptions().position(latLng).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                latLng, 10);

        mMap.animateCamera(location);

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