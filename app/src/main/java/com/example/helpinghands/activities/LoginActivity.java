
package com.example.helpinghands.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helpinghands.R;
import com.example.helpinghands.database.DatabaseHelper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends Activity {

    TextInputEditText etLoginEmail, etLoginPassword;
    MaterialButton btnLogin;
    TextView tvCreateAccount;

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        etLoginEmail = findViewById(R.id.etLoginEmail);
        etLoginPassword = findViewById(R.id.etLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvCreateAccount = findViewById(R.id.tvCreateAccount);

        // Initialize database
        databaseHelper = new DatabaseHelper(this);

        // Login button
        btnLogin.setOnClickListener(v -> {

            String email = etLoginEmail.getText().toString().trim();
            String password = etLoginPassword.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this,
                        "Please enter email and password",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // Validate email format
            if (!android.util.Patterns.EMAIL_ADDRESS
                    .matcher(email).matches()) {
                etLoginEmail.setError("Enter a valid email");
                return;
            }

            // Authenticate user
            int userId = databaseHelper.loginUser(email, password);

            if (userId != -1) {

                // Get account role
                String role = databaseHelper.getUserRole(userId);

                if (role == null) {
                    Toast.makeText(this,
                            "Unable to identify account type",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(this,
                        "Login successful!",
                        Toast.LENGTH_SHORT).show();

                // Navigate according to role
                Intent intent;

                if (role.equals("DONOR")) {

                    intent = new Intent(
                            LoginActivity.this,
                            DonorDashboardActivity.class
                    );

                } else if (role.equals("BENEFICIARY")) {

                    intent = new Intent(
                            LoginActivity.this,
                            BeneficiaryDashboardActivity.class
                    );

                } else if (role.equals("ADMIN")) {

                    intent = new Intent(
                            LoginActivity.this,
                            AdminDashboardActivity.class
                    );

                } else {

                    Toast.makeText(this,
                            "Unknown account type",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                intent.putExtra("USER_ID", userId);
                intent.putExtra("USER_ROLE", role);
                intent.putExtra("USER_EMAIL", email);

                startActivity(intent);
                finish();

            } else {

                Toast.makeText(this,
                        "Invalid email or password",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Navigate to registration
        tvCreateAccount.setOnClickListener(v -> {

            startActivity(new Intent(
                    LoginActivity.this,
                    RegisterActivity.class
            ));
        });
    }
}
