package com.experiment.hts.storedecor.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.TransactionTooLargeException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.experiment.hts.storedecor.R;
import com.experiment.hts.storedecor.model.Product;
import com.experiment.hts.storedecor.view.ProductDetailActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolderProduct> {

    Context context;
    int resource;
    List<Product> productList;

    public ProductAdapter(Context context, List<Product> productList, int resource) {
        this.context = context;
        this.productList = productList;
        this.resource = resource;
    }

    @NonNull
    @Override
    public ViewHolderProduct onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        ViewHolderProduct viewHolder = new ViewHolderProduct(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderProduct holder, int position) {
        final Product product = productList.get(position);
        holder.txtNameProduct.setText(product.getNameproduct());
        holder.txtPriceProduct.setText(product.getPrice() + "đ");
        holder.ratingbarPointProduct.setRating(product.getPoint());
        holder.imgProduct.setImageResource(R.drawable.test);

        if (product.getBitmapList().size() > 0) {
            holder.imgProduct.setImageBitmap(product.getBitmapList().get(0));
        }

        if (Build.VERSION.SDK_INT >= 24) {
            holder.cardviewContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ProductDetailActivity.class);
                    intent.putExtra("product", product);
                    context.startActivity(intent);
                }
            });
        }
        else {
            Log.e("kiemtra", Build.VERSION.SDK_INT + "");
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolderProduct extends RecyclerView.ViewHolder {
        TextView txtNameProduct, txtPriceProduct;
        RatingBar ratingbarPointProduct;
        ImageView imgProduct;
        RecyclerView recyclerProducts;
        CardView cardviewContainer;
        public ViewHolderProduct(@NonNull View itemView) {
            super(itemView);
            txtNameProduct = itemView.findViewById(R.id.txtNameProduct);
            txtPriceProduct = itemView.findViewById(R.id.txtPriceProduct);
            ratingbarPointProduct = itemView.findViewById(R.id.ratingbarPointProduct);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            recyclerProducts = itemView.findViewById(R.id.recyclerProducts);
            cardviewContainer = itemView.findViewById(R.id.cardviewContainer);
        }
    }
}
