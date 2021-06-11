package com.example.rigbys.Buyer;

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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rigbys.Model.CartModel;
import com.example.rigbys.Prevalent.Prevalent;
import com.example.rigbys.R;
import com.example.rigbys.Viewholder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CartActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button nextButton;
    private TextView txTotalPrice, txtCongrats;

    private int grandTotal = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        nextButton = findViewById(R.id.cart_next_button);
        txTotalPrice = findViewById(R.id.reset_pass_title);
        txtCongrats = findViewById(R.id.cart_congrats_text);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CartConfirmActivity.class);
                intent.putExtra("grand total", String.valueOf(grandTotal));
                grandTotal = 0;
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkOrderState();

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart Info");

        FirebaseRecyclerOptions<CartModel> options =
                new FirebaseRecyclerOptions.Builder<CartModel>()
                .setQuery(cartListRef.child("User View")
                .child(Prevalent.currentOnlineUser.getPhone())
                .child("Products"), CartModel.class).build();

        FirebaseRecyclerAdapter<CartModel, CartViewHolder> adapter
                = new FirebaseRecyclerAdapter<CartModel, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull CartModel model) {
                holder.txtProductName.setText(model.getName());
                holder.txtProductQuantity.setText("Quantity= "+model.getQuantity());
                holder.txtProductPrice.setText(model.getPrice()+" Tk");

                int perItemTotalPrice = Integer.valueOf(model.getPrice()) * Integer.valueOf(model.getQuantity());
                grandTotal = grandTotal + perItemTotalPrice;
                txTotalPrice.setText("Total Amount= " +String.valueOf(grandTotal)+" Tk");

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence charOptions[] = new CharSequence[]{
                                "Edit",
                                "Remove"
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this); //don't use getApplicationContext, it crush
                        builder.setTitle("Cart Options");
                        builder.setItems(charOptions, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which==0){
                                    Intent intent = new Intent(getApplicationContext(), ProductDetailActivity.class);
                                    intent.putExtra("id", model.getId());
                                    startActivity(intent);
                                }
                                if (which==1){
                                    startActivity(new Intent(getApplicationContext(), HomeActivity.class)); // to update cart activity total price
                                    //admin view
                                    cartListRef.child("Admin View")
                                            .child(Prevalent.currentOnlineUser.getPhone())
                                            .child("Products").child(model.getId())
                                            .removeValue();
                                    //user view
                                    cartListRef.child("User View")
                                            .child(Prevalent.currentOnlineUser.getPhone())
                                            .child("Products").child(model.getId())
                                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(getApplicationContext(), "Product removed successfully", Toast.LENGTH_SHORT).show();
                                            }else {
                                                Toast.makeText(getApplicationContext(), "Error"+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                }
                            }
                        });
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout, parent, false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void checkOrderState(){
        DatabaseReference orderRef= FirebaseDatabase.getInstance().getReference().child("Orders")
                .child(Prevalent.currentOnlineUser.getPhone());

        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String shippingState = snapshot.child("state").getValue().toString();
                    String userName = snapshot.child("name").getValue().toString();
                    if (shippingState.equals("Shipped")){
                        txTotalPrice.setText("Dear "+userName+" \nYour product has been shipped to your given address");
                        txTotalPrice.setTextSize(17);
                        txtCongrats.setText("Congratulations!!! Your order has been shipped successfully. After you get your product, you can purchase more");
                        txtCongrats.setVisibility(View.VISIBLE);
                        nextButton.setVisibility(View.GONE);

                    }else if (shippingState.equals("Not Shipped")){
                        txTotalPrice.setText("Please wait until our admin verify your order");
                        txTotalPrice.setTextSize(17);
                        txtCongrats.setVisibility(View.VISIBLE);
                        nextButton.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error: "+error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}