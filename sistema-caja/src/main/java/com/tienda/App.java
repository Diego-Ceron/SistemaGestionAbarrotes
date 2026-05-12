package com.tienda;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class App {
    public static void main(String[] args) {

        String url = "jdbc:sqlite:data/tienda.db";
        Path schemaPath = Path.of("data", "schema.sql");

        try (Connection conn = DriverManager.getConnection(url)) {
            System.out.println("Base de datos creada/conectada");

            if (Files.exists(schemaPath)) {
                String sql = Files.readString(schemaPath);
                try (Statement stmt = conn.createStatement()) {
                    for (String s : sql.split(";")) {
                        String trimmed = s.trim();
                        if (!trimmed.isEmpty()) {
                            stmt.execute(trimmed);
                        }
                    }
                }
                System.out.println("Esquema inicializado desde: " + schemaPath.toString());
            } else {
                System.out.println("schema.sql no encontrado en: " + schemaPath.toString());
            }

            File dbFile = new File("data/tienda.db");
            System.out.println("Ruta de la BD: " + dbFile.getAbsolutePath());

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        // Lanzar la vista de consola principal
        try {
            com.tienda.ui.MainView.main(args);
        } catch (Exception e) {
            System.out.println("Error al iniciar MainView: " + e.getMessage());
        }
    }
}