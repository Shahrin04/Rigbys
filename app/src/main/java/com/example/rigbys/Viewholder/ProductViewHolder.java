package com.example.rigbys.Viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rigbys.Interface.ItemClickListener;
import com.example.rigbys.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView productName, productDescription, productPrice;
    public ImageView productImage;
    public ItemClickListener itemClickListener;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);
        productName = itemView.findViewById(R.id.admin_product_maintain_name);
        productDescription = itemView.findViewById(R.id.admin_product_maintain_description);
        productPrice = itemView.findViewById(R.id.admin_product_maintain_price);
        productImage = itemView.findViewById(R.id.admin_product_maintain_image);
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }
}
