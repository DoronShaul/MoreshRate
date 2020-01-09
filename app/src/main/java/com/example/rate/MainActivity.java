package com.example.rate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    DatabaseReference drTeachers, drMain;
    TextView tvSignUp, tvForgotPassword;
    Button btnLogin;
    EditText emailId, password;
    final static String adminDoron = "doronsds@gmail.com";
    private FirebaseAuth.AuthStateListener asListener;
    private boolean isTeacher = false;
    String userType = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hides action bar.
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }
        setContentView(R.layout.activity_main);
//        ImageView logo = findViewById(R.id.imageView);
        firebaseAuth = firebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        drMain = database.getReference();
        drTeachers = database.getReference("teachers");
        emailId = findViewById(R.id.editText2);
        password = findViewById(R.id.editText3);
        btnLogin = findViewById(R.id.button);
        tvSignUp = findViewById(R.id.textView2);
        tvForgotPassword = findViewById(R.id.textView);

        btnLogin.setOnClickListener(new View.OnClickListener() {

            /**
             * this method checks the email and password given by the user, if all the parameters meet,
             * the database will check if the user exists. otherwise, there was a problem with the parameters.
             */
            @Override
            public void onClick(View v) {
                String strEmail = emailId.getText().toString();
                String strPassword = password.getText().toString();
                //if the email edit text is empty.
                if (strEmail.isEmpty()) {
                    emailId.setError("אנא הכנס מייל!");
                    emailId.requestFocus();
                }
                //if the password edit text is empty.
                if (strPassword.isEmpty()) {
                    password.setError("אנא הכנס סיסמא!");
                    password.requestFocus();
                }
                //if the email and the password edit texts are filled.
                else if (!(strEmail.isEmpty())) {
                    firebaseAuth.signInWithEmailAndPassword(strEmail, strPassword).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //there was a problem with the parameters.
                            if (!task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            //the parameters were valid, sign in successfully.
                            else {
                                Toast.makeText(MainActivity.this, "התחברת בהצלחה!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
                //system error occurred.
                else {
                    Toast.makeText(MainActivity.this, "אירעה תקלה!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        asListener = new FirebaseAuth.AuthStateListener() {
            /**
             * this method checks if the user exists. if it does it goes to the home page. otherwise, goes to the main page and request a login.
             */
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser fbUser = firebaseAuth.getCurrentUser();
                Intent extra = getIntent();
                Bundle b = extra.getExtras();
                if (b != null) {
                    userType = b.getString("type");
                }
                if (fbUser != null) {
                    //if the user is the admin, then it goes to the admin page.
                    if (fbUser.getEmail().equals(adminDoron)) {
                        Intent i = new Intent(MainActivity.this, AdminActivity.class);
                        startActivity(i);
                        finish();
                    } else { //checks if the user is a teacher or a student.
                        if (userType.equals("teacher")) { //after sign up as a teacher.
                            Intent i = new Intent(MainActivity.this, AdminActivity.class);
                            startActivity(i);
                            finish();
                        } else if (userType.equals("student")) { //after sign up as a student.
                            Intent i = new Intent(MainActivity.this, HomeActivity.class);
                            startActivity(i);
                            finish();
                        } else { //when the app is started and the user already logged in. needs to check in the database whether the user is a teacher or a student.
                            drTeachers.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator();
                                    while (it.hasNext()) {
                                        DataSnapshot node = it.next();
                                        if (node.child("userID").getValue().toString().equals(fbUser.getUid())) {
                                            isTeacher = true;
                                            break;
                                        }
                                    }
                                    if (isTeacher) { //if the user found in the teachers database.
                                        Intent i = new Intent(MainActivity.this, AdminActivity.class);
                                        startActivity(i);
                                        finish();
                                    } else {
                                        Intent ii = new Intent(MainActivity.this, HomeActivity.class);
                                        startActivity(ii);
                                        finish();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }


                    }

                } else {
                    Toast.makeText(MainActivity.this, "אנא התחבר/י", Toast.LENGTH_SHORT).show();
                }
            }
        };
        /**
         * this method goes to the sign up activity.
         */
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });

        /**
         * this method goes to the forgot password activity.
         */
        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ForgotPasswordActivity.class);
                startActivity(i);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(asListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (asListener != null) {
            firebaseAuth.removeAuthStateListener(asListener);
        }
    }
}
