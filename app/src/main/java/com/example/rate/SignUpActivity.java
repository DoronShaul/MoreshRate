package com.example.rate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Iterator;

public class SignUpActivity extends AppCompatActivity {
    EditText emailId, password, vPassword, personName;
    Button btnSignUp, btnBack;
    RadioGroup rg;
    RadioButton btnTeacher, btnStudent;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase fd;
    DatabaseReference drStudents, drTeachers;
    String strID, strName, strEmail, strPassword, strvPassword;
    boolean isExist = false;
    private NotificationManagerCompat notificationManager;

    public void sendOnChannel1(View v) {
        Notification notification = new NotificationCompat.Builder(this, App.CHANNEL_1_ID).setSmallIcon(R.drawable.ic_sms).setContentTitle("מרצה חדש").setContentText("יש מרצה חדש שרוצה להירשם").setPriority(NotificationCompat.PRIORITY_HIGH).setCategory(NotificationCompat.CATEGORY_MESSAGE).build();
        notificationManager.notify(1, notification);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }
        setContentView(R.layout.activity_sign_up);
        notificationManager = NotificationManagerCompat.from(this);
        firebaseAuth = firebaseAuth.getInstance();
        fd = FirebaseDatabase.getInstance();
        drStudents = fd.getReference("students");
        drTeachers = fd.getReference("teachers");
        emailId = findViewById(R.id.editText);
        password = findViewById(R.id.editText4);
        vPassword = findViewById(R.id.editText5);
        btnBack = findViewById(R.id.button6);
        btnSignUp = findViewById(R.id.button2);
        personName = findViewById(R.id.etName);
        btnTeacher = findViewById(R.id.btnAsTeacher);
        btnStudent = findViewById(R.id.btnAsStudent);
        rg = findViewById(R.id.radioGroup1);

        /**
         * this method checks the email and password given by the user, if all the parameters meet,
         * it creates a user. otherwise, the user has to input valid parameters or occurred a system error.
         */
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strName = personName.getText().toString();
                strEmail = emailId.getText().toString();
                strPassword = password.getText().toString();
                strvPassword = vPassword.getText().toString();
                if (strName.isEmpty()) {
                    personName.setError("אנא הכנס שם מלא!");
                    personName.requestFocus();
                }
                if (strEmail.isEmpty()) {
                    emailId.setError("אנא הכנס מייל!");
                    emailId.requestFocus();
                }
                if (strvPassword.isEmpty()) {
                    vPassword.setError("אנא הכנס את סיסמא בשנית!");
                    password.requestFocus();
                }
                if (strPassword.isEmpty()) {
                    password.setError("אנא הכנס סיסמא!");
                    vPassword.requestFocus();
                } else if (strPassword.length() < 6) {
                    Toast.makeText(SignUpActivity.this, "סיסמא חייבת להכיל 6 תווים ומעלה.", Toast.LENGTH_SHORT).show();
                    password.requestFocus();
                } else if (!(strEmail.isEmpty() && strvPassword.isEmpty())) {
                    //checks if the password and the verify password are equal.
                    if (strPassword.equals(strvPassword)) {
                        if (btnStudent.isChecked()) {
                            firebaseAuth.createUserWithEmailAndPassword(strEmail, strPassword).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(SignUpActivity.this, "הרישום לא הצליח, נסה שוב", Toast.LENGTH_SHORT).show();
                                    } else {
                                        firebaseAuth.addIdTokenListener(new FirebaseAuth.IdTokenListener() {
                                            @Override
                                            public void onIdTokenChanged(@NonNull FirebaseAuth firebaseAuth) {
                                                strID = firebaseAuth.getCurrentUser().getUid();
                                                Students newStudent = new Students(strName);
                                                Toast.makeText(SignUpActivity.this, strID, Toast.LENGTH_SHORT).show();
                                                drStudents.child(strID).setValue(newStudent);
                                            }
                                        });
                                        startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                                        finish();
                                    }
                                }
                            });
                        } else {
                            drTeachers.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator();
                                    while (it.hasNext()) {
                                        DataSnapshot node = it.next();

                                        if (node.child("teacherName").getValue().toString().equals(strName)) {
                                            firebaseAuth.createUserWithEmailAndPassword(strEmail, strPassword).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if (!task.isSuccessful()) {
                                                        Toast.makeText(SignUpActivity.this, "הרישום לא הצליח, נסה שוב", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        firebaseAuth.addIdTokenListener(new FirebaseAuth.IdTokenListener() {
                                                            @Override
                                                            public void onIdTokenChanged(@NonNull FirebaseAuth firebaseAuth) {
                                                                strID = firebaseAuth.getCurrentUser().getUid();
                                                                Toast.makeText(SignUpActivity.this,strID, Toast.LENGTH_SHORT).show();

                                                            }
                                                        });
                                                    }
                                                }
                                            });

                                            HashMap<String, Object> hm = new HashMap<>();
                                            hm.put("uID", strID);
                                            drTeachers.child(node.getKey()).updateChildren(hm);
                                            isExist = true;
                                            break;
                                        }
                                    }
                                    if (!isExist) {
                                        firebaseAuth.createUserWithEmailAndPassword(strEmail, strPassword).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (!task.isSuccessful()) {
                                                    Toast.makeText(SignUpActivity.this, "הרישום לא הצליח, נסה שוב", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    firebaseAuth.addIdTokenListener(new FirebaseAuth.IdTokenListener() {
                                                        @Override
                                                        public void onIdTokenChanged(@NonNull FirebaseAuth firebaseAuth) {
                                                            strID = firebaseAuth.getCurrentUser().getUid();
                                                            Teachers newTeacher = new Teachers(strName, strID);
                                                            drTeachers.push().setValue(newTeacher);
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

                            //sendOnChannel1(v);
                            //Toast.makeText(SignUpActivity.this, "בקשתך להירשם כמרצה מחכה לאישור", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                            finish();
                        }

                    } else {
                        vPassword.setError("הסיסמאות לא תואמות!");
                    }
                } else {
                    Toast.makeText(SignUpActivity.this, "אירעה תקלה!", Toast.LENGTH_SHORT).show();
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
