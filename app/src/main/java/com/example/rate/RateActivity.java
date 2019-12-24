package com.example.rate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class RateActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Button btnBack, btnLogout;
    ListView lvCourses;
    ArrayList<String> courses = new ArrayList<>();
    ArrayAdapter<String> adapter;
    EditText etSearch;
    ValueEventListener vel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hides the action bar.
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }
        setContentView(R.layout.activity_rate);
        firebaseAuth = firebaseAuth.getInstance();
        btnBack = findViewById(R.id.button7);
        btnLogout = findViewById(R.id.button9);
        lvCourses = (ListView) findViewById(R.id.listView);
        etSearch = (EditText) findViewById(R.id.editText7);
        firebaseDatabase = firebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("courses");
        etSearch.requestFocus();

        adapter = new ArrayAdapter<>(this, R.layout.courses_info, R.id.textView3, courses);
        lvCourses.setAdapter(adapter);

        /**
         * this method makes the etSearch edit text a search option in the listView.
         */
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                (RateActivity.this).adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        /**
         * this method adds all the courses in the database to the courses arrayList.
         */
        vel = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator();
                while (it.hasNext()) {
                    courses.add(it.next().child("courseName").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /**
         * this method takes the user to the rate2 activity after clicking an item in the list view.
         */
        lvCourses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                String courseName = item.toString();
                Intent i = new Intent(RateActivity.this, Rate2Activity.class);
                i.putExtra("courseName", courseName);
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
                    Intent i = new Intent(RateActivity.this, MainActivity.class);
                    //terminates all activities on the stack.
                    finishAffinity();
                    startActivity(i);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        /**
         * this method takes the user back to the last activity.
         */
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
