package com.tienda.inventario;

public interface IMovimientoDAO {
    void registrar(MovimientoInventarioModel m);
    void listarPorProducto(int idProducto);
}
