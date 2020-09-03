package com.softrasol.ahmedgulsaqib.digitaltasker.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Adapters.TabsAccessorAdapter;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Fragments.ChatsFragment;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Fragments.HomeFragment;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Fragments.MoreFragment;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Fragments.NotificatinsFragment;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Interfaces.ToastMessage;
import com.softrasol.ahmedgulsaqib.digitaltasker.R;

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

        mViewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                page.setRotationY(position * -50);
            }
        });

    }


    //...............................................................................................

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

}
