package com.example.pm1e10072;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pm1e10072.transacciones.Transacciones;

public class ActivityGuardar extends AppCompatActivity {
    EditText nombre, telefono, nota;
    Spinner listaPaises;
    String pais;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardar);

        Button btnGuardar = findViewById(R.id.btnGuardarContacto);
        listaPaises = findViewById(R.id.spinnerPaises);
        nombre = findViewById(R.id.etNombre);
        telefono = findViewById(R.id.etTelefono);
        nota = findViewById(R.id.etNota);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.array_countries, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listaPaises.setAdapter(adapter);

        pais = listaPaises.getSelectedItem().toString();

        btnGuardar.setOnClickListener(view -> {
            guardarContacto();
        });
    }

    private void guardarContacto() {
        SQLiteConexion connection = new SQLiteConexion(this, Transacciones.nombreBaseDatos, null, 1);
        SQLiteDatabase db = connection.getWritableDatabase();

        ContentValues values = new ContentValues();


        nombre.setError(null);
        telefono.setError(null);
        nota.setError(null);

        String verificarNombre = nombre.getText().toString();
        String verificarTelefono = telefono.getText().toString();
        String verificarNota = nota.getText().toString();

        if (verificarNombre.trim().isEmpty()) {
            nombre.setError("Porfavor, ingrese su nombre completo.");
        } else if (verificarTelefono.trim().isEmpty()) {
            telefono.setError("Porfavor, ingrese su numero de telefono.");
        } else if (verificarNota.trim().isEmpty()) {
            nota.setError("Porfavor, ingrese una nota.");
        } else {
            values.put(Transacciones.pais, pais = listaPaises.getSelectedItem().toString());
            values.put(Transacciones.nombre, nombre.getText().toString());
            values.put(Transacciones.telefono, telefono.getText().toString());
            values.put(Transacciones.nota,  nota.getText().toString());
            Long result = db.insert(Transacciones.tablaContactos, Transacciones.id, values);
            Toast.makeText(getApplicationContext(),"Contacto guardado correctamente"+ result.toString(), Toast.LENGTH_LONG).show();
            db.close();

            LimpiarCamposDeTexto();
        }

    }

    private void LimpiarCamposDeTexto() {
        nombre.setText("");
        telefono.setText("");
        nota.setText("");
    }
}