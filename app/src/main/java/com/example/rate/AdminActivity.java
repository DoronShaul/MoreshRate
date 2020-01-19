package com.example.rate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class AdminActivity extends AppCompatActivity {
    Button btnAccount, btnAddCourse;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }
        btnAccount = findViewById(R.id.btnAdminAccount);
        btnAddCourse = findViewById(R.id.btnAddCourse);
        firebaseAuth = firebaseAuth.getInstance();


        /**
         * this method takes the user to the main activity after clicking the logout button.
         */
        btnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent i = new Intent(AdminActivity.this, TeacherProfileActivity.class);
                    startActivity(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        btnAddCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                if (firebaseAuth.getCurrentUser().getEmail().equals("doronsds@gmail.com")) {
                    i = new Intent(AdminActivity.this, AddCourseActivity.class);
                } else {
                    i = new Intent(AdminActivity.this, TeacherAddCourseActivity.class);
                }
                startActivity(i);
            }
        });

    }
}
