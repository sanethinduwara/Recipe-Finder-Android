package com.saneth.recipes;


import android.content.Intent;
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

public class Recipes extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static String[] SELECT = {PRODUCT_NAME};
    private ProductData products;
    private ListView listView;
    ProductsListAdapter listAdapter;
    private ArrayList<String> product_names, availability_status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        //Hide status bar if android version is Kitkat or higher than Kitkat
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        listView = findViewById(R.id.listView);
        listView.setFadingEdgeLength(150);

        products = new ProductData(this);


        product_names = getAvailableProductNames();



        availability_status = new ArrayList<String>();

        for (int i =0;i<product_names.size();i++) {
            availability_status.add("Available");
        }
        listAdapter = new ProductsListAdapter(this, product_names);
        listAdapter.setAllChecked(true);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(this);
    }

    //function to get available items from the database
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

        checkBox.setChecked(!checkBox.isChecked());     //changing state of check box when clicked

        String status;
        if (checkBox.isChecked()) {
            status = "Available";

        } else {
            status = "Not Available";
        }

        availability_status.set( position,status);
    }

    //function to get the value for parameter q
    public String getProductsToUse(){

        StringBuilder builder = new StringBuilder();

         for (int i = 0; i<availability_status.size();i++){
             if (availability_status.get(i).equals("Available")){
                 builder.append(product_names.get(i)).append(",");
             }
         }

         String params = String.valueOf(builder);
         if (!params.equals("")) {
             params = params.substring(0, params.length() - 1);     //removing the comma at the end of string
         }

         return params;
    }

    //function to start Activity RecipeList
    public void findRecipes(View view) {
        String params = getProductsToUse();
        if (!params.equals("")) {
            Intent intent = new Intent(Recipes.this,RecipeList.class);
            intent.putExtra("recipe.params.q",params);
            startActivity(intent);
        }else {
            Toast.makeText(this,"Select at least one item",Toast.LENGTH_LONG).show();       //displaying toast if no items selected to query
        }
        }

    }

