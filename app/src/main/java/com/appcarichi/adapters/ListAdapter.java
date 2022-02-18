package com.appcarichi.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.appcarichi.model.Carico;
import com.example.appcarichi.R;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<Carico> {

    public ListAdapter(Context context, ArrayList<Carico> caricoArrayList){
        super(context, R.layout.list_view,caricoArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Carico carico = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view,parent,false);
        }
        if (position % 2 == 1) {
            convertView.setBackgroundColor(Color.WHITE);
        } else {
            convertView.setBackgroundColor(Color.LTGRAY);
        }
        ImageView img = convertView.findViewById(R.id.img);
        TextView codiceCarico = convertView.findViewById(R.id.codiceCarico);
        codiceCarico.setText(String.valueOf(carico.getCodice()));
        TextView destinazione = convertView.findViewById(R.id.destinazione);
        if(("null").equals(carico.getDestinazione())) {
            destinazione.setText("");
        } else {
            destinazione.setText(carico.getDestinazione());
        }
        TextView tot_colli = convertView.findViewById(R.id.tot_colli);
        tot_colli.setText(String.valueOf(carico.getNumTotColli()));
        TextView colli_censiti = convertView.findViewById(R.id.colli_censiti);
        colli_censiti.setText(String.valueOf(carico.getNumColliSpuntati()));
        TextView stato_spedizione = convertView.findViewById(R.id.stato_spedizione);
        stato_spedizione.setText(String.valueOf(carico.getStato_spedizione()));
        TextView num_sedute = convertView.findViewById(R.id.num_sedute);
        num_sedute.setText(String.valueOf(carico.getNum_sedute()));
        if(carico.getNumColliSpuntati()==0){
            img.setImageResource(R.drawable.redcircle);
        }else if(carico.getNumColliSpuntati()!=0&&carico.getNumColliSpuntati()<carico.getNumTotColli()){
            img.setImageResource(R.drawable.yellowcircle);
        }else if(carico.getNumColliSpuntati()==carico.getNumTotColli()){
            img.setImageResource(R.drawable.greencircle);
        }

        return convertView;
    }




}
