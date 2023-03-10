package com.example.mtd319_eca_carsontham;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import java.util.List;

public class ListViewAdapter extends BaseAdapter {
    Context context;
    public List<Listing> newItemList;
    LayoutInflater inflater;
    DataService ds;
    int checkSameView;

    public ListViewAdapter(Context context, List<Listing> newItemList, int checker) {
        this.context = context;
        this.newItemList = newItemList;
        inflater = LayoutInflater.from(context);
        ds = new DataService(context);
        checkSameView = checker;
    }

    @Override
    public int getCount() {
        return newItemList.size();
    }

    @Override
    public Object getItem(int i) {
        return newItemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (checkSameView == 0) {
            view = inflater.inflate(R.layout.list_view_layout, null);
            TextView tv = view.findViewById(R.id.itemName);
            ImageView iv = view.findViewById(R.id.itemImage);
            TextView listOwner = view.findViewById(R.id.tvListedBy);

            String ownerName = ds.getListingOwner(newItemList.get(i).listingOwner);
            tv.setText(newItemList.get(i).listingName);
            iv.setImageBitmap(newItemList.get(i).listingImage);

            if (ds.checkStatusPending(newItemList.get(i).listingId)) {
                listOwner.setText("Your request for this item has been accepted! You can collect the item at the centralized community booth");
            } else {
                listOwner.setText("Listed by: " + ownerName);
            }
            return view;
        } else if (checkSameView == 1) {
            view = inflater.inflate(R.layout.list_view_layout_requests, null);
            TextView tv = view.findViewById(R.id.itemName);
            ImageView iv = view.findViewById(R.id.itemImage);
            TextView requestUser = view.findViewById(R.id.tvRequestedBy);

            tv.setText(newItemList.get(i).listingName);
            iv.setImageBitmap(newItemList.get(i).listingImage);
            int requester = newItemList.get(i).requestedBy;
            Button accept = view.findViewById(R.id.completeBtn);
            Button reject = view.findViewById(R.id.rejectBtn);

            if (requester == -1) {
                requestUser.setText("Your listing is still available");
                accept.setVisibility(View.GONE);
                reject.setVisibility(View.GONE);
            } else {
                if (ds.checkStatusPending(newItemList.get(i).listingId)) {
                    accept.setVisibility(View.GONE);
                    reject.setVisibility(View.GONE);
                    requestUser.setText("You have accepted this request. Please bring your item to the centralized community booth.");
                } else {
                    String requesterName = ds.getListingOwner(newItemList.get(i).requestedBy);
                    requestUser.setText("Requested by: " + requesterName);
                }

                reject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ds.cancelRequest(newItemList.get(i).listingId);
                        accept.setVisibility(View.GONE);
                        reject.setVisibility(View.GONE);
                        requestUser.setText("Your listing is still available");
                    }
                });

                accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ds.acceptRequest(newItemList.get(i).listingId);
                        accept.setVisibility(View.GONE);
                        reject.setVisibility(View.GONE);
                        requestUser.setText("You have accepted this request. Please bring your item to the centralized community booth. ");
                    }
                });
            }
            return view;
        }
            else {
            view = inflater.inflate(R.layout.list_view_admin, null);
            TextView tv = view.findViewById(R.id.itemName);
            ImageView iv = view.findViewById(R.id.itemImage);
            TextView requestedBy = view.findViewById(R.id.tvRequestedBy);
            TextView listedBy = view.findViewById(R.id.listedByOwner);
            tv.setText(newItemList.get(i).listingName);
            iv.setImageBitmap(newItemList.get(i).listingImage);
            requestedBy.setText("Requested by: " + ds.getListingOwner(newItemList.get(i).requestedBy));
            listedBy.setText("Listed by: " + ds.getListingOwner(newItemList.get(i).listingOwner));
            Button completeTransaction = view.findViewById(R.id.completeBtn);

            completeTransaction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ds.completeTransaction(newItemList.get(i).listingId);
                    completeTransaction.setText("Transaction Completed! Will be removed");
                    completeTransaction.setEnabled(false);
                }
            });
            return view;
        }
    }
}
