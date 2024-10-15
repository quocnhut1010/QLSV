package com.example.qlsv;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class InsertClassActivity extends Activity {
    Button btnSaveClass, btnClearClass, btnCloseClass;
    EditText edtClassName, edtClassCode, edtClassNumber;
    SQLiteDatabase db;


    private void initWidget() {
        btnSaveClass = (Button) findViewById(R.id.btnSaveInsertClass);
        btnClearClass = (Button) findViewById(R.id.btnClearInsertClass);
        btnCloseClass = (Button) findViewById(R.id.btnCloseInsertClass);
        edtClassName = (EditText) findViewById(R.id.edtClassName);
        edtClassCode = (EditText) findViewById(R.id.edtClassCode);
        edtClassNumber = (EditText) findViewById(R.id.edtClassNumber);
    }

    // Thêm lớp học
    private long saveClass() {
        try {
            // Xóa cơ sở dữ liệu cũ (điều này sẽ xóa tất cả dữ liệu)
         //   this.deleteDatabase(Login.DATABASE_NAME);

            db = openOrCreateDatabase(Login.DATABASE_NAME, MODE_PRIVATE, null);
            db.execSQL("CREATE TABLE IF NOT EXISTS tblclass (id_class INTEGER PRIMARY KEY AUTOINCREMENT, code_class TEXT NOT NULL, name_class TEXT NOT NULL, number_student INTEGER NOT NULL);");

            ContentValues values = new ContentValues();
            values.put("code_class", edtClassCode.getText().toString());
            values.put("name_class", edtClassName.getText().toString());

            String classNumberStr = edtClassNumber.getText().toString();
            if (classNumberStr.isEmpty()) {
                Toast.makeText(this, "Sĩ số không được để trống", Toast.LENGTH_LONG).show();
                return -1;
            }
            try {
                int classNumber = Integer.parseInt(classNumberStr);
                values.put("number_student", classNumber);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Sĩ số phải là số nguyên", Toast.LENGTH_LONG).show();
                return -1;
            }

            long id = db.insert("tblclass", null, values);
            db.close();
            if (id != -1) {
                return id; // Trả về id_class mới thêm vào
            }
        } catch (Exception ex) {
            Toast.makeText(this, "Thêm lớp học bị lỗi", Toast.LENGTH_LONG).show();
        }
        return -1;
    }


    private void clearClass() {
        edtClassCode.setText("");
        edtClassName.setText("");
        edtClassNumber.setText("");
        Toast.makeText(this, "Đã xóa trắng các trường nhập liệu", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_class_layout);
        initWidget();
        btnSaveClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long id = saveClass();
                Bundle bundle = new Bundle();
                Intent intent = getIntent();
                if (id != -1) { // -1 Thêm không thành công
                    // Lớp học gồm 4 trường Id class, code class, name class, number class
                    Room r = new Room(String.valueOf(id), edtClassCode.getText().toString(),
                            edtClassName.getText().toString(), edtClassNumber.getText().toString());
                    bundle.putSerializable("room", r);
                    intent.putExtra("data", bundle);
                    setResult(ClassList.SAVE_CLASS, intent);
                    Toast.makeText(getApplication(), "Thêm lớp học thành công", Toast.LENGTH_LONG).show();
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
                Notify.exit(InsertClassActivity.this);
            }
        });

    }
}
