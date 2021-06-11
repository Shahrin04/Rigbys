package com.example.rigbys.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.rigbys.Buyer.HomeActivity;
import com.example.rigbys.Buyer.LoginActivity;
import com.example.rigbys.R;

public class AdminHomeActivity extends AppCompatActivity {

    private Button confirmOrderButton, maintainProductsButton, approveSellerProductButton, logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        confirmOrderButton = findViewById(R.id.admin_confirm_order_button);
        maintainProductsButton = findViewById(R.id.admin_maintain_products_button);
        approveSellerProductButton = findViewById(R.id.admin_approve_seller_products_button);
        logoutButton = findViewById(R.id.admin_logout_button);

        confirmOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AdminConfirmOrderActivity.class));
            }
        });

        maintainProductsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.putExtra("admin", "admin");
                startActivity(intent);
            }
        });

        approveSellerProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AdminApproveSellerProductActivity.class));
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }
}