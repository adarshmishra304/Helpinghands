
package com.example.helpinghands.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helpinghands.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class DonorDashboardActivity extends Activity {

    private int userId;
    private String userEmail;

    private TextView tvGreeting;
    private MaterialButton btnExploreRequests;

    private BottomNavigationView bottomNavigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_dashboard);

        bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setSelectedItemId(R.id.nav_home);

        bottomNavigation.setOnItemSelectedListener(item -> {

            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                return true;

            } else if (itemId == R.id.nav_requests) {
                Toast.makeText(this,
                        "Browse assistance requests",
                        Toast.LENGTH_SHORT).show();
                return true;

            } else if (itemId == R.id.nav_history) {
                Toast.makeText(this,
                        "Your donation history",
                        Toast.LENGTH_SHORT).show();
                return true;

            } else if (itemId == R.id.nav_profile) {
                Toast.makeText(this,
                        "Your donor profile",
                        Toast.LENGTH_SHORT).show();
                return true;
            }

            return false;
        });


        userId = getIntent().getIntExtra("USER_ID", -1);
        userEmail = getIntent().getStringExtra("USER_EMAIL");

        tvGreeting = findViewById(R.id.tvGreeting);
        btnExploreRequests = findViewById(R.id.btnExploreRequests);

        tvGreeting.setText("Welcome, " +
                (userEmail != null ? userEmail : "Donor"));

        btnExploreRequests.setOnClickListener(v -> {
            Toast.makeText(this,
                    "Verified assistance requests will appear here.",
                    Toast.LENGTH_SHORT).show();
        });
    }
}
