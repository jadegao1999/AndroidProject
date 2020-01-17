package com.example.yourproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /** Called when the user taps the Send button */
    public void sendMessage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editTextFirstName = (EditText) findViewById(R.id.first_name);
        EditText editTextLastName = (EditText) findViewById(R.id.last_name);
        // TODO: Add location from phone GPS.

        String firstName = editTextFirstName.getText().toString();
        String lastName = editTextLastName.getText().toString();

        // TODO: Make connection to backend and store the data.

        startActivity(intent);
    }
}
