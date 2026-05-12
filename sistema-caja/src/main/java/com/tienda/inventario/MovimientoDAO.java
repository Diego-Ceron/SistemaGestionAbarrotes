package com.tienda.inventario;

import java.sql.*;
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
        String sql = "INSERT INTO movimiento (fecha, tipo, cantidad, producto_id) VALUES (?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, m.getFecha());
            ps.setString(2, m.getTipo());
            ps.setInt(3, m.getCantidad());
            ps.setInt(4, m.getProductoId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al registrar movimiento: " + e.getMessage());
        }
    }

    @Override
    public List<MovimientoInventarioModel> listarPorProducto(int idProducto) {
        String sql = "SELECT id, fecha, tipo, cantidad, producto_id FROM movimiento WHERE producto_id = ? ORDER BY fecha DESC";
        List<MovimientoInventarioModel> lista = new ArrayList<>();
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