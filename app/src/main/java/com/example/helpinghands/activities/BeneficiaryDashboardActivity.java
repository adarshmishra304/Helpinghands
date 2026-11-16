
package com.example.helpinghands.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import android.content.Intent;

import com.example.helpinghands.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class BeneficiaryDashboardActivity extends Activity {

    private int userId;
    private String userEmail;

    private TextView tvGreeting;
    private MaterialButton btnCreateRequest;
    private MaterialButton btnMyRequests;

    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beneficiary_dashboard);

        bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setSelectedItemId(R.id.nav_home);

        bottomNavigation.setOnItemSelectedListener(item -> {

            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                return true;

            } else if (itemId == R.id.nav_requests) {

                Intent intent = new Intent(
                        BeneficiaryDashboardActivity.this,
                        MyRequestsActivity.class
                );

                intent.putExtra("USER_ID", userId);

                startActivity(intent);

                return true;
            } else if (itemId == R.id.nav_history) {
                Toast.makeText(this,
                        "Your request history",
                        Toast.LENGTH_SHORT).show();
                return true;

            } else if (itemId == R.id.nav_profile) {
                Toast.makeText(this,
                        "Your beneficiary profile",
                        Toast.LENGTH_SHORT).show();
                return true;
            }

            return false;
        });


        userId = getIntent().getIntExtra("USER_ID", -1);
        userEmail = getIntent().getStringExtra("USER_EMAIL");

        tvGreeting = findViewById(R.id.tvBeneficiaryGreeting);
        btnCreateRequest = findViewById(R.id.btnCreateRequest);
        btnMyRequests = findViewById(R.id.btnMyRequests);

        tvGreeting.setText("Welcome, " +
                (userEmail != null ? userEmail : "Beneficiary"));

        btnCreateRequest.setOnClickListener(v -> {

            Intent intent = new Intent(
                    BeneficiaryDashboardActivity.this,
                    CreateRequestActivity.class
            );

            intent.putExtra("USER_ID", userId);

            startActivity(intent);
        });

        btnMyRequests.setOnClickListener(v -> {

            Intent intent = new Intent(
                    BeneficiaryDashboardActivity.this,
                    MyRequestsActivity.class
            );

            intent.putExtra("USER_ID", userId);

            startActivity(intent);
        });
    }
}
