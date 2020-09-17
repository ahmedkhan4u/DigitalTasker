package com.softrasol.ahmedgulsaqib.digitaltasker.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Helper.DatabaseHelper;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Helper.Notification;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Interfaces.ToastMessage;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Models.NotificationsModel;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Models.UserDataModel;
import com.softrasol.ahmedgulsaqib.digitaltasker.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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
    private StorageReference mStorage;
    private CircleImageView mImgProfileImage;
    private ImageView mImgCnicFront, mImgCnicBack;
    private TextInputEditText mEdtName, mEdtEmail, mEdtPrice, mEdtDescription, mEdtAddress;
    private TextInputLayout mLayoutTxtPrice;
    private Button mBtnCreateProfile;

    private Uri profileImageUri, cnicFrontUri, cnicBackUri;

    private int imagenum = 0;

    private RelativeLayout mLayoutProfileImage;

    private double latitude = 0, longitude = 0;

    private String name, email, price, description, address, category,
            profileImageDownloadUrl, cnincForntImageDownloadUrl, cnicBackImageDownloadUrl;
    private ProgressDialog progressDialog;

    private String[] categoryList = {"Choose Category", "Buyer", "Electrician", "Plumber", "Cleaner", "Delivery", "Handyman",
            "Carpenter", "Labour", "Ac Repair"};

    //...............................................................................................

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);

        googleMapFragment();
        toolbarInflation();
        widgetInflation();// XML
        fillCategoryList();// Spinner

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

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category = categoryList[i];

                if (category.equalsIgnoreCase("Buyer")) {
                    showToast("Buyer");
                    mLayoutTxtPrice.setVisibility(View.GONE);
                    mEdtPrice.setText("0");
                } else {
                    mLayoutTxtPrice.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void widgetInflation() {
        mSpinner = findViewById(R.id.dropdown_menu_account_setup);
        mImgProfileImage = findViewById(R.id.img_person_profile_setup);
        mImgCnicFront = findViewById(R.id.img_cnic_front_profile_setup);
        mImgCnicBack = findViewById(R.id.img_cnic_back_profile_setup);
        mEdtName = findViewById(R.id.edt_name_profile_setup);
        mEdtEmail = findViewById(R.id.edt_email_profile_setup);
        mEdtPrice = findViewById(R.id.edt_price_profile_setup);
        mEdtDescription = findViewById(R.id.edt_description_profile_setup);
        mEdtAddress = findViewById(R.id.edt_address_profile_setup);
        mBtnCreateProfile = findViewById(R.id.btn_create_profile_setup);
        mLayoutProfileImage = findViewById(R.id.layout_profile_img_setup);
        mLayoutTxtPrice = findViewById(R.id.layout_price_profile_setup);
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
                Geocoder geocoder = new Geocoder(ProfileSetupActivity.this, Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude,1);
                    address = addresses.get(0).getAddressLine(0);
                    mEdtAddress.setText(address);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void personImageClick() {
        mLayoutProfileImage.setOnClickListener(new View.OnClickListener() {
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
        mImgCnicFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagenum = 1;
                // start picker to get image for cropping and then use the image in cropping activity
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(ProfileSetupActivity.this);
            }
        });
    }

    private void cnicBackImageClick() {

        mImgCnicBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagenum = 2;
                // start picker to get image for cropping and then use the image in cropping activity
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
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
                        mImgProfileImage.setImageURI(profileImageUri);
                        break;

                    case 1:
                        cnicFrontUri = result.getUri();
                        mImgCnicFront.setImageURI(cnicFrontUri);
                        mImgCnicFront.setPadding(0,0,0,0);
                        mImgCnicFront.setScaleType(ImageView.ScaleType.FIT_XY);
                        break;

                    case 2:
                        cnicBackUri = result.getUri();
                        mImgCnicBack.setImageURI(cnicBackUri);
                        mImgCnicBack.setPadding(0,0,0,0);
                        mImgCnicBack.setScaleType(ImageView.ScaleType.FIT_XY);
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

                name = mEdtName.getText().toString().trim();
                email = mEdtEmail.getText().toString().trim();
                description = mEdtDescription.getText().toString().trim();
                price = mEdtPrice.getText().toString().trim();
                address = mEdtAddress.getText().toString().trim();

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
            mEdtName.setError("Required");
            mEdtName.requestFocus();
            return;
        }

        if (email.isEmpty()){
            mEdtEmail.setError("Required");
            mEdtEmail.requestFocus();
            return;
        }

        if (! Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            mEdtEmail.setError("Kindly Input Proper Email Address");
            mEdtEmail.requestFocus();
            return;
        }

        if (description.isEmpty()){
            mEdtDescription.setError("Required");
            mEdtDescription.requestFocus();
            return;
        }

        if (price.isEmpty()){
            mEdtPrice.setError("Required");
            mEdtPrice.requestFocus();
            return;
        }

        if (address.isEmpty()){
            mEdtAddress.setError("Required");
            mEdtAddress.requestFocus();
            return;
        }

        if (latitude == 0){
            showToast("Kindly Tap On Map To Choose Location");
            return;
        }

        sendDataToFirebaseDb();
        showProgressDialog();

    }

    private void sendDataToFirebaseDb() {

        saveImageToFirebaseDatabase(profileImageUri,"profile_image");

    }

    private void saveImageToFirebaseDatabase(final Uri profileImageUri, final String img) {
        showProgressDialog();
        mStorage = FirebaseStorage.getInstance().getReference().child("user_images")
                .child(FirebaseAuth.getInstance().getUid());

        final StorageReference reference = mStorage.child(img);

        Task uploadTask = reference.putFile(profileImageUri);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return reference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    if (img.equalsIgnoreCase("profile_image")){
                        profileImageDownloadUrl = task.getResult().toString();
                        saveImageToFirebaseDatabase(cnicFrontUri,"cnic_front_image");
                        showToast("Profile Image Uploaded");
                    }
                    else if (img.equalsIgnoreCase("cnic_front_image")){
                        cnincForntImageDownloadUrl = task.getResult().toString();
                        saveImageToFirebaseDatabase(cnicBackUri,"cnic_back_image");
                        showToast("Cnic Front Image Uploaded");
                    }
                    else if (img.equalsIgnoreCase("cnic_back_image")){
                        cnicBackImageDownloadUrl = task.getResult().toString();
                        showToast("Cnic Back Image Uploaded");
                        saveDetailsToFireStore();
                    }


                } else {
                    // Handle failures
                    // ...
                    showToast(task.getException().getMessage());
                    progressDialog.cancel();
                }
            }
        });
    }

    private void saveDetailsToFireStore() {

        CollectionReference collectionReference = FirebaseFirestore.getInstance()
                .collection("users");

        DocumentReference documentReference = collectionReference
                .document(FirebaseAuth.getInstance().getUid());

        UserDataModel model = new UserDataModel(name, email, price, description,
                address, category, profileImageDownloadUrl,
                cnincForntImageDownloadUrl, cnicBackImageDownloadUrl, latitude+"",
                longitude+"", "false", "false",System.currentTimeMillis()+"","","","");

        documentReference.update(
                "name", model.getName(),
                "email", model.getEmail(),
                "price", model.getPrice(),
                "description", model.getDescription(),
                "address", model.getAddress(),
                "category", model.getCategory(),
                "profile_img", model.getProfile_img(),
                "cnic_front_img", model.getCnic_front_img(),
                "cnic_back_img", model.getCnic_back_img(),
                "lat", model.getLat(),
                "lng", model.getLng(),
                "is_verified", model.getIs_verified(),
                "is_restrict", model.getIs_restrict(),
                "time_stamp", model.getDate()

        ).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    showToast("Data Saved Successfully");
                    sendNotificationToAdmin();
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    finish();
                    progressDialog.cancel();
                }else {
                    showToast(task.getException().getMessage());
                    progressDialog.cancel();
                }
            }
        });
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setMessage("Uploading Data In Progress");
        progressDialog.show();

    }

    private void sendNotificationToAdmin() {

        String uniqueKey = DatabaseHelper.mDatabase.collection("notifications").document().getId();
                NotificationsModel model = new NotificationsModel("New user added"
                ,"Verification approval pending",System.currentTimeMillis()+"",FirebaseAuth.getInstance().getUid()
                ,"admin","false","new user",uniqueKey);
        Notification.sendNotification(getApplicationContext(), model);

//        CollectionReference notificationReference = FirebaseFirestore.getInstance()
//                .collection("notifications");
//
//        String uid = notificationReference.document().getId();
//        DocumentReference documentReference = notificationReference.document(uid);
//
//        Date date = new Date();
//        String mDate = date.toLocaleString();
//
//        NotificationsModel model = new NotificationsModel("New user added"
//                ,"Verification approval pending",mDate,FirebaseAuth.getInstance().getUid()
//                ,"admin","false","new user",uid);
//
//
//        documentReference.set(model).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                    if (task.isSuccessful()){
//                        showToast("Your request is pending for the admin approval");
//                    }else {
//                        showToast(task.getException().getMessage());
//                    }
//            }
//        });
    }
}
