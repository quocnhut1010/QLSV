package com.example.qlsv;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class StudentList extends Activity {
    Button btnOpenStudent, btnExitStudent;
    ListView lstStudent;
    SQLiteDatabase db;
    ArrayList<Student> studentList = new ArrayList<Student>();
    MyAdapterStudent adapter;
    int posselected = -1; // Giu Vi tri tren ListView //Khai báo các biến nhận kết quả trả về từ activity
    public static final int OPEN_STUDENT = 113;
    public static final int EDIT_STUDENT = 114;
    public static final int SAVE_STUDENT = 115;


    private void getStudentList() {
        db = openOrCreateDatabase(Login.DATABASE_NAME, MODE_PRIVATE, null);
        Cursor c = db.rawQuery("SELECT tblclass.id_class, tblclass.name_class, tblstudent.id_student, " +
                "tblstudent.code_student, tblstudent.name_student, tblstudent.gender_student, " +
                "tblstudent.birthday_student, tblstudent.address_student " +
                "FROM tblclass, tblstudent WHERE tblclass.id_class = tblstudent.id_class", null);

        c.moveToFirst();
        while (!c.isAfterLast()) {
            studentList.add(new Student(c.getString(0).toString(), c.getString(1).toString(), c.getString(2).toString(), c.getString(3).toString(), c.getString(4).toString(), c.getString(5).toString(), c.getString(6).toString(), c.getString(7).toString()));
            c.moveToNext();
        }
        adapter = new MyAdapterStudent(this, android.R.layout.simple_list_item_1, studentList);
        lstStudent.setAdapter(adapter);
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menustudent, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        // Auto-generated method stub
        int id = item.getItemId();
        if (id == R.id.menueditstudent) {
            // Edit class option
            Student student = studentList.get(posselected);
            Bundle bundle = new Bundle();
            Intent intent = new Intent(StudentList.this, EditStudentActivity.class);
            bundle.putSerializable("student", student);
            intent.putExtra("data", bundle);
            startActivityForResult(intent, EDIT_STUDENT);
            return true;
        } else if (id == R.id.menudeletesudent) {
            // Delete class option
            confirmDelete();
            return true;
        } else if (id == R.id.menucloseclass) {
            // Close class option
            Notify.exit(this);
            return true;
        } else {
            return super.onContextItemSelected(item);
        }
    }

    private void confirmDelete() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
// Setting Alert Dialog Title
        alertDialogBuilder.setTitle("Xác nhận để xóa sinh viên..!!!");
// Icon Of Alert Dialog
        alertDialogBuilder.setIcon(R.drawable.ic_launcher_round);
// Setting Alert Dialog Message
        alertDialogBuilder.setMessage("Bạn có chắc xóa sinh viên?");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
//Đông Activity hiện tại
                db = openOrCreateDatabase(Login.DATABASE_NAME, MODE_PRIVATE, null);
                String id_stduent = studentList.get(posselected).getId_class();
                if (db.delete("tblstudent", "id_student=?", new String[]{id_stduent}) != -1) {
                    studentList.remove(posselected);// Xoa lop hoc ra khoi danh danh
                    adapter.notifyDataSetChanged();// cap nhat lai adapter
                    Toast.makeText(getApplication(), "Xóa sinh viên thành công!!!", Toast.LENGTH_LONG).show();
                }
            }
        });
        alertDialogBuilder.setNegativeButton("Không đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case StudentList.OPEN_STUDENT:
                if (StudentList.SAVE_STUDENT==resultCode) {
                    Bundle bundle = data.getBundleExtra("data");
                    Student student = (Student) bundle.getSerializable ("student");
                    studentList.add(student);
                    adapter.notifyDataSetChanged();
                }
                break;
            case StudentList.EDIT_STUDENT:
                Bundle bundle = data.getBundleExtra("data");
                Student student = (Student) bundle.getSerializable ("student");
                studentList.set (posselected, student);
                adapter.notifyDataSetChanged();
                break;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);
        btnOpenStudent = (Button) findViewById(R.id.btnOpenStudent);
        btnExitStudent = findViewById(R.id.btnexit);
        lstStudent = (ListView) findViewById(R.id.lststudent);
        getStudentList();
        registerForContextMenu (lstStudent);
        btnOpenStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(StudentList.this, InsertStudentActivity.class);
                startActivityForResult(intent, StudentList.OPEN_STUDENT);
            }
        });
        btnExitStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Notify.exit(StudentList.this);
            }
        });
        // lay vi tri list view
        lstStudent.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick (AdapterView<?> parent, View view,
                                            int position, long id) {
                // TODO Auto-generated method stub
                posselected = position;
                return false;
            }
        });
    }
}