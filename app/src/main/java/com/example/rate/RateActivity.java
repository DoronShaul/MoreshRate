package com.example.rate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RateActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Button btnBack, btnLogout;
    ListView lvCourses;
    ArrayList<String> courses = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        firebaseAuth = firebaseAuth.getInstance();
        btnBack = findViewById(R.id.button7);
        btnLogout = findViewById(R.id.button9);
        lvCourses = (ListView) findViewById(R.id.listView);
        firebaseDatabase = firebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("courses");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.courses_info,R.id.textView3, courses);
        lvCourses.setAdapter(adapter);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    firebaseAuth.getInstance().signOut();
                    Intent i = new Intent(RateActivity.this, MainActivity.class);
                    //terminates all activities on the stack.
                    finishAffinity();
                    startActivity(i);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}
