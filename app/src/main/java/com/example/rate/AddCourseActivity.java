package com.example.rate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class AddCourseActivity extends AppCompatActivity {
    Button btnBack, btnAccount, btnAddCourse;
    EditText etAddCourse, etAddTeacher;
    RadioButton rbYes, rbNo;
    RadioGroup rgAttendance;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase fd;
    DatabaseReference drCourses, drTeachers;
    Course newCourse;
    String teacherID = "";
    Query q;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }

        btnBack = findViewById(R.id.addCourseBackBtn);
        btnAccount = findViewById(R.id.addCourseAccountBtn);
        btnAddCourse = findViewById(R.id.button14);
        etAddCourse = findViewById(R.id.editText9);
        etAddTeacher = findViewById(R.id.editText10);
        rgAttendance = findViewById(R.id.radioGroup);
        rbNo = findViewById(R.id.addCourseNoBtn);
        rbYes = findViewById(R.id.addCourseYesBtn);
        firebaseAuth = firebaseAuth.getInstance();
        fd = FirebaseDatabase.getInstance();
        drCourses = fd.getReference("courses");
        drTeachers = fd.getReference("teachers");

        /*
        this listener makes sure that all the fields are filled and if they are, it creates a new course and adds it to the database.
         */
        btnAddCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String courseName = etAddCourse.getText().toString();
                final String teacherName = etAddTeacher.getText().toString();
                if (courseName.isEmpty()) {
                    etAddCourse.setError("נא להכניס קורס");
                }
                if (teacherName.isEmpty()) {
                    etAddTeacher.setError("נא להכניס מרצה");
                    //if none of the radio buttons are checked.
                } else if (!(rbNo.isChecked() || rbYes.isChecked())) {
                    Toast.makeText(AddCourseActivity.this, "נא לבחור האם הנוכחות חובה בקורס", Toast.LENGTH_SHORT).show();
                } else {  //if all fields are filled.
                    final boolean isMust = rbYes.isChecked();
                    q = drTeachers.orderByChild("teacherName").equalTo(teacherName);
                    q.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator();
                            if (it.hasNext()) { //if the teacher already exists
                                DataSnapshot node = it.next();
                                teacherID = node.getKey();
                                newCourse = new Course(courseName, teacherID, isMust); //creating the course
                                drCourses.push().setValue(newCourse); //adding the course to the database
                            } else {
                                Teachers newTeacher = new Teachers(teacherName, null); //creating new teacher with no userId
                                drTeachers.push().setValue(newTeacher).addOnCompleteListener(new OnCompleteListener<Void>() {  //adds the teacher to the database
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (!task.isSuccessful()) {
                                            Toast.makeText(AddCourseActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        } else {
                                            q.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator(); //finds the teacher
                                                    if (it.hasNext()) {
                                                        DataSnapshot node = it.next();
                                                        teacherID = node.getKey(); //Extracts the teacherID
                                                        newCourse = new Course(courseName, teacherID, isMust); //creating the course
                                                        drCourses.push().setValue(newCourse); //adding the course to the database
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                }
                                            });
                                        }
                                    }
                                });

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    Toast.makeText(AddCourseActivity.this, "הקורס הוסף בהצלחה!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(AddCourseActivity.this, AdminActivity.class);
                    startActivity(i);
                    finishAffinity();
                }
            }
        });

        btnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent i = new Intent(AddCourseActivity.this, TeacherProfileActivity.class);
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
