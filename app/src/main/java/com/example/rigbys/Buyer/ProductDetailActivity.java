package com.example.rigbys.Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.rigbys.Buyer.HomeActivity;
import com.example.rigbys.Model.ProductItemModel;
import com.example.rigbys.Prevalent.Prevalent;
import com.example.rigbys.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailActivity extends AppCompatActivity {
    private ImageView productImage;
    private TextView productName, productDescription, productPrice;
    private ElegantNumberButton numberButton;
    private Button cartButton;

    private String productId = "", state = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        productId = getIntent().getStringExtra("id");

        productImage = findViewById(R.id.p_a_image);
        productName = findViewById(R.id.p_a_product_name);
        productDescription = findViewById(R.id.p_a_product_description);
        productPrice = findViewById(R.id.p_a_product_price);
        numberButton = findViewById(R.id.elegant_button);
        cartButton = findViewById(R.id.p_a_addToCart);

        getProductDetails(productId);

        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state.equals("Order Shipped") || state.equals("Order Placed")){
                    Toast.makeText(getApplicationContext(), "You can purchase more products once your previous order is shipped or confirmed", Toast.LENGTH_LONG).show();
                }else {
                    addToCart(productId);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkOrderState();
    }

    private void addToCart(String productId) {
        String currentDate, currentTime;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat date = new SimpleDateFormat("dd-MM-yyyy");
        currentDate = date.format(calendar.getTime());

        SimpleDateFormat time = new SimpleDateFormat("hh:mm:ss a");
        currentTime = time.format(calendar.getTime());

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Cart Info");
        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("date", currentDate);
        cartMap.put("time", currentTime);
        cartMap.put("id", productId);
        cartMap.put("name", productName.getText().toString().trim());
        cartMap.put("price", productPrice.getText().toString().trim());
        cartMap.put("quantity", numberButton.getNumber());
        cartMap.put("discount", "");

        reference.child("User View").child(Prevalent.currentOnlineUser.getPhone())
                .child("Products").child(productId).updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            reference.child("Admin View").child(Prevalent.currentOnlineUser.getPhone())
                                    .child("Products").child(productId).updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(getApplicationContext(), "Product added to cart successfully", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                                        }
                                    });

                        }else {
                            Toast.makeText(getApplicationContext(), "Error: "+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void getProductDetails(String productId) {
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Products");
        productRef.child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    ProductItemModel products = snapshot.getValue(ProductItemModel.class);
                    productName.setText(products.getName());
                    productDescription.setText(products.getDescription());
                    productPrice.setText(products.getPrice());
                    Picasso.get().load(products.getLink()).into(productImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error: "+error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void checkOrderState(){
        DatabaseReference orderRef= FirebaseDatabase.getInstance().getReference().child("Orders")
                .child(Prevalent.currentOnlineUser.getPhone());

        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String shippingState = snapshot.child("state").getValue().toString();
                    String userName = snapshot.child("name").getValue().toString();
                    if (shippingState.equals("Shipped")){
                        state = "Order Shipped";

                    }else if (shippingState.equals("Not Shipped")){
                        state = "Order Placed";
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error: "+error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}