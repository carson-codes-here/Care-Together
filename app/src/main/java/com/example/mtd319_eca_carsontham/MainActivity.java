package com.example.mtd319_eca_carsontham;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    SessionManager sm;
    DataService ds;
    BottomNavigationView bnv;
    HomeFragment hf = new HomeFragment();
    ListingRequestFragment lf = new ListingRequestFragment();
    PastActivityFragment pf = new PastActivityFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sm = new SessionManager(this);
        ds = new DataService(this);
        setContentView(R.layout.activity_main);
        bnv = findViewById(R.id.bottomNavView);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, hf).commit();

        bnv.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId() ) {
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, hf).commit();
                        return true;

                    case R.id.receivedListView:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, lf).commit();
                        return true;

                    case R.id.pastActivities:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, pf).commit();
                        return true;
                }
                return false;
            }
        });
    }
    public void testBtn(View view) {
        Intent myIntent = new Intent(MainActivity.this, UploadActivity.class);
        startActivity(myIntent);
//        overridePendingTransition(R.anim.fade_in, R.anim.splash_fade_out);
    }

}