package com.example.qlsv;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import android.app.Activity;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends Activity {
    Button btnOpenClass, btnExit, btnOpenStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnOpenClass = (Button) findViewById(R.id.btnOpenClass);
        btnOpenStudent = (Button) findViewById(R.id.btnOpenStudent);
        btnExit = (Button) findViewById(R.id.btnExit);
        btnOpenClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ClassList.class);
                startActivity(intent);
            }
        });
        btnOpenStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, StudentList.class);
                startActivity(intent);
            }
        });
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Notify.exit(MainActivity.this);
            }
        });
    }
}