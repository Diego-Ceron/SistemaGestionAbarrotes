package com.tienda.cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.tienda.util.ConexionDB;

public class ClienteDAO implements IClienteDAO {

    private final Connection conn;

    public ClienteDAO() {
        conn = ConexionDB.getInstancia().getConexion();
    }

    @Override
    public void registrar(ClienteModel c) {
        String sql = "INSERT INTO cliente (nombre, direccion, telefono, email) VALUES (?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getNombre());
            ps.setString(2, c.getDireccion());
            ps.setString(3, c.getTelefono());
            ps.setString(4, c.getEmail());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    c.setId(keys.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al registrar cliente: " + e.getMessage());
        }
    }

    @Override
    public ClienteModel buscar(int id) {
        String sql = "SELECT id, nombre, direccion, telefono, email, fecha_registro FROM cliente WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ClienteModel c = new ClienteModel();
                    c.setId(rs.getInt("id"));
                    c.setNombre(rs.getString("nombre"));
                    c.setDireccion(rs.getString("direccion"));
                    c.setTelefono(rs.getString("telefono"));
                    c.setEmail(rs.getString("email"));
                    return c;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar cliente: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void actualizar(ClienteModel c) {
        String sql = "UPDATE cliente SET nombre = ?, direccion = ?, telefono = ?, email = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getNombre());
            ps.setString(2, c.getDireccion());
            ps.setString(3, c.getTelefono());
            ps.setString(4, c.getEmail());
            ps.setInt(5, c.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al actualizar cliente: " + e.getMessage());
        }
    }

    @Override
    public void listarFrecuentes() {
        // Por ahora listamos los últimos 10 clientes registrados
        String sql = "SELECT id, nombre, direccion, telefono, email FROM cliente ORDER BY fecha_registro DESC LIMIT 10";
        System.out.println("Listado de clientes (recientes):");
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                System.out.printf("- %d: %s (%s) %n", rs.getInt("id"), rs.getString("nombre"), rs.getString("email"));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar clientes: " + e.getMessage());
        }
    }

}
