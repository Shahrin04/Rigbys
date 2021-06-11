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
import android.widget.EditText;
import android.widget.Toast;

import com.example.rigbys.Admin.AdminMaintainProductsActivity;
import com.example.rigbys.Buyer.ProductDetailActivity;
import com.example.rigbys.Model.ProductItemModel;
import com.example.rigbys.R;
import com.example.rigbys.Viewholder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class SearchActivity extends AppCompatActivity {
    private EditText searchEdit;
    private Button searchButton;
    private RecyclerView searchRecycler;
    private RecyclerView.LayoutManager layoutManager;

    private DatabaseReference reference;
    private String searchProduct;
    private String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //for admin access to maintain products//
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle!=null){
            type = getIntent().getExtras().get("admin").toString();
        }
        //for admin access to maintain products//

        searchEdit = findViewById(R.id.search_product);
        searchButton = findViewById(R.id.searchButton_id);

        searchRecycler = findViewById(R.id.search_recyclerView);
        searchRecycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        searchRecycler.setLayoutManager(layoutManager);

        reference = FirebaseDatabase.getInstance().getReference().child("Products");

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchProduct = searchEdit.getText().toString().toLowerCase();
                //onStart(); //important
                productSearch();
            }
        });
    }

    private void productSearch() {
        FirebaseRecyclerOptions<ProductItemModel> options =
                new FirebaseRecyclerOptions.Builder<ProductItemModel>()
                        .setQuery(reference.orderByChild("lowerName").startAt(searchProduct).endAt(searchProduct+"\uf8ff"), ProductItemModel.class).build();

        FirebaseRecyclerAdapter<ProductItemModel, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<ProductItemModel, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull ProductItemModel model) {
                        String status = model.getStatus();
                        if (status.equals("Approved")){
                            holder.productName.setText(model.getName());
                            //holder.productDescription.setText(model.getDescription());
                            holder.productPrice.setText("Price: "+model.getPrice()+" Tk");
                            Picasso.get().load(model.getLink()).into(holder.productImage);

                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    if (type.equals("admin")){
                                        Intent intent = new Intent(getApplicationContext(), AdminMaintainProductsActivity.class);
                                        intent.putExtra("id", model.getId());
                                        startActivity(intent);

                                    }else{
                                        Intent intent = new Intent(getApplicationContext(), ProductDetailActivity.class);
                                        intent.putExtra("id", model.getId());
                                        startActivity(intent);
                                    }
                                }
                            });
                        }else if (!status.equals("Approved")){
                            holder.productName.setVisibility(View.GONE);
                            holder.productDescription.setVisibility(View.GONE);
                            holder.productPrice.setVisibility(View.GONE);
                            holder.productImage.setVisibility(View.GONE);
                        }
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout, parent, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }
                };
        searchRecycler.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<ProductItemModel> options =
                new FirebaseRecyclerOptions.Builder<ProductItemModel>()
                //.setQuery(reference.orderByChild("lowerName").startAt(searchProduct), ProductItemModel.class).build();
                .setQuery(reference.orderByChild("status").equalTo("Approved"), ProductItemModel.class).build();

        FirebaseRecyclerAdapter<ProductItemModel, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<ProductItemModel, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull ProductItemModel model) {
                        holder.productName.setText(model.getName());
                        //holder.productDescription.setText(model.getDescription());
                        holder.productPrice.setText("Price: "+model.getPrice()+" Tk");
                        Picasso.get().load(model.getLink()).into(holder.productImage);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (type.equals("admin")){
                                    Intent intent = new Intent(getApplicationContext(), AdminMaintainProductsActivity.class);
                                    intent.putExtra("id", model.getId());
                                    startActivity(intent);

                                }else{
                                    Intent intent = new Intent(getApplicationContext(), ProductDetailActivity.class);
                                    intent.putExtra("id", model.getId());
                                    startActivity(intent);
                                }
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
        searchRecycler.setAdapter(adapter);
        adapter.startListening();
    }
}