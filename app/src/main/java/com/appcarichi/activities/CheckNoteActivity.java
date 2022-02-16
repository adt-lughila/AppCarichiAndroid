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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.appcarichi.adapters.NoteListAdapter;
import com.appcarichi.model.Nota;
import com.appcarichi.model.NotaRigaOrdine;
import com.appcarichi.model.Rigaordine;
import com.appcarichi.utils.Utils;
import com.example.appcarichi.R;
import com.example.appcarichi.databinding.ActivityVisualizzaNoteBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

        String url= Utils.URL_BE+"/nota-riga-ordine?idRigaOrdine="+idrigaordine;
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
                                JSONObject rigaordine = notaRigaOrdine.getJSONObject("rigaOrdine");
                                int idnota = nota.getInt("idNota");
                                int codicenota = nota.getInt("codiceNota");
                                String descrizione = nota.getString("descrizioneNota");
                                int idrigaordine=rigaordine.getInt("idRigaOrdine");
                                String utente=notaRigaOrdine.getString("utente");
                                String commento=notaRigaOrdine.getString("commento");

                                Nota n = new Nota(codicenota,descrizione);
                                NotaRigaOrdine nro=new NotaRigaOrdine(idrigaordine,null,n,commento);
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

                                                String url2 = Utils.URL_BE+"/delete-nota-riga-ordine?idNotaRigaOrdine=26";
                                                RequestQueue queue2 = Volley.newRequestQueue(CheckNoteActivity.this);

                                                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url2, null,
                                                        new Response.Listener<JSONObject>() {
                                                            @Override
                                                            public void onResponse(JSONObject response) {
                                                                Intent i = new Intent(CheckNoteActivity.this, CheckNoteActivity.class);
                                                                i.putExtra("rigaordine", ro);
                                                                startActivity(i);
                                                                finish();
                                                            }
                                                        },
                                                        new Response.ErrorListener() {
                                                            @Override
                                                            public void onErrorResponse(VolleyError error) {

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
                          /*  modifica.setOnClickListener(new View.OnClickListener() {
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
                                                Nota nota = (Nota) notelistview.getAdapter().getItem(i);
                                                int idnota = nota.getIdnota();
                                                String commento = editText.getText().toString();
                                                String url3 = "http://192.168.1.158:8080/restCarichi/appCarichi/updatenota/" + idnota;
                                                RequestQueue queue3 = Volley.newRequestQueue(CheckNoteActivity.this);
                                                StringRequest putRequest = new StringRequest(Request.Method.PUT, url3,
                                                        new Response.Listener<String>()
                                                        {
                                                            @Override
                                                            public void onResponse(String response) {

                                                            }
                                                        },
                                                        new Response.ErrorListener()
                                                        {
                                                            @Override
                                                            public void onErrorResponse(VolleyError error) {

                                                            }
                                                        }
                                                ) {
                                                    @Override
                                                    protected Map<String, String> getParams() throws AuthFailureError {
                                                        Map<String, String>  params = new HashMap<String, String> ();
                                                        params.put("commento", commento);
                                                        params.put("idnota", String.valueOf(idnota));
                                                        return params;
                                                    }
                                                };
                                                queue3.add(putRequest);
                                                Intent intent = new Intent(CheckNoteActivity.this, CheckNoteActivity.class);
                                                intent.putExtra("rigaordine", ro);
                                                startActivity(intent);
                                                finish();

                                            }
                                        }
                                    }catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }); */

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


