package com.experiment.hts.storedecor.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterViewFlipper;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.experiment.hts.storedecor.adapter.ProductAdapter;
import com.experiment.hts.storedecor.R;
import com.experiment.hts.storedecor.adapter.SlideshowAdapter;
import com.experiment.hts.storedecor.model.Product;
import com.experiment.hts.storedecor.model.SlideImage;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ProductController {

    Context context;
    Product product;
    ProductAdapter productAdapter;
    int allitem = 8;
    HomeInterface homeInterface;
    List<Product> productList;
    List<String> list;
    SlideImage slideImage;
    SlideshowAdapter adapter;

    public ProductController(Context context) {
        this.context = context;
        product = new Product();
        list = new ArrayList<>();
        slideImage = new SlideImage();
    }

    public void getListProductController(final Context context, final RecyclerView recyclerView, NestedScrollView nestedScrollView, final ProgressBar progressBar, final TextView textView, final SwipeRefreshLayout swipeRefreshLayout) {
        setItemProduct(recyclerView, progressBar, textView);

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (v.getChildAt(v.getChildCount() - 1) != null) {
                    if (scrollY >= (v.getChildAt(v.getChildCount() - 1)).getMeasuredHeight() - v.getMeasuredHeight()) {
                        allitem += 8;
                        product.getListProduct(homeInterface, allitem, allitem - 8);
                        progressBar.setVisibility(View.VISIBLE);
                        if (allitem > productList.size()) {
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                }
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        product = new Product();
                        setItemProduct(recyclerView, progressBar, textView);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2500);
            }
        });

        product.getListProduct(homeInterface, allitem, 0);
    }

    public void setItemProduct(RecyclerView recyclerView, final ProgressBar progressBar, final TextView textView) {
        productList = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 2);
        recyclerView.setLayoutManager(layoutManager);
        productAdapter = new ProductAdapter(context, productList, R.layout.item_product);
        recyclerView.setAdapter(productAdapter);
        homeInterface = new HomeInterface() {
            @Override
            public void getListProduct(final Product product) {
                final List<Bitmap> bitmapList = new ArrayList<>();

                for (String linkimg : product.getImgproductlist()) {
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("hinhanhs").child(linkimg);
                    long ONE_MEGABYTE = 1024 * 1024;
                    storageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            bitmapList.add(bitmap);
                            product.setBitmapList(bitmapList);

                            if (product.getBitmapList().size() == product.getImgproductlist().size()) {
                                productList.add(product);
                                productAdapter.notifyDataSetChanged();
                                progressBar.setVisibility(View.GONE);
                                textView.setText(R.string.all);
                                textView.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }
            }
        };
    }

    public void getSlideShow(final AdapterViewFlipper adapterViewFlipper) {
        final List<Bitmap> bitmapList = new ArrayList<>();
        adapter = new SlideshowAdapter(bitmapList, slideImage, context);
        adapterViewFlipper.setAdapter(adapter);
        adapterViewFlipper.setInAnimation(context, R.animator.slide_right_in);
        adapterViewFlipper.setOutAnimation(context, R.animator.slide_left_out);
        adapterViewFlipper.setFlipInterval(5000);
        adapterViewFlipper.startFlipping();
        FirebaseDatabase.getInstance().getReference().child("slides").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshotSlide : dataSnapshot.getChildren()) {
                    list.add(dataSnapshotSlide.getValue(String.class));
                    slideImage.setStringList(list);
                    for (String linkimg : slideImage.getStringList()) {
                        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("tienichs").child(linkimg);
                        long ONE_MEGABYTE = 1024 * 1024;
                        storageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                                bitmapList.add(bitmap);
                                if (bitmapList.size() == slideImage.getStringList().size()) {
                                    slideImage.setBitmapList(bitmapList);
                                    adapter.notifyDataSetChanged();
                                    adapterViewFlipper.deferNotifyDataSetChanged();
                                }
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
