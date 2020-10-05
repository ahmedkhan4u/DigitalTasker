package com.softrasol.ahmedgulsaqib.digitaltasker.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.zxing.Result;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Adapters.OrdersAdapter;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Helper.DatabaseHelper;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Models.RatingsModel;
import com.softrasol.ahmedgulsaqib.digitaltasker.R;

import java.util.HashMap;
import java.util.Map;

public class BarCodeScannerActivity extends AppCompatActivity {

    private CodeScanner mCodeScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_code_scanner);


        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                BarCodeScannerActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (OrderDetailsActivity.list.getUid().equalsIgnoreCase(result.getText())
                        || result.getText().equalsIgnoreCase(OrderDetailsActivity.list.getUid())){
                            Toast.makeText(BarCodeScannerActivity.this, result.getText(), Toast.LENGTH_SHORT).show();
                            setOrderStatusCompleteInFirestore();
                        }

                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });


    }

    private void setOrderStatusCompleteInFirestore() {

        Map map = new HashMap();
        map.put("status", "Completed");
        DatabaseHelper.mDatabase.collection("orders").document(OrderDetailsActivity.list.getUid())
                .update(map).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()){

                    sellerReview();
                    buyerReview();

                    finish();

                }
            }
        });
    }

    private void buyerReview() {

        String uniqueKey = DatabaseHelper.mDatabase.collection("reviews").document().getId();

        RatingsModel model = new RatingsModel
                ("", OrderDetailsActivity.list.getSender_id(),  OrderDetailsActivity.list.getReciever_id(),
                        System.currentTimeMillis()+"", "", uniqueKey, "false");
        DatabaseHelper.mDatabase.collection("reviews").document(uniqueKey).
                set(model);

    }

    private void sellerReview() {

        String uniqueKey = DatabaseHelper.mDatabase.collection("reviews").document().getId();

        RatingsModel model = new RatingsModel
                ("", OrderDetailsActivity.list.getReciever_id(),  OrderDetailsActivity.list.getSender_id(),
                        System.currentTimeMillis()+"", "", uniqueKey, "false");
        DatabaseHelper.mDatabase.collection("reviews").document(uniqueKey).
                set(model);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
}