package com.example.rate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
        setContentView(R.layout.activity_main);
        firebaseAuth = firebaseAuth.getInstance();
        emailId = findViewById(R.id.editText2);
        password = findViewById(R.id.editText3);
        btnLogin = findViewById(R.id.button);
        tvSignUp = findViewById(R.id.textView2);
        tvForgotPassword = findViewById(R.id.textView);

        btnLogin.setOnClickListener(new View.OnClickListener() {

            /**
             * this method checks the email and password given by the user, if all the parameters meet,
             * the database will check if thw user exists. otherwise, there was a problem with the parameters.
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
                //if the email and the password edit texts are empty.
//                else if (strEmail.isEmpty() && strPassword.isEmpty()) {
//                    Toast.makeText(MainActivity.this, "השדות ריקים!", Toast.LENGTH_SHORT).show();
//
//                }
                //if the email and the password edit texts are filled.
                else if (!(strEmail.isEmpty() && strPassword.isEmpty())) {

                    firebaseAuth.signInWithEmailAndPassword(strEmail, strPassword).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //there was a problem with the parameters.
                            if (!task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "ההתחברות כשלה, נסה שוב.", Toast.LENGTH_SHORT).show();
                            }
                            //the parameters were valid, transfer to the home page.
                            else {
                                Toast.makeText(MainActivity.this, "בוא נראה אם רואים אותי!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(MainActivity.this, HomeActivity.class));
                                finish();
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
             * this method checks if the user exists. if it does
             * @param firebaseAuth
             */
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser fbUser = firebaseAuth.getCurrentUser();
                if (fbUser != null) {
                    Toast.makeText(MainActivity.this, "התחברת בהצלחה!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "אנא התחבר/י", Toast.LENGTH_SHORT).show();
                }
            }
        };

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });

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
