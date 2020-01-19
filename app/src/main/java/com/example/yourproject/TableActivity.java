package com.example.yourproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class TableActivity extends AppCompatActivity {

    private FirebaseFirestore db; // Firebase realtime DB ref
    private List<User> userList;
    private UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);
        db = FirebaseFirestore.getInstance();
        userAdapter = new UserAdapter(this, new ArrayList<User>());
        final ListView recordsView = (ListView) findViewById(R.id.users_view);
        recordsView.setAdapter(userAdapter);

        getAllUsers();
        recordsView.setSelection(userAdapter.getCount() - 1);
    }

    public void getAllUsers() {
        userList = new ArrayList<User>();
        db.collection("user_info")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                userAdapter.add(document.toObject(User.class));
                                Log.d("tag", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w("tag", "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}
