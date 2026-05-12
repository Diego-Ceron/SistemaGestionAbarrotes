import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBInspect {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:data/tienda.db";
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver no encontrado: " + e.getMessage());
        }
        try (Connection conn = DriverManager.getConnection(url)) {
            System.out.println("Conectado a: " + url);
            try (Statement st = conn.createStatement()) {
                System.out.println("--- Tablas ---");
                try (ResultSet rs = st.executeQuery("SELECT name FROM sqlite_master WHERE type='table' ORDER BY name")) {
                    while (rs.next()) System.out.println(rs.getString(1));
                }

                System.out.println("\n--- Counts ---");
                printCount(st, "venta");
                printCount(st, "venta_item");
                printCount(st, "producto");

                System.out.println("\n--- Venta samples ---");
                try (ResultSet rs = st.executeQuery("SELECT id, fecha, total FROM venta ORDER BY id DESC LIMIT 5")) {
                    while (rs.next()) System.out.println(rs.getInt(1)+" | "+rs.getString(2)+" | "+rs.getDouble(3));
                }

                System.out.println("\n--- Venta_item samples ---");
                try (ResultSet rs = st.executeQuery("SELECT id, venta_id, producto_id, cantidad, precio_unitario FROM venta_item ORDER BY id DESC LIMIT 5")) {
                    while (rs.next()) System.out.println(rs.getInt(1)+" | "+rs.getInt(2)+" | "+rs.getInt(3)+" | "+rs.getInt(4)+" | "+rs.getDouble(5));
                }

                System.out.println("\n--- Producto samples ---");
                try (ResultSet rs = st.executeQuery("SELECT id, nombre, categoria, cantidad FROM producto ORDER BY id LIMIT 5")) {
                    while (rs.next()) System.out.println(rs.getInt(1)+" | "+rs.getString(2)+" | "+rs.getString(3)+" | "+rs.getInt(4));
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void printCount(Statement st, String table) {
        try (ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM " + table)) {
            if (rs.next()) System.out.println(table + ": " + rs.getInt(1));
        } catch (SQLException e) {
            System.out.println(table + ": (error) " + e.getMessage());
        }
    }
}
