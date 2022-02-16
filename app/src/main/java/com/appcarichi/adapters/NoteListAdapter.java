package com.appcarichi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.appcarichi.model.Nota;
import com.appcarichi.model.NotaRigaOrdine;
import com.example.appcarichi.R;

import java.util.ArrayList;

public class NoteListAdapter extends ArrayAdapter<NotaRigaOrdine> {

    public NoteListAdapter(Context context, ArrayList<NotaRigaOrdine> noteArrayList){
        super(context, R.layout.note_list_view, noteArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        NotaRigaOrdine nota = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.note_list_view,parent,false);
        }

        TextView codicenota= convertView.findViewById(R.id.codice_nota);
        codicenota.setText(String.valueOf(nota.getNota().getCodicenota()));
        TextView descrizione= convertView.findViewById(R.id.descrizione);
        descrizione.setText(nota.getNota().getDescrizione());
        TextView commento= convertView.findViewById(R.id.commento);
        commento.setText(nota.getCommento());


        return convertView;
    }
}
