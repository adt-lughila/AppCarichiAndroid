package com.appcarichi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.appcarichi.adapters.ExpandableListAdapter;
import com.appcarichi.model.Ordine;
import com.example.appcarichi.R;
import com.appcarichi.model.Rigaordine;
import com.example.appcarichi.databinding.ActivityOrdineBinding;
import com.example.appcarichi.databinding.RigheOrdineBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrdineActivity extends AppCompatActivity {

    RigheOrdineBinding bind_for_buttons;
    ActivityOrdineBinding binding;
    private ExpandableListView expandableListView;
    private ExpandableListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bind_for_buttons = RigheOrdineBinding.inflate(getLayoutInflater());
        binding = ActivityOrdineBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        //recupero il codice del carico
        Intent intent = this.getIntent();
        int codice = intent.getIntExtra("codice", 0);

        init(codice);

    }

    public void init(int codice) {
        expandableListView = (ExpandableListView) findViewById(R.id.orderlistview);

        getGroupData(codice,new VolleyCallback() {
            @Override
            public void onSuccess(ArrayList<Ordine> ordini) {
                String url = "http://192.168.1.158:8080/restCarichi/appCarichi/righeordine";
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                List<Rigaordine> righeordine=new ArrayList<>();
                JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                try{
                                    for(int i=0; i<response.length(); i++){
                                        JSONObject rigaordine = response.getJSONObject(i);
                                        int idrigaordine=rigaordine.getInt("idrigaordine");
                                        String campo1=rigaordine.getString("campo1");
                                        String campo2=rigaordine.getString("campo2");
                                        String campo3=rigaordine.getString("campo3");
                                        String campo4=rigaordine.getString("campo4");
                                        String campo5=rigaordine.getString("campo5");
                                        String campo6=rigaordine.getString("campo6");
                                        int idordine=rigaordine.getInt("idOrdine");

                                        Rigaordine ro = new Rigaordine(idrigaordine,campo1,campo2,campo3,campo4,campo5,campo6,idordine);
                                        righeordine.add(ro);

                                    }
                                    creaListaFinale(righeordine,ordini);


                                }catch(JSONException e){
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                queue.add(request);
            }
        });

    }

    public void creaListaFinale(List<Rigaordine> righeordine, List<Ordine> ordini){
        expandableListView = (ExpandableListView) findViewById(R.id.orderlistview);
        HashMap<Ordine, List<Rigaordine>> childData = new HashMap<>();

        for(int i=0; i<ordini.size(); i++){
            childData.put(ordini.get(i), getRigheOrdine(ordini.get(i),righeordine));
        }

        adapter = new ExpandableListAdapter(this, ordini, childData);
        expandableListView.setAdapter(adapter);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                //Nothing here ever fires
                Intent intent = new Intent(OrdineActivity.this, SpuntaColloActivity.class);
                startActivity(intent);
                return true;
            }
        });
    }

    public List<Rigaordine> getRigheOrdine(Ordine o, List<Rigaordine> ro){
        List<Rigaordine> righeordine=new ArrayList<>();
        for(int i=0; i<ro.size(); i++){
            if(ro.get(i).getIdordine()==o.getIdordine()){
                righeordine.add(ro.get(i));
            }
        }
        return righeordine;
    }

    public void getGroupData(int codice, final VolleyCallback callBack){
        final ArrayList<Ordine> groupData = new ArrayList<>();
        String url="http://192.168.1.158:8080/restCarichi/appCarichi/ordine/"+codice;
        RequestQueue queue= Volley.newRequestQueue(this);
        JsonArrayRequest request=new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>(){
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject ordine = response.getJSONObject(i);
                                int idordine=ordine.getInt("idordine");
                                int idcarico=ordine.getInt("idcarico");
                                String fornitore=ordine.getString("fornitore");
                                String matricola=ordine.getString("matricola");
                                String barcode=ordine.getString("barcode");
                                String luogoconsegna=ordine.getString("luogo_consegna");
                                int tot_colli=ordine.getInt("tot_colli");
                                int colli_consegnati=ordine.getInt("colli_consegnati");
                                Ordine o=new Ordine(idordine,idcarico,tot_colli,colli_consegnati,fornitore,matricola,barcode,luogoconsegna);
                                groupData.add(o);
                            }
                            callBack.onSuccess(groupData);
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
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
