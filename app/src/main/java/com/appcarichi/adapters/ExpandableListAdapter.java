package com.appcarichi.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.appcarichi.activities.InsertNotaActivity;
import com.appcarichi.activities.CheckNoteActivity;
import com.appcarichi.model.Ordine;
import com.example.appcarichi.R;
import com.appcarichi.model.Rigaordine;

import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private List<Ordine> mListData;
    private HashMap<Ordine, List<Rigaordine>> mListChildData;

    public ExpandableListAdapter(Context context, List<Ordine> listData,
                                 HashMap<Ordine, List<Rigaordine>> listChildData){
        this.mContext = context;
        this.mListData = listData;
        this.mListChildData = listChildData;
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
        TextView matricola = convertView.findViewById(R.id.matricola);
        matricola.setText(ordine.getMatricola());
        TextView barcode = convertView.findViewById(R.id.barcode);
        barcode.setText(ordine.getBarcode());
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

        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.righe_ordine, null);
        }
        TextView campo1 = convertView.findViewById(R.id.campo1);
        campo1.setText(ro.getCampo1());
        TextView campo2 = convertView.findViewById(R.id.campo2);
        campo2.setText(ro.getCampo2());
        TextView campo3 = convertView.findViewById(R.id.campo3);
        campo3.setText(ro.getCampo3());
        TextView campo4 = convertView.findViewById(R.id.campo4);
        campo4.setText(ro.getCampo4());
        TextView campo5 = convertView.findViewById(R.id.campo5);
        campo5.setText(ro.getCampo5());
        TextView campo6 = convertView.findViewById(R.id.campo6);
        campo6.setText(ro.getCampo6());
        ImageButton insertnote = convertView.findViewById(R.id.insertnota);
        ImageButton checknote = convertView.findViewById(R.id.checknote);
        insertnote.setTag(ro);
        checknote.setTag(ro);
        CheckBox spunta = convertView.findViewById(R.id.spunta);

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


        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        // Don't forget to set this true so that the child items are selectable
        return true;
    }
}