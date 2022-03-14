package com.appcarichi.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.appcarichi.R;
import com.appcarichi.adapters.NoteListAdapter;
import com.appcarichi.databinding.ActivityVisualizzaNoteBinding;
import com.appcarichi.model.Nota;
import com.appcarichi.model.NotaRigaOrdine;
import com.appcarichi.model.Rigaordine;
import com.appcarichi.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CheckNoteActivity extends Activity {

    ActivityVisualizzaNoteBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivityVisualizzaNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        setWindowHeight(65);

        Button elimina = findViewById(R.id.remove_nota);
        Button modifica = findViewById(R.id.modify_nota);
        ListView notelistview=findViewById(R.id.notelistview);


        Intent intent=this.getIntent();
        Rigaordine ro = (Rigaordine) intent.getSerializableExtra("rigaordine");
        int idrigaordine = ro.getIdrigarodine();
        elimina.setVisibility(ro.isCaricoSpedito() ? View.INVISIBLE : View.VISIBLE);
        modifica.setVisibility(ro.isCaricoSpedito() ? View.INVISIBLE : View.VISIBLE);

        String url= Utils.getProperty("url.be",getApplicationContext())+"/nota-riga-ordine?idRigaOrdine="+idrigaordine;
        ArrayList<NotaRigaOrdine> note=new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject notaRigaOrdine = response.getJSONObject(i);
                                JSONObject nota = notaRigaOrdine.getJSONObject("nota");
                                int codicenota = nota.getInt("codiceNota");
                                int idNota = nota.getInt("idNota");
                                String descrizione = nota.getString("descrizioneNota");
                                int idNotaRigaOrdine=notaRigaOrdine.getInt("idNotaRigaOrdine");
                                String utente=notaRigaOrdine.getString("utente");
                                String commento=notaRigaOrdine.getString("commento");

                                Nota n = new Nota(idNota,codicenota,descrizione);
                                NotaRigaOrdine nro=new NotaRigaOrdine(idNotaRigaOrdine,null,n,commento,utente);
                                note.add(nro);

                                NoteListAdapter noteListAdapter = new NoteListAdapter(CheckNoteActivity.this,note);
                                binding.notelistview.setAdapter(noteListAdapter);
                            }

                            elimina.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    RelativeLayout relativeLayout;
                                    LinearLayout linearLayout;
                                    CheckBox checkbox;
                                    try {
                                        int count = notelistview.getAdapter().getCount();
                                        for (int i = 0; i < count; i++) {
                                            relativeLayout = (RelativeLayout) notelistview.getChildAt(i);
                                            linearLayout = (LinearLayout) relativeLayout.getChildAt(0);
                                            checkbox = (CheckBox) linearLayout.getChildAt(0);
                                            if (checkbox.isChecked()) {
                                                NotaRigaOrdine notaRO = (NotaRigaOrdine) notelistview.getAdapter().getItem(i);

                                                String url2 = Utils.getProperty("url.be",getApplicationContext())+"/delete-nota-riga-ordine?idNotaRigaOrdine="+notaRO.getIdNotaRigaOrdine();
                                                RequestQueue queue2 = Volley.newRequestQueue(CheckNoteActivity.this);

                                                StringRequest request = new StringRequest(Request.Method.POST, url2,
                                                        new Response.Listener<String>() {
                                                            @Override
                                                            public void onResponse(String response) {
                                                                Intent i = new Intent(CheckNoteActivity.this, CheckNoteActivity.class);
                                                                i.putExtra("rigaordine", ro);
                                                                startActivity(i);
                                                                finish();
                                                            }
                                                        },
                                                        new Response.ErrorListener() {
                                                            @Override
                                                            public void onErrorResponse(VolleyError error) {
                                                                Log.i("sono in errore","sono in errore");
                                                            }

                                                        });
                                                queue2.add(request);
                                            }
                                        }
                                    }catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            modifica.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    RelativeLayout relativeLayout;
                                    LinearLayout linearLayout;
                                    CheckBox checkbox;
                                    EditText editText;
                                    try {
                                        int count = notelistview.getAdapter().getCount();
                                        for (int i = 0; i < count; i++) {
                                            relativeLayout = (RelativeLayout) notelistview.getChildAt(i);
                                            linearLayout = (LinearLayout) relativeLayout.getChildAt(0);
                                            checkbox = (CheckBox) linearLayout.getChildAt(0);
                                            editText = (EditText) linearLayout.getChildAt(3);

                                            if (checkbox.isChecked()) {
                                                NotaRigaOrdine notaRO = (NotaRigaOrdine) notelistview.getAdapter().getItem(i);
                                                int idNotaRigaOrdine = notaRO.getIdNotaRigaOrdine();
                                                String commento = editText.getText().toString();
                                                String url3 = Utils.getProperty("url.be",getApplicationContext())+"/modifica-nota-riga-ordine";
                                                JSONObject rigaOrdineData = new JSONObject();
                                                try {
                                                    rigaOrdineData.put("idNotaRigaOrdine", idNotaRigaOrdine);
                                                    rigaOrdineData.put("commento", commento);

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                RequestQueue queue3 = Volley.newRequestQueue(CheckNoteActivity.this);
                                                StringRequest stringRequest = new StringRequest(Request.Method.POST, url3, new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        Intent i = new Intent(CheckNoteActivity.this, CheckNoteActivity.class);
                                                        i.putExtra("rigaordine", ro);
                                                        startActivity(i);
                                                        finish();
                                                    }},
                                                        new Response.ErrorListener()
                                                        {
                                                            @Override
                                                            public void onErrorResponse(VolleyError error) {

                                                            }
                                                        }
                                                ){
                                                    @Override
                                                    public byte[] getBody() throws AuthFailureError {
                                                        return rigaOrdineData.toString().getBytes();
                                                    }
                                                    @Override
                                                    public String getBodyContentType() {
                                                        return "application/json";
                                                    }
                                                };

                                                queue3.add(stringRequest);

                                            }
                                        }
                                    }catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

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
}


