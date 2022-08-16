package com.test.qrbarcodeplus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class SocialActivity extends AppCompatActivity {

    ImageButton btnScan;
    ImageButton btnQR;
    ImageButton btnLists;
    ImageButton btnSettings;
    WebView b;//browser
//use no keywords for anything webview related

    private String storeN = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social);

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        btnScan = findViewById(R.id.imageButton5);
        btnQR = findViewById(R.id.imageButton10);
        btnLists = findViewById(R.id.imageButton7);
        btnSettings = findViewById(R.id.imageButton6);
        b = findViewById(R.id.browserV);


        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentScan = new Intent(SocialActivity.this, HomeActivity.class);
                startActivity(intentScan);
            }
        });

        btnQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentQR = new Intent(SocialActivity.this, qrActivity.class);
                startActivity(intentQR);
            }
        });

        btnLists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLists = new Intent(SocialActivity.this, ListsActivity.class);
                startActivity(intentLists);
            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLists = new Intent(SocialActivity.this, SettingsActivity.class);
                startActivity(intentLists);
            }
        });


        WebSettings set = b.getSettings();
        set.setBuiltInZoomControls(true);

        String url = "https://www.twitter.com/";
        APICalls();

        String brandURL = getStoreName();
        StringBuilder sb = new StringBuilder();
        sb.append(url);
        sb.append(brandURL);
        url = sb.toString();

        b.loadUrl(url);
    }

    void setStoreName( String sN ){
        //trim of spaces
        sN = sN.replaceAll("\\s", "");

        storeN = sN;
    }

    String getStoreName(){
        return storeN;
    }


    public void APICalls() {
        //Change key here if subscription ends
//        String key = "wimlozl4c5h31ri2enulgsw9r726nr";

        String key2 = "p72icim2v99a2kpim2j6r2sx1pkal6";

        String data = "";
        String str = "";

        barcodeSingleton barcodeObject = barcodeSingleton.getInstance();
        String bc = barcodeObject.getBarcodeValue();

        String barcodeURL = String.format("https://api.barcodelookup.com/v2/products?barcode=%s", bc);
        StringBuilder sb = new StringBuilder();
        sb.append(barcodeURL);
        sb.append("&formatted=y&key=");
//        sb.append(key);
        sb.append(key2);
        String finalURL = sb.toString();

        try {
            URL url = new URL(finalURL);

            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));

            while (null != (str= br.readLine())) {
                data+=str;
            }

            Gson g = new Gson();

            NutritionActivity.RootObject value = g.fromJson(data, NutritionActivity.RootObject.class);


            String sN = value.products[0].manufacturer;//.stores[0].store_name;

            // NEEDS TO GET TO HERE TO WORK
            setStoreName( sN );

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public class Store
    {
        public String store_name;
        public String store_price;
        public String product_url;
        public String currency_code;
        public String currency_symbol;
    }

    public class Review
    {
        public String name;
        public String rating;
        public String title;
        public String review;
        public String datetime;
    }

    public class Product
    {
        public String barcode_number;
        public String barcode_type;
        public String barcode_formats;
        public String mpn;
        public String model;
        public String asin;
        public String product_name;
        public String title;
        public String category;
        public String manufacturer;
        public String brand;
        public String label;
        public String author;
        public String publisher;
        public String artist;
        public String actor;
        public String director;
        public String studio;
        public String genre;
        public String audience_rating;
        public String ingredients;
        public String nutrition_facts;
        public String color;
        public String format;
        public String package_quantity;
        public String size;
        public String length;
        public String width;
        public String height;
        public String weight;
        public String release_date;
        public String description;
        public Object[] features;
        public String[] images;
        public Store[] stores;
        public Review[] reviews;
    }

}
