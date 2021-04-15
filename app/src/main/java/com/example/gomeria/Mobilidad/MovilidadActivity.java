package com.example.gomeria.Mobilidad;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.gomeria.MainActivity;
import com.example.gomeria.R;
import com.example.gomeria.Reporte.ReportActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.time.MonthDay;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MovilidadActivity extends AppCompatActivity {
    ArrayList<MovilidadModel> mMovilidad_item = new ArrayList<>(); //mMenu_item
    public static ProgressDialog pDialog;
    DatabaseReference mDatabase;
    static DatabaseReference mDatabase_query;
    DatabaseReference mDatabase_queryIndex;
    static int indiceUltimo;
    public static ArrayList<String[]> Data_Array= new ArrayList<String[]>();
    public static float sumaTotal;
    public static String currentdate ="";
    public static String lastdate ="";
    public static String selecteddate ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movilidad);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat ss = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        currentdate= ss.format(date);
        selecteddate =currentdate;

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(v.getContext());
                dialog.setTitle("FECHA");
                dialog.setContentView(R.layout.fecha);

                Button btn_aceptar = (Button) dialog.findViewById(R.id.btn_aceptar);
                Button btn_cancelar =  (Button) dialog.findViewById(R.id.btn_cancelar);
                CalendarView calendarView = (CalendarView) dialog.findViewById(R.id.calendarView);

                //Al hacer click en el boton se settea el dia actual en la variable currentdate
                Calendar c = Calendar.getInstance();
                SimpleDateFormat ss = new SimpleDateFormat("yyyy-MM-dd");
                Date date1 = new Date();
                selecteddate= ss.format(date1);
                getLastOne();

                //Al seleccionar la fecha se settea el dia seleccionado en la variable currentdate
                calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                        //Formateo el día obtenido: antepone el 0 si son menores de 10
                        String diaFormateado = (dayOfMonth < 10)? "0" + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                        //Formateo el mes obtenido: antepone el 0 si son menores de 10
                        final int mesActual = month + 1;
                        String mesFormateado = (mesActual < 10)? "0" + String.valueOf(mesActual):String.valueOf(mesActual);
                        int y  = year;
                        selecteddate = String.valueOf(y)+"-"+mesFormateado+"-"+diaFormateado;
                        getLastOne();
                    }
                });

                btn_aceptar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pDialog= new ProgressDialog(MovilidadActivity.this);
                        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        pDialog.setMessage("Cargando Reporte...");
                        pDialog.setCancelable(true);
                        pDialog.setMax(100);

                        Data_Array= new ArrayList<String[]>();
                        //header para el export to PDF
                        sumaTotal=0;
                        for(int y=1;y<=indiceUltimo;y++){
                            getRegistro_Fila(selecteddate,y);
                        }
                        new Async_Download_Data().execute();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                pDialog.dismiss(); //aqui porq en el postExecute es muy rapido
                                Data_Array.add(new String[]{ "TOTAL","__", "__", "__", "__", String.valueOf(sumaTotal)});//footer tabla, despues de q cargo todo
                                Intent restaurantAddIntent = new Intent(MovilidadActivity.this, ReportActivity.class);
                                startActivity(restaurantAddIntent);
                            }
                        },5000);

                        dialog.dismiss();
                    }
                });

                btn_cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        populateMenu();
        getLastDay();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void populateMenu() {
        MovilidadModel item1 = new MovilidadModel(1, "Auto", "auto.jpg");
        mMovilidad_item.add(item1);

        MovilidadModel item2 = new MovilidadModel(2, "Camioneta", "camioneta.jpg");
        mMovilidad_item.add(item2);

        MovilidadModel item3 = new MovilidadModel(3, "Bus", "bus.jpg");
        mMovilidad_item.add(item3);

        MovilidadModel item4 = new MovilidadModel(4, "Camion", "camion.jpg");
        mMovilidad_item.add(item4);

        initRecyclerView();
    }

    public void initRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(this,3,0,false);
        //LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        RecyclerView recyclerView = findViewById(R.id.movilidad_list);
        recyclerView.setLayoutManager(layoutManager);

        MovilidadAdapter adapter = new MovilidadAdapter(mMovilidad_item, this);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private class Async_Download_Data extends AsyncTask<Void, Integer, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            for(int y=1;y<=10;y++){
                publishProgress(y*10);
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            /*super.onProgressUpdate(values);*/
            pDialog.setProgress(values[0].intValue());
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            /*super.onPostExecute(aBoolean);*/
        }
    }

    public void getRegistro_Fila(final String fecha_seleccionada, final int i){
        String nodo= "reports/"+selecteddate+"/1";
        mDatabase = FirebaseDatabase.getInstance().getReference().child("reports").child(selecteddate).child(String.valueOf(i));
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    float sumaFila = 0;
                    String auto = dataSnapshot.child("auto").getValue().toString();
                    String precio1 = dataSnapshot.child("1").child("precio").getValue().toString();//precio de aire
                    sumaFila= sumaFila+ Float.valueOf(precio1);
                    String precio2 = dataSnapshot.child("2").child("precio").getValue().toString();//precio de parchado
                    sumaFila= sumaFila+ Float.valueOf(precio2);
                    String precio3 = dataSnapshot.child("3").child("precio").getValue().toString();//precio de cambio de llanta
                    sumaFila= sumaFila+ Float.valueOf(precio3);
                    String precio4 = dataSnapshot.child("4").child("precio").getValue().toString();//precio de cambio de otro
                    sumaFila= sumaFila+ Float.valueOf(precio4);
                    sumaTotal = sumaTotal + sumaFila;

                    Data_Array.add(new String[]{ auto,precio2, precio3, precio1,precio4, String.valueOf(sumaFila) });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MovilidadActivity.this, "Cancelado", Toast.LENGTH_SHORT).show();
            }
        };
        mDatabase.addValueEventListener(postListener);
    }

    public void getLastDay(){
        mDatabase_query = FirebaseDatabase.getInstance().getReference().child("reports");
        Query query =mDatabase_query.orderByKey().limitToLast(1);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String name = ds.getKey();
                    lastdate= name;
                    settearFecha();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void settearFecha(){
        SimpleDateFormat ss = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = ss.parse(currentdate);
            Date dateLast = ss.parse(lastdate);
            if (date.equals(dateLast)){

            } else {
                if(dateLast.after(date)){
                    currentdate=lastdate;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public static void getLastOne(){
        String nodo= "reports/"+selecteddate;
        mDatabase_query = FirebaseDatabase.getInstance().getReference().child(nodo);
        Query query =mDatabase_query.orderByKey().limitToLast(1);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String name = ds.getKey();
                    indiceUltimo= Integer.valueOf(name);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
