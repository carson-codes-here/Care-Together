package com.example.mtd319_eca_carsontham;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class HomeFragment extends Fragment {
    SessionManager sm;
    private DataService ds;
    ListView listview;
    public List<Listing> newItemList;
    int listCounter;
    TextView tv;
    ImageButton ib;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ds = new DataService(getActivity());
        sm = new SessionManager(getActivity());
        newItemList = ds.getAllListings();
        listCounter = newItemList.size();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View homeView = inflater.inflate(R.layout.fragment_home, container, false);
        listview = homeView.findViewById(R.id.sharedListView);
        ListViewAdapter adapter = new ListViewAdapter(getActivity().getApplicationContext(), newItemList, 0);
        listview.setAdapter(adapter);
        ib = homeView.findViewById(R.id.popupBtn);
        tv = homeView.findViewById(R.id.welcomeTV);
        tv.setText("Welcome back, " + sm.getUsername());
        return homeView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getAllListings();
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(getActivity(), ib);
                popup.getMenuInflater().inflate(R.menu.popup, popup.getMenu() );
                popup.show();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        sm.setLogin(false);
                        sm.setUser("", -1);
                        Intent goToLogin = new Intent(getActivity(), LoginActivity.class);
                        startActivity(goToLogin);
                        getActivity().finish();
                        return true;
                    }
                });
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Listing listingClicked =  (Listing) listview.getAdapter().getItem(i);
                Intent viewListingIntent = new Intent(getActivity(), ViewListingDetail.class);
                viewListingIntent.putExtra("listingDetails", listingClicked);
                startActivity(viewListingIntent);
            }
        });
    }

    public void getAllListings() {
        newItemList = ds.getAllListings();
        if (listCounter != newItemList.size()) {
            ListViewAdapter adapter = new ListViewAdapter(getActivity().getApplicationContext(), newItemList, 0);
            listview.setAdapter(adapter);
            listCounter += 1;
        }
    }
}