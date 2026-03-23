package com.example.appevidenciaandroid;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class HumedadActivity extends AppCompatActivity {

    private EditText etTara, etHumedo, etSeco;
    private TextView tvResultado;
    private Button btnCalcular, btnVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_humedad);

        // Inicializar vistas
        etTara = findViewById(R.id.etTara);
        etHumedo = findViewById(R.id.etHumedo);
        etSeco = findViewById(R.id.etSeco);
        tvResultado = findViewById(R.id.tvResultado);
        btnCalcular = findViewById(R.id.btnCalcular);
        btnVolver = findViewById(R.id.btnVolver);

        // Configurar botón calcular
        btnCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcularHumedad();
            }
        });

        // Configurar botón volver
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Cierra esta activity y vuelve a MainActivity
            }
        });
    }

    private void calcularHumedad() {
        // Validar que los campos no estén vacíos
        if (etTara.getText().toString().isEmpty() ||
                etHumedo.getText().toString().isEmpty() ||
                etSeco.getText().toString().isEmpty()) {

            Toast.makeText(this, "Por favor ingrese todos los valores", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Obtener valores
            double tara = Double.parseDouble(etTara.getText().toString());
            double humedo = Double.parseDouble(etHumedo.getText().toString());
            double seco = Double.parseDouble(etSeco.getText().toString());

            // Calcular según norma ASTM D2216
            double pesoAgua = humedo - seco;
            double pesoSueloSeco = seco - tara;

            // Validar división por cero
            if (pesoSueloSeco == 0) {
                Toast.makeText(this, "Error: Peso de suelo seco no puede ser cero", Toast.LENGTH_SHORT).show();
                return;
            }

            double humedad = (pesoAgua / pesoSueloSeco) * 100;

            // Mostrar resultado con 2 decimales
            String resultado = String.format("Humedad = %.2f %%", humedad);
            tvResultado.setText(resultado);

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Error en los números ingresados", Toast.LENGTH_SHORT).show();
        }
    }
}