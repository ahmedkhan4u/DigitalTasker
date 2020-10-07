package com.softrasol.ahmedgulsaqib.digitaltasker.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.DialogOnAnyDeniedMultiplePermissionsListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.multi.SnackbarOnAnyDeniedMultiplePermissionsListener;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Interfaces.ToastMessage;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Models.NotificationsModel;
import com.softrasol.ahmedgulsaqib.digitaltasker.R;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PhoneAuthActivity extends AppCompatActivity implements ToastMessage {
    //...............................................................................................

    private FirebaseAuth mAuth;
    private TextInputEditText mTxtPhoneNumber, mTxtConfirmationCode;

    private ProgressDialog progressDialog;
    private String code, smsCode;
    private String comPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);

        mAuth = FirebaseAuth.getInstance();

        pemissionsCheck();


    }

    private void pemissionsCheck() {

        Dexter.withContext(PhoneAuthActivity.this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ).withListener(new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {

                MultiplePermissionsListener dialogMultiplePermissionsListener =
                        DialogOnAnyDeniedMultiplePermissionsListener.Builder
                                .withContext(PhoneAuthActivity.this)
                                .withTitle("Storage Camera & Location permission")
                                .withMessage("Both camera and storage permission are needed to take pictures users")
                                .withButtonText(android.R.string.ok)
                                .withIcon(R.mipmap.ic_launcher)
                                .build();


                MultiplePermissionsListener snackbarMultiplePermissionsListener =
                        SnackbarOnAnyDeniedMultiplePermissionsListener.Builder
                                .with(findViewById(R.id.view), "Camera and audio access is needed to take pictures of your dog")
                                .withOpenSettingsButton("Settings")
                                .withCallback(new Snackbar.Callback() {
                                    @Override
                                    public void onShown(Snackbar snackbar) {
                                        // Event handler for when the given Snackbar is visible
                                    }
                                    @Override
                                    public void onDismissed(Snackbar snackbar, int event) {
                                        // Event handler for when the given Snackbar has been dismissed
                                    }
                                })
                                .build();

            }
            @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();

    }

    public void ButtonPhoneAuthClick(View view) {
        phoneAuthBottomSheetDialog();
    }

    //...............................................................................................

    private void progressDialogFun() {
        progressDialog = new ProgressDialog(PhoneAuthActivity.this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

    }

    //Bottom Sheet Dialog Phone Contains Phone Authentication Fields...
    private void phoneAuthBottomSheetDialog() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(PhoneAuthActivity.this);
        bottomSheetDialog.setContentView(R.layout.phone_auth_bottom_sheet);
        bottomSheetDialog.show();
        bottomSheetDialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
        bottomSheetDialog.setCancelable(false);

        bottomSheetDialog.findViewById(R.id.btn_close_bottom_sheet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.cancel();
            }
        });

        bottomSheetDialog.findViewById(R.id.btn_send_code).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyPhoneNumber(bottomSheetDialog);
            }
        });

        bottomSheetDialog.findViewById(R.id.btn_confirm_code).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmAuthCode(bottomSheetDialog);
            }
        });

    }

    private void confirmAuthCode(BottomSheetDialog bottomSheetDialog) {
        mTxtConfirmationCode = bottomSheetDialog.findViewById(R.id.txt_confirmation_code);
        String confirmationCode = mTxtConfirmationCode.getText().toString().trim();

        if (code.isEmpty()) {
            mTxtConfirmationCode.setError("Required");
            mTxtConfirmationCode.requestFocus();
            return;
        }
        verifyCode(confirmationCode);

    }

    private void verifyPhoneNumber(BottomSheetDialog bottomSheetDialog) {
        mTxtPhoneNumber = bottomSheetDialog.findViewById(R.id.txt_phone_number);
        String phoneNumber = mTxtPhoneNumber.getText().toString().trim();

        if (phoneNumber.isEmpty()) {
            mTxtPhoneNumber.setError("Required");
            mTxtPhoneNumber.requestFocus();
            return;
        }
        if (phoneNumber.length() < 10) {
            mTxtPhoneNumber.setError("Enter Complete Phone Number");
            mTxtPhoneNumber.requestFocus();
            return;
        }


        comPhoneNumber = "+92"+phoneNumber;
        progressDialogFun();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                comPhoneNumber,        // Phone number to verify
                120,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                TaskExecutors.MAIN_THREAD,               // Activity (for callback binding)
                mCallbacks);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                @Override
                public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    code = s;
                    if (smsCode == null) {
                        progressDialog.cancel();
                    }
                }

                @Override
                public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                    smsCode = phoneAuthCredential.getSmsCode();
                    if (smsCode != null) {
                        verifyCode(smsCode);
                        progressDialog.cancel();
                    }
                }

                @Override
                public void onVerificationFailed(FirebaseException e) {

                    Toast.makeText(PhoneAuthActivity.this,
                            e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.cancel();

                }
            };

    private void verifyCode(String smsCode) {
        try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(code, smsCode);
            signInWithPhoneAuthCredential(credential);
        } catch (Exception e) {
            Toast.makeText(PhoneAuthActivity.this,
                    e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(PhoneAuthActivity.this, "Successful",
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();

                            createUserInFirebaseDatabase();

                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(PhoneAuthActivity.this,
                                        "In Correct Verification Code", Toast.LENGTH_SHORT).show();
                                progressDialog.cancel();
                            }
                        }
                    }
                });
    }

    private void createUserInFirebaseDatabase() {

        CollectionReference mRef = FirebaseFirestore.getInstance()
                .collection("users");

        Query query = mRef.whereEqualTo("uid", FirebaseAuth.getInstance().getUid());


        mRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.getResult().size() == 0){
                    
                }
            }
        });

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult().size()>0){
                        goToHomeActivity();
                    }else {
                        saveUserData();
                    }
                }
            }
        });

    }

    private void goToHomeActivity() {
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void saveUserData() {
        Map map = new HashMap();
        map.put("uid", FirebaseAuth.getInstance().getUid());
        map.put("phone",comPhoneNumber);
        CollectionReference firestore = FirebaseFirestore.getInstance()
                .collection("users");
        firestore.document(FirebaseAuth.getInstance().getUid())
                .set(map).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    showToast("User Created Successfully");
                    goToHomeActivity();
                } else {
                    showToast(task.getException().getMessage());
                }
            }
        });
    }


    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
