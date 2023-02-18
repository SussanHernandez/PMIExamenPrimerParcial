package com.example.pm1e10072;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.pm1e10072.tablas.Contactos;
import com.example.pm1e10072.transacciones.Transacciones;

import java.util.ArrayList;

public class ActivityLista extends AppCompatActivity {

    SQLiteConexion conexion;
    ListView listViewListaContactos;
    ArrayList<Contactos> arrayListContactos;
    ArrayList<String> arrayListContactosToString;
    int posicionDeFilaEnLista;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        conexion = new SQLiteConexion(this, Transacciones.nombreBaseDatos, null, 1);
        listViewListaContactos = (ListView) findViewById(R.id.listviewListaContactos);

        obtenerListaContactos();

        arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, arrayListContactosToString);
        listViewListaContactos.setAdapter(arrayAdapter);

        listViewListaContactos.setOnItemClickListener((adapterView, view, i, l) -> {
            posicionDeFilaEnLista = arrayListContactos.get(i).getId();
            String positionString = String.valueOf(posicionDeFilaEnLista);
            Toast.makeText(getApplicationContext(), positionString, Toast.LENGTH_SHORT).show();
            arrayAdapter.notifyDataSetChanged();
        });

        Button btnBorrar = findViewById(R.id.btnBorrar);
        btnBorrar.setOnClickListener(view -> {
            BorrarContacto();
            arrayAdapter.notifyDataSetChanged();
            listViewListaContactos.setAdapter(arrayAdapter);
        });
    }

    private void BorrarContacto() {
        SQLiteDatabase db = conexion.getWritableDatabase();
        String[] parameterId = {String.valueOf(posicionDeFilaEnLista)};

        db.delete(Transacciones.tablaContactos, Transacciones.id + "=?", parameterId);
        Toast.makeText(getApplicationContext(), "Contacto Eliminado", Toast.LENGTH_SHORT).show();
        arrayAdapter.notifyDataSetChanged();
    }

    private void obtenerListaContactos() {
        SQLiteDatabase db = conexion.getReadableDatabase();
        Contactos listaObjetosTipoContactos;
        arrayListContactos = new ArrayList<>();

        Cursor cursorConsultaContactos = db.rawQuery("SELECT * FROM " + Transacciones.tablaContactos, null);
        while (cursorConsultaContactos.moveToNext()) {
            listaObjetosTipoContactos = new Contactos();
            listaObjetosTipoContactos.setId(cursorConsultaContactos.getInt(0));
            listaObjetosTipoContactos.setPais(cursorConsultaContactos.getString(1));
            listaObjetosTipoContactos.setNombre(cursorConsultaContactos.getString(2));
            listaObjetosTipoContactos.setTelefono(cursorConsultaContactos.getString(3));
            listaObjetosTipoContactos.setNota(cursorConsultaContactos.getString(4));
            arrayListContactos.add(listaObjetosTipoContactos);
        }

        llenarLista();
        cursorConsultaContactos.close();

    }

    private void llenarLista() {
        arrayListContactosToString = new ArrayList<>();
        for (int i = 0; i < arrayListContactos.size(); i++) {
            arrayListContactosToString.add(arrayListContactos.get(i).getId()+" | "
                    + arrayListContactos.get(i).getNombre() + " | "
                    + arrayListContactos.get(i).getTelefono());
        }
    }
}