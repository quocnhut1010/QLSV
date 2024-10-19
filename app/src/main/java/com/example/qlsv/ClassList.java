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

public class ClassList extends Activity {
    ListView lstClass;
    Button btnOpenClass;
    Button btnExitClass;
    ArrayList<Room> classList = new ArrayList<>();
    MyAdapterClass adapter;
    SQLiteDatabase db;
    int posselected = -1;
    public static final int OPEN_CLASS = 113;
    public static final int EDIT_CLASS = 114;
    public static final int SAVE_CLASS = 115;

    private void getClassList() {
        try {
            classList.add(new Room("Mã Lớp", "Tên Lớp", "Sĩ Số"));
            db = openOrCreateDatabase(Login.DATABASE_NAME, MODE_PRIVATE, null);
            Cursor c = db.query("tblclass", null, null, null, null, null, null);
            c.moveToFirst();
            while (!c.isAfterLast()) {
                classList.add(new Room(c.getInt(0) + "", c.getString(1), c.getString(2), c.getString(3) + ""));
                c.moveToNext();
            }
            adapter = new MyAdapterClass(this, android.R.layout.simple_list_item_1, classList);
            lstClass.setAdapter(adapter);
        } catch (Exception ex) {
            Toast.makeText(getApplication(), "Error: " + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    // Confirmation dialog for deleting a class
    public void confirmDelete() {
        // Create an AlertDialog for confirmation
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Confirm class deletion");
        alertDialogBuilder.setMessage("BẠN CÓ CHẮC CHẮN MUỐN XOÁ LỚP NÀY????????");
        alertDialogBuilder.setCancelable(false);

        // Positive button for deletion
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Open the database
                SQLiteDatabase db = openOrCreateDatabase(Login.DATABASE_NAME, MODE_PRIVATE, null);
                // Get the id_class of the selected class
                String id_class = classList.get(posselected).getId_class();

                // Attempt to delete the class from the database
                if (db.delete("tblclass", "id_class=?", new String[]{id_class}) != -1) {
                    // Successfully deleted
                    classList.remove(posselected);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getApplication(), "XOÁ THÀNH CÔNG!!!!", Toast.LENGTH_LONG).show();
                } else {
                    // Deletion failed
                    Toast.makeText(getApplication(), "XOÁ THẤT BẠI!!!!!!", Toast.LENGTH_LONG).show();
                }
                db.close(); // Close the database connection
            }
        });

        // Negative button to cancel the deletion
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); // Dismiss the dialog
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.classlist_layout);
        lstClass = findViewById(R.id.lstclass);
        btnOpenClass = findViewById(R.id.btnOpenClass);
        btnExitClass = findViewById(R.id.btnexit);

        getClassList();

        // Registering the ListView for the context menu
        registerForContextMenu(lstClass);

        // Button to open the InsertClassActivity
        btnOpenClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClassList.this, InsertClassActivity.class);
                startActivityForResult(intent, OPEN_CLASS);
            }
        });

        btnExitClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Notify.exit(ClassList.this);
            }
        });

        // Handling long-clicks to open context menu
        lstClass.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view,
                                           int position, long id) {
                posselected = position;
                Log.d("ClassList", "Long click detected at position: " + position);  // Logging the position
                return false;  // Return true to indicate that the long-click was handled
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menuclass, menu);  // Inflate the context menu
        Log.d("ClassList", "Context menu created");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menueditclass) {
            // Edit class option
            Room room = classList.get(posselected);
            Bundle bundle = new Bundle();
            Intent intent = new Intent(ClassList.this, InsertEditActivity.class);
            bundle.putSerializable("room", room);
            intent.putExtra("data", bundle);
            startActivityForResult(intent, EDIT_CLASS);
            return true;
        } else if (id == R.id.menudeleteclass) {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OPEN_CLASS && resultCode == SAVE_CLASS) {
            // Handle new class addition
            Bundle bundle = data.getBundleExtra("data");
            Room room = (Room) bundle.getSerializable("room");
            classList.add(room);
            adapter.notifyDataSetChanged();
        } else if (requestCode == EDIT_CLASS && resultCode == SAVE_CLASS) {
            // Handle class edit
            Bundle bundle = data.getBundleExtra("data");
            Room room = (Room) bundle.getSerializable("room");
            classList.set(posselected, room);
            adapter.notifyDataSetChanged();
        }
    }
}