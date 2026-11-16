
package com.example.helpinghands.activities;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helpinghands.R;
import com.example.helpinghands.database.DatabaseHelper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

public class ManageRequestsActivity extends Activity {

    private DatabaseHelper databaseHelper;
    private LinearLayout requestsContainer;
    private TextView tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_requests);

        requestsContainer = findViewById(R.id.requestsContainer);
        tvEmpty = findViewById(R.id.tvEmpty);

        databaseHelper = new DatabaseHelper(this);

        loadRequests();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (requestsContainer != null && databaseHelper != null) {
            loadRequests();
        }
    }

    private void loadRequests() {

        requestsContainer.removeAllViews();

        Cursor cursor = databaseHelper.getAllAssistanceRequests();

        if (cursor == null || cursor.getCount() == 0) {
            tvEmpty.setVisibility(TextView.VISIBLE);
            tvEmpty.setText("No assistance requests found.");
            if (cursor != null) cursor.close();
            return;
        }

        tvEmpty.setVisibility(TextView.GONE);

        try {
            while (cursor.moveToNext()) {

                int requestId = cursor.getInt(
                        cursor.getColumnIndexOrThrow("request_id"));

                String title = cursor.getString(
                        cursor.getColumnIndexOrThrow("title"));

                String description = cursor.getString(
                        cursor.getColumnIndexOrThrow("description"));

                double amount = cursor.getDouble(
                        cursor.getColumnIndexOrThrow("amount_needed"));

                String status = cursor.getString(
                        cursor.getColumnIndexOrThrow("status"));

                String beneficiary = cursor.getString(
                        cursor.getColumnIndexOrThrow("name"));

                String email = cursor.getString(
                        cursor.getColumnIndexOrThrow("email"));

                addRequestCard(
                        requestId, title, description,
                        amount, status, beneficiary, email
                );
            }
        } finally {
            cursor.close();
        }
    }

    private void addRequestCard(
            int requestId,
            String title,
            String description,
            double amount,
            String status,
            String beneficiary,
            String email) {

        MaterialCardView card = new MaterialCardView(this);

        LinearLayout.LayoutParams cardParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );

        cardParams.setMargins(0, 0, 0, dp(16));

        card.setLayoutParams(cardParams);
        card.setRadius(dp(14));
        card.setCardElevation(dp(3));
        card.setCardBackgroundColor(getColor(R.color.white));

        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dp(16), dp(16), dp(16), dp(16));

        addText(content, title, 19, R.color.primary, true);
        addText(content, "Request ID: " + requestId,
                13, R.color.text_secondary, false);
        addText(content, "Beneficiary: " + beneficiary,
                14, R.color.text_primary, true);
        addText(content, "Email: " + email,
                13, R.color.text_secondary, false);
        addText(content, description,
                14, R.color.text_primary, false);
        addText(content, "Amount: ₹" + amount,
                15, R.color.primary, true);
        addText(content, "Status: " + status,
                14, R.color.accent, true);

        if ("PENDING".equalsIgnoreCase(status)) {

            MaterialButton btnApprove = new MaterialButton(this);
            btnApprove.setText("Approve");

            MaterialButton btnReject = new MaterialButton(this);
            btnReject.setText("Reject");

            content.addView(btnApprove);
            content.addView(btnReject);

            btnApprove.setOnClickListener(v ->
                    updateStatus(requestId, "APPROVED"));

            btnReject.setOnClickListener(v ->
                    updateStatus(requestId, "REJECTED"));
        }

        card.addView(content);
        requestsContainer.addView(card);
    }

    private void addText(
            LinearLayout parent,
            String value,
            int size,
            int color,
            boolean bold) {

        TextView textView = new TextView(this);
        textView.setText(value);
        textView.setTextSize(size);
        textView.setTextColor(getColor(color));

        if (bold) {
            textView.setTypeface(null,
                    android.graphics.Typeface.BOLD);
        }

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );

        params.bottomMargin = dp(9);
        textView.setLayoutParams(params);

        parent.addView(textView);
    }

    private void updateStatus(int requestId, String status) {

        boolean updated = databaseHelper.updateRequestStatus(
                requestId,
                status,
                "Request " + status.toLowerCase()
        );

        if (updated) {
            Toast.makeText(this,
                    "Request " + status.toLowerCase() + " successfully",
                    Toast.LENGTH_SHORT).show();

            loadRequests();
        } else {
            Toast.makeText(this,
                    "Unable to update request",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private int dp(int value) {
        return (int) (value *
                getResources().getDisplayMetrics().density);
    }
}
