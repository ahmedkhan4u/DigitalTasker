package com.softrasol.ahmedgulsaqib.digitaltasker.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.softrasol.ahmedgulsaqib.digitaltasker.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    Thread.sleep(500);

                }catch (Exception ex){
                    Log.d("dxdiag", ex.getMessage());
                }finally {
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    finish();
                }
            }
        }).start();

    }
}
