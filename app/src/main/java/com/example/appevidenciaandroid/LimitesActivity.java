package com.example.appevidenciaandroid;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LimitesActivity extends AppCompatActivity {

    // LL - 3 puntos
    private EditText etGolpes1, etGolpes2, etGolpes3;
    private EditText etCapsulaLL1, etCapsulaLL2, etCapsulaLL3;
    private EditText etHumedoLL1, etHumedoLL2, etHumedoLL3;
    private EditText etSecoLL1, etSecoLL2, etSecoLL3;
    private EditText etTaraLL1, etTaraLL2, etTaraLL3;
    private TextView tvHumedadLL1, tvHumedadLL2, tvHumedadLL3;

    // LP - 2 puntos
    private EditText etCapsulaLP1, etCapsulaLP2;
    private EditText etHumedoLP1, etHumedoLP2;
    private EditText etSecoLP1, etSecoLP2;
    private EditText etTaraLP1, etTaraLP2;
    private TextView tvHumedadLP1, tvHumedadLP2;

    // Resultados
    private TextView tvLL, tvLP, tvIP;
    private TextView tvM, tvB, tvR2, tvValidacion;

    private Button btnCalcularHumedadesLL, btnCalcularHumedadesLP, btnCalcularLimites, btnVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_limites);

        initViews();
        setupListeners();
    }

    private void initViews() {
        // LL
        etGolpes1 = findViewById(R.id.etGolpes1);
        etGolpes2 = findViewById(R.id.etGolpes2);
        etGolpes3 = findViewById(R.id.etGolpes3);

        etCapsulaLL1 = findViewById(R.id.etCapsulaLL1);
        etCapsulaLL2 = findViewById(R.id.etCapsulaLL2);
        etCapsulaLL3 = findViewById(R.id.etCapsulaLL3);

        etHumedoLL1 = findViewById(R.id.etHumedoLL1);
        etHumedoLL2 = findViewById(R.id.etHumedoLL2);
        etHumedoLL3 = findViewById(R.id.etHumedoLL3);

        etSecoLL1 = findViewById(R.id.etSecoLL1);
        etSecoLL2 = findViewById(R.id.etSecoLL2);
        etSecoLL3 = findViewById(R.id.etSecoLL3);

        etTaraLL1 = findViewById(R.id.etTaraLL1);
        etTaraLL2 = findViewById(R.id.etTaraLL2);
        etTaraLL3 = findViewById(R.id.etTaraLL3);

        tvHumedadLL1 = findViewById(R.id.tvHumedadLL1);
        tvHumedadLL2 = findViewById(R.id.tvHumedadLL2);
        tvHumedadLL3 = findViewById(R.id.tvHumedadLL3);

        // LP
        etCapsulaLP1 = findViewById(R.id.etCapsulaLP1);
        etCapsulaLP2 = findViewById(R.id.etCapsulaLP2);

        etHumedoLP1 = findViewById(R.id.etHumedoLP1);
        etHumedoLP2 = findViewById(R.id.etHumedoLP2);

        etSecoLP1 = findViewById(R.id.etSecoLP1);
        etSecoLP2 = findViewById(R.id.etSecoLP2);

        etTaraLP1 = findViewById(R.id.etTaraLP1);
        etTaraLP2 = findViewById(R.id.etTaraLP2);

        tvHumedadLP1 = findViewById(R.id.tvHumedadLP1);
        tvHumedadLP2 = findViewById(R.id.tvHumedadLP2);

        // Resultados
        tvLL = findViewById(R.id.tvLL);
        tvLP = findViewById(R.id.tvLP);
        tvIP = findViewById(R.id.tvIP);

        // Parámetros curva
        tvM = findViewById(R.id.tvM);
        tvB = findViewById(R.id.tvB);
        tvR2 = findViewById(R.id.tvR2);
        tvValidacion = findViewById(R.id.tvValidacion);

        // Botones
        btnCalcularHumedadesLL = findViewById(R.id.btnCalcularHumedadesLL);
        btnCalcularHumedadesLP = findViewById(R.id.btnCalcularHumedadesLP);
        btnCalcularLimites = findViewById(R.id.btnCalcularLimites);
        btnVolver = findViewById(R.id.btnVolverLimites);
    }

    private void setupListeners() {
        btnCalcularHumedadesLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcularHumedadesLL();
            }
        });

        btnCalcularHumedadesLP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcularHumedadesLP();
            }
        });

        btnCalcularLimites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcularLimites();
            }
        });

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void calcularHumedadesLL() {
        try {
            if (checkLLData()) {
                double h1 = calcularHumedad(etHumedoLL1, etSecoLL1, etTaraLL1);
                double h2 = calcularHumedad(etHumedoLL2, etSecoLL2, etTaraLL2);
                double h3 = calcularHumedad(etHumedoLL3, etSecoLL3, etTaraLL3);

                tvHumedadLL1.setText(String.format("%.0f", h1));
                tvHumedadLL2.setText(String.format("%.0f", h2));
                tvHumedadLL3.setText(String.format("%.0f", h3));
            } else {
                Toast.makeText(this, "Complete todos los datos de Límite Líquido", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error en datos de Límite Líquido", Toast.LENGTH_SHORT).show();
        }
    }

    private void calcularHumedadesLP() {
        try {
            if (checkLPData()) {
                double h1 = calcularHumedad(etHumedoLP1, etSecoLP1, etTaraLP1);
                double h2 = calcularHumedad(etHumedoLP2, etSecoLP2, etTaraLP2);

                tvHumedadLP1.setText(String.format("%.0f", h1));
                tvHumedadLP2.setText(String.format("%.0f", h2));
            } else {
                Toast.makeText(this, "Complete todos los datos de Límite Plástico", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error en datos de Límite Plástico", Toast.LENGTH_SHORT).show();
        }
    }

    private double calcularHumedad(EditText etHumedo, EditText etSeco, EditText etTara) {
        double ph = Double.parseDouble(etHumedo.getText().toString());
        double ps = Double.parseDouble(etSeco.getText().toString());
        double pt = Double.parseDouble(etTara.getText().toString());

        double pesoAgua = ph - ps;
        double pesoSueloSeco = ps - pt;

        if (pesoSueloSeco == 0) return 0;
        return (pesoAgua / pesoSueloSeco) * 100;
    }

    private void calcularLimites() {
        try {
            // Obtener humedades LL
            double h1 = Double.parseDouble(tvHumedadLL1.getText().toString());
            double h2 = Double.parseDouble(tvHumedadLL2.getText().toString());
            double h3 = Double.parseDouble(tvHumedadLL3.getText().toString());

            double g1 = Double.parseDouble(etGolpes1.getText().toString());
            double g2 = Double.parseDouble(etGolpes2.getText().toString());
            double g3 = Double.parseDouble(etGolpes3.getText().toString());

            // Calcular regresión logarítmica con R²
            double[] resultados = calcularRegresionLogaritmicaCompleta(
                    new double[]{g1, g2, g3},
                    new double[]{h1, h2, h3}
            );

            double m = resultados[0];
            double b = resultados[1];
            double r2 = resultados[2];

            // Mostrar parámetros de la curva
            tvM.setText(String.format("%.2f", m));
            tvB.setText(String.format("%.2f", b));
            tvR2.setText(String.format("%.4f", r2));

            // Validar R²
            if (r2 < 0.95) {
                tvValidacion.setText("⚠️ R² < 0.95: La curva no es confiable. Revise los datos.");
                tvValidacion.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            } else {
                tvValidacion.setText("✅ R² ≥ 0.95: Curva válida según norma.");
                tvValidacion.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            }

            // LL a 25 golpes
            double ll = m * Math.log(25) + b;
            int llEntero = (int) Math.round(ll);

            // Obtener humedades LP
            double lp1 = Double.parseDouble(tvHumedadLP1.getText().toString());
            double lp2 = Double.parseDouble(tvHumedadLP2.getText().toString());
            int lpEntero = (int) Math.round((lp1 + lp2) / 2);

            // IP
            int ipEntero = llEntero - lpEntero;

            // Mostrar resultados
            tvLL.setText(String.valueOf(llEntero));
            tvLP.setText(String.valueOf(lpEntero));
            tvIP.setText(String.valueOf(ipEntero));

        } catch (Exception e) {
            Toast.makeText(this, "Error al calcular límites. Verifique datos.", Toast.LENGTH_LONG).show();
        }
    }

    private double[] calcularRegresionLogaritmicaCompleta(double[] x, double[] y) {
        int n = x.length;
        double[] lnX = new double[n];

        for (int i = 0; i < n; i++) {
            lnX[i] = Math.log(x[i]);
        }

        double sumLnX = 0, sumY = 0, sumLnXY = 0, sumLnX2 = 0;

        for (int i = 0; i < n; i++) {
            sumLnX += lnX[i];
            sumY += y[i];
            sumLnXY += lnX[i] * y[i];
            sumLnX2 += lnX[i] * lnX[i];
        }

        double m = (n * sumLnXY - sumLnX * sumY) / (n * sumLnX2 - sumLnX * sumLnX);
        double b = (sumY - m * sumLnX) / n;

        // Calcular R²
        double sumY2 = 0, sumResid = 0;
        double yProm = sumY / n;

        for (int i = 0; i < n; i++) {
            double yEst = m * lnX[i] + b;
            sumY2 += Math.pow(y[i] - yProm, 2);
            sumResid += Math.pow(y[i] - yEst, 2);
        }

        double r2 = 1 - (sumResid / sumY2);

        return new double[]{m, b, r2};
    }

    private boolean checkLLData() {
        return !etGolpes1.getText().toString().isEmpty() &&
                !etGolpes2.getText().toString().isEmpty() &&
                !etGolpes3.getText().toString().isEmpty() &&
                !etHumedoLL1.getText().toString().isEmpty() &&
                !etHumedoLL2.getText().toString().isEmpty() &&
                !etHumedoLL3.getText().toString().isEmpty() &&
                !etSecoLL1.getText().toString().isEmpty() &&
                !etSecoLL2.getText().toString().isEmpty() &&
                !etSecoLL3.getText().toString().isEmpty() &&
                !etTaraLL1.getText().toString().isEmpty() &&
                !etTaraLL2.getText().toString().isEmpty() &&
                !etTaraLL3.getText().toString().isEmpty();
    }

    private boolean checkLPData() {
        return !etHumedoLP1.getText().toString().isEmpty() &&
                !etHumedoLP2.getText().toString().isEmpty() &&
                !etSecoLP1.getText().toString().isEmpty() &&
                !etSecoLP2.getText().toString().isEmpty() &&
                !etTaraLP1.getText().toString().isEmpty() &&
                !etTaraLP2.getText().toString().isEmpty();
    }
}