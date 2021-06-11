package com.example.rigbys.Seller;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.rigbys.Buyer.HomeActivity;
import com.example.rigbys.Buyer.ProductDetailActivity;
import com.example.rigbys.Model.ProductItemModel;
import com.example.rigbys.R;
import com.example.rigbys.Seller.SellerHomeActivity;
import com.example.rigbys.Viewholder.SellerProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


public class SellerHomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private String imageLink = "", imageName = "";

    private DatabaseReference reference;
    private StorageReference storageRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_home, container, false);

        recyclerView = view.findViewById(R.id.seller_home_product_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        reference = FirebaseDatabase.getInstance().getReference().child("Products");
        storageRef = FirebaseStorage.getInstance().getReference().child("Product Image");


        // Inflate the layout for this fragment
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<ProductItemModel> options
                = new FirebaseRecyclerOptions.Builder<ProductItemModel>()
                .setQuery(reference.orderByChild("sid").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()), ProductItemModel.class).build();

        FirebaseRecyclerAdapter<ProductItemModel, SellerProductViewHolder> adapter
                = new FirebaseRecyclerAdapter<ProductItemModel, SellerProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull SellerProductViewHolder holder, int position, @NonNull ProductItemModel model) {
                imageName = model.getImage();
                imageLink = model.getLink();
                holder.productStatus.setText("Status: "+model.getStatus());
                holder.productName.setText(model.getName());
                holder.productDescription.setText(model.getDescription());
                holder.productPrice.setText("Price: "+model.getPrice()+" TK");
                Picasso.get().load(model.getLink()).into(holder.productImage);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String productId = getRef(position).getKey();
                        CharSequence option[] = new CharSequence[]{
                                "Edit",
                                "No"
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Want to edit this Product ?");
                        builder.setItems(option, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which==0){
                                    Intent intent = new Intent(getActivity().getApplicationContext(), SellerMaintainProductActivity.class);
                                    intent.putExtra("id", model.getId());
                                    intent.putExtra("status", model.getStatus());
                                    startActivity(intent);
//                                    if (model.getStatus().equals("Not Approved")){
//                                        Intent intent = new Intent(getActivity().getApplicationContext(), SellerMaintainProductActivity.class);
//                                        intent.putExtra("id", model.getId());
//                                        startActivity(intent);
//                                    }else if (model.getStatus().equals("Approved")){
//                                        Intent intent = new Intent(getActivity().getApplicationContext(), SellerMaintainProductActivity.class);
//                                        intent.putExtra("id", model.getId());
//                                        intent.putExtra("status", model.getStatus());
//                                        startActivity(intent);
//                                    }

                                    //deleteProducts(productId);
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
            public SellerProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_product_item, parent, false);
                SellerProductViewHolder holder = new SellerProductViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }


//    private void deleteProducts(String productId) {
//        //storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(imageLink);
//
//        storageRef.child(imageName).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if (task.isSuccessful()){
//                    Toast.makeText(getActivity().getApplicationContext(), "Image from the storage deleted successfully", Toast.LENGTH_SHORT).show();
//
//                    reference.child(productId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if (task.isSuccessful()){
//                                Toast.makeText(getActivity().getApplicationContext(), "Product has been deleted successfully", Toast.LENGTH_SHORT).show();
//                            }else {
//                                Toast.makeText(getActivity().getApplicationContext(), "Error: "+task.getException().getMessage(), Toast.LENGTH_LONG).show();
//                            }
//                        }
//                    });
//
//                }else {
//                    Toast.makeText(getActivity().getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//    }
}