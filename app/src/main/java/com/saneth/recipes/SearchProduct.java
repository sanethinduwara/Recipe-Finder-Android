package com.saneth.recipes;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.saneth.recipes.Constants.DESCRIPTION;
import static com.saneth.recipes.Constants.PRODUCT_NAME;
import static com.saneth.recipes.Constants.TABLE_NAME;

public class SearchProduct extends AppCompatActivity {

    private static final String[] SELECT_NAME = {PRODUCT_NAME} ;
    private static final String[] SELECT = {PRODUCT_NAME, DESCRIPTION};
    private EditText search_txt;
    private Button search_btn;
    private ProductData products;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        search_btn = findViewById(R.id.search_btn);
        search_txt = findViewById(R.id.search_txt);
        listView = findViewById(R.id.listView);
        listView.setFadingEdgeLength(150);

        products = new ProductData(this);


    }

    private ArrayList<Product> getProductDetails(){

        SQLiteDatabase db = products.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,SELECT,null,null,null,null,PRODUCT_NAME);

        ArrayList<Product> product_details = new ArrayList<>();

        while (cursor.moveToNext()) {
            String name = cursor.getString(0);
            String description = cursor.getString(1);
            Product product = new Product(name,description,null,0,0);
            product_details.add(product);

        }
        cursor.close();
        return product_details;
    }



    private ArrayList<String> searchResults(String term){

        ArrayList<Product> products = getProductDetails();
        ArrayList<String> filtered_names = new ArrayList<>();


        for (int i = 0; i<products.size();i++){     //iterating through all products

            String name = products.get(i).getName();
            String desc = products.get(i).getDescription();

                if ((name.toLowerCase().contains(term)) || (desc.toLowerCase().contains(term))) {
                   filtered_names.add(name);        //adding to search list if the search term is contained in the product title or the description
                }
        }

        return filtered_names;      //return item names of items that match with the search query
    }

    public void searchProduct(View view) {

        // Hide the keyboard when the button is pushed.
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }

        String search_term = search_txt.getText().toString().toLowerCase().trim();      //remove spaces in the beginning and end


        ArrayList<String> filterd_results = searchResults(search_term);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, filterd_results){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);

                //styling the list item
                text.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.list_item_style));
                text.setTextSize(18);
                text.setTextColor(Color.GRAY);

                return view;
            }
        };
        listView.setAdapter(arrayAdapter);

        if (filterd_results.size()==0){
            Toast.makeText(this,"No Results Found",Toast.LENGTH_LONG).show();       //displaying toast if no results found
        }



    }
}
