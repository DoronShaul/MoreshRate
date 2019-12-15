package com.example.rate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class Rate2Activity extends AppCompatActivity {
    String courseName;
    TextView tvCourse;
    Button btnBack, btnLogout;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }
        setContentView(R.layout.activity_rate2);
        tvCourse = findViewById(R.id.textView4);
        btnBack = findViewById(R.id.button12);
        btnLogout = findViewById(R.id.button11);
        Intent i = getIntent();
        Bundle b = i.getExtras();
        if (b != null) {
            courseName = b.getString("courseName");
            tvCourse.setText(courseName);
        }

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    firebaseAuth.getInstance().signOut();
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
