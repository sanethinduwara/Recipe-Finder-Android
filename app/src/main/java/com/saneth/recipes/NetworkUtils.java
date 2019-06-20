package com.saneth.recipes;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtils {

    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();

    private static final String BASE_URL =  "https://www.food2fork.com/api/search?";
    private static final String API_KEY = "key";
    private static final String QUERY_PARAM = "q";
    private static final String PAGE_NO = "page";


    static String getRecipes(String queryString) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String recipesJSONString = null;

        try {
            // Build the full query URI with parameters
            Uri builtURI = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(API_KEY, "f75e3abc90a20064365e8da9a130b979")
                    .appendQueryParameter(QUERY_PARAM, queryString)
                    .appendQueryParameter(PAGE_NO, "1")
                    .build();

            // Convert the URI to a URL.
            URL requestURL = new URL(builtURI.toString());

            // Open the network connection.
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Get the InputStream.
            InputStream inputStream = urlConnection.getInputStream();

            // Create a buffered reader from that input stream.
            reader = new BufferedReader(new InputStreamReader(inputStream));

            // Use a StringBuilder to hold the incoming response.
            StringBuilder builder = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                // Add the current line to the string.
                builder.append(line);
                builder.append("\n");
            }

            if (builder.length() == 0) {
                // Stream was empty.  Exit without parsing.
                return null;
            }

            recipesJSONString = builder.toString();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Close the connection and the buffered reader.
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // Write the final JSON response to the log
        Log.d(LOG_TAG, recipesJSONString);

        return recipesJSONString;
    }
}