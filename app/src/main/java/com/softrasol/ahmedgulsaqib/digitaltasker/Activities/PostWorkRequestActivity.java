package com.softrasol.ahmedgulsaqib.digitaltasker.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

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
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Helper.DatabaseHelper;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Helper.Helper;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Models.WorkRequestModel;
import com.softrasol.ahmedgulsaqib.digitaltasker.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PostWorkRequestActivity extends FragmentActivity implements OnMapReadyCallback {

    private Toolbar toolbar;
    private TextView textView;
    private ImageButton mBtnBack;

    private TextInputEditText mTxtTitle;
    private TextInputEditText mTxtDesc;
    private TextInputEditText mTxtPrice, mTxtAddress;
    private TextView mTxtDate;

    private double latitude = 0, longitude = 0;
    private String address;

    private Button mBtnCancel;
    private Button mBtnPost;

    private GoogleMap mMap;

    String date_time = "";
    int mYear, mMonth, mDay, mHour, mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_work_request);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        widgetsInflation();
        toolbarInflation();
        postButtonClicked();
        chooseDateClick();





    }

    private void chooseDateClick() {
        mTxtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker(mTxtDate);
            }
        });
    }

    private void postButtonClicked() {

        mBtnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = mTxtTitle.getText().toString().trim();
                String description = mTxtDesc.getText().toString().trim();
                String price = mTxtPrice.getText().toString().trim();
                String address = mTxtAddress.getText().toString().trim();

                if (title.isEmpty()){
                    mTxtTitle.setError("Required");
                    mTxtTitle.requestFocus();
                    return;
                }

                if (description.isEmpty()){
                    mTxtDesc.setError("Required");
                    mTxtDesc.requestFocus();
                    return;
                }

                if (price.isEmpty()){
                    mTxtPrice.setError("Required");
                    mTxtPrice.requestFocus();
                    return;
                }

                if (address.isEmpty()){
                    mTxtAddress.setError("Required");
                    mTxtAddress.requestFocus();
                    return;
                }

                if (latitude == 0 || longitude == 0){
                    Helper.longToast(getBaseContext(), "Kindly tap on map to select location");
                    return;
                }

                if (date_time.equals("")){
                    Helper.longToast(getBaseContext(), "Kindly choose a date");
                    return;
                }

                saveWorkRequestToFirestore(title, description, price, latitude+"",
                        longitude+"", address);

            }
        });

    }

    private void widgetsInflation() {

        mTxtTitle = findViewById(R.id.txt_postrequest_title);
        mTxtDesc = findViewById(R.id.txt_postrequest_desc);
        mTxtPrice = findViewById(R.id.txt_postrequest_price);
        mTxtDate = findViewById(R.id.txt_postrequest_date);
        mTxtAddress = findViewById(R.id.edt_address_post_work);

        mBtnPost = findViewById(R.id.btn_post_request);

    }

    private void saveWorkRequestToFirestore(String title, String description, String price,
                                            String lat, String lng, String address) {

        String uniqueKey = DatabaseHelper.mDatabase.collection("work_requests")
                .document().getId();

        WorkRequestModel model = new WorkRequestModel(title, description,price,date_time,
                lat,lng,address, DatabaseHelper.Uid,
                System.currentTimeMillis()+"", uniqueKey);

        DatabaseHelper.mDatabase.collection("work_requests").document(uniqueKey)
                .set(model).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Helper.shortToast(getApplicationContext(), "Request Posted");
                }else {
                    Helper.logMessage(task.getException().getMessage());
                }
            }
        });
    }


    private void toolbarInflation() {

        toolbar = findViewById(R.id.toolbar);
        textView = toolbar.findViewById(R.id.toolbarText);
        textView.setText("Post Work");
        mBtnBack = toolbar.findViewById(R.id.btnBack);

        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
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
        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                kohatLatLng, 10);
        mMap.animateCamera(location);


        getAddressFromGoogleMap();

    }

    private void getAddressFromGoogleMap() {
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                latitude = latLng.latitude;
                longitude = latLng.longitude;

                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
                List<Address> addresses = new ArrayList<>();
                Geocoder geocoder = new Geocoder(PostWorkRequestActivity.this, Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude,1);
                    address = addresses.get(0).getAddressLine(0);
                    mTxtAddress.setText(address);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void datePicker(final TextView mTxtDate){

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        date_time = ""+dayOfMonth + "/" + Helper.getMonth(monthOfYear + 1) + "/" + year;
                        //*************Call Time Picker Here ********************
                        tiemPicker(mTxtDate);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void tiemPicker(final TextView mTxtDate){
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        mHour = hourOfDay;
                        mMinute = minute;

                        mTxtDate.setText(date_time+" - "+hourOfDay + "H: " + minute+"M ");
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }
}