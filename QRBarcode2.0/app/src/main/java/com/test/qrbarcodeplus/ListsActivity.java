package com.test.qrbarcodeplus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ListsActivity extends AppCompatActivity {

    ImageButton btnScan;
    ImageButton btnQR;
    ImageButton btnSettings;
    final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);

        btnScan = findViewById(R.id.imageButton5);
        btnQR = findViewById(R.id.imageButton10);
        btnSettings = findViewById(R.id.imageButton6);

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentScan = new Intent(ListsActivity.this, HomeActivity.class);
                startActivity(intentScan);
            }
        });

        btnQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentQR = new Intent(ListsActivity.this, qrActivity.class);
                startActivity(intentQR);
            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLists = new Intent(ListsActivity.this, SettingsActivity.class);
                startActivity(intentLists);
            }
        });







    }
}
