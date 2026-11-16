
package com.example.helpinghands.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helpinghands.R;
import com.google.android.material.button.MaterialButton;

public class AdminDashboardActivity extends Activity {

    private TextView tvAdminGreeting;
    private MaterialButton btnManageRequests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        tvAdminGreeting = findViewById(R.id.tvAdminGreeting);
        btnManageRequests = findViewById(R.id.btnManageRequests);

        tvAdminGreeting.setText("Welcome, Administrator");

        btnManageRequests.setOnClickListener(v -> {
            Intent intent = new Intent(
                    AdminDashboardActivity.this,
                    ManageRequestsActivity.class
            );
            startActivity(intent);
        });
    }
}
