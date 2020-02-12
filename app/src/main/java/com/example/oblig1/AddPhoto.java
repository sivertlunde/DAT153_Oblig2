package com.example.oblig1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class AddPhoto extends BaseActivity {

    private Image toBeUploaded;
    private Button addToDb;
    private Button libraryBtn;
    private Button cameraBtn;
    private Bitmap uploadBitmap;
    private ImageView imgView;
    private EditText nameInput;
    private ImageRepository repo;

    private void initializeVariables(){
        addToDb = findViewById(R.id.submit);
        libraryBtn = findViewById(R.id.library);
        cameraBtn = findViewById(R.id.camera);
        imgView = findViewById(R.id.uploadPhoto);
        nameInput = findViewById(R.id.photoName);
        toBeUploaded = null;
        uploadBitmap = null;
        repo = new ImageRepository(getApplication());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo);
        initializeVariables();
        libraryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                requestRead();
            }
        });
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                dispatchTakePictureIntent();
            }
        });
        addToDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                addImageToDB();
            }
        });
    }

    private void requestRead() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        } else {
            loadUpImage();
        }
    }

    private void loadUpImage(){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 2);
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (reqCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            imgView.setImageBitmap(imageBitmap);
            uploadBitmap = imageBitmap;
        }
        if(resultCode == RESULT_OK && reqCode == 2){
            try {
                final Uri imageUri = data.getData();
                Bitmap bitmapImg = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

                //uploadImageUri = Uri.parse(data(Intent.URI_ALLOW_UNSAFE));
                imgView.setImageBitmap(bitmapImg);
                uploadBitmap = bitmapImg;
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(AddPhoto.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }

    }

    private void addImageToDB(){
        final String name = nameInput.getText().toString();

        if (name.trim().isEmpty()) {
            nameInput.setError("Name required");
            nameInput.requestFocus();
            return;
        }

        if (uploadBitmap == null) {
            Toast.makeText(AddPhoto.this, "You need to upload an image first", Toast.LENGTH_LONG).show();
            return;
        }

        class SaveImage extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                //creating an Image
                Image image = new Image();
                image.setName(name);
                image.setEncodedImage(uploadBitmap);

                //adding to database
                repo.getImageDao().addImage(image);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                finish();
                startActivity(new Intent(getApplicationContext(), Database.class));
                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
            }
        }
        SaveImage si = new SaveImage();
        si.execute();
    }

    private void dispatchTakePictureIntent(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(takePictureIntent, 1);
        }
    }

}
