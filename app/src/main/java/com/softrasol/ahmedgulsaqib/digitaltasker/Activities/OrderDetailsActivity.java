package com.softrasol.ahmedgulsaqib.digitaltasker.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Adapters.MyOrdersAdapter;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Helper.DatabaseHelper;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Helper.Helper;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Models.OrderModel;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Models.UserDataModel;
import com.softrasol.ahmedgulsaqib.digitaltasker.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

public class OrderDetailsActivity extends FragmentActivity implements OnMapReadyCallback {

    private CircleImageView mImgProfile;
    private TextView mTxtName, mTxtBudget, mTxtAddress, mTxtDescription, mTxtTimeRequired;

    public static OrderModel list = MyOrdersAdapter.data.get(0);
    private Button mBtnScanCode;
    private ImageView imageViewQrCode;
    private GoogleMap mMap;
    double lat = 0, lng = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        toolbarInflation();
        widgetsInflation();
        scannerView();
        btnScanCode();
        getDetailsFromFirestore();

    }

    private void scannerView() {
    }

    private void btnScanCode() {

        mBtnScanCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), BarCodeScannerActivity.class));
            }
        });

    }

    private void getDetailsFromFirestore() {

        DatabaseHelper.mDatabase.collection("orders")
                .document(list.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                if (e!=null){
                    Helper.logMessage(e.getMessage());
                    return;
                }

                if (documentSnapshot.exists()){

                    final OrderModel model = documentSnapshot.toObject(OrderModel.class);

                    mTxtBudget.setText("Budget: "+model.getBudget());
                    mTxtDescription.setText("Description: "+model.getDescription());

                    if (model.getStatus().equalsIgnoreCase("Cancelled") ||
                            model.getStatus().equalsIgnoreCase("Completed")){
                        mBtnScanCode.setVisibility(View.GONE);
                        imageViewQrCode.setVisibility(View.GONE);
                        mTxtTimeRequired.setText("Time Passed");
                    }

                    CountDownTimer timer = new CountDownTimer(Long.parseLong(model.getTime_stamp()), 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            long seconds = millisUntilFinished / 1000;
                            long minutes = seconds / 60;
                            long hours = minutes / 60;
                            long days = hours / 24;
                            String time = "Time Left : "+hours % 24 + " hr " + minutes % 60 + " min " + seconds % 60+" sec ";
                            mTxtTimeRequired.setText(time);
                        }

                        @Override
                        public void onFinish() {

                        }
                    };
                    timer.start();


                    if (list.getReciever_id().equalsIgnoreCase(DatabaseHelper.Uid)){
                        getUserDetailsFromFirestore(list.getSender_id());
                    }else {
                        getUserDetailsFromFirestore(list.getReciever_id());
                    }

                }

            }
        });

    }

    private void getUserDetailsFromFirestore(String id) {

        DatabaseHelper.mDatabase.collection("users").document(id).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                if (e != null){
                    Helper.logMessage(e.getMessage());
                    return;
                }

                if (documentSnapshot.exists()){

                    UserDataModel model = documentSnapshot.toObject(UserDataModel.class);
                    lat = Double.parseDouble(model.getLat());
                    lng = Double.parseDouble(model.getLng());
                    Picasso.get().load(model.getProfile_img())
                            .resize(60, 60)
                            .placeholder(R.drawable.image_profile).into(mImgProfile);

                    onMapReady(mMap);
                    mTxtName.setText("Name: "+model.getName());
                    mTxtAddress.setText("Address: "+model.getAddress());


                    try {
                        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                        Bitmap bitmap = barcodeEncoder.encodeBitmap(list.getUid(), BarcodeFormat.QR_CODE, 400, 400);
                        imageViewQrCode = (ImageView) findViewById(R.id.qr_code);
                        imageViewQrCode.setImageBitmap(bitmap);
                    } catch(Exception ex) {
                        Helper.logMessage("QR Code Exception : "+ ex.getMessage());
                    }

                }

            }
        });

    }

    private void widgetsInflation() {

        mImgProfile = findViewById(R.id.img_orderdetail_profile);
        mTxtName = findViewById(R.id.txt_orderdetail_name);
        mTxtBudget = findViewById(R.id.txt_orderdetails_budget);
        mTxtAddress = findViewById(R.id.txt_orderdetail_address);
        mTxtDescription = findViewById(R.id.txt_orderdetails_description);
        mTxtTimeRequired = findViewById(R.id.txt_orderdetail_time_required);
        mBtnScanCode = findViewById(R.id.btn_scan_qr_code);

    }

    private void toolbarInflation() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView textView = toolbar.findViewById(R.id.toolbarText);
        textView.setText("Work Requests");
        ImageButton mBtnBack = toolbar.findViewById(R.id.btnBack);
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    public void CancelOrder(View view) {
        cancelOrder();
    }

    private void cancelOrder() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(OrderDetailsActivity.this);
        builder1.setTitle("Alert!");
        builder1.setMessage("Are you sure you want to cancel order.");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Map map = new HashMap();
                        map.put("status", "Cancelled");
                        DatabaseHelper.mDatabase.collection("orders").document(OrderDetailsActivity.list.getUid())
                                .update(map).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()){
                                    Helper.shortToast(getApplicationContext(), "Order Cancelled");
                                }
                            }
                        });
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

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

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

        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                kohatLatLng, 5);

        LatLng latLng = new LatLng(lat, lng);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        mMap.addMarker(markerOptions);

        mMap.animateCamera(location);

    }
}