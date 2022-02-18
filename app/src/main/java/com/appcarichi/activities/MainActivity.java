package com.appcarichi.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.graphics.drawable.ColorDrawable;

import androidx.drawerlayout.widget.DrawerLayout;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import com.appcarichi.model.Carico;
import com.appcarichi.adapters.ListAdapter;
import com.appcarichi.utils.Utils;
import com.example.appcarichi.R;
import com.example.appcarichi.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  {

    ActivityMainBinding binding;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //menu laterale
        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#CB504D")));

        //load lista carichi iniziale
        getCarichi();

        //refresh pagina
        Button aggiorna=findViewById(R.id.RefreshButton);
        aggiorna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //logout
        Button logout=findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        //trova carico da collo
        Button trovaCarico=findViewById(R.id.trovaCaricoButton);
        trovaCarico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),TrovaCaricoActivity.class);
                startActivity(i);
            }
        });


    }

    //apertura menu laterale
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getCarichi(){

        ListView listview=findViewById(R.id.listview);

        String url= Utils.URL_BE+"/carichi";

        ArrayList<Carico> carichi=new ArrayList<>();
        RequestQueue queue=Volley.newRequestQueue(this);

        //recupero dati dal db
        JsonArrayRequest request=new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>(){
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for(int i=0; i<response.length(); i++){
                                JSONObject carico=response.getJSONObject(i);
                                int idcarico=carico.getInt("idCarico");
                                int codice=carico.getInt("nCarico");
                                String destinazione=carico.getString("negozio");
                                String stato_spedizione=carico.getString("descrStato");
                                String stato_carico=carico.getString("stato");
                                int numTotColli = Integer.valueOf(carico.getString("numTotColli"));
                                int numColliSpuntati = Integer.valueOf(carico.getString("numColliSpuntati"));

                                Carico car=new Carico(idcarico,codice,10,8,
                                        2,destinazione,stato_spedizione,stato_carico,numColliSpuntati,numTotColli);
                                carichi.add(car);

                            }

                            //visualizzazione carichi
                            ListAdapter listAdapter = new ListAdapter(MainActivity.this, carichi);
                            binding.listview.setAdapter(listAdapter);
                            binding.listview.setClickable(true);

                            //vista del dettaglio
                            binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Intent i = new Intent(MainActivity.this, OrdineActivity.class);
                                    i.putExtra("idCarico", carichi.get(position).getIdcarico());
                                    i.putExtra("codice", carichi.get(position).getCodice());
                                    i.putExtra("tot_colli", carichi.get(position).getTot_colli());
                                    i.putExtra("colli_censiti", carichi.get(position).getColli_censiti());
                                    i.putExtra("num_sedute", carichi.get(position).getNum_sedute());
                                    i.putExtra("destinazione", carichi.get(position).getDestinazione());
                                    i.putExtra("stato_spedizione", carichi.get(position).getStato_spedizione());
                                    i.putExtra("statoCarico", carichi.get(position).getStatoCarico());
                                    startActivity(i);
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



