package com.example.rigbys.Seller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rigbys.R;
import com.example.rigbys.Seller.SellerHomeActivity;
import com.example.rigbys.Seller.SellerLoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SellerRegisterActivity extends AppCompatActivity {
    private EditText nameEdit, phoneEdit, emailEdit, passwordEdit, addressEdit;
    private Button haveAccountButton, registerButton;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_register);

        mAuth = FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(this);

        nameEdit = findViewById(R.id.seller_register_name);
        phoneEdit = findViewById(R.id.seller_register_phone);
        emailEdit = findViewById(R.id.seller_register_email);
        passwordEdit = findViewById(R.id.seller_register_password);
        addressEdit = findViewById(R.id.seller_register_address);
        registerButton = findViewById(R.id.seller_register_button);
        haveAccountButton = findViewById(R.id.seller_register_haveAccount_button);

        haveAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SellerLoginActivity.class));
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerSeller();
            }
        });
    }

    private void registerSeller() {
        String name = nameEdit.getText().toString().trim();
        String phone = phoneEdit.getText().toString().trim();
        String email = emailEdit.getText().toString().trim();
        String password = passwordEdit.getText().toString().trim();
        String address = addressEdit.getText().toString().trim();

        if (TextUtils.isEmpty(name)){
            nameEdit.setError("Input Name");
            nameEdit.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(phone)){
            phoneEdit.setError("Input Phone Number");
            phoneEdit.requestFocus();
            return;
        }else if (phone.length()<11){
            phoneEdit.setError("Input All Numbers");
            phoneEdit.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(email)) {
            emailEdit.setError("Input Email Address");
            emailEdit.requestFocus();
            return;
        }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEdit.setError("Input a valid Email Address");
            emailEdit.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            passwordEdit.setError("Input your Password");
            passwordEdit.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(address)) {
            addressEdit.setError("Input your Address");
            addressEdit.requestFocus();
            return;
        }else {
            loadingBar.setTitle("Creating Seller Account");
            loadingBar.setMessage("Please wait while your account is creating");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            startRegister(email, password, name, phone, address);
        }
    }

    private void startRegister(String email, String password, String name, String phone, String address) {

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Sellers");
                    String sid = mAuth.getCurrentUser().getUid();

                    HashMap<String, Object> map = new HashMap<>();
                    map.put("name", name);
                    map.put("phone", phone);
                    map.put("address", address);
                    map.put("email", email);
                    map.put("password", password);
                    map.put("sid", sid);

                    ref.child(sid).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                loadingBar.dismiss();
                                Toast.makeText(getApplicationContext(), "Registration has been completed successfully", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(getApplicationContext(), SellerHomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();

                                nameEdit.setText("");
                                phoneEdit.setText("");
                                addressEdit.setText("");
                                emailEdit.setText("");
                                passwordEdit.setText("");
                            }
                            else {
                                loadingBar.dismiss();
                                Toast.makeText(getApplicationContext(), "Error: "+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }else {
                    loadingBar.dismiss();
                    Toast.makeText(getApplicationContext(), "Error: "+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}