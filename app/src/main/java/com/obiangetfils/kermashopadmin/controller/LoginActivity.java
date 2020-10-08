package com.obiangetfils.kermashopadmin.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.obiangetfils.kermashopadmin.Prevalent.Prevalent;
import com.obiangetfils.kermashopadmin.R;
import com.obiangetfils.kermashopadmin.model.AdminModel;
import com.rey.material.widget.CheckBox;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEdt;
    private EditText passwordEdt;
    private Button loginBtn;
    private FirebaseAuth mAuth;
    private CheckBox rememberMeChk;
    private TextView forgetPasswordLink;
    private String email, password;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Bind view
        emailEdt = (EditText) findViewById(R.id.login_phone_number_input);
        passwordEdt = (EditText) findViewById(R.id.login_user_password);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        rememberMeChk = (CheckBox) findViewById(R.id.rememberMeChk);
        forgetPasswordLink = (TextView) findViewById(R.id.forgetPasswordLink);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth = FirebaseAuth.getInstance();
                email = emailEdt.getText().toString().trim();
                password = passwordEdt.getText().toString().trim();
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Email ou Mot de Passe invalide", Toast.LENGTH_SHORT).show();
                } else {
                    login(email, password);
                }
            }
        });
    }

    private void login(final String email, final String password) {

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        databaseReference.child("Admins").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // List to store admin Key
                List<String> keyAdminList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    keyAdminList.add(dataSnapshot.getKey());
                }

                List<AdminModel> adminModelList = new ArrayList<>();

                for (int i = 0; i < keyAdminList.size(); i++) {
                    String admin_firstname, admin_Name, admin_password, admin_phone, admin_email;
                    admin_firstname = snapshot.child(keyAdminList.get(i)).child("adminFirstName").getValue(String.class);
                    admin_Name = snapshot.child(keyAdminList.get(i)).child("adminName").getValue(String.class);
                    admin_password = snapshot.child(keyAdminList.get(i)).child("adminPassword").getValue(String.class);
                    admin_phone = snapshot.child(keyAdminList.get(i)).child("adminPhoneNumber").getValue(String.class);
                    admin_email = snapshot.child(keyAdminList.get(i)).child("adminEmail").getValue(String.class);
                    AdminModel adminModel = new AdminModel(admin_firstname, admin_Name, admin_password, admin_phone, admin_email);

                    if (email.equals(admin_email) && password.equals(admin_password)){

                        if (rememberMeChk.isChecked()){
                            SharedPreferences preferences = getSharedPreferences("REMEMBER_ME", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putBoolean("ADMIN_IS_CONNECTED", true);
                            editor.commit();
                        }

                        Prevalent.currentOnLineAdmin = adminModel;
                        Intent intent = new Intent(LoginActivity.this, AdminHomeActivity.class);
                        startActivity(intent);

                        return;
                    }

                    adminModelList.add(adminModel);

                    if(i == (keyAdminList.size() - 1) && !adminModelList.contains(admin_email) && !adminModelList.contains(admin_password)){
                        Toast.makeText(LoginActivity.this, "Cet administrateur n'existe pas.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}