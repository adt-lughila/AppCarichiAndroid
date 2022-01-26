package com.appcarichi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import model.Nota;
import com.example.listviewex.R;

import java.util.ArrayList;

public class NoteListAdapter extends ArrayAdapter<Nota> {

    public NoteListAdapter(Context context, ArrayList<Nota> noteArrayList){
        super(context, R.layout.note_list_view, noteArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Nota nota = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.note_list_view,parent,false);
        }

        TextView codicenota= convertView.findViewById(R.id.codice_nota);
        codicenota.setText(String.valueOf(nota.getCodicenota()));
        TextView descrizione= convertView.findViewById(R.id.descrizione);
        descrizione.setText(nota.getDescrizione());
        TextView commento= convertView.findViewById(R.id.commento);
        commento.setText(nota.getCommento());


        return convertView;
    }
}
