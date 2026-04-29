package com.tienda.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {

    private static final String URL = "jdbc:sqlite:data/tienda.db";

    // Instancia única
    private static ConexionDB instancia;

    // Conexión única
    private Connection conexion;

    // Constructor privado
    private ConexionDB() {
        try {
            conexion = DriverManager.getConnection(URL);
            System.out.println("Conexión establecida");
        } catch (SQLException e) {
            System.out.println("Error al conectar: " + e.getMessage());
        }
    }

    // Método para obtener la instancia
    public static ConexionDB getInstancia() {
        if (instancia == null) {
            instancia = new ConexionDB();
        }
        return instancia;
    }

    // Método para obtener la conexión
    public Connection getConexion() {
        return conexion;
    }

    public void cerrarConexion() {
    try {
        if (conexion != null) {
            conexion.close();
            System.out.println("Conexión cerrada");
        }
    } catch (SQLException e) {
        System.out.println("Error al cerrar: " + e.getMessage());
    }
}
}