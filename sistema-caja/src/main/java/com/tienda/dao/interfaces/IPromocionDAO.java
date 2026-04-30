package com.tienda.dao.interfaces;

import com.tienda.model.PromocionModel;

public interface IPromocionDAO {
    void agregar(PromocionModel p);
    void actualizar(PromocionModel p);
    void eliminar(int id);
    PromocionModel buscarPorProducto(int idProducto);
}