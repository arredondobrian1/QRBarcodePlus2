package com.test.qrbarcodeplus;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.apache.commons.math3.util.Precision;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;


public class NutritionActivity extends AppCompatActivity {

    TextView cals;
    TextView tFat;
    TextView sod;
    TextView carbo;
    TextView sug;
    TextView prot;

    TextView fatDV;
    TextView sodDV;
    TextView carbsDV;
    TextView protDV;

    TextView ingredients;
    TextView productName;
    ImageButton btnScan;
    ImageButton btnQR;
    //ImageButton btnLists;
    ImageButton btnSettings;
    ImageView image;

    private String productNameAPI;
    private String ingredientsAPI;
    private String imageURLAPI;
    private String nutritionFactsAPI;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition);
//API error fix
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        ingredients = findViewById(R.id.textView13);
        btnScan = findViewById(R.id.imageButton5);
        btnQR = findViewById(R.id.imageButton10);
        //btnLists = findViewById(R.id.imageButton7);
        btnSettings = findViewById(R.id.imageButton6);

//        barcodeSingleton bo = barcodeSingleton.getInstance();
 //       String bc = bo.getBarcodeValue();

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentScan = new Intent(NutritionActivity.this, HomeActivity.class);
                startActivity(intentScan);
            }
        });

        btnQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentQR = new Intent(NutritionActivity.this, qrActivity.class);
                startActivity(intentQR);
            }
        });
/*
        btnLists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLists = new Intent(NutritionActivity.this, ListsActivity.class);
                startActivity(intentLists);
            }
        });
*/
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLists = new Intent(NutritionActivity.this, SettingsActivity.class);
                startActivity(intentLists);
            }
        });

    }


    @Override
    protected  void onStart() {
        super.onStart();

        productName = findViewById(R.id.textView15);
        ingredients = findViewById(R.id.textView14);
        image = findViewById(R.id.iv);

        APICalls();

        String pn = getProductName();
        String ing = getIngredients();
        String url = getImageURL();
        String nf = getNutritionFacts();

        //God Bless the Picasso library
        Picasso.with(NutritionActivity.this).load(url).resize(415,415).into(image);
        ingredients.setText(ing);
        productName.setText(pn);

        displayNutritionalFacts(nf);

    }


    public String getProductName(){

        return productNameAPI;
    }

    public void setProductName(String productName){

        productNameAPI = productName;
    }


    public String getIngredients(){

        return ingredientsAPI;
    }

    public void setIngredients(String ingredients){

        ingredientsAPI = ingredients;
    }


    public void setNutritionFacts(String nf){

        nutritionFactsAPI = nf;
    }

    public String getNutritionFacts(){

        return nutritionFactsAPI;
    }

    public void displayNutritionalFacts(String nf){

        cals = findViewById(R.id.textView22);
        tFat = findViewById(R.id.textView23);
        sod = findViewById(R.id.textView24);
        carbo = findViewById(R.id.textView25);
        sug = findViewById(R.id.textView26);
        prot = findViewById(R.id.textView27);

        fatDV = findViewById(R.id.textView31);
        sodDV = findViewById(R.id.textView32);
        carbsDV = findViewById(R.id.textView33);
        protDV = findViewById(R.id.textView34);

        double dailyFat;
        double dailySodium;
        double dailyProtein;
        double dailyCarbohydrates;

        String dailyF;
        String dailyS;
        String dailyP;
        String dailyC;

        String calories = "";
        String fat = "";
        String sodium = "";
        String carbs = "";
        String sugar = "";
        String protein = "";

        if( nf.equals("N/A")){

            calories = "N/A";
            fat = "N/A";
            sodium = "N/A";
            carbs = "N/A";
            sugar = "N/A";
            protein = "N/A";

        }else {
            String[] nfList = nf.split(" ");

            //        Toast.makeText(NutritionActivity.this, nfList[3],Toast.LENGTH_SHORT).show();

            //CALORIES   FAT    SODIUM  CARBOHYDRATES   SUGAR  PROTEIN
            String[] keyWords = {"Energy", "(fat)", "Na", "Carbohydrate,", "NLEA", "Protein"};
            int[] subscripts = {0, 0, 0, 0, 0, 0};

            for (int i = 0; i < nfList.length; i++) {
                if (nfList[i].equals(keyWords[0])) {
                    subscripts[0] = i + 1;
                } else if (nfList[i].equals(keyWords[1])) {
                    subscripts[1] = i + 1;
                } else if (nfList[i].equals(keyWords[2])) {
                    subscripts[2] = i + 1;
                } else if (nfList[i].equals(keyWords[3])) {
                    subscripts[3] = i + 3;
                } else if (nfList[i].equals(keyWords[4])) {
                    subscripts[4] = i + 1;
                } else if (nfList[i].equals(keyWords[5])) {
                    subscripts[5] = i + 1;
                }
            }

            int caloriesSubscript = subscripts[0];
            int fatSubscript = subscripts[1];
            int sodiumSubscript = subscripts[2];
            int carbsSubscript = subscripts[3];
            int sugarSubscript = subscripts[4];
            int proteinSubscript = subscripts[5];

            //nfList[ subscripts[i] ] + _______Subscript = nfList[i] where ex. calories value exists

            calories = nfList[caloriesSubscript];
            fat = nfList[fatSubscript];
            sodium = nfList[sodiumSubscript];
            carbs = nfList[carbsSubscript];
            sugar = nfList[sugarSubscript];
            protein = nfList[proteinSubscript];

            dailyFat = Double.parseDouble(fat);
            dailySodium = Double.parseDouble(sodium);
            dailyCarbohydrates = Double.parseDouble(carbs);
            dailyProtein = Double.parseDouble(protein);

            if(dailyFat != 0.0){

                dailyFat = (dailyFat / 65) * 100;
                dailyFat = Precision.round(dailyFat, 2);
                dailyF = String.valueOf(dailyFat);

                StringBuilder df = new StringBuilder();
                df.append(dailyF);
                df.append("%");
                dailyF = df.toString();
                fatDV.setText(dailyF);
            }
            if(dailySodium != 0.0){

                dailySodium = (dailySodium / 2400) * 100;
                dailySodium = Precision.round(dailySodium, 2);
                dailyS = String.valueOf(dailySodium);

                StringBuilder ds = new StringBuilder();
                ds.append(dailyS);
                ds.append("%");
                dailyS = ds.toString();
                sodDV.setText(dailyS);
            }
            if(dailyCarbohydrates != 0.0){

                dailyCarbohydrates = (dailyCarbohydrates / 300) * 100;
                dailyCarbohydrates = Precision.round(dailyCarbohydrates, 2);
                dailyC = String.valueOf(dailyCarbohydrates);

                StringBuilder dc = new StringBuilder();
                dc.append(dailyC);
                dc.append("%");
                dailyC = dc.toString();
                carbsDV.setText(dailyC);
            }

            if(dailyProtein != 0.0){

                dailyProtein = (dailyProtein / 50) * 100;
                dailyProtein = Precision.round(dailyProtein, 2);
                dailyP= String.valueOf(dailyProtein);

                StringBuilder dp = new StringBuilder();
                dp.append(dailyP);
                dp.append("%");
                dailyP = dp.toString();
                protDV.setText(dailyP);
            }

            StringBuilder cal = new StringBuilder();
            cal.append(calories);
            cal.append(" kcal");
            calories = cal.toString();

            StringBuilder f = new StringBuilder();
            f.append(fat);
            f.append(" g");
            fat = f.toString();

            StringBuilder salt = new StringBuilder();
            salt.append(sodium);
            salt.append(" mg");
            sodium = salt.toString();

            StringBuilder c = new StringBuilder();
            c.append(carbs);
            c.append(" g");
            carbs = c.toString();

            StringBuilder s = new StringBuilder();
            s.append(sugar);
            s.append(" g");
            sugar = s.toString();

            StringBuilder p = new StringBuilder();
            p.append(protein);
            p.append(" g");
            protein = p.toString();
        }

        cals.setText(calories);
        tFat.setText(fat);
        sod.setText(sodium);
        carbo.setText(carbs);
        sug.setText(sugar);
        prot.setText(protein);
    }


    public void setImageURL(String url){

        imageURLAPI = url;
    }

    public String getImageURL(){

        return imageURLAPI;
    }

    public void APICalls() {
        //Change key here if subscription ends
//        String key = "wimlozl4c5h31ri2enulgsw9r726nr";
//              key = "p72icim2v99a2kpim2j6r2sx1pkal6
//                      33xmp0ceyex0askf7uwk7an24lm22f

        String key2 = "f3l69xwneaciuzcz3mp9tefx1io1hn";

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
            RootObject value = g.fromJson(data, RootObject.class);

            String ingredients = "";
            String nutritionFacts = "";

            String productName = value.products[0].product_name;

            String cat = value.products[0].category;
            String[] catList = cat.split(" ");

            if( catList[0].equals("Food,")){
                ingredients = value.products[0].ingredients;
                nutritionFacts = value.products[0].nutrition_facts;
            }
            else{
                ingredients = "N/A";
                nutritionFacts = "N/A";
            }

//            Toast.makeText(NutritionActivity.this, ingredients,Toast.LENGTH_SHORT).show();

            String imageURL = value.products[0].images[0];

            // NEEDS TO GET TO HERE TO WORK
            setImageURL(imageURL);
            setProductName(productName);
            setIngredients(ingredients);
            setNutritionFacts(nutritionFacts);

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
    }

    //  DELETED REVIEWS AND STORE CLASSES IF ERROR COPY FROM DOCUMENTATION

    public class RootObject
    {
        public Product[] products;
    }

}
