package com.example.pm1e10072;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityGuardar extends AppCompatActivity {
    EditText nombre, telefono, nota;
    Spinner listaPaises;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardar);

        listaPaises = findViewById(R.id.spinnerPaises);
        nombre = findViewById(R.id.etNombre);
        telefono = findViewById(R.id.etTelefono);
        nota = findViewById(R.id.etNota);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.array_countries, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        listaPaises.setAdapter(adapter);
    }
}