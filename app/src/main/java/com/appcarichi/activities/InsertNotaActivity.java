package com.appcarichi.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.appcarichi.R;
import com.appcarichi.adapters.ListAdapter;
import com.appcarichi.databinding.ActivityInsertNotaBinding;
import com.appcarichi.model.Carico;
import com.appcarichi.model.Nota;
import com.appcarichi.model.NotaRigaOrdine;
import com.appcarichi.model.Rigaordine;
import com.appcarichi.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InsertNotaActivity extends Activity {

    ActivityInsertNotaBinding binding;
    Button salva;
    Spinner selectNota;
    HashMap<String,Nota> mapNota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityInsertNotaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //getActionBar().hide();
        setWindowHeight(32);
        setWindowWidth(67);

        selectNota = (Spinner) findViewById(R.id.spinnernota);

        componiSpinnerNota();

        Intent i = this.getIntent();
        Rigaordine ro = (Rigaordine) i.getSerializableExtra("rigaordine");

        salva = findViewById(R.id.salvanota);
        salva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                EditText commentonota = (EditText) findViewById(R.id.commentonota);
                String commento = commentonota.getText().toString();

                String descrizione = selectNota.getSelectedItem().toString();
                //int codicenota=getCodiceNota(descrizione);

                String utente = settings.getString("username","utente");

                Nota n = mapNota.get(descrizione);

                NotaRigaOrdine nRo = new NotaRigaOrdine(ro,n,commento,utente);

                String url = Utils.getProperty("url.be",getApplicationContext())+"/inserisci-nota";
                RequestQueue requestQueue = Volley.newRequestQueue(InsertNotaActivity.this);

                JSONObject nroData = new JSONObject();
                JSONObject nota = new JSONObject();
                JSONObject rigaordine = new JSONObject();
                try{
                    nota.put("codiceNota",n.getCodicenota());
                    nota.put("descrizioneNota",n.getDescrizione());
                    rigaordine.put("idRigaOrdine",ro.getIdrigarodine());
                    nroData.put("nota",nota);
                    nroData.put("rigaOrdine",rigaordine);
                    nroData.put("commento",commento);
                    nroData.put("utente",utente);
                }catch(JSONException e){
                    e.printStackTrace();
                }

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(getApplicationContext(), "Nota salvata", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Errore nel salvataggio nota", Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        return nroData.toString().getBytes();
                    }
                    @Override
                    public String getBodyContentType() {
                        return "application/json";
                    }
                };
                requestQueue.add(stringRequest);
                finish();
            }
        });

    }

    private void setWindowHeight(int percent) {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenHeight = metrics.heightPixels;
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = (int) (screenHeight * percent / 100);
        this.getWindow().setAttributes(params);
    }

    private void setWindowWidth(int percent) {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenHeight = metrics.widthPixels;
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = (int) (screenHeight * percent / 100);
        this.getWindow().setAttributes(params);
    }

    private int getCodiceNota(String tipo){
        if(tipo.equals("Generica")){
            return 1;
        }else if(tipo.equals("Collo assente")){
            return 2;
        }else if(tipo.equals("Collo in più")){
            return 3;
        }else if(tipo.equals("Imballo rotto")){
            return 4;
        }else if(tipo.equals("Collo raggruppato")){
            return 5;
        }
        else{return 0;}
    }

    private void componiSpinnerNota() {
        String url= Utils.getProperty("url.be",getApplicationContext())+"/note";

        ArrayList<Nota> carichi=new ArrayList<>();
        RequestQueue queue=Volley.newRequestQueue(this);

        //recupero dati dal db
        JsonArrayRequest request=new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>(){
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            List<Nota> notaList = new ArrayList<Nota>();
                            mapNota = new HashMap<String, Nota>();
                            for(int i=0; i<response.length(); i++){
                                JSONObject notaObject=response.getJSONObject(i);
                                int id=notaObject.getInt("idNota");
                                int codice=notaObject.getInt("codiceNota");
                                String descrizione=notaObject.getString("descrizioneNota");

                                Nota nota = new Nota(id,codice,descrizione);
                                notaList.add(nota);
                                mapNota.put(nota.toString(),nota);
                            }

                            ArrayAdapter<Nota> adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, notaList);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            selectNota.setAdapter(adapter);

                        } catch (JSONException e) {
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
