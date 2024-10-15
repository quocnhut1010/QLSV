package com.example.qlsv;

import android.app.ActionBar;
import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Button;
import android.widget.EditText;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import java.nio.channels.ConnectionPendingException;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {
    public static final String DATABASE_NAME = "student.db";
    SQLiteDatabase db;
    EditText edtUsername, edtPassword;
    Button btnLogin, btnCloseLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);
        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnCloseLogin = (Button) findViewById(R.id.btnCloseLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = edtUsername.getText().toString();
                String password = edtPassword.getText().toString();

                if(username.isEmpty()){
                    Toast.makeText(getApplication(), "Vui long nhap tai khoan", Toast.LENGTH_LONG).show();
                    edtUsername.requestFocus();
                }else if(password.isEmpty()){
                    Toast.makeText(getApplication(), "Vui long nhap mat khau", Toast.LENGTH_LONG).show();
                    edtPassword.requestFocus();
                }else if(isUser(edtUsername.getText().toString(), edtPassword.getText().toString())){
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplication(), "Tai khoan hoac mat khau bi sai", Toast.LENGTH_LONG).show();

                }
            }
        });
        btnCloseLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Notify.exit(Login.this);;
            }
        });
    }
    private boolean isUser(String username, String password){
        try{
            db = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
            Cursor c = db.rawQuery("select * from tbluser where username = ? and password = ?", new String[]{username, password});
            c.moveToFirst();
            if(c.getCount() > 0){
                c.moveToFirst();
                return true;
            }
        }catch (Exception ex){
            Toast.makeText(this, "Lỗi đăng nhập", Toast.LENGTH_LONG).show();

        }
        return false;
    }
    private boolean isTableExists(SQLiteDatabase database, String tableName){
        Cursor cursor = database.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name" + "= '" + tableName + "'", null);
        if(cursor!=null){
            if(cursor.getCount()>0){
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }
}
