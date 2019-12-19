package com.example.rate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class AdminActivity extends AppCompatActivity {
    Button btnLogout,btnRate, btnSearch, btnAddCourse, btnEditCourse;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }
        btnLogout = findViewById(R.id.btnAdminLogout);
        btnRate = findViewById(R.id.btnAdminRate);
        btnSearch = findViewById(R.id.btnAdminSearch);
        btnAddCourse = findViewById(R.id.btnAddCourse);
        btnEditCourse = findViewById(R.id.btnEditCourse);
        firebaseAuth = firebaseAuth.getInstance();

        btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdminActivity.this, RateActivity.class);
                startActivity(i);
            }
        });

        /**
         * this method takes the user to the main activity after clicking the logout button.
         */
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    firebaseAuth.getInstance().signOut();
                    Intent i = new Intent(AdminActivity.this, MainActivity.class);
                    //terminates all activities on the stack.
                    finishAffinity();
                    startActivity(i);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
