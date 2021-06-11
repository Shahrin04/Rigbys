package com.example.rigbys.Viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rigbys.Interface.ItemClickListener;
import com.example.rigbys.R;

public class SellerProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView productName, productDescription, productPrice, productStatus;
    public ImageView productImage;
    public ItemClickListener itemClickListener;

    public SellerProductViewHolder(@NonNull View itemView) {
        super(itemView);
        productStatus = itemView.findViewById(R.id.seller_product_status);
        productName = itemView.findViewById(R.id.seller_product_name);
        productDescription = itemView.findViewById(R.id.seller_product_description);
        productPrice = itemView.findViewById(R.id.seller_product_price);
        productImage = itemView.findViewById(R.id.seller_product_image);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }
}