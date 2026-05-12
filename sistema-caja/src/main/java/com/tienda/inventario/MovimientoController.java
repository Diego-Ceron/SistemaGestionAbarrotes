package com.tienda.inventario;

import java.util.List;

public class MovimientoController {

    private final IMovimientoDAO movimientoDAO;

    public MovimientoController(IMovimientoDAO movimientoDAO) {
        this.movimientoDAO = movimientoDAO;
    }

    // Registrar movimiento (entrada o salida)
    public void registrarMovimiento(MovimientoInventarioModel m) {
        movimientoDAO.registrar(m);
    }

    // Listar historial de movimientos de un producto
    public List<MovimientoInventarioModel> listarPorProducto(int idProducto) {
        return movimientoDAO.listarPorProducto(idProducto);
    }
}