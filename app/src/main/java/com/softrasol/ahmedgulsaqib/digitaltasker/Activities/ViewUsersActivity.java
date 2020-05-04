package com.softrasol.ahmedgulsaqib.digitaltasker.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Interfaces.ToastMessage;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Models.UserDataModel;
import com.softrasol.ahmedgulsaqib.digitaltasker.R;

import java.util.ArrayList;
import java.util.List;

public class ViewUsersActivity extends FragmentActivity implements ToastMessage, OnMapReadyCallback {

    private Toolbar toolbar;
    private TextView textView;
    private ImageButton mBtnBack;

    private List<UserDataModel> list;

    private String mCategoryName;

    private double mLatitude, mLongitude;

    private GoogleMap mMap;

    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;


    //...............................................................................................
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_users);

        mCategoryName = getIntent().getStringExtra("category");

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLocation();
        toolbarInflation(mCategoryName);
        getAllUsersFromFirestoreDb();

    }

    //...............................................................................................


    private void getAllUsersFromFirestoreDb() {

        list = new ArrayList<>();

        CollectionReference collectionReference = FirebaseFirestore.getInstance()
                .collection("users");

        Query query = collectionReference.whereEqualTo("category", mCategoryName);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){

                    for (QueryDocumentSnapshot snapshot : task.getResult()){
                        UserDataModel model = snapshot.toObject(UserDataModel.class);
                        list.add(model);
                    }

                    onMapReady(mMap);
                    showToast(currentLocation.getLatitude()+ " "+currentLocation.getLongitude());
                }else {
                    showToast(task.getException().getMessage());
                }
            }
        });

    }

    private void toolbarInflation(String mCategoryName) {
        toolbar = findViewById(R.id.toolbar_view_users);
        textView = toolbar.findViewById(R.id.toolbarText);
        textView.setText(mCategoryName);
        mBtnBack = toolbar.findViewById(R.id.btnBack);
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    private void fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_view_users);
                    assert supportMapFragment != null;
                    supportMapFragment.getMapAsync(ViewUsersActivity.this);
                }
            }
        });
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        LatLng kohatLatLng = new LatLng(33.5612824,71.3974918);
        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                kohatLatLng, 10);
        mMap.animateCamera(location);
        mMap.setMyLocationEnabled(true);

        for (UserDataModel model : list){
            LatLng latLng = new LatLng(Double.parseDouble(model.getLat()),
                    Double.parseDouble(model.getLng()));
            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(model.getAddress());
            mMap.addMarker(markerOptions);
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLocation();
                }
                break;
        }
    }


    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
