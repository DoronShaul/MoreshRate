package com.example.rate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddCourseActivity extends AppCompatActivity {
    Button btnBack, btnLogout, btnAddCourse;
    EditText etAddCourse, etAddTeacher;
    RadioButton rbYes, rbNo;
    RadioGroup rgAttendance;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase fd;
    DatabaseReference drCourses;
    Course newCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }

        btnBack = findViewById(R.id.addCourseBackBtn);
        btnLogout = findViewById(R.id.addCourseLogoutBtn);
        btnAddCourse = findViewById(R.id.button14);
        etAddCourse = findViewById(R.id.editText9);
        etAddTeacher = findViewById(R.id.editText10);
        rgAttendance = findViewById(R.id.radioGroup);
        rbNo = findViewById(R.id.addCourseNoBtn);
        rbYes = findViewById(R.id.addCourseYesBtn);
        firebaseAuth = firebaseAuth.getInstance();
        fd = FirebaseDatabase.getInstance();
        drCourses = fd.getReference("courses");

        /*
        this listener makes sure that all the fields are filled and if they are, it creates a new course and adds it to the database.
         */
        btnAddCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String course = etAddCourse.getText().toString();
                String teacher = etAddTeacher.getText().toString();
                if (course.isEmpty()) {
                    etAddCourse.setError("נא להכניס קורס");
                }
                if (teacher.isEmpty()) {
                    etAddTeacher.setError("נא להכניס מרצה");
                    //if none of the radio buttons are checked.
                } else if (!(rbNo.isChecked() || rbYes.isChecked())) {
                    Toast.makeText(AddCourseActivity.this, "נא לבחור האם הנוכחות חובה בקורס", Toast.LENGTH_SHORT).show();
                } else {
                    boolean isMust = rbYes.isChecked();
                    newCourse = new Course(teacher, isMust);
                    drCourses.child(course).setValue(newCourse);
                    Toast.makeText(AddCourseActivity.this, "הקורס הוסף בהצלחה!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(AddCourseActivity.this, AdminActivity.class);
                    startActivity(i);
                    finishAffinity();
                }
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    firebaseAuth.signOut();
                    Intent i = new Intent(AddCourseActivity.this, MainActivity.class);
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
