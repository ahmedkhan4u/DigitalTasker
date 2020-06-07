package com.softrasol.ahmedgulsaqib.digitaltasker.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Models.ChatModel
import com.softrasol.ahmedgulsaqib.digitaltasker.R
import java.util.*

class ChatActivity : AppCompatActivity() {


    lateinit var mEdtMessage: EditText
    lateinit var mBtnSend : ImageButton
    lateinit var mRecylcerView : RecyclerView

    lateinit var mRecieverUid : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        mRecieverUid = intent.getStringExtra("reciever_uid");


        widgetsInflation()
        btnSendMessageClick()


    }

    private fun btnSendMessageClick() {

        mBtnSend.setOnClickListener(View.OnClickListener {
            view ->
            var message = mEdtMessage.text.trim().toString()

            if (message.isEmpty()){
                return@OnClickListener
            }

            var collectionReference = FirebaseFirestore.getInstance().collection("chats")
            var uniqueKey = collectionReference.document().id
            var documentReference = collectionReference.document(uniqueKey)

            var date_time = Date()
            var dateTime = date_time.toLocaleString()

            var model = ChatModel(FirebaseAuth.getInstance().uid, mRecieverUid, message,
                "false", uniqueKey, dateTime
            );

            documentReference.set(model).addOnCompleteListener(OnCompleteListener {
                    task ->

                if (task.isSuccessful){
                    Toast.makeText(applicationContext, "Message Sent", Toast.LENGTH_LONG).show()
                    mEdtMessage.text = null
                }
            })

        })

    }

    private fun widgetsInflation() {
        mEdtMessage = findViewById(R.id.edt_type_message)
        mBtnSend = findViewById(R.id.btn_send_message)
        mRecylcerView = findViewById(R.id.recyclerview_messages)
    }
}
