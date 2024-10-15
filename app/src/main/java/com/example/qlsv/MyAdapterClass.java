package com.example.qlsv;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MyAdapterClass extends ArrayAdapter<Room> {
    ArrayList<Room> classlist = new ArrayList<Room>();

    public MyAdapterClass(Context context, int resource, ArrayList<Room> objects){
        super(context, resource, objects);
        classlist = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.myclass_layout2, null);
        TextView txtcodeclass = (TextView) v.findViewById(R.id.txtclasscode);
        TextView txtnameclass = (TextView) v.findViewById(R.id.txtclassname);
        TextView txtnumberclass = (TextView) v.findViewById(R.id.txtclassnumber);

        if(position==0){
            txtcodeclass.setBackgroundColor(Color.WHITE);
            txtnameclass.setBackgroundColor(Color.WHITE);
            txtnumberclass.setBackgroundColor(Color.WHITE);
        }
        txtcodeclass.setText(classlist.get(position).getCode_class());
        txtnameclass.setText(classlist.get(position).getName_class());
        txtnumberclass.setText(classlist.get(position).getClass_number());
        return v;
    }
}
