package com.saneth.recipes;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.saneth.recipes.Constants.PRODUCT_NAME;
import static com.saneth.recipes.Constants.TABLE_NAME;

public class EditProduct extends ListActivity {

    private static final String[] SELECT = {PRODUCT_NAME};
    private ArrayList<String> listItems = new ArrayList<String>();
    private ProductData products;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        //Hide status bar if android version is Kitkat or higher than Kitkat
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }


        products = new ProductData(this);

        listItems = getProductNames();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, listItems) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.list_item_style));
                text.setTextSize(18);
                text.setTextColor(Color.GRAY);


                return view;
            }
        };
        setListAdapter(arrayAdapter);
        getListView().setFadingEdgeLength(150);
        arrayAdapter.notifyDataSetChanged();
    }

    //function to get product names from the database
    private ArrayList<String> getProductNames() {

        SQLiteDatabase db = products.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, SELECT, null, null, null, null, PRODUCT_NAME);

        ArrayList<String> product_names = new ArrayList<>();

        while (cursor.moveToNext()) {
            String name = cursor.getString(0);
            product_names.add(name);
        }
        return product_names;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        String productName = l.getItemAtPosition(position).toString();      //getting the name of selected product
        Intent intent = new Intent(EditProduct.this, ProductDetails.class);
        intent.putExtra("product.name", productName);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.onCreate(null);        //recreating the view to update list
    }
}
