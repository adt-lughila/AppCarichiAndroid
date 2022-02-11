package com.appcarichi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.appcarichi.model.User;
import com.example.appcarichi.R;
import com.example.appcarichi.databinding.LoginBinding;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    LoginBinding binding;
    EditText utente, password;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = LoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        login = findViewById(R.id.loginbutton);
        ArrayList<User> users=new ArrayList<>();
        RequestQueue queue= Volley.newRequestQueue(this);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                utente = (EditText) findViewById(R.id.username);
                password = (EditText) findViewById(R.id.password);
                String utente_str = utente.getText().toString();
                String password_str = password.getText().toString();
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                String url = "http://192.168.1.158:8080/resources/login";

                JSONObject postData = new JSONObject();
                try {
                    postData.put("username", utente_str);
                    postData.put("password", password_str);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(getApplicationContext(), "username o password errati", Toast.LENGTH_SHORT).show();
                    }
                });
                queue.add(jsonObjectRequest);

            }
        });
    }
}

