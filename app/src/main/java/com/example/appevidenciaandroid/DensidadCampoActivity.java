package com.example.appevidenciaandroid;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class DensidadCampoActivity extends AppCompatActivity {

    // EditText
    private EditText etDensidadMaxima, etHumedadOptima;
    private EditText etPesoInicial, etPesoFinal, etPesoEmbudoPlaca, etDensidadArena;
    private EditText etPesoHumedo, etHumedadSuelo;

    // TextView resultados
    private TextView tvMasaTotalHueco, tvMasaSoloHueco, tvVolumenHueco;
    private TextView tvMasaSeca;
    private TextView tvDensidadHumeda, tvDensidadSeca, tvPesoUnitario;
    private TextView tvCompactacion, tvMensajeCompactacion;

    private Button btnCalcular, btnVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_densidad_campo);

        initViews();
        setupListeners();
    }

    private void initViews() {
        // Proctor
        etDensidadMaxima = findViewById(R.id.etDensidadMaxima);
        etHumedadOptima = findViewById(R.id.etHumedadOptima);

        // Ensayo
        etPesoInicial = findViewById(R.id.etPesoInicial);
        etPesoFinal = findViewById(R.id.etPesoFinal);
        etPesoEmbudoPlaca = findViewById(R.id.etPesoEmbudoPlaca);
        etDensidadArena = findViewById(R.id.etDensidadArena);

        // Suelo
        etPesoHumedo = findViewById(R.id.etPesoHumedo);
        etHumedadSuelo = findViewById(R.id.etHumedadSuelo);

        // Resultados
        tvMasaTotalHueco = findViewById(R.id.tvMasaTotalHueco);
        tvMasaSoloHueco = findViewById(R.id.tvMasaSoloHueco);
        tvVolumenHueco = findViewById(R.id.tvVolumenHueco);
        tvMasaSeca = findViewById(R.id.tvMasaSeca);
        tvDensidadHumeda = findViewById(R.id.tvDensidadHumeda);
        tvDensidadSeca = findViewById(R.id.tvDensidadSeca);
        tvPesoUnitario = findViewById(R.id.tvPesoUnitario);
        tvCompactacion = findViewById(R.id.tvCompactacion);
        tvMensajeCompactacion = findViewById(R.id.tvMensajeCompactacion);

        // Botones
        btnCalcular = findViewById(R.id.btnCalcularDensidad);
        btnVolver = findViewById(R.id.btnVolverDensidad);
    }

    private void setupListeners() {
        btnCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcularDensidadCampo();
            }
        });

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void calcularDensidadCampo() {
        try {
            // Validar campos
            if (!validarCampos()) {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Obtener valores
            double pesoInicial = Double.parseDouble(etPesoInicial.getText().toString());
            double pesoFinal = Double.parseDouble(etPesoFinal.getText().toString());
            double pesoEmbudoPlaca = Double.parseDouble(etPesoEmbudoPlaca.getText().toString());
            double densidadArena = Double.parseDouble(etDensidadArena.getText().toString());
            double pesoHumedo = Double.parseDouble(etPesoHumedo.getText().toString());
            double humedadSuelo = Double.parseDouble(etHumedadSuelo.getText().toString());
            double densidadMaxima = Double.parseDouble(etDensidadMaxima.getText().toString());

            // 1. Calcular volumen del hueco
            double masaTotalHueco = pesoInicial - pesoFinal;
            double masaSoloHueco = masaTotalHueco - pesoEmbudoPlaca;
            double volumenHueco = masaSoloHueco / densidadArena;

            // 2. Calcular masa seca
            double masaSeca = pesoHumedo / (1 + (humedadSuelo / 100));

            // 3. Calcular densidades
            double densidadHumeda = pesoHumedo / volumenHueco;
            double densidadSeca = masaSeca / volumenHueco;
            double pesoUnitario = densidadSeca * 9.81; // kN/m³

            // 4. Calcular compactación
            double compactacion = (densidadSeca / densidadMaxima) * 100;

            // Mostrar resultados
            tvMasaTotalHueco.setText(String.format("%.0f g", masaTotalHueco));
            tvMasaSoloHueco.setText(String.format("%.0f g", masaSoloHueco));
            tvVolumenHueco.setText(String.format("%.0f cm³", volumenHueco));
            tvMasaSeca.setText(String.format("%.1f g", masaSeca));
            tvDensidadHumeda.setText(String.format("%.3f g/cm³", densidadHumeda));
            tvDensidadSeca.setText(String.format("%.3f g/cm³", densidadSeca));
            tvPesoUnitario.setText(String.format("%.1f kN/m³", pesoUnitario));
            tvCompactacion.setText(String.format("%.1f%%", compactacion));

            // Mensaje según compactación
            if (compactacion >= 95) {
                tvMensajeCompactacion.setText("✅ Compactación aceptable");
                tvMensajeCompactacion.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            } else if (compactacion >= 90) {
                tvMensajeCompactacion.setText("⚠️ Por debajo del mínimo, verificar");
                tvMensajeCompactacion.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
            } else {
                tvMensajeCompactacion.setText("❌ Rechazado - Requiere recompactación");
                tvMensajeCompactacion.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            }

        } catch (Exception e) {
            Toast.makeText(this, "Error en los cálculos: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private boolean validarCampos() {
        return !etDensidadMaxima.getText().toString().isEmpty() &&
                !etHumedadOptima.getText().toString().isEmpty() &&
                !etPesoInicial.getText().toString().isEmpty() &&
                !etPesoFinal.getText().toString().isEmpty() &&
                !etPesoEmbudoPlaca.getText().toString().isEmpty() &&
                !etDensidadArena.getText().toString().isEmpty() &&
                !etPesoHumedo.getText().toString().isEmpty() &&
                !etHumedadSuelo.getText().toString().isEmpty();
    }
}