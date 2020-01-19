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

public class TeacherAddCourseActivity extends AppCompatActivity {
    Button btnBack, btnAccount, btnAddCourse;
    EditText etAddCourse;
    RadioButton rbYes, rbNo;
    RadioGroup rgAttendance;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase fd;
    DatabaseReference drCourses, drTeachers;
    Course newCourse;
    String teacherID;
    Query qTeacherID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_add_course);
        btnAccount = findViewById(R.id.teacherAddCourseAccountBtn);
        btnAddCourse = findViewById(R.id.btnTeacherAddCourse);
        btnBack = findViewById(R.id.teacherAddCourseBackBtn);
        etAddCourse = findViewById(R.id.etCourseName);
        rbYes = findViewById(R.id.teacherAddCourseYesBtn);
        rbNo = findViewById(R.id.teacherAddCourseNoBtn);
        rgAttendance = findViewById(R.id.radioGroupTeacherAddCourse);
        fd = FirebaseDatabase.getInstance();
        drCourses = fd.getReference("courses");
        drTeachers = fd.getReference("teachers");

        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }

        /*
        this listener makes sure that all the fields are filled and if they are, it creates a new course and adds it to the database.
         */
        btnAddCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String courseName = etAddCourse.getText().toString();
                if (courseName.isEmpty()) {
                    etAddCourse.setError("נא להכניס קורס");
                } else if (!(rbNo.isChecked() || rbYes.isChecked())) {
                    Toast.makeText(TeacherAddCourseActivity.this, "נא לבחור האם הנוכחות חובה בקורס", Toast.LENGTH_SHORT).show();
                } else {  //if all fields are filled.
                    final boolean isMust = rbYes.isChecked();
                    qTeacherID = drTeachers.orderByChild("userID").equalTo(firebaseAuth.getInstance().getCurrentUser().getUid());
                    qTeacherID.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator();
                            if (it.hasNext()) {
                                DataSnapshot node = it.next();
                                teacherID = node.getKey();
                                newCourse = new Course(courseName, teacherID, isMust);
                                drCourses.push().setValue(newCourse).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(TeacherAddCourseActivity.this, "הקורס הוסף בהצלחה!", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(TeacherAddCourseActivity.this, AdminActivity.class);
                                        startActivity(i);
                                        finishAffinity();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }
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
                Intent i = new Intent(TeacherAddCourseActivity.this, TeacherProfileActivity.class);
                startActivity(i);
            }
        });

    }
}
