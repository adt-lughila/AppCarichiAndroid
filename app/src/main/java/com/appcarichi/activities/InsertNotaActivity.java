package com.appcarichi.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.appcarichi.R;
import com.appcarichi.databinding.ActivityInsertNotaBinding;
import com.appcarichi.model.Nota;
import com.appcarichi.model.NotaRigaOrdine;
import com.appcarichi.model.Rigaordine;
import com.appcarichi.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

public class InsertNotaActivity extends Activity {

    ActivityInsertNotaBinding binding;
    Button salva;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityInsertNotaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //getActionBar().hide();
        setWindowHeight(32);
        setWindowWidth(67);

        Intent i = this.getIntent();
        Rigaordine ro = (Rigaordine) i.getSerializableExtra("rigaordine");

        salva = findViewById(R.id.salvanota);
        salva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText commentonota = (EditText) findViewById(R.id.commentonota);
                String commento = commentonota.getText().toString();
                Spinner descrizionenota = (Spinner) findViewById(R.id.spinnernota);
                String descrizione = descrizionenota.getSelectedItem().toString();
                int codicenota=getCodiceNota(descrizione);

                Nota n = new Nota(codicenota, descrizione);

                NotaRigaOrdine nRo = new NotaRigaOrdine(ro,n,commento,"utente");

                String url = Utils.getProperty("url.be",getApplicationContext())+"/inserisci-nota";
                RequestQueue requestQueue = Volley.newRequestQueue(InsertNotaActivity.this);

                JSONObject nroData = new JSONObject();
                JSONObject nota = new JSONObject();
                JSONObject rigaordine = new JSONObject();
                try{
                    nota.put("codiceNota",codicenota);
                    nota.put("descrizioneNota",descrizione);
                    rigaordine.put("idRigaOrdine",ro.getIdrigarodine());
                    nroData.put("nota",nota);
                    nroData.put("rigaOrdine",rigaordine);
                    nroData.put("commento",commento);
                    nroData.put("utente","utente");
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


}
