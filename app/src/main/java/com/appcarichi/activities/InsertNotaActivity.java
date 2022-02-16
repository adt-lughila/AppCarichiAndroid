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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.appcarichi.model.Nota;
import com.appcarichi.model.NotaRigaOrdine;
import com.appcarichi.utils.Utils;
import com.example.appcarichi.R;
import com.appcarichi.model.Rigaordine;
import com.example.appcarichi.databinding.ActivityInsertNotaBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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

                String url = Utils.URL_BE+"/inserisci-nota";
                RequestQueue requestQueue = Volley.newRequestQueue(InsertNotaActivity.this);
                JSONObject notaRO = new JSONObject();
                try {
                    notaRO.put("rigaOrdine", ro);
                    notaRO.put("nota",n);
                    notaRO.put("commento",commento);
                    notaRO.put("utente", "utente");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,notaRO, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Toast.makeText(getApplicationContext(), "Nota salvata", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Errore nel salvataggio nota", Toast.LENGTH_SHORT).show();
                    }
                });
                requestQueue.add(jsonObjectRequest);
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
