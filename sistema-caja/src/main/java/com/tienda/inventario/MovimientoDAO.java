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
        String sql = "INSERT INTO movimiento (fecha, tipo, cantidad, producto_id, nota) VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, m.getFecha());
            ps.setString(2, m.getTipo());
            ps.setInt(3, m.getCantidad());
            ps.setInt(4, m.getProductoId());
            ps.setString(5, m.getNota());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al registrar movimiento: " + e.getMessage());
        }
    }

    @Override
    public void listarPorProducto(int idProducto) {
        String sql = "SELECT id, fecha, tipo, cantidad, producto_id, nota FROM movimiento WHERE producto_id = ? ORDER BY fecha DESC";
        List<MovimientoInventarioModel> lista = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idProducto);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MovimientoInventarioModel m = new MovimientoInventarioModel();
                    m.setFecha(rs.getString("fecha"));
                    m.setTipo(rs.getString("tipo"));
                    m.setCantidad(rs.getInt("cantidad"));
                    m.setProductoId(rs.getInt("producto_id"));
                    m.setNota(rs.getString("nota"));
                    lista.add(m);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al listar movimientos: " + e.getMessage());
        }

        // Imprimir resultados (interfaz actual no devuelve lista)
        System.out.println("Movimientos para producto " + idProducto + ":");
        for (MovimientoInventarioModel m : lista) {
            System.out.printf("- %d | %s | %s | %d | %s%n", m.getId(), m.getFecha(), m.getTipo(), m.getCantidad(), m.getNota());
        }
    }
}
