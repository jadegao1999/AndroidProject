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
    public int nextId;

    private FirebaseFirestore db; // Firebase realtime DB ref

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = FirebaseFirestore.getInstance();
    }

    /** Called when the user taps the Send button */
    public void sendMessage(View view) {
        Intent intent = new Intent(this, TableActivity.class);
        EditText editTextFirstName = (EditText) findViewById(R.id.first_name);
        EditText editTextLastName = (EditText) findViewById(R.id.last_name);
        EditText editTextLocation = (EditText) findViewById(R.id.location);
        // TODO: Add location from phone GPS.

        String firstName = editTextFirstName.getText().toString();
        String lastName = editTextLastName.getText().toString();
        String location = editTextLocation.getText().toString();

        // TODO: Make connection to backend and store the data.
        addNewUser(firstName, lastName, location);

        startActivity(intent);
    }

    /** Called when the user taps the All users button */
    public void goToTableView(View view) {
        Intent intent = new Intent(this, TableActivity.class);
        startActivity(intent);
    }

    public void addNewUser(String firstName, String lastName, String location) {
        // Create a new user with id, first, last name and location.
        Map<String, Object> user = new HashMap<>();
        DocumentReference docRef = db.collection("user_count").document("0");

        // Fetch nextId from DB.
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {
                        nextId = Integer.parseInt(document.getString("count")) + 1;
                        Log.d(TAG, "nextId = " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        // Update user count in DB.
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

        String newId = String.valueOf(nextId);
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

    public String getNextUserId() {
        DocumentReference docRef = db.collection("user_count").document("0");

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {
                        nextId = Integer.parseInt(document.getString("count")) + 1;
                        Log.d(TAG, "nextId = " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
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
        return String.valueOf(this.nextId);
    }
}
