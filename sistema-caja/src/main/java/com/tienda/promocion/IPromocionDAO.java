package com.tienda.promocion;

import main.java.com.tienda.promocion.PromocionModel;

public interface IPromocionDAO {
    void agregar(PromocionModel p);
    void actualizar(PromocionModel p);
    void eliminar(int id);
    PromocionModel buscarPorProducto(int idProducto);
}