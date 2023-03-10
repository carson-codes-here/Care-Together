package com.example.mtd319_eca_carsontham;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class UploadActivity extends AppCompatActivity {

    SessionManager sm;
    private DataService ds;
    ImageView listingImage;
    Button uploadBtn;
    ImageButton deleteImgBtn;
    EditText editName, editCategory, editDes;
    Bitmap photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        ds = new DataService(this);
        sm = new SessionManager(this);

        editName = findViewById(R.id.editTextName);
        editCategory = findViewById(R.id.editTextCategory);
        editDes = findViewById(R.id.editTextDescription);
        listingImage = findViewById(R.id.listingImage);
        uploadBtn = findViewById(R.id.uploadImageBtn);
        deleteImgBtn = findViewById(R.id.deleteImageButton);

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 2000);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        uploadBtn.setVisibility(View.INVISIBLE);
        deleteImgBtn.setVisibility(View.VISIBLE);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2000) {
            photo = (Bitmap) data.getExtras().get("data");
            listingImage.setImageBitmap(photo);
        }
    }

    public void removeImage(View view){
        listingImage.setImageResource(R.drawable.ic_launcher_foreground);
        uploadBtn.setVisibility(View.VISIBLE);
        deleteImgBtn.setVisibility(View.INVISIBLE);
    }

    public void submitBtn(View view) {
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.click);
        mp.start();
        if (editName.getText().toString().matches("") ||
                editCategory.getText().toString().matches("") ||
                editDes.getText().toString().matches("") ||
                photo == null ) {
            if (photo == null) {
                Toast.makeText(this, "Please upload an image!", Toast.LENGTH_SHORT).show();
            } else {
                editName.setError("Fill in item name");
                editCategory.setError("Fill in item category");
                editDes.setError("Fill in item description");

                Toast.makeText(this, "Please fill in all fills!", Toast.LENGTH_SHORT).show();
            }
        } else {
            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 100, byteArray);
            byte[] img = byteArray.toByteArray();
            ds.insertListing(editName.getText().toString(), editCategory.getText().toString(), editDes.getText().toString(), img, sm.getUserId());
            Toast.makeText(this, "Listing Submitted!", Toast.LENGTH_SHORT).show();
            finish();
        }

    }
}