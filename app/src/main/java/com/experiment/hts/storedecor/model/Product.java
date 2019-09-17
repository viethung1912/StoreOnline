package com.experiment.hts.storedecor.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import com.experiment.hts.storedecor.controller.HomeInterface;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Product implements Parcelable {

    String idproduct;
    String nameproduct;
    long price;
    String description;
    float point;
    List<String> imgproductlist;
    List <Bitmap> bitmapList;
    private DatabaseReference databaseReference;
    private DataSnapshot dataroot;

    public Product() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    protected Product(Parcel in) {
        idproduct = in.readString();
        nameproduct = in.readString();
        price = in.readLong();
        description = in.readString();
        point = in.readFloat();
        imgproductlist = in.createStringArrayList();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public String getIdproduct() {
        return idproduct;
    }

    public void setIdproduct(String idproduct) {
        this.idproduct = idproduct;
    }

    public String getNameproduct() {
        return nameproduct;
    }

    public void setNameproduct(String nameproduct) {
        this.nameproduct = nameproduct;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPoint() {
        return point;
    }

    public void setPoint(float point) {
        this.point = point;
    }

    public List<String> getImgproductlist() {
        return imgproductlist;
    }

    public void setImgproductlist(List<String> imgproductlist) {
        this.imgproductlist = imgproductlist;
    }

    public List<Bitmap> getBitmapList() {
        return bitmapList;
    }

    public void setBitmapList(List<Bitmap> bitmapList) {
        this.bitmapList = bitmapList;
    }

    //lay danh sach san pham
    public void getListProduct(final HomeInterface homeInterface, final int nextitem, final int allitem) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataroot = dataSnapshot;
                getListProduct(dataroot, homeInterface, nextitem, allitem);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        if (dataroot != null) {
            getListProduct(dataroot, homeInterface, nextitem, allitem);
        } else {
            databaseReference.addListenerForSingleValueEvent(valueEventListener);
        }
    }

    public void getListProduct(DataSnapshot dataSnapshot, final HomeInterface homeInterface, int nextitem, int allitem) {

        DataSnapshot dataSnapshotProduct = dataSnapshot.child("products");

        //Xử lý load more
        int i = 0;
        for (DataSnapshot valueProduct : dataSnapshotProduct.getChildren()) {
            if (i == nextitem) {
                break;
            }
            if (i < allitem) {
                i++;
                continue;
            }
            i++;
            Product product = valueProduct.getValue(Product.class);
            product.setIdproduct(valueProduct.getKey());

            //Lay danh sach hinh anh theo ma products
            DataSnapshot dataSnapshotImgProduct = dataSnapshot.child("imgproducts").child(valueProduct.getKey());
            imgproductlist = new ArrayList<>();
            for (DataSnapshot valueImgProduct: dataSnapshotImgProduct.getChildren()) {
                imgproductlist.add(valueImgProduct.getValue(String.class));
            }
            product.setImgproductlist(imgproductlist);

            homeInterface.getListProduct(product);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idproduct);
        dest.writeString(nameproduct);
        dest.writeLong(price);
        dest.writeString(description);
        dest.writeFloat(point);
        dest.writeStringList(imgproductlist);
    }
}
