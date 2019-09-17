package com.experiment.hts.storedecor.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.experiment.hts.storedecor.R;
import com.experiment.hts.storedecor.model.Product;

public class ProductDetailActivity extends AppCompatActivity {

    TextView txtContainer;
    Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        txtContainer = findViewById(R.id.txtContainer);

        product = getIntent().getParcelableExtra("product");

        //set text
        txtContainer.setText(product.getNameproduct());
    }
}
