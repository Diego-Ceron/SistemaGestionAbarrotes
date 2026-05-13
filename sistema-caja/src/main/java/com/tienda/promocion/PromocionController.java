package com.tienda.promocion;

import java.util.List;

public class PromocionController {

	private final IPromocionDAO promocionDAO;

	public PromocionController() {
		this.promocionDAO = new PromocionDAO();
	}

	public void agregarPromocion(PromocionModel p) {
		promocionDAO.agregar(p);
	}

	public void actualizarPromocion(PromocionModel p) {
		promocionDAO.actualizar(p);
	}

	public void eliminarPromocion(int id) {
		promocionDAO.eliminar(id);
	}

	public PromocionModel buscarPorProducto(int idProducto) {
		return promocionDAO.buscarPorProducto(idProducto);
	}

	public List<PromocionModel> listarPromociones() {
		return promocionDAO.listarTodos();
	}
}
