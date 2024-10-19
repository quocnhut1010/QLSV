package com.example.qlsv;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class EditStudentActivity extends Activity {
    Button btnSave, btnClear, btnClose;
    EditText edtCode, edtName, edtAddress, edtBirthday;
    RadioGroup rdigroupGender;
    RadioButton rdiMale, rdiFemale;
    Spinner spnClassCode;
    SQLiteDatabase db;
    ArrayList<Room> classList = new ArrayList<Room>();
    ArrayAdapter<Room> adapter;
    int posSpinner = -1;// Lấy vị trí spinner
    int idChecked, gender = 0;// Lay gioi tinh
    String id_student; // for updating

    // Khoi tao cac Widget sinh vien
    private void initWidget() {
        btnSave = (Button) findViewById(R.id.btnSaveEditStudent);
        btnClear = (Button) findViewById(R.id.btnClearEditStudent);
        btnClose = (Button) findViewById(R.id.btnCloseEditStudent);
        spnClassCode = (Spinner) findViewById(R.id.spnEditClassCode);
        edtCode = (EditText) findViewById(R.id.edtEditStudentCode);
        edtName = (EditText) findViewById(R.id.edtEditStudentName);
        edtAddress = (EditText) findViewById(R.id.edtEditStudentAddress);
        edtBirthday = (EditText) findViewById(R.id.edtEditStudentBirthday);
        rdigroupGender = (RadioGroup) findViewById(R.id.rdigroupEditGender);
        rdiMale = (RadioButton) findViewById(R.id.rdiEditMale);
        rdiFemale = (RadioButton) findViewById(R.id.rdiEditFemale);
    }

    // Show data to Activtiy
    private void getData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");
        Student student = (Student) bundle.getSerializable("student");
        edtCode.setText(student.getCode_student());
        edtName.setText(student.getName_student());
        edtBirthday.setText(student.getBirthday_student());
        edtAddress.setText(student.getAddress_student());
        id_student = student.getId_student();
        if (student.getGender_student().contains("1")) {
            rdiMale.setChecked(true);
        } else
            rdiFemale.setChecked(true);
        //set spnniner de chon
        int i = 0;
        while (i < classList.size()) {
            if (student.getId_class().contains(classList.get(i).getId_class()))
                break;
            i++;
        }
        spnClassCode.setSelection(i);
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
    private boolean saveStudent() {
        db = openOrCreateDatabase(Login.DATABASE_NAME, MODE_PRIVATE, null);
        ContentValues values = new ContentValues();
        idChecked = rdigroupGender.getCheckedRadioButtonId();
        if (idChecked == R.id.rdiEditMale)
            gender = 1;

        try {
            // Prepare the new values to update
            values.put("id_class", classList.get(posSpinner).getId_class());
            values.put("code_student", edtCode.getText().toString());
            values.put("name_student", edtName.getText().toString());
            values.put("birthday_student", edtBirthday.getText().toString());
            values.put("address_student", edtAddress.getText().toString());
            values.put("gender_student", gender);

            // Corrected the WHERE clause without the '?'
            if (db.update("tblstudent", values, "id_student = ?", new String[]{id_student}) != -1) {
                return true;
            }
        } catch (Exception ex) {
            Toast.makeText(getApplication(), "Lỗi: " + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
        return false; // Update failed
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student_layout);
        initWidget();
        getClassList();
        getData();
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (saveStudent()) {
                    Intent intent = getIntent();
                    Bundle bundle = new Bundle();
                    Student student = new Student(classList.get(posSpinner).getId_class(),
                            classList.get(posSpinner).getName_class(), id_student,
                            edtCode.getText().toString(), edtName.getText().toString(),
                            gender + "", edtBirthday.getText().toString(),
                            edtAddress.getText().toString());
                    bundle.putSerializable("student", student);
                    intent.putExtra("data", bundle);
                    setResult(StudentList.SAVE_STUDENT, intent);
                    Toast.makeText(getApplication(), "Cập nhật sinh viên thành công!!!", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });

btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                // TODO Auto-generated method stub
                    Notify.exit (EditStudentActivity.this);
                }
            });
            spnClassCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected (AdapterView<?> parent, View view,
                int position, long id) {
                // TODO Auto-generated method stub
                    posSpinner = position;
                }
                @Override
                public void onNothingSelected (AdapterView<?> parent) { // TODO Auto-generated method stub
                    posSpinner=-1;
                }
            });
        // Set a click listener to open the DatePickerDialog
        edtBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the current date as default
                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                // Create and show DatePickerDialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        EditStudentActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                                // Format and set the selected date in the EditText
                                edtBirthday.setText(selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear);
                            }
                        },
                        year, month, day);
                datePickerDialog.show();
            }
        });
    }
}
