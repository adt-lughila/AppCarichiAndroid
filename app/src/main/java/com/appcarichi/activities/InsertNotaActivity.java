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
import com.appcarichi.model.Nota;
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
                Spinner tiponota = (Spinner) findViewById(R.id.spinnernota);
                String tipo = tiponota.getSelectedItem().toString();
                int codicenota=getCodiceNota(tipo);
                System.out.println(codicenota);

                Nota n = new Nota(ro.getIdrigarodine(), codicenota, tipo, commento);
                String url = "http://192.168.1.158:8080/restCarichi/appCarichi/addnota";
                RequestQueue requestQueue = Volley.newRequestQueue(InsertNotaActivity.this);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Toast.makeText(getApplicationContext(), "Nota salvata", Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("idrigaordine", String.valueOf(ro.getIdrigarodine()));
                        params.put("codicenota", String.valueOf(codicenota));
                        params.put("descrizione", tipo);
                        params.put("commento", commento);
                        return params;
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
