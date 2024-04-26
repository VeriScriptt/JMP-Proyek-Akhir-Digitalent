package com.example.logsignsql;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class ListView extends AppCompatActivity {

    android.widget.ListView listView;
    TextView textView;
    DatabaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Daftar Produk");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Menampilkan tombol kembali

        listView = findViewById(R.id.list_view);
        textView = findViewById(R.id.textView);
        myDB = new DatabaseHelper(this);

        displayProdukList();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                final String selection = (String) listView.getItemAtPosition(arg2);
                final CharSequence[] dialogitem = {"Update Produk", "Hapus Produk","Lihat Produk"};
                AlertDialog.Builder builder = new AlertDialog.Builder(ListView.this);
                builder.setTitle("Pilihan");
                builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int item) {
                        switch (item) {
                            case 0:
                                // Lakukan sesuatu untuk mengupdate produk
//                                Toast.makeText(ListView.this, "Mengupdate Produk: " + selection, Toast.LENGTH_SHORT).show();
//                                break;
                                showUpdateDialog(selection);
                                break;
                            // Di tempat yang sesuai dalam kode Anda
                            case 1:
                                // Ambil ID produk dari database berdasarkan nama produk yang dipilih
                                int productId = myDB.getProductId(selection);
                                if (productId != -1) {
                                    if (myDB.deletedata(productId)) { // Menghapus produk dari database
                                        Toast.makeText(ListView.this, "Menghapus Produk: " + selection, Toast.LENGTH_SHORT).show();
                                        recreate(); // Merefresh halaman untuk memperbarui tampilan ListView
                                    } else {
                                        Toast.makeText(ListView.this, "Gagal menghapus Produk: " + selection, Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(ListView.this, "ID Produk tidak ditemukan: " + selection, Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case 2:
                                // Panggil method showProductDetailDialog dengan parameter selection (nama produk yang dipilih)
                                showProductDetailDialog(selection);
                                break;

                        }
                    }
                });
                builder.create().show();
            }
        });
    }


    private void displayProdukList() {
        SQLiteDatabase db = myDB.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM produk", null);

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No data available", Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayList<String> productList = new ArrayList<>();
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
            productList.add(name);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, productList);
        listView.setAdapter(adapter);
    }

    private void showUpdateDialog(final String productName) {
        // Dapatkan data produk dari database berdasarkan nama produk
        Cursor cursor = myDB.getDataByName(productName);
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") String oldName = cursor.getString(cursor.getColumnIndex("name"));
            @SuppressLint("Range") String oldPrice = cursor.getString(cursor.getColumnIndex("price"));

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.update_dialog, null);
            dialogBuilder.setView(dialogView);

            final EditText editTextName = dialogView.findViewById(R.id.editTextName);
            final EditText editTextPrice = dialogView.findViewById(R.id.editTextPrice);
            final Button buttonUpdate = dialogView.findViewById(R.id.buttonUpdate);

            dialogBuilder.setTitle("Update Produk");

            // Tetapkan nilai EditText dengan nilai produk yang diperoleh
            editTextName.setText(oldName);
            editTextPrice.setText(oldPrice);

            final AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();

            buttonUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String newName = editTextName.getText().toString().trim();
                    String newPrice = editTextPrice.getText().toString().trim();
                    if (!TextUtils.isEmpty(newName) && !TextUtils.isEmpty(newPrice)) {
                        boolean isUpdated = myDB.updateData(productName, newName, newPrice);
                        if (isUpdated) {
                            Toast.makeText(ListView.this, "Produk berhasil diperbarui", Toast.LENGTH_SHORT).show();
                            displayProdukList(); // Menyegarkan tampilan ListView
                            alertDialog.dismiss(); // Menutup dialog setelah memperbarui
                        } else {
                            Toast.makeText(ListView.this, "Gagal memperbarui produk", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ListView.this, "Nama dan harga produk harus diisi", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            // Handle ketika data produk tidak ditemukan dalam database
            Toast.makeText(this, "Data produk tidak ditemukan", Toast.LENGTH_SHORT).show();
        }
    }

    // Metode ini akan menangani klik tombol kembali pada action bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: // Kembali
                onBackPressed(); // Menjalankan fungsi onBackPressed() saat tombol kembali ditekan
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showProductDetailDialog(String name) {
        // Dapatkan data produk dari database berdasarkan nama produk yang dipilih
        Cursor cursor = myDB.getDataByName(name);
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") String productName = cursor.getString(cursor.getColumnIndex("name"));
            @SuppressLint("Range") String productPrice = cursor.getString(cursor.getColumnIndex("price"));

            // Membuat pesan yang berisi data produk
            String message = "Nama: " + productName + "\nHarga: " + productPrice;

            // Membuat dialog untuk menampilkan data produk
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ListView.this);
            dialogBuilder.setTitle("Detail Produk");
            dialogBuilder.setMessage(message);
            dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            dialogBuilder.create().show();
        } else {
            // Handle ketika data produk tidak ditemukan dalam database
            Toast.makeText(ListView.this, "Data produk tidak ditemukan", Toast.LENGTH_SHORT).show();
        }
    }



}
