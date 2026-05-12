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