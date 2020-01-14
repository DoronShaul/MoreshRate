package com.example.rate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.File;

public class StudentProfileActivity extends AppCompatActivity {
    public static final int CAMERA_REQUEST = 1;
    Button btnCamera;
    StorageReference srMain;
    public static final int IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageTask uploadTask;


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
        srMain = FirebaseStorage.getInstance().getReference("uploads");


        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera(v);
            }
        });


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
                    String path = String.valueOf(data.getData());
                    imageUri = Uri.fromFile(new File(path));
                    //imageUri = data.getData();
                    //Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    Toast.makeText(StudentProfileActivity.this, imageUri.toString(), Toast.LENGTH_SHORT).show();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage() {
        if (imageUri != null) {
            //final StorageReference
        }
    }


}
