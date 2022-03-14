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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.appcarichi.R;
import com.appcarichi.databinding.SpuntaColloBinding;
import com.appcarichi.model.Rigaordine;
import com.appcarichi.utils.Utils;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class SpuntaColloOrdineActivity extends AppCompatActivity {

    SpuntaColloBinding binding;
    private SurfaceView surfaceView;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private ToneGenerator toneGen1;
    private TextView barcodeText;
    private EditText editText;
    private String barcodeData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = SpuntaColloBinding.inflate(getLayoutInflater());
        setContentView(R.layout.spunta_collo);
        toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
        surfaceView = findViewById(R.id.surface_view);
        barcodeText = findViewById(R.id.barcode_text);
        editText = findViewById(R.id.editText);

        Intent intent = this.getIntent();
        int idCarico = intent.getIntExtra("idCarico",0);
        int carico = intent.getIntExtra("nCarico", 0);
        int codice = intent.getIntExtra("codice", 0);
        TextView idcarico = findViewById(R.id.spuntadelcollo);
        idcarico.setText("Spunta del collo, carico "+String.valueOf(codice));


        Button confermaSpunta = findViewById(R.id.confermaspunta);

        confermaSpunta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!barcodeText.getText().toString().equals("Barcode non rilevato") && barcodeText.getText() != null){
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    String url = Utils.getProperty("url.be",getApplicationContext())+"/spunta-collo-ordine";

                    JSONObject postData = new JSONObject();
                    try {
                        postData.put("barcode", barcodeText.getText());
                        postData.put("idCarico", idCarico);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Intent i = new Intent(getApplicationContext(), SpuntaColloOrdineActivity.class);
                            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                        }},
                            new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Errore nella spunta del barcode", Toast.LENGTH_LONG).show();
                        }
                    }){
                        @Override
                        public byte[] getBody() throws AuthFailureError {
                            return postData.toString().getBytes();
                        }
                        @Override
                        public String getBodyContentType() {
                            return "application/json";
                        }
                    };
                    queue.add(stringRequest);
                    /*
                    if(ro.getColliSpuntati() >= ro.getNroColli()) {
                        Toast.makeText(getApplicationContext(), "Collo già letto", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(), SpuntaColloOrdineActivity.class);
                        i.putExtra("idRigaOrdine", idRigaOrdine);
                        i.putExtra("nCarico", carico);
                        startActivity(i);
                        finish();
                    }else {

                    }
                    */
                }
                else{
                    Toast.makeText(getApplicationContext(), "Barcode errato", Toast.LENGTH_SHORT).show();
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
                    if (ActivityCompat.checkSelfPermission(SpuntaColloOrdineActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(SpuntaColloOrdineActivity.this, new
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
