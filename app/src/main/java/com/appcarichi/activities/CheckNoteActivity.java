package com.appcarichi.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.appcarichi.adapters.NoteListAdapter;
import com.appcarichi.model.Nota;
import com.appcarichi.model.Rigaordine;
import com.example.listviewex.R;
import com.example.listviewex.databinding.ActivityVisualizzaNoteBinding;

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

        Button elimina = findViewById(R.id.remove_nota);
        ListView notelistview=findViewById(R.id.notelistview);

        Intent intent=this.getIntent();
        Rigaordine ro = (Rigaordine) intent.getSerializableExtra("rigaordine");

        int idrigaordine = ro.getIdrigarodine();


        String url="http://192.168.1.158:8080/restCarichi/appCarichi/note/"+idrigaordine;
        ArrayList<Nota> note=new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject nota = response.getJSONObject(i);
                                int idnota=nota.getInt("idnota");
                                int idrigaordine=nota.getInt("idrigaordine");
                                int codicenota=nota.getInt("codicenota");
                                String descrizione=nota.getString("descrizione");
                                String commento=nota.getString("commento");

                                Nota n=new Nota(idnota,idrigaordine,codicenota,descrizione,commento);
                                note.add(n);

                                NoteListAdapter noteListAdapter = new NoteListAdapter(CheckNoteActivity.this,note);
                                binding.notelistview.setAdapter(noteListAdapter);
                            }

                            elimina.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    RelativeLayout relativeLayout;
                                    LinearLayout linearLayout;
                                    CheckBox checkbox;
                                    int count=notelistview.getAdapter().getCount();
                                    for(int i=0; i<count; i++){
                                        relativeLayout = (RelativeLayout) notelistview.getChildAt(i);
                                        linearLayout = (LinearLayout) relativeLayout.getChildAt(0);
                                        checkbox= (CheckBox) linearLayout.getChildAt(0);
                                        if(checkbox.isChecked()){
                                            Nota nota= (Nota)notelistview.getAdapter().getItem(i);
                                            int idnota=nota.getIdnota();
                                            String url2="http://192.168.1.158:8080/restCarichi/appCarichi/delnote/"+idnota;
                                            RequestQueue queue2 = Volley.newRequestQueue(CheckNoteActivity.this);
                                            JsonObjectRequest request = new  JsonObjectRequest(Request.Method.DELETE,url2,null,
                                                    new Response.Listener<JSONObject>() {
                                                        @Override
                                                        public void onResponse(JSONObject response) {
                                                            Intent i = new Intent(CheckNoteActivity.this, CheckNoteActivity.class);
                                                            i.putExtra("rigaordine",ro);
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
                                    Intent i = new Intent(CheckNoteActivity.this, CheckNoteActivity.class);
                                    i.putExtra("rigaordine",ro);
                                    finish();
                                    overridePendingTransition(0, 0);
                                    startActivity(i);
                                    overridePendingTransition(0, 0);
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
}


