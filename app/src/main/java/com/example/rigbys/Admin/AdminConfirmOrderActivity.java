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
import android.widget.Button;
import android.widget.TextView;

import com.example.rigbys.Model.AdminOrderModel;
import com.example.rigbys.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminConfirmOrderActivity extends AppCompatActivity {
    private RecyclerView orderList;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference orderRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_confirm_order);

        orderList = findViewById(R.id.order_list);
        layoutManager = new LinearLayoutManager(this);
        orderList.setLayoutManager(layoutManager);

        orderRef = FirebaseDatabase.getInstance().getReference().child("Orders");
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<AdminOrderModel> options =
                new FirebaseRecyclerOptions.Builder<AdminOrderModel>().setQuery(orderRef, AdminOrderModel.class).build();

        FirebaseRecyclerAdapter<AdminOrderModel, AdminOrderViewHolder> adapter =
                new FirebaseRecyclerAdapter<AdminOrderModel, AdminOrderViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull AdminOrderViewHolder holder, int position, @NonNull AdminOrderModel model) {
                        holder.ad_userName.setText("Name: " +model.getName());
                        holder.ad_phone.setText("Phone: "+model.getPhone());
                        holder.ad_totalAmount.setText("Total Amount= "+model.getTotalAmount()+" Tk");
                        holder.ad_address.setText("Shipping Address: "+model.getAddress()+"\nCity: "+model.getCity());
                        holder.ad_dateTime.setText("Date: "+model.getDate()+"  Time: "+model.getTime());

                        holder.showOrderProductsButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String id = getRef(position).getKey(); //id which is musked as phone number
                                Intent intent = new Intent(getApplicationContext(), AdminShowProductActivity.class);
                                intent.putExtra("id", id);
                                startActivity(intent);
                            }
                        });

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CharSequence option[] = new CharSequence[]{
                                        "Yes",
                                        "No"
                                };
                                AlertDialog.Builder builder = new AlertDialog.Builder(AdminConfirmOrderActivity.this);
                                builder.setTitle("Have you Shipped this Products already ?");
                                builder.setItems(option, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (which==0){
                                            String id = getRef(position).getKey(); //id which is musked as phone number
                                            orderRef.child(id).removeValue();
                                            FirebaseDatabase.getInstance().getReference().child("Cart Info").child("Admin View").child(id).removeValue();
                                        }else if (which==1){
                                            finish();
                                        }
                                    }
                                });
                                builder.show();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public AdminOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item_layout, parent, false);
                        AdminOrderViewHolder holder = new AdminOrderViewHolder(view);
                        return holder;
                    }
                };
        orderList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class AdminOrderViewHolder extends  RecyclerView.ViewHolder{
        private TextView ad_userName, ad_phone, ad_totalAmount, ad_address, ad_dateTime;
        private Button showOrderProductsButton;

        public AdminOrderViewHolder(@NonNull View itemView) {
            super(itemView);

            ad_userName = itemView.findViewById(R.id.order_item_user_name);
            ad_phone = itemView.findViewById(R.id.order_item_phone);
            ad_totalAmount = itemView.findViewById(R.id.order_item_total_amount);
            ad_address = itemView.findViewById(R.id.order_item_address);
            ad_dateTime = itemView.findViewById(R.id.order_item_date_time);
            showOrderProductsButton = itemView.findViewById(R.id.order_item_showProducts_Button);
        }
    }
}