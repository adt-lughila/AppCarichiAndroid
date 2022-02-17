package com.appcarichi.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.appcarichi.activities.MainActivity;
import com.appcarichi.activities.OrdineActivity;
import com.appcarichi.model.Carico;
import com.appcarichi.model.FlagCarico;
import com.appcarichi.utils.Utils;
import com.example.appcarichi.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        tot_colli.setText(String.valueOf(carico.getTot_colli()));
        TextView colli_censiti = convertView.findViewById(R.id.colli_censiti);
        colli_censiti.setText(String.valueOf(carico.getColli_censiti()));
        TextView stato_spedizione = convertView.findViewById(R.id.stato_spedizione);
        stato_spedizione.setText(String.valueOf(carico.getStato_spedizione()));
        TextView num_sedute = convertView.findViewById(R.id.num_sedute);
        num_sedute.setText(String.valueOf(carico.getNum_sedute()));



        if (carico.getStatoCarico().equals("x")) {
            img.setImageResource(R.drawable.yellowcircle);
        }else if (carico.getStatoCarico().equals("F")) {
            img.setImageResource(R.drawable.redcircle);
        }else if(carico.getStatoCarico().equals("v")){
            img.setImageResource(R.drawable.greencircle);
        }

        return convertView;
    }

    public void flag(int idcarico){
        RequestQueue queue= Volley.newRequestQueue(getContext());
        String url = Utils.URL_BE+"/flag-carico?idCarico="+idcarico;
        JsonArrayRequest request=new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>(){
                    @Override
                    public void onResponse(JSONArray response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(request);
    }
}
