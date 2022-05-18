package com.example.crudproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class Read extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        Bundle extras = getIntent().getExtras();

        if (extras != null){
            String values = extras.getString("key");
            TextView txtView = findViewById(R.id.IDTxt);
            txtView.setText(values);
        }

        String response = getString(R.string.readResponseJSON);
    }
}