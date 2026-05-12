package com.tienda.promocion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.tienda.util.ConexionDB;

public class PromocionDAO implements IPromocionDAO {

    private final Connection conn;

    public PromocionDAO() {
        conn = ConexionDB.getInstancia().getConexion();
    }

    @Override
    public void agregar(PromocionModel p) {
        if (conn == null) {
            System.out.println("No hay conexión a la base de datos. No se puede agregar promoción.");
            return;
        }
        String sql = "INSERT INTO promocion (descripcion, porcentajeDescuento, fecha_inicio, fecha_fin, activo) VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getDescripcion());
            ps.setDouble(2, p.getPorcentajeDescuento());
            ps.setString(3, null);
            ps.setString(4, null);
            ps.setInt(5, 1);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    p.setId(keys.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al agregar promocion: " + e.getMessage());
        }
    }

    @Override
    public void actualizar(PromocionModel p) {
        if (conn == null) {
            System.out.println("No hay conexión a la base de datos. No se puede actualizar promoción.");
            return;
        }
        String sql = "UPDATE promocion SET descripcion = ?, porcentajeDescuento = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getDescripcion());
            ps.setDouble(2, p.getPorcentajeDescuento());
            ps.setInt(3, p.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al actualizar promocion: " + e.getMessage());
        }
    }

    @Override
    public void eliminar(int id) {
        if (conn == null) {
            System.out.println("No hay conexión a la base de datos. No se puede eliminar promoción.");
            return;
        }
        String sql = "DELETE FROM promocion WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al eliminar promocion: " + e.getMessage());
        }
    }

    @Override
    public PromocionModel buscarPorProducto(int idProducto) {
        if (conn == null) {
            System.out.println("No hay conexión a la base de datos. Búsqueda de promoción vacía.");
            return null;
        }
        String sql = "SELECT pr.id, pr.descripcion, pr.porcentajeDescuento FROM promocion pr JOIN promocion_producto pp ON pr.id = pp.promocion_id WHERE pp.producto_id = ? LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idProducto);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    PromocionModel p = new PromocionModel();
                    p.setId(rs.getInt("id"));
                    p.setDescripcion(rs.getString("descripcion"));
                    p.setPorcentajeDescuento(rs.getDouble("porcentajeDescuento"));
                    return p;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar promocion por producto: " + e.getMessage());
        }
        return null;
    }

    @Override
    public java.util.List<PromocionModel> listarTodos() {
        String sql = "SELECT id, descripcion, porcentajeDescuento FROM promocion WHERE activo = 1";
        java.util.List<PromocionModel> lista = new java.util.ArrayList<>();
        if (conn == null) {
            System.out.println("No hay conexión a la base de datos. Lista de promociones vacía.");
            return lista;
        }
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                PromocionModel p = new PromocionModel();
                p.setId(rs.getInt("id"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setPorcentajeDescuento(rs.getDouble("porcentajeDescuento"));
                lista.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar promociones: " + e.getMessage());
        }
        return lista;
    }
}