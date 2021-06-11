package com.example.rigbys.Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rigbys.Admin.AdminHomeActivity;
import com.example.rigbys.Model.Users;
import com.example.rigbys.Prevalent.Prevalent;
import com.example.rigbys.R;
import com.example.rigbys.Seller.SellerRegisterActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {
    private String ParentDbName = "User Info";
    private EditText inputPhone, inputPassword;
    private Button loginButton;
    private TextView adminLink, notAdminLink, forgetPassword, registerAccount;
    private ProgressDialog loadingBar;
    private CheckBox checkBox;
   // private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Paper.init(this);
        checkBox = findViewById(R.id.rememberMe_check);

        loginButton = (Button) findViewById(R.id.login_button);
        inputPhone = (EditText) findViewById(R.id.login_phone);
        inputPassword = (EditText) findViewById(R.id.login_password);
        adminLink = findViewById(R.id.admin_link);
        notAdminLink = findViewById(R.id.notAdmin_link);
        registerAccount = findViewById(R.id.login_register_account);
        forgetPassword = findViewById(R.id.forgetMe_link);
        loadingBar = new ProgressDialog(this);

        //mAuth = FirebaseAuth.getInstance();

        registerAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SellerRegisterActivity.class));
            }
        });

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ResetPasswordActivity.class);
                intent.putExtra("check", "login");
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        adminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.setText("Admin Login");
                adminLink.setVisibility(View.INVISIBLE);
                notAdminLink.setVisibility(View.VISIBLE);
                ParentDbName = "Admin";
            }
        });

        notAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.setText("Login");
                notAdminLink.setVisibility(View.INVISIBLE);
                adminLink.setVisibility(View.VISIBLE);
                ParentDbName = "User Info";
            }
        });
    }

    private void loginUser() {
        String phone = inputPhone.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();

        if (phone.isEmpty())
        {
            inputPhone.setError("Please input Phone Number");
            inputPhone.requestFocus();
            return;

        }else if (phone.length()<11){
            inputPhone.setError("Please input all Digit");
            inputPhone.requestFocus();
            return;
        }
        if (password.isEmpty())
        {
            inputPassword.setError("Please input your Password");
            inputPassword.requestFocus();
            return;
        }
        else
        {
            loadingBar.setTitle("Login to Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            allowAccessToAccount(phone, password);
        }
    }

    private void allowAccessToAccount(String phone, String password) {
        if (checkBox.isChecked()){
            Paper.book().write(Prevalent.PhoneKey, phone);
            Paper.book().write(Prevalent.PasswordKey, password);
        }

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference(ParentDbName);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(phone).exists()){
                   Users userData = snapshot.child(phone).getValue(Users.class);
                   if (userData.getPhone().equals(phone)){
                       if (userData.getPassword().equals(password)){
                           if (ParentDbName.equals("Admin")){
                               Toast.makeText(getApplicationContext(), "Admin Logged In Successfully", Toast.LENGTH_LONG).show();
                               loadingBar.dismiss();
                               startActivity(new Intent(getApplicationContext(), AdminHomeActivity.class));
                           }else if (ParentDbName.equals("User Info")){
                               Toast.makeText(getApplicationContext(), "Logged In Successfully", Toast.LENGTH_LONG).show();
                               loadingBar.dismiss();
                               Prevalent.currentOnlineUser = userData;
                               startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                           }

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


        

/*
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    finish();
                    Intent intent = new Intent(getApplicationContext(), Home.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(getApplicationContext(), "Sign in Unsuccessful", Toast.LENGTH_LONG).show();
                }

            }
        }); */

    }
}