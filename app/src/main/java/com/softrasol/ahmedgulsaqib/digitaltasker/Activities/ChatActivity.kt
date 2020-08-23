package com.softrasol.ahmedgulsaqib.digitaltasker.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Adapters.ChatsAdapter
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Models.ChatModel
import com.softrasol.ahmedgulsaqib.digitaltasker.R
import java.util.*
import kotlin.collections.ArrayList

class ChatActivity : AppCompatActivity() {


    lateinit var mEdtMessage: EditText
    lateinit var mBtnSend : ImageButton
    lateinit var mRecylcerView : RecyclerView

    lateinit var mUserUid : String

    lateinit var list : ArrayList<ChatModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        mUserUid = intent.getStringExtra("reciever_uid");


        widgetsInflation()
        btnSendMessageClick()

        getAllMsgFromFirestoreDb()

    }

    private fun getAllMsgFromFirestoreDb() {


        val db = FirebaseFirestore.getInstance().collection("chats")

        db.addSnapshotListener { value, e ->

            list = ArrayList()

                if (e != null) {
                    Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show()
                    return@addSnapshotListener
                }

            Toast.makeText(applicationContext, value.toString(), Toast.LENGTH_LONG).show();


            if (value != null) {
                for (doc in value){


                    var dataModel = doc.toObject(ChatModel::class.java)

                    if (dataModel.reciever_id.equals(FirebaseAuth.getInstance().uid)
                        && dataModel.sender_id.equals(mUserUid)
                        || dataModel.reciever_id.equals(mUserUid)
                        && dataModel.sender_id.equals(FirebaseAuth.getInstance().uid)
                    ){
                        list.add(dataModel)


                    }
                }
            }


            var linearLayoutManager = LinearLayoutManager(this);
            linearLayoutManager.stackFromEnd = true
            linearLayoutManager.reverseLayout = true
            mRecylcerView.layoutManager = linearLayoutManager
            var adapter = ChatsAdapter(list, applicationContext);
            mRecylcerView.adapter = adapter

            }

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

            var model = ChatModel(FirebaseAuth.getInstance().uid, mUserUid, message,
                "false", uniqueKey, dateTime
            )

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
