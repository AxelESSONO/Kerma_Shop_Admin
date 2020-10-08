package com.obiangetfils.kermashopadmin.controller;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
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
import com.obiangetfils.kermashopadmin.Prevalent.Prevalent;
import com.obiangetfils.kermashopadmin.R;

import java.util.HashMap;

public class AddCategoryActivity extends AppCompatActivity {

    private static final String CAT_URI = "Category uri";
    private static final String URI = "uri";
    private ImageView imageCat;
    private EditText categoryName;
    private Button saveNewCat;
    static int PReqCode = 1;
    static int REQUESCODE = 1;
    Uri pickedImgUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        imageCat = (ImageView) findViewById(R.id.image_category);
        categoryName = (EditText) findViewById(R.id.category_name_edt);
        saveNewCat = (Button) findViewById(R.id.category_add);

        imageCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= 22) {
                    checkAndRequestForPermission();
                } else {
                    openGallery();
                }
            }
        });

        saveNewCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String catName = categoryName.getText().toString();
                if (TextUtils.isEmpty(catName)) {
                    Toast.makeText(AddCategoryActivity.this, "Veuillez saisir le nom de la catégorie", Toast.LENGTH_SHORT).show();
                } else {

                    Context context = AddCategoryActivity.this;
                    SharedPreferences sharedPrefGet = context.getSharedPreferences(CAT_URI, MODE_PRIVATE);
                    String image = sharedPrefGet.getString(URI, "R.drawable.profile");
                    Uri tmpUri = Uri.parse(image);
                    updateUserInfo(tmpUri);
                    sharedPrefGet.edit().clear();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null) {
            // the user has successfully picked an image
            // we need to save its reference to a Uri variable
            pickedImgUri = data.getData();
            imageCat.setImageURI(pickedImgUri);

            /** Share preference to retreive image URI **/
            Context context = AddCategoryActivity.this;
            SharedPreferences sharedPref = context.getSharedPreferences(CAT_URI, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(URI, String.valueOf(pickedImgUri));
            editor.commit();

            //updateUserInfo(pickedImgUri);
        }

    }

    private void updateUserInfo(Uri pickedImgUri) {

        // first we need to upload user photo to firebase storage and get url
        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("Category Images");
        final StorageReference imageFilePath = mStorage.child(pickedImgUri.getLastPathSegment());
        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                // image uploaded succesfully
                // now we can get our image url
                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(final Uri uri) {

                        final String nameCat = categoryName.getText().toString();

                        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                        reference.child("Categories")
                                .child(nameCat)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        if (!dataSnapshot.child("Categories").child(nameCat).exists()) {

                                            HashMap<String, Object> adminHashMap = new HashMap<>();
                                            adminHashMap.put("adminId", Prevalent.currentOnLineAdmin.getAdmin_phone());
                                            adminHashMap.put("image", uri.toString());
                                            adminHashMap.put("name", categoryName.getText().toString());
                                            reference.child("Categories")
                                                    .child(nameCat)
                                                    .updateChildren(adminHashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(AddCategoryActivity.this, "Catégorie ajoutée avec succès", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(AddCategoryActivity.this, AdminHomeActivity.class));
                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                    }
                                });
                    }
                });
            }
        });
    }

    private void checkAndRequestForPermission() {

        if (ContextCompat.checkSelfPermission(AddCategoryActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(AddCategoryActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(AddCategoryActivity.this, "Veuillez accepter les permissions requises", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(AddCategoryActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }
        } else
            openGallery();
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUESCODE);
    }
}