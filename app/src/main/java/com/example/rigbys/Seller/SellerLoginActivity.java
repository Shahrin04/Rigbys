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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SellerLoginActivity extends AppCompatActivity {

    private EditText emailEdit, passwordEdit;
    private Button loginButton;

    private ProgressDialog loadingBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_login);

        mAuth = FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(this);

        emailEdit = findViewById(R.id.seller_login_email);
        passwordEdit = findViewById(R.id.seller_login_password);
        loginButton = findViewById(R.id.seller_login_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

    }

    private void loginUser() {
        String email = emailEdit.getText().toString();
        String password = passwordEdit.getText().toString();

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
        }else {
            loginStart(email, password);
            loadingBar.setTitle("Login to Account");
            loadingBar.setMessage("Please wait while we check your credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
        }
    }

    private void loginStart(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    loadingBar.dismiss();
                    Toast.makeText(getApplicationContext(), "Logged In Successfully", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplicationContext(), SellerHomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                }else {
                    loadingBar.dismiss();
                    Toast.makeText(getApplicationContext(), "Error: "+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}