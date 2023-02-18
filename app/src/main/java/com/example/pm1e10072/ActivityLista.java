package com.example.pm1e10072;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.pm1e10072.tablas.Contactos;
import com.example.pm1e10072.transacciones.Transacciones;

import java.util.ArrayList;

import static android.Manifest.permission.CALL_PHONE;

public class ActivityLista extends AppCompatActivity {

    SQLiteConexion conexion;
    ListView listViewListaContactos;
    ArrayList<Contactos> arrayListContactos;
    ArrayList<String> arrayListContactosToString;
    int posicionDeFilaEnLista;
    ArrayAdapter<String> arrayAdapter;
    String telefonoElementoListaSeleccionado;
    String codigoPais;
    String codigoPaisExtraido;
    String nombreFilaSeleccionado;


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
            telefonoElementoListaSeleccionado = arrayListContactos.get(i).getTelefono();
            nombreFilaSeleccionado = arrayListContactos.get(i).getNombre();
            codigoPais = arrayListContactos.get(i).getPais();
            String positionString = String.valueOf(posicionDeFilaEnLista);
            telefonoElementoListaSeleccionado = arrayListContactos.get(i).getTelefono();

            if (codigoPais.contains("504")) {
                codigoPaisExtraido = "+504";
            } else if (codigoPais.contains("506")) {
                codigoPaisExtraido = "+506";
            } else if (codigoPais.contains("503")) {
                codigoPaisExtraido = "+503";
            } else if (codigoPais.contains("502")) {
                codigoPaisExtraido = "+502";
            }
            Toast.makeText(getApplicationContext(), "Contacto: "+positionString+" seleccionado", Toast.LENGTH_SHORT).show();
            arrayAdapter.notifyDataSetChanged();
        });

        Button btnBorrar = findViewById(R.id.btnBorrar);
        btnBorrar.setOnClickListener(view -> {
            if(posicionDeFilaEnLista > 0) {
                AlertDialog.Builder alertDelete = new AlertDialog.Builder(ActivityLista.this);
                alertDelete.setMessage("Esta seguro que desea eliminar a "+ nombreFilaSeleccionado)
                        .setCancelable(false)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                borrarContacto();
                                finish();
                                Intent intent = new Intent(getApplicationContext(), ActivityLista.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog titulo = alertDelete.create();
                titulo.setTitle("ADVERTENCIA");
                titulo.show();
            } else {
                Toast.makeText(getApplicationContext(), "Seleccione un contacto para poder eliminar", Toast.LENGTH_SHORT).show();
            }
        });

        Button btnAbrirPantallaActualizar = findViewById(R.id.btnActualizar);
        btnAbrirPantallaActualizar.setOnClickListener(view -> obtenerValoresParaEnviarActualizar());

        Button btnLlamar = findViewById(R.id.btnLlamar);
        btnLlamar.setOnClickListener(view -> {
            AlertDialog.Builder alertCall = new AlertDialog.Builder(ActivityLista.this);
            alertCall.setMessage("Desea llamar a "+nombreFilaSeleccionado)
                    .setCancelable(false)
                    .setPositiveButton("Si", (dialog, which) -> {
                        Intent i = new Intent(Intent.ACTION_CALL);
                        i.setData(Uri.parse("tel:"+ codigoPaisExtraido + telefonoElementoListaSeleccionado));

                        if (ContextCompat.checkSelfPermission(getApplicationContext(), CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                            startActivity(i);
                        } else {
                            requestPermissions(new String[]{CALL_PHONE}, 1);
                        }
                    })
                    .setNegativeButton("No", (dialog, which) -> dialog.cancel());
            AlertDialog titulo = alertCall.create();
            titulo.setTitle("ACCION");
            titulo.show();
        });

        Button btnCompartir = findViewById(R.id.btnCompartir);
        btnCompartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentCompartir = new Intent(Intent.ACTION_SEND);
                intentCompartir.setType("text/plain");
                intentCompartir.putExtra(Intent.EXTRA_TEXT, "Contacto: " + nombreFilaSeleccionado + " Tel: " + codigoPaisExtraido + telefonoElementoListaSeleccionado);
                startActivity(Intent.createChooser(intentCompartir, "Compartir con: "));
            }
        });
    }



    private void obtenerValoresParaEnviarActualizar() {
        SQLiteDatabase db = conexion.getWritableDatabase();
        String[] idParametro = {String.valueOf(posicionDeFilaEnLista)};
        String[] valoresObtenidos = {Transacciones.id,
                            Transacciones.pais,
                            Transacciones.nombre,
                            Transacciones.telefono,
                            Transacciones.nota};

        String condicionDeBusqueda = Transacciones.id + "=?";
        try {
            Cursor cursorBuscarContactoPorId = db.query(Transacciones.tablaContactos,
                    valoresObtenidos,
                    condicionDeBusqueda,
                    idParametro,
                    null,
                    null,
                    null);

            cursorBuscarContactoPorId.moveToFirst();

            Intent intentActualizar = new Intent(this, ActivityActualizar.class);

            Bundle valoresAEnviarParaActualizar = new Bundle();
            valoresAEnviarParaActualizar.putInt("id", cursorBuscarContactoPorId.getInt(0));
            valoresAEnviarParaActualizar.putString("pais", cursorBuscarContactoPorId.getString(1));
            valoresAEnviarParaActualizar.putString("nombre", cursorBuscarContactoPorId.getString(2));
            valoresAEnviarParaActualizar.putString("telefono", cursorBuscarContactoPorId.getString(3));
            valoresAEnviarParaActualizar.putString("nota", cursorBuscarContactoPorId.getString(4));

            intentActualizar.putExtras(valoresAEnviarParaActualizar);
            startActivity(intentActualizar);
            finish();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Ocurrio un error", Toast.LENGTH_SHORT).show();

        }
    }
    private void borrarContacto() {
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