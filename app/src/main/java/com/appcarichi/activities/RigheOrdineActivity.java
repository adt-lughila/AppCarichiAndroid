package com.appcarichi.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appcarichi.databinding.ActivityMainBinding;
import com.example.appcarichi.databinding.RigheOrdineBinding;

public class RigheOrdineActivity extends AppCompatActivity {

    RigheOrdineBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = RigheOrdineBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

    }

}
