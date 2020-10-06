package com.softrasol.ahmedgulsaqib.digitaltasker.Activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.*
import android.widget.AdapterView.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Helper.DatabaseHelper
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Helper.Helper
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Helper.Notification
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Interfaces.ToastMessage
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Models.NotificationsModel
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Models.OrderModel
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Models.UserDataModel
import com.softrasol.ahmedgulsaqib.digitaltasker.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.send_offer_bottom_sheet.*
import java.util.*
import java.util.concurrent.TimeUnit

class ViewUserDetailsActivity : AppCompatActivity(), OnMapReadyCallback, ToastMessage {

    lateinit var mTxtCategory : TextView
    lateinit var mTxtName : TextView
    lateinit var mTxtEmail : TextView
    lateinit var mTxtAddress : TextView
    lateinit var mTxtDescriptin : TextView
    lateinit var mImgProfile : ImageView
    lateinit var mTxtPrice : TextView

    lateinit var mBtnSendOffer : Button

    lateinit var mMap : GoogleMap

    lateinit var userDetailsList : ArrayList<UserDataModel>

    lateinit var mUid : String
    lateinit var mName : String
    lateinit var mImgUrl : String

    lateinit var isRestrict : String

    var time = ""
    var timeInMillis = ""

    private val progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_user_details)

        mUid = intent.getStringExtra("uid")

        toolbarInflation()
        widgetsInflation()
        googleMapFragment()
        fetchDataFromFirestoreDatabase()

        //progressDialog = ProgressDialog(this)

    }

    @SuppressLint("ResourceAsColor")
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
                isRestrict = model!!.is_restrict

                if (isRestrict.equals("true")){
                    mBtnSendOffer.isEnabled = false
                    mBtnSendOffer.setBackgroundColor(R.color.colorEdtOutline)
                    Helper.shortToast(applicationContext, "User Is Restricted From Getting Orders Right Now");
                }else{
                    mBtnSendOffer.isEnabled = true
                }

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
        mTxtPrice = findViewById(R.id.txt_viewuser_detail_price)
        mBtnSendOffer = findViewById(R.id.btn_send_offer)

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

    fun SendOfferClick(view: View) {

        val dialog = BottomSheetDialog(this@ViewUserDetailsActivity)
        dialog.setContentView(R.layout.send_offer_bottom_sheet)

        val mSpinner = dialog.findViewById<Spinner>(R.id.spinner)
        val mTxtBudget =
            dialog.findViewById<TextInputEditText>(R.id.txt_sendoffer_budget)
        val mTxtDescription =
            dialog.findViewById<TextInputEditText>(R.id.txt_sendoffer_desc)
        val mBtnSendOffer =
            dialog.findViewById<Button>(R.id.btn_send_offer)
        val mBtnCancel =
            dialog.findViewById<Button>(R.id.btn_cancel_offer)


        val adapter: ArrayAdapter<*> = ArrayAdapter<Any?>(
            baseContext,
            android.R.layout.simple_spinner_dropdown_item,
            Helper.getTimeList() as List<Any?>
        )

        mSpinner!!.adapter = adapter

        mSpinner!!.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                time =
                    Helper.getTimeList()[position].substring(0, 1)
                timeInMillis =
                    TimeUnit.HOURS.toMillis(time.toLong()).toString() + ""
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        mBtnSendOffer!!.setOnClickListener {
            if (mTxtBudget != null) {
                if (mTxtDescription != null) {
                    validateFields(dialog, mTxtBudget, mTxtDescription)
                }
            }
        }

        mBtnCancel!!.setOnClickListener { dialog.cancel() }

        dialog.show()

    }

    private fun validateFields(
        dialog: BottomSheetDialog,
        mTxtBudget: TextInputEditText,
        mTxtDescription: TextInputEditText
    ) {
        val budget = mTxtBudget.text.toString().trim { it <= ' ' }
        val description = mTxtDescription.text.toString().trim { it <= ' ' }
        if (budget.isEmpty()) {
            mTxtBudget.error = "Required"
            mTxtBudget.requestFocus()
            return
        }
        if (description.isEmpty()) {
            mTxtDescription.error = "Required"
            mTxtDescription.requestFocus()
            return
        }
        saveDataToFirestoreDatabase(dialog, budget, description)
    }

    private fun saveDataToFirestoreDatabase(
        dialog: BottomSheetDialog,
        budget: String,
        description: String
    ) {
        progressDialog?.setTitle("Wait...")
        progressDialog?.setCancelable(false)
        progressDialog?.show()
        val uniqueId =
            DatabaseHelper.mDatabase.collection("orders").document().id
        val model = OrderModel(
            timeInMillis,
            budget,
            description,
            DatabaseHelper.Uid,
            mUid,
            "",
            uniqueId,
            System.currentTimeMillis().toString() + "",
            "true",
            "Pending"
        )
        DatabaseHelper.mDatabase.collection("orders").document(uniqueId)
            .set(model).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Helper.shortToast(
                        baseContext,
                        "Request Sent"
                    )
                    //sentRequestSavedToFirestore()
                    dialog.cancel()
                    progressDialog?.cancel()
                } else {
                    Helper.shortToast(
                        baseContext,
                        task.exception!!.message
                    )
                    progressDialog?.cancel()
                }
            }
    }

}
