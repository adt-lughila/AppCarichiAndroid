package com.appcarichi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.appcarichi.model.User;
import com.example.appcarichi.R;
import com.example.appcarichi.databinding.LoginBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
                String utente_str=utente.getText().toString();
                String password_str=password.getText().toString();
                String url = "http://192.168.1.158:8080/restCarichi/user/"+utente_str+"/"+password_str;

                JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                try {
                                    for(int i=0; i< response.length(); i++) {
                                        JSONObject user = response.getJSONObject(i);
                                        int user_id=user.getInt("user_id");
                                        String name=user.getString("name");
                                        String password=user.getString("password");
                                        User u=new User(user_id,name,password);
                                        users.add(u);
                                    }

                                    if(users.size()==0){
                                        Toast toast = Toast.makeText(getApplicationContext(),
                                                "nome utente o password errati", Toast.LENGTH_SHORT);
                                        toast.setGravity(Gravity.TOP|Gravity.CENTER, 0, 300);
                                        toast.show();
                                    }
                                    else{
                                        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                                        intent.putExtra("user_id", users.get(0).getUser_id());
                                        intent.putExtra("name", users.get(0).getName());
                                        intent.putExtra("password", users.get(0).getPassword());
                                        startActivity(intent);
                                        finish();
                                    }
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

            });

    }



}
