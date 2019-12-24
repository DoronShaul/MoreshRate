package com.example.rate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Iterator;

public class Rate2Activity extends AppCompatActivity {
    String courseName;
    TextView tvCourse;
    EditText etComment;
    Button btnBack, btnLogout, btnRate;
    RatingBar rbCourse, rbTeacher, rbTest;
    FirebaseDatabase mDatabase;
    DatabaseReference drRating, drCourses;
    FirebaseAuth firebaseAuth;
    Ratings rat;
    Course course;
    final static String adminDoron = "doronsds@gmail.com";

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
        etComment = findViewById(R.id.editText8);
        btnBack = findViewById(R.id.button12);
        btnLogout = findViewById(R.id.button11);
        btnRate = findViewById(R.id.button13);
        rbCourse = findViewById(R.id.ratingBar);
        rbTeacher = findViewById(R.id.ratingBar1);
        rbTest = findViewById(R.id.ratingBar2);
        Intent i = getIntent();
        Bundle b = i.getExtras();
        if (b != null) {
            courseName = b.getString("courseName");
            tvCourse.setText(courseName);
        }

        /**
         * this method adds the rating given by the user to the database, when the user click on the rate button.
         * also, updates the average of every category in the courses database according to the new rating that been added.
         */
        btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if at least one of the rating fields are empty
                if ((int) rbTeacher.getRating() == 0 || (int) rbCourse.getRating() == 0 || (int) rbTest.getRating() == 0) {
                    Toast.makeText(Rate2Activity.this, "נא לדרג את כל הקטגוריות", Toast.LENGTH_SHORT).show();
                } else {
                    String comment = etComment.getText().toString();
                    //creates a new ratings for the selected course and push it to the database.
                    rat = new Ratings(courseName, comment, (int) rbTeacher.getRating(), (int) rbCourse.getRating(), (int) rbTest.getRating());
                    drRating.push().setValue(rat);
                    Query query = drCourses.orderByChild("courseName").equalTo(courseName);


                    /**
                     * this method reaches the specific course that been rated, and updates its average values.
                     */
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            DataSnapshot nodeDataSnapshot = dataSnapshot.getChildren().iterator().next();
                            String key = nodeDataSnapshot.getKey();
                            Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator();
                            int numOfRatings = it.next().child("numOfRatings").getValue(Integer.class);
                            Toast.makeText(Rate2Activity.this, "" + numOfRatings, Toast.LENGTH_SHORT).show();
                            String path = "/" + key;
                            HashMap<String, Object> result = new HashMap<>();
                            result.put("teacherAvg", 3.7);
                            drCourses.child(path).updateChildren(result);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                    Toast.makeText(Rate2Activity.this, "תודה שדירגת!", Toast.LENGTH_SHORT).show();
                    FirebaseUser fbUser = firebaseAuth.getCurrentUser();
                    if (fbUser.getEmail().equals(adminDoron)) {
                        Intent i = new Intent(Rate2Activity.this, AdminActivity.class);
                        startActivity(i);
                        finishAffinity();

                    } else {
                        Intent ii = new Intent(Rate2Activity.this, HomeActivity.class);
                        startActivity(ii);
                        finishAffinity();

                    }
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
