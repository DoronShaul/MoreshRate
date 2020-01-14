package com.example.rate;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    Button btnRate, btnSearch, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hides the action bar.
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_home);
        firebaseAuth = firebaseAuth.getInstance();
        btnSearch = findViewById(R.id.button5);
        btnRate = findViewById(R.id.button3);
        btnLogout = findViewById(R.id.button8);

        /**
         * this method takes the user to the rate activity after clicking the rate button.
         */
        btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, RateActivity.class);
                startActivity(i);
            }
        });

        /**
         * this method takes the user to the search activity after clicking the search button.
         */
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, StudentProfileActivity.class);
                startActivity(i);
            }
        });

        /**
         * this method takes the user to the main activity after clicking the logout button.
         */
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                try {
                                    firebaseAuth.getInstance().signOut();
                                    Intent i = new Intent(HomeActivity.this, MainActivity.class);
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
    }


}
