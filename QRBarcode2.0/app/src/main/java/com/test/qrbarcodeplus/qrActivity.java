package com.test.qrbarcodeplus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class qrActivity extends AppCompatActivity {

    ImageButton btnScan;
//    ImageButton btnLists;
    ImageButton btnSettings;
    ImageView qrCode;
    EditText urlLink;
    Button createButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        btnScan = findViewById(R.id.imageButton5);
//        btnLists = findViewById(R.id.imageButton7);
        btnSettings = findViewById(R.id.imageButton6);
        qrCode = findViewById(R.id.qr);
        urlLink = findViewById(R.id.url);
        createButton = findViewById(R.id.create);


        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentScan = new Intent(qrActivity.this, HomeActivity.class);
                startActivity(intentScan);
            }
        });
/*
        btnLists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLists = new Intent(qrActivity.this, ListsActivity.class);
                startActivity(intentLists);
            }
        });
*/
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLists = new Intent(qrActivity.this, SettingsActivity.class);
                startActivity(intentLists);
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String qrURL = "";

                if(urlLink.getText() != null) {

                    qrURL = urlLink.getText().toString();
                }
                else{
                    qrURL = "www.google.com";
                }

                int width = 612;
                int height = 612;

                QRCodeWriter writer = new QRCodeWriter();

                try {
                    BitMatrix bitMatrix = writer.encode(qrURL, BarcodeFormat.QR_CODE, width, height);

                    Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

                    for (int x = 0; x < width; x++) {
                        for (int y = 0; y < height; y++) {

                            bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                        }
                    }
                    qrCode.setImageBitmap(bmp);

                } catch (WriterException e) {
                    // Handle exception
                }
            }
        });


    }
}
