package com.tienda.inventario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tienda.util.ConexionDB;

public class MovimientoDAO implements IMovimientoDAO {

    private final Connection conn;

    public MovimientoDAO() {
        conn = ConexionDB.getInstancia().getConexion();
    }

    @Override
    public void registrar(MovimientoInventarioModel m) {
        if (conn == null) {
            System.out.println("No hay conexión a la base de datos. No se puede registrar movimiento.");
            return;
        }
        String sql = "INSERT INTO movimiento (fecha, tipo, cantidad, producto_id) VALUES (?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, m.getFecha());
            ps.setString(2, m.getTipo());
            ps.setInt(3, m.getCantidad());
            ps.setInt(4, m.getProductoId());
            ps.executeUpdate();
            // Ajustar stock del producto según el tipo de movimiento
            int delta;
            if ("SALIDA".equalsIgnoreCase(m.getTipo())) {
                delta = -Math.abs(m.getCantidad());
            } else if ("ENTRADA".equalsIgnoreCase(m.getTipo())) {
                delta = Math.abs(m.getCantidad());
            } else {
                // AJUSTE u otros: usar la cantidad tal cual (permite negativo)
                delta = m.getCantidad();
            }

            // Obtener stock actual
            String sel = "SELECT cantidad FROM producto WHERE id = ?";
            try (PreparedStatement ps2 = conn.prepareStatement(sel)) {
                ps2.setInt(1, m.getProductoId());
                try (ResultSet rs = ps2.executeQuery()) {
                    if (rs.next()) {
                        int actual = rs.getInt("cantidad");
                        int nuevo = actual + delta;
                        if (nuevo < 0) nuevo = 0;
                        String upd = "UPDATE producto SET cantidad = ? WHERE id = ?";
                        try (PreparedStatement ps3 = conn.prepareStatement(upd)) {
                            ps3.setInt(1, nuevo);
                            ps3.setInt(2, m.getProductoId());
                            ps3.executeUpdate();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al registrar movimiento: " + e.getMessage());
        }
    }

    @Override
    public List<MovimientoInventarioModel> listarPorProducto(int idProducto) {
        String sql = "SELECT id, fecha, tipo, cantidad, producto_id FROM movimiento WHERE producto_id = ? ORDER BY fecha DESC";
        List<MovimientoInventarioModel> lista = new ArrayList<>();
        if (conn == null) {
            System.out.println("No hay conexión a la base de datos. Lista de movimientos vacía.");
            return lista;
        }
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idProducto);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MovimientoInventarioModel m = new MovimientoInventarioModel();
                    m.setId(rs.getInt("id"));
                    m.setFecha(rs.getString("fecha"));
                    m.setTipo(rs.getString("tipo"));
                    m.setCantidad(rs.getInt("cantidad"));
                    m.setProductoId(rs.getInt("producto_id"));
                    lista.add(m);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al listar movimientos: " + e.getMessage());
        }
        return lista;
    }
}