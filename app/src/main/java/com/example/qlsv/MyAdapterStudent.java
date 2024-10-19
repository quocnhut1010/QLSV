package com.example.qlsv;

import static android.view.View.inflate;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MyAdapterStudent extends ArrayAdapter<Student> {
    ArrayList<Student> studentList = new ArrayList<>();

    public MyAdapterStudent(@NonNull Context context, int resource, @NonNull ArrayList<Student> objects) {
        super(context, resource, objects);
        studentList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.my_student_layout, null);
        ImageView imgstudent = (ImageView) v.findViewById(R.id.imageStudent);
        TextView txtclassstudent = (TextView) v.findViewById(R.id.txtstudentclass);
        TextView txtnamestudent = (TextView) v.findViewById(R.id.txtstudentname);
        TextView txtbirthdaystudent = (TextView) v.findViewById(R.id.txtstudentbirthday);
        TextView txtgenderstudent = (TextView) v.findViewById(R.id.txtstudentgender);
        TextView txtaddressstudent = (TextView) v.findViewById(R.id.txtstudentaddress);
        if (position == 0) {
            txtclassstudent.setBackgroundColor(Color.WHITE);
            txtnamestudent.setBackgroundColor(Color.WHITE);
            txtbirthdaystudent.setBackgroundColor(Color.WHITE);
            txtgenderstudent.setBackgroundColor(Color.WHITE);
            txtaddressstudent.setBackgroundColor(Color.WHITE);
        }
        imgstudent.setImageResource(R.drawable.ic_launcher_round);
        txtclassstudent.setText("Mã lớp: " + studentList.get(position).getName_class());
        txtnamestudent.setText("Tên sinh viên: " + studentList.get(position).getName_student());
        txtbirthdaystudent.setText("Ngày sinh: " + studentList.get(position).getBirthday_student());
        txtgenderstudent.setText("Gioi tinh: " + studentList.get(position).getGender_student());
        txtaddressstudent.setText("Địa chỉ: " + studentList.get(position).getAddress_student());
        return v;

    }
}

