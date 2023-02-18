package com.example.pm1e10072.transacciones;

public class Transacciones {
    public static final String tablaContactos = "contactos";

    public static final String id = "id";
    public static final String pais = "pais";
    public static final String nombre = "nombre";
    public static final String telefono = "telefono";
    public static final String nota = "nota";


    // Tablas Crear, Eliminar
    public static final String crearTablaContactos = "CREATE TABLE contactos( id INTEGER PRIMARY KEY AUTOINCREMENT, pais TEXT, nombre TEXT, telefono TEXT, nota TEXT)";

    public static final String eliminarTabla = "DROP TABLE IF EXISTS" + tablaContactos;

    public static final String nombreBaseDatos = "DBSchedule";
}
