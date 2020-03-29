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
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Fragments.ChatsFragment;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Fragments.HomeFragment;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Fragments.MoreFragment;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Fragments.NotificatinsFragment;
import com.softrasol.ahmedgulsaqib.digitaltasker.R;

public class HomeActivity extends AppCompatActivity {

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

    //...............................................................................................
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbarInflation();
        widgetInflation();
        tabLayout();

    }

    //...............................................................................................

    private void tabLayout() {
        TabsAccessorAdapter tabsAccessorAdapter = new TabsAccessorAdapter(getSupportFragmentManager());
        tabsAccessorAdapter.setFragment(new HomeFragment(),"");
        tabsAccessorAdapter.setFragment(new NotificatinsFragment(), "");
        tabsAccessorAdapter.setFragment(new ChatsFragment(), "");
        tabsAccessorAdapter.setFragment(new MoreFragment(), "");
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setAdapter(tabsAccessorAdapter);
        setupTabIcons();
        changeToolBarTextOnViewPageChanged();
    }

    private void changeToolBarTextOnViewPageChanged() {
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
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
        mTabLayout = findViewById(R.id.tab_layout);
        mViewPager = findViewById(R.id.view_pager);
    }

    private void setupTabIcons() {
        mTabLayout.getTabAt(0).setIcon(tabIcons[0]);
        mTabLayout.getTabAt(1).setIcon(tabIcons[1]);
        mTabLayout.getTabAt(2).setIcon(tabIcons[2]);
        mTabLayout.getTabAt(3).setIcon(tabIcons[3]);
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

}
