package com.saneth.recipes;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import static com.saneth.recipes.Constants.AVAILABILITY;
import static com.saneth.recipes.Constants.DESCRIPTION;
import static com.saneth.recipes.Constants.PRICE;
import static com.saneth.recipes.Constants.PRODUCT_NAME;
import static com.saneth.recipes.Constants.TABLE_NAME;
import static com.saneth.recipes.Constants.WEIGHT;

public class ProductDetails extends AppCompatActivity {

    private EditText name_txt, weight_txt, price_txt, description_txt;
    private ProductData products;
    private String productName, description, availability;
    private double weight, price;
    private Spinner spinner;
    private ImageButton delete_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        //Hide status bar if android version is Kitkat or higher than Kitkat
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        name_txt = findViewById(R.id.name_txt);
        weight_txt = findViewById(R.id.weight_txt);
        price_txt = findViewById(R.id.price_txt);
        description_txt = findViewById(R.id.description_txt);
        spinner = findViewById(R.id.spinner);
        delete_btn = findViewById(R.id.delete_btn);

        //setting options for the spinner
        ArrayAdapter adapter = ArrayAdapter.createFromResource(
                this,
                R.array.spinnerOptions,
                R.layout.spinner_item);

        spinner.setAdapter(adapter);

        //show confirmation dialog when delete button clicked
        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlert();
            }
        });


        products = new ProductData(this);

        productName = getIntent().getStringExtra("product.name");
        getProductDetails(productName);

        name_txt.setText(productName);
        weight_txt.setText("" + weight);
        price_txt.setText(price + "");
        description_txt.setText(description);
        if (availability.equals("Available")) {
            spinner.setSelection(0);
        } else {
            spinner.setSelection(1);
        }
    }


    //function to update records
    public void updateProduct(View view) {
        SQLiteDatabase db = products.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PRODUCT_NAME, name_txt.getText().toString().trim());
        values.put(WEIGHT, weight_txt.getText().toString().trim());
        values.put(PRICE, price_txt.getText().toString().trim());
        values.put(DESCRIPTION, description_txt.getText().toString().trim());
        values.put(AVAILABILITY, spinner.getSelectedItem().toString().trim());

        db.update(TABLE_NAME, values, "name= '" + productName + "'", null);
        Toast.makeText(this, "Successfully Updated", Toast.LENGTH_SHORT).show();
    }

    //function to get details of a selected product
    private void getProductDetails(String productName) {

        SQLiteDatabase db = products.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT "
                + PRODUCT_NAME + ", "
                + WEIGHT + ", "
                + PRICE + ", "
                + DESCRIPTION + ", "
                + AVAILABILITY
                + " FROM " + TABLE_NAME
                + " WHERE " + PRODUCT_NAME + "='" + productName + "'", null);

        while (cursor.moveToNext()) {
            weight = cursor.getDouble(1);
            price = cursor.getDouble(2);
            description = cursor.getString(3);
            availability = cursor.getString(4);
        }
        cursor.close();
    }

    //function to delete records
    private void deleteProduct() {
        SQLiteDatabase db = products.getReadableDatabase();
        db.delete(TABLE_NAME, PRODUCT_NAME + "='" + productName + "'", null);
    }

    //function to show confirmation alert when deleting a product
    public void showAlert() {

        AlertDialog.Builder myAlertBuilder = new
                AlertDialog.Builder(ProductDetails.this);

        myAlertBuilder.setTitle("Confirm Delete");
        myAlertBuilder.setMessage("Are You Sure You Want to Delete this Product");

        myAlertBuilder.setPositiveButton("OK", new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteProduct();
                        Toast.makeText(getApplicationContext(), "Successfully Deleted", Toast.LENGTH_SHORT).show();
                        onBackPressed();

                    }
                });
        myAlertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        myAlertBuilder.show();

    }
}
