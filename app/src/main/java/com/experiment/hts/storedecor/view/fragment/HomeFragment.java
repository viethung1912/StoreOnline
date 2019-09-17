package com.experiment.hts.storedecor.view.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterViewFlipper;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.experiment.hts.storedecor.adapter.SlideshowAdapter;
import com.experiment.hts.storedecor.controller.ProductController;
import com.experiment.hts.storedecor.R;
import com.experiment.hts.storedecor.model.SlideImage;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    public static final String TAG = "HomeFragment";

    ProductController productController;
    ProgressBar progressbarLoadProduct;
    NestedScrollView nestedScrollViewContainerHome;
    TextView txtAll;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerProducts;
    //Slideshow
    AdapterViewFlipper avfSlideshow;
    //Find product
    EditText edtFindProduct;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        //Slide with ViewFlipper
        //Slide data
        //Slide init
        avfSlideshow = root.findViewById(R.id.avfSlideshow);
        //Slide setting
        edtFindProduct=root.findViewById(R.id.edtFindProduct);
        recyclerProducts = root.findViewById(R.id.recyclerProducts);
        progressbarLoadProduct = root.findViewById(R.id.progressbarLoadProduct);
        nestedScrollViewContainerHome = root.findViewById(R.id.nestedScrollViewContainerHome);
        txtAll = root.findViewById(R.id.txtAll);
        swipeRefreshLayout = root.findViewById(R.id.swipeRefreshLayout);

        FindProduct();
        productController = new ProductController(getActivity());
        productController.getListProductController(getActivity(), recyclerProducts, nestedScrollViewContainerHome, progressbarLoadProduct, txtAll, swipeRefreshLayout);
        productController.getSlideShow(avfSlideshow);

        return root;
    }

    private void FindProduct() {
        edtFindProduct.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (avfSlideshow != null && !avfSlideshow.isFlipping())
            avfSlideshow.startFlipping();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (avfSlideshow != null && avfSlideshow.isFlipping())
            avfSlideshow.stopFlipping();
    }
}
