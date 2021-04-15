package com.example.gomeria.Mobilidad;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gomeria.MainActivity;
import com.example.gomeria.R;

import java.util.ArrayList;

public class MovilidadAdapter extends RecyclerView.Adapter<MovilidadAdapter.MovilidadViewHolder> {
    private ArrayList<MovilidadModel> movilidadData= new ArrayList<>();
    private Context mContext;

    public MovilidadAdapter(ArrayList<MovilidadModel> movilidadData, Context mContext) {
        this.movilidadData = movilidadData;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MovilidadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        // create a new view
        View itemHomeLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_movilidad, parent, false);
        // create ViewHolder
        MovilidadViewHolder viewHolder = new MovilidadViewHolder(itemHomeLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MovilidadViewHolder movilidadViewHolder, final int i) {
        movilidadViewHolder.movilidadName.setText(movilidadData.get(i).getDescripcion());

        //holder.menu_item_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.logo_hard_rock_cafe));
        if (movilidadData.get(i).getFoto().toLowerCase().equals("auto.jpg")) {
            movilidadViewHolder.movilidadPhoto.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.auto));
        } else if (movilidadData.get(i).getFoto().toLowerCase().equals("bus.jpg")) {
            movilidadViewHolder.movilidadPhoto.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.bus));
        } else if (movilidadData.get(i).getFoto().toLowerCase().equals("camioneta.jpg")){
            movilidadViewHolder.movilidadPhoto.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.caminoneta));
        } else if (movilidadData.get(i).getFoto().toLowerCase().equals("camion.jpg")){
            movilidadViewHolder.movilidadPhoto.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.camion));
        }

        movilidadViewHolder.movilidadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MovilidadActivity.getLastOne();
                Intent restaurantAddIntent = new Intent(mContext, MainActivity.class);
                restaurantAddIntent.putExtra("auto",movilidadData.get(i).getDescripcion());
                mContext.startActivity(restaurantAddIntent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return movilidadData.size();
    }

    public class MovilidadViewHolder extends RecyclerView.ViewHolder {
        ImageView movilidadPhoto;
        TextView movilidadName;

        public MovilidadViewHolder(@NonNull View itemView) {
            super(itemView);
            movilidadPhoto = (ImageView)itemView.findViewById(R.id.photoMovilidad);
            movilidadName = (TextView)itemView.findViewById(R.id.nameMovilidad);
        }
    }
}
