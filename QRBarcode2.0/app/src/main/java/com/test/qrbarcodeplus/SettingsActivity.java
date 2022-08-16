package com.test.qrbarcodeplus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivity extends AppCompatActivity {

    Button btnSignOut;
    ImageButton btnScan;
    ImageButton btnQR;
//    ImageButton btnLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        btnSignOut = findViewById(R.id.button2);
        btnScan = findViewById(R.id.imageButton5);
        btnQR = findViewById(R.id.imageButton10);
//        btnLists = findViewById(R.id.imageButton7);

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentScan = new Intent(SettingsActivity.this, HomeActivity.class);
                startActivity(intentScan);
            }
        });

        btnQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentQR = new Intent(SettingsActivity.this, qrActivity.class);
                startActivity(intentQR);
            }
        });
/*
        btnLists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLists = new Intent(SettingsActivity.this, ListsActivity.class);
                startActivity(intentLists);
            }
        });
*/
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(SettingsActivity.this, "Signed Out.", Toast.LENGTH_SHORT).show();
                Intent intentSignOut = new Intent(SettingsActivity.this, SignInActivity.class);
                startActivity(intentSignOut);
            }
        });

    }
}
