package com.example.test3;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

FloatingActionButton fabr;
FloatingActionButton fabl;
String newEntry;
DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fabl = findViewById(R.id.fabl);
        fabr = findViewById(R.id.fabr);
        fabl.setOnClickListener(this);
        fabr.setOnClickListener(this);
        fabr.setOnClickListener(this);
        databaseHelper = new DatabaseHelper(this);
    }

    public void AddData(String newEntry)
    {
        boolean insertData = databaseHelper.addData(newEntry);

        if (insertData) {
            toastMessage("Data Successfully Inserted");

        } else {
            toastMessage("Something went wrong");
        }
    }

    private void toastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.fabr:
                scanCode();
               break;
           case R.id.fabl:
                vediProdotti();
               break;
       }
    }

    private void saveCode() {

        AlertDialog.Builder build = new AlertDialog.Builder(this);
        //EditText eT = findViewById(R.id.edit_text);
        //Log.d("1", String.valueOf(eT));
        View edit_Text = getLayoutInflater().inflate(R.layout.edit_text, null);
        build.setView(edit_Text);
        build.setTitle("Salva Codice");
        build.setPositiveButton("Yes", (dialog, which) -> {
            newEntry = edit_Text.toString();
            Log.d("test",newEntry);
            if (newEntry.length() != 0) {
                AddData(newEntry);
            } else {
                toastMessage("You must put something in the text field!");
            }
        }).setNegativeButton("No", (dialog, which) -> {

        });
        AlertDialog dialog = build.create();
        dialog.show();
    }

    private  void vediProdotti(){
        Intent intent = new Intent(MainActivity.this, ListDataActivity.class);
        startActivity(intent);
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(ListDataActivity.class);
    }

    private void scanCode() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(CaptureAct.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scannin Code");
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result != null)
        {
            if(result.getContents() != null)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(result.getContents());
                builder.setTitle("Codice Articolo");
                builder.setNeutralButton("Salva Codice", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            saveCode();
                    }
                }).setPositiveButton("Scannerizza di nuovo", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        scanCode();
                    }
                }).setNegativeButton("Finito", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            else{
                Toast.makeText(this,"Nessun risultato", Toast.LENGTH_LONG).show();
            }
        }
        else{
            super.onActivityResult(requestCode,resultCode,data);
        }
    }


}