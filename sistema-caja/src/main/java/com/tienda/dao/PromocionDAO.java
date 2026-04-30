package com.tienda.dao;

import com.tienda.dao.interfaces.IPromocionDAO;
import com.tienda.model.PromocionModel;
import java.util.List;

public class PromocionDAO implements IPromocionDAO {
    // Aquí irá la conexión a la DB que hizo tu compañero

    @Override
    public void agregar(PromocionModel p) {
        // SQL para insertar
    }

    @Override
    public void actualizar(PromocionModel p) {
        // SQL para update
    }

    @Override
    public void eliminar(int id) {
        // SQL para delete
    }

    @Override
    public PromocionModel buscarPorProducto(int idProducto) {
        // SQL para select
        return null;
    }
}