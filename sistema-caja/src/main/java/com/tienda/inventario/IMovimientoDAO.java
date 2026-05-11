package com.tienda.inventario;

import main.java.com.tienda.inventario.MovimientoInventarioModel;

public interface IMovimientoDAO {
    void registrar(MovimientoInventarioModel m);
    void listarPorProducto(int idProducto);
}
