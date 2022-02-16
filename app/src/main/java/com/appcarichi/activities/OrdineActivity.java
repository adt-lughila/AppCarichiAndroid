package com.appcarichi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.example.appcarichi.R;
import com.appcarichi.model.Rigaordine;
import com.example.appcarichi.databinding.ActivityOrdineBinding;
import com.example.appcarichi.databinding.RigheOrdineBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
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
        codicecarico.setText(String.valueOf(codice));

        init(idcarico);

    }

    public void init(int idcarico) {

        expandableListView = (ExpandableListView) findViewById(R.id.orderlistview);
        getGroupData(idcarico,new VolleyCallback() {
            @Override
            public void onSuccess(ArrayList<Ordine> ordini) {
                List<Rigaordine> righeordine=new ArrayList<>();
                String url = Utils.URL_BE+"/riga-ordine-id-carico?idCarico="+idcarico;
                RequestQueue queue = Volley.newRequestQueue(OrdineActivity.this);
                JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                try{
                                    for(int i=0; i<response.length(); i++){
                                        JSONObject rigaordine = response.getJSONObject(i);
                                        JSONObject ordine = rigaordine.getJSONObject("ordine");
                                        int idrigaordine=rigaordine.getInt("idRigaOrdine");
                                        String codicearticolo=rigaordine.getString("codArt");
                                        String matricola=rigaordine.getString("matricola");
                                        String barcode=rigaordine.getString("barcode");
                                        String descrizione=rigaordine.getString("descArt");
                                        BigDecimal pezziordinati=BigDecimal.valueOf(rigaordine.getInt("qtaOrdinata"));
                                        BigDecimal pezzispediti=BigDecimal.valueOf(rigaordine.getInt("qtaSpedita"));
                                        BigDecimal sconto = null;
                                        if(!rigaordine.getString("sconto").equals("null")) {
                                            sconto=BigDecimal.valueOf(rigaordine.getInt("sconto"));
                                        }
                                        BigDecimal prezzo=BigDecimal.valueOf(rigaordine.getInt("prezzo"));
                                        int idordine=Integer.valueOf(ordine.getInt("idOrdine"));

                                        Rigaordine ro = new Rigaordine(idrigaordine,codicearticolo,
                                                matricola,barcode,descrizione,pezziordinati,pezzispediti,sconto,prezzo,idordine);
                                        righeordine.add(ro);

                                    }
                                    creaListaFinale(righeordine,ordini,idcarico);

                                }catch(JSONException e){
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
                request.setRetryPolicy(new DefaultRetryPolicy(30 * 1000, 1, 1.0f));
                queue.add(request);
            }
        });

    }

    public void creaListaFinale(List<Rigaordine> righeordine, List<Ordine> ordini, int carico){
        expandableListView = (ExpandableListView) findViewById(R.id.orderlistview);
        HashMap<Ordine, List<Rigaordine>> childData = new HashMap<>();

        for(int i=0; i<ordini.size(); i++){
            childData.put(ordini.get(i), getRigheOrdine(ordini.get(i),righeordine));
        }

        adapter = new ExpandableListAdapter(this, ordini, childData);
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
        String url=Utils.URL_BE+"/ordine-id-carico?idCarico="+idcarico;
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

                                Ordine o=new Ordine(idordine,idcarico,10,7,fornitore,cliente,tipoordine,"TO");
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
