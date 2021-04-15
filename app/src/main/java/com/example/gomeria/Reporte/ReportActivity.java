package com.example.gomeria.Reporte;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.Manifest;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gomeria.Mobilidad.MovilidadActivity;
import com.example.gomeria.R;
import com.example.gomeria.Trabajo.Cabecera;
import com.example.gomeria.Trabajo.Detalle;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.spec.DESedeKeySpec;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class ReportActivity extends AppCompatActivity {
    public static final String[] TABLE_HEADERS = { "Vehiculo", "Parchado", "Cambio Llanta", "Aire", "Otro","Sumatoria" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // rodeado de un try catch por android lo pidio
                try {
                    PDFsetting();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
                final Dialog dialog = new Dialog(v.getContext());
                dialog.setContentView(R.layout.exportacion);
                Button btn_cerrar = (Button) dialog.findViewById(R.id.btn_cerrar);
                TextView txt_texto = (TextView) dialog.findViewById(R.id.txt_texto);
                txt_texto.setText("El Reporte del dia: "+MovilidadActivity.selecteddate +" fue exportado en la carpeta: DCMI de la tarjeta externa.");

                btn_cerrar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        final TableView<String[]> tableView = (TableView<String[]>) findViewById(R.id.tableView);
        //Definicion del width para cada columna
        TableColumnWeightModel columnModel = new TableColumnWeightModel(6); //width total de la tabla
        columnModel.setColumnWeight(0, 2);
        columnModel.setColumnWeight(1, 2);
        columnModel.setColumnWeight(2, 2);
        columnModel.setColumnWeight(3, 1);
        columnModel.setColumnWeight(4, 1);
        columnModel.setColumnWeight(5, 2);
        tableView.setColumnModel(columnModel);
        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(this, TABLE_HEADERS));
        tableView.setDataAdapter(new SimpleTableDataAdapter2(this, MovilidadActivity.Data_Array));
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
        //return super.onSupportNavigateUp();
    }

    public void PDFsetting() throws IOException, DocumentException {
        //Grant permissions in order no avoid a permission denigned error
        int writeExternalStoragePermission = ContextCompat.checkSelfPermission(ReportActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(writeExternalStoragePermission!= PackageManager.PERMISSION_GRANTED)
        {
            // Request user to grant write external storage permission.
            ActivityCompat.requestPermissions(ReportActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        Document document = new Document();
        // Location to save
        String dest = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/"+MovilidadActivity.selecteddate+".pdf";
        PdfWriter.getInstance(document, new FileOutputStream(dest));
        // Open to write
        document.open();
        // Document Settings
        document.setPageSize(PageSize.A4);
        document.addCreationDate();
        document.addAuthor("Gomeria Autor");
        document.addCreator("Gomeria App");

        PdfPTable table = new PdfPTable(6);

        PdfPCell cell; //Header of Table
        cell = new PdfPCell(new Phrase("Vehiculo"));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Parchado"));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Cambio Llanta"));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Aire"));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Otro"));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Sumatoria"));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);  // end Header

        for (int x=0; x<MovilidadActivity.Data_Array.size(); x++){
                String[] aux= MovilidadActivity.Data_Array.get(x);
                table.addCell(aux[0]);
                table.addCell(aux[1]);
                table.addCell(aux[2]);
                table.addCell(aux[3]);
                table.addCell(aux[4]);
                table.addCell(aux[5]);
        }
        document.add(table);
        document.close();
    }

}
