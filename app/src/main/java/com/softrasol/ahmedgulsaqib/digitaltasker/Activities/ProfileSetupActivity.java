package com.softrasol.ahmedgulsaqib.digitaltasker.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Interfaces.ToastMessage;
import com.softrasol.ahmedgulsaqib.digitaltasker.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileSetupActivity extends FragmentActivity implements OnMapReadyCallback
, ToastMessage {

    private Toolbar toolbar;
    private TextView textView;
    private ImageButton mBtnBack;

    private Spinner mSpinner;
    private GoogleMap mMap;

    private CircleImageView mProfileImage;
    private ImageView mCnicFrontImage, mCnicBackImage;
    private TextInputEditText mName, mEmail, mPrice , mDescription, mAddress;
    private Button mBtnCreateProfile;

    private Uri profileImageUri, cnicFrontUri, cnicBackUri;

    private int imagenum = 0;

    private String name, email, price , description, address, category;

    private String [] categoryList = {"Choose Category", "Buyer", "Electrician", "Plumber", "Cleaner", "Delivery", "Handyman",
    "Carpenter","Labour", "Ac Repair"};

    //...............................................................................................

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);

        googleMapFragment();
        toolbarInflation();
        widgetInflation();
        fillCategoryList();

        personImageClick();
        cnicFrontImageClick();
        cnicBackImageClick();

        //Button Click
        submitDetailsButtonClick();


    }

    //...............................................................................................

    private void googleMapFragment() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_profile_setup);
        mapFragment.getMapAsync(this);
    }

    private void fillCategoryList() {

        ArrayAdapter categoryListAdapter = new ArrayAdapter(getApplicationContext(),
                R.layout.dropdown_profession_list_item, categoryList);
        mSpinner.setAdapter(categoryListAdapter);

    }

    private void widgetInflation(){
        mSpinner = findViewById(R.id.dropdown_menu_account_setup);
        mProfileImage = findViewById(R.id.img_person_profile_setup);
        mCnicFrontImage = findViewById(R.id.img_cnic_front_profile_setup);
        mCnicBackImage = findViewById(R.id.img_cnic_back_profile_setup);
        mName = findViewById(R.id.edt_name_profile_setup);
        mEmail = findViewById(R.id.edt_email_profile_setup);
        mPrice = findViewById(R.id.edt_price_profile_setup);
        mDescription = findViewById(R.id.edt_description_profile_setup);
        mAddress = findViewById(R.id.edt_address_profile_setup);
        mBtnCreateProfile = findViewById(R.id.btn_create_profile_setup);
    }

    private void toolbarInflation() {
        toolbar = findViewById(R.id.toolbar_profile_setup);
        textView = toolbar.findViewById(R.id.toolbarText);
        textView.setText("Profile Setup");
        mBtnBack = toolbar.findViewById(R.id.btnBack);
        mBtnBack.setVisibility(View.GONE);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        LatLng kohatLatLng = new LatLng(33.5612824,71.3974918);
        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                kohatLatLng, 10);
        mMap.animateCamera(location);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
                List<Address> addresses = new ArrayList<>();
                Geocoder geocoder = new Geocoder(ProfileSetupActivity.this, Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude,1);
                    mAddress.setText(addresses.get(0).getAddressLine(0));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    private void personImageClick() {
        mProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                imagenum = 0;
                // start picker to get image for cropping and then use the image in cropping activity
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(ProfileSetupActivity.this);
            }
        });
    }

    private void cnicFrontImageClick() {
        mCnicFrontImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagenum = 1;
                // start picker to get image for cropping and then use the image in cropping activity
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,2)
                        .start(ProfileSetupActivity.this);
            }
        });
    }

    private void cnicBackImageClick() {

        mCnicBackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagenum = 2;
                // start picker to get image for cropping and then use the image in cropping activity
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,2)
                        .start(ProfileSetupActivity.this);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                switch (imagenum) {
                    case 0:
                        profileImageUri = result.getUri();
                        mProfileImage.setImageURI(profileImageUri);
                        break;

                    case 1:
                        cnicFrontUri = result.getUri();
                        mCnicFrontImage.setImageURI(cnicFrontUri);
                        mCnicFrontImage.setPadding(0,0,0,0);
                        mCnicFrontImage.setScaleType(ImageView.ScaleType.FIT_XY);
                        break;

                    case 2:
                        cnicBackUri = result.getUri();
                        mCnicBackImage.setImageURI(cnicFrontUri);
                        mCnicBackImage.setPadding(0,0,0,0);
                        mCnicBackImage.setScaleType(ImageView.ScaleType.FIT_XY);
                        break;
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void submitDetailsButtonClick(){
        mBtnCreateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name = mName.getText().toString().trim();
                email = mEmail.getText().toString().trim();
                description = mDescription.getText().toString().trim();
                price = mPrice.getText().toString().trim();
                address = mAddress.getText().toString().trim();

                validatingAllTheFields();

            }
        });
    }

    private void validatingAllTheFields() {

        if (profileImageUri == null){
            showToast("Kindly Choose Your Profile Image");
            return;
        }

        if (cnicBackUri == null){
            showToast("Kindly Choose Your Cnic Back Image");
            return;
        }

        if (cnicFrontUri == null){
            showToast("Kindly Choose Your Cnic Front Image");
            return;
        }

        if (category.equalsIgnoreCase("choose category")){
            showToast("Kindly Choose Category");
            return;
        }

        if (name.isEmpty()){
            mName.setError("Required");
            mName.requestFocus();
            return;
        }

        if (email.isEmpty()){
            mEmail.setError("Required");
            mEmail.requestFocus();
            return;
        }

        if (! Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            mEmail.setError("Kindly Input Proper Email Address");
            mEmail.requestFocus();
            return;
        }

        if (description.isEmpty()){
            mDescription.setError("Required");
            mEmail.requestFocus();
            return;
        }

        if (price.isEmpty()){
            mPrice.setError("Required");
            mPrice.requestFocus();
            return;
        }

        if (address.isEmpty()){
            mAddress.setError("Required");
            mAddress.requestFocus();
            return;
        }

        sendDataToFirebaseDb();

    }

    private void sendDataToFirebaseDb() {

    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
