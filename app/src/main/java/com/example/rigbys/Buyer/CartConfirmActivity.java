package com.example.rigbys.Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rigbys.Prevalent.Prevalent;
import com.example.rigbys.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class CartConfirmActivity extends AppCompatActivity {
    private EditText nameEdit, phoneEdit, addressEdit, cityEdit;
    private Button confirmButton;

    private String grandTotal="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_confirm);

        nameEdit = findViewById(R.id.cart_confirm_name);
        phoneEdit = findViewById(R.id.cart_confirm_phone);
        addressEdit = findViewById(R.id.cart_confirm_address);
        cityEdit = findViewById(R.id.cart_confirm_city);
        confirmButton = findViewById(R.id.cart_confirm_confirm_button);

        grandTotal = getIntent().getStringExtra("grand total");
        Toast.makeText(getApplicationContext(), grandTotal+" Tk", Toast.LENGTH_LONG).show();

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkField();
            }
        });
    }

    private void checkField() {
        if (TextUtils.isEmpty(nameEdit.getText().toString().trim())){
            nameEdit.setError("Provide Name");
            nameEdit.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(phoneEdit.getText().toString().trim())){
            phoneEdit.setError("Provide Phone Number");
            phoneEdit.requestFocus();
            return;
        }else if (phoneEdit.getText().toString().length()<11){
            phoneEdit.setError("Input All Number");
            phoneEdit.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(addressEdit.getText().toString().trim())){
            addressEdit.setError("Provide Shipment Address");
            addressEdit.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(cityEdit.getText().toString().trim())){
            cityEdit.setError("Provide City Name");
            cityEdit.requestFocus();
            return;
        }else {
            confirmOrder();
        }
    }

    private void confirmOrder() {
        String currentDate, currentTime;

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat date = new SimpleDateFormat("dd-MM-yyyy");
        currentDate = date.format(calendar.getTime());
        SimpleDateFormat time = new SimpleDateFormat("hh:mm:ss a");
        currentTime = time.format(calendar.getTime());

        final DatabaseReference cartData = FirebaseDatabase.getInstance().getReference()
                .child("Orders").child(Prevalent.currentOnlineUser.getPhone());

        HashMap<String, Object> map = new HashMap<>();
        map.put("totalAmount" , grandTotal);
        map.put("name" , nameEdit.getText().toString().trim());
        map.put("phone" , phoneEdit.getText().toString().trim());
        map.put("address" , addressEdit.getText().toString().trim());
        map.put("city" , cityEdit.getText().toString().trim());
        map.put("date" , currentDate);
        map.put("time" , currentTime);
        map.put("state" , "Not Shipped");

        cartData.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference().child("Cart Info")
                            .child("User View").child(Prevalent.currentOnlineUser.getPhone())
                            .child("Products").removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(getApplicationContext(), "Your order has been placed successfully", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }else {
                                        Toast.makeText(getApplicationContext(), "Error: "+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                }else {
                    Toast.makeText(getApplicationContext(), "Error: "+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}