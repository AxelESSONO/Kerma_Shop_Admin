package com.obiangetfils.kermashopadmin.controller;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.obiangetfils.kermashopadmin.R;
import com.obiangetfils.kermashopadmin.adapter.GalleryAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddStoryActivity extends AppCompatActivity {

    private String saveCurrentDate, saveCurrentTime;
    private Button addNewProductButton;
    private ImageView ret, no_images;
    private static final int GALLERY_PICK = 1;
    private String productRandomKey;
    private StorageReference productImagesRef;
    private DatabaseReference productsRef;
    private ProgressDialog loadingBar;
    private Button choiceButton;
    int PICK_IMAGE_MULTIPLE = 1;
    String imageEncoded;
    List<String> imagesEncodedList;
    private GridView gvGallery;
    private GalleryAdapter galleryAdapter;
    private ClipData mClipData;
    private HashMap<String, Object> tmpProductImage = new HashMap<>();
    private ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
    private int upLoad_Count = 0;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_story);

        productImagesRef = FirebaseStorage.getInstance().getReference().child("Ads");
        productsRef = FirebaseDatabase.getInstance().getReference().child("Story");
        addNewProductButton = (Button) findViewById(R.id.add_new_product);

        choiceButton = (Button) findViewById(R.id.images_choice);
        gvGallery = (GridView) findViewById(R.id.gv);
        loadingBar = new ProgressDialog(this);
        ret = (ImageView) findViewById(R.id.ret);
        no_images = (ImageView) findViewById(R.id.no_images);

        ret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AddStoryActivity.this, AdminHomeActivity.class);
                startActivity(intent);
            }
        });
    }
}