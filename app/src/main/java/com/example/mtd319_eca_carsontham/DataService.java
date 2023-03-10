package com.example.mtd319_eca_carsontham;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DataService {
    private SQLiteDatabase db;
    public List<Listing> itemList = new ArrayList<>();

    public DataService(Context context) {
        ListingDatabase helper = new ListingDatabase(context);
        db = helper.getWritableDatabase();
    }

    public void insertListing(String listingName, String listingCategory, String listingDescription, byte[] image, int listingOwner) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("Name", listingName);
        contentValues.put("Category", listingCategory);
        contentValues.put("Description", listingDescription);
        contentValues.put("Image", image);
        contentValues.put("Listing_Owner", listingOwner);
        contentValues.put("REQUESTED_BY", -1);
        contentValues.put("TRANSACTION_STATUS", "A");
        db.insert("TBL_LISTING", null, contentValues);
    }

    public void acceptRequest(int listingId) {
        ContentValues cv = new ContentValues();
        cv.put("TRANSACTION_STATUS", "P");
        db.update("TBL_LISTING", cv, "LISTINGID = ?", new String[]{String.valueOf(listingId)});
    }

    public Boolean checkStatusPending(int listingId) {
        Cursor c = db.rawQuery("SELECT * FROM TBL_LISTING WHERE LISTINGID = ? ", new String[]{String.valueOf(listingId)});
        if (c.moveToFirst()){
            if (c.getString(7).equals("P") ){
                return true;
            };
        }
        c.close();
        return false;
    }

    public void cancelRequest(int listingId) {
        Cursor c = db.rawQuery("SELECT * FROM TBL_LISTING WHERE LISTINGID = ? ", new String[]{String.valueOf(listingId)});
        c.moveToFirst();
        if (c.getInt(6) != -1) {
            ContentValues cv = new ContentValues();
            cv.put("REQUESTED_BY", -1);
            db.update("TBL_LISTING", cv, "LISTINGID= ?", new String[]{String.valueOf(listingId)});
        }
        c.close();
    }

    public Listing getListingById(int listingId){
        Cursor c = db.rawQuery("SELECT * FROM TBL_LISTING WHERE LISTINGID = ? ", new String[]{String.valueOf(listingId)});
        c.moveToFirst();
        byte[] bitmap = c.getBlob(5);
        Bitmap image = BitmapFactory.decodeByteArray(bitmap, 0, bitmap.length);
        Listing l = new Listing(c.getInt(0), c.getInt(1), c.getString(2), c.getString(3), c.getString(4), image, c.getInt(6));
        return l;
    }

    public String getListingOwner(int userId) {
        Cursor c = db.rawQuery("SELECT * FROM TBL_USERS WHERE USERID = ?", new String[]{String.valueOf(userId)});
        String username = new String();
        if (c.moveToFirst()) {
            username = c.getString(1);
            c.close();
        }
        return username;
    }

    public void completeTransaction(int listingId) {
        ContentValues cv = new ContentValues();
        cv.put("TRANSACTION_STATUS", "C");
        db.update("TBL_LISTING", cv, "LISTINGID = ?", new String[]{String.valueOf(listingId)});
    }

    public List<Listing> getPendingCompletionListings() {
        List<Listing> pendingAdminList = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM TBL_LISTING WHERE TRANSACTION_STATUS = 'P';", null);
        while (c.moveToNext()) {
            Log.i("info", "pending list not empty?");
            byte[] bitmap = c.getBlob(5);
            Bitmap image = BitmapFactory.decodeByteArray(bitmap, 0, bitmap.length);
            Listing l = new Listing(c.getInt(0), c.getInt(1), c.getString(2), c.getString(3), c.getString(4), image, c.getInt(6));
            pendingAdminList.add(0, l);
        }
        c.close();
        return pendingAdminList;
    }

    public List<Listing> getAllListings() {
        itemList.clear();
        Cursor c = db.rawQuery("SELECT * FROM TBL_LISTING WHERE TRANSACTION_STATUS = ? ORDER BY LISTINGID;", new String[]{"A"});
        while (c.moveToNext()) {
            byte[] bitmap = c.getBlob(5);
            Bitmap image = BitmapFactory.decodeByteArray(bitmap, 0, bitmap.length);
            Listing l = new Listing(c.getInt(0), c.getInt(1), c.getString(2), c.getString(3), c.getString(4), image, c.getInt(6));
            //Listing l = new Listing(c.getString(0), c.getString(1), c.getString(2), image);
            itemList.add(0, l);
        }
        c.close();
        return itemList;
    }

    public List<Listing> getUserRequestedListings(int requesterUserId) {
        List<Listing> newList = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM TBL_LISTING WHERE REQUESTED_BY = ? AND TRANSACTION_STATUS != ? ORDER BY LISTINGID ", new String[]{String.valueOf(requesterUserId), "C"});
        while (c.moveToNext()) {
            byte[] bitmap = c.getBlob(5);
            Bitmap image = BitmapFactory.decodeByteArray(bitmap, 0, bitmap.length);
            Listing l = new Listing(c.getInt(0), c.getInt(1), c.getString(2), c.getString(3), c.getString(4), image, c.getInt(6));
            newList.add(0, l);
        }
        c.close();
        return newList;
    }

    public List<Listing> getListingsRequestedFromUser(int userId) {
        List<Listing> newList = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM TBL_LISTING WHERE LISTING_OWNER = ? AND TRANSACTION_STATUS != ? ORDER BY LISTINGID", new String[]{String.valueOf(userId), "C"});
        while (c.moveToNext()) {
            byte[] bitmap = c.getBlob(5);
            Bitmap image = BitmapFactory.decodeByteArray(bitmap, 0, bitmap.length);
            Listing l = new Listing(c.getInt(0), c.getInt(1), c.getString(2), c.getString(3), c.getString(4), image, c.getInt(6));
            newList.add(0, l);
        }
        c.close();
        return newList;
    }

    public List<Listing> getSharedList(int userId) {
        List<Listing> newList = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM TBL_LISTING WHERE LISTING_OWNER = ? AND TRANSACTION_STATUS = ? ORDER BY LISTINGID ", new String[]{String.valueOf(userId), "C"});
        while (c.moveToNext()) {
            byte[] bitmap = c.getBlob(5);
            Bitmap image = BitmapFactory.decodeByteArray(bitmap, 0, bitmap.length);
            Listing l = new Listing(c.getInt(0), c.getInt(1), c.getString(2), c.getString(3), c.getString(4), image, c.getInt(6));
            newList.add(0, l);
        }
        c.close();
        return newList;
    }

    public List<Listing> getReceivedList(int userId) {
        List<Listing> newList = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM TBL_LISTING WHERE REQUESTED_BY = ? AND TRANSACTION_STATUS = ? ORDER BY LISTINGID ", new String[]{String.valueOf(userId), "C"});
        while (c.moveToNext()) {
            byte[] bitmap = c.getBlob(5);
            Bitmap image = BitmapFactory.decodeByteArray(bitmap, 0, bitmap.length);
            Listing l = new Listing(c.getInt(0), c.getInt(1), c.getString(2), c.getString(3), c.getString(4), image, c.getInt(6));
            newList.add(0, l);
        }
        c.close();
        return newList;
    }

    public void registerNewUser(Context c, String username, String password) {
        ContentValues cv = new ContentValues();
        cv.put("Name", username);
        cv.put("Password", password);
        long ins = db.insert("TBL_USERS", null, cv);

        if (ins == -1) {
            Toast.makeText(c, "NOT ADDED!!!!!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(c, "Registered New User!", Toast.LENGTH_SHORT).show();
        }
    }

    public Boolean checkValidUser(String username) {
        Cursor c = db.rawQuery("SELECT * FROM TBL_USERS WHERE NAME = ? ", new String[]{username});
        if (c.moveToFirst()) {
            return true;
        }
        return false;
    }

    public Boolean checkValidPw(String username, String password) {
        Cursor c = db.rawQuery("SELECT * FROM TBL_USERS WHERE NAME = ? ", new String[]{username});
        if (c.moveToFirst()) {
            if (c.getString(2).equals(password)) return true;
        }
        return false;
    }

    public int getUserId(String username) {
        Cursor c = db.rawQuery("SELECT * FROM TBL_USERS WHERE NAME = ? ", new String[]{username});
        c.moveToFirst();
        return c.getInt(0);
    }

    public Boolean requestListing(Listing l, int requesterId) {
        Cursor c = db.rawQuery("SELECT * FROM TBL_LISTING WHERE LISTINGID = ? ", new String[]{String.valueOf(l.listingId)});
        c.moveToFirst();
        if (c.getInt(6) == -1) {
            ContentValues cv = new ContentValues();
            cv.put("REQUESTED_BY", requesterId);
            Log.i("info", "Listing Requested!");
            db.update("TBL_LISTING", cv, "LISTINGID= ?", new String[]{String.valueOf(l.listingId)});
            return true;
        }
        return false;
    }

    // this function is called to populateDB
    // basically it resets all data to default condition and populate table
    public Boolean populateDB(Context context) {
        ArrayList<ContentValues> list = new ArrayList<>();

        db.delete("TBL_Listing", null, null);
        db.delete("TBL_USERS", null, null);

        // Insert first listing
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.iphone);
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray);
        byte[] img = byteArray.toByteArray();

        ContentValues contentValues = new ContentValues();
        contentValues.put("LISTING_OWNER", 1);
        contentValues.put("Name", "iPhone 12");
        contentValues.put("Category", "electronics");
        contentValues.put("Description", "best");
        contentValues.put("Image", img);
        contentValues.put("REQUESTED_BY", 2);
        contentValues.put("TRANSACTION_STATUS", "A"); //
        list.add(contentValues);

        // Insert second listing
        Bitmap bitmap1 = BitmapFactory.decodeResource(context.getResources(), R.mipmap.shoe);
        ByteArrayOutputStream byteArray1 = new ByteArrayOutputStream();
        bitmap1.compress(Bitmap.CompressFormat.PNG, 100, byteArray1);
        byte[] img1 = byteArray1.toByteArray();

        ContentValues contentValues1 = new ContentValues();
        contentValues1.put("LISTING_OWNER", 1);
        contentValues1.put("Name", "Nike Shoe Air Force 1");
        contentValues1.put("Category", "shoes");
        contentValues1.put("Description", "best");
        contentValues1.put("Image", img1);
        contentValues1.put("REQUESTED_BY", 2);
        contentValues1.put("TRANSACTION_STATUS", "A"); //
        list.add(contentValues1);

        // Insert third listing
        Bitmap bitmap2 = BitmapFactory.decodeResource(context.getResources(), R.mipmap.sofa);
        ByteArrayOutputStream byteArray2 = new ByteArrayOutputStream();
        bitmap2.compress(Bitmap.CompressFormat.PNG, 100, byteArray2);
        byte[] img2 = byteArray2.toByteArray();

        ContentValues contentValues2 = new ContentValues();
        contentValues2.put("LISTING_OWNER", 1);
        contentValues2.put("Name", "Used Sofa");
        contentValues2.put("Category", "Furniture");
        contentValues2.put("Description", "Used for only 3 months");
        contentValues2.put("Image", img2);
        contentValues2.put("REQUESTED_BY", -1);
        contentValues2.put("TRANSACTION_STATUS", "A"); //
        list.add(contentValues2);

        // Insert fourth listing
        Bitmap bitmap3 = BitmapFactory.decodeResource(context.getResources(), R.mipmap.bike);
        ByteArrayOutputStream byteArray3 = new ByteArrayOutputStream();
        bitmap3.compress(Bitmap.CompressFormat.PNG, 100, byteArray3);
        byte[] img3 = byteArray3.toByteArray();

        ContentValues contentValues3 = new ContentValues();
        contentValues3.put("LISTING_OWNER", 1);
        contentValues3.put("Name", "Bike");
        contentValues3.put("Category", "Bicycle");
        contentValues3.put("Description", "Rarely cycle nowadays. Giving away for free.");
        contentValues3.put("Image", img3);
        contentValues3.put("REQUESTED_BY", -1);
        contentValues3.put("TRANSACTION_STATUS", "A"); //
        list.add(contentValues3);

        // Insert fifth listing
        Bitmap bitmap4 = BitmapFactory.decodeResource(context.getResources(), R.mipmap.coat);
        ByteArrayOutputStream byteArray4 = new ByteArrayOutputStream();
        bitmap4.compress(Bitmap.CompressFormat.PNG, 100, byteArray4);
        byte[] img4 = byteArray4.toByteArray();

        ContentValues contentValues4 = new ContentValues();
        contentValues4.put("LISTING_OWNER", 2);
        contentValues4.put("Name", "Thick Coat");
        contentValues4.put("Category", "Clothes");
        contentValues4.put("Description", "Bought in Korea but too small for me");
        contentValues4.put("Image", img4);
        contentValues4.put("REQUESTED_BY", -1);
        contentValues4.put("TRANSACTION_STATUS", "A"); //
        list.add(contentValues4);

        // Insert sixth listing
        Bitmap bitmap5 = BitmapFactory.decodeResource(context.getResources(), R.mipmap.bag);
        ByteArrayOutputStream byteArray5 = new ByteArrayOutputStream();
        bitmap5.compress(Bitmap.CompressFormat.PNG, 100, byteArray5);
        byte[] img5 = byteArray5.toByteArray();

        ContentValues contentValues5 = new ContentValues();
        contentValues5.put("LISTING_OWNER", 3);
        contentValues5.put("Name", "Leather bag");
        contentValues5.put("Category", "Bags");
        contentValues5.put("Description", "Bought in Italy. Used for 2-3 years. No longer need it.");
        contentValues5.put("Image", img5);
        contentValues5.put("REQUESTED_BY", -1);
        contentValues5.put("TRANSACTION_STATUS", "A"); //
        list.add(contentValues5);

        // Insert 7th listing
        Bitmap bitmap6 = BitmapFactory.decodeResource(context.getResources(), R.mipmap.luggage);
        ByteArrayOutputStream byteArray6 = new ByteArrayOutputStream();
        bitmap6.compress(Bitmap.CompressFormat.PNG, 100, byteArray6);
        byte[] img6 = byteArray6.toByteArray();

        ContentValues contentValues6 = new ContentValues();
        contentValues6.put("LISTING_OWNER", 3);
        contentValues6.put("Name", "Luggage");
        contentValues6.put("Category", "Travel Items");
        contentValues6.put("Description", "This was a free gift. Giving out to others as I don't need it :)");
        contentValues6.put("Image", img6);
        contentValues6.put("REQUESTED_BY", -1);
        contentValues6.put("TRANSACTION_STATUS", "A"); //
        list.add(contentValues6);

        // Insert 8th listing
        Bitmap bitmap7 = BitmapFactory.decodeResource(context.getResources(), R.mipmap.table);
        ByteArrayOutputStream byteArray7 = new ByteArrayOutputStream();
        bitmap7.compress(Bitmap.CompressFormat.PNG, 100, byteArray7);
        byte[] img7 = byteArray7.toByteArray();

        ContentValues contentValues7 = new ContentValues();
        contentValues7.put("LISTING_OWNER", 3);
        contentValues7.put("Name", "Coffee Table");
        contentValues7.put("Category", "Furniture");
        contentValues7.put("Description", "Cute coffee table. House does not have space for this sadly. Open to sharing it with the community!");
        contentValues7.put("Image", img7);
        contentValues7.put("REQUESTED_BY", -1);
        contentValues7.put("TRANSACTION_STATUS", "A"); //
        list.add(contentValues7);

        // populated database
        for (int i = 0; i < list.size(); i++) {
            db.insert("TBL_LISTING", null, list.get(i));
        }

        // insert 3 users
        ContentValues cv = new ContentValues();
        cv.put("Name", "Jon");
        cv.put("Password", 123);

        ContentValues cv1 = new ContentValues();
        cv1.put("Name", "Dan");
        cv1.put("Password", 123);

        ContentValues cv2 = new ContentValues();
        cv2.put("Name", "Fin");
        cv2.put("Password", 123);

        db.insert("TBL_USERS", null, cv);
        db.insert("TBL_USERS", null, cv1);
        db.insert("TBL_USERS", null, cv2);
        return true;
    }

}


