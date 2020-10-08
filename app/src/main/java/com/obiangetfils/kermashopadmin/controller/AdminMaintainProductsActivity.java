package com.obiangetfils.kermashopadmin.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.obiangetfils.kermashopadmin.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminMaintainProductsActivity extends AppCompatActivity {

    DatabaseReference productsRef;
    FirebaseDatabase firebaseDatabase;
    private Button applyChangesBtn, deleteBtn;
    private EditText name, price, description;
    private ImageView imageView;
    private String productID = "";
    private Toolbar toolbar;
    private ImageView ret;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain_products);

        toolbar = (Toolbar) findViewById(R.id.include_1);
        ret = toolbar.findViewById(R.id.ret);
        ret.setVisibility(View.VISIBLE);


        //productID = getIntent().getStringExtra("pid");
        productID = "-1723273329";
        firebaseDatabase = FirebaseDatabase.getInstance();
        productsRef = firebaseDatabase.getReference();

        applyChangesBtn = (Button) findViewById(R.id.apply_changes_btn);
        deleteBtn = (Button) findViewById(R.id.delete_product_btn);
        name = (EditText) findViewById(R.id.product_name_maintain);
        price = (EditText) findViewById(R.id.product_price_maintain);
        description = (EditText) findViewById(R.id.product_description_maintain);
        imageView = (ImageView) findViewById(R.id.product_image_maintain);

        displaySpecificProductInfo();

        applyChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyChanges();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                deleteThisProduct();
            }
        });

        ret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminMaintainProductsActivity.this, AdminHomeActivity.class));
            }
        });

    }

    private void deleteThisProduct() {

    }

    private void applyChanges() {

    }

    private void displaySpecificProductInfo() {

    }
}