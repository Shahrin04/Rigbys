package com.example.rigbys.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rigbys.Model.CartModel;
import com.example.rigbys.R;
import com.example.rigbys.Viewholder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminShowProductActivity extends AppCompatActivity {
    private RecyclerView cartProductList;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference cartProductRef;
    private String id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_show_product);

        cartProductList = findViewById(R.id.admin_show_product_list);
        cartProductList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        cartProductList.setLayoutManager(layoutManager);

        id = getIntent().getStringExtra("id");

        cartProductRef = FirebaseDatabase.getInstance().getReference().child("Cart Info");
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<CartModel> options =
                new FirebaseRecyclerOptions.Builder<CartModel>()
                        .setQuery(cartProductRef.child("Admin View").child(id).child("Products"), CartModel.class).build();

        FirebaseRecyclerAdapter<CartModel, CartViewHolder> adapter =
                new FirebaseRecyclerAdapter<CartModel, CartViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull CartModel model) {
                        holder.txtProductName.setText("Product: "+model.getName());
                        holder.txtProductQuantity.setText("Quantity= "+model.getQuantity());
                        holder.txtProductPrice.setText(model.getPrice()+" Tk");
                    }

                    @NonNull
                    @Override
                    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout, parent, false);
                        CartViewHolder holder = new CartViewHolder(view);
                        return holder;
                    }
                };
        cartProductList.setAdapter(adapter);
        adapter.startListening();
    }
}