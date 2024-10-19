package com.example.qlsv;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

public class Notify {
    public static void exit(Context context){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Xac nha thoat...!!!!!");
        alertDialogBuilder.setIcon(R.drawable.ic_launcher_round);
        alertDialogBuilder.setMessage("Ban co muon thoat?");
        alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setPositiveButton("Dong y", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    System.exit(1);
                }
            });
            alertDialogBuilder.setNegativeButton("Khong dong y", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
    }

}
