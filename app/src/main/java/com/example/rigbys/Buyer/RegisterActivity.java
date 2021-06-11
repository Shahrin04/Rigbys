package com.example.rigbys.Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rigbys.Buyer.LoginActivity;
import com.example.rigbys.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private String ParentDbName = "User Info";
    private EditText inputName, inputMobileNumber, inputPassword;
    private Button createAccount;
    private ProgressDialog loadingBar;

    //private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        loadingBar = new ProgressDialog(this);
        inputName = findViewById(R.id.register_name);
        inputMobileNumber = findViewById(R.id.register_input_phoneNumber);
        inputPassword = findViewById(R.id.register_input_password);
        createAccount = findViewById(R.id.register_create_account);

        //mAuth = FirebaseAuth.getInstance();

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerAccount();
            }
        });
    }

    private void registerAccount() {
        String name = inputName.getText().toString().trim();
        String phone = inputMobileNumber.getText().toString().trim();
        //String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();

        if (name.isEmpty()){
            inputName.setError("Please input your Name");
            inputName.requestFocus();
            return;
        }
        if (phone.isEmpty()){
            inputMobileNumber.setError("Please input your PhoneNumber");
            inputMobileNumber.requestFocus();
            return;
        }else if (phone.length()<11){
            inputMobileNumber.setError("Please input all Digit");
            inputMobileNumber.requestFocus();
            return;
        }
        /*
        if (email.isEmpty()){
            inputEmail.setError("Please input email address");
            inputEmail.requestFocus();
            return;
        }*/

        if (password.isEmpty()){
            inputPassword.setError("Please input your Password");
            inputPassword.requestFocus();
            return;
        }else{
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait while your account is creating");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            //validateInfo(name, phone, email, password);
            validateInfo(name, phone, password);

            inputName.setText("");
            inputMobileNumber.setText("");
            //inputEmail.setText("");
            inputPassword.setText("");
        }
    }

    private void validateInfo(String name, String phone, String password) {

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference(ParentDbName);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!(snapshot.child(phone).exists())){
                    HashMap<String, Object> loadData = new HashMap<String, Object>();
                    loadData.put("name", name);
                    loadData.put("phone", phone);
                    loadData.put("password", password);

                    ref.child(phone).updateChildren(loadData).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Congratulations!!! Your Account has been created",Toast.LENGTH_LONG).show();
                                loadingBar.dismiss();
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));

                            }else{
                                Toast.makeText(getApplicationContext(), "Error: "+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                loadingBar.dismiss();
                            }
                        }
                    });

                }else{
                    Toast.makeText(getApplicationContext(), "This "+phone+" number already exists",Toast.LENGTH_LONG).show();
                    loadingBar.dismiss();
                    inputMobileNumber.setError("Try a new One");
                    inputMobileNumber.requestFocus();
                    return;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error: "+error.getMessage(),Toast.LENGTH_LONG).show();
                loadingBar.dismiss();
            }
        });






/*
        Users users = new Users(name, phone, email, password);
        ref.child(phone).setValue(users);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Congratulations! Your account has been created successfully", Toast.LENGTH_LONG).show();
                            loadingBar.dismiss();
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        }else{
                            if (task.getException() instanceof FirebaseAuthUserCollisionException){
                                Toast.makeText(getApplicationContext(), "This email is already registered",Toast.LENGTH_LONG).show();
                                loadingBar.dismiss();
                            }else{
                                Toast.makeText(getApplicationContext(), "Error: "+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                loadingBar.dismiss();
                            }
                        }
                    }
                });
        */

    }


}