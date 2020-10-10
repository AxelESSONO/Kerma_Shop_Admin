package com.obiangetfils.kermashopadmin.controller;

import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.obiangetfils.kermashopadmin.R;
import com.obiangetfils.kermashopadmin.adapter.AddProductAdapter;
import com.obiangetfils.kermashopadmin.model.CategoryOBJ;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


public class AddNewProductActivity extends AppCompatActivity {

    private List<CategoryOBJ> categoryOBJArrayList = new ArrayList<>();
    private ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
    private HashMap<String, Object> tmpProductImage = new HashMap<>();
    List<String> imagesEncodedList;
    String imageEncoded;

    //private SearchableSpinner productTypeSpinner;

    private List<String> productTypeList;
    private ImageView noImage, ret, whatsappId, icFacebook, icGmail;
    private ImageView icPhone;

    private ImageView noImageLoaded;
    private RecyclerView recyclerViewImage;
    private Button selectButton, saveImage;

    int PICK_IMAGE_MULTIPLE = 1;
    private static final int IMAGE_CODE = 1;
    private AddProductAdapter galleryAdapter;
    private ClipData mClipData;
    private Toolbar toolbar;
    private EditText productNameEdt, productNewPriceEdt, productOldPriceEdt, productQuantityEdt;
    private EditText productDescriptionEdt;
    private TextView titleTxt;
    private CheckBox newness, sale;
    private ProgressBar progressBar;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private StorageReference mStorageRef;


    // get the Firebase  storage reference
    DatabaseReference productsRef;

    private SearchableSpinner categorySearchSpinner;

    private Uri mImageUri;


    private List<Uri> imageUri = new ArrayList<>();
    private List<String> imageName = new ArrayList<>();

    private List<Uri> mUri = new ArrayList<>();

    private String ProductRandomKey, downloadImageUrl;
    private String saveCurrentDate, saveCurrentTime;


    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int newSize = intent.getIntExtra("Size", -1);
            if (newSize != -1) {
                recyclerViewImage.setVisibility(View.GONE);
                noImageLoaded.setVisibility(View.VISIBLE);
                selectButton.setVisibility(View.VISIBLE);
                saveImage.setVisibility(View.GONE);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_product);

        bindView();

        /** Firebase Storage **/

        // get the Firebase  storage reference
        mStorageRef = FirebaseStorage.getInstance().getReference();
        productsRef = FirebaseDatabase.getInstance().getReference();

        titleTxt.setText(R.string.add_new_product);
        initSpinnerCategory();

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });

        saveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mArrayUri.size() > 0) {

                    String category, productName, currentPrice, oldPrice, quantity, productDescription;
                    Boolean tagNew, tagOnSale;
                    category = categorySearchSpinner.getSelectedItem().toString().trim();
                    productName = productNameEdt.getText().toString().trim();
                    currentPrice = productNewPriceEdt.getText().toString().trim();
                    oldPrice = productOldPriceEdt.getText().toString().trim();
                    quantity = productQuantityEdt.getText().toString().trim();
                    productDescription = productDescriptionEdt.getText().toString().trim();
                    tagNew = newness.isChecked();
                    tagOnSale = sale.isChecked();

                    checkIfIsNotEmpty(category, productName, currentPrice, oldPrice, quantity, productDescription, tagNew, tagOnSale, mArrayUri);

                } else {
                    Toast.makeText(AddNewProductActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddNewProductActivity.this, AdminHomeActivity.class));
            }
        });

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-message"));

    }

    private void checkIfIsNotEmpty(String category, String productName, String currentPrice, String oldPrice, String quantity,
                                   String productDescription, Boolean tagNew, Boolean tagOnSale, ArrayList<Uri> mArrayUri) {

        if (category.isEmpty() || productName.isEmpty() || currentPrice.isEmpty() || oldPrice.isEmpty() ||
            quantity.isEmpty() || productDescription.isEmpty() || tagNew == false || tagOnSale == false) {

            Toast.makeText(this, "Veuillez remplir tous les champs!", Toast.LENGTH_SHORT).show();
        } else {
            upLoadIntoDataBase(category, productName, currentPrice, oldPrice, quantity, productDescription, tagNew, tagOnSale, mArrayUri);
        }

    }

    private void upLoadIntoDataBase(
            final String category, final String productName, final String currentPrice, final String oldPrice, final String quantity,
            final String productDescription, final Boolean tagNew, final Boolean tagOnSale, final ArrayList<Uri> pArrayUri) {

        ProductRandomKey = UUID.randomUUID().toString();

        if (mArrayUri.size() > 0) {

            final HashMap<String, Object> productImage = new HashMap<>();

            for (int i = 0; i < imageName.size(); i++) {

                final StorageReference mRef = mStorageRef.child("Product Images")
                        .child(categorySearchSpinner.getSelectedItem().toString())
                        .child(imageName.get(i));

                final int finalI = i;
                mRef.putFile(imageUri.get(i)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        mRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                mUri.add(uri);

                                productImage.put("product_" + finalI, uri.toString());
                                productsRef.child("Products")
                                        .child(category)
                                        .child(ProductRandomKey)
                                        .child("ImagesProducts").updateChildren(productImage);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddNewProductActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddNewProductActivity.this, "Fail" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }

            SaveProductInfoToDatabase(category, productName, currentPrice, oldPrice, quantity, productDescription, tagNew, tagOnSale);
        }
    }

    private void SaveProductInfoToDatabase(
            final String category, String productName, String currentPrice, String oldPrice,
            String quantity, String productDescription, Boolean tagNew, Boolean tagOnSale) {

        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid", ProductRandomKey);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("description", productDescription);
        productMap.put("image", downloadImageUrl);
        productMap.put("category", category);
        productMap.put("currentPrice", currentPrice);
        productMap.put("oldPrice", oldPrice);
        productMap.put("pname", productName);
        productMap.put("quantity", quantity);
        productMap.put("tagNew", tagNew);
        productMap.put("tagOnSale", tagOnSale);

        productsRef.child("Products").child(category).child(ProductRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            Intent intent = new Intent(AddNewProductActivity.this, AdminHomeActivity.class);
                            startActivity(intent);

                        } else {
                            String message = task.getException().toString();
                            Toast.makeText(AddNewProductActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void initSpinnerCategory() {

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        databaseReference.child("Categories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // List to store admin Key
                List<String> keyCategories = new ArrayList<>();
                keyCategories.add("Sélectionner une catégorie");
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    keyCategories.add(dataSnapshot.getKey());
                }

                categorySearchSpinner.setTitle("Sélectionner une catégorie");
                categorySearchSpinner.setPositiveButton("OK");

                categorySearchSpinner.setAdapter(new ArrayAdapter<>(AddNewProductActivity.this,
                        android.R.layout.simple_spinner_dropdown_item, keyCategories));

                categorySearchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        if (position == 0) {
                            Toast.makeText(AddNewProductActivity.this, "Veuillez sélectionner une catégorie", Toast.LENGTH_SHORT).show();
                        } else {
                            String categoryName = parent.getItemAtPosition(position).toString();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void pickImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, IMAGE_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_CODE && resultCode == RESULT_OK) {

            if (data.getClipData() != null) {

                int totalitem = data.getClipData().getItemCount();

                for (int i = 0; i < totalitem; i++) {

                    imageUri.add(data.getClipData().getItemAt(i).getUri());
                    imageName.add(getFileName(imageUri.get(i)));
                    mArrayUri.add(data.getClipData().getItemAt(i).getUri());
/*
                    final StorageReference mRef = mStorageRef.child("Product Images")
                            .child(categorySearchSpinner.getSelectedItem().toString())
                            .child(imagename);

                    mRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(AddNewProductActivity.this, "Image(s) sauvegardée(s) dans la base de données",
                                    Toast.LENGTH_SHORT).show();

                            mRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    mUri.add(uri);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AddNewProductActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddNewProductActivity.this, "Fail" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });*/
                }

                galleryAdapter = new AddProductAdapter(AddNewProductActivity.this, mArrayUri);
                if (mArrayUri.size() > 0) {
                    recyclerViewImage.setVisibility(View.VISIBLE);
                    noImageLoaded.setVisibility(View.GONE);
                    selectButton.setVisibility(View.GONE);
                    saveImage.setVisibility(View.VISIBLE);
                    recyclerViewImage.setLayoutManager(new GridLayoutManager(this, 2));
                    recyclerViewImage.setAdapter(galleryAdapter);

                    Toast.makeText(this, "" + mArrayUri.size() + " image(s) sélectionnée(s) ", Toast.LENGTH_SHORT).show();

                } else {
                    recyclerViewImage.setVisibility(View.GONE);
                    noImageLoaded.setVisibility(View.VISIBLE);
                    selectButton.setVisibility(View.VISIBLE);
                    saveImage.setVisibility(View.GONE);
                }

            } else if (data.getData() != null) {
                Toast.makeText(this, "single", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    /**
     * bind View
     **/
    private void bindView() {

        productNameEdt = (EditText) findViewById(R.id.productName);
        productNewPriceEdt = (EditText) findViewById(R.id.productNewPrice);
        productOldPriceEdt = (EditText) findViewById(R.id.productOldPrice);
        productQuantityEdt = (EditText) findViewById(R.id.productQuantity);
        productDescriptionEdt = (EditText) findViewById(R.id.productDescriptionEdt);

        // Button
        selectButton = (Button) findViewById(R.id.select_image);
        saveImage = (Button) findViewById(R.id.save_image);

        // Recyclerview
        recyclerViewImage = (RecyclerView) findViewById(R.id.recyclerViewAddNewProduct);

        //Toolbar
        toolbar = (Toolbar) findViewById(R.id.include_1);

        //ImageView
        ret = (ImageView) toolbar.findViewById(R.id.ret);
        noImageLoaded = (ImageView) findViewById(R.id.noImageLoaded);
        whatsappId = (ImageView) findViewById(R.id.whatsappId);
        icFacebook = (ImageView) findViewById(R.id.icFacebook);
        icGmail = (ImageView) findViewById(R.id.icGmail);
        icPhone = (ImageView) findViewById(R.id.icPhone);

        //TextView
        titleTxt = (TextView) toolbar.findViewById(R.id.toolbar_title);

        //Spinners
        //productTypeSpinner = findViewById(R.id.SearchableProductType);
        categorySearchSpinner = findViewById(R.id.SearchableSpinnerCategory);

        // Checkbox
        newness = (CheckBox) findViewById(R.id.newness);
        sale = (CheckBox) findViewById(R.id.sale);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }
}