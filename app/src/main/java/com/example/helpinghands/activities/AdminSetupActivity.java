
package com.example.helpinghands.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.helpinghands.R;
import com.example.helpinghands.database.DatabaseHelper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class AdminSetupActivity extends Activity {

    private TextInputEditText etName, etEmail, etPhone, etPassword;
    private MaterialButton btnCreateAdmin;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_setup);

        etName = findViewById(R.id.etAdminName);
        etEmail = findViewById(R.id.etAdminEmail);
        etPhone = findViewById(R.id.etAdminPhone);
        etPassword = findViewById(R.id.etAdminPassword);
        btnCreateAdmin = findViewById(R.id.btnCreateAdmin);

        databaseHelper = new DatabaseHelper(this);

        btnCreateAdmin.setOnClickListener(v -> createAdmin());
    }

    private void createAdmin() {

        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString();

        if (name.isEmpty() || email.isEmpty()
                || phone.isEmpty() || password.isEmpty()) {
            Toast.makeText(this,
                    "Please fill all fields",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS
                .matcher(email).matches()) {
            etEmail.setError("Enter a valid email");
            return;
        }

        if (!phone.matches("[0-9]{10}")) {
            etPhone.setError("Enter a valid 10-digit phone number");
            return;
        }

        if (password.length() < 8) {
            etPassword.setError("Password must contain at least 8 characters");
            return;
        }

        boolean created = databaseHelper.createAdminAccount(
                name, email, phone, password
        );

        if (created) {
            Toast.makeText(this,
                    "Admin account created successfully!",
                    Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this,
                    "Email already exists or account creation failed",
                    Toast.LENGTH_LONG).show();
        }
    }
}
