package com.example.rigbys.Buyer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rigbys.Prevalent.Prevalent;
import com.example.rigbys.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {
    private TextView closeText, updateText;
    private CircleImageView profileImage;
    private Button chooseButton, securityQuestionButton;
    private EditText phoneEdit, nameEdit, addressEdit;

    private StorageReference storeRef;
    private StorageTask photoUpload;
    private DatabaseReference dataRef;
    private String checker = "";
    private String photoUrl = "";
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        closeText = findViewById(R.id.settings_close);
        updateText = findViewById(R.id.settings_update);
        profileImage = findViewById(R.id.settings_profile_image);
        chooseButton = findViewById(R.id.settings_choose_picture_button);
        phoneEdit = findViewById(R.id.settings_phone_number);
        nameEdit = findViewById(R.id.settings_full_name);
        addressEdit = findViewById(R.id.settings_adress);
        securityQuestionButton = findViewById(R.id.settings_security_questions_button);

        closeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        chooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker = "clicked";
                CropImage.activity(imageUri)
                        .setAspectRatio(1,1)
                        .start(SettingsActivity.this);
            }
        });

        updateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checker.equals("clicked")){
                    updatePhotoWithInfo();
                }else {
                    updateInfo();
                }
            }
        });

        securityQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ResetPasswordActivity.class);
                intent.putExtra("check", "settings");
                startActivity(intent);
            }
        });

        showInfo(profileImage, phoneEdit, nameEdit, addressEdit);

    }

    private void updateInfo() {
        if (TextUtils.isEmpty(phoneEdit.getText().toString().trim())){
            phoneEdit.setError("Input Order Phone Number");
            phoneEdit.requestFocus();
            return;
        }else if (phoneEdit.getText().toString().trim().length()<11){
            phoneEdit.setError("Input All Numbers");
            phoneEdit.requestFocus();
            return;
        }
        else if (TextUtils.isEmpty(nameEdit.getText().toString().trim())) {
            nameEdit.setError("Input Order Full Name");
            nameEdit.requestFocus();
            return;
        }
        else if (TextUtils.isEmpty(addressEdit.getText().toString().trim())) {
            addressEdit.setError("Input Delivery Address");
            addressEdit.requestFocus();
            return;
        }

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("User Info");
        HashMap<String, Object> upload = new HashMap<String, Object>();
        upload.put("name", nameEdit.getText().toString().trim());
        upload.put("phone_order", phoneEdit.getText().toString().trim());
        upload.put("address", addressEdit.getText().toString().trim());

        reference.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(upload);
        Toast.makeText(getApplicationContext(), "Information updated successfully", Toast.LENGTH_LONG).show();
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        finish();
    }

    private void updatePhotoWithInfo() {
        if (TextUtils.isEmpty(phoneEdit.getText().toString().trim())){
            phoneEdit.setError("Input Order Phone Number");
            phoneEdit.requestFocus();
            return;
        }else if (phoneEdit.getText().toString().trim().length()<11){
            phoneEdit.setError("Input All Numbers");
            phoneEdit.requestFocus();
            return;
        }
        else if (TextUtils.isEmpty(nameEdit.getText().toString().trim())) {
            nameEdit.setError("Input Order Full Name");
            nameEdit.requestFocus();
            return;
        }
        else if (TextUtils.isEmpty(addressEdit.getText().toString().trim())) {
            addressEdit.setError("Input Delivery Address");
            addressEdit.requestFocus();
            return;
        }else if (checker.equals("clicked")){
            photoWithInfo();
        }
    }

    private void photoWithInfo() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Profile Info upload");
        progressDialog.setMessage("Please wait until Profile Information is updating");
        progressDialog.show();

        if (imageUri!=null){
            storeRef = FirebaseStorage.getInstance().getReference().child("Profile Pictures");
            StorageReference filePath = storeRef.child(Prevalent.currentOnlineUser.getPhone()+".jpg");
            photoUpload = filePath.putFile(imageUri);

            photoUpload.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return filePath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUrl = task.getResult();
                        photoUrl = downloadUrl.toString();

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("User Info");
                        HashMap<String, Object> upload = new HashMap<String, Object>();
                        upload.put("name", nameEdit.getText().toString().trim());
                        upload.put("phone_order", phoneEdit.getText().toString().trim());
                        upload.put("address", addressEdit.getText().toString().trim());
                        upload.put("profile_image", photoUrl);

                        reference.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(upload);
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Information updated successfully", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        finish();
                    }else {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Error: "+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });

        }else {
            Toast.makeText(getApplicationContext(), "Image is not selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode== RESULT_OK && data != null){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            profileImage.setImageURI(imageUri);

        }else {
            Toast.makeText(getApplicationContext(), "Error, Try Again", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
            finish();
        }
    }

    private void showInfo(CircleImageView profileImage, EditText phoneEdit, EditText nameEdit, EditText addressEdit) {
        dataRef = FirebaseDatabase.getInstance().getReference().child("User Info").child(Prevalent.currentOnlineUser.getPhone());

        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    if (snapshot.child("address").exists()){
                        String name = snapshot.child("name").getValue().toString();
                        String phone = snapshot.child("phone_order").getValue().toString();
                        String address = snapshot.child("address").getValue().toString();

                        nameEdit.setText(name);
                        phoneEdit.setText(phone);
                        addressEdit.setText(address);

                        if (snapshot.child("profile_image").exists()){
                            String image = snapshot.child("profile_image").getValue().toString();
                            Picasso.get().load(image).into(profileImage);
                        }
                    }

                  /*  if (snapshot.child("profile_image").exists()){
                        String name = snapshot.child("name").getValue().toString();
                        String phone = snapshot.child("phone_order").getValue().toString();
                        String address = snapshot.child("address").getValue().toString();
                        String image = snapshot.child("profile_image").getValue().toString();

                        nameEdit.setText(name);
                        phoneEdit.setText(phone);
                        addressEdit.setText(address);
                        Picasso.get().load(image).into(profileImage);
                    }*/
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error: "+error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}