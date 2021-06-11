package com.example.rigbys.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.rigbys.Model.ProductItemModel;
import com.example.rigbys.R;
import com.example.rigbys.Viewholder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminApproveSellerProductActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_approve_seller_product);

        recyclerView = findViewById(R.id.approve_seller_product_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        reference = FirebaseDatabase.getInstance().getReference().child("Products");
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<ProductItemModel> options
                = new FirebaseRecyclerOptions.Builder<ProductItemModel>()
                .setQuery(reference.orderByChild("status").equalTo("Not Approved"), ProductItemModel.class).build();

        FirebaseRecyclerAdapter<ProductItemModel, ProductViewHolder> adapter
                = new FirebaseRecyclerAdapter<ProductItemModel, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull ProductItemModel model) {
                holder.productName.setText(model.getName());
                holder.productDescription.setText(model.getDescription());
                holder.productPrice.setText("Price: "+model.getPrice()+" TK");
                Picasso.get().load(model.getLink()).into(holder.productImage);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String trick = model.getCategory();
                        final String productId = getRef(position).getKey();
                        CharSequence option[] = new CharSequence[]{
                                "Yes",
                                "Cancel"
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(AdminApproveSellerProductActivity.this);
                        builder.setTitle("Want to approve this Product ?");
                        builder.setItems(option, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which==0){
                                    approveProducts(productId, trick);
                                    startActivity(new Intent(getApplicationContext(), AdminHomeActivity.class));
                                }else if (which==1){
                                    dialog.cancel();
                                }
                            }
                        });
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout, parent, false);
                ProductViewHolder holder = new ProductViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void approveProducts(String productId, String trick) {
        HashMap<String, Object> map = new HashMap<>();
        //map.put("status", "Approved");
        map.remove("status");
        map.put("status", "Approved");
        map.put("trick", trick+"Approved");
        reference.child(productId).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Product has been approved successfully", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(), "Error: "+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }


}