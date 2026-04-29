package com.tienda.producto;

import java.sql.Connection;

import com.tienda.util.ConexionDB;

public class ProductoDAO {

    private Connection conn;

    public ProductoDAO() {
        conn = ConexionDB.getInstancia().getConexion();
    }
}