package com.tienda.inventario;

import java.util.List;

public interface IMovimientoDAO {
    void registrar(MovimientoInventarioModel m);
    List<MovimientoInventarioModel> listarPorProducto(int idProducto);
}