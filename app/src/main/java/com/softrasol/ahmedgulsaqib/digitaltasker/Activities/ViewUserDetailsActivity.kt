package com.softrasol.ahmedgulsaqib.digitaltasker.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Interfaces.ToastMessage
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Models.UserDataModel
import com.softrasol.ahmedgulsaqib.digitaltasker.R

class ViewUserDetailsActivity : AppCompatActivity(), OnMapReadyCallback, ToastMessage {

    lateinit var mTxtCategory : TextView
    lateinit var mTxtName : TextView
    lateinit var mTxtEmail : TextView
    lateinit var mTxtAddress : TextView

    lateinit var mMap : GoogleMap

    lateinit var userDetailsList : ArrayList<UserDataModel>

    lateinit var mUid : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_user_details)

        mUid = intent.getStringExtra("uid")

        toolbarInflation()
        widgetsInflation()
        googleMapFragment()
        fetchDataFromFirestoreDatabase()


    }

    private fun fetchDataFromFirestoreDatabase() {

        val collectionReference = FirebaseFirestore.getInstance().collection("users")
        val documentReference = collectionReference.document(mUid);

        documentReference.get().addOnSuccessListener {document ->

            if (document != null){
                val model = document.toObject(UserDataModel::class.java)
                mTxtName.text = model?.name
                mTxtAddress.text = model?.address
                mTxtCategory.text = model?.category
                mTxtEmail.text = model?.email

                showTheWorkerLocationOnMap(model?.lat, model?.lng, model?.address)
            }

        }. addOnFailureListener { exception ->
            showToast(exception.message)
        }


    }

    private fun googleMapFragment() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map_viewuser_details) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun widgetsInflation() {
        mTxtCategory = findViewById(R.id.txt_viewuser_detail_category)
        mTxtName = findViewById(R.id.txt_viewuser_detail_name)
        mTxtEmail = findViewById(R.id.txt_viewuser_detail_email)
        mTxtAddress = findViewById(R.id.txt_viewuser_detail_address)

    }

    private fun toolbarInflation() {

        val mToolbar = findViewById(R.id.toolbar_view_user_details) as androidx.appcompat.widget.Toolbar
        val mBtnBack = mToolbar.findViewById(R.id.btnBack) as ImageButton
        val mToolbarText = mToolbar.findViewById(R.id.toolbarText) as TextView
        val actionBar = supportActionBar
        mToolbarText.text = "Worker Details"
        mBtnBack.setOnClickListener(){
            finish()
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isMyLocationButtonEnabled = true
        val kohatLatLng = LatLng(33.5612824, 71.3974918)
        val location = CameraUpdateFactory.newLatLngZoom(kohatLatLng, 10f)
        mMap.animateCamera(location)
        mMap.isMyLocationEnabled = true

    }

    private fun showTheWorkerLocationOnMap(lat: String?, lng: String?, address: String?) {

            val mLocation = LatLng(lat!!.toDouble(), lng!!.toDouble())
            mMap.addMarker(MarkerOptions().position(mLocation).title(address))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(mLocation))
    }

    override fun showToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_LONG)//To change body of created functions use File | Settings | File Templates.
    }

}
