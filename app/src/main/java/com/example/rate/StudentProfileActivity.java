package com.example.rate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.google.firebase.database.core.Constants;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class StudentProfileActivity extends AppCompatActivity {
    public static final int CAMERA_REQUEST = 1;
    Button btnCamera, btnLogout, btnBack;
    FirebaseAuth firebaseAuth;
    CircularImageView civProfilePic;
    TextView tvName;
    String getStudentName;
    ListView lvStudentRatings;
    DatabaseReference drPhotos, drStudents, drRatings, drCourses, drMain;
    ArrayAdapter<String> adapter;
    ArrayList<String> coursesRated;
    Query qUserRatings, qStudentName, qCourseID;
    ValueEventListener vel;
    ProgressBar progressBar;
    MyTask myTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);
        //hides the action bar.
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }
        btnCamera = findViewById(R.id.addPhoto);
        drPhotos = FirebaseDatabase.getInstance().getReference("photos");
        drStudents = FirebaseDatabase.getInstance().getReference("students");
        drRatings = FirebaseDatabase.getInstance().getReference("ratings");
        drCourses = FirebaseDatabase.getInstance().getReference("courses");
        drMain = FirebaseDatabase.getInstance().getReference();
        btnBack = findViewById(R.id.studentProfileBack);
        btnLogout = findViewById(R.id.studentProfileLogout);
        firebaseAuth = firebaseAuth.getInstance();
        civProfilePic = findViewById(R.id.circularImageView);
        tvName = findViewById(R.id.studentName);
        lvStudentRatings = findViewById(R.id.listViewStudent);
        coursesRated = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, R.layout.courses_info, R.id.textView3, coursesRated);
        lvStudentRatings.setAdapter(adapter);
        progressBar = findViewById(R.id.progressBar);
        myTask = new MyTask();
        myTask.execute();


        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera(v);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                try {
                                    firebaseAuth.getInstance().signOut();
                                    Intent i = new Intent(StudentProfileActivity.this, MainActivity.class);
                                    startActivity(i);
                                    finish();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("בטוח שברצונך להתנתק?").setPositiveButton("כן", dialogClickListener)
                        .setNegativeButton("לא", dialogClickListener).show();


            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    /**
     * takes the string of the image from the database, and decodes it so it can be presented as a bitmap.
     */
    public static Bitmap decodeFromFirebaseBase64(String image) throws IOException {

        byte[] decodedByteArray = android.util.Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);

    }

    public void openCamera(View v) {
        Intent newIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(newIntent, CAMERA_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST) {
            if (resultCode == RESULT_OK) {
                try {
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    civProfilePic.setImageBitmap(bitmap);
                    encodeBitmapAndSaveToFirebase(bitmap);

                } catch (NullPointerException e) {
                    e.printStackTrace();

                }
            }
        }
    }

    /**
     * this method encodes a bitmap image to a string and uploads it to the database.
     *
     * @param bitmap
     */
    private void encodeBitmapAndSaveToFirebase(Bitmap bitmap) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        final String imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);


        Query qUserID = drPhotos.orderByChild("userID").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
        qUserID.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator();
                //if the user already has a photo.
                if (it.hasNext()) {
                    DataSnapshot node = it.next();
                    HashMap<String, Object> update = new HashMap<>();
                    update.put("imageData", imageEncoded);
                    drPhotos.child(node.getKey()).updateChildren(update);
                } else {
                    Photos newPhoto = new Photos(FirebaseAuth.getInstance().getCurrentUser().getUid(), imageEncoded);
                    drPhotos.push().setValue(newPhoto);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            //finds all the ratings of the current user and adds it to the listView.
            qUserRatings = drRatings.orderByChild("userID").equalTo(firebaseAuth.getCurrentUser().getUid());
            qUserRatings.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator();
                    while (it.hasNext()) {
                        DataSnapshot node = it.next();
                        String courseID = node.child("courseID").getValue().toString();
                        //finds the course of the specific rating.
                        qCourseID = drCourses.orderByKey().equalTo(courseID);
                        qCourseID.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator();
                                if (it.hasNext()) {
                                    DataSnapshot node = it.next();
                                    String courseName = node.child("courseName").getValue().toString();
                                    if (!coursesRated.contains(courseName)) {
                                        coursesRated.add(0, courseName);
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            //gets the name of the current user and displays it.
            qStudentName = drStudents.orderByKey().equalTo(firebaseAuth.getCurrentUser().getUid());
            qStudentName.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator();
                    if (it.hasNext()) {
                        DataSnapshot node = it.next();
                        getStudentName = node.child("studentName").getValue().toString();
                        tvName.setText(getStudentName);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            //gets the profile photo of the user if he has one.
            Query qUserID = drPhotos.orderByChild("userID").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
            qUserID.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator();
                    //if the user already has a photo.
                    if (it.hasNext()) {
                        DataSnapshot node = it.next();
                        String imageData = node.child("imageData").getValue().toString();
                        try {
                            Bitmap bitmap = decodeFromFirebaseBase64(imageData);
                            civProfilePic.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            return null;

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            progressBar.getProgress();

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }

}
