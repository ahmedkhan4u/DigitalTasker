package com.softrasol.ahmedgulsaqib.digitaltasker.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Interfaces.ToastMessage;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Models.ProfileSetupModel;
import com.softrasol.ahmedgulsaqib.digitaltasker.R;

import java.util.ArrayList;
import java.util.List;

public class ViewUsersActivity extends FragmentActivity implements OnMapReadyCallback, ToastMessage {

    private Toolbar toolbar;
    private TextView textView;
    private ImageButton mBtnBack;

    private List<ProfileSetupModel> list;

    private String mCategoryName;

    private GoogleMap mMap;

    //...............................................................................................
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_users);

        mCategoryName = getIntent().getStringExtra("category");

        toolbarInflation(mCategoryName);
        googleMapFragment();

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
                        ProfileSetupModel model = snapshot.toObject(ProfileSetupModel.class);
                        list.add(model);
                        showToast(model.getName());
                    }
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

    private void googleMapFragment() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_view_users);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng kohatLatLng = new LatLng(33.5612824,71.3974918);
        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                kohatLatLng, 10);

        for (ProfileSetupModel model : list){
            // Add a marker in Sydney and move the camera
            LatLng latLng = new LatLng(Double.parseDouble(model.getLat()),
                    Double.parseDouble(model.getLng()));
            mMap.addMarker(new MarkerOptions().position(latLng).title(model.getAddress()));

        }

    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
