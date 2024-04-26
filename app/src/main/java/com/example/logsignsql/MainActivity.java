package com.example.logsignsql;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText editTextName, editTextPrice;
    Button buttonAdd, buttonView, buttonUpdate, buttonDelete;
    DatabaseHelper myDB;

//    public static MainActivity ma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextName = findViewById(R.id.editTextName);
        editTextPrice = findViewById(R.id.editTextPrice);
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonView = findViewById(R.id.buttonView);
//        buttonUpdate = findViewById(R.id.buttonUpdate);
//        buttonDelete = findViewById(R.id.buttonDelete);
        myDB = new DatabaseHelper(this);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString().trim();
                String price = editTextPrice.getText().toString().trim();

                if (name.isEmpty() || price.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                } else {
                    boolean isInserted = myDB.insertProduk(name, price);
                    if (isInserted) {
                        Toast.makeText(MainActivity.this, "Product inserted successfully", Toast.LENGTH_SHORT).show();
                        // Mengosongkan field setelah data dimasukkan
                        editTextName.setText("");
                        editTextPrice.setText("");
                    } else {
                        Toast.makeText(MainActivity.this, "Failed to insert product", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Arahkan ke ListView
                Intent intent = new Intent(MainActivity.this, ListView.class);
                startActivity(intent);
            }
        });







    }
}
