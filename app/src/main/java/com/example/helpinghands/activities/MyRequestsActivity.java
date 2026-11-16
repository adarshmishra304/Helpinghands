
package com.example.helpinghands.activities;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.helpinghands.R;
import com.example.helpinghands.database.DatabaseHelper;
import com.google.android.material.card.MaterialCardView;

import java.util.Locale;

public class MyRequestsActivity extends Activity {

    private DatabaseHelper databaseHelper;
    private LinearLayout requestsContainer;
    private TextView tvEmpty;

    private int beneficiaryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_requests);

        beneficiaryId = getIntent().getIntExtra("USER_ID", -1);

        requestsContainer = findViewById(R.id.requestsContainer);
        tvEmpty = findViewById(R.id.tvEmpty);

        databaseHelper = new DatabaseHelper(this);

        if (beneficiaryId == -1) {
            tvEmpty.setText("Unable to identify beneficiary account.");
            return;
        }

        loadRequests();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (requestsContainer != null && databaseHelper != null
                && beneficiaryId != -1) {
            loadRequests();
        }
    }

    private void loadRequests() {

        requestsContainer.removeAllViews();

        Cursor cursor =
                databaseHelper.getBeneficiaryRequests(beneficiaryId);

        if (cursor == null) {
            tvEmpty.setVisibility(TextView.VISIBLE);
            tvEmpty.setText("Unable to load requests.");
            return;
        }

        if (cursor.getCount() == 0) {
            tvEmpty.setVisibility(TextView.VISIBLE);
            tvEmpty.setText("You haven't submitted any requests yet.");
        } else {
            tvEmpty.setVisibility(TextView.GONE);
        }

        try {
            while (cursor.moveToNext()) {

                String title = cursor.getString(
                        cursor.getColumnIndexOrThrow("title"));

                String description = cursor.getString(
                        cursor.getColumnIndexOrThrow("description"));

                double amount = cursor.getDouble(
                        cursor.getColumnIndexOrThrow("amount_needed"));

                String status = cursor.getString(
                        cursor.getColumnIndexOrThrow("status"));

                String date = cursor.getString(
                        cursor.getColumnIndexOrThrow("created_at"));

                addRequestCard(title, description, amount, status, date);
            }
        } finally {
            cursor.close();
        }
    }

    private void addRequestCard(
            String title,
            String description,
            double amount,
            String status,
            String date) {

        MaterialCardView card = new MaterialCardView(this);

        LinearLayout.LayoutParams cardParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );

        cardParams.setMargins(0, 0, 0, dp(16));

        card.setLayoutParams(cardParams);
        card.setRadius(dp(14));
        card.setCardElevation(dp(2));
        card.setCardBackgroundColor(
                getColor(R.color.white));

        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dp(18), dp(18), dp(18), dp(18));

        TextView titleView = createText(
                title, 18, R.color.primary, true);

        TextView descriptionView = createText(
                description, 14, R.color.text_secondary, false);

        TextView amountView = createText(
                String.format(Locale.getDefault(),
                        "Amount requested: ₹%.2f", amount),
                15, R.color.text_primary, true);

        TextView statusView = createText(
                "Status: " + status,
                14,
                getStatusColor(status),
                true);

        TextView dateView = createText(
                "Submitted: " + date,
                12, R.color.text_secondary, false);

        content.addView(titleView);
        content.addView(descriptionView);
        content.addView(amountView);
        content.addView(statusView);
        content.addView(dateView);

        card.addView(content);
        requestsContainer.addView(card);
    }

    private TextView createText(
            String value,
            int size,
            int colorResource,
            boolean bold) {

        TextView textView = new TextView(this);

        textView.setText(value);
        textView.setTextSize(size);
        textView.setTextColor(getColor(colorResource));

        if (bold) {
            textView.setTypeface(null,
                    android.graphics.Typeface.BOLD);
        }

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );

        params.bottomMargin = dp(10);
        textView.setLayoutParams(params);

        return textView;
    }

    private int getStatusColor(String status) {

        if ("APPROVED".equalsIgnoreCase(status)) {
            return R.color.success;
        }

        if ("REJECTED".equalsIgnoreCase(status)) {
            return R.color.error;
        }

        return R.color.accent;
    }

    private int dp(int value) {
        return (int) (value *
                getResources().getDisplayMetrics().density);
    }
}
