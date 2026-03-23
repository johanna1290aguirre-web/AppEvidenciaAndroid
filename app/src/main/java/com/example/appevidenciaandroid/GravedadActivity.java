package com.example.appevidenciaandroid;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class GravedadActivity extends AppCompatActivity {

    private EditText etA1, etB1, etC1, etA2, etB2, etC2;
    private TextView tvGsOD1, tvGsSSD1, tvGsAparente1, tvAbs1;
    private TextView tvGsOD2, tvGsSSD2, tvGsAparente2, tvAbs2;
    private TextView tvGsODProm, tvGsSSDProm, tvGsAparenteProm, tvAbsProm;
    private Button btnCalcular, btnVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gravedad);

        // Inicializar vistas
        etA1 = findViewById(R.id.etA1);
        etB1 = findViewById(R.id.etB1);
        etC1 = findViewById(R.id.etC1);
        etA2 = findViewById(R.id.etA2);
        etB2 = findViewById(R.id.etB2);
        etC2 = findViewById(R.id.etC2);

        tvGsOD1 = findViewById(R.id.tvGsOD1);
        tvGsSSD1 = findViewById(R.id.tvGsSSD1);
        tvGsAparente1 = findViewById(R.id.tvGsAparente1);
        tvAbs1 = findViewById(R.id.tvAbs1);

        tvGsOD2 = findViewById(R.id.tvGsOD2);
        tvGsSSD2 = findViewById(R.id.tvGsSSD2);
        tvGsAparente2 = findViewById(R.id.tvGsAparente2);
        tvAbs2 = findViewById(R.id.tvAbs2);

        tvGsODProm = findViewById(R.id.tvGsODProm);
        tvGsSSDProm = findViewById(R.id.tvGsSSDProm);
        tvGsAparenteProm = findViewById(R.id.tvGsAparenteProm);
        tvAbsProm = findViewById(R.id.tvAbsProm);

        btnCalcular = findViewById(R.id.btnCalcularGs);
        btnVolver = findViewById(R.id.btnVolverGs);

        btnCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcularGravedadEspecifica();
            }
        });

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void calcularGravedadEspecifica() {
        // Variables para promedios
        double sumGsOD = 0, sumGsSSD = 0, sumGsAparente = 0, sumAbs = 0;
        int contador = 0;

        // Verificar si hay datos en Determinación 1
        if (!etA1.getText().toString().isEmpty() &&
                !etB1.getText().toString().isEmpty() &&
                !etC1.getText().toString().isEmpty()) {

            try {
                double A1 = Double.parseDouble(etA1.getText().toString());
                double B1 = Double.parseDouble(etB1.getText().toString());
                double C1 = Double.parseDouble(etC1.getText().toString());

                Resultados res1 = calcularGs(A1, B1, C1);

                tvGsOD1.setText(String.format("%.3f", res1.gsOD));
                tvGsSSD1.setText(String.format("%.3f", res1.gsSSD));
                tvGsAparente1.setText(String.format("%.3f", res1.gsAparente));
                tvAbs1.setText(String.format("%.2f", res1.absorcion));

                sumGsOD += res1.gsOD;
                sumGsSSD += res1.gsSSD;
                sumGsAparente += res1.gsAparente;
                sumAbs += res1.absorcion;
                contador++;

            } catch (NumberFormatException e) {
                Toast.makeText(this, "Error en datos de Ensayo 1", Toast.LENGTH_SHORT).show();
            }
        }

        // Verificar si hay datos en Determinación 2
        if (!etA2.getText().toString().isEmpty() &&
                !etB2.getText().toString().isEmpty() &&
                !etC2.getText().toString().isEmpty()) {

            try {
                double A2 = Double.parseDouble(etA2.getText().toString());
                double B2 = Double.parseDouble(etB2.getText().toString());
                double C2 = Double.parseDouble(etC2.getText().toString());

                Resultados res2 = calcularGs(A2, B2, C2);

                tvGsOD2.setText(String.format("%.3f", res2.gsOD));
                tvGsSSD2.setText(String.format("%.3f", res2.gsSSD));
                tvGsAparente2.setText(String.format("%.3f", res2.gsAparente));
                tvAbs2.setText(String.format("%.2f", res2.absorcion));

                sumGsOD += res2.gsOD;
                sumGsSSD += res2.gsSSD;
                sumGsAparente += res2.gsAparente;
                sumAbs += res2.absorcion;
                contador++;

            } catch (NumberFormatException e) {
                Toast.makeText(this, "Error en datos de Ensayo 2", Toast.LENGTH_SHORT).show();
            }
        }

        // Calcular promedios
        if (contador == 0) {
            Toast.makeText(this, "Ingrese al menos una determinación", Toast.LENGTH_SHORT).show();
            return;
        }

        double promGsOD = sumGsOD / contador;
        double promGsSSD = sumGsSSD / contador;
        double promGsAparente = sumGsAparente / contador;
        double promAbs = sumAbs / contador;

        tvGsODProm.setText(String.format("%.3f", promGsOD));
        tvGsSSDProm.setText(String.format("%.3f", promGsSSD));
        tvGsAparenteProm.setText(String.format("%.3f", promGsAparente));
        tvAbsProm.setText(String.format("%.2f", promAbs));
    }

    private Resultados calcularGs(double A, double B, double C) {
        Resultados res = new Resultados();

        // Validar divisiones
        if ((B - C) == 0 || (A - C) == 0) {
            throw new IllegalArgumentException("División por cero");
        }

        res.gsOD = A / (B - C);
        res.gsSSD = B / (B - C);
        res.gsAparente = A / (A - C);
        res.absorcion = ((B - A) / A) * 100;

        return res;
    }

    // Clase interna para resultados
    private class Resultados {
        double gsOD;
        double gsSSD;
        double gsAparente;
        double absorcion;
    }
}