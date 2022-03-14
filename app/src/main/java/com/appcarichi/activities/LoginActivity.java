package com.appcarichi.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.appcarichi.R;
import com.appcarichi.databinding.LoginBinding;
import com.appcarichi.model.User;
import com.appcarichi.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    LoginBinding binding;
    EditText utente, password;
    Button login;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = settings.edit();

        binding = LoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        utente = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        login = findViewById(R.id.loginbutton);

        utente.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent keyevent) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    utente.clearFocus();
                    password.requestFocus();
                    password.setCursorVisible(true);
                    return true;
                }
                return false;
            }});

        //password.setOnKeyListener(new View.OnKeyListener() {
        //    public boolean onKey(View view, int keyCode, KeyEvent keyevent) {
        //        if (keyCode == KeyEvent.KEYCODE_ENTER) {
        //            password.clearFocus();
        //            login.callOnClick();
        //            return true;
        //        }
        //        return false;
        //    }});


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
                String url = Utils.getProperty("url.be",getApplicationContext())+"/login";

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
                        editor.putString("username", utente.getText().toString());
                        editor.commit();
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

