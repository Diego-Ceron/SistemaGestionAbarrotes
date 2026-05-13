package com.tienda.reporte;

import java.util.List;

public class ReporteController {

    private final ReporteDAO reporteDAO = new ReporteDAO();

    public List<ReporteModel> ventasPorPeriodo(String tipo) {
        return reporteDAO.reporteVentas(tipo);
    }

    public List<ReporteModel> reporteInventario() {
        return reporteDAO.reporteInventario();
    }

    public ReporteModel productoPorId(int id) {
        return reporteDAO.productoVendidoPorId(id);
    }

    public List<ReporteModel> productosPorCategoria(String categoria) {
        return reporteDAO.productosVendidosPorCategoria(categoria);
    }
}
