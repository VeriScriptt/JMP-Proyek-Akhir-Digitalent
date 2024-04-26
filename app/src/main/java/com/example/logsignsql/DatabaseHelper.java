package com.example.logsignsql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.Selection;
import android.util.Log;

import androidx.annotation.Nullable;
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String databaseName = "SignLog.db";

    public DatabaseHelper(@Nullable Context context) {
        super(context, databaseName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDatabase) {
        // Create users table
        MyDatabase.execSQL("CREATE TABLE users(email TEXT PRIMARY KEY, password TEXT)");

        // Create produk table
        MyDatabase.execSQL("CREATE TABLE IF NOT EXISTS produk(id INTEGER PRIMARY KEY AUTOINCREMENT ,name TEXT , price TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {
        MyDB.execSQL("DROP TABLE IF EXISTS users");
        MyDB.execSQL("DROP TABLE IF EXISTS produk");
        onCreate(MyDB);
    }

    public Boolean insertData(String email, String password) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("password", password);
        long result = MyDatabase.insert("users", null, contentValues);
        return result != -1;
    }

    public Boolean checkEmail(String email) {
        SQLiteDatabase MyDatabase = this.getReadableDatabase();
        Cursor cursor = MyDatabase.rawQuery("SELECT * FROM users WHERE email = ?", new String[]{email});
        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }

    public Boolean checkEmailPassword(String email, String password) {
        SQLiteDatabase MyDatabase = this.getReadableDatabase();
        Cursor cursor = MyDatabase.rawQuery("SELECT * FROM users WHERE email = ? AND password = ?", new String[]{email, password});
        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }

//    public Boolean insertProduk(String name, String price) {
//        SQLiteDatabase MyDatabase = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("name", name);
//        contentValues.put("price", price);
//        long result = MyDatabase.insert("produk", null, contentValues);
//        return result != -1;
//    }

    public boolean insertProduk(String name, String price) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("price", price);
        long result = MyDatabase.insert("produk", null, contentValues);
        if (result == -1) {
            Log.e("DatabaseHelper", "Failed to insert product: " + name);
            return false;
        } else {
            Log.d("DatabaseHelper", "Product inserted successfully: " + name);
            return true;
        }
    }

//    public Cursor getAllProduk() {
//        SQLiteDatabase db = this.getReadableDatabase();
//        return db.query("produk", null, null, null, null, null, null);
//    }

    // Dalam DatabaseHelper.java
    public boolean deletedata(int productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete("produk", "id = ?", new String[]{String.valueOf(productId)});
        db.close();
        return rowsAffected > 0;
    }
    public int getProductId(String productName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM produk WHERE name = ?", new String[]{productName});
        if (cursor.moveToFirst()) {
            int productId = cursor.getInt(cursor.getColumnIndex("id"));
            cursor.close();
            return productId;
        } else {
            cursor.close();
            return -1; // Jika nama produk tidak ditemukan
        }
    }

    public boolean updateData(String oldName, String newName, String newPrice) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", newName);
        contentValues.put("price", newPrice);
        int result = db.update("produk", contentValues, "name = ?", new String[]{oldName});
        return result > 0;
    }
    public Cursor getDataByName(String productName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"name", "price"}; // Kolom yang ingin Anda ambil dari tabel produk
        String selection = "name = ?"; // Kondisi WHERE untuk memilih produk berdasarkan nama
        String[] selectionArgs = {productName}; // Nilai yang akan digunakan dalam kondisi WHERE
        return db.query("produk", columns, selection, selectionArgs, null, null, null);
    }





}
