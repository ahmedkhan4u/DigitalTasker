package com.softrasol.ahmedgulsaqib.digitaltasker.Activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.firestore.FirebaseFirestore
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Helper.DatabaseHelper
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Helper.Notification
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Interfaces.ToastMessage
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Models.NotificationsModel
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Models.UserDataModel
import com.softrasol.ahmedgulsaqib.digitaltasker.R
import com.squareup.picasso.Picasso

class ViewUserDetailsActivity : AppCompatActivity(), OnMapReadyCallback, ToastMessage {

    lateinit var mTxtCategory : TextView
    lateinit var mTxtName : TextView
    lateinit var mTxtEmail : TextView
    lateinit var mTxtAddress : TextView
    lateinit var mTxtDescriptin : TextView
    lateinit var mImgProfile : ImageView
    lateinit var mTxtPrice : TextView

    lateinit var mMap : GoogleMap

    lateinit var userDetailsList : ArrayList<UserDataModel>

    lateinit var mUid : String
    lateinit var mName : String
    lateinit var mImgUrl : String

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
                mTxtDescriptin.text = model?.description
                mTxtPrice.text = model?.price

                mImgUrl = model!!.profile_img

                Picasso.get().load(model?.profile_img).resize(400,400)
                    .placeholder(R.drawable.image_profile).into(mImgProfile)

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
        mTxtDescriptin = findViewById(R.id.txt_viewuser_detail_description)
        mImgProfile = findViewById(R.id.img_viewuser_details_profile)
        mTxtPrice = findViewById(R.id.txt_viewuser_detail_price);

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
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return
        }
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

    fun ContactSellerClick(view: View) {

        val intent =  Intent(this@ViewUserDetailsActivity, ChatsActivity::class.java);
        intent.putExtra("reciever_uid", mUid)
        intent.putExtra("name",mTxtName.text)
        intent.putExtra("image_url",mImgUrl)

        startActivity(intent)

    }

    fun PostComplaintClick(view: View) {

        var bottomSheetDialog = BottomSheetDialog(this@ViewUserDetailsActivity)
        bottomSheetDialog.setContentView(R.layout.complaints_bottomsheet_dialog);

        var mTxtName : TextView
        var mTxtMessage : TextView

        var mBtnCancel : Button
        var mBtnPost : Button

        mTxtName = bottomSheetDialog.findViewById(R.id.complait_title)!!
        mTxtMessage = bottomSheetDialog.findViewById(R.id.complaint_message)!!

        mBtnCancel = bottomSheetDialog.findViewById(R.id.btn_cancel_complaint)!!
        mBtnPost = bottomSheetDialog.findViewById(R.id.btn_post_complaint)!!

        mBtnCancel.setOnClickListener(View.OnClickListener { v ->
            bottomSheetDialog.cancel()
        })

        mBtnPost.setOnClickListener(View.OnClickListener { v ->

            var title = mTxtName.text;
            var message = mTxtMessage.text

            if (title.isEmpty()){
                mTxtName.setError("Required")
                mTxtName.requestFocus()
                return@OnClickListener
            }

            if (message.isEmpty()){
                mTxtMessage.setError("Required")
                mTxtMessage.requestFocus()
                return@OnClickListener
            }

            var uniqueKey = DatabaseHelper.mDatabase.collection("complaints").document().id

            var model = NotificationsModel(title.toString(), message.toString(),
                System.currentTimeMillis().toString(), DatabaseHelper.Uid, mUid.toString(),
                "false", "complaint", uniqueKey)

            Notification.postComplaint(this@ViewUserDetailsActivity, model)

            if (Notification.status == true){
                bottomSheetDialog.cancel();
            }

        })

        bottomSheetDialog.show()

    }

    fun ReviewsClick(view: View) {

        val intent =  Intent(this@ViewUserDetailsActivity, ReviewsActivity::class.java);
        intent.putExtra("uid", mUid)
        startActivity(intent)

    }
}
