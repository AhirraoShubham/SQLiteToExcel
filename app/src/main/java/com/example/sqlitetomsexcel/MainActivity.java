package com.example.sqlitetomsexcel;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ajts.androidmads.library.SQLiteToExcel;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    EditText etFName, etLName, etCity, etSalary;
    Button btnSubmit, btnReport;

    //SQLite Database
    DatabaseHelper databaseHelper;

    //Dialog
    ViewDialog viewDialog;

    //path for create the directory in your storage for store .xls file
    public String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyDir/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Database Helper Class Object
        databaseHelper = new DatabaseHelper(this);
        //Accept Permission
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        //Initialization of attributes
        init();
    }

    private void init() {
        //Dialog Box Class Initialization
        viewDialog = new ViewDialog(this);

        etFName = findViewById(R.id.etFName);
        etLName = findViewById(R.id.etLName);
        etCity = findViewById(R.id.etCity);
        etSalary = findViewById(R.id.etSalary);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnReport = findViewById(R.id.btnReport);

        //On submit Button Listener
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etFName.getText().toString().isEmpty() || etLName.getText().toString().isEmpty() ||
                        etCity.getText().toString().isEmpty() || etSalary.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Empty Field", Toast.LENGTH_SHORT).show();
                } else {
                    boolean isInserted = databaseHelper.insertData(etFName.getText().toString(),
                            etLName.getText().toString(), etCity.getText().toString(),
                            etSalary.getText().toString());
                    if (isInserted) {
                        Toast.makeText(MainActivity.this, "Inserted :-)", Toast.LENGTH_SHORT).show();
                        etFName.setText("");
                        etLName.setText("");
                        etCity.setText("");
                        etSalary.setText("");
                    } else {
                        etFName.setText("");
                        etLName.setText("");
                        etCity.setText("");
                        etSalary.setText("");
                        Toast.makeText(MainActivity.this, "Error :-( ,Plz try Again..", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //On Report Button Listener
        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(path);
                if (!file.exists()) {
                    file.mkdirs();
                }

                //Calls For SQLite To Excel

                //SINGLE TABLE
                //The Method are used to export a single table.
                convertSQLiteSingleTableToExcel();

                //LIST OF TABLE
                //The Method are used to export a Multiple table.
                //convertSQLiteMultiTablesToExcel();

                //WHOLE DATABASE
                //The following Method are used to export a whole database.
                //convertSQLiteDatabaseToExcel();
            }
        });
    }

    //Method For Conversion

    //WHOLE DATABASE
    //The following Method are used to export a whole database.
    private void convertSQLiteDatabaseToExcel() {
        // Export SQLite DB as EXCEL FILE
        viewDialog.showDialog();
        SQLiteToExcel sqliteToExcel = new SQLiteToExcel(getApplicationContext(), DatabaseHelper.DATABASE_NAME, path);
        sqliteToExcel.exportAllTables("employee.xls", new SQLiteToExcel.ExportListener() {
            @Override
            public void onStart() {
            }

            @Override
            public void onCompleted(String filePath) {
                viewDialog.hideDialog();
                Toast.makeText(MainActivity.this, "Exported", Toast.LENGTH_SHORT).show();
                //Open Report File
                openFile(filePath);
            }

            @Override
            public void onError(Exception e) {
                viewDialog.hideDialog();
                Toast.makeText(MainActivity.this, "Error :" + e, Toast.LENGTH_SHORT).show();
            }
        });

    }

    //LIST OF TABLE
    //The Method are used to export a Multiple table.
    private void convertSQLiteMultiTablesToExcel() {
        // Export SQLite DB as EXCEL FILE
        viewDialog.showDialog();
        String table1List = "Your Table List";
        SQLiteToExcel sqliteToExcel = new SQLiteToExcel(getApplicationContext(), DatabaseHelper.DATABASE_NAME, path);
        sqliteToExcel.exportSingleTable(table1List, "employee.xls", new SQLiteToExcel.ExportListener() {
            @Override
            public void onStart() {
            }

            @Override
            public void onCompleted(String filePath) {
                viewDialog.hideDialog();
                Toast.makeText(MainActivity.this, "Exported", Toast.LENGTH_SHORT).show();
                //Open Report File
                openFile(filePath);
            }

            @Override
            public void onError(Exception e) {
                viewDialog.hideDialog();
                Toast.makeText(MainActivity.this, "Error :" + e, Toast.LENGTH_SHORT).show();
            }
        });

    }

    //SINGLE TABLE
    //Method For SQLite to MS Excel File Convert
    private void convertSQLiteSingleTableToExcel() {
        // Export SQLite DB as EXCEL FILE
        viewDialog.showDialog();
        SQLiteToExcel sqliteToExcel = new SQLiteToExcel(getApplicationContext(), DatabaseHelper.DATABASE_NAME, path);
        sqliteToExcel.exportSingleTable(DatabaseHelper.TABLE_NAME, "employee.xls", new SQLiteToExcel.ExportListener() {
            @Override
            public void onStart() {
            }

            @Override
            public void onCompleted(String filePath) {
                viewDialog.hideDialog();
                Toast.makeText(MainActivity.this, "Exported", Toast.LENGTH_SHORT).show();
                //Open Report File
                openFile(filePath);
            }

            @Override
            public void onError(Exception e) {
                viewDialog.hideDialog();
                Toast.makeText(MainActivity.this, "Error :" + e, Toast.LENGTH_SHORT).show();
            }
        });

    }

    //Open Report From File Manager Method
    private void openFile(String filePath) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setDataAndType(Uri.parse(filePath), "application/*");
        startActivity(Intent.createChooser(intent, "View File"));
    }

    //For Storage Permission
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        // If request is cancelled, the result arrays are empty.
        if (requestCode == 1) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // permission was granted, yoo! Do the
                // storage-related task you need to do.
                File dir = new File(path);
                dir.mkdirs();
            } else {
                // permission denied, Woo! Disable the
                // functionality that depends on this permission.
                Toast.makeText(MainActivity.this, "Permission denied to read your External Storage", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
