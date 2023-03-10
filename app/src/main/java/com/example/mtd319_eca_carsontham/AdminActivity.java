package com.example.mtd319_eca_carsontham;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;

import java.util.List;

public class AdminActivity extends AppCompatActivity {
    SessionManager sm;
    private DataService ds;
    ListView listview;
    public List<Listing> pendingCompletionList;
    ImageButton logOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        ds = new DataService(this);
        sm = new SessionManager(this);

        pendingCompletionList = ds.getPendingCompletionListings();
        listview = findViewById(R.id.sharedListView);
        ListViewAdapter adapter = new ListViewAdapter(this, pendingCompletionList, -1);
        listview.setAdapter(adapter);

        logOut = findViewById(R.id.popupBtn);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(getApplicationContext(), logOut);
                popup.getMenuInflater().inflate(R.menu.popup, popup.getMenu() );
                popup.show();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        sm.setLogin(false);
                        sm.setUser("", -1);
                        Intent goToLogin = new Intent(AdminActivity.this, LoginActivity.class);
                        startActivity(goToLogin);
                        finish();
                        return true;
                    }
                });
            }
        });
    }
}