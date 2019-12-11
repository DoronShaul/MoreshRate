package com.example.rate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {
    EditText emailId, password, vPassword;
    Button btnSignUp;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        firebaseAuth = firebaseAuth.getInstance();
        emailId = findViewById(R.id.editText);
        password = findViewById(R.id.editText4);
        vPassword = findViewById(R.id.editText5);

        btnSignUp = findViewById(R.id.button2);

        /*
         * this method checks the email and password given by the user, if all the parameters meet,
         * it creates a user. otherwise, the user has to input valid parameters or occurred a system error.
         */
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strEmail = emailId.getText().toString();
                String strPassword = password.getText().toString();
                String strvPassword = vPassword.getText().toString();
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
                        firebaseAuth.createUserWithEmailAndPassword(strEmail, strPassword).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignUpActivity.this, "הרישום לא הצליח, נסה שוב", Toast.LENGTH_SHORT).show();
                                } else {
                                    startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
                                }
                            }
                        });
                    } else {
                        vPassword.setError("הסיסמאות לא תואמות!");
                    }
                } else {
                    Toast.makeText(SignUpActivity.this, "אירעה תקלה!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
