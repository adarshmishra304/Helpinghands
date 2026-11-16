
package com.example.helpinghands.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helpinghands.R;
import com.example.helpinghands.database.DatabaseHelper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends Activity {

    TextInputEditText etName, etEmail, etPhone;
    TextInputEditText etPassword, etConfirmPassword;

    RadioGroup rgRole;
    MaterialButton btnCreateAccount;
    TextView tvLogin;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize views
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etRegisterEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etRegisterPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        rgRole = findViewById(R.id.rgRole);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);
        tvLogin = findViewById(R.id.tvLogin);

        // Initialize database
        databaseHelper = new DatabaseHelper(this);

        // Registration button
        btnCreateAccount.setOnClickListener(v -> {

            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();

            String password = etPassword.getText().toString();
            String confirmPassword =
                    etConfirmPassword.getText().toString();

            // Check empty fields
            if (name.isEmpty() || email.isEmpty()
                    || phone.isEmpty() || password.isEmpty()
                    || confirmPassword.isEmpty()) {

                Toast.makeText(this,
                        "Please fill all fields",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // Validate email
            if (!android.util.Patterns.EMAIL_ADDRESS
                    .matcher(email).matches()) {

                etEmail.setError("Enter a valid email");
                return;
            }

            // Validate phone
            if (!phone.matches("[0-9]{10}")) {
                etPhone.setError(
                        "Enter a valid 10-digit phone number");
                return;
            }

            // Validate password
            if (password.length() < 8) {
                etPassword.setError(
                        "Password must contain at least 8 characters");
                return;
            }

            // Confirm password
            if (!password.equals(confirmPassword)) {
                etConfirmPassword.setError(
                        "Passwords do not match");
                return;
            }

            // Check selected role
            int selectedRole = rgRole.getCheckedRadioButtonId();

            if (selectedRole == -1) {
                Toast.makeText(this,
                        "Please select account type",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // Get role
            String role;

            if (selectedRole == R.id.rbDonor) {
                role = "DONOR";
            } else {
                role = "BENEFICIARY";
            }

            // Save user in SQLite database
            boolean inserted = databaseHelper.registerUser(
                    name,
                    email,
                    phone,
                    password,
                    role
            );

            if (inserted) {

                Toast.makeText(this,
                        "Registration successful!",
                        Toast.LENGTH_SHORT).show();

                // Return to login screen
                finish();

            } else {

                Toast.makeText(this,
                        "Email already exists or registration failed",
                        Toast.LENGTH_LONG).show();
            }
        });

        // Navigate to login screen
        tvLogin.setOnClickListener(v -> {

            startActivity(new Intent(
                    RegisterActivity.this,
                    LoginActivity.class
            ));

            finish();
        });
    }
}
