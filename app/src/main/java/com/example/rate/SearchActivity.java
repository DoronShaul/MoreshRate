package com.example.rate;

import androidx.annotation.NonNull;
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
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

public class SearchActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Button btnBack, btnAccount;
    ListView lvCourses;
    ArrayList<String> courses = new ArrayList<>();
    ArrayList<Double> coursesTotal = new ArrayList<>();
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
        setContentView(R.layout.activity_search);
        firebaseAuth = firebaseAuth.getInstance();
        btnBack = findViewById(R.id.btnSearchBack);
        btnAccount = findViewById(R.id.btnSearchAccount);
        lvCourses = (ListView) findViewById(R.id.listViewSearch);
        etSearch = (EditText) findViewById(R.id.searchBarSearch);
        firebaseDatabase = firebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("courses");

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
                (SearchActivity.this).adapter.getFilter().filter(s);
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
                    DataSnapshot node = it.next();
                    coursesTotal.add(node.child("totalAvg").getValue(Double.class));
                    courses.add(node.child("courseName").getValue().toString());
                    sort(courses, coursesTotal);
                    adapter.notifyDataSetChanged();

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
                Intent i = new Intent(SearchActivity.this, Search2Activity.class);
                i.putExtra("courseName", courseName);
                startActivity(i);
            }
        });

        /**
         * this method takes the user to the main activity after clicking the logout button.
         */
        btnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent i = new Intent(SearchActivity.this, StudentProfileActivity.class);
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

    /**
     * this function sorts the courses list so the high rated courses will be first.
     * @param alString
     * @param alDouble
     */
    private void sort(ArrayList<String> alString, ArrayList<Double> alDouble) {
        for (int i = 0; i < alDouble.size() - 1; i++) {
            int maxIndex = i;
            for (int j = i + 1; j < alDouble.size(); j++) {
                if (alDouble.get(j) > alDouble.get(maxIndex)) {
                    maxIndex = j;
                }
            }
            Collections.swap(alString, i, maxIndex);
            Collections.swap(alDouble, i, maxIndex);

        }
    }
}
