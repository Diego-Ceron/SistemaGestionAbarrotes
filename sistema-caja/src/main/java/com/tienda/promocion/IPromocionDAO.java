package com.tienda.promocion;

public interface IPromocionDAO {
    void agregar(PromocionModel p);
    void actualizar(PromocionModel p);
    void eliminar(int id);
    PromocionModel buscarPorProducto(int idProducto);
}