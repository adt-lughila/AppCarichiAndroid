package com.appcarichi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.appcarichi.adapters.ExpandableListAdapter;
import com.appcarichi.model.Ordine;
import com.appcarichi.utils.Utils;
import com.appcarichi.R;
import com.appcarichi.model.Rigaordine;
import com.appcarichi.model.Carico;
import com.appcarichi.databinding.ActivityOrdineBinding;
import com.appcarichi.databinding.RigheOrdineBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        int idcarico = intent.getIntExtra("idCarico", 0);
        int codice = intent.getIntExtra("codice", 0);
        TextView codicecarico = binding.numerocaricoordini;
        codicecarico.setText("ORDINI NEL CARICO "+String.valueOf(codice));

        init(idcarico,codice);

        Button spuntaOrdine=findViewById(R.id.spuntaordine);
        spuntaOrdine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),SpuntaColloOrdineActivity.class);
                intent.putExtra("idCarico",idcarico);
                intent.putExtra("codice",codice);
                startActivity(intent);
                finish();
            }
        });

        Button aggiorna=findViewById(R.id.aggiornaordini);
        aggiorna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrdineActivity.this,OrdineActivity.class);
                intent.putExtra("idCarico",idcarico);
                intent.putExtra("codice",codice);
                startActivity(intent);
                finish();
            }
        });

    }

    public void init(int codice,int numeroCarico) {
        expandableListView = (ExpandableListView) findViewById(R.id.orderlistview);
        getGroupData(codice,new VolleyCallback() {
            @Override
            public void onSuccess(ArrayList<Ordine> ordini) {
                String url = Utils.getProperty("url.be",getApplicationContext())+"/riga-ordine-id-carico?idCarico="+codice;
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                List<Rigaordine> righeordine=new ArrayList<>();
                final boolean[] caricoSpedito = {false};
                JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                try{
                                    for(int i=0; i<response.length(); i++){
                                        JSONObject rigaordine = response.getJSONObject(i);
                                        JSONObject ordine = rigaordine.getJSONObject("ordine");
                                        JSONObject carico = ordine.getJSONObject("carico");
                                        caricoSpedito[0] = !carico.isNull("dataSpedizione") || !carico.isNull("commentoSpedizione");
                                        int idrigaordine=rigaordine.getInt("idRigaOrdine");
                                        String codicearticolo=rigaordine.getString("codArt");
                                        String matricola=rigaordine.getString("matricola");
                                        String barcode=rigaordine.getString("barcode");
                                        String descrizione=rigaordine.getString("descArt");
                                        BigDecimal pezziordinati=BigDecimal.valueOf(rigaordine.getInt("qtaOrdinata"));
                                        BigDecimal pezzispediti=BigDecimal.valueOf(rigaordine.getInt("qtaSpedita"));
                                        String scontoString = rigaordine.getString("sconto");
                                        BigDecimal sconto = null;
                                        if(!("null").equals(scontoString)) {
                                            sconto = (new BigDecimal(scontoString)).setScale(2, RoundingMode.CEILING);
                                        }
                                        BigDecimal prezzo=(BigDecimal.valueOf(rigaordine.getDouble("prezzo"))).setScale(2, RoundingMode.CEILING);
                                        int idordine=Integer.valueOf(ordine.getInt("idOrdine"));
                                        int nroColli=Integer.valueOf(rigaordine.getInt("nroColli"));
                                        int colliSpuntati=Integer.valueOf(rigaordine.getInt("colliSpuntati"));

                                        Rigaordine ro = new Rigaordine(idrigaordine,codicearticolo,
                                                matricola,barcode,descrizione,pezziordinati,pezzispediti,sconto,prezzo,idordine,nroColli,colliSpuntati);
                                        ro.setCaricoSpedito(caricoSpedito[0]);
                                        righeordine.add(ro);

                                    }
                                    creaListaFinale(righeordine,ordini,numeroCarico, caricoSpedito[0]);


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

    public void creaListaFinale(List<Rigaordine> righeordine, List<Ordine> ordini, int carico, boolean caricoSpedito){
        expandableListView = (ExpandableListView) findViewById(R.id.orderlistview);
        HashMap<Ordine, List<Rigaordine>> childData = new HashMap<>();

        for(int i=0; i<ordini.size(); i++){
            childData.put(ordini.get(i), getRigheOrdine(ordini.get(i),righeordine));
        }

        adapter = new ExpandableListAdapter(this, ordini, childData, carico,caricoSpedito);
        expandableListView.setAdapter(adapter);
      /*  expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                Intent intent = new Intent(OrdineActivity.this, SpuntaColloActivity.class);
                intent.putExtra("idcarico",carico);
                startActivity(intent);
                return true;
            }
        }); */
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

    public void getGroupData(int idcarico, final VolleyCallback callBack){
        final ArrayList<Ordine> groupData = new ArrayList<>();
        String url=Utils.getProperty("url.be",getApplicationContext())+"/ordine-id-carico?idCarico="+idcarico;
        RequestQueue queue= Volley.newRequestQueue(this);
        JsonArrayRequest request=new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>(){
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject ordine = response.getJSONObject(i);
                                int idordine=ordine.getInt("idOrdine");
                                String fornitore=ordine.getString("ragSocFornit");
                                String cliente=ordine.getString("cliente");
                                String tipoordine=ordine.getString("tipoOrd");
                                int totColli=ordine.getInt("numTotColli");
                                int colliSpuntati=ordine.getInt("numColliSpuntati");

                                Ordine o=new Ordine(idordine,idcarico,totColli,colliSpuntati,fornitore,cliente,tipoordine,"TO");
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
