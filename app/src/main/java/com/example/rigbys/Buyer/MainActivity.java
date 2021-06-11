package com.example.rigbys.Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rigbys.Model.Users;
import com.example.rigbys.Prevalent.Prevalent;
import com.example.rigbys.R;
import com.example.rigbys.Seller.SellerHomeActivity;
import com.example.rigbys.Seller.SellerRegisterActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
    private String ParentDbName = "User Info";
    private Button joinNowButton;
    private TextView loginText, sellerText;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Paper.init(this);

        joinNowButton = findViewById(R.id.main_join_button);
        loginText = findViewById(R.id.main_login_text);
        sellerText = findViewById(R.id.app_seller);
        loadingBar = new ProgressDialog(this);

        sellerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SellerRegisterActivity.class));
            }
        });

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        joinNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });

        //////Auto Login (Paper)
        String phone = Paper.book().read(Prevalent.PhoneKey);
        String password = Paper.book().read(Prevalent.PasswordKey);
        if (phone != "" && password != ""){
            if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(password)){
                allowAccess(phone, password);
                loadingBar.setTitle("Login to Account");
                loadingBar.setMessage("Please wait....");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //for seller auto login//
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            Intent intent = new Intent(getApplicationContext(), SellerHomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
        //for seller auto login//
    }

    private void allowAccess(String phone, String password) {
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference(ParentDbName);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(phone).exists()){
                    Users userData = snapshot.child(phone).getValue(Users.class);
                    if (userData.getPhone().equals(phone)){
                        if (userData.getPassword().equals(password)){
                            Toast.makeText(getApplicationContext(), "Logged In Successfully", Toast.LENGTH_LONG).show();
                            loadingBar.dismiss();
                            Prevalent.currentOnlineUser = userData;
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        }else {
                            Toast.makeText(getApplicationContext(), "Wrong Password entered", Toast.LENGTH_LONG).show();
                            loadingBar.dismiss();
                        }
                    }

                }else {
                    Toast.makeText(getApplicationContext(), "This "+phone+" number does not exist",Toast.LENGTH_LONG).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error: "+error.getMessage(),Toast.LENGTH_LONG).show();
                loadingBar.dismiss();
            }
        });
    }
}