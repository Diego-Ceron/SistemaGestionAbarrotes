import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ReportCheck {
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
                try (ResultSet rs = st.executeQuery("SELECT IFNULL(SUM(total),0) FROM venta")) {
                    if (rs.next()) System.out.println("Suma total en tabla venta: " + rs.getDouble(1));
                }
                try (ResultSet rs = st.executeQuery("SELECT IFNULL(SUM(vi.cantidad * vi.precio_unitario),0) FROM venta_item vi")) {
                    if (rs.next()) System.out.println("Suma calculada desde venta_item: " + rs.getDouble(1));
                }

                System.out.println("\nDetalle por venta (venta.total vs suma de items):");
                String sql = "SELECT v.id, v.total, IFNULL(SUM(vi.cantidad * vi.precio_unitario),0) as items_total " +
                             "FROM venta v LEFT JOIN venta_item vi ON v.id = vi.venta_id GROUP BY v.id ORDER BY v.id";
                try (ResultSet rs = st.executeQuery(sql)) {
                    while (rs.next()) {
                        System.out.printf("Venta ID=%d  total=%f  items_sum=%f%n", rs.getInt(1), rs.getDouble(2), rs.getDouble(3));
                    }
                }

            }
        } catch (SQLException e) {
            System.out.println("SQL error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
