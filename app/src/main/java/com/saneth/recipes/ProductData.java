package com.saneth.recipes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;
import static com.saneth.recipes.Constants.AVAILABILITY;
import static com.saneth.recipes.Constants.DESCRIPTION;
import static com.saneth.recipes.Constants.PRICE;
import static com.saneth.recipes.Constants.PRODUCT_NAME;
import static com.saneth.recipes.Constants.TABLE_NAME;
import static com.saneth.recipes.Constants.WEIGHT;


public class ProductData extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "products.db";
    private static final int DATABASE_VERSION = 1;


    public ProductData(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL( " CREATE TABLE " + TABLE_NAME + " ( "
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                + PRODUCT_NAME + " TEXT , "
                + WEIGHT + " DOUBLE ,"
                + PRICE +" DOUBLE ,"
                + DESCRIPTION +" TEXT ,"
                + AVAILABILITY + " TEXT ) ; " );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

}
