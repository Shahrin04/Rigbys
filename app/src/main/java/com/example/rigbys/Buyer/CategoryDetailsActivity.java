package com.example.rigbys.Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.example.rigbys.Admin.AdminMaintainProductsActivity;
import com.example.rigbys.Model.ProductItemModel;
import com.example.rigbys.R;
import com.example.rigbys.Viewholder.CategoryProductViewHolder;
import com.example.rigbys.Viewholder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class CategoryDetailsActivity extends AppCompatActivity {

    //private GridView categoryGridView;
    private String category = "";
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private DatabaseReference reference;
    private String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_details);

//        //for admin access to maintain products//
//        Intent intent = getIntent();
//        Bundle bundle = intent.getExtras();
//        if (bundle!=null){
//            type = getIntent().getExtras().get("admin").toString();
//        }
//        //for admin access to maintain products//

//        type = getIntent().getExtras().get("admin").toString();
//        category = getIntent().getStringExtra("category");

        Bundle extras = getIntent().getExtras();
        category = extras.getString("category");
        type = extras.getString("admin");

        reference = FirebaseDatabase.getInstance().getReference().child("Products");

        recyclerView = findViewById(R.id.category_product_recycler_view);
        recyclerView.setHasFixedSize(true);
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        //categoryGridView = findViewById(R.id.category_product_grid_view);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<ProductItemModel> options =
                new FirebaseRecyclerOptions.Builder<ProductItemModel>().setQuery(reference
                        .orderByChild("trick").equalTo(category+"Approved"), ProductItemModel.class).build();

        FirebaseRecyclerAdapter<ProductItemModel, CategoryProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<ProductItemModel, CategoryProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull CategoryProductViewHolder holder, int position, @NonNull ProductItemModel model) {
                        holder.name.setText(model.getName());
                        holder.price.setText("Price: "+model.getPrice()+" Tk");
                        Picasso.get().load(model.getLink()).into(holder.image);

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
                    public CategoryProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_product_item, parent, false);
                        CategoryProductViewHolder holder = new CategoryProductViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}