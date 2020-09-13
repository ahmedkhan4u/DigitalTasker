package com.softrasol.ahmedgulsaqib.digitaltasker.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Adapters.ViewUsersAdapter;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Helper.DatabaseHelper;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Interfaces.ToastMessage;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Models.UserDataModel;
import com.softrasol.ahmedgulsaqib.digitaltasker.R;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class ViewUsersActivity extends FragmentActivity implements ToastMessage, OnMapReadyCallback,
        SwipeRefreshLayout.OnRefreshListener {

    private Toolbar toolbar;
    private TextView textView;
    private ImageButton mBtnBack;

    private List<UserDataModel> usersList;

    private String mCategoryName;

    private RecyclerView mRecyclerViewUsers;

    private GoogleMap mMap;

    private boolean gps_enabled = false;
    private boolean network_enabled = false;

    double latitude = 0, longitude = 0;

    Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    private SwipeRefreshLayout mSwipeRefreshLayout;


    //...............................................................................................
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_users);

        mCategoryName = getIntent().getStringExtra("category");
        mSwipeRefreshLayout = findViewById(R.id.view_users_swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_view_users);
        supportMapFragment.getMapAsync(ViewUsersActivity.this);

        fetchLocation();
        toolbarInflation(mCategoryName);
        checkLocationServiceEnableOrDisabled();

    }

    //...............................................................................................

    private void getAllUsersFromFirestoreDb() {

        usersList = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").whereEqualTo("category", mCategoryName)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override

                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                        if (!usersList.isEmpty()) {
                            usersList.clear();
                        }

                        if (e != null) {
                            showToast(e.getMessage());
                            return;
                        }

                        for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                            UserDataModel model = snapshot.toObject(UserDataModel.class);
                            if (model.getIs_verified().equalsIgnoreCase("true") && !model.getUid().equals(DatabaseHelper.Uid)) {
                                usersList.add(model);
                            }
                        }

                        if (usersList.isEmpty()) {
                            return;
                        } else {
                            addMarkersOnGoogleMap();
                        }
                        //showToast(currentLocation.getLatitude()+ " "+currentLocation.getLongitude());
                        recyclerViewUsers();

                    }
                });
    }

    private void recyclerViewUsers() {

        mRecyclerViewUsers = findViewById(R.id.recycler_view_view_users);
        mRecyclerViewUsers.setLayoutManager(new LinearLayoutManager(this));

        ViewUsersAdapter adapter = new ViewUsersAdapter(getApplicationContext(), usersList,
                latitude, longitude);
        mRecyclerViewUsers.setAdapter(adapter);

        adapter.notifyDataSetChanged();

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
        task.addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {

                if (task.isSuccessful()) {

                    try {
                        latitude = task.getResult().getLatitude();
                        longitude = task.getResult().getLongitude();
                    } catch (Exception ex) {
                        Log.d("dxdiag", ex.getMessage());
                    }


                    showToast(latitude + "Longitude" + longitude);
                    getAllUsersFromFirestoreDb();
                } else {
                    Log.d("dxdiag", task.getException().getMessage());
                    getAllUsersFromFirestoreDb();
                }
            }
        });
//        task.addOnSuccessListener(new OnSuccessListener<Location>() {
//            @Override
//            public void onSuccess(Location location) {
//                if (location != null) {
//                    currentLocation = location;
//                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_view_users);
//                    assert supportMapFragment != null;
//                    supportMapFragment.getMapAsync(ViewUsersActivity.this);
//                }
//            }
//        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


        if (gps_enabled == false) {

            return;
        }

        mMap = googleMap;

        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        LatLng kohatLatLng = new LatLng(33.5612824, 71.3974918);
        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                kohatLatLng, 10);
        mMap.animateCamera(location);
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

    }

    private void addMarkersOnGoogleMap() {


        for (final UserDataModel model : usersList){

                try{
                    LatLng latLng = new LatLng(Double.parseDouble(model.getLat()),
                            Double.parseDouble(model.getLng()));
                    MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(model.getAddress());
                    mMap.addMarker(markerOptions);
                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            Intent intent = new Intent(getApplicationContext(), ViewUserDetailsActivity.class);
                            intent.putExtra("uid",model.getUid());
                            startActivity(intent);
                            return false;
                        }
                    });
                }catch (Exception ex){
                    Log.d("dxdiag", ex.getMessage());
                }
        }
    }

    private void checkLocationServiceEnableOrDisabled() {

        LocationManager lm = (LocationManager)ViewUsersActivity.this.getSystemService(Context.LOCATION_SERVICE);

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {
            // notify user
            new AlertDialog.Builder(ViewUsersActivity.this)
                    .setMessage("GPS Network Not Enabled")
                    .setPositiveButton("Open Settings", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ViewUsersActivity.this.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    }).setNegativeButton("", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }).show();
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

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fetchLocation();
                getAllUsersFromFirestoreDb();
                onMapReady(mMap);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 2000);
    }
}
