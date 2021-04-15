package com.example.gomeria;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.gomeria.Mobilidad.MovilidadActivity;
import com.example.gomeria.Trabajo.Cabecera;
import com.example.gomeria.Trabajo.Detalle;
import com.example.gomeria.Trabajo.TrabajoAdapter;
import com.example.gomeria.Trabajo.TrabajoModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    static ArrayList<TrabajoModel> mTrabajo_item = new ArrayList<>(); //mMenu_item
    DatabaseReference mDatabase;
    DatabaseReference mDatabase_query;
    public static int indiceUltimo=1;
    Button btn_registrar;
    static TextView total_text;
    public static float total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        total_text = findViewById(R.id.total_text);
        btn_registrar = findViewById(R.id.registrar_btn);
        getLastOne();
        populateMenu();

        final String auto = getIntent().getExtras().getString("auto");

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final String strDate = sdf.format(c.getTime());

        btn_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(v.getContext());
                dialog.setContentView(R.layout.confirmacion);
                Button btn_cerrar = (Button) dialog.findViewById(R.id.btn_cerrar);
                TextView txt_total = (TextView) dialog.findViewById(R.id.txt_total);
                txt_total.setText("BS. "+total_text.getText());

                btn_cerrar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Para Limpiar todo los items y el total
                        populateMenu();
                        recalcular_total();
                        dialog.dismiss();
                        Intent trabajoIntent = new Intent(MainActivity.this, MovilidadActivity.class);
                        startActivity(trabajoIntent);
                    }
                });
                dialog.show();
                writeRegistro(strDate, auto);
                getLastOne();

            }
        });
    }

    private void populateMenu() {
        mTrabajo_item = new ArrayList<>();
        TrabajoModel item1 = new TrabajoModel(1, "Aire", "aire_llanta.jpg",0.0f);
        mTrabajo_item.add(item1);

        TrabajoModel item2 = new TrabajoModel(2, "Parchado", "parchado.jpg",0.0f);
        mTrabajo_item.add(item2);

        TrabajoModel item3 = new TrabajoModel(3, "Cambio Llanta", "cambio_llanta.jpg",0.0f);
        mTrabajo_item.add(item3);

        TrabajoModel item4 = new TrabajoModel(4, "Otro", "otros.png",0.0f);
        mTrabajo_item.add(item4);
        initRecyclerView();
    }

    public void initRecyclerView() {
        //GridLayoutManager layoutManager = new GridLayoutManager(this,3,0,false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerView = findViewById(R.id.trabajo_list);
        recyclerView.setLayoutManager(layoutManager);
        TrabajoAdapter adapter = new TrabajoAdapter(mTrabajo_item, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void writeRegistro(String fecha, String auto){
        String nodo= "reports/"+MovilidadActivity.currentdate;
        Cabecera cabecera = new Cabecera(indiceUltimo,fecha,auto);
        mDatabase.child(nodo).child(String.valueOf(indiceUltimo)).setValue(cabecera);
        writeDetalle(indiceUltimo);
    }

    private void writeDetalle(int id){
        String nodo= "reports/"+MovilidadActivity.currentdate;
        for (int i=0;i<mTrabajo_item.size();i++){
            Detalle detalle = new Detalle(id,mTrabajo_item.get(i).getDescripcion(),mTrabajo_item.get(i).getPrecio());
            mDatabase.child(nodo).child(String.valueOf(id)).child(String.valueOf(i+1)).setValue(detalle);
        }
    }

    public static void recalcular_total(){
        total=0.0f;
        total= total+ mTrabajo_item.get(0).getPrecio()+ mTrabajo_item.get(1).getPrecio()+ mTrabajo_item.get(2).getPrecio()+ mTrabajo_item.get(3).getPrecio();
        total_text.setText(String.valueOf(total));
    }

    public void getLastOne(){
        String nodo= "reports/"+MovilidadActivity.currentdate;
        mDatabase_query = FirebaseDatabase.getInstance().getReference().child(nodo);
        Query query =mDatabase_query.orderByKey().limitToLast(1);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String name = ds.getKey();
                    indiceUltimo= Integer.valueOf(name)+1;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

}
