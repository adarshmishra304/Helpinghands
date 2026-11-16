
package com.example.helpinghands.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "HelpingHands.db";
    private static final int DATABASE_VERSION = 1;

    // Users table
    public static final String TABLE_USERS = "users";

    public static final String COL_USER_ID = "user_id";
    public static final String COL_NAME = "name";
    public static final String COL_EMAIL = "email";
    public static final String COL_PHONE = "phone";
    public static final String COL_PASSWORD = "password_hash";
    public static final String COL_ROLE = "role";

    // Assistance requests table
    public static final String TABLE_REQUESTS = "assistance_requests";

    // Donations table
    public static final String TABLE_DONATIONS = "donations";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create users table
        String createUsersTable =
                "CREATE TABLE " + TABLE_USERS + " (" +
                        COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COL_NAME + " TEXT NOT NULL, " +
                        COL_EMAIL + " TEXT NOT NULL UNIQUE, " +
                        COL_PHONE + " TEXT NOT NULL, " +
                        COL_PASSWORD + " TEXT NOT NULL, " +
                        COL_ROLE + " TEXT NOT NULL CHECK (" +
                        COL_ROLE + " IN ('DONOR', 'BENEFICIARY', 'ADMIN'))" +
                        ")";

        db.execSQL(createUsersTable);

        // Create assistance requests table
        String createRequestsTable =
                "CREATE TABLE " + TABLE_REQUESTS + " (" +
                        "request_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "beneficiary_id INTEGER NOT NULL, " +
                        "title TEXT NOT NULL, " +
                        "description TEXT NOT NULL, " +
                        "amount_needed REAL NOT NULL, " +
                        "status TEXT DEFAULT 'PENDING', " +
                        "admin_note TEXT, " +
                        "created_at DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                        "FOREIGN KEY(beneficiary_id) REFERENCES users(user_id)" +
                        ")";

        db.execSQL(createRequestsTable);

        // Create donations table
        String createDonationsTable =
                "CREATE TABLE " + TABLE_DONATIONS + " (" +
                        "donation_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "donor_id INTEGER NOT NULL, " +
                        "request_id INTEGER NOT NULL, " +
                        "amount REAL NOT NULL, " +
                        "donation_date DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                        "payment_status TEXT DEFAULT 'PENDING', " +
                        "FOREIGN KEY(donor_id) REFERENCES users(user_id), " +
                        "FOREIGN KEY(request_id) REFERENCES assistance_requests(request_id)" +
                        ")";

        db.execSQL(createDonationsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DONATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REQUESTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);

        onCreate(db);
    }

    // Hash password using SHA-256
    private String hashPassword(String password) {

        try {
            MessageDigest digest =
                    MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(
                    password.getBytes(java.nio.charset.StandardCharsets.UTF_8)
            );

            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);

                if (hex.length() == 1) {
                    hexString.append('0');
                }

                hexString.append(hex);
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    // Register a new user
    // Returns true if registration succeeds
    public boolean registerUser(
            String name,
            String email,
            String phone,
            String password,
            String role) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COL_NAME, name);
        values.put(COL_EMAIL, email.trim().toLowerCase());
        values.put(COL_PHONE, phone);
        values.put(COL_PASSWORD, hashPassword(password));
        values.put(COL_ROLE, role);

        long result = db.insert(TABLE_USERS, null, values);

        return result != -1;
    }

    // Authenticate user
    // Returns user ID if login succeeds, otherwise -1
    public int loginUser(String email, String password) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_USERS,
                new String[]{COL_USER_ID},
                COL_EMAIL + "=? AND " + COL_PASSWORD + "=?",
                new String[]{
                        email.trim().toLowerCase(),
                        hashPassword(password)
                },
                null,
                null,
                null
        );

        int userId = -1;

        if (cursor.moveToFirst()) {
            userId = cursor.getInt(0);
        }

        cursor.close();

        return userId;
    }

    // Get user role using user ID
    public String getUserRole(int userId) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_USERS,
                new String[]{COL_ROLE},
                COL_USER_ID + "=?",
                new String[]{String.valueOf(userId)},
                null,
                null,
                null
        );

        String role = null;

        if (cursor.moveToFirst()) {
            role = cursor.getString(0);
        }

        cursor.close();

        return role;
    }


    public boolean submitAssistanceRequest(
            int beneficiaryId,
            String title,
            String description,
            double amountNeeded) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("beneficiary_id", beneficiaryId);
        values.put("title", title);
        values.put("description", description);
        values.put("amount_needed", amountNeeded);
        values.put("status", "PENDING");

        long result = db.insert(TABLE_REQUESTS, null, values);

        return result != -1;
    }


    public Cursor getBeneficiaryRequests(int beneficiaryId) {

        SQLiteDatabase db = this.getReadableDatabase();

        return db.query(
                TABLE_REQUESTS,
                new String[]{
                        "request_id",
                        "title",
                        "description",
                        "amount_needed",
                        "status",
                        "created_at"
                },
                "beneficiary_id=?",
                new String[]{String.valueOf(beneficiaryId)},
                null,
                null,
                "request_id DESC"
        );
    }


    public Cursor getAllAssistanceRequests() {

        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(
                "SELECT assistance_requests.*, users.name, users.email " +
                        "FROM assistance_requests " +
                        "INNER JOIN users " +
                        "ON assistance_requests.beneficiary_id = users.user_id " +
                        "ORDER BY assistance_requests.request_id DESC",
                null
        );
    }

    public boolean updateRequestStatus(
            int requestId,
            String status,
            String adminNote) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("status", status);
        values.put("admin_note", adminNote);

        int result = db.update(
                TABLE_REQUESTS,
                values,
                "request_id=?",
                new String[]{String.valueOf(requestId)}
        );

        return result > 0;
    }

    public boolean createAdminAccount(
            String name,
            String email,
            String phone,
            String password) {

        SQLiteDatabase db = this.getWritableDatabase();

        // Check whether this email already exists
        Cursor cursor = db.query(
                TABLE_USERS,
                new String[]{COL_USER_ID},
                COL_EMAIL + "=?",
                new String[]{email.trim().toLowerCase()},
                null,
                null,
                null
        );

        boolean emailExists = cursor.moveToFirst();
        cursor.close();

        if (emailExists) {
            return false;
        }

        ContentValues values = new ContentValues();

        values.put(COL_NAME, name);
        values.put(COL_EMAIL, email.trim().toLowerCase());
        values.put(COL_PHONE, phone);
        values.put(COL_PASSWORD, hashPassword(password));
        values.put(COL_ROLE, "ADMIN");

        long result = db.insert(TABLE_USERS, null, values);

        return result != -1;
    }


    public Cursor getApprovedRequests() {

        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(
                "SELECT assistance_requests.*, users.name " +
                        "FROM assistance_requests " +
                        "INNER JOIN users " +
                        "ON assistance_requests.beneficiary_id = users.user_id " +
                        "WHERE assistance_requests.status = 'APPROVED' " +
                        "ORDER BY assistance_requests.request_id DESC",
                null
        );
    }

}
