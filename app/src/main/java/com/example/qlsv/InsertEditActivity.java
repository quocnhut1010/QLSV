package com.example.qlsv;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class InsertEditActivity extends Activity {
    Button btnSaveClass, btnClearClass, btnCloseClass;
    EditText edtCode, edtName, edtNumber;
    String id_class;

    private void initWidget() {
        btnSaveClass = findViewById(R.id.btnSaveEditClass);
        btnClearClass = findViewById(R.id.btnClearEditClass);
        btnCloseClass = findViewById(R.id.btnCloseEditClass);
        edtCode = findViewById(R.id.edtEditClassCode);
        edtName = findViewById(R.id.edtEditClassName);
        edtNumber = findViewById(R.id.edtEditClassNumber);
    }

    private void getData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");
        if (bundle != null) {
            Room room = (Room) bundle.getSerializable("room");
            if (room != null) {
                id_class = room.getId_class();
                edtCode.setText(room.getCode_class());
                edtName.setText(room.getName_class());
                edtNumber.setText(room.getClass_number());
                Log.d("InsertEditActivity", "Room Data: " + room.toString()); // Kiểm tra dữ liệu của room
            } else {
                Log.e("InsertEditActivity", "Room is null");
            }
        } else {
            Log.e("InsertEditActivity", "Bundle is null");
        }
    }


    private boolean SaveClass() {
        SQLiteDatabase db = null;
        try {
            db = openOrCreateDatabase(Login.DATABASE_NAME, MODE_PRIVATE, null);
            ContentValues values = new ContentValues();
            values.put("code_class", edtCode.getText().toString());
            values.put("name_class", edtName.getText().toString());
            values.put("number_student", Integer.parseInt(edtNumber.getText().toString()));

            Log.d("InsertEditActivity", "id_class: " + id_class); // Kiểm tra giá trị id_class

            int rowsAffected = db.update("tblclass", values, "id_class=?", new String[]{id_class});
            if (rowsAffected > 0) {
                return true;
            } else {
                Toast.makeText(getApplication(), "Không tìm thấy lớp để cập nhật", Toast.LENGTH_LONG).show();
            }
        } catch (Exception ex) {
            Log.e("InsertEditActivity", "Error updating class: ", ex);
            Toast.makeText(getApplication(), "Cập nhật lớp học không thành công: " + ex.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            if (db != null) {
                db.close(); // Đóng cơ sở dữ liệu
            }
        }
        return false;
    }


    private void clearClass() {
        edtCode.setText("");
        edtName.setText("");
        edtNumber.setText("");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_class_layout2);
        initWidget();
        getData();

        btnSaveClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Intent intent = getIntent();
                if (SaveClass()) {
                    Room r = new Room(id_class, edtCode.getText().toString(),
                            edtName.getText().toString(), edtNumber.getText().toString());
                    bundle.putSerializable("room", r);
                    intent.putExtra("data", bundle);
                    setResult(ClassList.SAVE_CLASS, intent);
                    Toast.makeText(getApplication(), "Cập nhật lớp học thành công", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });

        btnClearClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearClass();
            }
        });

        btnCloseClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Notify.exit(InsertEditActivity.this);
            }
        });
    }
}
