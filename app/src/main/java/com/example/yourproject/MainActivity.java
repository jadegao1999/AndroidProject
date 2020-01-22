package com.example.yourproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "tag";
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    private FirebaseFirestore db; // Firebase realtime DB ref

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = FirebaseFirestore.getInstance();
    }

    /** Called when the user taps the Send button */
    public void sendMessage(View view) {
        final Intent intent = new Intent(this, TableActivity.class);
        EditText editTextFirstName = (EditText) findViewById(R.id.first_name);
        EditText editTextLastName = (EditText) findViewById(R.id.last_name);
        EditText editTextLocation = (EditText) findViewById(R.id.location);

        final String firstName = editTextFirstName.getText().toString();
        final String lastName = editTextLastName.getText().toString();
        final String location = editTextLocation.getText().toString();

        // Use callbacks to handle async api connections.
        getNextUserId(new FirestoreCallback() {
            @Override
            public void onCallback(int nextId) {
                addNewUser(nextId, firstName, lastName, location, new FirestoreCallback() {
                    @Override
                    public void onCallback(int nextId) {
                        startActivity(intent);
                    }
                });
                updateUserCount(nextId);
            }
        });
    }

    /** Called when the user taps the All users button */
    public void goToTableView(View view) {
        Intent intent = new Intent(this, TableActivity.class);
        startActivity(intent);
    }

    /** Add a new user to Firestore DB. */
    public void addNewUser(int id, String firstName, String lastName, String location, final FirestoreCallback callback) {
        // Create a new user with id, first, last name and location.
        Map<String, Object> user = new HashMap<>();

        String newId = String.valueOf(id);
        user.put("id", newId);
        user.put("firstName", firstName);
        user.put("lastName", lastName);
        user.put("location", location);

        // Add a new document with a generated ID
        db.collection("user_info")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        callback.onCallback(0 /* random number, no use */);
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    /** Get the total users count and generate id for next user. */
    public void getNextUserId(final FirestoreCallback callback) {
        DocumentReference docRef = db.collection("user_count").document("0");

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {
                        callback.onCallback(Integer.parseInt(document.getString("count")) + 1);
                        Log.d(TAG, "nextId = " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    /** Update the total user count in Firestore DB. */
    public void updateUserCount(int nextId) {
        DocumentReference docRef = db.collection("user_count").document("0");

        docRef.update("count", String.valueOf(nextId))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
    }

    /** Interface to handle async functions. */
    private interface FirestoreCallback {
        void onCallback(int nextId);
    }
}
