 package com.project.personalcatalogue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

 public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView btmNav = findViewById(R.id.bottom_navigation);
        //btmNav.setOnClickListener(navListener);
        btmNav.setOnNavigationItemSelectedListener(navListener);

        //Setting home fragment as default
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_layout, new HomeFragment()).commit();
    }
    private final BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()){
                case R.id.item1:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.item2:
                    selectedFragment = new DiscoverFragment();
                    break;
                case R.id.item3:
                    selectedFragment = new SearchFragment();
                    break;
                case R.id.item4:
                    selectedFragment = new UserFragment();
                    break;
            }
            
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_layout,
                    selectedFragment).commit();

            return true;
        }
    };


 }