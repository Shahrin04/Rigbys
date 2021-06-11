package com.example.rigbys.Seller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.rigbys.Admin.AdminHomeActivity;
import com.example.rigbys.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class SellerMaintainProductActivity extends AppCompatActivity {
    private ImageView productImage;
    private EditText productName, productDescription, productPrice;
    private Button applyChangesButton, deleteProductButton;
    private DatabaseReference productRef;
    private StorageReference storageRef;

    private String productId = "", productStatus= "";
    private String image = "", imageName= "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_maintain_product);

        productStatus = getIntent().getStringExtra("status");
        productId = getIntent().getStringExtra("id");

        productRef = FirebaseDatabase.getInstance().getReference().child("Products");

        productImage = findViewById(R.id.seller_product_maintain_image);
        productName = findViewById(R.id.seller_product_maintain_name);
        productDescription = findViewById(R.id.seller_product_maintain_description);
        productPrice = findViewById(R.id.seller_product_maintain_price);
        applyChangesButton = findViewById(R.id.seller_apply_changes_button);
        deleteProductButton = findViewById(R.id.seller_product_maintain_delete_button);

        displayProductInfo();

        if (productStatus.equals("Approved")){
            applyChangesButton.setVisibility(View.GONE);
        }

        applyChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInfo();
            }
        });

        deleteProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProduct();
            }
        });
    }

    private void displayProductInfo() {
        productRef.child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    imageName = snapshot.child("image").getValue().toString();
                    String name = snapshot.child("name").getValue().toString();
                    String description = snapshot.child("description").getValue().toString();
                    String price = snapshot.child("price").getValue().toString();
                    image = snapshot.child("link").getValue().toString();

                    Picasso.get().load(image).into(productImage);
                    productName.setText(name);
                    productDescription.setText(description);
                    productPrice.setText(price);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error: "+error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    private void checkInfo() {
        String pName = productName.getText().toString().trim();
        String pDescription = productDescription.getText().toString().trim();
        String pPrice = productPrice.getText().toString().trim();

        if (TextUtils.isEmpty(pName)){
            productName.setError("Input Product name");
            productName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(pDescription)){
            productDescription.setError("Input Product Description");
            productDescription.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(pPrice)){
            productPrice.setError("Input Product Price");
            productPrice.requestFocus();
            return;
        }else {
            HashMap<String, Object> productMap = new HashMap<>();
            productMap.put("name", pName);
            productMap.put("lowerName", pName.toLowerCase());
            productMap.put("description", pDescription);
            productMap.put("price", pPrice);

            productRef.child(productId).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "Changes applied Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), SellerHomeActivity.class));
                        finish();
                    }else {
                        Toast.makeText(getApplicationContext(), "Error: "+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private void deleteProduct() {

        storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(image);

        storageRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Image from the storage deleted successfully", Toast.LENGTH_SHORT).show();

                    productRef.child(productId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Product has been deleted successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), SellerHomeActivity.class));
                                finish();
                            }else {
                                Toast.makeText(getApplicationContext(), "Error: "+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }else {
                    Toast.makeText(getApplicationContext(), "Image from the storage couldn't be deleted", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}