package com.example.rigbys.Seller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.rigbys.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class SellerAddNewProductActivity extends AppCompatActivity {
    private String categoryNames, imageName= "";
    private ImageView addProductImage;
    private EditText addProductName, addProductDescription, addProductPrice;
    private Button addProductButton;
    private ProgressDialog loadingBar;

    private static final int galleryPick = 1;
    private String randomProductKey;
    private Uri imageUri;
    private String name, description, price, saveCurrentDate, saveCurrentTime, downloadImageUrl;
    private StorageReference storageRef;
    private DatabaseReference databaseRef, sellerRef;
    private String sAddress, sName, sPhone, sEmail, sID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_add_new_product);

        addProductImage = findViewById(R.id.add_product_image);
        addProductName = findViewById(R.id.add_product_name);
        addProductDescription = findViewById(R.id.add_product_description);
        addProductPrice = findViewById(R.id.add_product_price);
        addProductButton = findViewById(R.id.add_product_button);
        loadingBar = new ProgressDialog(this);

        categoryNames = getIntent().getExtras().get("category").toString();
        storageRef = FirebaseStorage.getInstance().getReference().child("Product Image");
        databaseRef = FirebaseDatabase.getInstance().getReference().child("Products");
        sellerRef = FirebaseDatabase.getInstance().getReference().child("Sellers");

        Toast.makeText(getApplicationContext(), categoryNames, Toast.LENGTH_LONG).show();

        addProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateInfo();
            }
        });

        sellerRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    sAddress = snapshot.child("address").getValue().toString();
                    sName = snapshot.child("name").getValue().toString();
                    sPhone = snapshot.child("phone").getValue().toString();
                    sEmail = snapshot.child("email").getValue().toString();
                    sID = snapshot.child("sid").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error: "+error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
    private void openGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, galleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==galleryPick && resultCode==RESULT_OK && data!=null){
            imageUri = data.getData();
            addProductImage.setImageURI(imageUri);
        }
    }

    private void validateInfo() {
        name = addProductName.getText().toString().trim();
        description = addProductDescription.getText().toString().trim();
        price = addProductPrice.getText().toString().trim();

        if (imageUri==null){
            Toast.makeText(getApplicationContext(), "Select product Image first", Toast.LENGTH_LONG).show();
        }else if (imageUri!=null){
            if (TextUtils.isEmpty(name)){
                addProductName.setError("Input Product Name");
                addProductName.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(description)){
                addProductDescription.setError("Input Product Description");
                addProductDescription.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(price)){
                addProductPrice.setError("Input Product Price");
                addProductPrice.requestFocus();
                return;
            }else {
                storeProductInfo();
                loadingBar.setTitle("Product Upload");
                loadingBar.setMessage("Please wait, while product is uploading");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
            }
        }
    }

    private void storeProductInfo() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        randomProductKey = saveCurrentDate +" "+ saveCurrentTime;

        imageName = imageUri.getLastPathSegment()+randomProductKey+".jpg";

        final StorageReference filepath = storageRef.child(imageName);
        final UploadTask uploadTask = filepath.putFile(imageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loadingBar.dismiss();
                Toast.makeText(getApplicationContext(), "Error: "+e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getApplicationContext(), "Product Image uploaded Successfully", Toast.LENGTH_SHORT).show();
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()){
                            throw task.getException();
                        }
                        downloadImageUrl = filepath.getDownloadUrl().toString();
                        return filepath.getDownloadUrl();
                    }

                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){
                            loadingBar.dismiss();
                            Toast.makeText(getApplicationContext(), "Got Product Image Url", Toast.LENGTH_SHORT).show();
                            downloadImageUrl = task.getResult().toString();
                            saveProductInfoToDatabase();
                        }else {
                            loadingBar.dismiss();
                            Toast.makeText(getApplicationContext(), "Error: "+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                        addProductName.setText("");
                        addProductDescription.setText("");
                        addProductPrice.setText("");
                    }
                });
            }
        });
    }

    private void saveProductInfoToDatabase() {
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("id", randomProductKey);
        productMap.put("name", name);
        productMap.put("lowerName", name.toLowerCase());
        productMap.put("description", description);
        productMap.put("price", price);
        productMap.put("link", downloadImageUrl);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("category", categoryNames);
        productMap.put("image", imageName);

        productMap.put("sellerAddress", sAddress );
        productMap.put("sellerName", sName);
        productMap.put("sellerPhone", sPhone);
        productMap.put("sellerEmail", sEmail);
        productMap.put("sid", sID);
        productMap.put("status", "Not Approved");

        databaseRef.child(randomProductKey).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    loadingBar.dismiss();
                    Toast.makeText(getApplicationContext(), "Product data saved successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), SellerHomeActivity.class));
                  /*  SellerAddFragment fragment = new SellerAddFragment();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.seller_add_new_product_layout, fragment).commit(); */
                }else {
                    loadingBar.dismiss();
                    Toast.makeText(getApplicationContext(), "Error: "+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }


}