package com.tienda;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class App {
    public static void main(String[] args) {

        String url = "jdbc:sqlite:data/tienda.db";

        try {
            // Conectar (crea la BD si no existe)
            Connection conn = DriverManager.getConnection(url);
            System.out.println("Base de datos creada/conectada");

            // Crear tabla de prueba
            Statement stmt = conn.createStatement();
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS productos (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nombre TEXT
                );
            """);

            System.out.println("Tabla 'productos' lista");

            // Mostrar ruta real del archivo
            File dbFile = new File("tienda.db");
            System.out.println("Ruta de la BD: " + dbFile.getAbsolutePath());

            conn.close();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}