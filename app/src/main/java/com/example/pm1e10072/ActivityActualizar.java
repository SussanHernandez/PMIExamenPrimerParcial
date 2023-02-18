package com.example.pm1e10072;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pm1e10072.transacciones.Transacciones;

public class ActivityActualizar extends AppCompatActivity {

    SQLiteConexion conexion;
    Spinner spinnerPais;
    String pais;
    Integer idRecibido;
    EditText nombreActuailizar, telefonoActualizar, notaActualizar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar);

        conexion = new SQLiteConexion(this, Transacciones.nombreBaseDatos, null, 1);

        spinnerPais = findViewById(R.id.spinnerPais);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.array_countries, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPais.setAdapter(adapter);

        nombreActuailizar = findViewById(R.id.etActualizarNombre);
        telefonoActualizar = findViewById(R.id.etActualizarTelefono);
        notaActualizar = findViewById(R.id.etActualizarNota);

        Bundle valoresRecuperadosDeBundle = this.getIntent().getExtras();
        idRecibido = valoresRecuperadosDeBundle.getInt("id");
        nombreActuailizar.setText(valoresRecuperadosDeBundle.getString("nombre"));
        telefonoActualizar.setText(valoresRecuperadosDeBundle.getString("telefono"));
        notaActualizar.setText(valoresRecuperadosDeBundle.getString("nota"));

        Button btnRealizarActualizacion = findViewById(R.id.btnConfirmarActualizacion);
        btnRealizarActualizacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actualizarContacto();
                finish();
            }
        });

    }

    private void actualizarContacto() {
        SQLiteDatabase db = conexion.getWritableDatabase();
        String[] params = {String.valueOf(idRecibido)};

        ContentValues values = new ContentValues();
        pais = spinnerPais.getSelectedItem().toString();
        values.put(Transacciones.pais, pais);
        values.put(Transacciones.nombre, nombreActuailizar.getText().toString());
        values.put(Transacciones.telefono, telefonoActualizar.getText().toString());
        values.put(Transacciones.nota, notaActualizar.getText().toString());
        db.update(Transacciones.tablaContactos, values, Transacciones.id + "=?", params);
        Toast.makeText(getApplicationContext(), "Contacto actualizado", Toast.LENGTH_SHORT).show();

    }
}