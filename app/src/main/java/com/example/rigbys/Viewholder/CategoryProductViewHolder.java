package com.example.rigbys.Viewholder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rigbys.Interface.ItemClickListener;
import com.example.rigbys.Model.ProductItemModel;
import com.example.rigbys.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView name, price;
    public ImageView image;
    public ItemClickListener itemClickListener;

    public CategoryProductViewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.category_product_name);
        price = itemView.findViewById(R.id.category_product_price);
        image = itemView.findViewById(R.id.category_product_image);
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }
}
