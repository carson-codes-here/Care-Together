package com.example.mtd319_eca_carsontham;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class ListingRequestFragment extends Fragment {
    SessionManager sm;
    private DataService ds;
    ListView listview, reqListingView;
    public List<Listing> itemsRequestedList, itemRequestsList;
    Button b;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ds = new DataService(getActivity());
        sm = new SessionManager(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Toast.makeText(getActivity(), "new listingreq frag", Toast.LENGTH_SHORT).show();
        View requestListView = inflater.inflate(R.layout.fragment_listing_requests, container, false);
        listview = requestListView.findViewById(R.id.sharedListView);
        reqListingView = requestListView.findViewById(R.id.receivedListView);

        itemsRequestedList = ds.getUserRequestedListings(sm.getUserId());
        ListViewAdapter adapter = new ListViewAdapter(getActivity().getApplicationContext(), itemsRequestedList, 0);
        listview.setAdapter(adapter);

        itemRequestsList = ds.getListingsRequestedFromUser(sm.getUserId());
        ListViewAdapter adapter1 = new ListViewAdapter(getActivity().getApplicationContext(), itemRequestsList, 1);
        reqListingView.setAdapter(adapter1);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final MediaPlayer mp = MediaPlayer.create(getActivity(), R.raw.click);
                mp.start();
                Listing listingClicked =  (Listing) listview.getAdapter().getItem(i);
                Intent viewListingIntent = new Intent(getActivity(), ViewListingDetail.class);
                viewListingIntent.putExtra("listingDetails", listingClicked);
                startActivity(viewListingIntent);
            }
        });

        reqListingView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final MediaPlayer mp = MediaPlayer.create(getActivity(), R.raw.click);
                mp.start();
                Listing listingClicked =  (Listing) reqListingView.getAdapter().getItem(i);
                Intent viewListingIntent = new Intent(getActivity(), ViewListingDetail.class);
                viewListingIntent.putExtra("listingDetails", listingClicked);
                startActivity(viewListingIntent);
            }
        });

        return requestListView;
    }
}