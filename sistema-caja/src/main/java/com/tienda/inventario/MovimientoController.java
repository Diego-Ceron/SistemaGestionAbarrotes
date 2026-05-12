package com.tienda.inventario;

import java.util.List;

import com.tienda.producto.BuscarProductoService;
import com.tienda.producto.IProductoDAO;
import com.tienda.producto.ProductoModel;

public class MovimientoController {

    private final IProductoDAO productoDAO;
    private final IMovimientoDAO movimientoDAO;

    public MovimientoController(IProductoDAO productoDAO, IMovimientoDAO movimientoDAO) {
        this.productoDAO = productoDAO;
        this.movimientoDAO = movimientoDAO;
    }

    // ===================== CRUD PRODUCTOS =====================

    public void agregarProducto(ProductoModel p) {
        productoDAO.agregar(p);
    }

    public void actualizarProducto(ProductoModel p) {
        productoDAO.actualizar(p);
    }

    public void eliminarProducto(int id) {
        productoDAO.eliminar(id);
    }

    public List<ProductoModel> listarProductos() {
        return productoDAO.listarTodos();
    }

    // ===================== STOCK =====================

    public List<ProductoModel> verificarStock(int umbral) {
        return productoDAO.listarTodos().stream()
                .filter(p -> p.getCantidad() <= umbral)
                .toList();
    }

    // ===================== MOVIMIENTOS =====================

    public void registrarMovimiento(MovimientoInventarioModel m) {
        movimientoDAO.registrar(m);
    }

    public List<MovimientoInventarioModel> verHistorialMovimientos(int idProducto) {
        return movimientoDAO.listarPorProducto(idProducto);
    }

    // ===================== BÚSQUEDA =====================

    public BuscarProductoService getBuscador() {
        return productoDAO.getBuscador();
    }
}