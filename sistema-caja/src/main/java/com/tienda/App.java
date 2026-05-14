package com.tienda;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.tienda.ui.JavaFXApp;

import javafx.application.Application;

public class App {
    public static void main(String[] args) {

        String url = "jdbc:sqlite:data/tienda.db";
        Path schemaPath = Path.of("data", "schema.sql");

        try (Connection conn = DriverManager.getConnection(url)) {
            System.out.println("Base de datos creada/conectada");

            if (Files.exists(schemaPath)) {
                String sql = Files.readString(schemaPath);
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute(sql);
                }
                System.out.println("Esquema inicializado desde: " + schemaPath.toString());
            } else {
                System.out.println("schema.sql no encontrado en: " + schemaPath.toString());
            }

            ensureVentaTarjetaColumn(conn);

            File dbFile = new File("data/tienda.db");
            System.out.println("Ruta de la BD: " + dbFile.getAbsolutePath());

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }

        if (args.length > 0 && args[0].equalsIgnoreCase("cli")) {
            try {
                com.tienda.ui.MainView.main(args);
            } catch (Exception e) {
                System.out.println("Error al iniciar MainView: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            Application.launch(JavaFXApp.class, args);
        }
    }

    private static void ensureVentaTarjetaColumn(Connection conn) throws SQLException {
        boolean exists = false;
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("PRAGMA table_info('venta')")) {
            while (rs.next()) {
                if ("numero_tarjeta".equalsIgnoreCase(rs.getString("name"))) {
                    exists = true;
                    break;
                }
            }
        }
        if (!exists) {
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate("ALTER TABLE venta ADD COLUMN numero_tarjeta TEXT");
                System.out.println("Columna numero_tarjeta agregada a la tabla venta.");
            }
        }
    }

    private static void seedSampleData(Connection conn) throws SQLException {
        try (Statement st = conn.createStatement()) {
            st.executeUpdate("INSERT INTO cliente (nombre, direccion, telefono, email) " +
                    "SELECT 'Juan Pérez', 'Av. Siempre Viva 742', '5512345678', 'juan.perez@example.com' " +
                    "WHERE NOT EXISTS (SELECT 1 FROM cliente WHERE telefono='5512345678')");
            st.executeUpdate("UPDATE cliente SET nombre='Juan Pérez', direccion='Av. Siempre Viva 742', email='juan.perez@example.com' " +
                    "WHERE telefono='5512345678'");

            st.executeUpdate("INSERT INTO cliente (nombre, direccion, telefono, email) " +
                    "SELECT 'Diego López', 'Calle Falsa 123', '5523456789', 'diego.lopez@example.com' " +
                    "WHERE NOT EXISTS (SELECT 1 FROM cliente WHERE telefono='5523456789')");
            st.executeUpdate("UPDATE cliente SET nombre='Diego López', direccion='Calle Falsa 123', email='diego.lopez@example.com' " +
                    "WHERE telefono='5523456789'");

            st.executeUpdate("INSERT INTO producto (nombre, precio, cantidad, vencimiento, descripcion, categoria, proveedor, codigo) " +
                    "SELECT 'Coca Cola', 10.0, 20, NULL, 'Refresco cola', 'Bebidas', 'Coca Cola Co', 'CC001' " +
                    "WHERE NOT EXISTS (SELECT 1 FROM producto WHERE codigo IN ('CC001','CC-001') OR nombre='Coca Cola')");
            st.executeUpdate("UPDATE producto SET cantidad=20, precio=10.0, descripcion='Refresco cola', categoria='Bebidas', proveedor='Coca Cola Co', codigo='CC001' " +
                    "WHERE codigo IN ('CC001','CC-001') OR nombre='Coca Cola'");

            st.executeUpdate("INSERT INTO producto (nombre, precio, cantidad, vencimiento, descripcion, categoria, proveedor, codigo) " +
                    "SELECT 'Pepsi', 9.5, 50, NULL, 'Refresco cola alternativo', 'Bebidas', 'PepsiCo', 'PEP001' " +
                    "WHERE NOT EXISTS (SELECT 1 FROM producto WHERE codigo='PEP001' OR nombre='Pepsi')");
            st.executeUpdate("UPDATE producto SET cantidad=50, precio=9.5, descripcion='Refresco cola alternativo', categoria='Bebidas', proveedor='PepsiCo', codigo='PEP001' " +
                    "WHERE codigo='PEP001' OR nombre='Pepsi'");

            st.executeUpdate("INSERT INTO producto (nombre, precio, cantidad, vencimiento, descripcion, categoria, proveedor, codigo) " +
                    "SELECT 'Pepsi', 9.5, 20, NULL, 'Refresco cola alternativo con stock 20', 'Bebidas', 'PepsiCo', 'PEP002' " +
                    "WHERE NOT EXISTS (SELECT 1 FROM producto WHERE codigo='PEP002')");
            st.executeUpdate("UPDATE producto SET cantidad=20, precio=9.5, descripcion='Refresco cola alternativo con stock 20', categoria='Bebidas', proveedor='PepsiCo' " +
                    "WHERE codigo='PEP002'");

            st.executeUpdate("INSERT INTO producto (nombre, precio, cantidad, vencimiento, descripcion, categoria, proveedor, codigo) " +
                    "SELECT 'Sabritas', 10.0, 20, NULL, 'Botana salada', 'Botanas', 'Pepsico Sabritas', 'SAB001' " +
                    "WHERE NOT EXISTS (SELECT 1 FROM producto WHERE codigo='SAB001' OR nombre='Sabritas')");
            st.executeUpdate("UPDATE producto SET cantidad=20, precio=10.0, descripcion='Botana salada', categoria='Botanas', proveedor='Pepsico Sabritas', codigo='SAB001' " +
                    "WHERE codigo='SAB001' OR nombre='Sabritas'");

            st.executeUpdate("UPDATE promocion SET porcentajeDescuento=10.0, fecha_inicio=date('now','-30 day'), fecha_fin=date('now','+30 day'), activo=1 " +
                    "WHERE descripcion='10% descuento bebidas'");
            st.executeUpdate("INSERT INTO promocion (descripcion, porcentajeDescuento, fecha_inicio, fecha_fin, activo) " +
                    "SELECT '10% descuento bebidas', 10.0, date('now','-30 day'), date('now','+30 day'), 1 " +
                    "WHERE NOT EXISTS (SELECT 1 FROM promocion WHERE descripcion='10% descuento bebidas')");

            st.executeUpdate("UPDATE promocion SET porcentajeDescuento=5.0, fecha_inicio=date('now','-30 day'), fecha_fin=date('now','+30 day'), activo=1 " +
                    "WHERE descripcion='5% descuento clientes frecuentes'");
            st.executeUpdate("INSERT INTO promocion (descripcion, porcentajeDescuento, fecha_inicio, fecha_fin, activo) " +
                    "SELECT '5% descuento clientes frecuentes', 5.0, date('now','-30 day'), date('now','+30 day'), 1 " +
                    "WHERE NOT EXISTS (SELECT 1 FROM promocion WHERE descripcion='5% descuento clientes frecuentes')");

            st.executeUpdate("INSERT OR IGNORE INTO promocion_producto (promocion_id, producto_id) " +
                    "SELECT p.id, prod.id FROM promocion p JOIN producto prod ON prod.codigo='CC001' " +
                    "WHERE p.descripcion='10% descuento bebidas'");
            st.executeUpdate("INSERT OR IGNORE INTO promocion_producto (promocion_id, producto_id) " +
                    "SELECT p.id, prod.id FROM promocion p JOIN producto prod ON prod.codigo='PEP001' " +
                    "WHERE p.descripcion='10% descuento bebidas'");

            // Insertar una venta de prueba para hacer al cliente frecuente
            st.executeUpdate("INSERT INTO venta (cliente_id, total, metodo_pago) " +
                    "SELECT c.id, 20.0, 'Efectivo' FROM cliente c WHERE c.telefono='5512345678' " +
                    "AND NOT EXISTS (SELECT 1 FROM venta WHERE cliente_id = c.id)");
            // Obtener el id de la venta insertada
            try (ResultSet rs = st.executeQuery("SELECT last_insert_rowid() as venta_id")) {
                if (rs.next()) {
                    int ventaId = rs.getInt("venta_id");
                    st.executeUpdate("INSERT INTO venta_item (venta_id, producto_id, cantidad, precio_unitario) " +
                            "SELECT " + ventaId + ", p.id, 1, 10.0 FROM producto p WHERE p.codigo='CC001'");
                }
            }
        }
    }
}
