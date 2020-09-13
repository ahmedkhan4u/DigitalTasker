package com.softrasol.ahmedgulsaqib.digitaltasker.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Adapters.ChatAdapter;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Helper.DatabaseHelper;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Helper.Helper;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Models.ChatModel;
import com.softrasol.ahmedgulsaqib.digitaltasker.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatsActivity extends AppCompatActivity {

    private TextView mEdtMessage;
    private ImageButton mBtnSend;
    private CircleImageView mImgProfile;
    private TextView mTxtName;
    private RecyclerView mRecyclerView;
    private String mReceiverUid, mName, mImgUrl;
    private List<ChatModel> list;
    private String message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        widgetsInflation();
        getIntentsData();
        btnSendMessageClick();
        getAllMsgFromFirestoreDb();

    }

    private void getIntentsData() {
        mReceiverUid = getIntent().getStringExtra("reciever_uid");
        mName = getIntent().getStringExtra("name");
        mImgUrl = getIntent().getStringExtra("image_url");

        Picasso.get().load(mImgUrl).resize(50,50).into(mImgProfile);
        mTxtName.setText(mName);

    }

    private void btnSendMessageClick() {

        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMessageInFirebaseFirestore();
            }
        });

    }

    private void saveMessageInFirebaseFirestore() {

        message = mEdtMessage.getText().toString().trim();
        String date_time = System.currentTimeMillis()+"";

        if (message.isEmpty()){
            return;
        }

        String uniqueKey = DatabaseHelper.mDatabase.collection("chats").document().getId();

        ChatModel chatModel = new ChatModel(DatabaseHelper.Uid, mReceiverUid,message,"false",uniqueKey,date_time);

        DatabaseHelper.mDatabase.collection("chats").document(uniqueKey)
                .set(chatModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Helper.shortToast(getApplicationContext(), "Message Sent");
                    addUserToChatList();
                    mEdtMessage.setText(null);
                }else {
                    Helper.logMessage(task.getException().getMessage());
                }
            }
        });


    }

    private void addUserToChatList() {
        final Map map = new HashMap();
        map.put("id", mReceiverUid);

        final CollectionReference collectionReference = FirebaseFirestore.getInstance()
                .collection("chat_list")
                .document(DatabaseHelper.Uid)
                .collection("chat_list");

        collectionReference.document(mReceiverUid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful() && !task.getResult().exists()){
                    collectionReference.document(mReceiverUid).set(map).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()){
                                Helper.logMessage("Chat List Created");
                            }else {
                                Helper.logMessage(task.getException().getMessage());
                            }
                        }
                    });
                }
            }
        });
    }

    private  void widgetsInflation() {
        mEdtMessage = findViewById(R.id.edt_type_message);
        mBtnSend = findViewById(R.id.btn_send_message);
        mRecyclerView = findViewById(R.id.recyclerview_messages);
        mImgProfile = findViewById(R.id.img_chat_profile);
        mTxtName = findViewById(R.id.txt_chat_person_name);
    }

    private void getAllMsgFromFirestoreDb() {

        list =  new ArrayList<>();

        DatabaseHelper.mDatabase.collection("chats").orderBy("date_time",
                Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (!list.isEmpty()){
                    list.clear();
                }

                if ( e != null){
                    Helper.logMessage(" Snapshot Error "+e.getMessage());
                    return;
                }

                if (!queryDocumentSnapshots.isEmpty()){
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){

                        ChatModel chatModel = snapshot.toObject(ChatModel.class);

                        if (chatModel.getReciever_id().equals(DatabaseHelper.Uid) && chatModel.getSender_id().equals(mReceiverUid)
                                || chatModel.getReciever_id().equals(mReceiverUid) && chatModel.getSender_id().equals(DatabaseHelper.Uid))
                        {
                            list.add(chatModel);
                        }

                    }

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                    mRecyclerView.hasFixedSize();
                    linearLayoutManager.setStackFromEnd(true);
                    //linearLayoutManager.setReverseLayout(true);
                    mRecyclerView.setLayoutManager(linearLayoutManager);
                    ChatAdapter adapter = new ChatAdapter(getApplicationContext(), list, mImgUrl);
                    mRecyclerView.setAdapter(adapter);
                }
            }
        });
    }

    public void BackClick(View view) {
        onBackPressed();
    }
}