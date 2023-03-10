package com.example.pm1e10072;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnAgregarContacto = findViewById(R.id.btnAgregarContacto);
        Button btnListaContactos = findViewById(R.id.btnListaContactos);
        btnAgregarContacto.setOnClickListener(v -> {
            Intent intentAgregarContacto = new Intent(getApplicationContext(), ActivityGuardar.class);
            startActivity(intentAgregarContacto);
        });

        btnListaContactos.setOnClickListener(view -> {
            Intent intentListaContactos = new Intent(getApplicationContext(), ActivityLista.class);
            startActivity(intentListaContactos);

        });
    }
}