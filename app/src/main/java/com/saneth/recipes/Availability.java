package com.saneth.recipes;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.saneth.recipes.Constants.AVAILABILITY;
import static com.saneth.recipes.Constants.PRODUCT_NAME;
import static com.saneth.recipes.Constants.TABLE_NAME;

public class Availability extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ProductData products;
    private ListView listView;
    ProductsListAdapter listAdapter;
    private ArrayList<String> product_names, availability_status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_availability);

        //Hide status bar if android version is Kitkat or higher than Kitkat
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        listView = findViewById(R.id.listView);

        products = new ProductData(this);


        product_names = getAvailableProductNames();



        availability_status = new ArrayList<>();

        for (int i =0;i<product_names.size();i++) {
            availability_status.add("Available");
        }
        listAdapter = new ProductsListAdapter(this, product_names);
        listAdapter.setAllChecked(true);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(this);
        listView.setFadingEdgeLength(150);
    }

    //function to get available products from the database
    private ArrayList<String> getAvailableProductNames() {

        ArrayList<String> product_names = new ArrayList<>();

        SQLiteDatabase db = products.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT "+PRODUCT_NAME+", "+AVAILABILITY+" FROM "+TABLE_NAME+" WHERE availability='Available'", null);

        while (cursor.moveToNext()) {
            String name = cursor.getString(0);
            product_names.add(name);
        }
        return product_names;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        CheckBox checkBox = view.findViewById(R.id.check_box);

        checkBox.setChecked(!checkBox.isChecked());     //changing the check box state when clicked

        String status;
        //updating the availability state of items in the availability_status list
        if (checkBox.isChecked()) {
            status = "Available";

        } else {
            status = "Not Available";
        }

        availability_status.set( position,status);
    }

    //function to update availability state of products in database
    public void setAvailability(View view) {

        SQLiteDatabase db = products.getWritableDatabase();

        for (int i = 0;i<availability_status.size();i++ ) {
            ContentValues values = new ContentValues();
            values.put(AVAILABILITY, availability_status.get(i));
            db.update(TABLE_NAME, values, "name='" + product_names.get(i) + "'", null);
            Toast.makeText(this, "Successfully Saved", Toast.LENGTH_LONG).show();

        }
    }
}
