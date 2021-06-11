package com.example.rigbys.Buyer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.rigbys.R;
import com.example.rigbys.Seller.SellerAddNewProductActivity;

public class CategoryActivity extends AppCompatActivity {
    private ImageView tshirts, sports_tshirts, female_dress, sweaters, glasses, ladies_bags, hats_caps, shoes, headphone, laptop, watches, mobiles;
    private String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        tshirts = findViewById(R.id.male_dress);
        sports_tshirts = findViewById(R.id.sports_dress);
        female_dress = findViewById(R.id.female_dress);
        sweaters = findViewById(R.id.winter_dress);
        glasses = findViewById(R.id.glasses);
        ladies_bags = findViewById(R.id.female_bags);
        hats_caps = findViewById(R.id.hats);
        shoes = findViewById(R.id.shoes);
        headphone = findViewById(R.id.headphones);
        laptop = findViewById(R.id.computer);
        watches = findViewById(R.id.watches);
        mobiles = findViewById(R.id.mobiles);

        //for admin access to maintain products//
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle!=null){
            type = getIntent().getExtras().get("admin").toString();
        }
        //for admin access to maintain products//

        tshirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), CategoryDetailsActivity.class);
//                intent.putExtra("category", "tshirts");
//                if (type.equals("admin")){
//                    intent.putExtra("admin", "admin");
//                }
//                startActivity(intent);
                Intent intent = new Intent(getApplicationContext(), CategoryDetailsActivity.class);
                Bundle extras = new Bundle();
                extras.putString("category", "tshirts");
                extras.putString("admin", type);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
        sports_tshirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CategoryDetailsActivity.class);
                Bundle extras = new Bundle();
                extras.putString("category", "sportsTshirts");
                extras.putString("admin", type);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
        female_dress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CategoryDetailsActivity.class);
                Bundle extras = new Bundle();
                extras.putString("category", "femaleDress");
                extras.putString("admin", type);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
        sweaters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CategoryDetailsActivity.class);
                Bundle extras = new Bundle();
                extras.putString("category", "sweaters");
                extras.putString("admin", type);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
        glasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CategoryDetailsActivity.class);
                Bundle extras = new Bundle();
                extras.putString("category", "glasses");
                extras.putString("admin", type);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
        ladies_bags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CategoryDetailsActivity.class);
                Bundle extras = new Bundle();
                extras.putString("category", "ladiesBags");
                extras.putString("admin", type);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
        hats_caps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CategoryDetailsActivity.class);
                Bundle extras = new Bundle();
                extras.putString("category", "hatsCaps");
                extras.putString("admin", type);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
        shoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CategoryDetailsActivity.class);
                Bundle extras = new Bundle();
                extras.putString("category", "shoes");
                extras.putString("admin", type);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
        headphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CategoryDetailsActivity.class);
                Bundle extras = new Bundle();
                extras.putString("category", "headphone");
                extras.putString("admin", type);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
        laptop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CategoryDetailsActivity.class);
                Bundle extras = new Bundle();
                extras.putString("category", "laptop");
                extras.putString("admin", type);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
        watches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CategoryDetailsActivity.class);
                Bundle extras = new Bundle();
                extras.putString("category", "watches");
                extras.putString("admin", type);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
        mobiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CategoryDetailsActivity.class);
                Bundle extras = new Bundle();
                extras.putString("category", "mobile");
                extras.putString("admin", type);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
    }
}