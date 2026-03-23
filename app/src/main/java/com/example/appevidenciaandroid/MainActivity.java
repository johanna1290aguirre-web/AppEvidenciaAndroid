package com.example.appevidenciaandroid;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnHumedad = findViewById(R.id.btnHumedad);
        Button btnProctor = findViewById(R.id.btnProctor);
        Button btnLimites = findViewById(R.id.btnLimites);
        Button btnDensidad = findViewById(R.id.btnDensidad);
        Button btnGravedad = findViewById(R.id.btnGravedad);

        btnHumedad.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, HumedadActivity.class)));

        btnProctor.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, ProctorActivity.class)));

        btnLimites.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, LimitesActivity.class)));

        btnDensidad.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, DensidadCampoActivity.class)));

        btnGravedad.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, GravedadActivity.class)));
    }
}