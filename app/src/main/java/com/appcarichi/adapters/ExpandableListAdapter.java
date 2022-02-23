package com.appcarichi.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.appcarichi.activities.InsertNotaActivity;
import com.appcarichi.activities.CheckNoteActivity;
import com.appcarichi.activities.OrdineActivity;
import com.appcarichi.activities.SpuntaColloActivity;
import com.appcarichi.model.Ordine;
import com.appcarichi.R;
import com.appcarichi.model.Rigaordine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private List<Ordine> mListData;
    private HashMap<Ordine, List<Rigaordine>> mListChildData;
    private int codice;
    private boolean caricoSpedito;

    public ExpandableListAdapter(Context context, List<Ordine> listData,
                                 HashMap<Ordine, List<Rigaordine>> listChildData, int codice,boolean caricoSpedito){
        this.mContext = context;
        this.mListData = listData;
        this.mListChildData = listChildData;
        this.codice = codice;
        this.caricoSpedito = caricoSpedito;
    }

    @Override
    public int getGroupCount() {
        return mListData.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return mListChildData.get(mListData.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return mListData.get(i);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mListChildData.get(mListData.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupId) {
        return groupId;
    }

    @Override
    public long getChildId(int groupId, int childId) {
        return childId;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View convertView, ViewGroup viewGroup) {

        TextView listItemText;
        Ordine ordine = mListData.get(i);

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.order_list_view, null);
        }
        TextView fornitore = convertView.findViewById(R.id.fornitore);
        fornitore.setText(ordine.getFornitore());
        TextView cliente = convertView.findViewById(R.id.cliente);
        cliente.setText(ordine.getCliente());
        TextView tipoordine = convertView.findViewById(R.id.tipoordine);
        tipoordine.setText(ordine.getTipoOrdine());
        TextView luogo_consegna_ordine = convertView.findViewById(R.id.luogo_consegna_ordine);
        luogo_consegna_ordine.setText(ordine.getLuogo_consegna());
        TextView tot_colli_ordine = convertView.findViewById(R.id.tot_colli_ordine);
        tot_colli_ordine.setText(String.valueOf(ordine.getTot_colli()));
        TextView colli_consegnati_ordine = convertView.findViewById(R.id.colli_consegnati_ordine);
        colli_consegnati_ordine.setText(String.valueOf(ordine.getColli_consegnati()));

        return convertView;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View convertView, ViewGroup viewGroup) {

        TextView listChildItemText;
        Rigaordine ro = (Rigaordine) mListChildData.get(mListData.get(i)).get(i1);
        Ordine ordine = mListData.get(i);

        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.righe_ordine, null);
        }
        ImageView img_statocarico = convertView.findViewById(R.id.stato_rigaordine);
        TextView codiceArticolo = convertView.findViewById(R.id.codicearticolo);
        codiceArticolo.setText(ro.getCodiceArticolo());
        TextView matricola = convertView.findViewById(R.id.matricola);
        matricola.setText(ro.getMatricola());
        TextView barcode = convertView.findViewById(R.id.barcode);
        barcode.setText(ro.getBarcode());
        TextView descrizione = convertView.findViewById(R.id.descrizione);
        descrizione.setText(ro.getDescrizione());
        TextView pezziordinati = convertView.findViewById(R.id.pezziordinati);
        pezziordinati.setText(String.valueOf(ro.getPezziordinati()));
        TextView pezzispediti = convertView.findViewById(R.id.pezzispediti);
        pezzispediti.setText(String.valueOf(ro.getPezzispediti()));
        TextView sconto = convertView.findViewById(R.id.sconto);
        if(String.valueOf(ro.getSconto()).equals("null")){
            sconto.setText(" ");
        }else {
            sconto.setText(String.valueOf(ro.getSconto()));
        }
        TextView prezzo = convertView.findViewById(R.id.prezzo);
        prezzo.setText(String.valueOf(ro.getPrezzo()));
        ImageButton insertnote = convertView.findViewById(R.id.insertnota);
        ImageButton checknote = convertView.findViewById(R.id.checknote);
        ImageButton spunta = convertView.findViewById(R.id.spuntanota);
        insertnote.setTag(ro);
        checknote.setTag(ro);
        insertnote.setVisibility(caricoSpedito ? View.INVISIBLE : View.VISIBLE);
        spunta.setVisibility(caricoSpedito ? View.INVISIBLE : View.VISIBLE);
        if(ro.getColliSpuntati()==ro.getNroColli()){
            img_statocarico.setImageResource(R.drawable.greencircle);
        }else if(ro.getColliSpuntati()==0){
            img_statocarico.setImageResource(R.drawable.redcircle);
        }else if(ro.getColliSpuntati()<ro.getNroColli() && ro.getColliSpuntati()!=0){
            img_statocarico.setImageResource(R.drawable.yellowcircle);
        }

        insertnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Rigaordine ro=(Rigaordine) insertnote.getTag();
                Intent intent = new Intent(mContext, InsertNotaActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("rigaordine", ro);
                mContext.startActivity(intent);
            }
        });

        checknote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Rigaordine ro=(Rigaordine) checknote.getTag();
                Intent intent = new Intent(mContext, CheckNoteActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("rigaordine", ro);
                mContext.startActivity(intent);
            }
        });

        spunta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Rigaordine ro=(Rigaordine) checknote.getTag();
                Intent intent = new Intent(mContext, SpuntaColloActivity.class);
                intent.putExtra("nCarico",ordine.getIdcarico());
                intent.putExtra("idRigaOrdine", ro.getIdrigarodine());
                intent.putExtra("rigaordine", ro);
                intent.putExtra("codice", codice);
                mContext.startActivity(intent);
            }
        });


        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        // Don't forget to set this true so that the child items are selectable
        return true;
    }
}