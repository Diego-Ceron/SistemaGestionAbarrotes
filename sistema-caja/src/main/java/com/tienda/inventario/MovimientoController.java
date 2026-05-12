package com.tienda.inventario;

import com.tienda.producto.ProductoModel;

import java.util.List;

public class MovimientoController {

    private final IMovimientoDAO movimientoDAO;

    // Constructor
    public MovimientoController(IMovimientoDAO movimientoDAO) {
        this.movimientoDAO = movimientoDAO;
    }

    // Registrar entrada de producto
    public void registrarEntrada(ProductoModel producto, int cantidad) {

        MovimientoInventarioModel movimiento = new MovimientoInventarioModel();

        movimiento.setId(producto);
        movimiento.setCantidad(cantidad);
        movimiento.setTipo("ENTRADA");

        movimientoDAO.registrarMovimiento(movimiento);
    }

    // Registrar salida de producto
    public void registrarSalida(ProductoModel producto, int cantidad) {

        MovimientoModel movimiento = new MovimientoModel();

        movimiento.setProducto(producto);
        movimiento.setCantidad(cantidad);
        movimiento.setTipoMovimiento("SALIDA");

        movimientoDAO.registrarMovimiento(movimiento);
    }

    // Obtener historial de movimientos
    public List<MovimientoModel> obtenerMovimientos() {
        return movimientoDAO.obtenerMovimientos();
    }

    // Buscar movimiento por ID
    public MovimientoModel buscarMovimiento(int idMovimiento) {
        return movimientoDAO.buscarMovimiento(idMovimiento);
    }
}