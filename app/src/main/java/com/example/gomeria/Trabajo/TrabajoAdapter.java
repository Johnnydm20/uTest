package com.example.gomeria.Trabajo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gomeria.MainActivity;
import com.example.gomeria.Mobilidad.MovilidadActivity;
import com.example.gomeria.R;

import java.util.ArrayList;

public class TrabajoAdapter extends RecyclerView.Adapter<TrabajoAdapter.TrabajoViewHolder> {
    private ArrayList<TrabajoModel> trabajoData= new ArrayList<>();
    private Context mContext;

    public TrabajoAdapter(ArrayList<TrabajoModel> trabajoData, Context mContext) {
        this.trabajoData = trabajoData;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public TrabajoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        // create a new view
        View itemHomeLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_trabajo, parent, false);
        // create ViewHolder
        TrabajoViewHolder viewHolder = new TrabajoViewHolder(itemHomeLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final TrabajoViewHolder trabajoViewHolder, final int i) {
        //trabajoViewHolder.trabajoName.setText(trabajoData.get(i).getDescripcion());
        trabajoViewHolder.trabajoName.setText(trabajoData.get(i).getDescripcion());
        trabajoViewHolder.precioText.setText("Bs. "+trabajoData.get(i).precio);

        if (trabajoData.get(i).getFoto().toLowerCase().equals("aire_llanta.jpg")) {
            trabajoViewHolder.trabajoPhoto.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.aire_llanta));
        } else if (trabajoData.get(i).getFoto().toLowerCase().equals("parchado.jpg")) {
            trabajoViewHolder.trabajoPhoto.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.parchado));
        } else if (trabajoData.get(i).getFoto().toLowerCase().equals("cambio_llanta.jpg")){
            trabajoViewHolder.trabajoPhoto.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.cambio_llanta));
        } else if (trabajoData.get(i).getFoto().toLowerCase().equals("grua.jpg")){
            trabajoViewHolder.trabajoPhoto.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.grua));
        } else if (trabajoData.get(i).getFoto().toLowerCase().equals("otros.png")){
            trabajoViewHolder.trabajoPhoto.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.otro));
        }

        trabajoViewHolder.trabajoPhoto.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(mContext);
                dialog.setTitle("COSTO");
                dialog.setContentView(R.layout.costo);

                final EditText costo = (EditText) dialog.findViewById(R.id.costo_field);
                Button btn_aceptar = (Button) dialog.findViewById(R.id.btn_aceptar);
                Button btn_cancelar =  (Button) dialog.findViewById(R.id.btn_cancelar);

                costo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus){
                            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                        }
                    }
                });

                btn_aceptar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TrabajoModel item1 = new TrabajoModel(trabajoData.get(i).getId(), trabajoData.get(i).getDescripcion(), trabajoData.get(i).getFoto(),Float.parseFloat(costo.getText().toString()));
                        trabajoData.set(i,item1);
                        MainActivity.recalcular_total();
                        String x= "BS. "+costo.getText().toString();
                        trabajoViewHolder.precioText.setText(x);
                        trabajoViewHolder.enableIcon.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_check_circle));
                        dialog.dismiss();
                    }
                });

                btn_cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mContext,"boton Cancelar",Toast.LENGTH_SHORT);
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        trabajoViewHolder.enableIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TrabajoModel item1 = new TrabajoModel(trabajoData.get(i).getId(), trabajoData.get(i).getDescripcion(), trabajoData.get(i).getFoto(),0.0f);
                trabajoData.set(i,item1);
                MainActivity.recalcular_total();
                trabajoViewHolder.precioText.setText("BS."+trabajoData.get(i).precio);
                trabajoViewHolder.enableIcon.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_delete));
            }
        });
    }

    @Override
    public int getItemCount() {
        return trabajoData.size();
    }


    public class TrabajoViewHolder extends RecyclerView.ViewHolder {
        ImageView trabajoPhoto;
        TextView trabajoName;
        TextView precioText;
        ImageView enableIcon;

        TrabajoViewHolder(View itemView) {
            super(itemView);
            trabajoPhoto = (ImageView)itemView.findViewById(R.id.photoTrabajo);
            trabajoName = (TextView)itemView.findViewById(R.id.trabajo_name);
            precioText = (TextView)itemView.findViewById(R.id.precio);
            enableIcon = (ImageView)itemView.findViewById(R.id.image_enable);
        }
    }
}
