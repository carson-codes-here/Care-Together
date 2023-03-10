package com.example.mtd319_eca_carsontham;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

public class PastActivityFragment extends Fragment {
    SessionManager sm;
    private DataService ds;
    ListView sharedListView, receivedListView;
    public List<Listing> itemsSharedList, itemsReceivedList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ds = new DataService(getActivity());
        sm = new SessionManager(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View pastActivitiesView = inflater.inflate(R.layout.fragment_past_activity, container, false);
        sharedListView = pastActivitiesView.findViewById(R.id.sharedListView);
        receivedListView = pastActivitiesView.findViewById(R.id.receivedListView);

        itemsSharedList = ds.getSharedList(sm.getUserId());
        ListViewAdapter adapter = new ListViewAdapter(getActivity().getApplicationContext(), itemsSharedList, 0);
        sharedListView.setAdapter(adapter);

        itemsReceivedList = ds.getReceivedList(sm.getUserId());
        ListViewAdapter adapter1 = new ListViewAdapter(getActivity().getApplicationContext(), itemsReceivedList, 0);
        receivedListView.setAdapter(adapter1);

        return pastActivitiesView;
    }
}