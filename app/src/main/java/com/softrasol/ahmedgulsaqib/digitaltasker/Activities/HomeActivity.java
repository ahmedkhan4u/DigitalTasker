package com.softrasol.ahmedgulsaqib.digitaltasker.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Adapters.TabsAccessorAdapter;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.BackgroundService.MyService;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.BackgroundService.Restarter;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Fragments.ChatsFragment;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Fragments.HomeFragment;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Fragments.MoreFragment;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Fragments.NotificatinsFragment;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Helper.DatabaseHelper;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Helper.Helper;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Helper.UserStatus;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Interfaces.ToastMessage;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Models.WorkRequestModel;
import com.softrasol.ahmedgulsaqib.digitaltasker.R;

import java.util.Calendar;

public class HomeActivity extends AppCompatActivity implements ToastMessage {

    private int[] tabIcons = {
            R.drawable.ic_home,
            R.drawable.ic_notification,
            R.drawable.ic_chat,
            R.drawable.ic_more
    };

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private Toolbar toolbar;
    private TextView textView;
    private ImageButton mBtnBack;
    private String unselectedColor = "#ffffff";
    private String selectedColor = "#D6D6D6";
    private FloatingActionButton mFab;

    private Intent mServiceIntent;
    private MyService mMyService;

    String date_time = "";
    int mYear;
    int mMonth;
    int mDay;

    int mHour;
    int mMinute;

    //................................................................................................
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        checkUserAuthenticationToken();
        toolbarInflation();
        widgetInflation();
        tabLayout();
        changeTabBarIconColors();
        floatingActionButtonClick();
        statusCheck();

        //startService();

        mViewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                page.setRotationY(position * -50);
            }
        });
     //...............................................................................................
    }

    private void startService() {
        mMyService = new MyService();
        mServiceIntent = new Intent(this, mMyService.getClass());
//        if (!isMyServiceRunning(mMyService.getClass())) {
//            startService(mServiceIntent);
//        }
    }

    private void floatingActionButtonClick() {
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //postWorkRequest();
                startActivity(new Intent(getApplicationContext(), PostWorkRequestActivity.class));

            }
        });
    }

    private void tabLayout() {
        TabsAccessorAdapter tabsAccessorAdapter = new TabsAccessorAdapter(getSupportFragmentManager());
        tabsAccessorAdapter.setFragment(new HomeFragment(), "");
        tabsAccessorAdapter.setFragment(new NotificatinsFragment(), "");
        tabsAccessorAdapter.setFragment(new ChatsFragment(), "");
        tabsAccessorAdapter.setFragment(new MoreFragment(), "");
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setAdapter(tabsAccessorAdapter);
        setupTabIcons();
        changeToolBarTextOnViewPageChanged();
    }

    private void changeTabBarIconColors() {
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.parseColor(selectedColor), PorterDuff.Mode.SRC_IN);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.parseColor(unselectedColor), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void changeToolBarTextOnViewPageChanged() {
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        textView.setText("Home");
                        break;

                    case 1:
                        textView.setText("Notifications");
                        break;

                    case 2:
                        textView.setText("Chats");
                        break;

                    case 3:
                        textView.setText("Menu");
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void widgetInflation() {
        mTabLayout = findViewById(R.id.tabLayout);
        mViewPager = findViewById(R.id.viewPager);
        mFab = findViewById(R.id.fab);
    }

    private void setupTabIcons() {
        mTabLayout.getTabAt(0).setIcon(tabIcons[0]);
        mTabLayout.getTabAt(1).setIcon(tabIcons[1]);
        mTabLayout.getTabAt(2).setIcon(tabIcons[2]);
        mTabLayout.getTabAt(3).setIcon(tabIcons[3]);

        mTabLayout.getTabAt(0).getIcon().setColorFilter(Color.parseColor(selectedColor), PorterDuff.Mode.SRC_IN);
        mTabLayout.getTabAt(1).getIcon().setColorFilter(Color.parseColor(unselectedColor), PorterDuff.Mode.SRC_IN);
        mTabLayout.getTabAt(2).getIcon().setColorFilter(Color.parseColor(unselectedColor), PorterDuff.Mode.SRC_IN);
        mTabLayout.getTabAt(3).getIcon().setColorFilter(Color.parseColor(unselectedColor), PorterDuff.Mode.SRC_IN);

    }

    private void toolbarInflation() {
        toolbar = findViewById(R.id.toolbar);
        textView = toolbar.findViewById(R.id.toolbarText);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        textView.setText("Home");
        mBtnBack = toolbar.findViewById(R.id.btnBack);
        mBtnBack.setVisibility(View.GONE);
    }

    private void checkUserAuthenticationToken() {


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            startActivity(new Intent(getApplicationContext(), PhoneAuthActivity.class));
            finish();
            return;
        }

            checkIfUserDataExistsOrNot();

    }

    private void checkIfUserDataExistsOrNot() {
        CollectionReference mRef = FirebaseFirestore.getInstance()
                .collection("users");
        Query query = mRef.whereEqualTo("uid", FirebaseAuth.getInstance().getUid());

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().size() > 0) {
                        for (QueryDocumentSnapshot snapshot : task.getResult()) {
                            if (!snapshot.contains("name")) {
                                startActivity(new Intent
                                        (getApplicationContext(), ProfileSetupActivity.class));
                                finish();
                                return;
                            }else {

                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        UserStatus.saveUserStatus("online");
    }

    @Override
    protected void onStop() {
        super.onStop();
        UserStatus.saveUserStatus("offline");

    }

    @Override
    public void onBackPressed() {
        exitAppDialog();
    }

    private void exitAppDialog() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(HomeActivity.this);
        builder1.setTitle("Alert!");
        builder1.setMessage("Are you sure you want to exit.");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        HomeActivity.this.finish();
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

//    private boolean isMyServiceRunning(Class<?> serviceClass) {
//        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
//            if (serviceClass.getName().equals(service.service.getClassName())) {
//                Log.d("Service status", "Running");
//                return true;
//            }
//        }
//        Log.d ("Service status", "Not running");
//        return false;
//    }


    @Override
    protected void onDestroy() {
        //stopService(mServiceIntent);
//        Intent broadcastIntent = new Intent();
//        broadcastIntent.setAction("restartservice");
//        broadcastIntent.setClass(this, Restarter.class);
//        this.sendBroadcast(broadcastIntent);
        super.onDestroy();
    }


    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}
