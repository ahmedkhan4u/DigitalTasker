package com.softrasol.ahmedgulsaqib.digitaltasker.Activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Adapters.SearchUserAdapter;
import com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Models.UserDataModel;
import com.softrasol.ahmedgulsaqib.digitaltasker.R;

import java.util.ArrayList;
import java.util.List;

public class SearchUserActivity extends AppCompatActivity {

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private CollectionReference reference = firestore.collection("users");
    Query q;
    EditText mTxtUser;

    RecyclerView mRecycerView;
    SearchUserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);

        mTxtUser = findViewById(R.id.txt_search_user);

        q = firestore.collection("users");
        showAdapter(q);

        mTxtUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() == 0) {
                    q = firestore.collection("users");
                    showAdapter(q);
                } // This is used as if user erases the characters in the search field.
                else {
                    q = reference.orderBy("name").startAt(charSequence.toString().trim()).endAt(charSequence.toString().trim() + "\uf8ff"); // name - the field for which you want to make search
                    showAdapter(q);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });

    }

    void showAdapter(Query q1) {
        q1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<UserDataModel> names = new ArrayList<>();
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        UserDataModel model = document.toObject(UserDataModel.class);
                        names.add(model);
                    }
                    mRecycerView = findViewById(R.id.recyclerview_search);
                    mRecycerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    adapter = new SearchUserAdapter(SearchUserActivity.this, names);
                    mRecycerView.setAdapter(adapter);
                }
            }
        });


    }
}