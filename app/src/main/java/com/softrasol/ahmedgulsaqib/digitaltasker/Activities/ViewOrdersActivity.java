package com.softrasol.ahmedgulsaqib.digitaltasker.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Adapters.TabsAccessorAdapter;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Fragments.CancelledOrdersFragment;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Fragments.CompletedOrdersFragment;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Fragments.PendingOrdersFragment;
import com.softrasol.ahmedgulsaqib.digitaltasker.R;

public class ViewOrdersActivity extends AppCompatActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_orders);

        toolbarInflation();

        mTabLayout = findViewById(R.id.tab_layout);
        mViewPager = findViewById(R.id.view_pager);

        TabsAccessorAdapter adapter = new TabsAccessorAdapter(getSupportFragmentManager());
        adapter.setFragment(new PendingOrdersFragment(), "Pending");
        adapter.setFragment(new CompletedOrdersFragment(), "Completed");
        adapter.setFragment(new CancelledOrdersFragment(), "Cancelled");

        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setAdapter(adapter);


    }


    private void toolbarInflation() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView textView = toolbar.findViewById(R.id.toolbarText);
        textView.setText("Work Requests");
        ImageButton mBtnBack = toolbar.findViewById(R.id.btnBack);
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}