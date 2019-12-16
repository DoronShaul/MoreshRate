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

public class MainActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    TextView tvSignUp, tvForgotPassword;
    Button btnLogin;
    EditText emailId, password;
    private FirebaseAuth.AuthStateListener asListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hides action bar.
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_main);
        ImageView logo = findViewById(R.id.imageView);
        firebaseAuth = firebaseAuth.getInstance();
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
                FirebaseUser fbUser = firebaseAuth.getCurrentUser();
                if (fbUser != null) {
                    Intent i = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(i);
                    finish();
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
}
