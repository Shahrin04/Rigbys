package com.example.rigbys.Seller;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.rigbys.Buyer.CategoryDetailsActivity;
import com.example.rigbys.R;
import com.example.rigbys.Seller.SellerAddNewProductActivity;

public class SellerAddFragment extends Fragment {
    private ImageView tshirts, sports_tshirts, female_dress, sweaters, glasses, ladies_bags, hats_caps, shoes, headphone, laptop, watches, mobiles;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_seller_add, container, false);
        // Inflate the layout for this fragment
        tshirts = view.findViewById(R.id.category_male_dress);
        sports_tshirts = view.findViewById(R.id.category_sports_dress);
        female_dress = view.findViewById(R.id.category_female_dress);
        sweaters = view.findViewById(R.id.category_winter_dress);
        glasses = view.findViewById(R.id.category_glasses);
        ladies_bags = view.findViewById(R.id.category_female_bags);
        hats_caps = view.findViewById(R.id.category_hats);
        shoes = view.findViewById(R.id.category_shoes);
        headphone = view.findViewById(R.id.category_headphones);
        laptop = view.findViewById(R.id.category_computer);
        watches = view.findViewById(R.id.category_watches);
        mobiles = view.findViewById(R.id.category_mobiles);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tshirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), SellerAddNewProductActivity.class);
                intent.putExtra("category", "tshirts");
                startActivity(intent);
            }
        });
        sports_tshirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), SellerAddNewProductActivity.class);
                intent.putExtra("category", "sportsTshirts");
                startActivity(intent);
            }
        });
        female_dress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), SellerAddNewProductActivity.class);
                intent.putExtra("category", "femaleDress");
                startActivity(intent);
            }
        });
        sweaters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), SellerAddNewProductActivity.class);
                intent.putExtra("category", "sweaters");
                startActivity(intent);
            }
        });
        glasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), SellerAddNewProductActivity.class);
                intent.putExtra("category", "glasses");
                startActivity(intent);
            }
        });
        ladies_bags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), SellerAddNewProductActivity.class);
                intent.putExtra("category", "ladiesBags");
                startActivity(intent);
            }
        });
        hats_caps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), SellerAddNewProductActivity.class);
                intent.putExtra("category", "hatsCaps");
                startActivity(intent);
            }
        });
        shoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), SellerAddNewProductActivity.class);
                intent.putExtra("category", "shoes");
                startActivity(intent);
            }
        });
        headphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), SellerAddNewProductActivity.class);
                intent.putExtra("category", "headphone");
                startActivity(intent);
            }
        });
        laptop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), SellerAddNewProductActivity.class);
                intent.putExtra("category", "laptop");
                startActivity(intent);
            }
        });
        watches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), SellerAddNewProductActivity.class);
                intent.putExtra("category", "watches");
                startActivity(intent);
            }
        });
        mobiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), SellerAddNewProductActivity.class);
                intent.putExtra("category", "mobile");
                startActivity(intent);
            }
        });
    }
}