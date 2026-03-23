package com.example.appevidenciaandroid;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ProctorActivity extends AppCompatActivity {

    // Tipo de ensayo
    private RadioGroup rgTipoProctor;
    private RadioButton rbEstandar, rbModificado;

    // Datos molde
    private EditText etVolumenMolde, etPesoMolde;

    // 4 muestras
    private EditText etPeso1, etPeso2, etPeso3, etPeso4;
    private EditText etHum1, etHum2, etHum3, etHum4;

    // Corrección
    private EditText etPorcentajeGrueso, etGs;

    // Gráfica
    private LineChart chartProctor;

    // Resultados
    private TextView tvDensidadMaxima, tvHumedadOptima;
    private TextView tvTituloCorregido, tvDensidadCorregida, tvHumedadCorregida, tvMensajeProctor;
    private LinearLayout layoutCorregido;

    private Button btnCalcular, btnVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proctor);

        initViews();
        setupListeners();
        configurarValoresPorDefecto();
    }

    private void initViews() {
        rgTipoProctor = findViewById(R.id.rgTipoProctor);
        rbEstandar = findViewById(R.id.rbEstandar);
        rbModificado = findViewById(R.id.rbModificado);

        etVolumenMolde = findViewById(R.id.etVolumenMolde);
        etPesoMolde = findViewById(R.id.etPesoMolde);

        etPeso1 = findViewById(R.id.etPeso1);
        etPeso2 = findViewById(R.id.etPeso2);
        etPeso3 = findViewById(R.id.etPeso3);
        etPeso4 = findViewById(R.id.etPeso4);

        etHum1 = findViewById(R.id.etHum1);
        etHum2 = findViewById(R.id.etHum2);
        etHum3 = findViewById(R.id.etHum3);
        etHum4 = findViewById(R.id.etHum4);

        etPorcentajeGrueso = findViewById(R.id.etPorcentajeGrueso);
        etGs = findViewById(R.id.etGs);

        chartProctor = findViewById(R.id.chartProctor);

        tvDensidadMaxima = findViewById(R.id.tvDensidadMaxima);
        tvHumedadOptima = findViewById(R.id.tvHumedadOptima);
        tvTituloCorregido = findViewById(R.id.tvTituloCorregido);
        tvDensidadCorregida = findViewById(R.id.tvDensidadCorregida);
        tvHumedadCorregida = findViewById(R.id.tvHumedadCorregida);
        tvMensajeProctor = findViewById(R.id.tvMensajeProctor);
        layoutCorregido = findViewById(R.id.layoutCorregido);

        btnCalcular = findViewById(R.id.btnCalcularProctor);
        btnVolver = findViewById(R.id.btnVolverProctor);
    }

    private void setupListeners() {
        btnCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcularProctor();
            }
        });

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rgTipoProctor.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbEstandar) {
                    etVolumenMolde.setText("944");
                    etPesoMolde.setText("2000");
                } else {
                    etVolumenMolde.setText("944");
                    etPesoMolde.setText("2000");
                }
            }
        });
    }

    private void configurarValoresPorDefecto() {
        rbEstandar.setChecked(true);
        etVolumenMolde.setText("944");
        etPesoMolde.setText("2000");
    }

    private void calcularProctor() {
        try {
            // Validar datos básicos
            if (!validarCampos()) {
                Toast.makeText(this, "Complete los datos del molde y las 4 muestras", Toast.LENGTH_SHORT).show();
                return;
            }

            double volumen = Double.parseDouble(etVolumenMolde.getText().toString());
            double pesoMolde = Double.parseDouble(etPesoMolde.getText().toString());

            // Arrays para almacenar datos
            ArrayList<Muestra> muestras = new ArrayList<>();

            // Procesar cada muestra
            procesarMuestra(muestras, etPeso1, etHum1, pesoMolde, volumen, 1);
            procesarMuestra(muestras, etPeso2, etHum2, pesoMolde, volumen, 2);
            procesarMuestra(muestras, etPeso3, etHum3, pesoMolde, volumen, 3);
            procesarMuestra(muestras, etPeso4, etHum4, pesoMolde, volumen, 4);

            // Ordenar por humedad para la curva
            Collections.sort(muestras, new Comparator<Muestra>() {
                @Override
                public int compare(Muestra m1, Muestra m2) {
                    return Double.compare(m1.humedad, m2.humedad);
                }
            });

            // Calcular óptimo mediante interpolación parabólica
            double[] optimoCalculado = calcularOptimoParabolico(muestras);
            double densidadOptima = optimoCalculado[0];
            double humedadOptima = optimoCalculado[1];

            // Mostrar resultados sin corregir
            tvDensidadMaxima.setText(String.format("%.3f g/cm³", densidadOptima));
            tvHumedadOptima.setText(String.format("%.1f %%", humedadOptima));

            // Crear Muestra temporal para la gráfica
            Muestra optimoGrafico = new Muestra(0, humedadOptima, densidadOptima);

            // Dibujar gráfica con curva polinómica
            dibujarGraficaPolinomica(muestras, optimoGrafico);

            // --- LÓGICA DE CORRECCIÓN SEGÚN ASTM D4718 ---
            boolean tieneCorreccion = !etPorcentajeGrueso.getText().toString().isEmpty() &&
                    !etGs.getText().toString().isEmpty();

            if (tieneCorreccion) {
                double R = Double.parseDouble(etPorcentajeGrueso.getText().toString());
                double Gs = Double.parseDouble(etGs.getText().toString());

                if (R <= 0 || R > 100) {
                    tvMensajeProctor.setText("❌ % gruesos debe estar entre 1 y 100");
                    tvMensajeProctor.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                    layoutCorregido.setVisibility(View.GONE);
                    tvTituloCorregido.setVisibility(View.GONE);
                    return;
                }

                if (Gs <= 0 || Gs > 5) {
                    tvMensajeProctor.setText("❌ Gs debe estar entre 1.0 y 5.0");
                    tvMensajeProctor.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                    layoutCorregido.setVisibility(View.GONE);
                    tvTituloCorregido.setVisibility(View.GONE);
                    return;
                }

                double gammaG = Gs * 1.0;
                double gammaCorregida = (100 * densidadOptima * gammaG) /
                        ((100 - R) * gammaG + (R * densidadOptima));
                double wCorregida = (humedadOptima * (100 - R) + (0.5 * R)) / 100;

                tvDensidadCorregida.setText(String.format("%.3f g/cm³", gammaCorregida));
                tvHumedadCorregida.setText(String.format("%.1f %%", wCorregida));

                if (R < 5) {
                    tvMensajeProctor.setText("ℹ️ % gruesos < 5%: No requiere corrección según ASTM. Resultado referencial.");
                    tvMensajeProctor.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
                    layoutCorregido.setVisibility(View.VISIBLE);
                    tvTituloCorregido.setVisibility(View.VISIBLE);
                } else if (R <= 40) {
                    tvMensajeProctor.setText("✅ Corrección válida según ASTM D4718 (Rango 5-40%)");
                    tvMensajeProctor.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                    layoutCorregido.setVisibility(View.VISIBLE);
                    tvTituloCorregido.setVisibility(View.VISIBLE);
                } else {
                    tvMensajeProctor.setText("⚠️ % gruesos > 40%: Excede límite ASTM. Resultado aproximado");
                    tvMensajeProctor.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
                    layoutCorregido.setVisibility(View.VISIBLE);
                    tvTituloCorregido.setVisibility(View.VISIBLE);
                }
            } else {
                layoutCorregido.setVisibility(View.GONE);
                tvTituloCorregido.setVisibility(View.GONE);
                tvMensajeProctor.setText("");
            }

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Error: Use punto (.) como separador decimal", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void procesarMuestra(ArrayList<Muestra> muestras, EditText etPeso, EditText etHum,
                                 double pesoMolde, double volumen, int num) {
        if (!etPeso.getText().toString().isEmpty() && !etHum.getText().toString().isEmpty()) {
            double pesoTotal = Double.parseDouble(etPeso.getText().toString());
            double humedad = Double.parseDouble(etHum.getText().toString());

            double pesoSueloHumedo = pesoTotal - pesoMolde;
            double densidadHumeda = pesoSueloHumedo / volumen;
            double densidadSeca = densidadHumeda / (1 + (humedad / 100));

            muestras.add(new Muestra(num, humedad, densidadSeca));
        }
    }

    private double[] calcularOptimoParabolico(ArrayList<Muestra> muestras) {
        Collections.sort(muestras, new Comparator<Muestra>() {
            @Override
            public int compare(Muestra m1, Muestra m2) {
                return Double.compare(m1.humedad, m2.humedad);
            }
        });

        int maxIndex = 0;
        double maxDensidad = muestras.get(0).densidadSeca;
        for (int i = 1; i < muestras.size(); i++) {
            if (muestras.get(i).densidadSeca > maxDensidad) {
                maxDensidad = muestras.get(i).densidadSeca;
                maxIndex = i;
            }
        }

        if (maxIndex == 0 || maxIndex == muestras.size() - 1) {
            return new double[]{maxDensidad, muestras.get(maxIndex).humedad};
        }

        Muestra izquierdo = muestras.get(maxIndex - 1);
        Muestra centro = muestras.get(maxIndex);
        Muestra derecho = muestras.get(maxIndex + 1);

        double x1 = izquierdo.humedad;
        double y1 = izquierdo.densidadSeca;
        double x2 = centro.humedad;
        double y2 = centro.densidadSeca;
        double x3 = derecho.humedad;
        double y3 = derecho.densidadSeca;

        double denominador = (x1 - x2) * (x1 - x3) * (x2 - x3);
        double a = (x3 * (y2 - y1) + x2 * (y1 - y3) + x1 * (y3 - y2)) / denominador;
        double b = (x3 * x3 * (y1 - y2) + x2 * x2 * (y3 - y1) + x1 * x1 * (y2 - y3)) / denominador;
        double c = (x2 * x3 * (x2 - x3) * y1 + x3 * x1 * (x3 - x1) * y2 + x1 * x2 * (x1 - x2) * y3) / denominador;

        double humedadOptima = -b / (2 * a);
        double densidadOptima = a * humedadOptima * humedadOptima + b * humedadOptima + c;

        if (humedadOptima < x1) humedadOptima = x1;
        if (humedadOptima > x3) humedadOptima = x3;

        return new double[]{densidadOptima, humedadOptima};
    }

    private void dibujarGraficaPolinomica(ArrayList<Muestra> muestras, Muestra optimo) {
        // Ordenar muestras por humedad
        Collections.sort(muestras, new Comparator<Muestra>() {
            @Override
            public int compare(Muestra m1, Muestra m2) {
                return Double.compare(m1.humedad, m2.humedad);
            }
        });

        // Calcular regresión polinómica de grado 2
        double[] coeficientes = calcularRegresionPolinomica(muestras);
        double a = coeficientes[0]; // coeficiente cuadrático
        double b = coeficientes[1]; // coeficiente lineal
        double c = coeficientes[2]; // término independiente

        // Crear curva polinómica con muchos puntos
        ArrayList<Entry> curvaPolinomica = new ArrayList<>();

        double minHumedad = muestras.get(0).humedad;
        double maxHumedad = muestras.get(muestras.size() - 1).humedad;
        int puntosTotales = 200;

        for (int i = 0; i <= puntosTotales; i++) {
            double humedad = minHumedad + (maxHumedad - minHumedad) * i / puntosTotales;
            double densidad = a * humedad * humedad + b * humedad + c;
            curvaPolinomica.add(new Entry((float) humedad, (float) densidad));
        }

        // Dataset de la curva polinómica
        LineDataSet curvaDataSet = new LineDataSet(curvaPolinomica, "Curva Proctor");
        curvaDataSet.setColor(Color.rgb(230, 126, 34));
        curvaDataSet.setLineWidth(3f);
        curvaDataSet.setCircleRadius(0f);
        curvaDataSet.setDrawValues(false);
        curvaDataSet.setDrawCircles(false);
        curvaDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        // Puntos reales del ensayo
        ArrayList<Entry> puntosReales = new ArrayList<>();
        for (Muestra m : muestras) {
            puntosReales.add(new Entry((float) m.humedad, (float) m.densidadSeca));
        }
        LineDataSet puntosDataSet = new LineDataSet(puntosReales, "Puntos de ensayo");
        puntosDataSet.setColor(Color.rgb(52, 152, 219));
        puntosDataSet.setCircleColor(Color.rgb(52, 152, 219));
        puntosDataSet.setCircleRadius(7f);
        puntosDataSet.setDrawValues(false);
        puntosDataSet.setDrawCircles(true);

        // Punto óptimo
        ArrayList<Entry> puntoOptimo = new ArrayList<>();
        puntoOptimo.add(new Entry((float) optimo.humedad, (float) optimo.densidadSeca));
        LineDataSet puntoSet = new LineDataSet(puntoOptimo, "Óptimo");
        puntoSet.setColor(Color.RED);
        puntoSet.setCircleColor(Color.RED);
        puntoSet.setCircleRadius(10f);
        puntoSet.setDrawValues(true);
        puntoSet.setValueTextSize(12f);
        puntoSet.setValueTextColor(Color.RED);
        puntoSet.setDrawCircles(true);
        puntoSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.format("Óptimo\n%.1f%%\n%.3f", optimo.humedad, optimo.densidadSeca);
            }
        });

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(curvaDataSet);
        dataSets.add(puntosDataSet);
        dataSets.add(puntoSet);

        LineData lineData = new LineData(dataSets);
        chartProctor.setData(lineData);

        // Configurar ejes
        chartProctor.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.format("%.1f%%", value);
            }
        });
        chartProctor.getXAxis().setGranularity(0.5f);
        chartProctor.getXAxis().setPosition(chartProctor.getXAxis().getPosition().BOTTOM);
        chartProctor.getXAxis().setTextSize(11f);
        chartProctor.getXAxis().setLabelCount(8, true);

        chartProctor.getAxisLeft().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.format("%.3f", value);
            }
        });
        chartProctor.getAxisLeft().setTextSize(11f);

        chartProctor.getAxisRight().setEnabled(false);
        chartProctor.getDescription().setEnabled(false);
        chartProctor.getLegend().setTextSize(11f);
        chartProctor.getLegend().setFormSize(8f);
        chartProctor.invalidate();
    }

    private double[] calcularRegresionPolinomica(ArrayList<Muestra> muestras) {
        int n = muestras.size();

        double sumX = 0, sumX2 = 0, sumX3 = 0, sumX4 = 0;
        double sumY = 0, sumXY = 0, sumX2Y = 0;

        for (Muestra m : muestras) {
            double x = m.humedad;
            double y = m.densidadSeca;

            sumX += x;
            sumX2 += x * x;
            sumX3 += x * x * x;
            sumX4 += x * x * x * x;
            sumY += y;
            sumXY += x * y;
            sumX2Y += x * x * y;
        }

        // Resolver sistema para y = a*x² + b*x + c
        double[][] A = {
                {n, sumX, sumX2},
                {sumX, sumX2, sumX3},
                {sumX2, sumX3, sumX4}
        };
        double[] B = {sumY, sumXY, sumX2Y};

        double[] coeficientes = resolverSistema(A, B);

        // coeficientes[0] = c, coeficientes[1] = b, coeficientes[2] = a
        return new double[]{coeficientes[2], coeficientes[1], coeficientes[0]}; // {a, b, c}
    }

    private double[] resolverSistema(double[][] A, double[] B) {
        int n = B.length;
        double[][] a = new double[n][n+1];

        // Crear matriz aumentada
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                a[i][j] = A[i][j];
            }
            a[i][n] = B[i];
        }

        // Eliminación gaussiana
        for (int i = 0; i < n; i++) {
            // Pivoteo
            int max = i;
            for (int k = i+1; k < n; k++) {
                if (Math.abs(a[k][i]) > Math.abs(a[max][i])) {
                    max = k;
                }
            }
            double[] temp = a[i];
            a[i] = a[max];
            a[max] = temp;

            // Normalizar
            double pivote = a[i][i];
            for (int k = i; k <= n; k++) {
                a[i][k] /= pivote;
            }

            // Eliminar
            for (int k = 0; k < n; k++) {
                if (k != i && a[k][i] != 0) {
                    double factor = a[k][i];
                    for (int j = i; j <= n; j++) {
                        a[k][j] -= factor * a[i][j];
                    }
                }
            }
        }

        double[] resultado = new double[n];
        for (int i = 0; i < n; i++) {
            resultado[i] = a[i][n];
        }
        return resultado;
    }

    private boolean validarCampos() {
        return !etVolumenMolde.getText().toString().isEmpty() &&
                !etPesoMolde.getText().toString().isEmpty() &&
                !etPeso1.getText().toString().isEmpty() &&
                !etPeso2.getText().toString().isEmpty() &&
                !etPeso3.getText().toString().isEmpty() &&
                !etPeso4.getText().toString().isEmpty() &&
                !etHum1.getText().toString().isEmpty() &&
                !etHum2.getText().toString().isEmpty() &&
                !etHum3.getText().toString().isEmpty() &&
                !etHum4.getText().toString().isEmpty();
    }

    // Clase interna para muestras
    private class Muestra {
        int numero;
        double humedad;
        double densidadSeca;

        Muestra(int numero, double humedad, double densidadSeca) {
            this.numero = numero;
            this.humedad = humedad;
            this.densidadSeca = densidadSeca;
        }
    }
}