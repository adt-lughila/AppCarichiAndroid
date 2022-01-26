package com.appcarichi.listviewex;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.listviewex.databinding.ActivityMainBinding;
import com.example.listviewex.databinding.RigheOrdineBinding;

public class RigheOrdine extends AppCompatActivity {

    RigheOrdineBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = RigheOrdineBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

    }

}
