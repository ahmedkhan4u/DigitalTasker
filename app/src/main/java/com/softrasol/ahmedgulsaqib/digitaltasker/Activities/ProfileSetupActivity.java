package com.softrasol.ahmedgulsaqib.digitaltasker.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.softrasol.ahmedgulsaqib.digitaltasker.R;

public class ProfileSetupActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView textView;
    private ImageButton mBtnBack;

    private Spinner mSpinner;
    private String [] categoryList = {"Choose Profession","Electrician", "Plumber", "Cleaner", "Delivery", "Handyman",
    "Carpenter","Labour", "Ac Repair"};

    //...............................................................................................
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);

        toolbarInflation();
        widgetInflation();
        fillCategoryList();


    }
    //...............................................................................................

    private void fillCategoryList() {

        ArrayAdapter categoryListAdapter = new ArrayAdapter(getApplicationContext(),
                R.layout.dropdown_profession_list_item, categoryList);
        mSpinner.setAdapter(categoryListAdapter);

    }

    private void widgetInflation(){
        mSpinner = findViewById(R.id.dropdown_menu_account_setup);
    }

    private void toolbarInflation() {
        toolbar = findViewById(R.id.toolbar_profile_setup);
        textView = toolbar.findViewById(R.id.toolbarText);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        textView.setText("Profile Setup");
        mBtnBack = toolbar.findViewById(R.id.btnBack);
        mBtnBack.setVisibility(View.GONE);
    }
}
