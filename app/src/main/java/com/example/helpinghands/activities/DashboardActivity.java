
package com.example.helpinghands.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helpinghands.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

public class DashboardActivity extends Activity {

    BottomNavigationView bottomNavigation;
    TextView tvGreeting;
    MaterialButton btnExplore;

    private int userId;
    private String userRole;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        bottomNavigation = findViewById(R.id.bottomNavigation);
        tvGreeting = findViewById(R.id.tvGreeting);
        btnExplore = findViewById(R.id.btnExplore);

        userId = getIntent().getIntExtra("USER_ID", -1);
        userRole = getIntent().getStringExtra("USER_ROLE");
        userEmail = getIntent().getStringExtra("USER_EMAIL");


        String role = getIntent().getStringExtra("role");

        if (role == null) {
            role = "DONOR";
        }

        tvGreeting.setText("Hello, " + role + "!");

        bottomNavigation.setSelectedItemId(R.id.nav_home);

        bottomNavigation.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            Toast.makeText(
                    this,
                    "Welcome! Account type: " + userRole,
                    Toast.LENGTH_LONG
            ).show();

            if (id == R.id.nav_home) {
                Toast.makeText(this,
                        "Home",
                        Toast.LENGTH_SHORT).show();

            } else if (id == R.id.nav_requests) {
                Toast.makeText(this,
                        "Requests module coming next",
                        Toast.LENGTH_SHORT).show();

            } else if (id == R.id.nav_history) {
                Toast.makeText(this,
                        "Donation history coming next",
                        Toast.LENGTH_SHORT).show();

            } else if (id == R.id.nav_profile) {
                Toast.makeText(this,
                        "Profile module coming next",
                        Toast.LENGTH_SHORT).show();
            }

            return true;
        });

        btnExplore.setOnClickListener(v -> {
            Toast.makeText(this,
                    "Request browsing will be implemented next",
                    Toast.LENGTH_SHORT).show();
        });
    }
}
