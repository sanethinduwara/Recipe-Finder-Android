package com.saneth.recipes;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class RecipeList extends AppCompatActivity {

    private String q;
    private ListView listView;
    private TextView title_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        //Hide status bar if android version is Kitkat or higher than Kitkat
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        listView = findViewById(R.id.listView);
        title_txt = findViewById(R.id.heading);
        listView.setFadingEdgeLength(50);

        q = getIntent().getStringExtra("recipe.params.q");
        Log.d("query",q);

        searchRecipes();
    }

    public void searchRecipes() {

        // Check the status of the network connection.
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }

        // If the network is available, connected, and the search field
        // is not empty, start a  AsyncTask
        if (networkInfo != null && networkInfo.isConnected()
                && q.length() != 0) {
            new FetchRecipes(this,listView,title_txt).execute(q);

        }

        else {
            if (q.length() == 0) {
                Toast.makeText(this,"Select at least one item",Toast.LENGTH_LONG).show();       //displaying toast if no items selected to query
            } else {
                Toast.makeText(this,"Please check your internet connection",Toast.LENGTH_LONG).show();      //displaying toast if something is wrong with the internet connection
            }
        }
    }
}