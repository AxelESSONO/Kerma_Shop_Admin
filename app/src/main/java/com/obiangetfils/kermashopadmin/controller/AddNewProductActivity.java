package com.obiangetfils.kermashopadmin.controller;

import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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


    // get the Firebase  storage reference
    private FirebaseStorage storage;
    private StorageReference storageReference;
    DatabaseReference productsRef;

    private SearchableSpinner categorySearchSpinner;

    private Uri mImageUri;

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

        /** bind View **/
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

        /** bind View End **/

        /** Firebase Storage **/

        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        titleTxt.setText(R.string.add_new_product);
        initSpinnerCategory();
        //initSpinnerProductType();

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
        final StorageReference reference = storageReference.child("Product images" + "/" + category + "/" + ProductRandomKey);

        if (mArrayUri.size() > 0) {
            for (int i = 0; i < mArrayUri.size(); i++) {
                reference.putFile(mArrayUri.get(i)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                mUri.add(uri);
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddNewProductActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            SaveProductInfoToDatabase(category, productName, currentPrice,
                    oldPrice, quantity, productDescription, tagNew, tagOnSale);
        }
    }

    private void SaveProductInfoToDatabase(
            String category, String productName, String currentPrice, String oldPrice,
            String quantity, String productDescription, Boolean tagNew, Boolean tagOnSale) {

        productsRef = FirebaseDatabase.getInstance().getReference();
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

        productsRef.child("Products").child(ProductRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            Intent intent = new Intent(AddNewProductActivity.this, AdminHomeActivity.class);
                            startActivity(intent);

                            Toast.makeText(AddNewProductActivity.this, "Produit(s) ajouté(s) avec succès.", Toast.LENGTH_SHORT).show();

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
                keyCategories.add("Sélectionner la catégory");
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    keyCategories.add(dataSnapshot.getKey());
                }

                categorySearchSpinner.setTitle("Sélectionner la catégory");
                categorySearchSpinner.setPositiveButton("OK");

                categorySearchSpinner.setAdapter(new ArrayAdapter<>(AddNewProductActivity.this,
                        android.R.layout.simple_spinner_dropdown_item, keyCategories));

                categorySearchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        if (position == 0) {
                            Toast.makeText(AddNewProductActivity.this, "Veuillez sélectionner une Catégorie", Toast.LENGTH_SHORT).show();
                        } else {
                            String categoryName = parent.getItemAtPosition(position).toString();
                            Toast.makeText(AddNewProductActivity.this, categoryName, Toast.LENGTH_SHORT).show();
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
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Sélection"), PICK_IMAGE_MULTIPLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        try {
            // When an Image is picked
            if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK && data != null) {
                // Get the Image from data

                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                imagesEncodedList = new ArrayList<String>();
                if (data.getData() != null) {
                    mImageUri = data.getData();
                    // Get the cursor
                    Cursor cursor = getContentResolver().query(mImageUri, filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imageEncoded = cursor.getString(columnIndex);
                    cursor.close();
                    mArrayUri.add(mImageUri);
                    galleryAdapter = new AddProductAdapter(AddNewProductActivity.this, mArrayUri);
                    recyclerViewImage.setLayoutManager(new GridLayoutManager(this, 2));
                    recyclerViewImage.setAdapter(galleryAdapter);


                } else {
                    if (data.getClipData() != null) {
                        //ClipData
                        mClipData = data.getClipData();
                        Uri uri;
                        for (int i = 0; i < mClipData.getItemCount(); i++) {
                            ClipData.Item item = mClipData.getItemAt(i);
                            uri = item.getUri();
                            mArrayUri.add(uri);
                            /** Load tmpProductImage **/
                            tmpProductImage.put("image " + i, uri);
                            /** Load ends **/
                            // Get the cursor
                            Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                            // Move to first row
                            cursor.moveToFirst();
                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            imageEncoded = cursor.getString(columnIndex);
                            imagesEncodedList.add(imageEncoded);
                            cursor.close();
                        }

                        galleryAdapter = new AddProductAdapter(getApplicationContext(), mArrayUri);

                        if (mArrayUri.size() > 0) {
                            recyclerViewImage.setVisibility(View.VISIBLE);
                            noImageLoaded.setVisibility(View.GONE);
                            selectButton.setVisibility(View.GONE);
                            saveImage.setVisibility(View.VISIBLE);
                            recyclerViewImage.setLayoutManager(new GridLayoutManager(this, 2));
                            recyclerViewImage.setAdapter(galleryAdapter);
                            Toast.makeText(this, "" + mArrayUri.size() + "Images sélectionnée(s) ", Toast.LENGTH_SHORT).show();

                        } else {
                            recyclerViewImage.setVisibility(View.GONE);
                            noImageLoaded.setVisibility(View.VISIBLE);
                            selectButton.setVisibility(View.VISIBLE);
                            saveImage.setVisibility(View.GONE);
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Vous avez choisi aucune image", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            if (mArrayUri.size() >= 1) {
                Toast.makeText(this, "Images sélectionnée(s) " + mArrayUri.size(), Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}