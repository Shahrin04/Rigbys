package com.example.rigbys.Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rigbys.Prevalent.Prevalent;
import com.example.rigbys.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class ResetPasswordActivity extends AppCompatActivity {
    private TextView pageTitle, pageText;
    private EditText phoneEdit, questionOneEdit, questionTwoEdit;
    private Button verifyButton;
    private String check = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        check = getIntent().getStringExtra("check");

        pageTitle = findViewById(R.id.reset_pass_title);
        pageText = findViewById(R.id.reset_pass_text);
        phoneEdit = findViewById(R.id.reset_pass_phone_number);
        questionOneEdit = findViewById(R.id.reset_pass_question1);
        questionTwoEdit = findViewById(R.id.reset_pass_question2);
        verifyButton = findViewById(R.id.reset_pass_verify_button);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (check.equals("login")){
            verifyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fromLogin();
                }
            });

        }else if (check.equals("settings")){
            pageTitle.setText("Set Security Questions");
            phoneEdit.setVisibility(View.GONE);
            verifyButton.setText("Set");

            displaySecurityAnswers();

            verifyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fromSettings();
                }
            });
        }
    }

    private void displaySecurityAnswers() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child("User Info").child(Prevalent.currentOnlineUser.getPhone());

        ref.child("Security Question").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String ans1 = snapshot.child("answer1").getValue().toString();
                    String ans2 = snapshot.child("answer2").getValue().toString();

                    questionOneEdit.setText(ans1);
                    questionTwoEdit.setText(ans2);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error: "+error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void fromSettings() {
        String answer1 = questionOneEdit.getText().toString().trim().toLowerCase();
        String answer2 = questionTwoEdit.getText().toString().trim().toLowerCase();

        if (TextUtils.isEmpty(answer1)){
            questionOneEdit.setError("Please answer this question");
            questionOneEdit.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(answer2)){
            questionTwoEdit.setError("Please answer this question");
            questionTwoEdit.requestFocus();
            return;
        }else {
            final DatabaseReference refSettings = FirebaseDatabase.getInstance().getReference()
                    .child("User Info").child(Prevalent.currentOnlineUser.getPhone());
            HashMap<String, Object> map = new HashMap<>();
            map.put("answer1", answer1);
            map.put("answer2", answer2);

            refSettings.child("Security Question").updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "Your answer stored successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        finish();
                    }else {
                        Toast.makeText(getApplicationContext(), "Error. Please check your answers", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private void fromLogin() {
        String phone = phoneEdit.getText().toString().trim();
        String answer1 = questionOneEdit.getText().toString().trim().toLowerCase();
        String answer2 = questionTwoEdit.getText().toString().trim().toLowerCase();

        if (TextUtils.isEmpty(phone)){
            phoneEdit.setError("Please input Phone Number");
            phoneEdit.requestFocus();
            return;
        }else if (phone.length()<11){
            phoneEdit.setError("Please input all Numbers");
            phoneEdit.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(answer1)){
            questionOneEdit.setError("Please answer this question");
            questionOneEdit.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(answer2)){
            questionTwoEdit.setError("Please answer this question");
            questionTwoEdit.requestFocus();
            return;
        }else {
            final DatabaseReference refLogin = FirebaseDatabase.getInstance().getReference()
                    .child("User Info").child(phone);
            refLogin.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        if (snapshot.hasChild("Security Question")){
                            String ans1 = snapshot.child("Security Question").child("answer1").getValue().toString();
                            String ans2 = snapshot.child("Security Question").child("answer2").getValue().toString();

                            if (!ans1.equals(answer1)){
                                questionOneEdit.setError("Answer didn't match");
                                questionOneEdit.requestFocus();
                                return;
                            }
                            if (!ans2.equals(answer2)){
                                questionTwoEdit.setError("Answer didn't match");
                                questionTwoEdit.requestFocus();
                                return;
                            }else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ResetPasswordActivity.this);
                                builder.setTitle("Reset Password");

                                final EditText newPassword = new EditText(ResetPasswordActivity.this);
                                newPassword.setHint("Write New Password");
                                builder.setView(newPassword);

                                builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (!newPassword.getText().toString().equals("")){
                                            refLogin.child("password").setValue(newPassword.getText().toString())
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()){
                                                                Toast.makeText(getApplicationContext(), "Your Password has been changed Successfully, Please Login Again", Toast.LENGTH_LONG).show();
                                                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                                                finish();
                                                            }else {
                                                                Toast.makeText(getApplicationContext(), "Error: "+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                            }
                                                        }
                                                    });
                                        }else {
                                            Toast.makeText(getApplicationContext(), "Write new Password", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                builder.show();
                            }
                        }else {
                            Toast.makeText(getApplicationContext(), "You didn't set the Security Questions. Please contact with Customer Service", Toast.LENGTH_LONG).show();
                        }

                    }else {
                        Toast.makeText(getApplicationContext(), "Your have entered wrong Phone Number", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(), "Error: "+error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

}