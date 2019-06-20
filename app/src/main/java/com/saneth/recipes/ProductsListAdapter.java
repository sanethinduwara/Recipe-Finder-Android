package com.saneth.recipes;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;


public class ProductsListAdapter extends ArrayAdapter {
    //to reference the Activity
    private final Activity context;



    private final ArrayList<String> nameArray;              //to store the list of product names
    private final ArrayList<Boolean> isCheckedList;         //maintains the state of all checkboxes

    public ProductsListAdapter(Activity context, ArrayList<String> nameArrayParam) {

        super(context, R.layout.product_item, nameArrayParam);
        this.context = context;
        this.nameArray = nameArrayParam;
        this.isCheckedList = new ArrayList<>();

    }




    @Override
    public View getView(final int position, View rowView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        rowView = inflater.inflate(R.layout.product_item, parent, false);

        TextView nameTextField = (TextView) rowView.findViewById(R.id.name_txt1);
        CheckBox checkBox = (CheckBox) rowView.findViewById(R.id.check_box);

        checkBox.setChecked(isCheckedList.get(position));       //checking the check box considering it's state
        nameTextField.setText(nameArray.get(position));         //displaying product name
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isCheckedList.set(position, isChecked);     //setting the new state of checkbox
            }
        });

        return rowView;
    }


    @Override
    public int getCount() {
        return nameArray.size();
    }


    @Override
    public Object getItem(int position) {
        return nameArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    //function to initiate checkbox state
    public void setAllChecked(boolean checked) {

        for (int i = 0; i < nameArray.size(); i++) {
            isCheckedList.add(checked);
        }
    }
}






