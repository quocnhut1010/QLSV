package com.example.qlsv;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class InsertStudentActivity extends Activity {
    Button btnSave, btnClear, btnClose;
    EditText edtCode, edtName, edtAddress, edtBirthday;
    RadioGroup rdigroupGender;
    Spinner spnClassCode;
    SQLiteDatabase db;
    ArrayList<Room> classList = new ArrayList<Room>();
    ArrayAdapter<Room> adapter;
    int posSpinner = -1;// Lấy vị tri spinner
    int idChecked, gender = 0;// Lay gioi tinh

    // Khoi tao cac Widget sinh vien
    private void initWidget() {
        btnSave = (Button) findViewById(R.id.btnSaveInsertStudent);
        btnClear = (Button) findViewById(R.id.btnClearInsertStudent);
        btnClose = (Button) findViewById(R.id.btnCloseInsertStudent);
        spnClassCode = (Spinner) findViewById(R.id.spnClassCode);
        edtCode = (EditText) findViewById(R.id.edtStudentCode);
        edtName = (EditText) findViewById(R.id.edtStudentName);
        edtAddress = (EditText) findViewById(R.id.edtStudentAddress);
        edtBirthday = (EditText) findViewById(R.id.edtStudentBirthday);
        rdigroupGender = (RadioGroup) findViewById(R.id.rdigroupGender);
    }

    private void getClassList() {
        try {
            // Them tieu de danh sach lop hoc
            db = openOrCreateDatabase(Login.DATABASE_NAME, MODE_PRIVATE, null);
            Cursor c = db.query("tblclass", null, null, null, null, null, null);
            c.moveToFirst();
            while (!c.isAfterLast()) {
                classList.add(new Room(c.getInt(0) + "", c.getString(1).toString(), c.getString(2).toString(), c.getInt(3) + ""));
                c.moveToNext();
            }
            adapter = new ArrayAdapter<Room>(this, android.R.layout.simple_spinner_item, classList);
            adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
            spnClassCode.setAdapter(adapter);
        } catch (Exception ex) {
            Toast.makeText(getApplication(), "Loi" + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    // Save Sinh Vien
    private long saveStudent() {
        db = openOrCreateDatabase(Login.DATABASE_NAME, MODE_PRIVATE, null);
        ContentValues values = new ContentValues();
        idChecked = rdigroupGender.getCheckedRadioButtonId();
        // Kiểm tra giới tính và lưu chuỗi "Nam" hoặc "Nữ"
        String genderStr = "Nữ";  // Mặc định là Nữ
        if (idChecked == R.id.rdiMale)
            genderStr = "Nam";  // Nếu chọn nam thì lưu "Nam"
        try {
            values.put("id_class", classList.get(posSpinner).getId_class());
            values.put("code_student", edtCode.getText().toString());
            values.put("name_student", edtName.getText().toString());
            values.put("birthday_student", edtBirthday.getText().toString());
            values.put("address_student", edtAddress.getText().toString());
            values.put("gender_student", genderStr);
            return (db.insert("tblstudent", null, values));
        } catch (Exception ex) {
            Toast.makeText(getApplication(), "Loi" + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
        return -1; // Thêm không thành công
    }

    private void clearStudent() {
        edtName.setText("");
        edtCode.setText("");
        edtBirthday.setText("");
        edtAddress.setText("");
        spnClassCode.setSelection(0);
        Toast.makeText(this, "Đã xóa trắng các trường nhập liệu", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_student_layout);
        initWidget();
        getClassList();
        spnClassCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                posSpinner = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Notify.exit(InsertStudentActivity.this);
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                long id = saveStudent();
                if (id!=-1) {
                    Intent intent = getIntent();
                    Bundle bundle = new Bundle();
                    Student student = new Student(classList.get(posSpinner).getId_class(),
                            classList.get(posSpinner).getName_class(),
                            id + "", edtCode.getText().toString(), edtName.getText().toString(),
                            gender + "", edtBirthday.getText().toString(),
                            edtAddress.getText().toString());
                    bundle.putSerializable("student", student);
                    intent.putExtra("data", bundle);
                    setResult(StudentList.SAVE_STUDENT, intent);
                    Toast.makeText(getApplication(), "Thêm sinh viên thành công!!!", Toast.LENGTH_LONG).show();
                    finish();
                }
                }
            });
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                clearStudent();
            }
        });
    }
}
