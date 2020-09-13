package com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Helper.DatabaseHelper;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Helper.Helper;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Models.ChatListModel;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Models.ChatModel;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Models.UserDataModel;
import com.softrasol.ahmedgulsaqib.digitaltasker.R;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatsFragment extends Fragment {


    public ChatsFragment() {
        // Required empty public constructor
    }
    private View mView;
    private RecyclerView mRecyclerView;
    private List<ChatListModel> chatList = new ArrayList<>();
    private List<UserDataModel> userList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_chats, container, false);

        mRecyclerView = mView.findViewById(R.id.recyclerview_chat_users);

        getAllUsersWithWhomChatFromFirestore();

        return mView;
    }

    private void getAllUsersWithWhomChatFromFirestore() {

        final CollectionReference collectionReference = FirebaseFirestore.getInstance()
                .collection("chat_list")
                .document(DatabaseHelper.Uid)
                .collection("chat_list");

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if ( e!= null){
                    Helper.logMessage(e.getMessage());
                    return;
                }

                if (!queryDocumentSnapshots.isEmpty()){

                    for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots){

                        ChatListModel model = snapshot.toObject(ChatListModel.class);
                        chatList.add(model);

                    }

                    getUserListFromFirestore();

                }

            }
        });

    }

    private void getUserListFromFirestore() {

        DatabaseHelper.mDatabase.collection("users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null){
                    Helper.logMessage(e.getMessage());
                    return;
                }

                if (!queryDocumentSnapshots.isEmpty()){
                    for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots){
                        UserDataModel userModel = snapshot.toObject(UserDataModel.class);
                            for (ChatListModel chatModel : chatList){
                                if (userModel.getUid().equals(chatModel.getId())){
                                    userList.add(userModel);
                                    Helper.logMessage(userModel.getName());
                                }
                            }
                        }
                    }
                }
        });
    }
}
