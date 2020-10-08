package com.obiangetfils.kermashopadmin.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.obiangetfils.kermashopadmin.R;
import com.obiangetfils.kermashopadmin.adapter.HomeAdapter;
import com.obiangetfils.kermashopadmin.model.HomeObject;

import java.util.ArrayList;
import java.util.List;

public class AdminHomeActivity extends AppCompatActivity {

    private Button logoutBtn;
    private RecyclerView recyclerHome;
    private List<HomeObject> homeObjectList;
    private HomeAdapter homeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        logoutBtn = (Button) findViewById(R.id.logout);

        homeObjectList = new ArrayList<>();

        /** Load homeList **/
        homeObjectList.add(new HomeObject("Ajouter un nouveau produit", R.drawable.ic_control_point_black_24dp));
        homeObjectList.add(new HomeObject("Ajouter une nouvelle catégorie", R.drawable.ic_add_category_24dp));
        homeObjectList.add(new HomeObject("Modifier un produit", R.drawable.ic_settings_black_24dp));
        homeObjectList.add(new HomeObject("Gestion des commandes", R.drawable.ic_orders));
        homeObjectList.add(new HomeObject("Gestion des nouveaux produits", R.drawable.ic_stock));
        homeObjectList.add(new HomeObject("Gestion des utilisateurs", R.drawable.ic_group));
        homeObjectList.add(new HomeObject("Gestion des Slides", R.drawable.ic_slideshow));
        homeObjectList.add(new HomeObject("Gestions des recettes", R.drawable.ic_accounting));

        homeAdapter = new HomeAdapter(homeObjectList, this);

        /** Init RecyclerView **/
        recyclerHome = (RecyclerView) findViewById(R.id.recyclerHome);
        recyclerHome.setHasFixedSize(false);
        recyclerHome.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerHome.setNestedScrollingEnabled(false);
        recyclerHome.setAdapter(homeAdapter);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }
}