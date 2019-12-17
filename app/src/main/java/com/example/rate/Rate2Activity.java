package com.example.rate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Rate2Activity extends AppCompatActivity {
    String courseName;
    TextView tvCourse;
    Button btnBack, btnLogout, btnRate;
    RatingBar rbCourse, rbTeacher, rbTest;
    RadioGroup rgAttendance;
    RadioButton rbtnYes, rbtnNo;
    FirebaseDatabase mDatabase;
    DatabaseReference drRating, drCourses;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        firebaseAuth = firebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        drCourses = mDatabase.getReference("courses");
        drRating = mDatabase.getReference("ratings");
        setContentView(R.layout.activity_rate2);
        tvCourse = findViewById(R.id.textView4);
        btnBack = findViewById(R.id.button12);
        btnLogout = findViewById(R.id.button11);
        btnRate = findViewById(R.id.button13);
        rbCourse = findViewById(R.id.ratingBar);
        rbTeacher = findViewById(R.id.ratingBar1);
        rbTest = findViewById(R.id.ratingBar2);
        rgAttendance = findViewById(R.id.radioGroup);
        rbtnNo = findViewById(R.id.radioButton5);
        rbtnYes = findViewById(R.id.radioButton4);
        Intent i = getIntent();
        Bundle b = i.getExtras();
        if (b != null) {
            courseName = b.getString("courseName");
            tvCourse.setText(courseName);
        }

        btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rgAttendance.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(Rate2Activity.this, "נא לסמן האם יש נוכחות בקורס זה!", Toast.LENGTH_SHORT).show();
                } else {
                    //creates a new ratings for the selected course and push it to the database.
                    Ratings rat = new Ratings(courseName, (int)rbTeacher.getRating(), (int)rbCourse.getRating(), (int)rbTest.getRating());
                    drRating.push().setValue(rat);
                    Toast.makeText(Rate2Activity.this, "תודה שדירגת!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Rate2Activity.this, HomeActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    firebaseAuth.signOut();
                    Intent i = new Intent(Rate2Activity.this, MainActivity.class);
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
