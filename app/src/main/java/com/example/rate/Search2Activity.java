package com.example.rate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Iterator;

import static java.lang.Double.parseDouble;

public class Search2Activity extends AppCompatActivity {
    String courseName, teacherName, teacherID, type, courseID;
    TextView tvCourse, tvAttendance, tvCourseRating, tvTeacherRating, tvTestRating, tvTotalRating, tvTeacherName;
    Button btnBack, btnAccount;
    RatingBar rbCourse, rbTeacher, rbTest, rbTotal;
    FirebaseDatabase mDatabase;
    DatabaseReference drTeachers, drCourses, drRatings;
    FirebaseAuth firebaseAuth;
    Query qcourseID, qTeacherName, qTeacherID, qComments;
    Double toBeTruncated, truncatedDouble;
    ListView lvComments;
    ArrayList<String> commentsArray = new ArrayList<>();
    ArrayAdapter<String> adapter;
    final static String adminDoron = "doronsds@gmail.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search2);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        firebaseAuth = FirebaseAuth.getInstance();
        tvCourse = findViewById(R.id.search2CourseName);
        tvAttendance = findViewById(R.id.tvSearchAttendanceRating);
        tvCourseRating = findViewById(R.id.tvSearchCourseRating);
        tvTeacherRating = findViewById(R.id.tvSearchTeacherRating);
        tvTestRating = findViewById(R.id.tvSearchTestRating);
        tvTotalRating = findViewById(R.id.tvSearchTotalRating);
        tvTeacherName = findViewById(R.id.search2TeacherName);
        btnAccount = findViewById(R.id.btnSearch2Account);
        btnBack = findViewById(R.id.btnSearch2Back);
        rbCourse = findViewById(R.id.searchRatingBar);
        rbTeacher = findViewById(R.id.searchRatingBar1);
        rbTest = findViewById(R.id.searchRatingBar2);
        rbTotal = findViewById(R.id.searchRatingBar3);
        lvComments = findViewById(R.id.listViewSearch2);
        adapter = new ArrayAdapter<>(this, R.layout.courses_info, R.id.textView3, commentsArray);
        lvComments.setAdapter(adapter);
        mDatabase = FirebaseDatabase.getInstance();
        drCourses = mDatabase.getReference("courses");
        drTeachers = mDatabase.getReference("teachers");
        drRatings = mDatabase.getReference("ratings");


        Intent i = getIntent();
        Bundle b = i.getExtras();
        if (b != null) {
            courseName = b.getString("courseName");
            type = b.getString("type");
            tvCourse.setText(courseName);
        }

        qcourseID = drCourses.orderByChild("courseName").equalTo(courseName);
        qcourseID.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator();
                if (it.hasNext()) {
                    DataSnapshot node = it.next();
                    courseID = node.getKey();
                    qComments = drRatings.orderByChild("courseID").equalTo(courseID);
                    qComments.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator();
                            while (it.hasNext()) {
                                DataSnapshot node = it.next();
                                if (!(node.child("comment").getValue().toString().equals(""))) {
                                    commentsArray.add("'' " + node.child("comment").getValue().toString() + " ''");
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                    rbCourse.setRating(Float.parseFloat(node.child("courseAvg").getValue().toString()));
                    rbTeacher.setRating(node.child("teacherAvg").getValue(Float.class));
                    rbTest.setRating(node.child("testAvg").getValue(Float.class));
                    rbTotal.setRating(node.child("totalAvg").getValue(Float.class));
                    if (node.child("isMust").getValue().toString().equals("true")) {
                        tvAttendance.setText("חובה");
                    } else {
                        tvAttendance.setText("לא חובה");
                    }

                    toBeTruncated = parseDouble(node.child("courseAvg").getValue().toString());
                    truncatedDouble = BigDecimal.valueOf(toBeTruncated).setScale(1, RoundingMode.HALF_UP).doubleValue();
                    tvCourseRating.setText(truncatedDouble.toString() + "/5");

                    toBeTruncated = parseDouble(node.child("teacherAvg").getValue().toString());
                    truncatedDouble = BigDecimal.valueOf(toBeTruncated).setScale(1, RoundingMode.HALF_UP).doubleValue();
                    tvTeacherRating.setText(truncatedDouble.toString() + "/5");

                    toBeTruncated = parseDouble(node.child("testAvg").getValue().toString());
                    truncatedDouble = BigDecimal.valueOf(toBeTruncated).setScale(1, RoundingMode.HALF_UP).doubleValue();
                    tvTestRating.setText(truncatedDouble.toString() + "/5");

                    toBeTruncated = parseDouble(node.child("totalAvg").getValue().toString());
                    truncatedDouble = BigDecimal.valueOf(toBeTruncated).setScale(1, RoundingMode.HALF_UP).doubleValue();
                    tvTotalRating.setText(truncatedDouble.toString() + "/5");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        qTeacherID = drCourses.orderByChild("courseName").equalTo(courseName);
        qTeacherID.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator();
                if (it.hasNext()) {
                    DataSnapshot node = it.next();
                    teacherID = node.child("teacherID").getValue().toString();
                }
                qTeacherName = drTeachers.orderByKey().equalTo(teacherID);
                qTeacherName.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator();
                        if (it.hasNext()) {
                            DataSnapshot node = it.next();
                            teacherName = node.child("teacherName").getValue().toString();
                        }
                        tvTeacherName.setText(teacherName);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                if (type.equals("teacher")) {
                    i = new Intent(Search2Activity.this, TeacherProfileActivity.class);
                } else {
                    i = new Intent(Search2Activity.this, StudentProfileActivity.class);
                }
                startActivity(i);
            }
        });

    }

}
