package com.example.mtd319_eca_carsontham;

import android.graphics.Bitmap;
import android.media.Image;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.sql.Blob;

public class Listing implements Parcelable {
    int listingId;
    int listingOwner;
    String listingName;
    String listingCategory;
    String listingDescription;
    Bitmap listingImage;
    int requestedBy;

    byte[] img;

    public Listing(int listingId, int listingOwner, String listingName, String listingCategory, String listingDescription, Bitmap listingImage, int requestedBy) {
        this.listingId = listingId;
        this.listingOwner = listingOwner;
        this.listingName = listingName;
        this.listingCategory = listingCategory;
        this.listingDescription = listingDescription;
        this.listingImage = listingImage;
        this.requestedBy = requestedBy;
    }

    protected Listing(Parcel in) {
        listingId = in.readInt();
        listingOwner = in.readInt();
        listingName = in.readString();
        listingCategory = in.readString();
        listingDescription = in.readString();
        img = in.createByteArray();
        requestedBy = in.readInt();
//      listingImage = in.//in.readByteArray();//in.readParcelable(Bitmap.class.getClassLoader());
    }

    public String getListingName() {
        return listingName;
    }

    public void setListingName(String listingName) {
        this.listingName = listingName;
    }

    public String getListingCategory() {
        return listingCategory;
    }

    public void setListingCategory(String listingCategory) {
        this.listingCategory = listingCategory;
    }

    public String getListingDescription() {
        return listingDescription;
    }

    public void setListingDescription(String listingDescription) {
        this.listingDescription = listingDescription;
    }

    public Bitmap getListingImage() {
        return listingImage;
    }

    public void setListingImage(Bitmap listingImage) {
        this.listingImage = listingImage;
    }

    public int getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(int requestedBy) {
        this.requestedBy = requestedBy;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(listingId);
        parcel.writeInt(listingOwner);
        parcel.writeString(listingName);
        parcel.writeString(listingCategory);
        parcel.writeString(listingDescription);
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        listingImage.compress(Bitmap.CompressFormat.PNG, 100, byteArray);
        img = byteArray.toByteArray();
        parcel.writeByteArray(img);
        parcel.writeInt(requestedBy);
    }

    public static final Creator<Listing> CREATOR = new Creator<Listing>() {
        @Override
        public Listing createFromParcel(Parcel in) {
            return new Listing(in);
        }

        @Override
        public Listing[] newArray(int size) {
            return new Listing[size];
        }
    };


}
