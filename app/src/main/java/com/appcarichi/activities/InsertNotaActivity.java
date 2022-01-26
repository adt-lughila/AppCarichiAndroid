package com.appcarichi.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import model.Nota;
import com.example.listviewex.R;
import model.Rigaordine;
import com.example.listviewex.databinding.ActivityInsertNotaBinding;

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


        Intent i = this.getIntent();
        Rigaordine ro = (Rigaordine) i.getSerializableExtra("rigaordine");

        LinearLayout inserireNote= findViewById(R.id.inserirenote);
        salva = findViewById(R.id.save_nota);
        salva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* operazione di insert nota*/

                int count = inserireNote.getChildCount();
                for (int i = 1; i < count - 1; i++) {
                    LinearLayout linLay = (LinearLayout) inserireNote.getChildAt(i);
                    RadioButton radioButton = (RadioButton) linLay.getChildAt(0);
                    if (radioButton.isChecked()) {
                        TextView txtcodicenota = (TextView) linLay.getChildAt(1);
                        String codicenota = txtcodicenota.getText().toString();
                        int codice_nota = Integer.valueOf(codicenota);
                        TextView txtdescrizione = (TextView) linLay.getChildAt(2);
                        String descrizione = txtdescrizione.getText().toString();
                        TextView txtcommento = (TextView) linLay.getChildAt(3);
                        String commento = txtcommento.getText().toString();
                        Nota n=new Nota(ro.getIdrigarodine(),codice_nota,descrizione,commento);
                        String url="http://192.168.1.158:8080/restCarichi/appCarichi/addnota";
                        RequestQueue requestQueue = Volley.newRequestQueue(InsertNotaActivity.this);

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                JSONObject jsonObject=new JSONObject(response);
                                Toast.makeText(getApplicationContext(), "Nota salvata", Toast.LENGTH_SHORT).show();

                               }catch (JSONException e){
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
                                params.put("codicenota", codicenota);
                                params.put("descrizione", descrizione);
                                params.put("commento", commento);
                                return params;
                            }

                        };
                        requestQueue.add(stringRequest);
                    }
                }
                Intent intent = new Intent(InsertNotaActivity.this, InsertNotaActivity.class);
                intent.putExtra("rigaordine", ro);
                startActivity(intent);
                finish();
            }
        });
    }
}
