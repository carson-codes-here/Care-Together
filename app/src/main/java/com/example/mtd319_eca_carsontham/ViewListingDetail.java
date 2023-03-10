package com.example.mtd319_eca_carsontham;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ViewListingDetail extends AppCompatActivity {

    TextView name, category, description, owner;
    ImageView listImage;
    DataService ds;
    SessionManager sm;
    Button requestListingBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_listing_detail);

        Listing l = (Listing) getIntent().getParcelableExtra("listingDetails");

        name = findViewById(R.id.listingName);
        category = findViewById(R.id.listingCategory);
        description = findViewById(R.id.listingDescriptionDetails);
        listImage = findViewById(R.id.listingImage);
        owner = findViewById(R.id.listingOwner);
        requestListingBtn = findViewById(R.id.requestBtn);
        ds = new DataService(getApplicationContext());
        sm = new SessionManager(getApplicationContext());

        String s = ds.getListingOwner(l.listingOwner);
        name.setText(s + "'s " + l.listingName);
        category.setText(l.listingCategory);
        description.setText(l.listingDescription);
        String listingOwner = ds.getListingOwner(l.listingOwner);
        owner.setText(listingOwner);
        Bitmap image = BitmapFactory.decodeByteArray(l.img, 0, l.img.length);
        listImage.setImageBitmap(image);

        // check if the listing belongs to owner requested, -1 is default, meaning not requested by any users
        if (l.listingOwner == sm.getUserId()) {
            requestListingBtn.setVisibility(View.GONE);
            Toast.makeText(ViewListingDetail.this, "You can't request for your own listing", Toast.LENGTH_SHORT).show();
        } else {
            if (l.requestedBy != -1 && l.requestedBy == sm.getUserId()) {
                requestListingBtn.setText("Undo Request");
                if (ds.checkStatusPending(l.listingId)) {
                    setButtonDisabled();
                    Toast.makeText(this, "Unable to undo as your requests has been accepted.", Toast.LENGTH_SHORT).show();
                }
            } else if (l.requestedBy != -1 && l.requestedBy != sm.getUserId()) {
                setButtonDisabled();
                Toast.makeText(this, "This listing has been requested. Not available for now.", Toast.LENGTH_SHORT).show();
            }
        }

        final MediaPlayer mp = MediaPlayer.create(this, R.raw.click);

        requestListingBtn.setOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View view) {
                                                     mp.start();
                                                     Listing l1 = ds.getListingById(l.listingId);
                                                     if (l1.requestedBy == sm.getUserId()) {
                                                         ds.cancelRequest(l1.listingId);
                                                         requestListingBtn.setText("Request Listing");
                                                     } else {
                                                         ds.requestListing(l1, sm.getUserId());
                                                         requestListingBtn.setText("Undo Request");
                                                         Toast.makeText(ViewListingDetail.this, "You have requested the item!", Toast.LENGTH_SHORT).show();
                                                     }
                                                 }
                                             }
        );
    }

    public void setButtonDisabled() {
        requestListingBtn.setEnabled(false);
        requestListingBtn.setBackgroundColor(getResources().getColor(R.color.gray));
        requestListingBtn.setTextColor(getResources().getColor(R.color.darkgray));
    }
}
