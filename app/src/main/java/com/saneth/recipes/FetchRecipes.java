package com.saneth.recipes;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;


public class FetchRecipes extends AsyncTask<String, Void, String> implements AdapterView.OnItemClickListener {

    private WeakReference<ListView> listView;
    private WeakReference<Context> context;
    private ArrayList<String> recipe_titles, recipe_source_urls;
    private WeakReference<TextView> textView;

    FetchRecipes(Context context, ListView lv, TextView tv) {
        this.listView = new WeakReference<>(lv);
        this.context = new WeakReference<>(context);
        this.textView =new WeakReference<>(tv);
    }


    @Override
    protected String doInBackground(String... strings) {
        return NetworkUtils.getRecipes(strings[0]);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
        textView.get().setText("Finding Recipes...");

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {

            listView.get().setOnItemClickListener(this);

            int i = 0;
            recipe_titles = new ArrayList<>();
            recipe_source_urls = new ArrayList<>();

            JSONObject jsonObject = new JSONObject(s);
            String result_count = jsonObject.getString("count");        //getting number of recipes found
            textView.get().setText(result_count +" Recipes Found");            //displaying number of recipes found
            JSONArray itemsArray = jsonObject.getJSONArray("recipes");

            while (i < itemsArray.length()) {
                // Get the current item information.
                JSONObject recipe = itemsArray.getJSONObject(i);

                // catch if either field is empty and move on.
                try {
                    String recipe_title = recipe.getString("title");    //getting the title of the recipe
                    recipe_titles.add(recipe_title);        //adding title to recipe name list

                    String recipe_source_url = recipe.getString("source_url");      //getting the source url of the recipe
                    recipe_source_urls.add(recipe_source_url);      //adding title to source url list

                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                            (context.get(), android.R.layout.simple_list_item_1, recipe_titles){
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

                    listView.get().setAdapter(arrayAdapter);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Move to the next item.
                i++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        openRecipe(recipe_source_urls.get(position));       //opening url when the recipe name clicked
    }

    //function to recipe in the browser
    private void openRecipe(String url) {

        Uri webPage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webPage);

        if (intent.resolveActivity(context.get().getPackageManager()) != null) {
            context.get().startActivity(intent);
        } else {
            Toast.makeText(context.get(),"Something Went Wrong",Toast.LENGTH_LONG).show();
        }

    }
}
