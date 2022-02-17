package com.appcarichi.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.appcarichi.model.Carico;
import com.appcarichi.utils.Utils;
import com.example.appcarichi.R;
import com.example.appcarichi.databinding.SpuntaColloBinding;
import com.example.appcarichi.databinding.TrovaCaricoBinding;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class TrovaCaricoActivity extends AppCompatActivity {

    TrovaCaricoBinding binding;
    private SurfaceView surfaceView;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private ToneGenerator toneGen1;
    private TextView barcodeText;
    private String barcodeData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = TrovaCaricoBinding.inflate(getLayoutInflater());
        setContentView(R.layout.trova_carico);
        toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
        surfaceView = findViewById(R.id.surface_view_trovacarico);
        barcodeText = findViewById(R.id.barcode_text_trovacarico);

        Button cercaCarico = findViewById(R.id.ricercacarico);

        cercaCarico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText barcodeEditText = findViewById(R.id.editTextTrovaCarico);
                String barcodeEdited = barcodeEditText.getText().toString();
                String barcodeRilevato = barcodeText.getText().toString();

                if(!barcodeRilevato.equals("Barcode non rilevato")){
                    String url = Utils.URL_BE+"/carico-barcode/"+barcodeEdited;

                    RequestQueue queue=Volley.newRequestQueue(TrovaCaricoActivity.this);

                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        JSONObject carico=response;
                                        int idcarico=carico.getInt("idCarico");
                                        int codice=carico.getInt("nCarico");
                                        String destinazione=carico.getString("desCarico");
                                        String stato_spedizione=carico.getString("descrStato");
                                        String stato_carico=carico.getString("stato");

                                        Carico car=new Carico(idcarico,codice,10,8,
                                                2,destinazione,stato_spedizione,stato_carico);
                                        Intent i = new Intent(TrovaCaricoActivity.this, OrdineActivity.class);
                                        i.putExtra("idCarico", car.getIdcarico());
                                        i.putExtra("codice", car.getCodice());
                                        i.putExtra("tot_colli", car.getTot_colli());
                                        i.putExtra("colli_censiti", car.getColli_censiti());
                                        i.putExtra("num_sedute", car.getNum_sedute());
                                        i.putExtra("destinazione", car.getDestinazione());
                                        i.putExtra("stato_spedizione", car.getStato_spedizione());
                                        i.putExtra("statoCarico", car.getStatoCarico());
                                        startActivity(i);

                                    }catch(JSONException e){
                                        e.printStackTrace();
                                    }
                                }
                            },new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Nessun carico trovato", Toast.LENGTH_SHORT).show();
                        }
                    });
                    queue.add(request);

                }
                else {
                    String url = Utils.URL_BE + "/carico-barcode/" + barcodeEdited;

                    RequestQueue queue = Volley.newRequestQueue(TrovaCaricoActivity.this);

                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        JSONObject carico = response;
                                        int idcarico = carico.getInt("idCarico");
                                        int codice = carico.getInt("nCarico");
                                        String destinazione = carico.getString("desCarico");
                                        String stato_spedizione = carico.getString("descrStato");
                                        String stato_carico = carico.getString("stato");

                                        Carico car = new Carico(idcarico, codice, 10, 8,
                                                2, destinazione, stato_spedizione, stato_carico);
                                        Intent i = new Intent(TrovaCaricoActivity.this, OrdineActivity.class);
                                        i.putExtra("idCarico", car.getIdcarico());
                                        i.putExtra("codice", car.getCodice());
                                        i.putExtra("tot_colli", car.getTot_colli());
                                        i.putExtra("colli_censiti", car.getColli_censiti());
                                        i.putExtra("num_sedute", car.getNum_sedute());
                                        i.putExtra("destinazione", car.getDestinazione());
                                        i.putExtra("stato_spedizione", car.getStato_spedizione());
                                        i.putExtra("statoCarico", car.getStatoCarico());
                                        startActivity(i);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Nessun carico trovato", Toast.LENGTH_SHORT).show();
                        }
                    });
                    queue.add(request);
                }
            }
        });


        initialiseDetectorsAndSources();

    }

    private void initialiseDetectorsAndSources() {

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080).setFacing(0)
                .setAutoFocusEnabled(true)
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(TrovaCaricoActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(TrovaCaricoActivity.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                // Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {


                    barcodeText.post(new Runnable() {

                        @Override
                        public void run() {

                            if (barcodes.valueAt(0).email != null) {
                                barcodeText.removeCallbacks(null);
                                barcodeData = barcodes.valueAt(0).email.address;
                                barcodeText.setText(barcodeData);
                                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
                            } else {

                                barcodeData = barcodes.valueAt(0).displayValue;
                                barcodeText.setText(barcodeData);
                                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);

                            }
                        }
                    });

                }
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        getSupportActionBar().hide();
        cameraSource.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportActionBar().hide();
        initialiseDetectorsAndSources();
    }
}
