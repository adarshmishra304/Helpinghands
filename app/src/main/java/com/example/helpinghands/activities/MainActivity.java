package com.example.helpinghands.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

import com.example.helpinghands.R;
import com.example.helpinghands.database.DatabaseHelper;
import com.google.android.material.button.MaterialButton;


public class MainActivity extends Activity {

    MaterialButton btnLogin, btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Admin privileges

//
//        startActivity(new Intent(
//                MainActivity.this,
//                AdminSetupActivity.class
//        ));




        //database

        DatabaseHelper dbHelper = new DatabaseHelper(this);

        SQLiteDatabase database = dbHelper.getWritableDatabase();

        Toast.makeText(
                this,
                "Helping Hands Database Created!",
                Toast.LENGTH_SHORT
        ).show();




        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    LoginActivity.class
            );
            startActivity(intent);
        });

        btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    RegisterActivity.class
            );
            startActivity(intent);
        });
    }
}