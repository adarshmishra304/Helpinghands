
package com.example.helpinghands.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.helpinghands.R;
import com.example.helpinghands.database.DatabaseHelper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class CreateRequestActivity extends Activity {

    private TextInputEditText etRequestTitle;
    private TextInputEditText etRequestDescription;
    private TextInputEditText etAmountNeeded;

    private MaterialButton btnSubmitRequest;

    private DatabaseHelper databaseHelper;

    private int beneficiaryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_request);

        etRequestTitle = findViewById(R.id.etRequestTitle);
        etRequestDescription = findViewById(R.id.etRequestDescription);
        etAmountNeeded = findViewById(R.id.etAmountNeeded);
        btnSubmitRequest = findViewById(R.id.btnSubmitRequest);

        databaseHelper = new DatabaseHelper(this);

        beneficiaryId = getIntent().getIntExtra("USER_ID", -1);

        if (beneficiaryId == -1) {
            Toast.makeText(this,
                    "Unable to identify beneficiary account",
                    Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        btnSubmitRequest.setOnClickListener(v -> submitRequest());
    }

    private void submitRequest() {

        String title = etRequestTitle.getText().toString().trim();
        String description =
                etRequestDescription.getText().toString().trim();
        String amountText =
                etAmountNeeded.getText().toString().trim();

        if (title.isEmpty()) {
            etRequestTitle.setError("Enter request title");
            return;
        }

        if (description.isEmpty()) {
            etRequestDescription.setError("Enter request description");
            return;
        }

        if (amountText.isEmpty()) {
            etAmountNeeded.setError("Enter required amount");
            return;
        }

        double amount;

        try {
            amount = Double.parseDouble(amountText);
        } catch (NumberFormatException e) {
            etAmountNeeded.setError("Enter a valid amount");
            return;
        }

        if (!Double.isFinite(amount) || amount <= 0) {
            etAmountNeeded.setError("Amount must be greater than zero");
            return;
        }

        boolean inserted = databaseHelper.submitAssistanceRequest(
                beneficiaryId,
                title,
                description,
                amount
        );

        if (inserted) {
            Toast.makeText(this,
                    "Assistance request submitted successfully!",
                    Toast.LENGTH_LONG).show();

            finish();

        } else {
            Toast.makeText(this,
                    "Unable to submit request",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
